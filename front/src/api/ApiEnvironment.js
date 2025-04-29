import { http } from '@/utils/request'

// API环境配置相关接口
export const ApiEnvironmentApi = {
  // 获取环境配置列表（分页）
  getEnvironmentList(params) {
    // 构造干净的查询参数对象，只包含非空值
    const cleanParams = {};
    
    // 只添加有值的参数
    if (params.page) cleanParams.page = params.page;
    if (params.pageSize) cleanParams.pageSize = params.pageSize;
    if (params.keyword) cleanParams.keyword = params.keyword;
    
    console.log("环境配置API调用参数:", cleanParams);
    return http.get('/api/environments/list', cleanParams);
  },

  // 获取所有环境配置
  getAllEnvironments() {
    return http.get('/api/environments')
  },

  // 获取单个环境配置详情
  getEnvironmentById(id) {
    return http.get(`/api/environments/${id}`)
  },

  // 获取默认环境配置
  getDefaultEnvironment() {
    return http.get('/api/environments/default')
  },

  // 创建环境配置
  createEnvironment(data) {
    return http.post('/api/environments', data)
  },

  // 更新环境配置
  updateEnvironment(id, data) {
    return http.put(`/api/environments/${id}`, data)
  },

  // 设置默认环境
  setDefaultEnvironment(id) {
    return http.put(`/api/environments/${id}/default`)
  },

  // 删除环境配置
  deleteEnvironment(id) {
    return http.delete(`/api/environments/${id}`)
  },

  // 批量删除环境配置
  batchDeleteEnvironments(ids) {
    return http.post('/api/environments/batch-delete', { ids })
  }
}

export default ApiEnvironmentApi 