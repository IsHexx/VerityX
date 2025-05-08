<template>
  <div class="system-monitor-container">
    <el-tabs v-model="activeTab" class="monitor-tabs" @tab-change="handleTabChange">
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
                <el-radio-group v-model="chartTimeRange" size="small" @change="handleChartRangeChange">
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
                <el-select v-model="logQuery.logType" placeholder="选择日志类型" style="width: 150px;">
                  <el-option label="操作日志" value="operation"></el-option>
                  <el-option label="异常日志" value="error"></el-option>
                  <el-option label="性能日志" value="performance"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="日志级别">
                <el-select v-model="logQuery.levels" placeholder="选择日志级别" multiple collapse-tags>
                  <el-option label="INFO" value="INFO"></el-option>
                  <el-option label="WARN" value="WARN"></el-option>
                  <el-option label="ERROR" value="ERROR"></el-option>
                  <el-option label="DEBUG" value="DEBUG"></el-option>
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
import { ref, onMounted, computed } from 'vue';
import { SystemMonitorApi, SystemBackupApi } from '@/api/systemMonitorService';
import { ElMessage, ElMessageBox } from 'element-plus';

// 选项卡状态
const activeTab = ref('status');
const loading = ref(false);

// 分页数据
const total = ref(0);
const pageSize = ref(10);
const currentPage = ref(1);

// 系统状态监控数据
const statusCards = ref([]);
const systemStatus = ref({});

// 获取系统状态数据
const fetchSystemStatus = async () => {
  loading.value = true;
  try {
    const res = await SystemMonitorApi.getSystemStatus();
    if (res.code === 200 && res.data) {
      systemStatus.value = res.data;

      // 转换数据格式以适应UI显示
      statusCards.value = [
        {
          title: '服务器状态',
          status: res.data.server.status === 'NORMAL' ? '正常' : (res.data.server.status === 'WARNING' ? '警告' : '错误'),
          metrics: formatMetrics(res.data.server.metrics)
        },
        {
          title: '数据库状态',
          status: res.data.database.status === 'NORMAL' ? '正常' : (res.data.database.status === 'WARNING' ? '警告' : '错误'),
          metrics: formatMetrics(res.data.database.metrics)
        },
        {
          title: '测试执行引擎状态',
          status: res.data.testEngine.status === 'NORMAL' ? '正常' : (res.data.testEngine.status === 'WARNING' ? '警告' : '错误'),
          metrics: formatMetrics(res.data.testEngine.metrics)
        }
      ];
    } else {
      ElMessage.error(res.message || '获取系统状态失败');
    }
  } catch (error) {
    console.error('获取系统状态失败:', error);
    ElMessage.error('获取系统状态失败');
  } finally {
    loading.value = false;
  }
};

// 格式化指标数据
const formatMetrics = (metricsArray) => {
  return metricsArray.map(item => ({
    name: item.metricName,
    value: item.metricValue
  }));
};

const chartTimeRange = ref('hour');
const resourceTrendData = ref([]);

// 获取资源趋势数据
const fetchResourceTrend = async () => {
  loading.value = true;
  try {
    // 根据时间范围计算开始时间和结束时间
    const endTime = new Date();
    let startTime;

    switch (chartTimeRange.value) {
      case 'hour':
        startTime = new Date(endTime.getTime() - 60 * 60 * 1000); // 1小时前
        break;
      case 'day':
        startTime = new Date(endTime.getTime() - 24 * 60 * 60 * 1000); // 24小时前
        break;
      case 'week':
        startTime = new Date(endTime.getTime() - 7 * 24 * 60 * 60 * 1000); // 7天前
        break;
      default:
        startTime = new Date(endTime.getTime() - 24 * 60 * 60 * 1000); // 默认24小时
    }

    // 将日期格式转换为ISO但移除Z后缀
    const startTimeStr = startTime.toISOString().replace('Z', '');
    const endTimeStr = endTime.toISOString().replace('Z', '');

    const res = await SystemMonitorApi.getResourceTrend('CPU', startTimeStr, endTimeStr);
    if (res.code === 200 && res.data) {
      resourceTrendData.value = res.data;
      // 此处可以添加图表渲染逻辑
    } else {
      ElMessage.error(res.message || '获取资源趋势数据失败');
    }
  } catch (error) {
    console.error('获取资源趋势数据失败:', error);
    ElMessage.error('获取资源趋势数据失败');
  } finally {
    loading.value = false;
  }
};

