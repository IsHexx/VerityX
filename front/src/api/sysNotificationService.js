import { http } from '@/utils/request';

/**
 * 通知模板API
 */
export const NotificationTemplateApi = {
  // 获取通知模板列表（分页）
  getTemplateList: (params) => {
    return http.get('/api/sys/notification-templates', params);
  },
  
  // 获取通知模板详情
  getTemplate: (id) => {
    return http.get(`/api/sys/notification-templates/${id}`);
  },
  
  // 根据编码获取通知模板
  getTemplateByCode: (code) => {
    return http.get(`/api/sys/notification-templates/code/${code}`);
  },
  
  // 创建通知模板
  createTemplate: (data) => {
    return http.post('/api/sys/notification-templates', data);
  },
  
  // 更新通知模板
  updateTemplate: (data) => {
    return http.put('/api/sys/notification-templates', data);
  },
  
  // 删除通知模板
  deleteTemplate: (id) => {
    return http.delete(`/api/sys/notification-templates/${id}`);
  },
  
  // 批量删除通知模板
  batchDeleteTemplate: (ids) => {
    return http.post('/api/sys/notification-templates/batch', ids);
  },
  
  // 修改通知模板状态
  updateTemplateStatus: (id, status) => {
    return http.put(`/api/sys/notification-templates/${id}/status?status=${status}`);
  }
};

/**
 * 通知规则API
 */
export const NotificationRuleApi = {
  // 获取通知规则列表（分页）
  getRuleList: (params) => {
    return http.get('/api/sys/notification-rules', params);
  },
  
  // 获取通知规则详情
  getRule: (id) => {
    return http.get(`/api/sys/notification-rules/${id}`);
  },
  
  // 根据编码获取通知规则
  getRuleByCode: (code) => {
    return http.get(`/api/sys/notification-rules/code/${code}`);
  },
  
  // 根据事件类型和事件动作获取规则列表
  getRulesByEvent: (eventType, eventAction) => {
    return http.get(`/api/sys/notification-rules/event?eventType=${eventType}&eventAction=${eventAction}`);
  },
  
  // 创建通知规则
  createRule: (data) => {
    return http.post('/api/sys/notification-rules', data);
  },
  
  // 更新通知规则
  updateRule: (data) => {
    return http.put('/api/sys/notification-rules', data);
  },
  
  // 删除通知规则
  deleteRule: (id) => {
    return http.delete(`/api/sys/notification-rules/${id}`);
  },
  
  // 批量删除通知规则
  batchDeleteRule: (ids) => {
    return http.post('/api/sys/notification-rules/batch', ids);
  },
  
  // 修改通知规则状态
  updateRuleStatus: (id, status) => {
    return http.put(`/api/sys/notification-rules/${id}/status?status=${status}`);
  }
};

/**
 * 用户订阅API
 */
export const UserSubscriptionApi = {
  // 获取用户订阅列表（分页）
  getUserSubscriptionList: (params) => {
    return http.get('/api/sys/user-subscriptions', params);
  },
  
  // 获取用户订阅详情
  getUserSubscription: (id) => {
    return http.get(`/api/sys/user-subscriptions/${id}`);
  },
  
  // 根据用户ID获取订阅列表
  getUserSubscriptionByUserId: (userId) => {
    return http.get(`/api/sys/user-subscriptions/user/${userId}`);
  },
  
  // 根据规则ID获取用户订阅列表
  getUserSubscriptionByRuleId: (ruleId) => {
    return http.get(`/api/sys/user-subscriptions/rule/${ruleId}`);
  },
  
  // 创建用户订阅
  createUserSubscription: (data) => {
    return http.post('/api/sys/user-subscriptions', data);
  },
  
  // 更新用户订阅
  updateUserSubscription: (data) => {
    return http.put('/api/sys/user-subscriptions', data);
  },
  
  // 删除用户订阅
  deleteUserSubscription: (id) => {
    return http.delete(`/api/sys/user-subscriptions/${id}`);
  },
  
  // 批量删除用户订阅
  batchDeleteUserSubscription: (ids) => {
    return http.post('/api/sys/user-subscriptions/batch', ids);
  },
  
  // 修改用户订阅状态
  updateUserSubscriptionStatus: (id, status) => {
    return http.put(`/api/sys/user-subscriptions/${id}/status?status=${status}`);
  }
};

/**
 * 部门订阅API
 */
export const DeptSubscriptionApi = {
  // 获取部门订阅列表（分页）
  getDeptSubscriptionList: (params) => {
    return http.get('/api/sys/dept-subscriptions', params);
  },
  
  // 获取部门订阅详情
  getDeptSubscription: (id) => {
    return http.get(`/api/sys/dept-subscriptions/${id}`);
  },
  
  // 根据部门ID获取订阅列表
  getDeptSubscriptionByDeptId: (deptId) => {
    return http.get(`/api/sys/dept-subscriptions/dept/${deptId}`);
  },
  
  // 根据规则ID获取部门订阅列表
  getDeptSubscriptionByRuleId: (ruleId) => {
    return http.get(`/api/sys/dept-subscriptions/rule/${ruleId}`);
  },
  
  // 创建部门订阅
  createDeptSubscription: (data) => {
    return http.post('/api/sys/dept-subscriptions', data);
  },
  
  // 更新部门订阅
  updateDeptSubscription: (data) => {
    return http.put('/api/sys/dept-subscriptions', data);
  },
  
  // 删除部门订阅
  deleteDeptSubscription: (id) => {
    return http.delete(`/api/sys/dept-subscriptions/${id}`);
  },
  
  // 批量删除部门订阅
  batchDeleteDeptSubscription: (ids) => {
    return http.post('/api/sys/dept-subscriptions/batch', ids);
  },
  
  // 修改部门订阅状态
  updateDeptSubscriptionStatus: (id, status) => {
    return http.put(`/api/sys/dept-subscriptions/${id}/status?status=${status}`);
  }
};

/**
 * 用户API
 */
export const UserApi = {
  // 获取用户列表（分页）
  getUserList: (params) => {
    return http.get('/api/sys/users', params);
  },
  
  // 获取用户详情
  getUser: (id) => {
    return http.get(`/api/sys/users/${id}`);
  }
}; 