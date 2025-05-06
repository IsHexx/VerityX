import { http } from '@/utils/request'

export const UiTestReportApi = {
  // 获取UI测试报告列表
  getUiTestReports: (params) => {
    // 构造干净的查询参数对象，只包含非空值
    const cleanParams = {};
    
    // 只添加有值的参数
    if (params.page) cleanParams.page = params.page;
    if (params.pageSize) cleanParams.pageSize = params.pageSize;
    if (params.keyword) cleanParams.keyword = params.keyword;
    if (params.status) cleanParams.status = params.status;
    if (params.startDate) cleanParams.startDate = params.startDate;
    if (params.endDate) cleanParams.endDate = params.endDate;
    
    console.log("UI测试报告API调用参数:", cleanParams);
    return http.get('/api/ui-test-reports/list', cleanParams);
  },

  // 获取单个UI测试报告详情
  getUiTestReportDetail(id) {
    return http.get(`/api/ui-test-reports/${id}`)
  },

  // 删除UI测试报告
  deleteUiTestReport(id) {
    return http.delete(`/api/ui-test-reports/${id}`)
  },

  // 导出UI测试报告为HTML
  exportUiTestReportHtml(id) {
    return http.get(`/api/ui-test-reports/${id}/export/html`, { 
      responseType: 'blob' 
    })
  },
  
  // 导出UI测试报告为PDF
  exportUiTestReportPdf(id) {
    return http.get(`/api/ui-test-reports/${id}/export/pdf`, { 
      responseType: 'blob' 
    })
  },

  // 批量删除UI测试报告
  batchDeleteUiTestReports(ids) {
    return http.post('/api/ui-test-reports/batch-delete', { ids })
  }
}

export default UiTestReportApi 