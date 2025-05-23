<template>
  <div class="test-report-detail">
    <el-card shadow="always" class="mb-4">
      <template #header>
        <div class="flex justify-between items-center">
          <h2 class="text-xl font-bold">{{ report.reportTitle }}</h2>
          <div>
            <el-button type="primary" @click="handleExportReport">导出报告</el-button>
            <el-button @click="goBack">返回</el-button>
          </div>
        </div>
      </template>
      
      <div class="grid grid-cols-2 gap-4">
        <div>
          <p><strong>报告ID:</strong> {{ report.reportId }}</p>
          <p><strong>创建人:</strong> {{ report.createdBy }}</p>
          <p><strong>创建时间:</strong> {{ formatDateTime(report.createdAt) }}</p>
        </div>
        <div>
          <p><strong>测试版本:</strong> {{ report.testVersion }}</p>
          <p><strong>所属计划:</strong> {{ testPlan ? testPlan.planName : '无' }}</p>
          <p><strong>项目ID:</strong> {{ report.projectId }}</p>
        </div>
      </div>
      
      <div class="mt-4">
        <h3 class="text-lg font-bold">报告摘要</h3>
        <p>{{ report.summary }}</p>
      </div>
      
      <div class="mt-4">
        <h3 class="text-lg font-bold">测试结论</h3>
        <p>{{ report.conclusion }}</p>
      </div>
    </el-card>
    
    <!-- 统计信息 -->
    <el-card shadow="always" class="mb-4">
      <template #header>
        <h3 class="text-lg font-bold">测试统计</h3>
      </template>
      
      <el-row :gutter="20">
        <el-col :span="6">
          <el-statistic title="测试用例总数" :value="statistics.totalCases">
            <template #suffix>
              <span>个</span>
            </template>
          </el-statistic>
        </el-col>
        <el-col :span="6">
          <el-statistic title="通过用例" :value="statistics.passedCases">
            <template #suffix>
              <span>个</span>
            </template>
          </el-statistic>
        </el-col>
        <el-col :span="6">
          <el-statistic title="失败用例" :value="statistics.failedCases">
            <template #suffix>
              <span>个</span>
            </template>
          </el-statistic>
        </el-col>
        <el-col :span="6">
          <el-statistic title="缺陷数量" :value="statistics.bugCount">
            <template #suffix>
              <span>个</span>
            </template>
          </el-statistic>
        </el-col>
      </el-row>
      
      <div class="mt-4">
        <el-progress 
          :percentage="passRate" 
          :color="getPassRateColor(passRate)"
          :format="format"
          :stroke-width="20"
        ></el-progress>
      </div>
    </el-card>
    
    <!-- 关联的测试用例 -->
    <el-card shadow="always" class="mb-4">
      <template #header>
        <h3 class="text-lg font-bold">关联的测试用例</h3>
      </template>
      
      <el-table :data="testCases" border stripe>
        <el-table-column prop="caseId" label="用例ID" width="80" />
        <el-table-column prop="caseTitle" label="用例标题" />
        <el-table-column prop="importanceLevel" label="重要级别" width="100" />
        <el-table-column prop="executionResult" label="执行结果" width="100">
          <template #default="{ row }">
            <el-tag :type="getResultTagType(row.executionResult)">
              {{ row.executionResult }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="viewTestCase(row)">查看</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="flex justify-center mt-4" v-if="testCases.length === 0">
        <el-empty description="暂无关联的测试用例" />
      </div>
    </el-card>
    
    <!-- 关联的缺陷 -->
    <el-card shadow="always" class="mb-4">
      <template #header>
        <h3 class="text-lg font-bold">关联的缺陷</h3>
      </template>
      
      <el-table :data="bugs" border stripe>
        <el-table-column prop="bugId" label="缺陷ID" width="80" />
        <el-table-column prop="bugTitle" label="缺陷标题" />
        <el-table-column prop="severity" label="严重程度" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getBugStatusTagType(row.status)">
              {{ row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="viewBug(row)">查看</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="flex justify-center mt-4" v-if="bugs.length === 0">
        <el-empty description="暂无关联的缺陷" />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { TestReportApi } from '@/api/testReportService'

const route = useRoute()
const router = useRouter()
const reportId = route.params.id

// 报告数据
const report = ref({})
const testPlan = ref(null)
const testCases = ref([])
const bugs = ref([])
const statistics = reactive({
  totalCases: 0,
  passedCases: 0,
  failedCases: 0,
  blockedCases: 0,
  bugCount: 0
})

// 计算通过率
const passRate = computed(() => {
  if (statistics.totalCases === 0) return 0
  return Math.round((statistics.passedCases / statistics.totalCases) * 100)
})

// 格式化通过率显示
const format = (percentage) => {
  return `通过率: ${percentage}%`
}

// 获取通过率颜色
const getPassRateColor = (rate) => {
  if (rate >= 90) return '#67C23A'
  if (rate >= 70) return '#E6A23C'
  return '#F56C6C'
}

// 获取测试报告详情
const fetchReportDetail = async () => {
  try {
    const res = await TestReportApi.getAggregatedReportInfo(reportId)
    report.value = res.data.report || {}
    testPlan.value = res.data.testPlan || null
    testCases.value = res.data.testCases || []
    bugs.value = res.data.bugs || []
    
    // 更新统计信息
    Object.assign(statistics, res.data.statistics || {
      totalCases: 0,
      passedCases: 0,
      failedCases: 0,
      blockedCases: 0,
      bugCount: 0
    })
  } catch (error) {
    ElMessage.error('获取测试报告详情失败')
    console.error('获取测试报告详情失败:', error)
  }
}

// 获取执行结果标签类型
const getResultTagType = (result) => {
  const resultMap = {
    '通过': 'success',
    '失败': 'danger',
    '阻塞': 'warning',
    '未执行': 'info'
  }
  return resultMap[result] || 'info'
}

// 获取缺陷状态标签类型
const getBugStatusTagType = (status) => {
  const statusMap = {
    '已修复': 'success',
    '待修复': 'warning',
    '已创建': 'info',
    '已关闭': 'info'
  }
  return statusMap[status] || 'info'
}

// 查看测试用例
const viewTestCase = (row) => {
  router.push(`/testcase/${row.caseId}`)
}

// 查看缺陷
const viewBug = (row) => {
  router.push(`/bug/${row.bugId}`)
}

// 导出报告
const handleExportReport = () => {
  ElMessage.success('报告导出功能正在开发中')
}

// 返回上一页
const goBack = () => {
  router.push('/testreport')
}

// 格式化日期时间
const formatDateTime = (dateTime) => {
  if (!dateTime) return ''
  const date = new Date(dateTime)
  return date.toLocaleString()
}

onMounted(() => {
  fetchReportDetail()
})
</script>

<style scoped>
.test-report-detail {
  padding: 20px;
}
</style>
