<template>
  <div class="default-config-container">
    <div class="header">
      <h3>默认配置</h3>
      <el-button type="primary" @click="handleSave" :loading="loading">保存配置</el-button>
    </div>

    <el-form :model="formData" ref="formRef" label-width="180px" v-loading="loading">
      <el-form-item label="默认浏览器配置" prop="browserConfig">
        <el-select v-model="formData.browserConfig" placeholder="选择默认浏览器配置">
          <el-option
            v-for="browser in browserOptions"
            :key="browser.id"
            :label="browser.configName"
            :value="browser.id"
            :disabled="!browser.isActive"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="默认等待时间配置" prop="waitTimeConfig">
        <el-select v-model="formData.waitTimeConfig" placeholder="选择默认等待时间配置">
          <el-option
            v-for="config in waitTimeOptions"
            :key="config.id"
            :label="config.configName"
            :value="config.id"
            :disabled="!config.isActive"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="默认截图策略配置" prop="screenshotConfig">
        <el-select v-model="formData.screenshotConfig" placeholder="选择默认截图策略配置">
          <el-option
            v-for="config in screenshotOptions"
            :key="config.id"
            :label="config.configName"
            :value="config.id"
            :disabled="!config.isActive"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="默认重试配置" prop="retryConfig">
        <el-select v-model="formData.retryConfig" placeholder="选择默认重试配置">
          <el-option
            v-for="config in retryOptions"
            :key="config.id"
            :label="config.configName"
            :value="config.id"
            :disabled="!config.isActive"
          />
        </el-select>
      </el-form-item>

      <el-alert
        title="设置说明"
        type="info"
        description="选择并设置各类配置的默认选项，这些配置将作为新建测试用例和测试套件时的默认配置。"
        show-icon
        :closable="false"
      />
    </el-form>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, defineEmits } from 'vue';
import { ElMessage } from 'element-plus';
import { UiTestConfigApi } from '@/api/uiTestConfigService';

// 定义事件
const emit = defineEmits(['refresh']);

// 表单引用和加载状态
const formRef = ref(null);
const loading = ref(false);
const browserOptions = ref([]);

// 表单数据
const formData = reactive({
  browserConfig: null,    // 默认浏览器配置ID
  waitTimeConfig: null,   // 默认等待时间配置ID
  screenshotConfig: null, // 默认截图策略配置ID
  retryConfig: null,      // 默认重试配置ID
});

// 加载浏览器配置选项
const loadBrowserOptions = async () => {
  try {
    loading.value = true;
    const res = await UiTestConfigApi.getBrowserConfigs();
    if (res.success) {
      browserOptions.value = res.data || [];
    } else {
      ElMessage.error(res.message || '获取浏览器配置失败');
    }
  } catch (error) {
    console.error('获取浏览器配置失败:', error);
    ElMessage.error('获取浏览器配置失败');
  } finally {
    loading.value = false;
  }
};

