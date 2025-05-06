<template>
  <el-dialog
    v-model="dialogVisible"
    title="执行详情"
    width="800px"
    :close-on-click-modal="false"
  >
    <div v-loading="loading" class="execution-detail">
      <div class="header-section">
        <div class="execution-header">
          <h3>{{ execution.name || '未命名执行' }}</h3>
          <el-tag
            :type="getStatusTagType(execution.status)"
            effect="light"
          >
            {{ execution.status }}
          </el-tag>
        </div>
        
        <div class="execution-info-grid">
          <div class="info-item">
            <label>执行ID</label>
            <span>{{ execution.id }}</span>
          </div>
          <div class="info-item">
            <label>执行类型</label>
            <span>{{ execution.type }}</span>
          </div>
          <div class="info-item">
            <label>目标名称</label>
            <span>{{ execution.targetName }}</span>
          </div>
          <div class="info-item">
            <label>执行环境</label>
            <span>{{ execution.environment }}</span>
          </div>
          <div class="info-item">
            <label>浏览器</label>
            <span>{{ execution.browser }}</span>
          </div>
          <div class="info-item">
            <label>浏览器尺寸</label>
            <span>{{ execution.browserSize }}</span>
          </div>
          <div class="info-item">
            <label>开始时间</label>
            <span>{{ execution.startTime }}</span>
          </div>
          <div class="info-item">
            <label>结束时间</label>
            <span>{{ execution.endTime || '-' }}</span>
          </div>
          <div class="info-item">
            <label>执行时长</label>
            <span>{{ execution.duration || '-' }}</span>
          </div>
          <div class="info-item">
            <label>执行人</label>
            <span>{{ execution.executedBy }}</span>
          </div>
        </div>
      </div>
      
      <el-divider />
      
      <div class="stats-section">
        <div class="execution-stats">
          <div class="stat-item success">
            <div class="stat-value">{{ execution.passedCount || 0 }}</div>
            <div class="stat-label">通过</div>
          </div>
          <div class="stat-item danger">
            <div class="stat-value">{{ execution.failedCount || 0 }}</div>
            <div class="stat-label">失败</div>
          </div>
          <div class="stat-item warning">
            <div class="stat-value">{{ execution.skippedCount || 0 }}</div>
            <div class="stat-label">跳过</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">{{ execution.totalCount || 0 }}</div>
            <div class="stat-label">总计</div>
          </div>
        </div>
        
        <div class="execution-progress">
          <el-progress 
            :percentage="execution.progress || 0" 
            :status="getProgressStatus(execution.status)"
          />
          <div class="progress-text">
            {{ execution.status === '执行中' ? '执行中...' : '执行完成' }}
            {{ execution.progress || 0 }}%
          </div>
        </div>
      </div>
      
      <el-divider />
      
      <div class="steps-section">
        <h4>执行步骤</h4>
        <el-table :data="steps" style="width: 100%" stripe>
          <el-table-column prop="step" label="步骤" width="60" />
          <el-table-column prop="description" label="描述" min-width="180" />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="scope">
              <el-tag
                :type="getStatusTagType(scope.row.status)"
                effect="light"
                size="small"
              >
                {{ scope.row.status }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="duration" label="耗时" width="100" />
          <el-table-column label="操作" width="120">
            <template #default="scope">
              <el-button 
                v-if="scope.row.hasScreenshot" 
                link 
                type="primary" 
                size="small" 
                @click="viewStepScreenshot(scope.row)"
              >
                查看截图
              </el-button>
              <span v-else class="no-action">-</span>
            </template>
          </el-table-column>
        </el-table>
      </div>
      
      <el-divider />
      
      <div v-if="execution.failedCount > 0" class="failure-section">
        <h4>失败详情</h4>
        <el-collapse>
          <el-collapse-item 
            v-for="(failure, index) in failures" 
            :key="index" 
            :title="`步骤 ${failure.step}: ${failure.description}`"
            :name="index"
          >
            <div class="failure-details">
              <div class="failure-message">
                <strong>错误信息:</strong> {{ failure.message }}
              </div>
              <div class="failure-trace" v-if="failure.trace">
                <strong>错误堆栈:</strong>
                <pre>{{ failure.trace }}</pre>
              </div>
              <div class="failure-screenshot" v-if="failure.screenshot">
                <strong>错误截图:</strong>
                <div class="screenshot-container">
                  <img :src="failure.screenshot" alt="错误截图" />
                </div>
              </div>
            </div>
          </el-collapse-item>
        </el-collapse>
      </div>
      
      <div class="execution-params" v-if="execution.params && execution.params.length > 0">
        <h4>执行参数</h4>
        <el-table :data="execution.params" style="width: 100%" border>
          <el-table-column prop="name" label="参数名" min-width="120" />
          <el-table-column prop="value" label="参数值" min-width="180" />
        </el-table>
      </div>
    </div>
    
    <div class="dialog-footer" style="margin-top: 20px; text-align: right;">
      <el-button @click="viewExecutionLogs">查看日志</el-button>
      <el-button @click="dialogVisible = false">关闭</el-button>
      <el-button type="primary" @click="rerunExecution" v-if="execution.status !== '执行中' && execution.status !== '排队中'">
        重新执行
      </el-button>
    </div>
    
    <!-- 截图查看对话框 -->
    <el-dialog v-model="screenshotVisible" title="步骤截图" width="800px" append-to-body>
      <div class="screenshot-dialog-content">
        <img :src="currentScreenshot" alt="截图" class="step-screenshot" />
      </div>
    </el-dialog>
  </el-dialog>
</template>

<script setup>
import { ref, defineProps, defineEmits, watch, computed, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { UiTestExecutionApi } from '@/api/uiTestExecutionService';

const props = defineProps({
  visible: Boolean,
  executionId: String
});

const emit = defineEmits(['update:visible', 'view-logs', 'rerun']);

// 对话框可见性
const dialogVisible = ref(props.visible);

// 数据状态
const loading = ref(false);
const execution = ref({});
const steps = ref([]);
const failures = ref([]);

// 截图对话框
const screenshotVisible = ref(false);
const currentScreenshot = ref('');
const currentStep = ref(null);

// 监听对话框可见性变化
watch(() => props.visible, (newVal) => {
  dialogVisible.value = newVal;
  if (newVal && props.executionId) {
    fetchExecutionDetail();
  }
});

// 监听executionId变化
watch(() => props.executionId, (newVal) => {
  if (newVal && dialogVisible.value) {
    fetchExecutionDetail();
  }
});

// 监听dialogVisible变化
watch(dialogVisible, (newVal) => {
  emit('update:visible', newVal);
});

// 状态标签类型映射
const getStatusTagType = (status) => {
  const statusMap = {
    "成功": "success",
    "通过": "success",
    "失败": "danger",
    "执行中": "warning",
    "排队中": "info",
    "已取消": "",
    "跳过": "info"
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

// 获取执行详情
const fetchExecutionDetail = async () => {
  if (!props.executionId) return;
  
  loading.value = true;
  try {
    const res = await UiTestExecutionApi.getExecutionDetail(props.executionId);
    if (res.code === 200) {
      execution.value = res.data;
      
      // 获取执行步骤
      const stepsRes = await UiTestExecutionApi.getExecutionSteps(props.executionId);
      if (stepsRes.code === 200) {
        steps.value = stepsRes.data;
        
        // 提取失败步骤作为失败详情
        failures.value = steps.value
          .filter(step => step.status === '失败')
          .map(step => ({
            step: step.stepIndex || step.step,
            description: step.stepName || step.description,
            message: step.errorMessage || '执行失败',
            trace: step.errorTrace || '',
            screenshot: step.screenshotPath || null
          }));
      }
    } else {
      ElMessage.error(res.message || '获取执行详情失败');
    }
  } catch (error) {
    ElMessage.error("获取执行详情失败");
    console.error("获取执行详情失败:", error);
  } finally {
    loading.value = false;
  }
};

// 查看步骤截图
const viewStepScreenshot = (step) => {
  currentStep.value = step;
  currentScreenshot.value = step.screenshotPath || `/api/ui-test-execution/${props.executionId}/steps/${step.stepIndex || step.step}/screenshot`;
  screenshotVisible.value = true;
};

// 查看执行日志
const viewExecutionLogs = () => {
  emit('view-logs', execution.value);
  dialogVisible.value = false;
};

// 重新执行
const rerunExecution = () => {
  emit('rerun', execution.value);
  dialogVisible.value = false;
};
</script>

<style scoped>
.execution-detail {
  min-height: 400px;
}

.execution-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.execution-header h3 {
  margin: 0;
  font-weight: bold;
  font-size: 18px;
}

.execution-info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 15px;
}

.info-item {
  display: flex;
  flex-direction: column;
}

.info-item label {
  font-size: 12px;
  color: #909399;
  margin-bottom: 4px;
}

.info-item span {
  font-size: 14px;
  font-weight: 500;
}

.execution-stats {
  display: flex;
  justify-content: space-between;
  margin: 20px 0;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 15px;
  background-color: #f8f9fa;
  border-radius: 8px;
  width: 80px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
}

.stat-label {
  font-size: 12px;
  color: #606266;
}

.stat-item.success {
  background-color: #f0f9eb;
}

.stat-item.success .stat-value {
  color: #67c23a;
}

.stat-item.danger {
  background-color: #fef0f0;
}

.stat-item.danger .stat-value {
  color: #f56c6c;
}

.stat-item.warning {
  background-color: #fdf6ec;
}

.stat-item.warning .stat-value {
  color: #e6a23c;
}

.execution-progress {
  margin: 20px 0;
}

.progress-text {
  margin-top: 5px;
  text-align: center;
  font-size: 14px;
  color: #606266;
}

.steps-section h4, 
.failure-section h4,
.execution-params h4 {
  margin: 0 0 15px 0;
  font-size: 16px;
  font-weight: bold;
}

.failure-section {
  margin: 20px 0;
}

.failure-details {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-top: 10px;
}

.failure-message {
  padding: 10px;
  background-color: #fef0f0;
  border-radius: 4px;
}

.failure-trace {
  margin-top: 5px;
}

.failure-trace pre {
  padding: 10px;
  background-color: #f8f8f8;
  border-radius: 4px;
  font-family: monospace;
  font-size: 12px;
  overflow-x: auto;
}

.failure-screenshot {
  margin-top: 10px;
}

.screenshot-container {
  margin-top: 5px;
  text-align: center;
}

.screenshot-container img {
  max-width: 100%;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
}

.execution-params {
  margin-top: 20px;
}

.no-action {
  color: #c0c4cc;
}

.screenshot-dialog-content {
  display: flex;
  justify-content: center;
}

.step-screenshot {
  max-width: 100%;
  max-height: 600px;
  border: 1px solid #ebeef5;
}
</style> 