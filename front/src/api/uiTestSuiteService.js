import { http } from '@/utils/request'

export const UiTestSuiteApi = {
  // 获取UI测试套件列表
  getUiTestSuiteList: (params) => {
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
  
    return http.get('/api/uitestsuites/list', cleanParams);
  },
  
  // 获取UI测试套件详情
  getUiTestSuiteDetail: (id) => {
    return http.get(`/api/uitestsuites/${id}`);
  },
  
  // 创建UI测试套件
  createUiTestSuite: (data) => {
    return http.post('/api/uitestsuites/create', data);
  },
  
  // 更新UI测试套件
  updateUiTestSuite: (id, data) => {
    return http.put(`/api/uitestsuites/update/${id}`, data);
  },
  
  // 删除UI测试套件
  deleteUiTestSuite: (id) => {
    return http.delete(`/api/uitestsuites/delete/${id}`);
  },
  
  // 执行UI测试套件
  executeUiTestSuite: (id) => {
    return http.post(`/api/uitestsuites/execute/${id}`);
  },
  
  // 获取套件中的测试用例
  getSuiteCases: (suiteId) => {
    return http.get(`/api/uitestsuites/${suiteId}/cases`);
  },
  
  // 更新套件中的测试用例
  updateSuiteCases: (suiteId, data) => {
    return http.put(`/api/uitestsuites/${suiteId}/cases`, data);
  },
  
  // 更新测试用例执行顺序
  updateCasesOrder: (suiteId, data) => {
    return http.put(`/api/uitestsuites/${suiteId}/cases-order`, data);
  },
  
  // 更新并发配置
  updateConcurrencyConfig: (suiteId, data) => {
    return http.put(`/api/uitestsuites/${suiteId}/concurrency`, data);
  },
  
  // 获取定时任务配置
  getScheduleConfig: (suiteId) => {
    return http.get(`/api/uitestsuites/${suiteId}/schedule`);
  },
  
  // 创建或更新定时任务配置
  saveScheduleConfig: (suiteId, data) => {
    return http.post(`/api/uitestsuites/${suiteId}/schedule`, data);
  },
  
  // 删除定时任务配置
  deleteScheduleConfig: (suiteId) => {
    return http.delete(`/api/uitestsuites/${suiteId}/schedule`);
  }
}; 