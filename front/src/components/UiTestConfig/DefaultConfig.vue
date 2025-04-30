<template>
  <div class="default-config-container">
    <div class="header">
      <h3>默认配置</h3>
      <el-button type="primary" @click="handleSave">保存配置</el-button>
    </div>
    
    <el-form :model="formData" :rules="rules" ref="formRef" label-width="180px">
      <el-form-item label="默认浏览器" prop="defaultBrowserId">
        <el-select v-model="formData.defaultBrowserId" placeholder="选择默认浏览器">
          <el-option
            v-for="browser in browserOptions"
            :key="browser.id"
            :label="browser.name + ' ' + browser.version"
            :value="browser.id"
            :disabled="!browser.isEnabled"
          />
        </el-select>
      </el-form-item>
      
      <el-form-item label="默认分辨率" prop="defaultResolution">
        <el-select v-model="formData.defaultResolution" placeholder="选择默认分辨率">
          <el-option label="1920x1080" value="1920x1080" />
          <el-option label="1366x768" value="1366x768" />
          <el-option label="1280x720" value="1280x720" />
          <el-option label="1440x900" value="1440x900" />
          <el-option label="2560x1440" value="2560x1440" />
          <el-option label="3840x2160" value="3840x2160" />
          <el-option label="自动" value="auto" />
        </el-select>
      </el-form-item>
      
      <el-form-item label="默认超时时间(秒)" prop="defaultTimeout">
        <el-input-number
          v-model="formData.defaultTimeout"
          :min="1"
          :max="300"
          controls-position="right"
        />
      </el-form-item>
      
      <el-form-item label="默认隐式等待(秒)" prop="defaultImplicitWait">
        <el-input-number
          v-model="formData.defaultImplicitWait"
          :min="0"
          :max="60"
          controls-position="right"
        />
      </el-form-item>
      
      <el-divider content-position="left">默认功能设置</el-divider>
      
      <el-form-item label="默认启用截图" prop="defaultScreenshotEnabled">
        <el-switch v-model="formData.defaultScreenshotEnabled" />
      </el-form-item>
      
      <el-form-item label="默认启用重试" prop="defaultRetryEnabled">
        <el-switch v-model="formData.defaultRetryEnabled" />
      </el-form-item>
      
      <el-form-item label="默认高亮元素" prop="defaultElementHighlight">
        <el-switch v-model="formData.defaultElementHighlight" />
      </el-form-item>
      
      <el-form-item label="默认启用视频录制" prop="defaultVideoCaptureEnabled">
        <el-switch v-model="formData.defaultVideoCaptureEnabled" />
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, defineProps, defineEmits, watch } from 'vue';
import { ElMessage } from 'element-plus';

// 定义接收的属性
const props = defineProps({
  defaultConfig: {
    type: Object,
    required: true
  },
  browserOptions: {
    type: Array,
    required: true
  }
});

// 定义向父组件发送的事件
const emit = defineEmits(['update-default']);

// 表单引用
const formRef = ref(null);

// 表单数据
const formData = reactive({
  defaultBrowserId: 1,
  defaultResolution: '1920x1080',
  defaultScreenshotEnabled: true,
  defaultRetryEnabled: true,
  defaultTimeout: 30,
  defaultImplicitWait: 10,
  defaultElementHighlight: true,
  defaultVideoCaptureEnabled: false
});

// 表单验证规则
const rules = {
  defaultBrowserId: [
    { required: true, message: '请选择默认浏览器', trigger: 'change' }
  ],
  defaultResolution: [
    { required: true, message: '请选择默认分辨率', trigger: 'change' }
  ],
  defaultTimeout: [
    { required: true, message: '请设置默认超时时间', trigger: 'blur' },
    { type: 'number', min: 1, max: 300, message: '超时时间必须在1-300秒之间', trigger: 'blur' }
  ],
  defaultImplicitWait: [
    { required: true, message: '请设置默认隐式等待时间', trigger: 'blur' },
    { type: 'number', min: 0, max: 60, message: '等待时间必须在0-60秒之间', trigger: 'blur' }
  ]
};

// 监听属性变化
watch(() => props.defaultConfig, (newConfig) => {
  if (newConfig) {
    Object.assign(formData, newConfig);
  }
}, { deep: true, immediate: true });

// 监听浏览器选项变化，确保选择的浏览器有效
watch(() => props.browserOptions, (newOptions) => {
  if (newOptions && newOptions.length > 0) {
    const selectedBrowser = newOptions.find(browser => browser.id === formData.defaultBrowserId);
    
    // 如果当前选中的浏览器不存在或被禁用，选择第一个启用的浏览器
    if (!selectedBrowser || !selectedBrowser.isEnabled) {
      const firstEnabledBrowser = newOptions.find(browser => browser.isEnabled);
      if (firstEnabledBrowser) {
        formData.defaultBrowserId = firstEnabledBrowser.id;
      }
    }
  }
}, { deep: true, immediate: true });

// 保存配置
const handleSave = async () => {
  if (!formRef.value) return;
  
  await formRef.value.validate((valid) => {
    if (valid) {
      emit('update-default', { ...formData });
      ElMessage.success('默认配置已保存');
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
  if (props.defaultConfig) {
    Object.assign(formData, props.defaultConfig);
  }
});
</script>

<style scoped>
.default-config-container {
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
</style> 