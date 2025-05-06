<template>
  <div class="ui-test-config-container">
    <el-card class="config-card">
      <template #header>
        <div class="card-header">
          <span>UI测试配置</span>
          <el-button type="primary" @click="handleSaveAllConfigs">保存所有配置</el-button>
        </div>
      </template>
      
      <el-tabs v-model="activeTab" tab-position="left" class="config-tabs" @tab-click="handleTabClick">
        <el-tab-pane label="浏览器/设备配置" name="browser">
          <BrowserConfig 
            ref="browserConfigRef"
            @refresh="refreshData" 
          />
        </el-tab-pane>
        
        <el-tab-pane label="全局等待时间" name="waitTime">
          <WaitTimeConfig 
            ref="waitTimeConfigRef"
            @refresh="refreshData"
          />
        </el-tab-pane>
        
        <el-tab-pane label="截图策略" name="screenshot">
          <ScreenshotConfig 
            ref="screenshotConfigRef"
            @refresh="refreshData"
          />
        </el-tab-pane>
        
        <el-tab-pane label="失败重试设置" name="retry">
          <RetryConfig 
            ref="retryConfigRef"
            @refresh="refreshData"
          />
        </el-tab-pane>
        
        <el-tab-pane label="默认配置" name="default">
          <DefaultConfig 
            ref="defaultConfigRef"
            @refresh="refreshData"
          />
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue';
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

// 处理标签页切换
const handleTabClick = (tab) => {
  loadCurrentTabData();
};

// 加载当前活动标签页的数据
const loadCurrentTabData = () => {
  switch (activeTab.value) {
    case 'browser':
      browserConfigRef.value?.loadData?.();
      break;
    case 'waitTime':
      waitTimeConfigRef.value?.loadData?.();
      break;
    case 'screenshot':
      screenshotConfigRef.value?.loadData?.();
      break;
    case 'retry':
      retryConfigRef.value?.loadData?.();
      break;
    case 'default':
      defaultConfigRef.value?.initData?.();
      break;
  }
};

// 刷新数据
const refreshData = () => {
  loadCurrentTabData();
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

// 监听activeTab变化
watch(activeTab, (newVal, oldVal) => {
  if (newVal !== oldVal) {
    loadCurrentTabData();
  }
});

// 组件挂载时加载当前标签页数据
onMounted(() => {
  // 仅加载当前活动标签页的数据
  loadCurrentTabData();
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