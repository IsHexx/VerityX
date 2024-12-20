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

authState.isAuthenticated = !!localStorage.getItem('token'); // 初始化登录状态
const app = createApp(App);
for (let iconName in ElIconModules){
    app.component(iconName, ElIconModules[iconName])
}
// 设置 axios 的全局默认配置
axios.defaults.baseURL = '/api';
axios.defaults.timeout = 10000;

app.component("v-chart", Echarts)

app.config.globalProperties.$echarts = echarts;
app.use(router);
app.use(store);
app.use(ElementPlus);
app.use(createPinia())


app.mount('#app');
