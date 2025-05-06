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
  
    return http.get('/api/ui-elements/list', cleanParams);
  },
  
  // 获取元素详情
  getElementById: (id) => {
    return http.get(`/api/ui-elements/${id}`);
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
  deleteElement: (id) => {
    return http.delete(`/api/ui-elements/delete/${id}`);
  },
  
  // 获取元素分组列表
  getElementGroups: () => {
    return http.get('/api/ui-elements/groups');
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
  deleteElementGroup: (id) => {
    return http.delete(`/api/ui-elements/groups/delete/${id}`);
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
  uploadElementScreenshot: (elementId, formData) => {
    return http.post(`/api/ui-elements/${elementId}/screenshot`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });
  }
}; 