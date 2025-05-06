<template>
  <el-dialog
    v-model="dialogVisible"
    title="执行日志"
    width="800px"
    :close-on-click-modal="false"
  >
    <div class="log-header">
      <div class="execution-info">
        <p><strong>执行ID:</strong> {{ execution.id }}</p>
        <p><strong>执行名称:</strong> {{ execution.name }}</p>
        <p>
          <strong>状态:</strong>
          <el-tag
            :type="getStatusTagType(execution.status)"
            effect="light"
            size="small"
          >
            {{ execution.status }}
          </el-tag>
        </p>
      </div>
      <div class="log-actions">
        <el-button plain @click="refreshLogs">刷新</el-button>
        <el-button plain @click="downloadLogs">下载日志</el-button>
        <el-select v-model="logLevel" placeholder="日志级别" size="small">
          <el-option label="全部" value="ALL" />
          <el-option label="INFO" value="INFO" />
          <el-option label="WARN" value="WARN" />
          <el-option label="ERROR" value="ERROR" />
          <el-option label="DEBUG" value="DEBUG" />
        </el-select>
      </div>
    </div>
    
    <div class="log-container" v-loading="loading">
      <div v-if="logs.length > 0" class="log-content">
        <div v-for="(log, index) in filteredLogs" :key="index" :class="['log-item', `log-${log.level.toLowerCase()}`]">
          <span class="log-time">{{ log.time }}</span>
          <span class="log-level">{{ log.level }}</span>
          <span class="log-message">{{ log.message }}</span>
        </div>
      </div>
      <div v-else class="no-logs">
        <el-empty description="暂无日志数据" />
      </div>
    </div>
    
    <div v-if="execution.status === '执行中'" class="auto-refresh">
      <el-checkbox v-model="autoRefresh">自动刷新</el-checkbox>
      <span class="refresh-hint" v-if="autoRefresh">每 {{ refreshInterval }} 秒刷新一次</span>
    </div>
    
    <div class="dialog-footer" style="margin-top: 20px; text-align: right;">
      <el-button @click="dialogVisible = false">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, defineProps, defineEmits, watch, computed, onMounted, onUnmounted } from 'vue';
import { ElMessage } from 'element-plus';
import { UiTestExecutionApi } from '@/api/uiTestExecutionService';

const props = defineProps({
  visible: Boolean,
  execution: {
    type: Object,
    default: () => ({})
  }
});

const emit = defineEmits(['update:visible']);

// 对话框可见性
const dialogVisible = ref(props.visible);

// 日志数据
const logs = ref([]);
const loading = ref(false);
const logLevel = ref('ALL');
const autoRefresh = ref(false);
const refreshInterval = ref(5);
let refreshTimer = null;

// 监听对话框可见性变化
watch(() => props.visible, (newVal) => {
  dialogVisible.value = newVal;
  if (newVal) {
    fetchLogs();
    setupAutoRefresh();
  } else {
    clearInterval(refreshTimer);
  }
});

// 监听dialogVisible变化
watch(dialogVisible, (newVal) => {
  emit('update:visible', newVal);
  if (!newVal) {
    clearInterval(refreshTimer);
  }
});

// 监听自动刷新设置变化
watch(autoRefresh, (newVal) => {
  if (newVal) {
    setupAutoRefresh();
  } else {
    clearInterval(refreshTimer);
  }
});

// 过滤后的日志
const filteredLogs = computed(() => {
  if (logLevel.value === 'ALL') {
    return logs.value;
  }
  return logs.value.filter(log => log.level === logLevel.value);
});

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

// 获取日志
const fetchLogs = async () => {
  if (!props.execution || !props.execution.id) return;
  
  loading.value = true;
  try {
    const res = await UiTestExecutionApi.getExecutionLogs(props.execution.id);
    if (res.code === 200) {
      logs.value = res.data;
    } else {
      ElMessage.error(res.message || '获取执行日志失败');
    }
  } catch (error) {
    ElMessage.error("获取执行日志失败");
    console.error("获取执行日志失败:", error);
  } finally {
    loading.value = false;
  }
};

// 刷新日志
const refreshLogs = () => {
  fetchLogs();
};

// 下载日志
const downloadLogs = () => {
  if (logs.value.length === 0) {
    ElMessage.warning('没有可下载的日志');
    return;
  }
  
  try {
    // 构造日志文本
    const logText = logs.value.map(log => `${log.time} [${log.level}] ${log.message}`).join('\n');
    
    // 创建Blob
    const blob = new Blob([logText], { type: 'text/plain' });
    
    // 创建下载链接
    const link = document.createElement('a');
    link.href = URL.createObjectURL(blob);
    link.download = `execution_log_${props.execution.id}.txt`;
    
    // 模拟点击下载
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    
    ElMessage.success('日志下载成功');
  } catch (error) {
    ElMessage.error('日志下载失败');
    console.error('日志下载失败:', error);
  }
};

// 设置自动刷新
const setupAutoRefresh = () => {
  if (props.execution.status === '执行中' && autoRefresh.value) {
    clearInterval(refreshTimer);
    refreshTimer = setInterval(() => {
      fetchLogs();
    }, refreshInterval.value * 1000);
  }
};

// 组件销毁时清除定时器
onUnmounted(() => {
  clearInterval(refreshTimer);
});
</script>

<style scoped>
.log-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 15px;
}

.execution-info {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.log-actions {
  display: flex;
  gap: 10px;
  align-items: flex-start;
}

.log-container {
  height: 400px;
  overflow-y: auto;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  background-color: #f8f8f8;
  padding: 10px;
  font-family: 'Courier New', monospace;
  font-size: 14px;
}

.log-content {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.log-item {
  display: flex;
  padding: 3px 5px;
  border-radius: 2px;
}

.log-item:hover {
  background-color: rgba(0, 0, 0, 0.05);
}

.log-time {
  flex: 0 0 80px;
  color: #909399;
}

.log-level {
  flex: 0 0 60px;
  font-weight: bold;
}

.log-message {
  flex: 1;
  word-break: break-all;
}

.log-info .log-level {
  color: #409eff;
}

.log-debug .log-level {
  color: #909399;
}

.log-warn .log-level {
  color: #e6a23c;
}

.log-error .log-level {
  color: #f56c6c;
}

.auto-refresh {
  margin-top: 10px;
  display: flex;
  align-items: center;
  gap: 10px;
}

.refresh-hint {
  color: #909399;
  font-size: 12px;
}

.no-logs {
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style> 