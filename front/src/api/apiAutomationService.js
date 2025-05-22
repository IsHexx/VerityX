// api/apiAutomationService.js
import { http } from '@/utils/request'
import { useProjectStore } from '@/store/projectStore';

export const ApiAutomationApi = {
  // 获取所有场景
  getAllScenes: () => {
    // 添加项目ID参数
    const projectStore = useProjectStore();
    const params = {};
    if (projectStore.currentProjectId) {
      params.projectId = projectStore.currentProjectId;
    }
    return http.get('/api/automation', params);
  },

  // 获取场景列表（带分页）
  getSceneList: (params) => {
    // 构造干净的查询参数对象
    const cleanParams = {};
    
    // 只添加有值的参数
    if (params.page) cleanParams.page = params.page;
    if (params.pageSize) cleanParams.pageSize = params.pageSize;
    if (params.keyword) cleanParams.keyword = params.keyword;
    
    // 添加项目ID
    const projectStore = useProjectStore();
    if (projectStore.currentProjectId) {
      cleanParams.projectId = projectStore.currentProjectId;
    }
    
    console.log("场景列表API调用参数:", cleanParams);
    return http.get('/api/automation/list', cleanParams);
  },

  // 获取单个场景详情
  getSceneDetail: (id) => {
    return http.get(`/api/automation/${id}`);
  },

  // 创建新场景
  createScene: (data) => {
    // 添加项目ID
    const projectStore = useProjectStore();
    if (projectStore.currentProjectId) {
      data.projectId = projectStore.currentProjectId;
    }
    return http.post('/api/automation', data);
  },

  // 更新场景
  updateScene: (id, data) => {
    // 添加项目ID
    const projectStore = useProjectStore();
    if (projectStore.currentProjectId) {
      data.projectId = projectStore.currentProjectId;
    }
    return http.put(`/api/automation/${id}`, data);
  },

  // 删除场景
  deleteScene: (id) => {
    return http.delete(`/api/automation/${id}`);
  },

  // 执行场景
  executeScene: (id) => {
    // 添加项目ID参数
    const projectStore = useProjectStore();
    const params = {};
    if (projectStore.currentProjectId) {
      params.projectId = projectStore.currentProjectId;
    }
    return http.post(`/api/automation/${id}/execute`, null, { params });
  }
}

export default ApiAutomationApi 