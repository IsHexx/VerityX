// api/testplanService.js
import { http } from '@/utils/request'

// 从localStorage获取当前项目ID
const getCurrentProjectId = () => {
  try {
    const storedProject = localStorage.getItem('currentProject');
    if (storedProject) {
      const projectData = JSON.parse(storedProject);
      return projectData.id || projectData.projectId || null;
    }
  } catch (error) {
    console.error('获取项目ID失败:', error);
  }
  return null;
};

export const testplanApi = {
  // 获取所有测试计划列表
  getTestplans: (params) => {
    // 确保params是对象
    params = params || {};

    // 获取当前项目ID
    const projectId = getCurrentProjectId();
    console.log('测试计划API - 当前项目ID:', projectId);

    // 构造干净的查询参数对象
    const cleanParams = {
      page: params.page || 1, // 默认值为 1
      pageSize: params.pageSize || 10, // 默认值为 10
      projectId: params.projectId || projectId // 优先使用传入的项目ID，否则使用当前项目ID
    };

    // 仅在 status 有值时添加
    if (params.status) {
      cleanParams.status = params.status;
    }

    console.log('测试计划API - 请求参数:', cleanParams);

    return http.get('/api/testplans/list', { params: cleanParams });
  },

  // 获取单个测试计划详情
  getTestplan(id) {
    const projectId = getCurrentProjectId();
    return http.get(`/api/testplans/${id}`, {
      params: { projectId }
    });
  },

  // 创建新测试计划
  createTestplan(data) {
    // 添加项目ID
    if (!data.projectId) {
    const projectId = getCurrentProjectId();
    data.projectId = projectId;
    }
    console.log('创建测试计划 - 数据:', data);

    return http.post('/api/testplans', data);
  },

  // 更新测试计划信息
  updateTestplan(id, data) {
    // 确保有项目ID
    if (!data.projectId) {
      const projectId = getCurrentProjectId();
      data.projectId = projectId;
    }

    return http.put(`/api/testplans/${id}`, data);
  },

  // 删除测试计划
  deleteTestplan(id) {
    const projectId = getCurrentProjectId();
    return http.delete(`/api/testplans/${id}`, {
      params: { projectId }
    });
  },

  // 获取测试计划关联的测试用例
  getAssociatedTestCases(planId) {
    return http.get(`/api/testplans/${planId}/cases`);
  },

  // 关联测试用例到测试计划
  associateTestCases(planId, caseIds) {
    return http.post(`/api/testplans/${planId}/cases`, { caseIds });
  },

  // 从测试计划中移除测试用例
  removeTestCase(planId, caseId) {
    return http.delete(`/api/testplans/${planId}/cases/${caseId}`);
  }
}

export default testplanApi