<template>
  <div class="ui-test-config-container">
    <el-card class="config-card">
      <template #header>
        <div class="card-header">
          <span>UI测试配置</span>
          <el-button type="primary" @click="handleSaveAllConfigs">保存所有配置</el-button>
        </div>
      </template>
      
      <el-tabs v-model="activeTab" tab-position="left" class="config-tabs">
        <el-tab-pane label="浏览器/设备配置" name="browser">
          <BrowserConfig 
            ref="browserConfigRef"
            :browserConfigs="browserConfigs" 
            @add-browser="handleAddBrowser" 
            @delete-browser="handleDeleteBrowser" 
            @update-browser="handleUpdateBrowser"
          />
        </el-tab-pane>
        
        <el-tab-pane label="全局等待时间" name="waitTime">
          <WaitTimeConfig 
            ref="waitTimeConfigRef"
            :waitTimeConfig="waitTimeConfig" 
            @update-wait-time="handleUpdateWaitTime"
          />
        </el-tab-pane>
        
        <el-tab-pane label="截图策略" name="screenshot">
          <ScreenshotConfig 
            ref="screenshotConfigRef"
            :screenshotConfig="screenshotConfig" 
            @update-screenshot="handleUpdateScreenshot"
          />
        </el-tab-pane>
        
        <el-tab-pane label="失败重试设置" name="retry">
          <RetryConfig 
            ref="retryConfigRef"
            :retryConfig="retryConfig" 
            @update-retry="handleUpdateRetry"
          />
        </el-tab-pane>
        
        <el-tab-pane label="默认配置" name="default">
          <DefaultConfig 
            ref="defaultConfigRef"
            :defaultConfig="defaultConfig" 
            :browserOptions="browserConfigs" 
            @update-default="handleUpdateDefault"
          />
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { UiTestConfigApi } from '@/api/uiTestConfigService';
import BrowserConfig from '@/components/UiTestConfig/BrowserConfig.vue';
import WaitTimeConfig from '@/components/UiTestConfig/WaitTimeConfig.vue';
import ScreenshotConfig from '@/components/UiTestConfig/ScreenshotConfig.vue';
import RetryConfig from '@/components/UiTestConfig/RetryConfig.vue';
import DefaultConfig from '@/components/UiTestConfig/DefaultConfig.vue';

// 状态管理
const activeTab = ref('browser');
const loading = ref(false);
const browserConfigRef = ref(null);
const waitTimeConfigRef = ref(null);
const screenshotConfigRef = ref(null);
const retryConfigRef = ref(null);
const defaultConfigRef = ref(null);

// 各配置数据
const browserConfigs = ref([]);
const waitTimeConfig = ref({});
const screenshotConfig = ref({});
const retryConfig = ref({});
const defaultConfig = ref({});

