<!-- EditPage.vue -->
<template>
  <div class="api-test-report">
    <el-tab-pane label="测试报告" name="report">
      <!-- 顶部筛选栏 -->
      <div class="filter-bar">
        <div class="left-filters">
          <el-radio-group v-model="filterType" size="default">
            <el-radio-button label="个人">个人</el-radio-button>
          </el-radio-group>
          
          <el-select v-model="reportType" placeholder="全部类型" class="ml-10">
            <el-option label="全部类型" value="all" />
            <el-option label="性能测试" value="performance" />
            <el-option label="功能测试" value="functional" />
            <el-option label="接口测试" value="api" />
          </el-select>
        </div>
        
        <div class="search-box">
                  <el-input
            v-model="searchKeyword"
            placeholder="输入关键字进行搜索"
            prefix-icon="Search"
            clearable
          />
          <el-button type="primary" icon="Search" circle />
        </div>
      </div>
      
      <!-- 报告表格 -->
      <div class="table-header">
        <div class="col report-info">报告信息</div>
        <div class="col status">状态</div>
        <div class="col result">结果</div>
        <div class="col actions"></div>
      </div>
      
      <div class="report-list">
        <div v-for="(report, index) in filteredReports" :key="index" class="report-item">
          <div class="report-content">
            <div class="col report-info">
              <div class="report-title">
                <el-icon><Document /></el-icon>
                <span>{{ report.name }}</span>
              </div>
              <div class="report-details">
                <span class="detail-item">
                  <el-icon><Monitor /></el-icon>
                  {{ report.server }}
                </span>
                <span class="detail-item">
                  <el-icon><Timer /></el-icon>
                  {{ report.time }}
                </span>
                <span class="detail-item">
                  <el-icon><User /></el-icon>
                  {{ report.creator }}
                </span>
                <span class="detail-item">
                  <el-icon><Connection /></el-icon>
                  {{ report.concurrent }}并发用户
                </span>
                <span class="detail-item">
                  <el-icon><Clock /></el-icon>
                  运行时间 {{ report.duration }}
                </span>
              </div>
            </div>
            
            <div class="col status">
              <el-tag :type="getStatusTagType(report.status)" size="large" effect="light" round>
                {{ report.status }}
              </el-tag>
            </div>

            <div class="col result">
              <template v-if="report.type === 'performance'">
                <div class="metrics">
                  <div class="metric">
                    <div class="metric-label">每秒接口请求数:</div>
                    <div class="metric-value">{{ report.metrics.requestsPerSecond }}</div>
                  </div>
                  <div class="metric">
                    <div class="metric-label">平均响应时间:</div>
                    <div class="metric-value">{{ report.metrics.avgResponseTime }} ms</div>
                  </div>
                </div>
              </template>
              
              <template v-else>
                <div class="metrics">
                  <div class="metric">
                    <div class="metric-label">总请求数:</div>
                    <div class="metric-value">{{ report.metrics.totalRequests }}</div>
                  </div>
                  <div class="metric-row">
                    <div class="metric">
                      <div class="metric-label success">成功:</div>
                      <div class="metric-value success">{{ report.metrics.success }}</div>
                    </div>
                    <div class="metric">
                      <div class="metric-label failed">失败:</div>
                      <div class="metric-value failed">{{ report.metrics.failed }}</div>
                    </div>
                    <div class="metric">
                      <div class="metric-label untested">未测:</div>
                      <div class="metric-value untested">{{ report.metrics.untested }}</div>
                    </div>
                  </div>
                </div>
              </template>
            </div>
            
            <div class="col actions">
              <el-dropdown trigger="click">
                <el-button type="primary" text>
                  <el-icon><More /></el-icon>
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item @click="viewReport(report)">
                      <el-icon><View /></el-icon>查看报告
                    </el-dropdown-item>
                    <el-dropdown-item @click="downloadReport(report)">
                      <el-icon><Download /></el-icon>下载报告
                    </el-dropdown-item>
                    <el-dropdown-item @click="deleteReport(report)">
                      <el-icon><Delete /></el-icon>删除
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
              <el-button type="primary" text circle @click="shareReport(report)">
                <el-icon><Share /></el-icon>
              </el-button>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 分页控件 -->
      <div class="pagination-container">
        <el-pagination
          v-model="currentPage"
          :page-size="pageSize"
          :page-sizes="[10, 20, 30, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="totalReports"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-tab-pane>
    </div>
  </template>
  
  <script setup>
