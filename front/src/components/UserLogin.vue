<template>
  <div class="login-container">
    <div class="login-box">
      <h2 class="login-title">用户登录</h2>
      <el-form :model="form" ref="loginForm" @submit.prevent="handleLogin">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" prefix-icon="el-icon-user"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input type="password" v-model="form.password" placeholder="请输入密码" prefix-icon="el-icon-lock"></el-input>
        </el-form-item>
        <el-button type="primary" class="login-button" @click="handleLogin">登录</el-button>
      </el-form>
    </div>
  </div>
</template>

<script>
import { mapActions } from 'vuex';

export default {
  data() {
    return {
      form: {
        username: '',
        password: ''
      }
    };
  },
  methods: {
    ...mapActions(['login']),
    async handleLogin() {
      const success = await this.login(this.form);
      if (success) {
        this.$router.push('/');
      } else {
        this.$message.error('登录失败，请检查用户名或密码');
      }
    }
  }
};
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
  text-align: center;
}

.login-title {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 20px;
  color: #333;
}

.el-form-item {
  margin-bottom: 20px;
}

.el-input {
  border-radius: 5px;
  height: 40px;
  padding-left: 10px;
}

.el-button.login-button {
  width: 100%;
  border-radius: 5px;
  height: 40px;
}

.login-button {
  background-color: #4caf50;
  border-color: #4caf50;
  font-size: 16px;
  font-weight: bold;
}

.login-button:hover {
  background-color: #388e3c;
  border-color: #388e3c;
}
</style>
