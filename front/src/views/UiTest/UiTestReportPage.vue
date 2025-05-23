<template>
  <div class="ui-test-report-container">
    <el-card class="report-card">
      <template #header>
        <div class="card-header">
          <div>
            <el-tabs v-model="activeTab" @tab-click="handleTabClick">
              <el-tab-pane label="全部报告" name="all_report" />
              <el-tab-pane label="通过报告" name="passed_report" />
              <el-tab-pane label="失败报告" name="failed_report" />
            </el-tabs>
          </div>
          <div class="action-buttons">
            <el-input
              v-model="searchKeyword"
              placeholder="搜索测试名称或执行人"
              @keyup.enter="fetchUiTestReports"
              class="search-input"
              clearable
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
            </el-input>
            
            <el-date-picker
              v-model="dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              format="YYYY-MM-DD"
              value-format="YYYY-MM-DD"
              @change="handleDateChange"
              :shortcuts="dateShortcuts"
              style="width: 300px"
            />
            
            <el-button type="primary" @click="fetchUiTestReports">
              搜索
            </el-button>
          </div>
        </div>
      </template>
      
      <el-table
        :data="tableData"
        :border="true"
        v-loading="loading"
        style="width: 100%; min-height: 560px"
        class="report-table"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="testName" label="测试名称" min-width="150" />
        <el-table-column prop="testType" label="测试类型" width="120">
          <template #default="{ row }">
            <el-tag>{{ row.testType === 'case' ? '测试用例' : '测试套件' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="browser" label="浏览器" width="120">
          <template #default="{ row }">
            <div class="browser-info">
              <i :class="getBrowserIcon(row.browser)"></i>
              {{ row.browser }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="environment" label="执行环境" width="120" />
        <el-table-column prop="result" label="结果" width="100">
          <template #default="{ row }">
            <el-tag :type="getResultTagType(row.result)">
              {{ getResultText(row.result) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="statistics" label="通过率" width="160">
          <template #default="{ row }">
            <el-progress
              :percentage="getPassRate(row.statistics)"
              :status="getProgressStatus(row.result)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="duration" label="耗时" width="100">
          <template #default="{ row }">
            {{ row.duration }} 秒
          </template>
        </el-table-column>
        <el-table-column prop="startTime" label="开始时间" width="180" />
        <el-table-column prop="executor" label="执行人" width="120" />
        
        <el-table-column fixed="right" label="操作" width="200">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleViewReport(row)">
              查看
            </el-button>
            <el-dropdown @command="(cmd) => handleExport(cmd, row)">
              <el-button link type="primary" size="small">
                导出<el-icon class="el-icon--right"><arrow-down /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="html">HTML报告</el-dropdown-item>
                  <el-dropdown-item command="pdf">PDF报告</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
            <el-popconfirm
              title="确认删除该测试报告吗?"
              @confirm="handleDeleteReport(row)"
              confirm-button-text="确定"
              cancel-button-text="取消"
            >
              <template #reference>
                <el-button link type="primary" size="small">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="batch-actions" v-if="selectedReports.length > 0">
        <el-button type="danger" size="small" @click="handleBatchDelete">
          批量删除 ({{ selectedReports.length }})
        </el-button>
      </div>
      
      <PaginationPage 
        :total="total" 
        @update:pagination="handlePaginationChange"
      />
    </el-card>
    
    <!-- 测试报告详情对话框 -->
    <el-dialog
      v-model="reportDialogVisible"
      title="UI测试报告详情"
      width="80%"
      destroy-on-close
      fullscreen
    >
      <div class="report-dialog-content">
        <el-tabs v-model="reportActiveTab" type="border-card">
          <el-tab-pane label="执行概览" name="overview">
            <ReportOverview 
              :reportData="currentReport" 
              @locate-step="handleLocateStep"
            />
          </el-tab-pane>
          <el-tab-pane label="步骤详情" name="steps">
            <ReportStepDetails 
              :stepsList="currentReport.steps || []" 
              ref="stepsTabRef"
            />
          </el-tab-pane>
          <el-tab-pane label="失败分析" name="analysis">
            <FailureAnalysis 
              :failureData="currentReport.failureAnalysis || {}" 
              @locate-step="handleLocateStep"
            />
          </el-tab-pane>
          <el-tab-pane label="截图对比" name="screenshots">
            <ScreenshotComparison 
              :screenshots="currentReport.screenshotPairs || []" 
            />
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue';
import { Search, ArrowDown } from '@element-plus/icons-vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import PaginationPage from '@/components/PaginationPage.vue';
import ReportOverview from '@/components/UiTestReport/ReportOverview.vue';
import ReportStepDetails from '@/components/UiTestReport/ReportStepDetails.vue';
import FailureAnalysis from '@/components/UiTestReport/FailureAnalysis.vue';
import ScreenshotComparison from '@/components/UiTestReport/ScreenshotComparison.vue';
import { UiTestReportApi } from '@/api/uiTestReportService';
import { useProjectStore } from '@/store/projectStore';

// 使用项目Store
const projectStore = useProjectStore();
// 确保初始化项目状态
projectStore.initProjectState();

// 计算当前项目ID
const currentProjectId = computed(() => projectStore.getCurrentProjectId());

// 状态管理
const loading = ref(false);
const tableData = ref([]);
const total = ref(0);
const activeTab = ref('all_report');
const searchKeyword = ref('');
const dateRange = ref(null);
const selectedReports = ref([]);
const reportDialogVisible = ref(false);
const reportActiveTab = ref('overview');
const currentReport = ref({});
const stepsTabRef = ref(null);

// 分页
const pagination = reactive({
  page: 1,
  pageSize: 10,
});

// 日期快捷选项
const dateShortcuts = [
  {
    text: '最近一周',
    value: () => {
      const end = new Date();
      const start = new Date();
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
      return [start, end];
    },
  },
  {
    text: '最近一个月',
    value: () => {
      const end = new Date();
      const start = new Date();
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
      return [start, end];
    },
  },
  {
    text: '最近三个月',
    value: () => {
      const end = new Date();
      const start = new Date();
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
      return [start, end];
    },
  },
];

// 获取浏览器图标
const getBrowserIcon = (browser) => {
  const browserIcons = {
    'chrome': 'fab fa-chrome',
    'firefox': 'fab fa-firefox',
    'edge': 'fab fa-edge',
    'safari': 'fab fa-safari',
    'ie': 'fab fa-internet-explorer'
  };
  
  return browserIcons[browser?.toLowerCase()] || 'fas fa-globe';
};

// 获取测试结果标签类型
const getResultTagType = (result) => {
  const resultMap = {
    'passed': 'success',
    'failed': 'danger',
    'running': 'warning',
    'cancelled': 'info'
  };
  return resultMap[result] || 'info';
};

// 获取测试结果文本
const getResultText = (result) => {
  const resultTextMap = {
    'passed': '通过',
    'failed': '失败',
    'running': '执行中',
    'cancelled': '已取消'
  };
  return resultTextMap[result] || result;
};

// 计算通过率
const getPassRate = (statistics) => {
  if (!statistics) return 0;
  
  const passed = statistics.passed || 0;
  const total = passed + (statistics.failed || 0) + (statistics.skipped || 0);
  
  if (total === 0) return 0;
  return Math.round((passed / total) * 100);
};

// 获取进度条状态
const getProgressStatus = (result) => {
  if (result === 'passed') return 'success';
  if (result === 'failed') return 'exception';
  if (result === 'running') return '';
  return 'warning';
};

// 加载报告数据
const loadReportData = async (tabName, page = 1, pageSize = 10, keyword = '', startDate = null, endDate = null) => {
  loading.value = true;
  try {
    // 根据tab名称确定status值
    let status = null;
    if (tabName === 'passed_report') {
      console.log("加载通过报告数据");
      status = 'passed';
    } else if (tabName === 'failed_report') {
      console.log("加载失败报告数据");
      status = 'failed';
    } else {
      console.log("加载全部报告数据");
    }
    
    // 构造请求参数
    const params = { page, pageSize };
    if (keyword) params.keyword = keyword;
    if (status) params.status = status;
    if (startDate) params.startDate = startDate;
    if (endDate) params.endDate = endDate;
    
    console.log("请求参数:", params);
    
    // 调用真实API
    const res = await UiTestReportApi.getUiTestReports(params);
    
    console.log("API响应:", res);
    
    if (res.code === 200) {
      console.log("API返回数据:", res.data);
      // 修正数据结构，将list数据映射到需要的字段结构
      if (res.data.list && Array.isArray(res.data.list)) {
        tableData.value = res.data.list.map(item => ({
          id: item.id,
          testName: item.reportName,
          testType: item.reportType === '用例执行' ? 'case' : 'suite',
          browser: item.browser?.split(' ')[0] || 'Chrome',
          environment: item.environment,
          result: item.passedCases === item.totalCases ? 'passed' : 'failed',
          statistics: {
            passed: item.passedCases || 0,
            failed: item.failedCases || 0,
            skipped: item.skippedCases || 0
          },
          duration: Math.round(item.duration / 1000) || 0,
          startTime: new Date(item.startTime).toLocaleString(),
          executor: item.executor
        }));
        total.value = res.data.total || 0;
      } else {
        tableData.value = [];
        total.value = 0;
        console.warn("API返回的数据结构不符合预期:", res.data);
      }
    } else {
      ElMessage.error(res.message || "获取UI测试报告列表失败");
    }
  } catch (error) {
    ElMessage.error("获取UI测试报告列表失败");
    console.error("获取UI测试报告列表失败:", error);
  } finally {
    loading.value = false;
  }
};

// 获取报告列表
const fetchUiTestReports = () => {
  let startDate = null;
  let endDate = null;
  
  if (dateRange.value && dateRange.value.length === 2) {
    startDate = dateRange.value[0];
    endDate = dateRange.value[1];
  }
  
  loadReportData(
    activeTab.value,
    pagination.page,
    pagination.pageSize,
    searchKeyword.value,
    startDate,
    endDate
  );
};

// 处理日期变化
const handleDateChange = () => {
  // 日期变化时不自动触发搜索，等用户点击搜索按钮
  console.log("日期范围变化:", dateRange.value);
};

// 处理表格选择变化
const handleSelectionChange = (selection) => {
  selectedReports.value = selection;
};

// 查看测试报告
const handleViewReport = async (row) => {
  try {
    // 调用真实API获取报告详情
    const res = await UiTestReportApi.getUiTestReportDetail(row.id);
    
    if (res.code === 200) {
      console.log("报告详情数据:", res.data);
      
      // 处理API返回的报告详情数据
      const reportData = res.data;
      
      // 构造前端需要的详情数据结构
      const detailData = {
        id: reportData.id,
        executionId: reportData.executionId,
        testName: reportData.reportName,
        environment: reportData.environment,
        browser: reportData.browser,
        browserVersion: reportData.browser?.split(' ')[1] || '',
        resolution: reportData.resolution || '1920x1080',
        executor: reportData.executor,
        executionType: reportData.reportType,
        startTime: new Date(reportData.startTime).toLocaleString(),
        endTime: new Date(reportData.endTime).toLocaleString(),
        duration: Math.round(reportData.duration / 1000) || 0,
        statistics: {
          passed: reportData.passedCases || 0,
          failed: reportData.failedCases || 0,
          skipped: reportData.skippedCases || 0
        },
        // 如果API返回了步骤数据，使用API数据；否则初始化为空数组
        steps: reportData.details?.steps || [],
        // 如果API返回了失败分析数据，使用API数据；否则初始化为空对象
        failureAnalysis: reportData.details?.failureAnalysis || {
          totalFailures: reportData.failedCases || 0,
          failureRate: reportData.totalCases ? (reportData.failedCases / reportData.totalCases * 100) : 0,
          criticalFailures: 0,
          errorTypes: [],
          failures: []
        },
        // 如果API返回了截图对比数据，使用API数据；否则初始化为空数组
        screenshotPairs: reportData.details?.screenshotPairs || []
      };
      
      currentReport.value = detailData;
      reportDialogVisible.value = true;
      reportActiveTab.value = 'overview';
    } else {
      ElMessage.error(res.message || "获取报告详情失败");
    }
  } catch (error) {
    ElMessage.error("获取报告详情失败");
    console.error("获取报告详情失败:", error);
  }
};

// 处理导出报告
const handleExport = async (command, row) => {
  try {
    ElMessage.info(`正在导出${command === 'html' ? 'HTML' : 'PDF'}报告...`);
    
    let response;
    if (command === 'html') {
      response = await UiTestReportApi.exportUiTestReportHtml(row.id);
    } else {
      response = await UiTestReportApi.exportUiTestReportPdf(row.id);
    }
    
    // 创建下载链接
    const blob = new Blob([response.data]);
    const link = document.createElement('a');
    link.href = URL.createObjectURL(blob);
    link.download = `UI测试报告_${row.id}.${command}`;
    link.click();
    URL.revokeObjectURL(link.href);
    
    ElMessage.success(`${command === 'html' ? 'HTML' : 'PDF'}报告导出成功`);
  } catch (error) {
    ElMessage.error("报告导出失败");
    console.error("报告导出失败:", error);
  }
};

// 删除测试报告
const handleDeleteReport = async (row) => {
  try {
    // 调用真实API删除报告
    const res = await UiTestReportApi.deleteUiTestReport(row.id);
    
    if (res.code === 200) {
      ElMessage.success("删除成功");
      await fetchUiTestReports();
    } else {
      ElMessage.error(res.message || "删除失败");
    }
  } catch (error) {
    ElMessage.error(error.message || "删除失败");
  }
};

// 批量删除报告
const handleBatchDelete = async () => {
  try {
    if (selectedReports.value.length === 0) {
      ElMessage.warning("请选择要删除的报告");
      return;
    }
    
    await ElMessageBox.confirm(
      `确认删除选中的 ${selectedReports.value.length} 条报告记录吗？此操作不可恢复！`,
      '批量删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    );
    
    const ids = selectedReports.value.map(item => item.id);
    
    // 调用真实API批量删除
    const res = await UiTestReportApi.batchDeleteUiTestReports(ids);
    
    if (res.code === 200) {
      ElMessage.success("批量删除成功");
      await fetchUiTestReports();
    } else {
      ElMessage.error(res.message || "批量删除失败");
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error("批量删除失败");
    }
  }
};

// 处理定位到特定步骤
const handleLocateStep = (stepNumber) => {
  reportActiveTab.value = 'steps';
  
  // 等待DOM更新后滚动到指定步骤
  setTimeout(() => {
    // 实现滚动到指定步骤的逻辑
    const stepRows = document.querySelectorAll('.report-step-details .el-table__row');
    const targetRow = Array.from(stepRows).find(row => {
      const firstCell = row.querySelector('td');
      return firstCell && firstCell.textContent.trim() === String(stepNumber);
    });
    
    if (targetRow) {
      targetRow.scrollIntoView({ behavior: 'smooth', block: 'center' });
      targetRow.classList.add('highlight-row');
      setTimeout(() => {
        targetRow.classList.remove('highlight-row');
      }, 3000);
    }
  }, 300);
};

// 处理Tab切换
const handleTabClick = (tab) => {
  console.log("Tab切换, 新的tab值:", tab.props.name);
  
  // 更新当前激活的tab值
  activeTab.value = tab.props.name;
  
  // 重置分页和搜索条件
  pagination.page = 1;
  pagination.pageSize = 10;
  searchKeyword.value = '';
  dateRange.value = null;
  
  // 使用正确的tab名称加载数据
  loadReportData(tab.props.name);
};

// 处理分页变化
const handlePaginationChange = ({ page, pageSize }) => {
  pagination.page = page;
  pagination.pageSize = pageSize;
  fetchUiTestReports();
};

// 组件挂载时获取数据
onMounted(() => {
  loadReportData('all_report');
});
</script>

<style scoped>
.ui-test-report-container {
  padding: 10px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.action-buttons {
  display: flex;
  gap: 10px;
  align-items: center;
}

.search-input {
  width: 240px;
}

.report-table :deep(.el-table__row) {
  cursor: pointer;
}

.report-table :deep(.highlight-row) {
  background-color: rgba(64, 158, 255, 0.1);
  transition: background-color 0.5s;
}

.browser-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.browser-info i {
  font-size: 18px;
}

.batch-actions {
  margin: 10px 0;
  display: flex;
  justify-content: flex-end;
}

.report-dialog-content {
  min-height: 600px;
}
</style> 