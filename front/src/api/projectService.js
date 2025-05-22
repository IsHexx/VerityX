// api/projectService.js
import { http } from '@/utils/request'

/**
 * 项目管理API服务
 */
export const ProjectApi = {
  /**
   * 获取项目列表
   * @param {Object} params 查询参数
   * @returns {Promise} 返回Promise对象
   */
  getProjectList: (params = {}) => {
    const cleanParams = {
      page: params.page || 1,
      pageSize: params.pageSize || 50,
    };
    
    if (params.keyword) {
      cleanParams.keyword = params.keyword;
    }
    
    if (params.status) {
      cleanParams.status = params.status;
    }
    
    return http.get('/api/projects', { params: cleanParams });
  },
  
  /**
   * 获取项目详情
   * @param {Number} id 项目ID
   * @returns {Promise} 返回Promise对象
   */
  getProjectDetail: (id) => {
    return http.get(`/api/projects/${id}`);
  },
  
  /**
   * 创建项目
   * @param {Object} data 项目数据
   * @returns {Promise} 返回Promise对象
   */
  createProject: (data) => {
    return http.post('/api/projects', data);
  },
  
  /**
   * 更新项目
   * @param {Number} id 项目ID
   * @param {Object} data 项目数据
   * @returns {Promise} 返回Promise对象
   */
  updateProject: (id, data) => {
    return http.put(`/api/projects/${id}`, data);
  },
  
  /**
   * 删除项目
   * @param {Number} id 项目ID
   * @returns {Promise} 返回Promise对象
   */
  deleteProject: (id) => {
    return http.delete(`/api/projects/${id}`);
  },

  // 上传项目图标（包含上传进度）
  uploadProjectIcon(file, onProgress) {
    return http.upload('/upload', file, onProgress)
  },

  // 项目成员管理相关接口
  getProjectMembers(projectId) {
    return http.get(`/projects/${projectId}/members`)
  },

  addProjectMember(projectId, data) {
    return http.post(`/projects/${projectId}/members`, data)
  },

  removeProjectMember(projectId, memberId) {
    return http.delete(`/projects/${projectId}/members/${memberId}`)
  }
}

export default ProjectApi