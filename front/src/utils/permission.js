/**
 * 权限检查工具函数
 * 用于检查当前用户是否拥有指定的权限
 */

import store from '@/store'

/**
 * 检查用户是否拥有指定权限
 * @param {string} permission 权限标识
 * @returns {boolean} 是否拥有权限
 */
export function checkPermission(permission) {
  // 如果没有传入权限标识或传入空字符串，默认有权限
  if (!permission || permission === '') {
    return true
  }

  // 从store中获取当前用户权限
  const userPermissions = store.state.user ? store.state.user.permissions : []
  
  // 如果没有获取到权限列表或权限列表为空，则认为没有权限
  if (!userPermissions || userPermissions.length === 0) {
    return false
  }

  // 判断是否有指定权限
  return userPermissions.includes(permission)
}

// 简单实现，实际可能需要更复杂的权限判断逻辑
export function hasRole(role) {
  const userRoles = store.state.user ? store.state.user.roles : []
  return userRoles.includes(role)
}

export default {
  checkPermission,
  hasRole
} 