import { http } from '@/utils/request'

/**
 * 系统信息相关API
 */
export const SysInfoApi = {
  /**
   * 获取系统基本信息
   * @returns {Promise}
   */
  getSystemInfo() {
    return http.get('/api/system/info/about')
  },

  /**
   * 更新系统信息
   * @param {Object} data 系统信息数据
   * @returns {Promise}
   */
  updateSystemInfo(data) {
    return http.put('/api/system/info/about', data)
  },

  /**
   * 获取服务条款
   * @returns {Promise}
   */
  getTermsOfService() {
    return http.get('/api/system/info/about/terms')
  },

  /**
   * 获取隐私政策
   * @returns {Promise}
   */
  getPrivacyPolicy() {
    return http.get('/api/system/info/about/privacy')
  },

  /**
   * 获取当前版本信息
   * @returns {Promise}
   */
  getCurrentVersion() {
    return http.get('/api/system/info/version/current')
  },

  /**
   * 获取版本历史
   * @returns {Promise}
   */
  getVersionHistory() {
    return http.get('/api/system/info/version/history')
  },

  /**
   * 添加版本信息
   * @param {Object} data 版本信息数据
   * @returns {Promise}
   */
  addVersionInfo(data) {
    return http.post('/api/system/info/version', data)
  },

  /**
   * 更新版本信息
   * @param {Object} data 版本信息数据
   * @returns {Promise}
   */
  updateVersionInfo(data) {
    return http.put('/api/system/info/version', data)
  },

  /**
   * 设置当前版本
   * @param {string|number} id 版本ID
   * @returns {Promise}
   */
  setCurrentVersion(id) {
    return http.put(`/api/system/info/version/${id}/current`)
  },

  /**
   * 获取当前许可证信息
   * @returns {Promise}
   */
  getCurrentLicense() {
    return http.get('/api/system/info/license/current')
  },

  /**
   * 获取许可证列表
   * @returns {Promise}
   */
  getLicenseList() {
    return http.get('/api/system/info/license/list')
  },

  /**
   * 添加许可证
   * @param {Object} data 许可证数据
   * @returns {Promise}
   */
  addLicense(data) {
    return http.post('/api/system/info/license', data)
  },

  /**
   * 更新许可证信息
   * @param {Object} data 许可证数据
   * @returns {Promise}
   */
  updateLicense(data) {
    return http.put('/api/system/info/license', data)
  },

  /**
   * 验证许可证密钥
   * @param {string} licenseKey 许可证密钥
   * @returns {Promise}
   */
  validateLicenseKey(licenseKey) {
    return http.post('/api/system/info/license/validate', { licenseKey })
  },

  /**
   * 获取最近的升级记录
   * @returns {Promise}
   */
  getLatestUpgradeRecord() {
    return http.get('/api/system/info/upgrade/latest')
  },

  /**
   * 获取升级记录列表
   * @param {number} page 页码
   * @param {number} pageSize 每页大小
   * @param {string} status 过滤状态
   * @returns {Promise}
   */
  getUpgradeRecordList(page = 1, pageSize = 10, status = '') {
    return http.get('/api/system/info/upgrade/list', { 
      params: { page, pageSize, status }
    })
  },

  /**
   * 获取升级记录详情
   * @param {string|number} id 升级记录ID
   * @returns {Promise}
   */
  getUpgradeRecordDetail(id) {
    return http.get(`/api/system/info/upgrade/${id}`)
  },

  /**
   * 执行系统升级
   * @param {Object} data 升级配置数据
   * @returns {Promise}
   */
  executeUpgrade(data) {
    return http.post('/api/system/info/upgrade', data)
  }
}

export default SysInfoApi; 