// 监听图表时间范围变化
const handleChartRangeChange = () => {
  fetchResourceTrend();
};

// 系统日志查询参数
const logQuery = ref({
  logType: 'operation',
  levels: ['INFO', 'WARN', 'ERROR'],
  timeRange: [new Date(Date.now() - 24 * 60 * 60 * 1000), new Date()],
  keyword: ''
});

// 系统日志数据
const logList = ref([]);

// 获取系统日志
const fetchLogs = async () => {
  loading.value = true;
  try {
    // 准备查询参数
    const queryParams = {
      keyword: logQuery.value.keyword || ''
    };

    // 添加时间范围
    if (logQuery.value.timeRange && logQuery.value.timeRange.length === 2) {
      // 不再使用日期格式化函数，将日期直接作为字符串参数传递
      queryParams.startTime = logQuery.value.timeRange[0].toISOString().replace('Z', '');
      queryParams.endTime = logQuery.value.timeRange[1].toISOString().replace('Z', '');
    }

    // 处理日志级别数组参数 - 直接传递数组，不转换为字符串
    if (logQuery.value.levels && logQuery.value.levels.length > 0) {
      queryParams.levels = logQuery.value.levels;
    }

    let res;
    if (logQuery.value.logType === 'operation') {
      res = await SystemMonitorApi.getOperationLogs(currentPage.value, pageSize.value, queryParams);
    } else if (logQuery.value.logType === 'error') {
      res = await SystemMonitorApi.getErrorLogs(currentPage.value, pageSize.value, queryParams);
    } else if (logQuery.value.logType === 'performance') {
      res = await SystemMonitorApi.getPerformanceLogs(currentPage.value, pageSize.value, queryParams);
    }

    if (res && res.code === 200 && res.data) {
      // 转换数据格式以适应UI显示
      const apiData = res.data;
      logList.value = apiData.records.map(log => {
        if (logQuery.value.logType === 'operation') {
          return {
            id: log.id,
            timestamp: formatDateTime(log.createTime || log.operationTime),
            level: log.logLevel || (log.status === 1 ? 'INFO' : 'ERROR'),
            module: log.module || log.operation,
            content: log.content || log.method,
            user: log.username,
            ip: log.ipAddress || log.requestIp
          };
        } else if (logQuery.value.logType === 'error') {
          return {
            id: log.id,
            timestamp: formatDateTime(log.errorTime),
            level: 'ERROR',
            module: log.requestMethod,
            content: log.errorMsg,
            user: log.username,
            ip: log.requestIp
          };
        } else { // performance logs
          return {
            id: log.id,
            timestamp: formatDateTime(log.recordTime),
            level: log.logLevel || 'INFO',
            module: log.resourceType,
            content: `${log.metricName}: ${log.metricValue}`,
            user: log.username || '-',
            ip: log.ipAddress || '-'
          };
        }
      });

      total.value = apiData.total;
    } else {
      ElMessage.error(res.message || '获取日志失败');
    }
  } catch (error) {
    console.error('获取日志失败:', error);
    ElMessage.error('获取日志失败');
  } finally {
    loading.value = false;
  }
};

// 格式化日期时间
const formatDateTime = (dateTimeStr) => {
  if (!dateTimeStr) return '';
  const date = new Date(dateTimeStr);
  return date.toLocaleString();
};

// 数据备份配置
const backupConfig = ref({
  autoBackup: true,
  frequency: 'daily',
  time: new Date(2000, 0, 1, 2, 0), // 02:00
  keepCount: 7,
  location: '/data/backups'
});

// 备份列表数据
const backupList = ref([]);

