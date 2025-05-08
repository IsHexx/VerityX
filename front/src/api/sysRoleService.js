import { http } from '@/utils/request'

export const SysRoleApi = {
  // 获取角色列表（分页）
  getRoleList: (params) => {
    return http.get('/api/sys/roles', params );
  },
  
  // 获取所有角色列表
  getAllRoles: () => {
    return http.get('/api/sys/roles/all');
  },
  
  // 获取角色详情
  getRoleById: (id) => {
    return http.get(`/api/sys/roles/${id}`);
  },
  
  // 创建角色
  createRole: (data) => {
    return http.post('/api/sys/roles', data);
  },
  
  // 更新角色
  updateRole: (data) => {
    return http.put('/api/sys/roles', data);
  },
  
  // 删除角色
  deleteRole: (id) => {
    return http.delete(`/api/sys/roles/${id}`);
  },
  
  // 批量删除角色
  batchDeleteRoles: (ids) => {
    return http.delete('/api/sys/roles/batch', { data: ids });
  },
  
  // 修改角色状态
  updateRoleStatus: (id, status) => {
    return http.put(`/api/sys/roles/${id}/status?status=${status}`);
  },
  
  // 分配角色权限
  assignRolePermissions: (roleId, permissionIds) => {
    return http.post(`/api/sys/roles/${roleId}/permissions`, permissionIds);
  },
  
  // 获取角色权限ID列表
  getRolePermissionIds: (roleId) => {
    return http.get(`/api/sys/roles/${roleId}/permissions`);
  },
  
  // 根据用户ID获取角色列表
  getRolesByUserId: (userId) => {
    return http.get(`/api/sys/roles/user/${userId}`);
  }
};

export default SysRoleApi; 