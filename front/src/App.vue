<template>
  <div id="app">
    <!-- 加载遮罩 - 只在路由变化时显示，而不是刷新时 -->
    <loading-mask :visible="isLoading" text="页面加载中..."/>
    
    <!-- 使用全局状态动态控制 -->
    <UserLogin v-if="!authState.isAuthenticated" @login-success="handleLoginSuccess"/>
    <home-page v-else @logout="handleLogout"/>
  </div>
</template>

<script>
import { onMounted, ref, nextTick, onBeforeMount } from 'vue';
import { useRouter } from 'vue-router';
import UserLogin from '@/components/UserLogin.vue';
import HomePage from './views/HomePage.vue';
import LoadingMask from '@/components/LoadingMask.vue';
import { authState } from '@/auth';

export default {
  name: 'App',
  components: {
    UserLogin,
    HomePage,
    LoadingMask
  },
  setup() {
    const router = useRouter();
    // 初始状态为false，避免刷新时再次加载遮罩
    const isLoading = ref(false);
    // 标记是否是首次加载
    const isFirstLoad = ref(true);

    // 检查登录状态 - 优化为同步操作
    const checkAuthStatus = () => {
      const token = localStorage.getItem('token');
      authState.isAuthenticated = !!token;
    };

    // 处理登录成功
    const handleLoginSuccess = (userData) => {
      // 存储 Token 和用户信息
      localStorage.setItem('token', userData.token);
      if (userData.userInfo) {
        localStorage.setItem('userInfo', JSON.stringify(userData.userInfo));
      }
      authState.isAuthenticated = true;
      
      // 显示加载遮罩
      isLoading.value = true;
      
      // 登录后跳转
      router.push('/');
      
      // 减少延迟时间
      setTimeout(() => {
        isLoading.value = false;
      }, 200);
    };

    // 处理登出
    const handleLogout = () => {
      localStorage.removeItem('token');
      localStorage.removeItem('userInfo');
      authState.isAuthenticated = false;
      router.push('/login');
    };

    // 在组件挂载前检查登录状态，避免延迟
    onBeforeMount(() => {
      checkAuthStatus();
      if (!authState.isAuthenticated && router.currentRoute.value.path !== '/login') {
        router.push('/login');
      }
    });

    // 监听路由变化，保存最后访问的路径
    router.beforeEach((to, from, next) => {
      // 只有非首次加载且有真实路由变化时才显示加载遮罩
      if (!isFirstLoad.value && from.name) {
        isLoading.value = true;
      }
      
      // 第一次加载完成后标记
      isFirstLoad.value = false;
      
      // 只在非登录页和非空路径保存路径
      if (to.path !== '/login' && to.path !== '/') {
        localStorage.setItem('lastPath', to.path);
      }
      next();
    });
    
    // 路由变化后处理
    router.afterEach(() => {
      // 减少延迟时间
      setTimeout(() => {
        isLoading.value = false;
      }, 100);
    });

    return {
      authState,
      handleLoginSuccess,
      handleLogout,
      isLoading
    };
  },
};
</script>

<style>
* {
  margin: 0;
  padding: 0;
}

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  height: 100vh;
  width: 100vw;
  overflow: hidden;
}

html, body {
  height: 100%;
  width: 100%;
  overflow: hidden;
}
</style>