// 加载所有默认配置
const loadDefaultConfigs = async () => {
  loading.value = true;
  try {
    // 加载默认浏览器配置
    try {
      const browserRes = await UiTestConfigApi.getDefaultConfig('BROWSER');
      console.log('默认浏览器配置响应:', browserRes);
      if (browserRes.success) {
        if (browserRes.data) {
          formData.browserConfig = browserRes.data.id;
        } else {
          console.warn('未找到默认浏览器配置');
          formData.browserConfig = null;
        }
      } else if (browserRes.message) {
        console.warn('获取默认浏览器配置提示:', browserRes.message);
      }
    } catch (err) {
      console.error('获取默认浏览器配置错误:', err);
      // 不显示错误消息，继续加载其他配置
    }

    // 加载默认等待时间配置
    try {
      const waitTimeRes = await UiTestConfigApi.getDefaultConfig('WAIT_TIME');
      console.log('默认等待时间配置响应:', waitTimeRes);
      if (waitTimeRes.success) {
        if (waitTimeRes.data) {
          formData.waitTimeConfig = waitTimeRes.data.id;
        } else {
          console.warn('未找到默认等待时间配置');
          formData.waitTimeConfig = null;
        }
      } else if (waitTimeRes.message) {
        console.warn('获取默认等待时间配置提示:', waitTimeRes.message);
      }
    } catch (err) {
      console.error('获取默认等待时间配置错误:', err);
      // 不显示错误消息，继续加载其他配置
    }

    // 加载默认截图策略配置
    try {
      const screenshotRes = await UiTestConfigApi.getDefaultConfig('SCREENSHOT');
      console.log('默认截图策略配置响应:', screenshotRes);
      if (screenshotRes.success) {
        if (screenshotRes.data) {
          formData.screenshotConfig = screenshotRes.data.id;
        } else {
          console.warn('未找到默认截图策略配置');
          formData.screenshotConfig = null;
        }
      } else if (screenshotRes.message) {
        console.warn('获取默认截图策略配置提示:', screenshotRes.message);
      }
    } catch (err) {
      console.error('获取默认截图策略配置错误:', err);
      // 不显示错误消息，继续加载其他配置
    }

    // 加载默认重试配置
    try {
      const retryRes = await UiTestConfigApi.getDefaultConfig('RETRY');
      console.log('默认重试配置响应:', retryRes);
      if (retryRes.success) {
        if (retryRes.data) {
          formData.retryConfig = retryRes.data.id;
        } else {
          console.warn('未找到默认重试配置');
          formData.retryConfig = null;
          // 显示友好提示
          ElMessage.info('尚未设置默认重试配置，请在配置列表中选择一个配置并设为默认');
        }
      } else if (retryRes.message) {
        console.warn('获取默认重试配置提示:', retryRes.message);
        // 显示后端返回的消息
        ElMessage.info(retryRes.message);
      }
    } catch (err) {
      console.error('获取默认重试配置错误:', err);
      // 不显示错误消息，继续加载其他配置
    }
  } catch (error) {
    console.error('获取默认配置失败:', error);
    ElMessage.error('获取默认配置失败');
  } finally {
    loading.value = false;
  }
};

// 保存默认配置
const handleSave = async () => {
  if (!formRef.value) return;

  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true;
      try {
        // 设置默认浏览器配置
        if (formData.browserConfig) {
          await UiTestConfigApi.setDefaultConfig(formData.browserConfig);
        }

        // 设置默认等待时间配置
        if (formData.waitTimeConfig) {
          await UiTestConfigApi.setDefaultConfig(formData.waitTimeConfig);
        }

        // 设置默认截图策略配置
        if (formData.screenshotConfig) {
          await UiTestConfigApi.setDefaultConfig(formData.screenshotConfig);
        }

        // 设置默认重试配置
        if (formData.retryConfig) {
          await UiTestConfigApi.setDefaultConfig(formData.retryConfig);
        }

        ElMessage.success('默认配置已保存');
        emit('refresh');
      } catch (error) {
        console.error('保存默认配置失败:', error);
        ElMessage.error('保存默认配置失败');
      } finally {
        loading.value = false;
      }
    } else {
      ElMessage.error('表单验证失败，请检查输入');
    }
  });
};

// 加载配置选项
const loadWaitTimeOptions = async () => {
  try {
    const res = await UiTestConfigApi.getWaitTimeConfigs();
    return res.success ? res.data || [] : [];
  } catch (error) {
    console.error('获取等待时间配置失败:', error);
    return [];
  }
};

const loadScreenshotOptions = async () => {
  try {
    const res = await UiTestConfigApi.getScreenshotConfigs();
    return res.success ? res.data || [] : [];
  } catch (error) {
    console.error('获取截图配置失败:', error);
    return [];
  }
};

const loadRetryOptions = async () => {
  try {
    const res = await UiTestConfigApi.getRetryConfigs();
    return res.success ? res.data || [] : [];
  } catch (error) {
    console.error('获取重试配置失败:', error);
    return [];
  }
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

// 初始化数据
const initData = async () => {
  await Promise.all([
    loadBrowserOptions(),
    loadDefaultConfigs()
  ]);
};

// 对外暴露方法
defineExpose({
  validate,
  initData
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