<template>
  <div class="bug-create-page">
    <el-card shadow="always" class="mb-4">
      <template #header>
        <div class="flex justify-between items-center">
          <h2 class="text-xl font-bold">创建缺陷</h2>
          <el-button @click="goBack">返回</el-button>
        </div>
      </template>
      
      <el-form :model="form" label-width="120px" ref="bugFormRef">
        <el-form-item label="缺陷标题" prop="bugTitle">
          <el-input v-model="form.bugTitle" placeholder="请输入缺陷标题" />
        </el-form-item>
        
        <el-form-item label="严重程度" prop="severity">
          <el-select v-model="form.severity" placeholder="请选择严重程度" style="width: 100%">
            <el-option label="阻塞" value="阻塞" />
            <el-option label="严重" value="严重" />
            <el-option label="一般" value="一般" />
            <el-option label="轻微" value="轻微" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="优先级" prop="level">
          <el-select v-model="form.level" placeholder="请选择优先级" style="width: 100%">
            <el-option label="1" value="1" />
            <el-option label="2" value="2" />
            <el-option label="3" value="3" />
            <el-option label="4" value="4" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="缺陷类型" prop="bugType">
          <el-select v-model="form.bugType" placeholder="请选择缺陷类型" style="width: 100%">
            <el-option label="功能问题" value="功能问题" />
            <el-option label="接口问题" value="接口问题" />
            <el-option label="性能问题" value="性能问题" />
            <el-option label="安全问题" value="安全问题" />
            <el-option label="兼容性问题" value="兼容性问题" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
            <el-option label="已创建" value="已创建" />
            <el-option label="待修复" value="待修复" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="指派给" prop="assignedTo">
          <el-input v-model="form.assignedTo" placeholder="请输入指派人" />
        </el-form-item>
        
        <el-form-item label="关联测试计划" prop="planId" v-if="!form.planId">
          <el-select v-model="form.planId" placeholder="请选择关联的测试计划" style="width: 100%">
            <el-option 
              v-for="plan in testPlans" 
              :key="plan.planId" 
              :label="plan.planName" 
              :value="plan.planId" 
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="关联测试用例" prop="caseId" v-if="form.caseId">
          <el-input v-model="form.caseId" disabled />
          <span class="text-gray-500 ml-2">{{ relatedCaseTitle }}</span>
        </el-form-item>
        
        <el-form-item label="重现步骤" prop="reproductionSteps">
          <el-input 
            v-model="form.reproductionSteps" 
            type="textarea" 
            rows="5" 
            placeholder="请输入重现步骤，包括：【复现步骤】【实际结果】【期望结果】" 
          />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="submitForm">提交</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { BugApi } from '@/api/bugService'
import { testplanApi } from '@/api/testplanService'
import { useProjectStore } from '@/store/projectStore'

const route = useRoute()
const router = useRouter()

// 使用项目Store
const projectStore = useProjectStore()
// 确保初始化项目状态
projectStore.initProjectState()

// 计算当前项目ID
const currentProjectId = computed(() => projectStore.getCurrentProjectId())

// 表单数据
const form = reactive({
  bugTitle: '',
  severity: '一般',
  level: '2',
  bugType: '功能问题',
  status: '已创建',
  assignedTo: '',
  planId: null,
  caseId: null,
  reproductionSteps: '',
  projectId: currentProjectId.value || ''
})

// 测试计划列表
const testPlans = ref([])
// 关联的测试用例标题
const relatedCaseTitle = ref('')

// 获取测试计划列表
const fetchTestPlans = async () => {
  try {
    const res = await TestPlanApi.getTestplans({
      projectId: currentProjectId.value
    })
    testPlans.value = res.data.data || []
  } catch (error) {
    console.error('获取测试计划列表失败:', error)
  }
}

// 提交表单
const submitForm = async () => {
  try {
    // 确保projectId有值
    form.projectId = currentProjectId.value || ''
    
    await BugApi.createBug(form)
    ElMessage.success('缺陷创建成功')
    router.push('/bug')
  } catch (error) {
    ElMessage.error(error.message || '创建缺陷失败')
    console.error('创建缺陷失败:', error)
  }
}

// 重置表单
const resetForm = () => {
  Object.assign(form, {
    bugTitle: '',
    severity: '一般',
    level: '2',
    bugType: '功能问题',
    status: '已创建',
    assignedTo: '',
    planId: null,
    reproductionSteps: '',
    projectId: currentProjectId.value || ''
  })
  
  // 保留从URL参数获取的caseId
  const caseId = route.query.caseId
  if (caseId) {
    form.caseId = caseId
  }
}

// 返回上一页
const goBack = () => {
  router.go(-1)
}

onMounted(async () => {
  // 获取测试计划列表
  await fetchTestPlans()
  
  // 从URL参数获取测试用例信息
  const { caseId, caseTitle, failureDesc } = route.query
  
  if (caseId) {
    form.caseId = caseId
    relatedCaseTitle.value = caseTitle || ''
    
    // 如果有失败描述，填入重现步骤
    if (failureDesc) {
      form.reproductionSteps = `【复现步骤】\n执行测试用例 ${caseTitle || caseId} 时发现问题\n\n【实际结果】\n${failureDesc}\n\n【期望结果】\n`
    }
    
    // 如果有测试用例标题，自动填入缺陷标题
    if (caseTitle) {
      form.bugTitle = `测试用例 "${caseTitle}" 执行失败`
    }
  }
})
</script>

<style scoped>
.bug-create-page {
  padding: 20px;
}
</style>
