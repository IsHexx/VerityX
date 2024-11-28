// api/BugService.js
import { http } from '@/utils/request'

export const ApiManageApi = {
  // 获取所有接口
  getApis: () => {
    return http.get(`/api/apis`)
  },

  // 获取单个接口详情
  getApiDetail(id) {
    return http.get(`/api/apis/${id}`)
  },

  // 创建新的接口
  createApi(data) {
    return http.post('/api/apis', data)
  },

  // 更新接口信息
  updateApi(id, data) {
    return http.put(`/api/apis/${id}`, data)
  },

  // 删除接口
  deleteApi(id) {
    return http.delete(`/api/apis/${id}`)
  }
}

export default ApiManageApi