<!-- UserLogin.vue -->
<template>
  <div class="login-container">
    <div class="login-box">
      <h2 class="login-title">用户登录</h2>
      <el-form 
        :model="form" 
        :rules="rules"
        ref="loginForm" 
        @submit.prevent="handleLogin"
      >
        <el-form-item prop="username">
          <el-input 
            v-model="form.username" 
            placeholder="请输入用户名"
            prefix-icon="User"
          ></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input 
            type="password" 
            v-model="form.password" 
            placeholder="请输入密码"
            prefix-icon="Lock"
            @keyup.enter="handleLogin"
          ></el-input>
        </el-form-item>
        <div class="login-options">
          <el-checkbox v-model="rememberMe">记住我</el-checkbox>
          <a href="#" class="forget-password">忘记密码？</a>
        </div>
        <el-button 
          type="primary" 
          class="login-button" 
          @click="handleLogin"
          :loading="isLoading"
        >
          {{ isLoading ? '登录中...' : '登录' }}
        </el-button>
      </el-form>
    </div>
  </div>
</template>

<script>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'

export default {
  name: 'UserLogin',
  emits: ['login-success'],
  
  setup(props, { emit }) {
    const loginForm = ref(null)
    const isLoading = ref(false)
    const rememberMe = ref(false)
    
    const form = reactive({
      username: '',
      password: ''
    })
    
    // 表单验证规则
    const rules = {
      username: [
        { required: true, message: '请输入用户名', trigger: 'blur' },
        { min: 3, message: '用户名长度不能小于3位', trigger: 'blur' }
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 6, message: '密码长度不能小于6位', trigger: 'blur' }
      ]
    }
    
    const handleLogin = async () => {
      if (!loginForm.value) return
      
      try {
        await loginForm.value.validate()
        isLoading.value = true
        
        // 这里调用你的登录API
        // const response = await login(form)
        // 模拟登录API调用
        await new Promise(resolve => setTimeout(resolve, 1000))
        
        // 模拟登录成功
        const mockUserData = {
          token: 'mock-token-' + Date.now(),
          userInfo: {
            id: 1,
            username: form.username,
            role: 'admin'
          }
        }
        
        if (rememberMe.value) {
          localStorage.setItem('rememberedUser', form.username)
        } else {
          localStorage.removeItem('rememberedUser')
        }
        
        emit('login-success', mockUserData)
        ElMessage.success('登录成功')
        
      } catch (error) {
        console.error('Login failed:', error)
        ElMessage.error('登录失败，请检查用户名和密码')
      } finally {
        isLoading.value = false
      }
    }
    
    // 检查是否有记住的用户名
    const initRememberedUser = () => {
      const rememberedUser = localStorage.getItem('rememberedUser')
      if (rememberedUser) {
        form.username = rememberedUser
        rememberMe.value = true
      }
    }
    
    // 初始化
    initRememberedUser()
    
    return {
      loginForm,
      form,
      rules,
      isLoading,
      rememberMe,
      handleLogin
    }
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(135deg, #81c784, #4caf50);
}

.login-box {
  background: #fff;
  padding: 30px;
  border-radius: 10px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  width: 400px;
}

.login-title {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 30px;
  text-align: center;
  color: #333;
}

.el-form-item {
  margin-bottom: 25px;
}

.login-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.forget-password {
  color: #409EFF;
  text-decoration: none;
  font-size: 14px;
}

.forget-password:hover {
  color: #66b1ff;
}

.login-button {
  width: 100%;
}

@media screen and (max-width: 768px) {
  .login-box {
    width: 90%;
    margin: 0 20px;
  }
}
</style>