<template>
  <div class="report-step-details">
    <el-table
      :data="stepsList"
      :border="true"
      style="width: 100%"
      row-key="id"
      :row-class-name="getRowClassName"
    >
      <el-table-column type="expand">
        <template #default="props">
          <div class="step-expand-detail">
            <el-row>
              <el-col :span="12">
                <div class="screenshot-container" v-if="props.row.screenshot">
                  <div class="screenshot-title">操作截图</div>
                  <el-image 
                    :src="props.row.screenshot" 
                    fit="contain"
                    :preview-src-list="[props.row.screenshot]"
                  />
                </div>
              </el-col>
              <el-col :span="12" v-if="props.row.status === 'failed'">
                <div class="failure-details">
                  <div class="failure-title">失败详情</div>
                  <el-descriptions :column="1" border>
                    <el-descriptions-item label="错误类型">{{ props.row.errorType || '未知错误' }}</el-descriptions-item>
                    <el-descriptions-item label="错误信息">{{ props.row.errorMessage }}</el-descriptions-item>
                    <el-descriptions-item label="期望值" v-if="props.row.expectedValue">{{ props.row.expectedValue }}</el-descriptions-item>
                    <el-descriptions-item label="实际值" v-if="props.row.actualValue">{{ props.row.actualValue }}</el-descriptions-item>
                  </el-descriptions>
                </div>
              </el-col>
            </el-row>
            <el-row v-if="props.row.logs && props.row.logs.length > 0">
              <el-col :span="24">
                <div class="step-logs">
                  <div class="logs-title">执行日志</div>
                  <el-timeline>
                    <el-timeline-item
                      v-for="(log, index) in props.row.logs"
                      :key="index"
                      :type="getLogTypeIcon(log.level)"
                      :color="getLogColor(log.level)"
                      :timestamp="log.timestamp"
                      placement="top"
                    >
                      <div class="log-content" :class="`log-${log.level.toLowerCase()}`">
                        {{ log.message }}
                      </div>
                    </el-timeline-item>
                  </el-timeline>
                </div>
              </el-col>
            </el-row>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="stepNumber" label="步骤" width="70" />
      <el-table-column prop="description" label="步骤描述" min-width="200" />
      <el-table-column prop="action" label="操作" min-width="120" />
      <el-table-column prop="target" label="目标元素" min-width="150" />
      <el-table-column prop="value" label="参数值" min-width="120" />
      <el-table-column prop="duration" label="执行时间(ms)" width="120" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)" :effect="row.status === 'skipped' ? 'plain' : 'dark'">
            {{ getStatusText(row.status) }}
          </el-tag>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { defineProps } from 'vue';

const props = defineProps({
  stepsList: {
    type: Array,
    required: true,
    default: () => []
  }
});

// 根据步骤状态返回对应的状态标签类型
const getStatusType = (status) => {
  const statusMap = {
    'passed': 'success',
    'failed': 'danger',
    'skipped': 'info',
    'running': 'warning'
  };
  return statusMap[status] || 'info';
};

// 步骤状态文本转换
const getStatusText = (status) => {
  const statusTextMap = {
    'passed': '通过',
    'failed': '失败',
    'skipped': '跳过',
    'running': '执行中'
  };
  return statusTextMap[status] || status;
};

// 获取行的类名，用于设置不同状态的行样式
const getRowClassName = ({ row }) => {
  return `step-row-${row.status}`;
};

// 根据日志级别获取图标类型
const getLogTypeIcon = (level) => {
  const iconMap = {
    'ERROR': 'danger',
    'WARNING': 'warning',
    'INFO': 'primary',
    'DEBUG': 'info'
  };
  return iconMap[level] || 'info';
};

// 根据日志级别获取颜色
const getLogColor = (level) => {
  const colorMap = {
    'ERROR': '#F56C6C',
    'WARNING': '#E6A23C',
    'INFO': '#409EFF',
    'DEBUG': '#909399'
  };
  return colorMap[level] || '#909399';
};
</script>

<style scoped>
.step-expand-detail {
  padding: 20px;
  background-color: #f9f9f9;
}

.screenshot-container, .failure-details, .step-logs {
  margin-bottom: 20px;
  border-radius: 4px;
  overflow: hidden;
  border: 1px solid #ebeef5;
  background-color: white;
}

.screenshot-title, .failure-title, .logs-title {
  padding: 8px 16px;
  background-color: #f2f6fc;
  font-weight: bold;
  color: #606266;
  border-bottom: 1px solid #ebeef5;
}

.screenshot-container :deep(img) {
  max-width: 100%;
  max-height: 300px;
  display: block;
  margin: 10px auto;
}

.failure-details {
  height: 100%;
}

.step-logs {
  padding-bottom: 10px;
}

.log-content {
  padding: 5px 10px;
  border-radius: 4px;
  margin-bottom: 5px;
  word-break: break-word;
  font-family: monospace;
}

.log-error {
  background-color: rgba(245, 108, 108, 0.1);
}

.log-warning {
  background-color: rgba(230, 162, 60, 0.1);
}

.log-info {
  background-color: rgba(64, 158, 255, 0.1);
}

.log-debug {
  background-color: rgba(144, 147, 153, 0.1);
}

/* 行状态样式 */
:deep(.step-row-failed) {
  background-color: rgba(245, 108, 108, 0.1);
}

:deep(.step-row-passed) {
  background-color: rgba(103, 194, 58, 0.1);
}

:deep(.step-row-skipped) {
  background-color: rgba(144, 147, 153, 0.1);
  color: #909399;
}

:deep(.step-row-running) {
  background-color: rgba(230, 162, 60, 0.1);
}
</style> 