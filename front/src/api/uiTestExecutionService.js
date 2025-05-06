import { http } from '@/utils/request'

export const UiTestExecutionApi = {
  // 获取测试执行记录列表
  getExecutionList: (params) => {
    // 构造干净的查询参数对象
    const cleanParams = {
      page: params.page || 1,
      pageSize: params.pageSize || 10,
    };
  
    // 仅在有值时添加其他参数
    if (params.keyword) {
      cleanParams.keyword = params.keyword;
    }
  
    if (params.status) {
      cleanParams.status = params.status;
    }

    if (params.startTime) {
      cleanParams.startTime = params.startTime;
    }

    if (params.endTime) {
      cleanParams.endTime = params.endTime;
    }
  
    return http.get('/api/ui-test-executions/list', cleanParams);
  },
  
  // 获取测试执行详情
  getExecutionDetail: (id) => {
    return http.get(`/api/ui-test-executions/${id}`);
  },
  
  // 获取执行步骤
  getExecutionSteps: (id) => {
    return http.get(`/api/ui-test-executions/details/${id}/steps`);
  },
  
  // 启动测试执行
  startExecution: (data) => {
    return http.post('/api/ui-test-executions/create', data);
  },
  
  // 停止测试执行
  stopExecution: (id) => {
    return http.post(`/api/ui-test-executions/${id}/abort`);
  },
  
  // 重新执行测试
  rerunExecution: (id, data) => {
    const params = data || {};
    return http.post(`/api/ui-test-executions/${id}/start`, params);
  },
  
  // 获取执行日志
  getExecutionLogs: (id, params) => {
    return http.get(`/api/ui-test-executions/${id}/logs`, params ? { params } : {});
  },
  
  // 获取执行队列
  getExecutionQueue: (params = { page: 1, pageSize: 10 }) => {
    const cleanParams = {
      page: params.page || 1,
      pageSize: params.pageSize || 10
    };
    
    if (params.status) {
      cleanParams.status = params.status;
    }
    
    return http.get('/api/ui-test-executions/queue', cleanParams);
  },
  
  // 获取可用的执行环境
  getExecutionEnvironments: () => {
    return http.get('/api/ui-test-executions/environments');
  }
}; 