// 获取备份列表
const fetchBackupList = async () => {
  loading.value = true;
  try {
    const res = await SystemBackupApi.getBackupList(currentPage.value, pageSize.value);
    if (res.code === 200 && res.data) {
      // 转换数据格式以适应UI显示
      const apiData = res.data;
      backupList.value = apiData.records.map(backup => ({
        id: backup.id,
        filename: backup.fileName,
        size: formatFileSize(backup.fileSize),
        createTime: formatDateTime(backup.createdAt),
        type: backup.backupType === 'AUTO' ? '自动备份' : '手动备份'
      }));

      total.value = apiData.total;
    } else {
      ElMessage.error(res.message || '获取备份列表失败');
    }
  } catch (error) {
    console.error('获取备份列表失败:', error);
    ElMessage.error('获取备份列表失败');
  } finally {
    loading.value = false;
  }
};

// 格式化文件大小
const formatFileSize = (bytes) => {
  if (bytes === 0) return '0 B';
  const k = 1024;
  const sizes = ['B', 'KB', 'MB', 'GB', 'TB'];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
};

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
  currentPage.value = 1; // 重置为第一页
  fetchLogs();
};

const handleResetLogQuery = () => {
  logQuery.value = {
    logType: 'operation',
    levels: ['INFO', 'WARN', 'ERROR'],
    timeRange: [new Date(Date.now() - 24 * 60 * 60 * 1000), new Date()],
    keyword: ''
  };
};

const handleViewLogDetail = async (log) => {
  loading.value = true;
  try {
    let res;
    if (logQuery.value.logType === 'operation') {
      res = await SystemMonitorApi.getOperationLogDetail(log.id);
    } else if (logQuery.value.logType === 'error') {
      res = await SystemMonitorApi.getErrorLogDetail(log.id);
    } else if (logQuery.value.logType === 'performance') {
      res = await SystemMonitorApi.getPerformanceLogDetail(log.id);
    }

    if (res && res.code === 200 && res.data) {
      // 可以使用Element UI的对话框显示详情
      const logDetail = res.data;

      // 这里可以根据返回的具体数据格式做处理
      let detailContent = '';
      if (logQuery.value.logType === 'operation') {
        detailContent = `
          <div>
            <p><strong>ID:</strong> ${logDetail.id}</p>
            <p><strong>操作用户:</strong> ${logDetail.username}</p>
            <p><strong>操作行为:</strong> ${logDetail.operation}</p>
            <p><strong>请求方法:</strong> ${logDetail.method}</p>
            <p><strong>请求URL:</strong> ${logDetail.requestUrl}</p>
            <p><strong>请求参数:</strong> ${logDetail.requestParams || '-'}</p>
            <p><strong>操作IP:</strong> ${logDetail.requestIp}</p>
            <p><strong>操作状态:</strong> ${logDetail.status === 1 ? '成功' : '失败'}</p>
            <p><strong>操作时间:</strong> ${formatDateTime(logDetail.operationTime)}</p>
            ${logDetail.errorMsg ? `<p><strong>错误信息:</strong> ${logDetail.errorMsg}</p>` : ''}
          </div>
        `;
      } else if (logQuery.value.logType === 'error') {
        detailContent = `
          <div>
            <p><strong>ID:</strong> ${logDetail.id}</p>
            <p><strong>操作用户:</strong> ${logDetail.username}</p>
            <p><strong>请求URL:</strong> ${logDetail.requestUrl}</p>
            <p><strong>请求方法:</strong> ${logDetail.requestMethod}</p>
            <p><strong>请求参数:</strong> ${logDetail.requestParams || '-'}</p>
            <p><strong>操作IP:</strong> ${logDetail.requestIp}</p>
            <p><strong>错误信息:</strong> ${logDetail.errorMsg}</p>
            <p><strong>异常堆栈:</strong> ${logDetail.stackTrace}</p>
            <p><strong>异常时间:</strong> ${formatDateTime(logDetail.errorTime)}</p>
          </div>
        `;
      } else { // performance logs
        detailContent = `
          <div>
            <p><strong>ID:</strong> ${logDetail.id}</p>
            <p><strong>资源类型:</strong> ${logDetail.resourceType}</p>
            <p><strong>指标名称:</strong> ${logDetail.metricName}</p>
            <p><strong>指标值:</strong> ${logDetail.metricValue}</p>
            <p><strong>阈值:</strong> ${logDetail.threshold || '-'}</p>
            <p><strong>IP地址:</strong> ${logDetail.ipAddress || '-'}</p>
            <p><strong>记录时间:</strong> ${formatDateTime(logDetail.recordTime)}</p>
            <p><strong>备注:</strong> ${logDetail.remark || '-'}</p>
          </div>
        `;
      }

      ElMessageBox.alert(detailContent, '日志详情', {
        dangerouslyUseHTMLString: true,
        confirmButtonText: '关闭'
      });
    } else {
      ElMessage.error(res.message || '获取日志详情失败');
    }
  } catch (error) {
    console.error('获取日志详情失败:', error);
    ElMessage.error('获取日志详情失败');
  } finally {
    loading.value = false;
  }
};

