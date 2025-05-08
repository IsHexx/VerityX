import { http } from '@/utils/request'

export const SysDeptApi = {
  // 获取所有部门列表
  getAllDepts: (params) => {
    return http.get('/api/sys/depts', params);
  },
  
  // 获取部门详情
  getDeptById: (id) => {
    return http.get(`/api/sys/depts/${id}`);
  },
  
  // 创建部门
  createDept: (data) => {
    // 确保数据格式正确
    const deptData = {
      deptName: data.deptName,
      deptCode: data.deptCode,
      parentId: data.parentId ? Number(data.parentId) : null,
      leader: data.leader || '',
      phone: data.phone || '',
      email: data.email || '',
      sort: data.sort ? Number(data.sort) : 0,
      isActive: data.isActive !== false,
      remark: data.remark || ''
    };
    
    return http.post('/api/sys/depts', deptData);
  },
  
  // 更新部门
  updateDept: (data) => {
    // 确保数据格式正确
    const deptData = {
      id: data.id ? Number(data.id) : null,
      deptName: data.deptName,
      deptCode: data.deptCode,
      parentId: data.parentId ? Number(data.parentId) : null,
      leader: data.leader || '',
      phone: data.phone || '',
      email: data.email || '',
      sort: data.sort ? Number(data.sort) : 0,
      isActive: data.isActive !== false,
      remark: data.remark || ''
    };
    
    return http.put('/api/sys/depts', deptData);
  },
  
  // 删除部门
  deleteDept: (id) => {
    return http.delete(`/api/sys/depts/${id}`);
  },
  
  // 修改部门状态
  updateDeptStatus: (id, status) => {
    return http.put(`/api/sys/depts/${id}/status?status=${status}`);
  },
  
  // 根据父ID获取部门列表
  getDeptsByParentId: (parentId) => {
    return http.get(`/api/sys/depts/parent/${parentId}`);
  },
  
  // 根据用户ID获取部门列表
  getDeptsByUserId: (userId) => {
    return http.get(`/api/sys/depts/user/${userId}`);
  },
  
  // 查询部门及其下级部门ID列表
  getChildDeptIds: (deptId) => {
    return http.get(`/api/sys/depts/${deptId}/children/ids`);
  },
  
  // 查询用户的主部门
  getPrimaryDeptByUserId: (userId) => {
    return http.get(`/api/sys/depts/user/${userId}/primary`);
  }
};

export default SysDeptApi; 