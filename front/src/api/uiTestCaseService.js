import { http } from '@/utils/request'

/**
 * UI测试用例API服务
 */
export const UiTestCaseApi = {
  /**
   * 获取UI测试用例列表
   * @param {Object} params 查询参数，包括page、pageSize、keyword、status、projectId
   * @returns {Promise} 返回Promise对象
   */
  getUiTestCaseList: (params) => {
    // 构造干净的查询参数对象
    const cleanParams = {
      page: params.page || 1, // 默认值为 1
      pageSize: params.pageSize || 10, // 默认值为 10
    };
  
    // 仅在有值时添加其他参数
    if (params.keyword) {
      cleanParams.keyword = params.keyword;
    }
  
    if (params.status) {
      cleanParams.status = params.status;
    }
    
    // 添加项目ID参数（如果提供了的话）
    if (params.projectId) {
      cleanParams.projectId = params.projectId;
    }
  
    return http.get('/api/uitestcases/list', { params: cleanParams });
  },

  /**
   * 获取UI测试用例详情
   * @param {String} id 用例ID
   * @param {Number} projectId 项目ID（可选）
   * @returns {Promise} 返回Promise对象
   */
  getUiTestCaseDetail(id, projectId) {
    const params = {};
    if (projectId) params.projectId = projectId;
    
    return http.get(`/api/uitestcases/${id}`, { params });
  },

  /**
   * 创建UI测试用例
   * @param {Object} data 用例数据，应包含projectId字段
   * @returns {Promise} 返回Promise对象
   */
  createUiTestCase(data) {
    return http.post('/api/uitestcases', data);
  },

  /**
   * 更新UI测试用例
   * @param {String} id 用例ID
   * @param {Object} data 用例数据，应包含projectId字段
   * @returns {Promise} 返回Promise对象
   */
  updateUiTestCase(id, data) {
    return http.put(`/api/uitestcases/${id}`, data);
  },

  /**
   * 删除UI测试用例
   * @param {String} id 用例ID
   * @param {Number} projectId 项目ID（可选）
   * @returns {Promise} 返回Promise对象
   */
  deleteUiTestCase(id, projectId) {
    const params = {};
    if (projectId) params.projectId = projectId;
    
    return http.delete(`/api/uitestcases/${id}`, { params });
  },

  /**
   * 执行UI测试用例
   * @param {String} id 用例ID
   * @param {Number} projectId 项目ID（可选）
   * @returns {Promise} 返回Promise对象
   */
  executeUiTestCase(id, projectId) {
    const data = {};
    if (projectId) data.projectId = projectId;
    
    return http.post(`/api/uitestcases/${id}/execute`, data);
  }
};

export default UiTestCaseApi 