// 初始化配置数据
const initConfigData = async () => {
  loading.value = true;
  try {
    // 在实际项目中应该调用API获取配置
    // const res = await UiTestConfigApi.getTestConfigs();
    // if (res.code === 200) {
    //   const { browser, waitTime, screenshot, retry, defaultConfig: defConfig } = res.data;
    //   browserConfigs.value = browser;
    //   waitTimeConfig.value = waitTime;
    //   screenshotConfig.value = screenshot;
    //   retryConfig.value = retry;
    //   defaultConfig.value = defConfig;
    // } else {
    //   ElMessage.error(res.message || '获取配置失败');
    // }
    
    // 使用模拟数据进行开发
    browserConfigs.value = [
      {
        id: 1,
        name: 'Chrome',
        version: '100.0.4896.127',
        isEnabled: true,
        isHeadless: false,
        description: '稳定版Chrome浏览器'
      },
      {
        id: 2,
        name: 'Firefox',
        version: '99.0',
        isEnabled: true,
        isHeadless: false,
        description: '稳定版Firefox浏览器'
      },
      {
        id: 3,
        name: 'Edge',
        version: '100.0.1185.39',
        isEnabled: true,
        isHeadless: true,
        description: '稳定版Edge浏览器'
      }
    ];
    
    waitTimeConfig.value = {
      pageLoadTimeout: 30,
      implicitWait: 10,
      explicitWait: 20,
      scriptTimeout: 15,
      ajaxTimeout: 25,
      pollingInterval: 500,
      customWaits: [
        { name: '登录页面加载', selector: '#loginForm', timeout: 15 },
        { name: '数据表格加载', selector: '.el-table', timeout: 10 }
      ]
    };
    
    screenshotConfig.value = {
      takeScreenshotOnStep: true,
      takeScreenshotOnError: true,
      compareWithBaseline: true,
      screenshotQuality: 80,
      maxScreenshots: 100,
      screenshotStorage: 'local',
      screenshotStoragePath: '/data/screenshots',
      screenshotNamingPattern: '{testName}_{timestamp}_{stepId}',
      screenshotFormat: 'png',
      screenshotScale: 1.0,
      highlightElements: true
    };
    
    retryConfig.value = {
      enableRetry: true,
      maxRetryCount: 3,
      retryInterval: 2000,
      retryOnlyOnSpecificErrors: true,
      retryableErrors: [
        { id: 1, errorType: 'ElementNotFound', description: '元素未找到错误' },
        { id: 2, errorType: 'StaleElementReference', description: '元素引用过期错误' },
        { id: 3, errorType: 'TimeoutException', description: '超时错误' }
      ],
      skipRetryOnSpecificSteps: false,
      skipRetryStepTypes: [],
      exponentialBackoff: true
    };
    
    defaultConfig.value = {
      defaultBrowserId: 1,
      defaultResolution: '1920x1080',
      defaultScreenshotEnabled: true,
      defaultRetryEnabled: true,
      defaultTimeout: 30,
      defaultImplicitWait: 10,
      defaultElementHighlight: true,
      defaultVideoCaptureEnabled: false
    };
  } catch (error) {
    ElMessage.error('获取配置信息失败: ' + error.message);
    console.error('获取配置信息失败:', error);
  } finally {
    loading.value = false;
  }
};

// 浏览器配置相关处理
const handleAddBrowser = (newBrowser) => {
  // 模拟添加浏览器
  const maxId = Math.max(0, ...browserConfigs.value.map(item => item.id));
  newBrowser.id = maxId + 1;
  browserConfigs.value.push(newBrowser);
  ElMessage.success('添加浏览器成功');
  
  // 实际项目中应该调用API
  // try {
  //   const res = await UiTestConfigApi.addBrowser(newBrowser);
  //   if (res.code === 200) {
  //     newBrowser.id = res.data.id;
  //     browserConfigs.value.push(newBrowser);
  //     ElMessage.success('添加浏览器成功');
  //   } else {
  //     ElMessage.error(res.message || '添加浏览器失败');
  //   }
  // } catch (error) {
  //   ElMessage.error('添加浏览器失败: ' + error.message);
  // }
};

const handleDeleteBrowser = async (browserId) => {
  try {
    await ElMessageBox.confirm(
      '确定要删除该浏览器配置吗？关联的测试可能会受到影响。',
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    );
    
    // 模拟删除浏览器
    browserConfigs.value = browserConfigs.value.filter(browser => browser.id !== browserId);
    ElMessage.success('删除浏览器成功');
    
    // 实际项目中应该调用API
    // const res = await UiTestConfigApi.deleteBrowser(browserId);
    // if (res.code === 200) {
    //   browserConfigs.value = browserConfigs.value.filter(browser => browser.id !== browserId);
    //   ElMessage.success('删除浏览器成功');
    // } else {
    //   ElMessage.error(res.message || '删除浏览器失败');
    // }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除浏览器失败: ' + error.message);
    }
  }
};

const handleUpdateBrowser = (updatedBrowser) => {
  // 模拟更新浏览器
  const index = browserConfigs.value.findIndex(item => item.id === updatedBrowser.id);
  if (index !== -1) {
    browserConfigs.value[index] = { ...browserConfigs.value[index], ...updatedBrowser };
    ElMessage.success('更新浏览器配置成功');
  }
  
  // 实际项目中应该调用API
  // try {
  //   const res = await UiTestConfigApi.updateBrowserConfig(updatedBrowser);
  //   if (res.code === 200) {
  //     const index = browserConfigs.value.findIndex(item => item.id === updatedBrowser.id);
  //     if (index !== -1) {
  //       browserConfigs.value[index] = { ...browserConfigs.value[index], ...updatedBrowser };
  //       ElMessage.success('更新浏览器配置成功');
  //     }
  //   } else {
  //     ElMessage.error(res.message || '更新浏览器配置失败');
  //   }
  // } catch (error) {
  //   ElMessage.error('更新浏览器配置失败: ' + error.message);
  // }
};

