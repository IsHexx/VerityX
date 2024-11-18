<template>
  <div id="app">
    <!-- 使用全局状态动态控制 -->
    <UserLogin v-if="!authState.isAuthenticated" @login-success="handleLoginSuccess"/>
    <home-page v-else @logout="handleLogout"/>
  </div>
</template>

<script>
import { onMounted } from 'vue';
import { useRouter } from 'vue-router';
import UserLogin from '@/components/UserLogin.vue';
import HomePage from './views/HomePage.vue';
import { authState } from '@/auth';

export default {
  name: 'App',
  components: {
    UserLogin,
    HomePage,
  },
  setup() {
    const router = useRouter();

    // 检查登录状态
    const checkAuthStatus = () => {
      const token = localStorage.getItem('token');
      authState.isAuthenticated = !!token;
      console.log('检查登录状态:', authState.isAuthenticated);
    };

    // 处理登录成功
    const handleLoginSuccess = (userData) => {
      // 存储 Token 和用户信息
      localStorage.setItem('token', userData.token);
      if (userData.userInfo) {
        localStorage.setItem('userInfo', JSON.stringify(userData.userInfo));
      }
      authState.isAuthenticated = true;
      router.push('/overview');
    };

    // 处理登出
    const handleLogout = () => {
      localStorage.removeItem('token');
      localStorage.removeItem('userInfo');
      authState.isAuthenticated = false;
      router.push('/login');
    };

    // 组件挂载时检查登录状态
    onMounted(() => {
      checkAuthStatus();
      if (!authState.isAuthenticated && router.currentRoute.value.path !== '/login') {
        router.push('/login');
      }
    });

    return {
      authState,
      handleLoginSuccess,
      handleLogout,
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
