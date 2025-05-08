import { http } from '@/utils/request'

export const SysUserApi = {
  // 获取用户列表（分页）
  getUserList: (params) => {
    return http.get('/api/sys/users', params);
  },
  
  // 获取用户详情
  getUserById: (id) => {
    return http.get(`/api/sys/users/${id}`);
  },
  
  // 创建用户
  createUser: (data) => {
    // 确保数据格式正确
    const userData = {
      username: data.username,
      password: data.password,
      realName: data.realName,
      nickname: data.nickname,
      gender: typeof data.gender === 'number' ? data.gender : 0,
      email: data.email,
      phone: data.phone,
      avatar: data.avatar || '',
      deptId: data.deptId ? Number(data.deptId) : null,
      roleIds: data.roleIds && data.roleIds.length > 0 ? data.roleIds.map(id => Number(id)) : [],
      remark: data.remark || ''
    };
    
    return http.post('/api/sys/users', userData);
  },
  
  // 更新用户
  updateUser: (data) => {
    // 确保数据格式正确
    const userData = {
      id: data.id ? Number(data.id) : null,
      realName: data.realName,
      nickname: data.nickname,
      gender: typeof data.gender === 'number' ? data.gender : 0,
      email: data.email,
      phone: data.phone,
      avatar: data.avatar || '',
      deptId: data.deptId ? Number(data.deptId) : null,
      roleIds: data.roleIds && data.roleIds.length > 0 ? data.roleIds.map(id => Number(id)) : [],
      remark: data.remark || ''
    };
    
    return http.put('/api/sys/users', userData);
  },
  
  // 删除用户
  deleteUser: (id) => {
    return http.delete(`/api/sys/users/${id}`);
  },
  
  // 批量删除用户
  batchDeleteUsers: (ids) => {
    return http.delete('/api/sys/users/batch', { data: ids });
  },
  
  // 修改用户状态
  updateUserStatus: (id, status) => {
    return http.put(`/api/sys/users/${id}/status?status=${status}`);
  },
  
  // 重置用户密码
  resetPassword: (data) => {
    return http.put('/api/sys/users/reset-password', data);
  },
  
  // 获取用户权限列表
  getUserPermissions: (id) => {
    return http.get(`/api/sys/users/${id}/permissions`);
  },
  
  // 根据部门ID获取用户列表
  getUsersByDeptId: (deptId) => {
    return http.get(`/api/sys/users/dept/${deptId}`);
  }
};

export default SysUserApi; 