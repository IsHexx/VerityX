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
  
    return http.get('/api/ui-test-execution/list', cleanParams);
  },
  
  // 获取测试执行详情
  getExecutionDetail: (id) => {
    return http.get(`/api/ui-test-execution/${id}`);
  },
  
  // 启动测试执行
  startExecution: (data) => {
    return http.post('/api/ui-test-execution/start', data);
  },
  
  // 停止测试执行
  stopExecution: (id) => {
    return http.post(`/api/ui-test-execution/${id}/stop`);
  },
  
  // 重新执行测试
  rerunExecution: (id, data) => {
    return http.post(`/api/ui-test-execution/${id}/rerun`, data);
  },
  
  // 获取执行日志
  getExecutionLogs: (id, params) => {
    return http.get(`/api/ui-test-execution/${id}/logs`, { params });
  },
  
  // 获取执行队列
  getExecutionQueue: () => {
    return http.get('/api/ui-test-execution/queue');
  },
  
  // 获取可用的执行环境
  getExecutionEnvironments: () => {
    return http.get('/api/ui-test-execution/environments');
  }
}; 