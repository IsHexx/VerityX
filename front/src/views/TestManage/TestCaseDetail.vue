<template>
  <div class="test-case-detail">
    <el-card shadow="always" class="mb-4">
      <template #header>
        <div class="flex justify-between items-center">
          <h2 class="text-xl font-bold">{{ testCase.caseTitle }}</h2>
          <div>
            <el-button type="primary" @click="handleEditCase">编辑用例</el-button>
            <el-button @click="goBack">返回</el-button>
          </div>
        </div>
      </template>
      
      <div class="grid grid-cols-2 gap-4">
        <div>
          <p><strong>用例ID:</strong> {{ testCase.caseId }}</p>
          <p><strong>用例类型:</strong> {{ testCase.caseType }}</p>
          <p><strong>重要级别:</strong> {{ testCase.importanceLevel }}</p>
          <p><strong>状态:</strong> {{ testCase.caseStatus }}</p>
        </div>
        <div>
          <p><strong>前置条件:</strong> {{ testCase.precondition }}</p>
          <p><strong>创建时间:</strong> {{ formatDateTime(testCase.createdAt) }}</p>
          <p><strong>更新时间:</strong> {{ formatDateTime(testCase.updatedAt) }}</p>
        </div>
      </div>
      
      <div class="mt-4">
        <p><strong>描述:</strong></p>
        <p>{{ testCase.caseDesc }}</p>
      </div>
      
      <div class="mt-4">
        <p><strong>测试步骤:</strong></p>
        <p>{{ testCase.testSteps }}</p>
      </div>
      
      <div class="mt-4">
        <p><strong>预期结果:</strong></p>
        <p>{{ testCase.expectedResult }}</p>
      </div>
    </el-card>
    
    <!-- 关联缺陷列表 -->
    <el-card shadow="always" class="mb-4">
      <template #header>
        <div class="flex justify-between items-center">
          <h3 class="text-lg font-bold">关联缺陷</h3>
          <el-button type="primary" @click="createBug">创建缺陷</el-button>
        </div>
      </template>
      
      <el-table :data="relatedBugs" v-loading="bugsLoading" border stripe>
        <el-table-column prop="bugId" label="缺陷ID" width="80" />
        <el-table-column prop="bugTitle" label="缺陷标题" />
        <el-table-column prop="severity" label="严重程度" width="100" />
        <el-table-column prop="level" label="优先级" width="100" />
        <el-table-column prop="status" label="状态" width="100" />
        <el-table-column prop="assignedTo" label="指派给" width="100" />
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="viewBug(row)">查看</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="flex justify-center mt-4" v-if="relatedBugs.length === 0">
        <el-empty description="暂无关联的缺陷" />
      </div>
    </el-card>
    
    <!-- 编辑测试用例对话框 -->
    <el-dialog
      v-model="editDialogVisible"
      title="编辑测试用例"
      width="50%"
    >
      <el-form :model="form" label-width="100px" ref="testCaseFormRef">
        <el-form-item label="用例标题" prop="caseTitle">
          <el-input v-model="form.caseTitle" />
        </el-form-item>
        <el-form-item label="用例类型" prop="caseType">
          <el-select v-model="form.caseType" placeholder="请选择用例类型" style="width: 100%">
            <el-option label="功能测试" value="功能测试" />
            <el-option label="性能测试" value="性能测试" />
            <el-option label="安全测试" value="安全测试" />
            <el-option label="接口测试" value="接口测试" />
            <el-option label="UI测试" value="UI测试" />
          </el-select>
        </el-form-item>
        <el-form-item label="重要级别" prop="importanceLevel">
          <el-select v-model="form.importanceLevel" placeholder="请选择重要级别" style="width: 100%">
            <el-option label="高" value="高" />
            <el-option label="中" value="中" />
            <el-option label="低" value="低" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="caseStatus">
          <el-select v-model="form.caseStatus" placeholder="请选择状态" style="width: 100%">
            <el-option label="草稿" value="草稿" />
            <el-option label="待评审" value="待评审" />
            <el-option label="已评审" value="已评审" />
            <el-option label="已废弃" value="已废弃" />
          </el-select>
        </el-form-item>
        <el-form-item label="前置条件" prop="precondition">
          <el-input v-model="form.precondition" type="textarea" rows="2" />
        </el-form-item>
        <el-form-item label="描述" prop="caseDesc">
          <el-input v-model="form.caseDesc" type="textarea" rows="3" />
        </el-form-item>
        <el-form-item label="测试步骤" prop="testSteps">
          <el-input v-model="form.testSteps" type="textarea" rows="4" />
        </el-form-item>
        <el-form-item label="预期结果" prop="expectedResult">
          <el-input v-model="form.expectedResult" type="textarea" rows="3" />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleUpdateCase">确认</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { testcaseApi } from '@/api/testcaseService'
