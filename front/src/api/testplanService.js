// api/testplanService.js
import { http } from '@/utils/request'

export const testplanApi = {
  // 获取所有测试计划列表
  getTestplans: (params) => {
    // 构造干净的查询参数对象
    const cleanParams = {
      page: params.page || 1, // 默认值为 1
      pageSize: params.pageSize || 10, // 默认值为 10
    };
  
    // 仅在 status 有值时添加
    if (params.status) {
      cleanParams.status = params.status;
      cleanParams.status = encodeURIComponent(params.status);

    }
  
    return http.get('/api/testplans/list', {
      page: cleanParams.page, // 使用清理后的参数
      pageSize: cleanParams.pageSize, // 使用清理后的参数
      status: cleanParams.status,
    });
  },

  // 获取单个测试计划详情
  getTestplan(id) {
    return http.get(`/api/testplans/${id}`)
  },

  // 创建新测试计划
  createTestplan(data) {
    return http.post('/api/testplans', data)
  },

  // 更新测试计划信息
  updateTestplan(id, data) {
    return http.put(`/api/testplans/${id}`, data)
  },

  // 删除测试计划
  deleteTestplan(id) {
    return http.delete(`/api/testplans/${id}`)
  }
}

export default testplanApi