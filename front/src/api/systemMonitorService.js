import { http } from '@/utils/request';

export const SystemMonitorApi = {
  // 系统状态相关API
  getSystemStatus: () => {
    return http.get('/api/system/monitor/status');
  },

  getServerMetrics: () => {
    return http.get('/api/system/monitor/server/metrics');
  },

  getDatabaseMetrics: () => {
    return http.get('/api/system/monitor/database/metrics');
  },

  getTestEngineMetrics: () => {
    return http.get('/api/system/monitor/test-engine/metrics');
  },

  getResourceTrend: (metricsType, startTime, endTime) => {
    return http.get('/api/system/monitor/resource/trend', {
      params: {
        metricsType,
        startTime,
        endTime
      }
    });
  },

  // 系统日志相关API
  getOperationLogs: (page, size, params) => {
    const queryParams = {
      page,
      size,
      ...params
    };

    return http.get('/api/system/monitor/logs/operation', {
      params: queryParams,
      paramsSerializer: {
        indexes: null // 确保数组参数被正确序列化
      }
    });
  },

  getOperationLogDetail: (id) => {
    return http.get(`/api/system/monitor/logs/operation/${id}`);
  },

  exportOperationLog: (params) => {
    return http.post('/api/system/monitor/logs/operation/export', params);
  },

  getErrorLogs: (page, size, params) => {
    const queryParams = {
      page,
      size,
      ...params
    };

    return http.get('/api/system/monitor/logs/error', {
      params: queryParams,
      paramsSerializer: {
        indexes: null // 确保数组参数被正确序列化
      }
    });
  },

  getErrorLogDetail: (id) => {
    return http.get(`/api/system/monitor/logs/error/${id}`);
  },

  exportErrorLog: (params) => {
    return http.post('/api/system/monitor/logs/error/export', params);
  },

  // 性能日志相关API
  getPerformanceLogs: (page, size, params) => {
    const queryParams = {
      page,
      size,
      ...params
    };

    return http.get('/api/system/monitor/logs/performance', {
      params: queryParams,
      paramsSerializer: {
        indexes: null // 确保数组参数被正确序列化
      }
    });
  },

  getPerformanceLogDetail: (id) => {
    return http.get(`/api/system/monitor/logs/performance/${id}`);
  },

  exportPerformanceLog: (params) => {
    return http.post('/api/system/monitor/logs/performance/export', params);
  },

  // 系统清理相关API
  cleanupSystem: (params) => {
    return http.post('/api/system/monitor/cleanup', params);
  }
};

export const SystemBackupApi = {
  // 备份列表
  getBackupList: (page, size, params) => {
    return http.get('/api/system/backup/list', {
      params: {
        page,
        size,
        ...params
      }
    });
  },

  // 获取备份详情
  getBackupDetail: (id) => {
    return http.get(`/api/system/backup/${id}`);
  },

  // 创建备份
  createBackup: (data) => {
    return http.post('/api/system/backup', data);
  },

  // 恢复备份
  restoreBackup: (id) => {
    return http.post(`/api/system/backup/${id}/restore`);
  },

  // 删除备份
  deleteBackup: (id) => {
    return http.delete(`/api/system/backup/${id}`);
  },

  // 批量删除备份
  batchDeleteBackup: (ids) => {
    return http.delete('/api/system/backup/batch', { data: ids });
  },

  // 下载备份
  downloadBackup: (id) => {
    return `/api/system/backup/${id}/download`; // 直接返回下载URL
  }
};