const handleExportLogs = async () => {
  loading.value = true;
  try {
    // 准备导出参数
    const exportParams = {
      logType: logQuery.value.logType,
      levels: logQuery.value.levels,
      keyword: logQuery.value.keyword
    };

    // 添加时间范围
    if (logQuery.value.timeRange && logQuery.value.timeRange.length === 2) {
      exportParams.startTime = logQuery.value.timeRange[0].toISOString();
      exportParams.endTime = logQuery.value.timeRange[1].toISOString();
    }

    let res;
    if (logQuery.value.logType === 'operation') {
      res = await SystemMonitorApi.exportOperationLog(exportParams);
    } else if (logQuery.value.logType === 'error') {
      res = await SystemMonitorApi.exportErrorLog(exportParams);
    } else if (logQuery.value.logType === 'performance') {
      res = await SystemMonitorApi.exportPerformanceLog(exportParams);
    }

    if (res.code === 200 && res.data) {
      // 假设返回的是下载链接
      const downloadLink = res.data;
      const a = document.createElement('a');
      a.href = downloadLink;
      a.download = `${logQuery.value.logType}_logs_${new Date().getTime()}.xlsx`;
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);

      ElMessage.success('导出成功');
    } else {
      ElMessage.error(res.message || '导出日志失败');
    }
  } catch (error) {
    console.error('导出日志失败:', error);
    ElMessage.error('导出日志失败');
  } finally {
    loading.value = false;
  }
};

// 数据备份与恢复方法
const handleSaveBackupConfig = () => {
  ElMessage.success('保存备份配置成功');
};

const handleCreateBackup = async () => {
  try {
    // 打开对话框让用户输入备份名称
    ElMessageBox.prompt('请输入备份名称', '创建备份', {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      inputPattern: /^.{1,100}$/,
      inputErrorMessage: '备份名称不能为空且不能超过100个字符'
    }).then(async ({ value }) => {
      loading.value = true;

      const backupRequest = {
        backupName: value,
        backupType: 'MANUAL',
        remark: '手动创建的备份'
      };

      const res = await SystemBackupApi.createBackup(backupRequest);
      if (res.code === 200) {
        ElMessage.success('创建备份成功');
        fetchBackupList(); // 刷新列表
      } else {
        ElMessage.error(res.message || '创建备份失败');
      }
    }).catch(() => {
      // 用户取消，不做任何处理
    });
  } catch (error) {
    console.error('创建备份失败:', error);
    ElMessage.error('创建备份失败');
  } finally {
    loading.value = false;
  }
};

const handleDownloadBackup = (backup) => {
  const downloadUrl = SystemBackupApi.downloadBackup(backup.id);
  window.open(downloadUrl, '_blank');
};

const handleRestoreBackup = async (backup) => {
  try {
    // 确认是否恢复
    await ElMessageBox.confirm(
      '恢复备份将覆盖当前数据，确定要继续吗？',
      '警告',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    );

    loading.value = true;
    const res = await SystemBackupApi.restoreBackup(backup.id);
    if (res.code === 200) {
      ElMessage.success('恢复备份成功');
    } else {
      ElMessage.error(res.message || '恢复备份失败');
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('恢复备份失败:', error);
      ElMessage.error('恢复备份失败');
    }
  } finally {
    loading.value = false;
  }
};

