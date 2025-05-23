// api/TestcaseService.js
import { http } from '@/utils/request'
import { useProjectStore } from '@/store/projectStore'

export const TestcaseApi = {
  // 获取所有测试用例列表
  getTestcases: (params) => {
    // 确保params是对象
    params = params || {};

    // 获取当前项目ID
    const { getCurrentProjectId } = useProjectStore();
    const projectId = getCurrentProjectId();

    // 构造干净的查询参数对象
    const cleanParams = {
      page: params.page || 1, // 默认值为 1
      pageSize: params.pageSize || 10, // 默认值为 10
      projectId: projectId // 添加项目ID
    };

    // 仅在 status 有值时添加
    if (params.caseStatus) {
      cleanParams.caseStatus = params.caseStatus;
    }

    return http.get('/api/testcases/list', {
      params: {
        page: cleanParams.page,
        pageSize: cleanParams.pageSize,
        caseStatus: cleanParams.caseStatus,
        projectId: cleanParams.projectId // 传递项目ID
      }
    });
  },

  // 获取单个测试用例详情
  getTestcase(id) {
    return http.get(`/api/testcases/${id}`)
  },

  // 创建新测试用例
  createTestcase(data) {
    // 添加项目ID
    const { getCurrentProjectId } = useProjectStore();
    const projectId = getCurrentProjectId();
    data.projectId = projectId;

    return http.post('/api/testcases', data)
  },

  // 更新测试用例信息
  updateTestcase(id, data) {
    return http.put(`/api/testcases/${id}`, data)
  },

  // 删除测试用例
  deleteTestcase(id) {
    return http.delete(`/api/testcases/${id}`)
  },

  // 更新测试用例执行结果
  updateTestcaseExecution(id, data) {
    return http.put(`/api/testcases/${id}/execution`, data)
  }
}

export default TestcaseApi