<template>
  <div class="system-monitor-container">
    <el-tabs v-model="activeTab" class="monitor-tabs">
      <el-tab-pane label="系统状态监控" name="status">
        <div class="tab-content">
          <el-row :gutter="20">
            <el-col :span="8" v-for="(item, index) in statusCards" :key="index">
              <el-card shadow="hover" class="status-card">
                <template #header>
                  <div class="status-header">
                    <span>{{ item.title }}</span>
                    <el-tag :type="item.status === '正常' ? 'success' : (item.status === '警告' ? 'warning' : 'danger')">
                      {{ item.status }}
                    </el-tag>
                  </div>
                </template>
                <div class="status-content">
                  <div class="metric" v-for="(metric, midx) in item.metrics" :key="midx">
                    <span class="metric-name">{{ metric.name }}:</span>
                    <span class="metric-value">{{ metric.value }}</span>
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>
          
          <el-card shadow="hover" class="status-chart-card">
            <template #header>
              <div class="card-header">
                <span>系统资源使用趋势</span>
                <el-radio-group v-model="chartTimeRange" size="small">
                  <el-radio-button label="hour">1小时</el-radio-button>
                  <el-radio-button label="day">24小时</el-radio-button>
                  <el-radio-button label="week">7天</el-radio-button>
                </el-radio-group>
              </div>
            </template>
            <div class="chart-container">
              <!-- 此处放置资源监控图表 -->
              <div class="chart-placeholder">
                此处为资源监控图表展示区域
              </div>
            </div>
          </el-card>
        </div>
      </el-tab-pane>
      
      <el-tab-pane label="系统日志" name="logs">
        <div class="tab-content">
          <el-card shadow="hover" class="log-card">
            <template #header>
              <div class="card-header">
                <span>系统日志查询</span>
                <el-button type="primary" size="small" @click="handleExportLogs">导出日志</el-button>
              </div>
            </template>
            <el-form :model="logQuery" :inline="true" class="log-query-form">
              <el-form-item label="日志类型">
                <el-select v-model="logQuery.type" placeholder="选择日志类型">
                  <el-option label="操作日志" value="operation"></el-option>
                  <el-option label="异常日志" value="error"></el-option>
                  <el-option label="性能日志" value="performance"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="日志级别">
                <el-select v-model="logQuery.level" placeholder="选择日志级别" multiple collapse-tags>
                  <el-option label="INFO" value="info"></el-option>
                  <el-option label="WARN" value="warn"></el-option>
                  <el-option label="ERROR" value="error"></el-option>
                  <el-option label="DEBUG" value="debug"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="时间范围">
                <el-date-picker
                  v-model="logQuery.timeRange"
                  type="datetimerange"
                  range-separator="至"
                  start-placeholder="开始时间"
                  end-placeholder="结束时间"
                  format="YYYY-MM-DD HH:mm:ss"
                ></el-date-picker>
              </el-form-item>
              <el-form-item label="关键词">
                <el-input v-model="logQuery.keyword" placeholder="输入关键词"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="handleQueryLogs">查询</el-button>
                <el-button @click="handleResetLogQuery">重置</el-button>
              </el-form-item>
            </el-form>
            
            <el-table :data="logList" style="width: 100%" height="450" v-loading="loading">
              <el-table-column prop="timestamp" label="时间" width="180"></el-table-column>
              <el-table-column prop="level" label="级别" width="100">
                <template #default="scope">
                  <el-tag :type="getLogLevelType(scope.row.level)">{{ scope.row.level }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="module" label="模块" width="150"></el-table-column>
              <el-table-column prop="content" label="内容" show-overflow-tooltip></el-table-column>
              <el-table-column prop="user" label="操作用户" width="120"></el-table-column>
              <el-table-column prop="ip" label="IP地址" width="150"></el-table-column>
              <el-table-column label="操作" width="80">
                <template #default="scope">
                  <el-button type="primary" link @click="handleViewLogDetail(scope.row)">详情</el-button>
                </template>
              </el-table-column>
            </el-table>
            
            <div class="pagination-container">
              <el-pagination
                background
                layout="total, sizes, prev, pager, next"
                :total="total"
                :page-size="pageSize"
                :current-page="currentPage"
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
              />
            </div>
          </el-card>
        </div>
      </el-tab-pane>
      
      <el-tab-pane label="数据备份与恢复" name="backup">
        <div class="tab-content">
          <el-card shadow="hover" class="backup-card">
            <template #header>
              <div class="card-header">
                <span>数据备份</span>
                <el-button type="primary" size="small" @click="handleCreateBackup">创建备份</el-button>
              </div>
            </template>
            <el-form :model="backupConfig" label-width="140px" label-position="left">
              <el-form-item label="自动备份">
                <el-switch v-model="backupConfig.autoBackup"></el-switch>
              </el-form-item>
              <el-form-item label="备份频率" v-if="backupConfig.autoBackup">
                <el-select v-model="backupConfig.frequency" placeholder="选择备份频率">
                  <el-option label="每天" value="daily"></el-option>
                  <el-option label="每周" value="weekly"></el-option>
                  <el-option label="每月" value="monthly"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="备份时间" v-if="backupConfig.autoBackup">
                <el-time-picker
                  v-model="backupConfig.time"
                  format="HH:mm"
                  placeholder="选择时间"
                ></el-time-picker>
              </el-form-item>
              <el-form-item label="保留备份数量">
                <el-input-number v-model="backupConfig.keepCount" :min="1" :max="30"></el-input-number>
              </el-form-item>
              <el-form-item label="备份存储位置">
                <el-input v-model="backupConfig.location" placeholder="输入备份存储路径"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="handleSaveBackupConfig">保存配置</el-button>
              </el-form-item>
            </el-form>
            
            <el-divider content-position="left">备份历史</el-divider>
            <el-table :data="backupList" style="width: 100%" v-loading="loading">
              <el-table-column prop="filename" label="备份文件名"></el-table-column>
              <el-table-column prop="size" label="大小" width="120"></el-table-column>
              <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
              <el-table-column prop="type" label="类型" width="120">
                <template #default="scope">
                  <el-tag>{{ scope.row.type }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="250">
                <template #default="scope">
                  <el-button type="primary" link @click="handleDownloadBackup(scope.row)">下载</el-button>
                  <el-button type="primary" link @click="handleRestoreBackup(scope.row)">恢复</el-button>
                  <el-button type="danger" link @click="handleDeleteBackup(scope.row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </div>
      </el-tab-pane>
      
      <el-tab-pane label="系统清理" name="cleanup">
        <div class="tab-content">
          <el-card shadow="hover" class="cleanup-card">
            <template #header>
              <div class="card-header">
                <span>系统清理配置</span>
                <el-button type="primary" size="small" @click="handleSaveCleanupConfig">保存配置</el-button>
              </div>
            </template>
            <el-form :model="cleanupConfig" label-width="180px" label-position="left">
              <el-divider content-position="left">历史数据清理</el-divider>
              <el-form-item label="自动清理历史数据">
                <el-switch v-model="cleanupConfig.autoCleanup"></el-switch>
              </el-form-item>
              <el-form-item label="清理频率" v-if="cleanupConfig.autoCleanup">
                <el-select v-model="cleanupConfig.frequency" placeholder="选择清理频率">
                  <el-option label="每周" value="weekly"></el-option>
                  <el-option label="每月" value="monthly"></el-option>
                  <el-option label="每季度" value="quarterly"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="保留测试报告天数">
                <el-input-number v-model="cleanupConfig.keepReportDays" :min="30" :max="3650"></el-input-number>
              </el-form-item>
              <el-form-item label="保留操作日志天数">
                <el-input-number v-model="cleanupConfig.keepLogDays" :min="30" :max="1825"></el-input-number>
              </el-form-item>
              <el-form-item label="保留系统监控数据天数">
                <el-input-number v-model="cleanupConfig.keepMonitoringDays" :min="7" :max="365"></el-input-number>
              </el-form-item>
              
              <el-divider content-position="left">临时文件清理</el-divider>
              <el-form-item label="自动清理临时文件">
                <el-switch v-model="cleanupConfig.autoCleanupTemp"></el-switch>
              </el-form-item>
              <el-form-item label="清理频率" v-if="cleanupConfig.autoCleanupTemp">
                <el-select v-model="cleanupConfig.tempFrequency" placeholder="选择清理频率">
                  <el-option label="每天" value="daily"></el-option>
                  <el-option label="每周" value="weekly"></el-option>
                  <el-option label="每月" value="monthly"></el-option>
                </el-select>
              </el-form-item>
              
              <el-divider content-position="left">报告归档设置</el-divider>
              <el-form-item label="自动归档报告">
                <el-switch v-model="cleanupConfig.autoArchive"></el-switch>
              </el-form-item>
              <el-form-item label="归档阈值(天)" v-if="cleanupConfig.autoArchive">
                <el-input-number v-model="cleanupConfig.archiveDays" :min="30" :max="365"></el-input-number>
              </el-form-item>
              <el-form-item label="归档存储位置" v-if="cleanupConfig.autoArchive">
                <el-input v-model="cleanupConfig.archiveLocation" placeholder="输入归档存储路径"></el-input>
              </el-form-item>
            </el-form>
            
            <el-divider content-position="left">手动清理</el-divider>
            <el-row :gutter="20">
              <el-col :span="8">
                <el-card shadow="hover" class="manual-cleanup-card">
                  <template #header>
                    <div class="manual-cleanup-header">
                      <span>清理临时文件</span>
                    </div>
                  </template>
                  <div class="manual-cleanup-content">
                    <p>清理系统生成的临时文件，包括临时上传文件、缓存文件等。</p>
                    <el-button type="primary" @click="handleCleanupTemp">立即清理</el-button>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="8">
                <el-card shadow="hover" class="manual-cleanup-card">
                  <template #header>
                    <div class="manual-cleanup-header">
                      <span>清理历史日志</span>
                    </div>
                  </template>
                  <div class="manual-cleanup-content">
                    <p>清理系统日志，可以选择保留最近日期的日志。</p>
                    <el-button type="primary" @click="handleCleanupLogs">立即清理</el-button>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="8">
                <el-card shadow="hover" class="manual-cleanup-card">
                  <template #header>
                    <div class="manual-cleanup-header">
                      <span>归档历史报告</span>
                    </div>
                  </template>
                  <div class="manual-cleanup-content">
                    <p>将历史测试报告归档到指定位置，释放系统空间。</p>
                    <el-button type="primary" @click="handleArchiveReports">立即归档</el-button>
                  </div>
                </el-card>
              </el-col>
            </el-row>
          </el-card>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';

// 选项卡状态
const activeTab = ref('status');
const loading = ref(false);

// 分页数据
const total = ref(0);
const pageSize = ref(10);
const currentPage = ref(1);

// 系统状态监控数据
const statusCards = ref([
  {
    title: '服务器状态',
    status: '正常',
    metrics: [
      { name: 'CPU使用率', value: '32%' },
      { name: '内存使用率', value: '45%' },
      { name: '磁盘使用率', value: '58%' },
      { name: '运行时间', value: '15天8小时' }
    ]
  },
  {
    title: '数据库状态',
    status: '警告',
    metrics: [
      { name: '连接数', value: '28/50' },
      { name: '查询响应时间', value: '150ms' },
      { name: '磁盘使用率', value: '78%' },
      { name: '缓存命中率', value: '92%' }
    ]
  },
  {
    title: '测试执行引擎状态',
    status: '正常',
    metrics: [
      { name: '当前任务数', value: '5' },
      { name: '队列等待任务', value: '2' },
      { name: '可用执行器', value: '8/10' },
      { name: '平均执行时间', value: '45秒' }
    ]
  }
]);

const chartTimeRange = ref('hour');

// 系统日志查询参数
const logQuery = ref({
  type: 'operation',
  level: ['info', 'warn', 'error'],
  timeRange: [new Date(Date.now() - 24 * 60 * 60 * 1000), new Date()],
  keyword: ''
});

// 系统日志数据
const logList = ref([
  { 
    id: 1, 
    timestamp: '2023-07-10 10:15:23', 
    level: 'INFO', 
    module: '用户管理', 
    content: '用户admin登录系统',
    user: 'admin',
    ip: '192.168.1.100'
  },
  { 
    id: 2, 
    timestamp: '2023-07-10 10:30:45', 
    level: 'ERROR', 
    module: '测试执行', 
    content: '测试任务执行失败: 连接超时',
    user: 'tester1',
    ip: '192.168.1.101'
  },
  { 
    id: 3, 
    timestamp: '2023-07-10 11:05:12', 
    level: 'WARN', 
    module: '数据库', 
    content: '数据库连接池接近上限',
    user: 'system',
    ip: '127.0.0.1'
  }
]);

// 数据备份配置
const backupConfig = ref({
  autoBackup: true,
  frequency: 'daily',
  time: new Date(2000, 0, 1, 2, 0), // 02:00
  keepCount: 7,
  location: '/data/backups'
});

// 备份列表数据
const backupList = ref([
  { 
    id: 1, 
    filename: 'verityx_backup_20230710_020000.sql', 
    size: '256 MB', 
    createTime: '2023-07-10 02:00:00', 
    type: '自动备份' 
  },
  { 
    id: 2, 
    filename: 'verityx_backup_20230709_020000.sql', 
    size: '252 MB', 
    createTime: '2023-07-09 02:00:00', 
    type: '自动备份' 
  },
  { 
    id: 3, 
    filename: 'verityx_backup_20230708_143025.sql', 
    size: '255 MB', 
    createTime: '2023-07-08 14:30:25', 
    type: '手动备份' 
  }
]);

// 系统清理配置
const cleanupConfig = ref({
  autoCleanup: true,
  frequency: 'monthly',
  keepReportDays: 365,
  keepLogDays: 180,
  keepMonitoringDays: 30,
  autoCleanupTemp: true,
  tempFrequency: 'daily',
  autoArchive: true,
  archiveDays: 90,
  archiveLocation: '/data/archives'
});

// 系统状态监控方法

// 系统日志方法
const getLogLevelType = (level) => {
  const typeMap = {
    'INFO': 'info',
    'WARN': 'warning',
    'ERROR': 'danger',
    'DEBUG': ''
  };
  return typeMap[level] || '';
};

const handleQueryLogs = () => {
  loading.value = true;
  console.log('查询日志', logQuery.value);
  // 模拟加载
  setTimeout(() => {
    loading.value = false;
  }, 500);
};

const handleResetLogQuery = () => {
  logQuery.value = {
    type: 'operation',
    level: ['info', 'warn', 'error'],
    timeRange: [new Date(Date.now() - 24 * 60 * 60 * 1000), new Date()],
    keyword: ''
  };
};

const handleViewLogDetail = (log) => {
  console.log('查看日志详情', log);
};

const handleExportLogs = () => {
  console.log('导出日志');
};

// 数据备份与恢复方法
const handleSaveBackupConfig = () => {
  console.log('保存备份配置', backupConfig.value);
};

const handleCreateBackup = () => {
  console.log('创建备份');
};

const handleDownloadBackup = (backup) => {
  console.log('下载备份', backup);
};

const handleRestoreBackup = (backup) => {
  console.log('恢复备份', backup);
};

const handleDeleteBackup = (backup) => {
  console.log('删除备份', backup);
};

// 系统清理方法
const handleSaveCleanupConfig = () => {
  console.log('保存清理配置', cleanupConfig.value);
};

const handleCleanupTemp = () => {
  console.log('清理临时文件');
};

const handleCleanupLogs = () => {
  console.log('清理历史日志');
};

const handleArchiveReports = () => {
  console.log('归档历史报告');
};

// 分页方法
const handleSizeChange = (val) => {
  pageSize.value = val;
  // 获取数据
};

const handleCurrentChange = (val) => {
  currentPage.value = val;
  // 获取数据
};

onMounted(() => {
  // 初始化加载数据
});
</script>

<style scoped>
.system-monitor-container {
  padding: 20px;
}

.status-card {
  margin-bottom: 20px;
  height: 200px;
}

.status-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.status-content {
  padding: 10px 0;
}

.metric {
  margin-bottom: 10px;
  display: flex;
  justify-content: space-between;
}

.metric-name {
  font-weight: bold;
}

.status-chart-card {
  margin-bottom: 20px;
}

.chart-container {
  height: 400px;
}

.chart-placeholder {
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f5f7fa;
  border-radius: 4px;
  color: #909399;
  font-size: 16px;
}

.log-query-form {
  margin-bottom: 20px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.backup-card, .cleanup-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.manual-cleanup-card {
  height: 180px;
}

.manual-cleanup-header {
  text-align: center;
}

.manual-cleanup-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  height: 100px;
}

.tab-content {
  margin-top: 20px;
}

.monitor-tabs {
  min-height: 500px;
}
</style> 