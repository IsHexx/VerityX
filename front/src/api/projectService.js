// api/projectService.js
import { http } from '@/utils/request'

export const projectApi = {
  // 获取所有项目列表
  getProjects() {
    console.log('getProjects')
    return http.get('/api/projects')
  },

  // 获取单个项目详情
  getProject(id) {
    return http.get(`/api/projects/${id}`)
  },

  // 创建新项目
  createProject(data) {
    return http.post('/api/projects', data)
  },

  // 更新项目信息
  updateProject(id, data) {
    return http.put(`/api/projects/${id}`, data)
  },

  // 删除项目
  deleteProject(id) {
    return http.delete(`/api/projects/${id}`)
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

export default projectApi