import { http } from '@/utils/request'

/**
 * UI测试用例API服务
 */
export const UiTestCaseApi = {
  /**
   * 获取UI测试用例列表
   * @param {Object} params 查询参数
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
  
    return http.get('/api/uitestcases/list', cleanParams);
  },

  /**
   * 获取UI测试用例详情
   * @param {String} caseId 用例ID
   * @returns {Promise} 返回Promise对象
   */
  getUiTestCaseDetail(id) {
    return http.get(`/api/uitestcases/${id}`);
  },

  /**
   * 创建UI测试用例
   * @param {Object} data 用例数据
   * @returns {Promise} 返回Promise对象
   */
  createUiTestCase(data) {
    return http.post('/api/uitestcases', data);
  },

  /**
   * 更新UI测试用例
   * @param {String} caseId 用例ID
   * @param {Object} data 用例数据
   * @returns {Promise} 返回Promise对象
   */
  updateUiTestCase(id, data) {
    return http.put(`/api/uitestcases/${id}`, data);
  },

  /**
   * 删除UI测试用例
   * @param {String} caseId 用例ID
   * @returns {Promise} 返回Promise对象
   */
  deleteUiTestCase(id) {
    return http.delete(`/api/uitestcases/${id}`);
  },

  /**
   * 执行UI测试用例
   * @param {String} caseId 用例ID
   * @returns {Promise} 返回Promise对象
   */
  executeUiTestCase(id) {
    return http.post(`/api/uitestcases/${id}/execute`);
  }
};

export default UiTestCaseApi 