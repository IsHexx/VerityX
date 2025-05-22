// api/BugService.js
import { http } from '@/utils/request'
import { useProjectStore } from '@/store/projectStore'

export const BugApi = {
  // 获取所有缺陷列表
  getBugs: (params) => {
    // 确保params是对象
    params = params || {};
    
    // 获取当前项目ID
    const { getCurrentProjectId } = useProjectStore();
    const projectId = getCurrentProjectId();
    
    // 构造干净的查询参数对象
    const cleanParams = {
      page: params.page || 1, // 默认值为 1
      pageSize: params.pageSize || 10, // 默认值为 10
      projectId: projectId // 添加项目ID
    };
  
    // 仅在 status 有值时添加
    if (params.status) {
      cleanParams.status = params.status;
    }
  
    return http.get('/api/bugs/list', {
      params: {
        page: cleanParams.page,
        pageSize: cleanParams.pageSize,
        status: cleanParams.status,
        projectId: cleanParams.projectId // 传递项目ID
      }
    });
  },

  // 获取单个缺陷详情
  getBug(id) {
    return http.get(`/api/bugs/${id}`)
  },

  // 创建新缺陷
  createBug(data) {
    // 添加项目ID
    const { getCurrentProjectId } = useProjectStore();
    const projectId = getCurrentProjectId();
    data.projectId = projectId;
    
    return http.post('/api/bugs', data)
  },

  // 更新缺陷信息
  updateBug(id, data) {
    return http.put(`/api/bugs/${id}`, data)
  },

  // 删除缺陷
  deleteBug(id) {
    return http.delete(`/api/bugs/${id}`)
  }
}

export default BugApi