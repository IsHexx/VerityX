<template>
  <div>
    <div class="flex flex-wrap gap-4">
      <el-card shadow="always" class="w-full">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-tabs v-model="activeTab" @tab-click="handleTabClick">
              <el-tab-pane label="执行队列" name="queue"></el-tab-pane>
              <el-tab-pane label="执行历史" name="history"></el-tab-pane>
            </el-tabs>
          </el-col>
          <el-col :span="18" style="text-align: right; align-content: center">
            <el-input
              v-model="searchKeyword"
              style="width: 240px"
              placeholder="搜索执行记录..."
              :suffix-icon="Search"
              @keyup.enter="handleSearch"
            />
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button type="primary" @click="openExecutionDialog">+ 新建执行</el-button>
          </el-col>
        </el-row>

        <div v-if="activeTab === 'queue'">
          <!-- 执行队列 -->
          <el-table
            :data="queueData"
            :border="true"
            v-loading="queueLoading"
            style="width: 100%; margin-top: 20px; min-height: 300px"
          >
            <el-table-column prop="id" label="队列ID" min-width="80" />
            <el-table-column prop="name" label="任务名称" min-width="150" />
            <el-table-column prop="type" label="执行类型" min-width="100">
              <template #default="scope">
                <el-tag
                  :type="scope.row.type === '用例' ? 'primary' : 'success'"
                  effect="light"
                >
                  {{ scope.row.type }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" min-width="100">
              <template #default="scope">
                <el-tag
                  :type="getStatusTagType(scope.row.status)"
                  effect="light"
                >
                  {{ scope.row.status }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="environment" label="执行环境" min-width="120" />
            <el-table-column prop="browser" label="浏览器" min-width="100" />
            <el-table-column prop="progress" label="进度" min-width="200">
              <template #default="scope">
                <el-progress
                  :percentage="scope.row.progress"
                  :status="getProgressStatus(scope.row.status)"
                />
              </template>
            </el-table-column>
            <el-table-column prop="queueTime" label="进入队列时间" min-width="180" />
            <el-table-column prop="startTime" label="开始执行时间" min-width="180" />

            <el-table-column label="操作" width="220" fixed="right">
              <template #default="scope">
                <el-button type="primary" size="small" @click="viewExecutionDetail(scope.row)" :disabled="scope.row.status === '排队中'">
                  详情
                </el-button>
                <el-button type="danger" size="small" @click="cancelExecution(scope.row)" v-if="scope.row.status === '排队中' || scope.row.status === '执行中'">
                  取消
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <!-- 队列分页组件 -->
          <div class="pagination-container" v-if="total > 0">
            <el-pagination
              :current-page="pagination.page"
              :page-size="pagination.pageSize"
              :page-sizes="[10, 20, 50, 100]"
              layout="total, sizes, prev, pager, next, jumper"
              :total="total"
              @size-change="(size) => handlePaginationChange({page: pagination.page, pageSize: size})"
              @current-change="(page) => handlePaginationChange({page, pageSize: pagination.pageSize})"
            />
          </div>
        </div>

        <div v-else>
          <!-- 执行历史 -->
          <el-row style="margin: 20px 0 10px 0">
            <el-col :span="24">
              <el-form :inline="true" class="filter-form">
                <el-form-item label="执行状态">
                  <el-select v-model="filters.status" placeholder="全部" clearable style="width: 120px">
                    <el-option label="成功" value="成功" />
                    <el-option label="失败" value="失败" />
                    <el-option label="执行中" value="执行中" />
                    <el-option label="已取消" value="已取消" />
                  </el-select>
                </el-form-item>
                <el-form-item label="执行类型">
                  <el-select v-model="filters.type" placeholder="全部" clearable style="width: 120px">
                    <el-option label="用例" value="用例" />
                    <el-option label="测试套件" value="测试套件" />
                  </el-select>
                </el-form-item>
                <el-form-item label="执行时间">
                  <el-date-picker
                    v-model="filters.timeRange"
                    type="daterange"
                    range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                    format="YYYY-MM-DD"
                    value-format="YYYY-MM-DD"
                  />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="handleFilterSearch">筛选</el-button>
                  <el-button @click="resetFilters">重置</el-button>
                </el-form-item>
              </el-form>
            </el-col>
          </el-row>

          <el-table
            :data="historyData"
            :border="true"
            v-loading="historyLoading"
            style="width: 100%; margin-top: 10px; min-height: 300px"
          >
            <el-table-column prop="id" label="执行ID" min-width="80" />
            <el-table-column prop="name" label="任务名称" min-width="150" />
            <el-table-column prop="type" label="执行类型" min-width="100">
              <template #default="scope">
                <el-tag
                  :type="scope.row.type === '用例' ? 'primary' : 'success'"
                  effect="light"
                >
                  {{ scope.row.type }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="执行结果" min-width="100">
              <template #default="scope">
                <el-tag
                  :type="getStatusTagType(scope.row.status)"
                  effect="light"
                >
                  {{ scope.row.status }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="environment" label="执行环境" min-width="120" />
            <el-table-column prop="browser" label="浏览器" min-width="100" />
            <el-table-column prop="duration" label="执行时长" min-width="100" />
            <el-table-column prop="startTime" label="开始时间" min-width="180" />
            <el-table-column prop="endTime" label="结束时间" min-width="180" />
            <el-table-column prop="executedBy" label="执行人" min-width="100" />

            <el-table-column fixed="right" label="操作" min-width="240">
              <template #default="{ row }">
                <el-button link type="primary" size="small" @click="viewExecutionDetail(row)">
                  详情
                </el-button>
                <el-button link type="primary" size="small" @click="viewExecutionLogs(row)">
                  日志
                </el-button>
                <el-button link type="primary" size="small" @click="rerunExecution(row)">
                  重新执行
                </el-button>
                <el-button link type="primary" size="small" @click="viewExecutionReport(row)" :disabled="row.status !== '成功' && row.status !== '失败'">
                  查看报告
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <PaginationPage
            :total="total"
            @update:pagination="handlePaginationChange"
          />
        </div>
      </el-card>
    </div>

    <!-- 添加必要的对话框组件 -->
    <ExecutionSettingDialog
      :visible="executionSettingVisible"
      @update:visible="executionSettingVisible = $event"
      @execution-started="handleExecutionStarted"
    />

    <ExecutionDetailDialog
      :visible="executionDetailVisible"
      @update:visible="executionDetailVisible = $event"
      :executionId="currentExecutionId"
      @view-logs="openLogDialog"
      @rerun="rerunExecution"
    />

    <ExecutionLogDialog
      :visible="executionLogsVisible"
      @update:visible="executionLogsVisible = $event"
      :execution="currentExecution"
    />
  </div>
</template>

<script setup>
import PaginationPage from "@/components/PaginationPage.vue";
import ExecutionSettingDialog from "@/components/UiTestExecute/ExecutionSettingDialog.vue";
import ExecutionDetailDialog from "@/components/UiTestExecute/ExecutionDetailDialog.vue";
import ExecutionLogDialog from "@/components/UiTestExecute/ExecutionLogDialog.vue";
import { ref, reactive, onMounted, computed } from "vue";
import { Search } from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";
import { UiTestExecutionApi } from "@/api/uiTestExecutionService";
import { useProjectStore } from '@/store/projectStore';

// 使用项目Store
const projectStore = useProjectStore();
// 确保初始化项目状态
projectStore.initProjectState();

// 计算当前项目ID
const currentProjectId = computed(() => {
  const id = projectStore.getCurrentProjectId();
  console.log("UiTestExecutionPage - 当前项目ID:", id);
  return id;
});

// 状态管理
const loading = ref(false);
const queueLoading = ref(false);
const historyLoading = ref(false);
const searchKeyword = ref("");
const activeTab = ref("queue");
const queueData = ref([]);
const historyData = ref([]);
const total = ref(0);

// 筛选条件
const filters = reactive({
  status: "",
  type: "",
  timeRange: [],
});

// 分页
const pagination = reactive({
  page: 1,
  pageSize: 10,
});

// 更新对话框状态
const executionSettingVisible = ref(false);
const executionDetailVisible = ref(false);
const executionLogsVisible = ref(false);
const currentExecutionId = ref(null);
const currentExecution = ref(null);

// 状态标签类型映射
const getStatusTagType = (status) => {
  const statusMap = {
    "成功": "success",
    "失败": "danger",
    "执行中": "warning",
    "排队中": "info",
    "已取消": ""
  };
  return statusMap[status] || "";
};

// 进度状态映射
const getProgressStatus = (status) => {
  if (status === '执行中') return '';
  if (status === '成功') return 'success';
  if (status === '失败') return 'exception';
  return '';
};

// 处理Tab切换
const handleTabClick = (tab) => {
  try {
    if (tab.props.name === 'queue') {
      fetchQueueData();
    } else {
      fetchHistoryData();
    }
  } catch (error) {
    console.error("Tab切换错误:", error);
    ElMessage.error("页面加载出错，请刷新重试");
  }
};

// 获取队列数据
const fetchQueueData = async () => {
  queueLoading.value = true;
  try {
    const params = {
      page: pagination.page,
      pageSize: pagination.pageSize,
      projectId: currentProjectId.value
    };

    if (filters.status) params.status = filters.status;

    console.log("fetchQueueData - 发送请求参数:", JSON.stringify(params));

    const res = await UiTestExecutionApi.getExecutionQueue(params);
    if (res.code === 200 && res.data) {
      // 处理返回的分页数据结构
      if (res.data.list && Array.isArray(res.data.list)) {
        queueData.value = res.data.list.map(item => ({
          ...item,
          // 确保必要的字段存在
          id: item.id || '',
          name: item.executionName || item.name || '未命名执行',
          type: item.executionType || item.type || '未知',
          status: item.status || '未知',
          progress: item.progress || 0
        }));
        total.value = res.data.total || 0;
      } else if (Array.isArray(res.data)) {
        // 兼容处理直接返回数组的情况
        queueData.value = res.data.map(item => ({
          ...item,
          id: item.id || '',
          name: item.executionName || item.name || '未命名执行',
          type: item.executionType || item.type || '未知',
          status: item.status || '未知',
          progress: item.progress || 0
        }));
      } else {
        queueData.value = [];
        console.warn("API返回数据格式不符合预期:", res.data);
      }
    } else {
      ElMessage.error(res.message || '获取执行队列失败');
      queueData.value = []; // 出错时清空数据
    }
  } catch (error) {
    ElMessage.error("获取执行队列失败");
    console.error("获取执行队列失败:", error);
    queueData.value = []; // 出错时清空数据
  } finally {
    queueLoading.value = false;
  }
};

// 获取历史数据
const fetchHistoryData = async () => {
  historyLoading.value = true;
  try {
    // 构造请求参数
    const params = {
      page: pagination.page,
      pageSize: pagination.pageSize,
      keyword: searchKeyword.value,
      projectId: currentProjectId.value
    };

    if (filters.status) params.status = filters.status;
    if (filters.type) params.type = filters.type;
    if (filters.timeRange && filters.timeRange.length === 2) {
      params.startTime = filters.timeRange[0];
      params.endTime = filters.timeRange[1];
    }

    console.log("fetchHistoryData - 发送请求参数:", JSON.stringify(params));

    const res = await UiTestExecutionApi.getExecutionList(params);
    if (res.code === 200 && res.data) {
      // 确保返回的数据是结构化的，包含list字段
      if (res.data.list && Array.isArray(res.data.list)) {
        historyData.value = res.data.list.map(item => ({
          ...item,
          // 确保必要的字段存在
          id: item.id || '',
          name: item.name || item.executionName || '未命名执行',
          type: item.type || item.executionType || '未知',
          status: item.status || '未知'
        }));
        total.value = res.data.total || 0;
      } else {
        // 处理返回结构不符合预期的情况
        historyData.value = [];
        total.value = 0;
        console.warn("API返回数据格式不符合预期:", res.data);
      }
    } else {
      ElMessage.error(res.message || '获取执行历史失败');
      historyData.value = []; // 出错时清空数据
      total.value = 0;
    }
  } catch (error) {
    ElMessage.error("获取执行历史失败");
    console.error("获取执行历史失败:", error);
    historyData.value = []; // 出错时清空数据
    total.value = 0;
  } finally {
    historyLoading.value = false;
  }
};

// 处理分页变化
const handlePaginationChange = ({ page, pageSize }) => {
  pagination.page = page;
  pagination.pageSize = pageSize;
  if (activeTab.value === 'history') {
    fetchHistoryData();
  } else if (activeTab.value === 'queue') {
    fetchQueueData();
  }
};

// 处理搜索
const handleSearch = () => {
  pagination.page = 1; // 重置页码
  if (activeTab.value === 'history') {
    fetchHistoryData();
  } else {
    fetchQueueData();
  }
};

// 处理筛选
const handleFilterSearch = () => {
  pagination.page = 1;
  fetchHistoryData();
};

// 重置筛选条件
const resetFilters = () => {
  filters.status = "";
  filters.type = "";
  filters.timeRange = [];
  pagination.page = 1;
  fetchHistoryData();
};

// 打开执行对话框
const openExecutionDialog = () => {
  executionSettingVisible.value = true;
};

// 查看执行详情
const viewExecutionDetail = (row) => {
  currentExecutionId.value = row.id;
  executionDetailVisible.value = true;
};

// 查看执行日志
const viewExecutionLogs = (row) => {
  currentExecution.value = row;
  executionLogsVisible.value = true;
};

// 打开日志对话框（从执行详情触发）
const openLogDialog = (execution) => {
  currentExecution.value = execution;
  executionLogsVisible.value = true;
};

// 查看执行报告
const viewExecutionReport = (row) => {
  window.open(`/api/ui-test-report/${row.id}`, '_blank');
};

// 重新执行
const rerunExecution = async (execution) => {
  try {
    const res = await UiTestExecutionApi.rerunExecution(execution.id);
    if (res.code === 200) {
      ElMessage.success('测试重新执行已提交');
      fetchQueueData();
    } else {
      ElMessage.error(res.message || '重新执行失败');
    }
  } catch (error) {
    ElMessage.error('重新执行失败');
    console.error('重新执行失败:', error);
  }
};

// 取消执行
const cancelExecution = async (row) => {
  try {
    const res = await UiTestExecutionApi.stopExecution(row.id);
    if (res.code === 200) {
      ElMessage.success('已取消执行');
      fetchQueueData();
    } else {
      ElMessage.error(res.message || '取消执行失败');
    }
  } catch (error) {
    ElMessage.error('取消执行失败');
    console.error('取消执行失败:', error);
  }
};

// 处理新执行提交
const handleExecutionStarted = (executionData) => {
  ElMessage.success(`执行任务已提交: ${executionData.name}`);
  // 刷新队列数据
  fetchQueueData();
};

// 组件挂载时获取数据
onMounted(() => {
  // 重置分页和过滤条件
  pagination.page = 1;
  pagination.pageSize = 10;
  searchKeyword.value = '';

  console.log("UiTestExecutionPage - 组件挂载时的项目ID:", currentProjectId.value);
  console.log("UiTestExecutionPage - 项目ID类型:", typeof currentProjectId.value);

  // 获取队列数据
  fetchQueueData();
});
</script>

<style scoped>
.filter-form {
  display: flex;
  flex-wrap: wrap;
}

.filter-form .el-form-item {
  margin-right: 20px;
  margin-bottom: 10px;
}

/* 定制表格样式 */
:deep(.el-table__header) {
  font-weight: 600;
  background-color: #f5f7fa;
}

:deep(.el-table__row) {
  transition: background-color 0.25s ease;
}

:deep(.el-table__row:hover) {
  background-color: #ecf5ff;
}

/* 进度条样式 */
:deep(.el-progress) {
  margin: 10px 0;
}

/* 标签样式 */
:deep(.el-tag) {
  font-weight: 500;
}
</style>