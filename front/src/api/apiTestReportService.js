import { http } from '@/utils/request'
import { useProjectStore } from '@/store/projectStore';

export const ApiTestReportApi = {
  // 获取接口测试报告列表
  getApiTestReports: (params) => {
    // 构造干净的查询参数对象，只包含非空值
    const cleanParams = {};
    
    // 只添加有值的参数
    if (params.page) cleanParams.page = params.page;
    if (params.pageSize) cleanParams.pageSize = params.pageSize;
    if (params.keyword) cleanParams.keyword = params.keyword;
    if (params.status) cleanParams.status = params.status;
    
    // 添加项目ID
    const projectStore = useProjectStore();
    if (projectStore.currentProjectId) {
      cleanParams.projectId = projectStore.currentProjectId;
    }
    
    console.log("API调用参数:", cleanParams);
    return http.get('/api/apitestreports/list', cleanParams);
  },

  // 获取单个接口测试报告详情
  getApiTestReportDetail(id) {
    return http.get(`/api/apitestreports/${id}`)
  },

  // 删除接口测试报告
  deleteApiTestReport(id) {
    return http.delete(`/api/apitestreports/${id}`)
  },

  // 导出接口测试报告
  exportApiTestReport(id) {
    return http.get(`/api/apitestreports/${id}/export`, { 
      responseType: 'blob' 
    })
  },

  // 批量删除接口测试报告
  batchDeleteApiTestReports(ids) {
    return http.post('/api/apitestreports/batch-delete', { ids })
  }
}

export default ApiTestReportApi 