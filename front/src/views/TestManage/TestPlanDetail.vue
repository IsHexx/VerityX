<template>
  <div class="test-plan-detail">
    <el-card shadow="always" class="mb-4">
      <template #header>
        <div class="flex justify-between items-center">
          <h2 class="text-xl font-bold">{{ testPlan.planName }}</h2>
          <div>
            <el-button type="primary" @click="handleEditPlan">编辑计划</el-button>
            <el-button @click="goBack">返回</el-button>
          </div>
        </div>
      </template>

      <div class="grid grid-cols-2 gap-4">
        <div>
          <p><strong>计划版本:</strong> {{ testPlan.planVersion }}</p>
          <p><strong>状态:</strong> {{ testPlan.status }}</p>
          <p><strong>开始日期:</strong> {{ testPlan.startDate }}</p>
          <p><strong>结束日期:</strong> {{ testPlan.endDate }}</p>
        </div>
        <div>
          <p><strong>参与人员:</strong> {{ testPlan.participants }}</p>
          <p><strong>创建时间:</strong> {{ formatDateTime(testPlan.createdAt) }}</p>
          <p><strong>更新时间:</strong> {{ formatDateTime(testPlan.updatedAt) }}</p>
        </div>
      </div>

      <div class="mt-4">
        <p><strong>描述:</strong></p>
        <p>{{ testPlan.description }}</p>
      </div>
    </el-card>

    <!-- 测试用例关联部分 -->
    <el-card shadow="always" class="mb-4">
      <template #header>
        <div class="flex justify-between items-center">
          <h3 class="text-lg font-bold">关联测试用例</h3>
          <el-button type="primary" @click="showAssociateDialog">关联测试用例</el-button>
        </div>
      </template>

      <el-table :data="associatedTestCases" v-loading="loading" border stripe>
        <el-table-column prop="caseId" label="用例ID" width="80" />
        <el-table-column prop="caseTitle" label="用例标题" />
        <el-table-column prop="importanceLevel" label="重要级别" width="100" />
        <el-table-column prop="caseType" label="用例类型" width="100" />
        <el-table-column prop="caseStatus" label="状态" width="100" />
        <el-table-column label="操作" width="220">
          <template #default="{ row }">
            <el-button type="danger" size="small" @click="handleRemoveCase(row)">移除</el-button>
            <el-button type="primary" size="small" @click="viewTestCase(row)">查看</el-button>
            <el-button type="warning" size="small" @click="createBugFromCase(row)">创建缺陷</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="flex justify-center mt-4" v-if="associatedTestCases.length === 0">
        <el-empty description="暂无关联的测试用例" />
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

    <!-- 关联测试用例对话框 -->
    <el-dialog
      v-model="associateDialogVisible"
      title="关联测试用例"
      width="70%"
    >
      <div class="mb-4">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索测试用例"
          clearable
          @keyup.enter="searchTestCases"
        >
          <template #append>
            <el-button @click="searchTestCases">搜索</el-button>
          </template>
        </el-input>
      </div>

      <el-table
        :data="testCases"
        v-loading="searchLoading"
        border
        stripe
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="caseId" label="用例ID" width="80" />
        <el-table-column prop="caseTitle" label="用例标题" />
        <el-table-column prop="importanceLevel" label="重要级别" width="100" />
        <el-table-column prop="caseType" label="用例类型" width="100" />
        <el-table-column prop="caseStatus" label="状态" width="100" />
      </el-table>

      <div class="flex justify-center mt-4" v-if="testCases.length === 0 && !searchLoading">
        <el-empty description="未找到测试用例" />
      </div>

      <el-pagination
        v-if="testCases.length > 0"
        class="mt-4"
        layout="total, sizes, prev, pager, next"
        :total="total"
        :page-size="pagination.pageSize"
        :current-page="pagination.page"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="associateDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleAssociateCases">确认关联</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 编辑测试计划对话框 -->
    <el-dialog
      v-model="editDialogVisible"
      title="编辑测试计划"
      width="50%"
    >
      <el-form :model="form" label-width="100px" ref="testplanFormRef">
        <el-form-item label="计划名称" prop="planName">
          <el-input v-model="form.planName" />
        </el-form-item>
        <el-form-item label="计划版本" prop="planVersion">
          <el-input v-model="form.planVersion" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
            <el-option label="未开始" value="未开始" />
            <el-option label="进行中" value="进行中" />
            <el-option label="已完成" value="已完成" />
          </el-select>
        </el-form-item>
        <el-form-item label="开始日期" prop="startDate">
          <el-date-picker v-model="form.startDate" type="date" style="width: 100%" />
        </el-form-item>
        <el-form-item label="结束日期" prop="endDate">
          <el-date-picker v-model="form.endDate" type="date" style="width: 100%" />
        </el-form-item>
        <el-form-item label="参与人员" prop="participants">
          <el-input v-model="form.participants" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" rows="4" />
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleUpdatePlan">确认</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { testplanApi } from '@/api/testplanService'
import { testcaseApi } from '@/api/testcaseService'
import { BugApi } from '@/api/bugService'
import { http } from '@/utils/request'

