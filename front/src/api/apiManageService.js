// api/BugService.js
import { http } from '@/utils/request'
import { useProjectStore } from '@/store/projectStore';

export const ApiManageApi = {
  // 获取所有接口
  getApis: () => {
    // 添加项目ID参数
    const projectStore = useProjectStore();
    const params = {};
    if (projectStore.currentProjectId) {
      params.projectId = projectStore.currentProjectId;
    }
    return http.get(`/api/apis`, params);
  },

  // 获取接口列表（带分页）
  getApiList: (params) => {
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
    
    console.log("接口管理API调用参数:", cleanParams);
    return http.get('/api/apis/list', cleanParams);
  },

  // 获取单个接口详情
  getApiDetail(id) {
    return http.get(`/api/apis/${id}`);
  },

  // 创建新的接口
  createApi(data) {
    // 添加项目ID
    const projectStore = useProjectStore();
    if (projectStore.currentProjectId) {
      data.projectId = projectStore.currentProjectId;
    }
    return http.post('/api/apis', data);
  },

  // 更新接口信息
  updateApi(id, data) {
    // 添加项目ID
    const projectStore = useProjectStore();
    if (projectStore.currentProjectId) {
      data.projectId = projectStore.currentProjectId;
    }
    return http.put(`/api/apis/${id}`, data);
  },

  // 删除接口
  deleteApi(id) {
    return http.delete(`/api/apis/${id}`);
  }
}

export default ApiManageApi