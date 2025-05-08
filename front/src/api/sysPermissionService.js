import { http } from '@/utils/request'

export const SysPermissionApi = {
  // 获取所有权限列表
  getAllPermissions: () => {
    return http.get('/api/sys/permissions');
  },
  
  // 根据权限类型获取权限列表
  getPermissionsByType: (permissionType) => {
    return http.get(`/api/sys/permissions/type/${permissionType}`);
  },
  
  // 获取权限详情
  getPermissionById: (id) => {
    return http.get(`/api/sys/permissions/${id}`);
  },
  
  // 创建权限
  createPermission: (data) => {
    // 确保数据格式正确
    const permissionData = {
      permissionName: data.permissionName,
      permissionCode: data.permissionCode,
      permissionType: data.permissionType || 'menu',
      parentId: data.parentId ? Number(data.parentId) : 0,
      routePath: data.routePath || '',
      icon: data.icon || '',
      sort: data.sort ? Number(data.sort) : 0,
      isActive: data.isActive !== false,
      description: data.description || ''
    };
    
    return http.post('/api/sys/permissions', permissionData);
  },
  
  // 更新权限
  updatePermission: (data) => {
    // 确保数据格式正确
    const permissionData = {
      id: data.id ? Number(data.id) : null,
      permissionName: data.permissionName,
      permissionCode: data.permissionCode,
      permissionType: data.permissionType || 'menu',
      parentId: data.parentId ? Number(data.parentId) : 0,
      routePath: data.routePath || '',
      icon: data.icon || '',
      sort: data.sort ? Number(data.sort) : 0,
      isActive: data.isActive !== false,
      description: data.description || ''
    };
    
    return http.put('/api/sys/permissions', permissionData);
  },
  
  // 删除权限
  deletePermission: (id) => {
    return http.delete(`/api/sys/permissions/${id}`);
  },
  
  // 根据父ID获取权限列表
  getPermissionsByParentId: (parentId) => {
    return http.get(`/api/sys/permissions/parent/${parentId}`);
  },
  
  // 根据角色ID获取权限列表
  getPermissionsByRoleId: (roleId) => {
    return http.get(`/api/sys/permissions/role/${roleId}`);
  },
  
  // 根据用户ID获取权限列表
  getPermissionsByUserId: (userId) => {
    return http.get(`/api/sys/permissions/user/${userId}`);
  },
  
  // 获取菜单权限列表
  getMenuPermissions: () => {
    return http.get('/api/sys/permissions/menus');
  },
  
  // 根据用户ID获取菜单权限列表
  getMenuPermissionsByUserId: (userId) => {
    return http.get(`/api/sys/permissions/menus/user/${userId}`);
  }
};

export default SysPermissionApi; 