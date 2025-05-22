import { http } from '@/utils/request'

export const UiElementLocatorApi = {
  // 获取元素列表（支持分页、分组和搜索）
  getElementList: (params) => {
    // 构造干净的查询参数对象
    const cleanParams = {
      page: params.page || 1, // 默认值为 1
      pageSize: params.pageSize || 10, // 默认值为 10
    };
  
    // 仅在有值时添加其他参数
    if (params.keyword) {
      cleanParams.keyword = params.keyword;
    }
  
    if (params.groupId) {
      cleanParams.groupId = params.groupId;
    }

    if (params.locatorType) {
      cleanParams.locatorType = params.locatorType;
    }
    
    // 添加项目ID参数（如果提供了的话）
    if (params.projectId) {
      cleanParams.projectId = params.projectId;
    }
  
    return http.get('/api/ui-elements/list', { params: cleanParams });
  },
  
  // 获取元素详情
  getElementById: (id, projectId) => {
    const params = {};
    if (projectId) params.projectId = projectId;
    
    return http.get(`/api/ui-elements/${id}`, { params });
  },
  
  // 创建元素
  createElement: (data) => {
    return http.post('/api/ui-elements/create', data);
  },
  
  // 更新元素
  updateElement: (id, data) => {
    return http.put(`/api/ui-elements/update/${id}`, data);
  },
  
  // 删除元素
  deleteElement: (id, projectId) => {
    const params = {};
    if (projectId) params.projectId = projectId;
    
    return http.delete(`/api/ui-elements/delete/${id}`, { params });
  },
  
  // 获取元素分组列表
  getElementGroups: (projectId) => {
    const params = {};
    if (projectId) params.projectId = projectId;
    
    return http.get('/api/ui-elements/groups', { params });
  },
  
  // 创建元素分组
  createElementGroup: (data) => {
    return http.post('/api/ui-elements/groups/create', data);
  },
  
  // 更新元素分组
  updateElementGroup: (id, data) => {
    return http.put(`/api/ui-elements/groups/update/${id}`, data);
  },
  
  // 删除元素分组
  deleteElementGroup: (id, projectId) => {
    const params = {};
    if (projectId) params.projectId = projectId;
    
    return http.delete(`/api/ui-elements/groups/delete/${id}`, { params });
  },
  
  // 验证元素定位器
  validateLocator: (data) => {
    return http.post('/api/ui-elements/validate', data);
  },
  
  // 获取智能推荐的元素定位器（占位，未来实现）
  getSmartLocatorSuggestions: (data) => {
    return http.post('/api/ui-elements/smart-suggestions', data);
  },
  
  // 上传元素截图
  uploadElementScreenshot: (elementId, formData, projectId) => {
    const params = {};
    if (projectId) params.projectId = projectId;
    
    return http.post(`/api/ui-elements/${elementId}/screenshot`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      },
      params
    });
  }
}; 