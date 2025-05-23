// api/TestreportService.js
import { http } from '@/utils/request'
import { useProjectStore } from '@/store/projectStore'

export const TestReportApi = {
  // 获取所有测试报告列表
  getTestreports: (params) => {
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

    return http.get('/api/testreports/list', {
      params: {
        page: cleanParams.page,
        pageSize: cleanParams.pageSize,
        projectId: cleanParams.projectId // 传递项目ID
      }
    });
  },

  // 获取单个测试报告详情
  getTestreport(id) {
    return http.get(`/api/testreports/${id}`)
  },

  // 创建新测试报告
  createTestreport(data) {
    // 添加项目ID
    const { getCurrentProjectId } = useProjectStore();
    const projectId = getCurrentProjectId();
    data.projectId = projectId;

    return http.post('/api/testreports', data)
  },

  // 更新测试报告信息
  updateTestreport(id, data) {
    return http.put(`/api/testreports/${id}`, data)
  },

  // 删除测试报告
  deleteTestreport(id) {
    return http.delete(`/api/testreports/${id}`)
  },

  // 获取测试报告聚合信息（包括测试计划、用例和缺陷）
  getAggregatedReportInfo(id) {
    return http.get(`/api/testreports/${id}/aggregated`)
  },

  // 从测试计划生成测试报告
  generateReportFromTestPlan(planId, data) {
    return http.post(`/api/testreports/generate/${planId}`, data)
  }
}

export default TestReportApi