// api/BugService.js
import { http } from '@/utils/request'

export const BugApi = {
  // 获取所有测试计划列表
  getBugs: (params) => {
    // 构造干净的查询参数对象
    const cleanParams = {
      page: params.page || 1, // 默认值为 1
      pageSize: params.pageSize || 10, // 默认值为 10
    };
  
    // 仅在 status 有值时添加
    if (params.status) {
      cleanParams.status = params.status;
    }
  
    return http.get('/api/bugs/list', {
      page: cleanParams.page, // 使用清理后的参数
      pageSize: cleanParams.pageSize, // 使用清理后的参数
      status: cleanParams.status,
    });
  },

  // 获取单个测试计划详情
  getBug(id) {
    return http.get(`/api/bugs/${id}`)
  },

  // 创建新测试计划
  createBug(data) {
    return http.post('/api/bugs', data)
  },

  // 更新测试计划信息
  updateBug(id, data) {
    return http.put(`/api/bugs/${id}`, data)
  },

  // 删除测试计划
  deleteBug(id) {
    return http.delete(`/api/bugs/${id}`)
  }
}

export default BugApi