// api/TestreportService.js
import { http } from '@/utils/request'

export const TestReportApi = {
  // 获取所有测试计划列表
  getTestreports: (params) => {
    // 构造干净的查询参数对象
    const cleanParams = {
      page: params.page || 1, // 默认值为 1
      pageSize: params.pageSize || 10, // 默认值为 10
    };

    return http.get('/api/testreports/list', {
      page: cleanParams.page, // 使用清理后的参数
      pageSize: cleanParams.pageSize, // 使用清理后的参数
    });
  },

  // 获取单个测试计划详情
  getTestreport(id) {
    return http.get(`/api/testreports/${id}`)
  },

  // 创建新测试计划
  createTestreport(data) {
    return http.post('/api/testreports', data)
  },

  // 更新测试计划信息
  updateTestreport(id, data) {
    return http.put(`/api/testreports/${id}`, data)
  },

  // 删除测试计划
  deleteTestreport(id) {
    return http.delete(`/api/testreports/${id}`)
  }
}

export default TestReportApi