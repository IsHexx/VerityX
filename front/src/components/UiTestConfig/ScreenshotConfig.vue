<template>
  <div class="screenshot-config-container">
    <div class="header">
      <h3>截图策略配置</h3>
      <el-button type="primary" @click="handleSave">保存配置</el-button>
    </div>
    
    <el-form :model="formData" :rules="rules" ref="formRef" label-width="180px">
      <el-divider content-position="left">基本设置</el-divider>
      
      <el-form-item label="每步截图" prop="takeScreenshotOnStep">
        <el-switch v-model="formData.takeScreenshotOnStep" />
        <span class="tip">每个测试步骤执行后自动截图</span>
      </el-form-item>
      
      <el-form-item label="错误时截图" prop="takeScreenshotOnError">
        <el-switch v-model="formData.takeScreenshotOnError" />
        <span class="tip">测试步骤失败时自动截图</span>
      </el-form-item>
      
      <el-form-item label="与基准图比对" prop="compareWithBaseline">
        <el-switch v-model="formData.compareWithBaseline" />
        <span class="tip">自动与基准图进行像素比对</span>
      </el-form-item>
      
      <el-form-item label="高亮元素" prop="highlightElements">
        <el-switch v-model="formData.highlightElements" />
        <span class="tip">截图前高亮操作元素</span>
      </el-form-item>
      
      <el-divider content-position="left">存储设置</el-divider>
      
      <el-form-item label="截图质量" prop="screenshotQuality">
        <el-slider
          v-model="formData.screenshotQuality"
          :min="1"
          :max="100"
          :step="1"
          :show-input="true"
        />
      </el-form-item>
      
      <el-form-item label="最大截图数量" prop="maxScreenshots">
        <el-input-number
          v-model="formData.maxScreenshots"
          :min="1"
          :max="1000"
          controls-position="right"
        />
      </el-form-item>
      
      <el-form-item label="截图缩放比例" prop="screenshotScale">
        <el-input-number
          v-model="formData.screenshotScale"
          :min="0.1"
          :max="2"
          :step="0.1"
          :precision="1"
          controls-position="right"
        />
      </el-form-item>
      
      <el-form-item label="截图格式" prop="screenshotFormat">
        <el-select v-model="formData.screenshotFormat">
          <el-option label="PNG" value="png" />
          <el-option label="JPEG" value="jpeg" />
          <el-option label="WebP" value="webp" />
        </el-select>
      </el-form-item>
      
      <el-form-item label="存储方式" prop="screenshotStorage">
        <el-select v-model="formData.screenshotStorage">
          <el-option label="本地存储" value="local" />
          <el-option label="云存储" value="cloud" />
          <el-option label="数据库" value="database" />
        </el-select>
      </el-form-item>
      
      <el-form-item label="存储路径" prop="screenshotStoragePath" v-if="formData.screenshotStorage === 'local'">
        <el-input v-model="formData.screenshotStoragePath" placeholder="本地存储路径" />
      </el-form-item>
      
      <el-form-item label="命名模式" prop="screenshotNamingPattern">
        <el-input v-model="formData.screenshotNamingPattern" placeholder="{testName}_{timestamp}_{stepId}" />
        <div class="naming-help">
          <p>可用变量：</p>
          <ul>
            <li>{testName}: 测试名称</li>
            <li>{timestamp}: 时间戳</li>
            <li>{stepId}: 步骤ID</li>
            <li>{stepName}: 步骤名称</li>
            <li>{status}: 执行状态</li>
          </ul>
        </div>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, defineProps, defineEmits, watch } from 'vue';
import { ElMessage } from 'element-plus';

// 定义接收的属性
const props = defineProps({
  screenshotConfig: {
    type: Object,
    required: true
  }
});

// 定义向父组件发送的事件
const emit = defineEmits(['update-screenshot']);

// 表单引用
const formRef = ref(null);

// 表单数据
const formData = reactive({
  takeScreenshotOnStep: true,
  takeScreenshotOnError: true,
  compareWithBaseline: false,
  screenshotQuality: 80,
  maxScreenshots: 100,
  screenshotStorage: 'local',
  screenshotStoragePath: '/data/screenshots',
  screenshotNamingPattern: '{testName}_{timestamp}_{stepId}',
  screenshotFormat: 'png',
  screenshotScale: 1.0,
  highlightElements: true
});

// 表单验证规则
const rules = {
  screenshotQuality: [
    { required: true, message: '请设置截图质量', trigger: 'blur' },
    { type: 'number', min: 1, max: 100, message: '质量必须在1-100之间', trigger: 'blur' }
  ],
  maxScreenshots: [
    { required: true, message: '请设置最大截图数量', trigger: 'blur' },
    { type: 'number', min: 1, max: 1000, message: '数量必须在1-1000之间', trigger: 'blur' }
  ],
  screenshotStoragePath: [
    { required: true, message: '请输入存储路径', trigger: 'blur' }
  ],
  screenshotNamingPattern: [
    { required: true, message: '请输入命名模式', trigger: 'blur' }
  ]
};

// 监听属性变化
watch(() => props.screenshotConfig, (newConfig) => {
  if (newConfig) {
    Object.assign(formData, newConfig);
  }
}, { deep: true, immediate: true });

// 保存配置
const handleSave = async () => {
  if (!formRef.value) return;
  
  await formRef.value.validate((valid) => {
    if (valid) {
      emit('update-screenshot', { ...formData });
      ElMessage.success('截图策略配置已保存');
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
  if (props.screenshotConfig) {
    Object.assign(formData, props.screenshotConfig);
  }
});
</script>

<style scoped>
.screenshot-config-container {
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

.naming-help {
  margin-top: 10px;
  background-color: #f5f7fa;
  padding: 10px;
  border-radius: 4px;
}

.naming-help p {
  margin: 0 0 5px 0;
  font-weight: bold;
}

.naming-help ul {
  margin: 0;
  padding-left: 20px;
}

.naming-help li {
  margin-bottom: 3px;
}
</style> 