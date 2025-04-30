import { http } from '@/utils/request'

export const UiTestConfigApi = {
  // 获取测试配置列表
  getTestConfigs: () => {
    return http.get('/api/uitestconfigs')
  },

  // 获取特定配置
  getTestConfig: (configType) => {
    return http.get(`/api/uitestconfigs/${configType}`)
  },

  // 更新浏览器/设备配置
  updateBrowserConfig: (data) => {
    return http.put('/api/uitestconfigs/browser', data)
  },

  // 更新全局等待时间配置
  updateWaitTimeConfig: (data) => {
    return http.put('/api/uitestconfigs/waittime', data)
  },

  // 更新截图策略配置
  updateScreenshotConfig: (data) => {
    return http.put('/api/uitestconfigs/screenshot', data)
  },

  // 更新失败重试配置
  updateRetryConfig: (data) => {
    return http.put('/api/uitestconfigs/retry', data)
  },

  // 添加新的浏览器配置
  addBrowser: (data) => {
    return http.post('/api/uitestconfigs/browser', data)
  },

  // 删除浏览器配置
  deleteBrowser: (id) => {
    return http.delete(`/api/uitestconfigs/browser/${id}`)
  },

  // 获取浏览器列表
  getBrowserList: () => {
    return http.get('/api/uitestconfigs/browser/list')
  },

  // 更新系统默认配置
  updateDefaultConfig: (data) => {
    return http.put('/api/uitestconfigs/default', data)
  }
}

export default UiTestConfigApi 