const handleDeleteBackup = async (backup) => {
  try {
    // 确认是否删除
    await ElMessageBox.confirm(
      '确定要删除该备份吗？',
      '警告',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    );

    loading.value = true;
    const res = await SystemBackupApi.deleteBackup(backup.id);
    if (res.code === 200) {
      ElMessage.success('删除备份成功');
      fetchBackupList(); // 刷新列表
    } else {
      ElMessage.error(res.message || '删除备份失败');
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除备份失败:', error);
      ElMessage.error('删除备份失败');
    }
  } finally {
    loading.value = false;
  }
};

// 系统清理方法
const handleSaveCleanupConfig = () => {
  ElMessage.success('保存清理配置成功');
};

const handleCleanupTemp = async () => {
  try {
    // 确认是否清理
    await ElMessageBox.confirm(
      '确定要清理临时文件吗？',
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }
    );

    loading.value = true;
    const res = await SystemMonitorApi.cleanupSystem({
      cleanupType: 'temp'
    });
    if (res.code === 200) {
      ElMessage.success('清理临时文件成功');
    } else {
      ElMessage.error(res.message || '清理临时文件失败');
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('清理临时文件失败:', error);
      ElMessage.error('清理临时文件失败');
    }
  } finally {
    loading.value = false;
  }
};

const handleCleanupLogs = async () => {
  try {
    // 让用户输入保留天数
    const { value } = await ElMessageBox.prompt(
      '请输入要保留的日志天数',
      '清理历史日志',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /^[1-9]\d*$/,
        inputErrorMessage: '请输入正整数',
        inputValue: cleanupConfig.value.keepLogDays.toString()
      }
    );

    loading.value = true;
    const res = await SystemMonitorApi.cleanupSystem({
      cleanupType: 'log',
      keepDays: parseInt(value)
    });
    if (res.code === 200) {
      ElMessage.success('清理历史日志成功');
    } else {
      ElMessage.error(res.message || '清理历史日志失败');
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('清理历史日志失败:', error);
      ElMessage.error('清理历史日志失败');
    }
  } finally {
    loading.value = false;
  }
};

const handleArchiveReports = async () => {
  try {
    // 让用户输入保留天数和归档路径
    const { value: keepDays } = await ElMessageBox.prompt(
      '请输入归档阈值天数（超过该天数的报告将被归档）',
      '归档历史报告',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /^[1-9]\d*$/,
        inputErrorMessage: '请输入正整数',
        inputValue: cleanupConfig.value.archiveDays.toString()
      }
    );

    loading.value = true;
    const res = await SystemMonitorApi.cleanupSystem({
      cleanupType: 'archive',
      keepDays: parseInt(keepDays),
      archivePath: cleanupConfig.value.archiveLocation
    });
    if (res.code === 200) {
      ElMessage.success('归档历史报告成功');
    } else {
      ElMessage.error(res.message || '归档历史报告失败');
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('归档历史报告失败:', error);
      ElMessage.error('归档历史报告失败');
    }
  } finally {
    loading.value = false;
  }
};

// 分页方法
const handleSizeChange = (val) => {
  pageSize.value = val;
  if (activeTab.value === 'logs') {
    fetchLogs();
  } else if (activeTab.value === 'backup') {
    fetchBackupList();
  }
};

const handleCurrentChange = (val) => {
  currentPage.value = val;
  if (activeTab.value === 'logs') {
    fetchLogs();
  } else if (activeTab.value === 'backup') {
    fetchBackupList();
  }
};

// 监听选项卡切换
const handleTabChange = () => {
  // 根据当前选项卡加载相应数据
  if (activeTab.value === 'status') {
    fetchSystemStatus();
    fetchResourceTrend();
  } else if (activeTab.value === 'logs') {
    fetchLogs();
  } else if (activeTab.value === 'backup') {
    fetchBackupList();
  }
};

onMounted(() => {
  // 初始化加载数据
  handleTabChange();
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