// api/TestcaseService.js
import { http } from '@/utils/request'

export const TestcaseApi = {
  // 获取所有测试计划列表
  getTestcases: (params) => {
    // 构造干净的查询参数对象
    const cleanParams = {
      page: params.page || 1, // 默认值为 1
      pageSize: params.pageSize || 10, // 默认值为 10
    };
  
    // 仅在 status 有值时添加
    if (params.caseStatus) {
      cleanParams.caseStatus = params.caseStatus;
    }
  
    return http.get('/api/testcases/list', {
      page: cleanParams.page, // 使用清理后的参数
      pageSize: cleanParams.pageSize, // 使用清理后的参数
      caseStatus: cleanParams.caseStatus,
    });
  },

  // 获取单个测试计划详情
  getTestcase(id) {
    return http.get(`/api/testcases/${id}`)
  },

  // 创建新测试计划
  createTestcase(data) {
    return http.post('/api/testcases', data)
  },

  // 更新测试计划信息
  updateTestcase(id, data) {
    return http.put(`/api/testcases/${id}`, data)
  },

  // 删除测试计划
  deleteTestcase(id) {
    return http.delete(`/api/testcases/${id}`)
  }
}

export default TestcaseApi