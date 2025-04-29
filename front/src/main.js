import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import Echarts from 'vue-echarts';
import * as echarts from 'echarts';
import axios from 'axios';
import { authState } from '@/auth';
import * as ElIconModules from '@element-plus/icons-vue';
import { createPinia } from 'pinia'

// 性能优化：提前检查登录状态
authState.isAuthenticated = !!localStorage.getItem('token');

// 创建应用实例
const app = createApp(App);

// 注册图标组件
for (let iconName in ElIconModules){
    app.component(iconName, ElIconModules[iconName])
}

// 设置 axios 的全局默认配置
axios.defaults.baseURL = '/api';
axios.defaults.timeout = 10000;

// 注册ECharts组件
app.component("v-chart", Echarts)
app.config.globalProperties.$echarts = echarts;

// 注册插件
app.use(router);
app.use(ElementPlus);
app.use(store);
app.use(createPinia());

// 移除预加载器的函数
const removePreloader = () => {
  if (window.preloaderHandled) return;
  
  window.appMounted = true;
  
  // 通知预加载器已挂载
  if (typeof window.handlePreloader === 'function') {
    window.handlePreloader();
  } else {
    const preloader = document.getElementById('preloader');
    if (preloader) {
      preloader.style.opacity = '0';
      preloader.style.visibility = 'hidden';
      
      setTimeout(() => {
        if (preloader.parentNode) {
          preloader.parentNode.removeChild(preloader);
        }
      }, 100);
    }
  }
};

// 挂载应用
app.mount('#app');

// 立即尝试移除预加载器
removePreloader();
