import { http } from '@/utils/request'

// 转换API响应格式的辅助函数
const convertResponse = (promise) => {
  return promise.then(res => {
    // 处理响应数据，支持两种格式
    // 如果已经是 {success: true/false, data: ...} 格式，则直接返回
    if (res.success !== undefined) {
      return res;
    }

    // 如果是 {code: 200, data: ...} 格式，则转换为 {success: true, data: ...}
    return {
      success: true,
      data: res.data,
      message: res.message
    };
  }).catch(error => {
    console.error('API请求失败:', error);
    // 添加详细的错误日志，帮助调试
    if (error.response) {
      console.error('错误响应数据:', error.response.data);
      console.error('错误状态码:', error.response.status);
    }
    return {
      success: false,
      message: error.message || '请求失败',
      data: null
    };
  });
};

export const UiTestConfigApi = {
  // 获取UI测试配置列表
  getConfigList: (configType, configName) => {
    const params = {};
    if (configType) params.configType = configType;
    if (configName) params.configName = configName;
    return convertResponse(http.get('/api/ui-test-config', params));
  },

  // 获取特定配置详情
  getConfigById: (id) => {
    return convertResponse(http.get(`/api/ui-test-config/${id}`));
  },

  // 获取默认配置
  getDefaultConfig: (configType) => {
    return convertResponse(http.get(`/api/ui-test-config/default`, { params: { configType: configType } }));
  },

  // 创建UI测试配置
  createConfig: (data) => {
    return convertResponse(http.post('/api/ui-test-config', data));
  },

  // 更新UI测试配置
  updateConfig: (id, data) => {
    return convertResponse(http.put(`/api/ui-test-config/${id}`, data));
  },

  // 删除UI测试配置
  deleteConfig: (id) => {
    return convertResponse(http.delete(`/api/ui-test-config/${id}`));
  },

  // 设置为默认配置
  setDefaultConfig: (id) => {
    return convertResponse(http.put(`/api/ui-test-config/${id}/set-default`));
  },

  // 启用/禁用配置
  toggleConfigStatus: (id, active) => {
    return convertResponse(http.put(`/api/ui-test-config/${id}/toggle-status`, null, { params: { active } }));
  },

  // 获取配置类型统计
  getConfigTypeStats: () => {
    return convertResponse(http.get('/api/ui-test-config/stats'));
  },

  // 浏览器配置相关方法
  getBrowserConfigs: () => {
    const params = { configType: 'BROWSER' };
    return convertResponse(http.get('/api/ui-test-config', params));
  },

  // 等待时间配置相关方法
  getWaitTimeConfigs: () => {
    const params = { configType: 'WAIT_TIME' };
    return convertResponse(http.get('/api/ui-test-config', params));
  },

  // 截图策略配置相关方法
  getScreenshotConfigs: () => {
    const params = { configType: 'SCREENSHOT' };
    return convertResponse(http.get('/api/ui-test-config', params));
  },

  // 重试配置相关方法
  getRetryConfigs: () => {
    const params = { configType: 'RETRY' };
    return convertResponse(http.get('/api/ui-test-config', params));
  }
}

export default UiTestConfigApi