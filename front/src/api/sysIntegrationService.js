import { http } from '@/utils/request'

// 系统集成管理API服务
export const SysIntegrationApi = {
  // 第三方工具集成API
  toolIntegration: {
    // 获取工具集成列表
    getToolIntegrationList: (params) => {
      return http.get('/api/sys/tool-integrations', params);
    },
    
    // 获取工具集成详情
    getToolIntegrationById: (id) => {
      return http.get(`/api/sys/tool-integrations/${id}`);
    },
    
    // 创建工具集成
    createToolIntegration: (data) => {
      return http.post('/api/sys/tool-integrations', data);
    },
    
    // 更新工具集成
    updateToolIntegration: (data) => {
      return http.put('/api/sys/tool-integrations', data);
    },
    
    // 修改工具集成状态
    updateToolIntegrationStatus: (id, status) => {
      return http.put(`/api/sys/tool-integrations/${id}/status?status=${status}`);
    },
    
    // 删除工具集成
    deleteToolIntegration: (id) => {
      return http.delete(`/api/sys/tool-integrations/${id}`);
    },
    
    // 批量删除工具集成
    batchDeleteToolIntegration: (ids) => {
      return http.delete('/api/sys/tool-integrations/batch', { data: ids });
    },
    
    // 测试工具集成连接
    testToolIntegrationConnection: (id) => {
      return http.post(`/api/sys/tool-integrations/${id}/test-connection`);
    }
  },
  
  // API管理API
  apiManagement: {
    // 获取API管理列表
    getApiManagementList: (params) => {
      return http.get('/api/sys/api-management', params);
    },
    
    // 获取API管理详情
    getApiManagementById: (id) => {
      return http.get(`/api/sys/api-management/${id}`);
    },
    
    // 创建API管理
    createApiManagement: (data) => {
      return http.post('/api/sys/api-management', data);
    },
    
    // 更新API管理
    updateApiManagement: (data) => {
      return http.put('/api/sys/api-management', data);
    },
    
    // 修改API管理状态
    updateApiManagementStatus: (id, status) => {
      return http.put(`/api/sys/api-management/${id}/status?status=${status}`);
    },
    
    // 删除API管理
    deleteApiManagement: (id) => {
      return http.delete(`/api/sys/api-management/${id}`);
    },
    
    // 批量删除API管理
    batchDeleteApiManagement: (ids) => {
      return http.delete('/api/sys/api-management/batch', { data: ids });
    },
    
    // 重新生成API密钥
    regenerateApiKey: (id) => {
      return http.post(`/api/sys/api-management/${id}/regenerate`);
    }
  },
  
  // 单点登录配置API
  ssoConfig: {
    // 获取单点登录配置列表
    getSsoConfigList: (params) => {
      return http.get('/api/sys/sso-configs', params);
    },
    
    // 获取单点登录配置详情
    getSsoConfigById: (id) => {
      return http.get(`/api/sys/sso-configs/${id}`);
    },
    
    // 创建单点登录配置
    createSsoConfig: (data) => {
      return http.post('/api/sys/sso-configs', data);
    },
    
    // 更新单点登录配置
    updateSsoConfig: (data) => {
      return http.put('/api/sys/sso-configs', data);
    },
    
    // 修改单点登录配置状态
    updateSsoConfigStatus: (id, status) => {
      return http.put(`/api/sys/sso-configs/${id}/status?status=${status}`);
    },
    
    // 设置默认单点登录配置
    setDefaultSsoConfig: (id) => {
      return http.put(`/api/sys/sso-configs/${id}/default`);
    },
    
    // 删除单点登录配置
    deleteSsoConfig: (id) => {
      return http.delete(`/api/sys/sso-configs/${id}`);
    },
    
    // 批量删除单点登录配置
    batchDeleteSsoConfig: (ids) => {
      return http.delete('/api/sys/sso-configs/batch', { data: ids });
    },
    
    // 测试单点登录配置连接
    testSsoConfigConnection: (id) => {
      return http.post(`/api/sys/sso-configs/${id}/test-connection`);
    }
  }
};

export default SysIntegrationApi; 