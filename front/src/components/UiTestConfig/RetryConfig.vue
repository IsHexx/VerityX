<template>
  <div class="retry-config-container">
    <div class="header">
      <h3>失败重试配置</h3>
      <el-button type="primary" @click="handleSave">保存配置</el-button>
    </div>
    
    <el-form :model="formData" :rules="rules" ref="formRef" label-width="180px">
      <el-form-item label="启用重试" prop="enableRetry">
        <el-switch v-model="formData.enableRetry" />
      </el-form-item>
      
      <template v-if="formData.enableRetry">
        <el-form-item label="最大重试次数" prop="maxRetryCount">
          <el-input-number
            v-model="formData.maxRetryCount"
            :min="1"
            :max="10"
            controls-position="right"
          />
        </el-form-item>
        
        <el-form-item label="重试间隔时间(毫秒)" prop="retryInterval">
          <el-input-number
            v-model="formData.retryInterval"
            :min="0"
            :max="60000"
            :step="500"
            controls-position="right"
          />
        </el-form-item>
        
        <el-form-item label="指数退避" prop="exponentialBackoff">
          <el-switch v-model="formData.exponentialBackoff" />
          <span class="tip">启用后，每次重试间隔会成倍增加</span>
        </el-form-item>
        
        <el-form-item label="仅针对特定错误重试" prop="retryOnlyOnSpecificErrors">
          <el-switch v-model="formData.retryOnlyOnSpecificErrors" />
        </el-form-item>
        
        <template v-if="formData.retryOnlyOnSpecificErrors">
          <el-divider content-position="left">可重试错误类型</el-divider>
          
          <div class="errors-header">
            <span>错误类型列表</span>
            <el-button type="primary" size="small" @click="addRetryableError">添加错误类型</el-button>
          </div>
          
          <el-table
            :data="formData.retryableErrors"
            border
            style="width: 100%"
            class="errors-table"
          >
            <el-table-column prop="errorType" label="错误类型">
              <template #default="scope">
                <el-input v-model="scope.row.errorType" placeholder="错误类型" />
              </template>
            </el-table-column>
            
            <el-table-column prop="description" label="描述">
              <template #default="scope">
                <el-input v-model="scope.row.description" placeholder="错误描述" />
              </template>
            </el-table-column>
            
            <el-table-column label="操作" width="120">
              <template #default="scope">
                <el-button 
                  type="danger" 
                  size="small" 
                  circle
                  @click="removeRetryableError(scope.$index)"
                >
                  <el-icon><Delete /></el-icon>
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </template>
        
        <el-form-item label="跳过特定步骤的重试" prop="skipRetryOnSpecificSteps">
          <el-switch v-model="formData.skipRetryOnSpecificSteps" />
        </el-form-item>
        
        <template v-if="formData.skipRetryOnSpecificSteps">
          <el-form-item label="跳过的步骤类型" prop="skipRetryStepTypes">
            <el-select
              v-model="formData.skipRetryStepTypes"
              multiple
              placeholder="选择步骤类型"
              style="width: 100%"
            >
              <el-option label="输入文本" value="input" />
              <el-option label="点击元素" value="click" />
              <el-option label="断言检查" value="assert" />
              <el-option label="等待元素" value="wait" />
              <el-option label="滚动操作" value="scroll" />
              <el-option label="截图操作" value="screenshot" />
              <el-option label="JavaScript执行" value="javascript" />
              <el-option label="清除文本" value="clear" />
              <el-option label="选择下拉框" value="select" />
              <el-option label="悬停操作" value="hover" />
            </el-select>
          </el-form-item>
        </template>
      </template>
    </el-form>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, defineProps, defineEmits, watch } from 'vue';
import { ElMessage } from 'element-plus';
import { Delete } from '@element-plus/icons-vue';

// 定义接收的属性
const props = defineProps({
  retryConfig: {
    type: Object,
    required: true
  }
});

// 定义向父组件发送的事件
const emit = defineEmits(['update-retry']);

// 表单引用
const formRef = ref(null);

// 表单数据
const formData = reactive({
  enableRetry: true,
  maxRetryCount: 3,
  retryInterval: 2000,
  retryOnlyOnSpecificErrors: false,
  retryableErrors: [],
  skipRetryOnSpecificSteps: false,
  skipRetryStepTypes: [],
  exponentialBackoff: true
});

// 表单验证规则
const rules = {
  maxRetryCount: [
    { required: true, message: '请设置最大重试次数', trigger: 'blur' },
    { type: 'number', min: 1, max: 10, message: '次数必须在1-10之间', trigger: 'blur' }
  ],
  retryInterval: [
    { required: true, message: '请设置重试间隔', trigger: 'blur' },
    { type: 'number', min: 0, max: 60000, message: '间隔必须在0-60000毫秒之间', trigger: 'blur' }
  ]
};

// 监听属性变化
watch(() => props.retryConfig, (newConfig) => {
  if (newConfig) {
    Object.assign(formData, newConfig);
    
    // 确保retryableErrors是数组
    if (!Array.isArray(formData.retryableErrors)) {
      formData.retryableErrors = [];
    }
    
    // 确保skipRetryStepTypes是数组
    if (!Array.isArray(formData.skipRetryStepTypes)) {
      formData.skipRetryStepTypes = [];
    }
  }
}, { deep: true, immediate: true });

// 添加可重试错误类型
const addRetryableError = () => {
  // 确保retryableErrors已初始化
  if (!formData.retryableErrors) {
    formData.retryableErrors = [];
  }
  
  const maxId = formData.retryableErrors.length > 0 
    ? Math.max(...formData.retryableErrors.map(error => error.id || 0)) 
    : 0;
  
  formData.retryableErrors.push({
    id: maxId + 1,
    errorType: '',
    description: ''
  });
};

// 移除可重试错误类型
const removeRetryableError = (index) => {
  formData.retryableErrors.splice(index, 1);
};

// 保存配置
const handleSave = async () => {
  if (!formRef.value) return;
  
  await formRef.value.validate((valid) => {
    if (valid) {
      emit('update-retry', { ...formData });
      ElMessage.success('失败重试配置已保存');
    } else {
      ElMessage.error('表单验证失败，请检查输入');
    }
  });
};

// 表单验证方法
const validate = () => {
  if (!formRef.value) return true;
  
  let isValid = false;
  formRef.value.validate((valid) => {
    isValid = valid;
  });
  return isValid;
};

// 对外暴露方法
defineExpose({
  validate
});

// 组件挂载时处理
onMounted(() => {
  if (props.retryConfig) {
    Object.assign(formData, props.retryConfig);
  }
});
</script>

<style scoped>
.retry-config-container {
  width: 100%;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header h3 {
  margin: 0;
}

.tip {
  margin-left: 10px;
  color: #909399;
  font-size: 14px;
}

.errors-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.errors-table {
  margin-bottom: 20px;
}
</style> 