// 全局等待时间配置处理
const handleUpdateWaitTime = (updatedConfig) => {
  waitTimeConfig.value = { ...waitTimeConfig.value, ...updatedConfig };
  ElMessage.success('全局等待时间配置已更新');
  
  // 实际项目中应该调用API
  // try {
  //   const res = await UiTestConfigApi.updateWaitTimeConfig(updatedConfig);
  //   if (res.code === 200) {
  //     waitTimeConfig.value = { ...waitTimeConfig.value, ...updatedConfig };
  //     ElMessage.success('全局等待时间配置已更新');
  //   } else {
  //     ElMessage.error(res.message || '更新全局等待时间配置失败');
  //   }
  // } catch (error) {
  //   ElMessage.error('更新全局等待时间配置失败: ' + error.message);
  // }
};

// 截图策略配置处理
const handleUpdateScreenshot = (updatedConfig) => {
  screenshotConfig.value = { ...screenshotConfig.value, ...updatedConfig };
  ElMessage.success('截图策略配置已更新');
  
  // 实际项目中应该调用API
  // try {
  //   const res = await UiTestConfigApi.updateScreenshotConfig(updatedConfig);
  //   if (res.code === 200) {
  //     screenshotConfig.value = { ...screenshotConfig.value, ...updatedConfig };
  //     ElMessage.success('截图策略配置已更新');
  //   } else {
  //     ElMessage.error(res.message || '更新截图策略配置失败');
  //   }
  // } catch (error) {
  //   ElMessage.error('更新截图策略配置失败: ' + error.message);
  // }
};

// 失败重试配置处理
const handleUpdateRetry = (updatedConfig) => {
  retryConfig.value = { ...retryConfig.value, ...updatedConfig };
  ElMessage.success('失败重试配置已更新');
  
  // 实际项目中应该调用API
  // try {
  //   const res = await UiTestConfigApi.updateRetryConfig(updatedConfig);
  //   if (res.code === 200) {
  //     retryConfig.value = { ...retryConfig.value, ...updatedConfig };
  //     ElMessage.success('失败重试配置已更新');
  //   } else {
  //     ElMessage.error(res.message || '更新失败重试配置失败');
  //   }
  // } catch (error) {
  //   ElMessage.error('更新失败重试配置失败: ' + error.message);
  // }
};

// 默认配置处理
const handleUpdateDefault = (updatedConfig) => {
  defaultConfig.value = { ...defaultConfig.value, ...updatedConfig };
  ElMessage.success('默认配置已更新');
  
  // 实际项目中应该调用API
  // try {
  //   const res = await UiTestConfigApi.updateDefaultConfig(updatedConfig);
  //   if (res.code === 200) {
  //     defaultConfig.value = { ...defaultConfig.value, ...updatedConfig };
  //     ElMessage.success('默认配置已更新');
  //   } else {
  //     ElMessage.error(res.message || '更新默认配置失败');
  //   }
  // } catch (error) {
  //   ElMessage.error('更新默认配置失败: ' + error.message);
  // }
};

// 保存所有配置
const handleSaveAllConfigs = async () => {
  try {
    // 调用各组件的验证方法
    const browserValid = browserConfigRef.value?.validate?.() ?? true;
    const waitTimeValid = waitTimeConfigRef.value?.validate?.() ?? true;
    const screenshotValid = screenshotConfigRef.value?.validate?.() ?? true;
    const retryValid = retryConfigRef.value?.validate?.() ?? true;
    const defaultValid = defaultConfigRef.value?.validate?.() ?? true;
    
    if (!browserValid || !waitTimeValid || !screenshotValid || !retryValid || !defaultValid) {
      ElMessage.error('配置验证失败，请检查表单');
      return;
    }
    
    // 这里应该调用各配置的保存API
    // 实际项目中可能需要多个API调用或一个批量保存API
    
    // 模拟保存成功
    ElMessage.success('所有配置已保存成功');
  } catch (error) {
    ElMessage.error('保存配置失败: ' + error.message);
  }
};

// 组件挂载时初始化数据
onMounted(() => {
  initConfigData();
});
</script>

<style scoped>
.ui-test-config-container {
  padding: 10px;
}

.config-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.config-tabs {
  min-height: 500px;
}

.config-tabs :deep(.el-tabs__content) {
  padding: 0 20px;
}
</style> 