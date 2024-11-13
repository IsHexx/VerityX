<template>
  <div id="app">
    <UserLogin v-if="!isAuthenticated" @login-success="handleLoginSuccess"/>
    <home-page v-else/>
  </div>
  
</template>
   
  <script>


import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import UserLogin from '@/components/UserLogin.vue'
import HomePage from './views/HomePage.vue'

export default {
  name: 'App',
  components: {
    UserLogin,
    HomePage
  },
  
  setup() {
    const router = useRouter()
    const isAuthenticated = ref(false)
    
    // 检查登录状态
    const checkAuthStatus = () => {
      console.log("检查登录状态")
      const token = localStorage.getItem('token')
      isAuthenticated.value = !!token
      console.log("isAuthenticated:", isAuthenticated.value)
      console.log("token:", token)
    }
    
    // 处理登录成功
    const handleLoginSuccess = (userData) => {
      // 存储token和必要的用户信息
      localStorage.setItem('token', userData.token)
      if(userData.userInfo) {
        localStorage.setItem('userInfo', JSON.stringify(userData.userInfo))
      }
      
      isAuthenticated.value = true
      // 登录成功后跳转到首页
      router.push('/overview')
    }
    
    // 处理登出
    const handleLogout = () => {
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      isAuthenticated.value = false
      router.push('/login')
    }
    
    // 组件挂载时检查登录状态
    onMounted(() => {
      checkAuthStatus()
      // 如果没有登录且当前不在登录页，重定向到登录页
      console.log("isAuthenticated:", isAuthenticated.value)
      if (!isAuthenticated.value && router.currentRoute.value.path !== '/login') {
        router.push('/login')
      }
    })
    
    return {
      isAuthenticated,
      handleLoginSuccess,
      handleLogout
    }
  }
}
  </script>
   
  <style>
  *{
   margin:0;
   padding:0
  }

  #app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  height: 100vh;
  width: 100vw;
  overflow: hidden;
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

/* 添加一些必要的全局样式 */
html, body {
  height: 100%;
  width: 100%;
  overflow: hidden;
}

/* 响应式布局支持 */
@media screen and (max-width: 768px) {
  #app {
    overflow-y: auto;
  }
}
  </style>
   