// 如果API方法不存在，添加临时方法
if (typeof testplanApi.getAssociatedTestCases !== 'function') {
  testplanApi.getAssociatedTestCases = (planId) => {
    console.log('使用临时的getAssociatedTestCases方法')
    return http.get(`/api/testplans/${planId}/cases`)
  }
}

if (typeof testplanApi.associateTestCases !== 'function') {
  testplanApi.associateTestCases = (planId, data) => {
    console.log('使用临时的associateTestCases方法')
    return http.post(`/api/testplans/${planId}/cases`, data)
  }
}

if (typeof testplanApi.removeTestCase !== 'function') {
  testplanApi.removeTestCase = (planId, caseId) => {
    console.log('使用临时的removeTestCase方法')
    return http.delete(`/api/testplans/${planId}/cases/${caseId}`)
  }
}

if (typeof BugApi.getBugsByPlanId !== 'function') {
  BugApi.getBugsByPlanId = (planId) => {
    console.log('使用临时的getBugsByPlanId方法')
    return http.get(`/api/bugs/plan/${planId}`)
  }
}

const route = useRoute()
const router = useRouter()
const planId = route.params.id

// 测试计划数据
const testPlan = ref({})
const loading = ref(false)
const associatedTestCases = ref([])

// 缺陷数据
const relatedBugs = ref([])
const bugsLoading = ref(false)

// 关联测试用例对话框
const associateDialogVisible = ref(false)
const searchKeyword = ref('')
const testCases = ref([])
const searchLoading = ref(false)
const selectedCases = ref([])
const total = ref(0)
const pagination = reactive({
  page: 1,
  pageSize: 10
})

// 编辑测试计划对话框
const editDialogVisible = ref(false)
const testplanFormRef = ref(null)
const form = reactive({
  planId: '',
  planName: '',
  planVersion: '',
  status: '',
  startDate: '',
  endDate: '',
  participants: '',
  description: '',
  projectId: ''
})

// 获取测试计划详情
const fetchTestPlan = async () => {
  loading.value = true
  try {
    const res = await testplanApi.getTestplan(planId)
    testPlan.value = res.data
    loading.value = false
  } catch (error) {
    ElMessage.error('获取测试计划详情失败')
    console.error('获取测试计划详情失败:', error)
    loading.value = false
  }
}

// 获取关联的测试用例
const fetchAssociatedTestCases = async () => {
  loading.value = true
  try {
    // 检查方法是否存在
    if (typeof testplanApi.getAssociatedTestCases !== 'function') {
      // 如果方法不存在，使用替代方法
      console.warn('getAssociatedTestCases方法不存在，使用替代方法')
      // 这里使用一个空数组作为替代
      associatedTestCases.value = []
    } else {
      const res = await testplanApi.getAssociatedTestCases(planId)
      // 处理后端返回的数据格式
      if (res.data && res.data.data) {
        associatedTestCases.value = res.data.data
      } else {
        associatedTestCases.value = res.data || []
      }
    }
    loading.value = false
  } catch (error) {
    ElMessage.error('获取关联测试用例失败')
    console.error('获取关联测试用例失败:', error)
    loading.value = false
  }
}

// 搜索测试用例
const searchTestCases = async () => {
  searchLoading.value = true
  try {
    const res = await testcaseApi.getTestcases({
      page: pagination.page,
      pageSize: pagination.pageSize,
      keyword: searchKeyword.value
    })
    testCases.value = res.data.list
    total.value = res.data.total
    searchLoading.value = false
  } catch (error) {
    ElMessage.error('搜索测试用例失败')
    console.error('搜索测试用例失败:', error)
    searchLoading.value = false
  }
}

