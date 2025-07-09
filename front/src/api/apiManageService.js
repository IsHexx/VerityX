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

  // 获取单个接口详情（使用新的v2 API）
  getApiDetail(id) {
    return http.get(`/api/v2/endpoints/${id}`);
  },

  // 创建新的接口
  createApi(data) {
    // 添加项目ID
    const projectStore = useProjectStore();
    if (projectStore.currentProjectId) {
      data.projectId = parseInt(projectStore.currentProjectId);
    }
    console.log('====== 前端发送创建接口请求 ======');
    console.log('原始数据:', data);
    console.log('项目ID:', projectStore.currentProjectId);
    console.log('转换后的项目ID:', data.projectId);
    console.log('接口目录:', data.apiDirectory);
    console.log('接口名称:', data.apiName);
    console.log('请求方法:', data.requestMethod);
    console.log('接口路径:', data.apiPath);
    console.log('状态:', data.status);
    return http.post('/api/apis', data);
  },

  // 更新接口信息（使用新的v2 API）
  updateApi(id, data) {
    console.log('====== 前端发送更新接口请求 ======');
    console.log('接口ID:', id);
    console.log('更新数据:', data);

    // 转换数据格式以适配新的API
    const endpointData = {
      moduleId: data.moduleId || data.module_id,  // 必须包含module_id
      name: data.apiName || data.name,
      path: data.apiPath || data.path,
      method: data.requestMethod || data.method,
      description: data.description,
      status: data.status || 'active',
      mockUrl: data.mockUrl,
      tags: data.tags,
      preUrl: data.preUrl,
      owner: data.owner,
      // 参数相关字段
      pathParams: data.pathParams,
      queryParams: data.queryParams,
      headerParams: data.headerParams
    };

    // 如果moduleId仍然为空，抛出错误
    if (!endpointData.moduleId) {
      console.error('moduleId不能为空！当前数据:', data);
      throw new Error('moduleId不能为空，请确保接口属于某个模块');
    }

    console.log('转换后的接口数据:', endpointData);
    return http.put(`/api/v2/endpoints/${id}`, endpointData);
  },

  // 删除接口
  deleteApi(id) {
    return http.delete(`/api/apis/${id}`);
  },

  // 新增：获取树形结构（使用新的v2接口）
  getTreeStructure(projectId) {
    const params = {};
    if (projectId) {
      params.projectId = projectId;
    } else {
      // 如果没有传入项目ID，从store获取
      const projectStore = useProjectStore();
      if (projectStore.currentProjectId) {
        params.projectId = projectStore.currentProjectId;
      }
    }
    console.log('获取树形结构API调用参数:', params);
    return http.get('/api/v2/tree', { params });
  },

  // 新增：创建目录
  createDirectory(data) {
    const projectStore = useProjectStore();
    if (projectStore.currentProjectId) {
      data.projectId = projectStore.currentProjectId;
    }
    console.log('创建目录API调用参数:', data);
    return http.post('/api/apis/directories', data);
  },

  // 新增：删除目录
  deleteDirectory(id) {
    return http.delete(`/api/apis/directories/${id}`);
  },

  // 新增：获取目录列表
  getDirectories(projectId) {
    const params = {};
    if (projectId) {
      params.projectId = projectId;
    } else {
      const projectStore = useProjectStore();
      if (projectStore.currentProjectId) {
        params.projectId = projectStore.currentProjectId;
      }
    }
    return http.get('/api/apis/directories', { params });
  },

  // ==================== 新的v2 API方法 ====================

  // v2: 创建模块
  createModule(data) {
    const projectStore = useProjectStore();
    if (projectStore.currentProjectId) {
      data.projectId = parseInt(projectStore.currentProjectId);
    }
    console.log('创建模块API调用参数:', data);
    return http.post('/api/v2/modules', data);
  },

  // v2: 更新模块
  updateModule(id, data) {
    console.log('更新模块API调用参数:', { id, data });
    return http.put(`/api/v2/modules/${id}`, data);
  },

  // v2: 删除模块
  deleteModule(id) {
    console.log('删除模块API调用参数:', { id });
    return http.delete(`/api/v2/modules/${id}`);
  },

  // v2: 创建接口
  createEndpoint(data) {
    console.log('创建接口API调用参数:', data);
    return http.post('/api/v2/endpoints', data);
  },

  // v2: 更新接口
  updateEndpoint(id, data) {
    console.log('更新接口API调用参数:', { id, data });
    return http.put(`/api/v2/endpoints/${id}`, data);
  },

  // v2: 删除接口
  deleteEndpoint(id) {
    console.log('删除接口API调用参数:', { id });
    return http.delete(`/api/v2/endpoints/${id}`);
  }
}

export default ApiManageApi