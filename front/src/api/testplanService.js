// api/testplanService.js
import { http } from '@/utils/request'

export const testplanApi = {
  // 获取所有测试计划列表
  getTestplans() {
    console.log('getTestplan')
    return http.get('/api/testplans')
  },

  // 获取单个测试计划详情
  getTestplan(id) {
    return http.get(`/api/testplans/${id}`)
  },

  // 创建新测试计划
  createTestplan(data) {
    return http.post('/api/testplans', data)
  },

  // 更新测试计划信息
  updateTestplan(id, data) {
    return http.put(`/api/testplans/${id}`, data)
  },

  // 删除测试计划
  deleteTestplan(id) {
    return http.delete(`/api/testplans/${id}`)
  }
}

export default testplanApi