// 处理选择变化
const handleSelectionChange = (selection) => {
  selectedCases.value = selection
}

// 处理分页大小变化
const handleSizeChange = (size) => {
  pagination.pageSize = size
  searchTestCases()
}

// 处理页码变化
const handleCurrentChange = (page) => {
  pagination.page = page
  searchTestCases()
}

// 关联测试用例
const handleAssociateCases = async () => {
  if (selectedCases.value.length === 0) {
    ElMessage.warning('请选择要关联的测试用例')
    return
  }

  try {
    // 检查方法是否存在
    if (typeof testplanApi.associateTestCases !== 'function') {
      // 如果方法不存在，显示错误信息
      console.error('associateTestCases方法不存在')
      ElMessage.error('关联测试用例功能暂不可用')
      return
    }

    const caseIds = selectedCases.value.map(item => item.caseId)
    // 确保传递的是正确的参数格式
    await testplanApi.associateTestCases(planId, { caseIds })
    ElMessage.success('关联测试用例成功')
    associateDialogVisible.value = false
    fetchAssociatedTestCases()
  } catch (error) {
    ElMessage.error('关联测试用例失败')
    console.error('关联测试用例失败:', error)
  }
}

// 移除测试用例
const handleRemoveCase = async (row) => {
  try {
    await ElMessageBox.confirm('确定要移除该测试用例吗?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    // 检查方法是否存在
    if (typeof testplanApi.removeTestCase !== 'function') {
      // 如果方法不存在，显示错误信息
      console.error('removeTestCase方法不存在')
      ElMessage.error('移除测试用例功能暂不可用')
      return
    }

    await testplanApi.removeTestCase(planId, row.caseId)
    ElMessage.success('移除测试用例成功')
    fetchAssociatedTestCases()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('移除测试用例失败')
      console.error('移除测试用例失败:', error)
    }
  }
}

// 查看测试用例
const viewTestCase = (row) => {
  router.push(`/testcase/${row.caseId}`)
}

// 显示关联对话框
const showAssociateDialog = () => {
  associateDialogVisible.value = true
  searchTestCases()
}

// 编辑测试计划
const handleEditPlan = () => {
  Object.assign(form, {
    planId: testPlan.value.planId,
    planName: testPlan.value.planName,
    planVersion: testPlan.value.planVersion,
    status: testPlan.value.status,
    startDate: testPlan.value.startDate,
    endDate: testPlan.value.endDate,
    participants: testPlan.value.participants,
    description: testPlan.value.description,
    projectId: testPlan.value.projectId
  })
  editDialogVisible.value = true
}

// 更新测试计划
const handleUpdatePlan = async () => {
  try {
    await testplanApi.updateTestplan(form.planId, form)
    ElMessage.success('更新测试计划成功')
    editDialogVisible.value = false
    fetchTestPlan()
  } catch (error) {
    ElMessage.error('更新测试计划失败')
    console.error('更新测试计划失败:', error)
  }
}

// 返回上一页
const goBack = () => {
  router.push('/testplan')
}

// 格式化日期时间
const formatDateTime = (dateTime) => {
  if (!dateTime) return ''
  const date = new Date(dateTime)
  return date.toLocaleString()
}

// 获取关联的缺陷
const fetchRelatedBugs = async () => {
  bugsLoading.value = true
  try {
    // 检查方法是否存在
    if (typeof BugApi.getBugsByPlanId !== 'function') {
      // 如果方法不存在，使用替代方法
      console.warn('getBugsByPlanId方法不存在，使用替代方法')
      // 这里使用一个空数组作为替代
      relatedBugs.value = []
    } else {
      const res = await BugApi.getBugsByPlanId(planId)
      // 处理后端返回的数据格式
      if (res.data && res.data.data) {
        relatedBugs.value = res.data.data
      } else {
        relatedBugs.value = res.data || []
      }
    }
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
    query: { planId: planId }
  })
}

// 从测试用例创建缺陷
const createBugFromCase = (testCase) => {
  router.push({
    path: '/bug/create',
    query: {
      planId: planId,
      caseId: testCase.caseId,
      caseTitle: testCase.caseTitle
    }
  })
}

onMounted(() => {
  fetchTestPlan()
  fetchAssociatedTestCases()
  fetchRelatedBugs()
})
</script>

<style scoped>
.test-plan-detail {
  padding: 20px;
}
</style>
