<template>
  <div class="api-test-report-container">
    <el-card class="report-card">
      <template #header>
        <div class="card-header">
          <div>
            <el-tabs v-model="activeTab" @tab-click="handleTabClick">
              <el-tab-pane label="全部报告" name="all_report" />
              <el-tab-pane label="成功记录" name="success_report" />
              <el-tab-pane label="失败记录" name="failed_report" />
            </el-tabs>
          </div>
          <div class="action-buttons">
            <el-input
              v-model="searchKeyword"
              :placeholder="placeholderText"
              @focus="handleFocus"
              @blur="handleBlur"
              @keyup.enter="fetchApiTestReportList"
              class="search-input"
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
            </el-input>
            <el-button type="primary" @click="handleExportReport">导出报告</el-button>
          </div>
        </div>
      </template>
      
      <el-table
        :data="tableData"
        :border="true"
        v-loading="loading"
        style="width: 100%; min-height: 560px"
        class="customer-table"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="ID" min-width="80" />
        <el-table-column prop="apiName" label="接口名称" min-width="150" />
        <el-table-column prop="url" label="接口地址" min-width="200" />
        <el-table-column prop="method" label="请求方法" min-width="100">
          <template #default="{ row }">
            <el-tag :type="getMethodType(row.method)">{{ row.method }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" min-width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'success' ? 'success' : 'danger'">
              {{ row.status === 'success' ? '成功' : '失败' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="responseTime" label="响应时间(ms)" min-width="100" />
        <el-table-column prop="executionTime" label="执行时间" min-width="170" />
        <el-table-column prop="executor" label="执行人" min-width="120" />
        
        <el-table-column fixed="right" label="操作" min-width="150">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleViewDetails(row)">
              详情
            </el-button>
            <el-popconfirm
              title="确认删除该测试记录吗?"
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
      
      <!-- 接口详情弹窗 -->
      <el-dialog
        v-model="dialogVisible"
        title="接口测试详情"
        width="800px"
      >
        <el-descriptions :column="1" border>
          <el-descriptions-item label="接口名称">{{ currentReport.apiName }}</el-descriptions-item>
          <el-descriptions-item label="接口地址">{{ currentReport.url }}</el-descriptions-item>
          <el-descriptions-item label="请求方法">{{ currentReport.method }}</el-descriptions-item>
          <el-descriptions-item label="执行时间">{{ currentReport.executionTime }}</el-descriptions-item>
          <el-descriptions-item label="执行人">{{ currentReport.executor }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="currentReport.status === 'success' ? 'success' : 'danger'">
              {{ currentReport.status === 'success' ? '成功' : '失败' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="响应时间">{{ currentReport.responseTime }}ms</el-descriptions-item>
        </el-descriptions>
        
        <el-divider content-position="left">请求头</el-divider>
        <el-input
          type="textarea"
          v-model="formattedHeaders"
          :rows="4"
          readonly
        />
        
        <el-divider content-position="left">请求参数</el-divider>
        <el-input
          type="textarea"
          v-model="formattedParams"
          :rows="4"
          readonly
        />
        
        <el-divider content-position="left">响应结果</el-divider>
        <el-input
          type="textarea"
          v-model="formattedResponse"
          :rows="7"
          readonly
        />
        
        <template #footer>
          <span>
            <el-button @click="dialogVisible = false">关闭</el-button>
            <el-button type="primary" @click="handleCopyResponse">复制响应</el-button>
          </span>
        </template>
      </el-dialog>
      
      <PaginationPage 
        :total="total" 
        @update:pagination="handlePaginationChange"
      />
    </el-card>
  </div>
</template>

<script setup>
import PaginationPage from "@/components/PaginationPage.vue";
import { ref, reactive, onMounted, computed } from "vue";
import { Search } from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";
import { useRouter } from "vue-router";
import { ApiTestReportApi } from "@/api/apiTestReportService";

const router = useRouter();

// 状态管理
const loading = ref(false);
const dialogVisible = ref(false);
const placeholderText = ref("输入查询关键字⏎");
const searchKeyword = ref("");
const activeTab = ref("all_report");
const tableData = ref([]);
const total = ref(0);
const currentReport = ref({});

// 分页
const pagination = reactive({
  page: 1,
  pageSize: 10,
});

// 格式化显示内容
const formattedHeaders = computed(() => {
  return JSON.stringify(currentReport.value.headers || {}, null, 2);
});

const formattedParams = computed(() => {
  return JSON.stringify(currentReport.value.params || {}, null, 2);
});

const formattedResponse = computed(() => {
  return JSON.stringify(currentReport.value.response || {}, null, 2);
});

// 获取请求方法对应的标签类型
const getMethodType = (method) => {
  const methodMap = {
    GET: '',
    POST: 'success',
    PUT: 'warning',
    DELETE: 'danger'
  };
  return methodMap[method] || 'info';
};

// 加载报告数据的核心方法
const loadReportData = async (tabName, page = 1, pageSize = 10, keyword = '') => {
  loading.value = true;
  try {
    // 根据tab名称确定status值
    let status = null;
    if (tabName === 'success_report') {
      console.log("加载成功报告数据");
      status = 'success';
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
    
    console.log("请求参数:", params);
    
    const res = await ApiTestReportApi.getApiTestReports(params);
    tableData.value = res.data.data;
    total.value = res.data.total;
  } catch (error) {
    ElMessage.error("获取接口测试报告列表失败");
    console.error("获取接口测试报告列表失败:", error);
  } finally {
    loading.value = false;
  }
};

// 获取报告列表 - 这个方法用于搜索和分页
const fetchApiTestReportList = () => {
  loadReportData(
    activeTab.value,
    pagination.page,
    pagination.pageSize,
    searchKeyword.value
  );
};

// 查看详情
const handleViewDetails = async (row) => {
  try {
    const res = await ApiTestReportApi.getApiTestReportDetail(row.id);
    currentReport.value = res.data;
    dialogVisible.value = true;
  } catch (error) {
    ElMessage.error("获取接口测试详情失败");
  }
};

// 删除报告
const handleDeleteReport = async (row) => {
  try {
    await ApiTestReportApi.deleteApiTestReport(row.id);
    ElMessage.success("删除成功");
    await fetchApiTestReportList();
  } catch (error) {
    ElMessage.error(error.message || "删除失败");
  }
};

// 导出报告
const handleExportReport = async () => {
  try {
    const selectedRows = tableData.value.filter(row => row.selected);
    if (selectedRows.length === 0) {
      ElMessage.warning("请选择要导出的报告");
      return;
    }
    
    const id = selectedRows[0].id;
    const response = await ApiTestReportApi.exportApiTestReport(id);
    
    // 创建下载链接
    const blob = new Blob([response.data]);
    const link = document.createElement('a');
    link.href = URL.createObjectURL(blob);
    link.download = `接口测试报告_${id}.xlsx`;
    link.click();
    URL.revokeObjectURL(link.href);
    
    ElMessage.success("报告导出成功");
  } catch (error) {
    ElMessage.error("报告导出失败");
  }
};

// 复制响应内容
const handleCopyResponse = () => {
  navigator.clipboard.writeText(formattedResponse.value)
    .then(() => {
      ElMessage.success("复制成功");
    })
    .catch(() => {
      ElMessage.error("复制失败");
    });
};

// 处理焦点事件
const handleFocus = () => {
  placeholderText.value = "";
};

const handleBlur = () => {
  placeholderText.value = "输入查询关键字⏎";
};

// 处理Tab切换
const handleTabClick = (tab) => {
  console.log("Tab切换, 新的tab值:", tab.props.name);
  
  // 重置分页和搜索条件
  pagination.page = 1;
  pagination.pageSize = 10;
  searchKeyword.value = '';
  
  // 使用传入的tab.props.name而不是activeTab.value
  loadReportData(tab.props.name);
};

// 处理分页变化
const handlePaginationChange = ({ page, pageSize }) => {
  pagination.page = page;
  pagination.pageSize = pageSize;
  fetchApiTestReportList();
};

// 组件挂载时获取数据
onMounted(() => {
  loadReportData('all_report');
});
</script>

<style scoped>
.api-test-report-container {
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
}

.search-input {
  width: 240px;
}

.customer-table :deep(.el-table__cell) {
  border-right: none; /* 隐藏表格纵向边框 */
}

.customer-table {
  overflow: hidden; /* 隐藏溢出的内容 */
}

.customer-table :deep(.el-table__inner-wrapper) {
  overflow-x: auto; /* 仅在需要时显示横向滚动条 */
  overflow-y: hidden; /* 隐藏纵向滚动条 */
}

.customer-table :deep(.el-table__body-wrapper) {
  overflow-y: auto; /* 允许内容区域纵向滚动 */
  max-height: 560px; /* 限制最大高度 */
}
</style> 