import { BugApi } from '@/api/bugService'

const route = useRoute()
const router = useRouter()
const caseId = route.params.id

// 测试用例数据
const testCase = ref({})
const loading = ref(false)

// 缺陷数据
const relatedBugs = ref([])
const bugsLoading = ref(false)

// 编辑测试用例对话框
const editDialogVisible = ref(false)
const testCaseFormRef = ref(null)
const form = reactive({
  caseId: '',
  caseTitle: '',
  caseType: '',
  importanceLevel: '',
  caseStatus: '',
  precondition: '',
  caseDesc: '',
  testSteps: '',
  expectedResult: '',
  projectId: ''
})

// 获取测试用例详情
const fetchTestCase = async () => {
  loading.value = true
  try {
    const res = await testcaseApi.getTestcase(caseId)
    testCase.value = res.data
    loading.value = false
  } catch (error) {
    ElMessage.error('获取测试用例详情失败')
    console.error('获取测试用例详情失败:', error)
    loading.value = false
  }
}

// 获取关联的缺陷
const fetchRelatedBugs = async () => {
  bugsLoading.value = true
  try {
    const res = await BugApi.getBugsByCaseId(caseId)
    relatedBugs.value = res.data
    bugsLoading.value = false
  } catch (error) {
    ElMessage.error('获取关联缺陷失败')
    console.error('获取关联缺陷失败:', error)
    bugsLoading.value = false
  }
}

// 查看缺陷详情
const viewBug = (row) => {
  router.push(`/bug/${row.bugId}`)
}

// 创建缺陷
const createBug = () => {
  router.push({
    path: '/bug/create',
    query: { 
      caseId: caseId,
      caseTitle: testCase.value.caseTitle
    }
  })
}

// 编辑测试用例
const handleEditCase = () => {
  Object.assign(form, {
    caseId: testCase.value.caseId,
    caseTitle: testCase.value.caseTitle,
    caseType: testCase.value.caseType,
    importanceLevel: testCase.value.importanceLevel,
    caseStatus: testCase.value.caseStatus,
    precondition: testCase.value.precondition,
    caseDesc: testCase.value.caseDesc,
    testSteps: testCase.value.testSteps,
    expectedResult: testCase.value.expectedResult,
    projectId: testCase.value.projectId
  })
  editDialogVisible.value = true
}

// 更新测试用例
const handleUpdateCase = async () => {
  try {
    await testcaseApi.updateTestcase(form.caseId, form)
    ElMessage.success('更新测试用例成功')
    editDialogVisible.value = false
    fetchTestCase()
  } catch (error) {
    ElMessage.error('更新测试用例失败')
    console.error('更新测试用例失败:', error)
  }
}

// 返回上一页
const goBack = () => {
  router.push('/testcase')
}

// 格式化日期时间
const formatDateTime = (dateTime) => {
  if (!dateTime) return ''
  const date = new Date(dateTime)
  return date.toLocaleString()
}

onMounted(() => {
  fetchTestCase()
  fetchRelatedBugs()
})
</script>

<style scoped>
.test-case-detail {
  padding: 20px;
}
</style>