import { ref, computed, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { 
  Document, Monitor, Timer, User, Connection, Clock, 
  More, Download, Delete, View, Share, Search
} from '@element-plus/icons-vue';
  
// 筛选状态
const filterType = ref('个人');
const reportType = ref('all');
const searchKeyword = ref('');

// 分页控制
const currentPage = ref(1);
const pageSize = ref(10);
const totalReports = ref(100);

// 模拟报告数据
const reports = ref([
  {
    id: 1,
    name: '初始化数据',
    server: 'liys.online:8000',
    time: '7个月前',
    creator: '何祥',
    concurrent: 1,
    duration: '22 s',
    type: 'performance',
    status: '已终止',
    metrics: {
      requestsPerSecond: 0,
      avgResponseTime: '0 ms'
    }
  },
  {
    id: 2,
    name: '初始化数据',
    server: 'liys.online:8000',
    time: '7个月前',
    creator: '何祥',
    concurrent: 20,
    duration: '20 s',
    type: 'performance',
    status: '已终止',
    metrics: {
      requestsPerSecond: 0,
      avgResponseTime: '0 ms'
    }
  },
  {
    id: 3,
    name: '自动化测试',
    server: '测试环境',
    time: '9个月前',
    creator: '何祥',
    concurrent: 0,
    duration: '15 min',
    type: 'functional',
    status: '已完成',
    metrics: {
      totalRequests: 21,
      success: 0,
      failed: 21,
      untested: 0
    }
  },
  {
    id: 4,
    name: 'API性能测试',
    server: 'localhost:8080',
    time: '2天前',
    creator: '开发人员',
    concurrent: 50,
    duration: '5 min',
    type: 'performance',
    status: '已完成',
    metrics: {
      requestsPerSecond: 234,
      avgResponseTime: '45 ms'
    }
  },
  {
    id: 5,
    name: '登录接口测试',
    server: 'api.test.com',
    time: '3天前',
    creator: '测试组',
    concurrent: 10,
    duration: '1 min',
    type: 'api',
    status: '进行中',
    metrics: {
      totalRequests: 30,
      success: 25,
      failed: 3,
      untested: 2
    }
  }
]);
  
// 过滤报告列表
const filteredReports = computed(() => {
  let filtered = [...reports.value];
  
  // 按类型过滤
  if (reportType.value !== 'all') {
    filtered = filtered.filter(report => report.type === reportType.value);
  }
  
  // 关键字过滤
  if (searchKeyword.value) {
    const keyword = searchKeyword.value.toLowerCase();
    filtered = filtered.filter(report => 
      report.name.toLowerCase().includes(keyword) || 
      report.server.toLowerCase().includes(keyword) ||
      report.creator.toLowerCase().includes(keyword)
    );
  }
  
  return filtered;
});

// 根据状态获取对应的标签类型
const getStatusTagType = (status) => {
  const statusMap = {
    '已完成': 'success',
    '进行中': 'warning',
    '已终止': 'info',
    '失败': 'danger'
  };
  return statusMap[status] || 'info';
};

// 分页处理
const handleSizeChange = (val) => {
  pageSize.value = val;
  // 在实际应用中这里应该重新加载数据
};

const handleCurrentChange = (val) => {
  currentPage.value = val;
  // 在实际应用中这里应该重新加载数据
};

// 报告操作方法
const viewReport = (report) => {
  ElMessage.success(`查看报告: ${report.name}`);
  // 这里应该跳转到报告详情页面
};

const downloadReport = (report) => {
  ElMessage.success(`下载报告: ${report.name}`);
  // 下载逻辑
};

const deleteReport = (report) => {
  ElMessageBox.confirm(
    `确定要删除报告"${report.name}"吗？`,
    '删除确认',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
    .then(() => {
      ElMessage.success(`已删除报告: ${report.name}`);
      // 删除逻辑
    })
    .catch(() => {
      ElMessage.info('已取消删除');
    });
};

const shareReport = (report) => {
  ElMessage.success(`分享报告: ${report.name}`);
  // 分享逻辑
};

onMounted(() => {
  // 在实际应用中这里应该从API加载数据
  totalReports.value = reports.value.length;
});
  </script>

<style scoped>
.api-test-report {
  padding: 0;
}

.filter-bar {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
  align-items: center;
}

.left-filters {
  display: flex;
  align-items: center;
}

.ml-10 {
  margin-left: 10px;
}

.search-box {
  display: flex;
  align-items: center;
  gap: 10px;
  width: 300px;
}

.table-header {
  display: flex;
  background-color: #f5f7fa;
  padding: 12px 20px;
  font-weight: bold;
  border-top-left-radius: 8px;
  border-top-right-radius: 8px;
  color: #606266;
}

.report-list {
  border: 1px solid #ebeef5;
  border-top: none;
  border-bottom-left-radius: 8px;
  border-bottom-right-radius: 8px;
}

.report-item {
  border-bottom: 1px solid #ebeef5;
  padding: 15px 20px;
  transition: background-color 0.3s;
}

.report-item:last-child {
  border-bottom: none;
}

.report-item:hover {
  background-color: #f5f7fa;
}

.report-content {
  display: flex;
  align-items: center;
}

.col {
  padding: 0 10px;
}

.report-info {
  flex: 3;
}

.status {
  flex: 1;
  display: flex;
  justify-content: center;
}

.result {
  flex: 2;
}

.actions {
  flex: 0.5;
  display: flex;
  justify-content: flex-end;
  gap: 5px;
}

.report-title {
  font-size: 16px;
  font-weight: 500;
  margin-bottom: 8px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.report-details {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  color: #606266;
  font-size: 13px;
}

.detail-item {
  display: flex;
  align-items: center;
  gap: 5px;
}

.metrics {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.metric {
  display: flex;
  align-items: center;
  gap: 5px;
}

.metric-row {
  display: flex;
  gap: 15px;
}

.metric-label {
  color: #606266;
  font-size: 13px;
}

.metric-value {
  font-weight: 500;
  font-size: 14px;
}

.success {
  color: #67c23a;
}

.failed {
  color: #f56c6c;
}

.untested {
  color: #909399;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>