// utils/request.js
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'
import router from '@/router'
import { useProjectStore } from '@/store/projectStore'

// 创建axios实例
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  timeout: 15000
})

// 从localStorage获取当前项目ID
const getCurrentProjectId = () => {
  try {
    const storedProject = localStorage.getItem('currentProject');
    if (storedProject) {
      const projectData = JSON.parse(storedProject);
      return projectData.id || projectData.projectId || null;
    }
  } catch (error) {
    console.error('获取项目ID失败:', error);
  }
  return null;
};

// 请求拦截器
service.interceptors.request.use(
  config => {
    // 可以在这里添加loading
    // store.commit('SET_LOADING', true)

    // 获取当前项目ID
    // 注意：不能在拦截器中直接使用组合式API(如useProjectStore)，因为它们需要在Vue组件上下文中使用
    // 改为直接从localStorage中获取
    const currentProjectId = getCurrentProjectId();

    // 添加调试信息
    console.log(`请求URL: ${config.url}, 当前项目ID: ${currentProjectId}`, config);

    // 仅为GET请求添加项目ID参数
    if (config.method === 'get') {
      config.params = config.params || {};
      // 如果没有明确设置projectId且当前有选中项目，则添加项目ID
      if ((!config.params.projectId || config.params.projectId === null) && currentProjectId) {
        config.params.projectId = currentProjectId;
        console.log(`自动添加项目ID: ${currentProjectId} 到GET请求参数`);
      }
    }
    // 为POST、PUT请求的请求体添加项目ID
    else if (['post', 'put'].includes(config.method) && config.data && typeof config.data === 'object') {
      // 如果请求数据是对象且没有明确设置projectId且当前有选中项目，则添加项目ID
      if ((!config.data.projectId || config.data.projectId === null) && currentProjectId) {
        config.data.projectId = currentProjectId;
        console.log(`自动添加项目ID: ${currentProjectId} 到请求体`);
      }
    }

    // 从localStorage获取token
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  error => {
    // 关闭loading
    // store.commit('SET_LOADING', false)
    console.error('Request error:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    // 关闭loading
    // store.commit('SET_LOADING', false)

    const res = response.data;
    console.log('Response data:', res);

    // 检查响应状态 - 支持两种格式：code=200 或 success=true
    if (res.code === 200 || res.success === true) {
      // 请求成功，直接返回数据
      return res;
    }

    // 请求失败，显示错误信息
    ElMessage({
      message: res.message || '请求失败',
      type: 'error',
      duration: 5 * 1000
    });

    // 处理token过期
    if (res.code === 401) {
      ElMessageBox.confirm(
        '登录状态已过期，请重新登录',
        '系统提示',
        {
          confirmButtonText: '重新登录',
          cancelButtonText: '取消',
          type: 'warning'
        }
      ).then(() => {
        localStorage.removeItem('token');
        router.push('/login');
      });
    }

    return Promise.reject(new Error(res.message || '请求失败'));
  },
  error => {
    // 关闭loading
    // store.commit('SET_LOADING', false)

    console.error('Response error:', error);
    const { response } = error;

    if (response) {
      // 处理http状态码错误
      switch (response.status) {
        case 400:
          ElMessage.error('请求错误');
          break;
        case 401:
          ElMessage.error('未授权，请登录');
          localStorage.removeItem('token');
          router.push('/login');
          break;
        case 403:
          ElMessage.error('拒绝访问');
          break;
        case 404:
          ElMessage.error('请求地址出错');
          break;
        case 408:
          ElMessage.error('请求超时');
          break;
        case 500:
          ElMessage.error('服务器内部错误');
          break;
        case 501:
          ElMessage.error('服务未实现');
          break;
        case 502:
          ElMessage.error('网关错误');
          break;
        case 503:
          ElMessage.error('服务不可用');
          break;
        case 504:
          ElMessage.error('网关超时');
          break;
        case 505:
          ElMessage.error('HTTP版本不受支持');
          break;
        default:
          ElMessage.error('系统未知错误');
      }
    } else {
      // 处理断网的情况
      if (!window.navigator.onLine) {
        ElMessage.error('网络连接失败，请检查网络');
      } else {
        ElMessage.error('系统未知错误');
      }
    }
    return Promise.reject(error);
  }
);

// 封装常用的请求方法
const http = {
  get(url, config = {}) {
    return service.get(url, config);
  },

  post(url, data, config = {}) {
    return service.post(url, data, config)
  },

  put(url, data, config = {}) {
    return service.put(url, data, config)
  },

  delete(url, config = {}) {
    return service.delete(url, config)
  },

  // 上传文件方法
  upload(url, file, onUploadProgress = null) {
    const formData = new FormData()
    formData.append('file', file)

    return service.post(url, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      },
      onUploadProgress: onUploadProgress ? (progressEvent) => {
        const progress = (progressEvent.loaded / progressEvent.total * 100) | 0
        onUploadProgress(progress)
      } : null
    })
  },

  // 下载文件方法
  download(url, params, fileName) {
    return service.get(url, {
      params,
      responseType: 'blob'
    }).then(response => {
      // 创建blob链接
      const blob = new Blob([response.data])
      const link = document.createElement('a')
      link.href = window.URL.createObjectURL(blob)
      link.download = fileName
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      window.URL.revokeObjectURL(link.href)
    })
  }
}

export { service, http }