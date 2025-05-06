// utils/request.js
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'
import router from '@/router'

// 创建axios实例
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  timeout: 15000
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    // 可以在这里添加loading
    // store.commit('SET_LOADING', true)
    
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
    
    const res = response.data
    console.log('Response data:', res)
    
    // 支持两种API响应格式：{code:200}和{success:true}
    if (res.code !== undefined && res.code !== 200) {
      // 处理旧格式响应
      ElMessage({
        message: res.message || '请求失败',
        type: 'error',
        duration: 5 * 1000
      })
      
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
          localStorage.removeItem('token')
          router.push('/login')
        })
      }
      return Promise.reject(new Error(res.message || '请求失败'))
    } else if (res.success === false) {
      // 处理新格式响应
      ElMessage({
        message: res.message || '请求失败',
        type: 'error',
        duration: 5 * 1000
      })
      
      return Promise.reject(new Error(res.message || '请求失败'))
    }
    
    return res
  },
  error => {
    // 关闭loading
    // store.commit('SET_LOADING', false)
    
    console.error('Response error:', error)
    const { response } = error
    
    if (response) {
      // 处理http状态码错误
      switch (response.status) {
        case 400:
          ElMessage.error('请求错误')
          break
        case 401:
          ElMessage.error('未授权，请登录')
          localStorage.removeItem('token')
          router.push('/login')
          break
        case 403:
          ElMessage.error('拒绝访问')
          break
        case 404:
          ElMessage.error('请求地址出错')
          break
        case 408:
          ElMessage.error('请求超时')
          break
        case 500:
          ElMessage.error('服务器内部错误')
          break
        case 501:
          ElMessage.error('服务未实现')
          break
        case 502:
          ElMessage.error('网关错误')
          break
        case 503:
          ElMessage.error('服务不可用')
          break
        case 504:
          ElMessage.error('网关超时')
          break
        case 505:
          ElMessage.error('HTTP版本不受支持')
          break
        default:
          ElMessage.error('系统未知错误')
      }
    } else {
      // 处理断网的情况
      if (!window.navigator.onLine) {
        ElMessage.error('网络连接失败，请检查网络')
      } else {
        ElMessage.error('系统未知错误')
      }
    }
    return Promise.reject(error)
  }
)

// 封装常用的请求方法
const http = {
  get(url, params, config = {}) {
    // console.log('get', url,)
    // console.log('params', params)
    // console.log('config', config)
    return service.get(url, { params, ...config })
  },
  
  post(url, data, config = {}) {
    return service.post(url, data, config)
  },
  
  put(url, data, config = {}) {
    return service.put(url, data, config)
  },
  
  delete(url, params, config = {}) {
    return service.delete(url, { params, ...config })
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