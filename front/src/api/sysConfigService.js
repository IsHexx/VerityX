import { http } from '@/utils/request'

export const SysConfigApi = {
  // 获取系统配置分页列表
  getConfigPage: (page, size, configName, configKey, configType) => {
    const params = { page, size };
    if (configName) params.configName = configName;
    if (configKey) params.configKey = configKey;
    if (configType) params.configType = configType;
    
    return http.get('/api/sys/configs', { params });
  },
  
  // 获取系统配置详情
  getConfigById: (id) => {
    return http.get(`/api/sys/configs/${id}`);
  },
  
  // 根据配置键名获取配置
  getConfigByKey: (configKey) => {
    return http.get(`/api/sys/configs/key/${configKey}`);
  },
  
  // 根据配置键名获取配置值
  getConfigValue: (configKey) => {
    return http.get(`/api/sys/configs/value/${configKey}`);
  },
  
  // 获取多个配置键值对
  getConfigMap: (configKeys) => {
    return http.post('/api/sys/configs/values', configKeys);
  },
  
  // 创建系统配置
  createConfig: (data) => {
    return http.post('/api/sys/configs', data);
  },
  
  // 更新系统配置
  updateConfig: (data) => {
    return http.put('/api/sys/configs', data);
  },
  
  // 批量更新系统配置
  batchUpdateConfig: (data) => {
    return http.put('/api/sys/configs/batch', data);
  },
  
  // 删除系统配置
  deleteConfig: (id) => {
    return http.delete(`/api/sys/configs/${id}`);
  },
  
  // 批量删除系统配置
  batchDeleteConfig: (ids) => {
    return http.delete('/api/sys/configs/batch', { data: ids });
  },
  
  // 刷新系统配置缓存
  refreshCache: () => {
    return http.post('/api/sys/configs/refresh');
  }
};

export default SysConfigApi; 