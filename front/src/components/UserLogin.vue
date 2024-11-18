<template>
  <div class="login-page">
    <div class="login-card">
      <div class="login-image">
        <img src="https://via.placeholder.com/150" alt="Login Icon" />
      </div>
      <div class="login-form">
        <h2>Member Login</h2>
        <el-form
          :model="form"
          :rules="rules"
          ref="loginForm"
          @submit.prevent="handleLogin"
        >
          <el-form-item prop="username">
            <el-input
              v-model="form.username"
              placeholder="Email"
              :prefix-icon="User"
            ></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              type="password"
              v-model="form.password"
              placeholder="Password"
              :prefix-icon="Lock"
              @keyup.enter="handleLogin"
            ></el-input>
          </el-form-item>
          <div class="login-options">
            <a href="#" class="forget-password">Forgot Username / Password?</a>
          </div>
          <el-button
            type="primary"
            class="login-button"
            @click="handleLogin"
            :loading="isLoading"
          >
            {{ isLoading ? 'Logging In...' : 'LOGIN' }}
          </el-button>
        </el-form>
        <p class="create-account">
          <a href="#">Create your Account →</a>
        </p>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive } from "vue";
import { ElMessage } from "element-plus";
import axios from "axios";
import { useRouter } from "vue-router";
import { User, Lock } from "@element-plus/icons-vue";

export default {
  name: "UserLogin",
  emits: ["login-success"],
  setup(props, { emit }) {
    const loginForm = ref(null);
    const isLoading = ref(false);

    const form = reactive({
      username: "",
      password: "",
    });
    const router = useRouter();

    // 表单验证规则
    const rules = {
      username: [
        { required: true, message: "请输入用户名", trigger: "blur" },
        { type: "email", message: "请输入有效的邮箱地址", trigger: "blur" },
      ],
      password: [
        { required: true, message: "请输入密码", trigger: "blur" },
        { min: 6, message: "密码长度至少6位", trigger: "blur" },
      ],
    };

    const handleLogin = async () => {
      if (!loginForm.value) return;

      try {
        await loginForm.value.validate();
        isLoading.value = true;

        const response = await axios.post("/api/user/login", {
          username: form.username,
          password: form.password,
        });

        const { token, userInfo } = response.data;

        if (response.data.status === "success") {
          emit("login-success", { token, userInfo });
          ElMessage.success("登录成功");
        } else {
          throw new Error(response.data.message || "登录失败");
        }
      } catch (error) {
        ElMessage.error(error.message || "登录失败，请检查用户名和密码");
      } finally {
        isLoading.value = false;
      }
    };

    return {
      loginForm,
      form,
      rules,
      isLoading,
      handleLogin,
    };
  },
};
</script>

<style scoped>
.login-page {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(135deg, 
  #0093E9,   /* 第一个颜色：浅橙色 */
  #0093E9 20%, /* 第一个颜色的结束位置 */
  #80D0C7,   /* 第二个颜色：浅粉色 */
  #80D0C7 40%, /* 第二个颜色的结束位置 */
  #E0FBFC,   /* 第三个颜色：浅橙色 */
  #E0FBFC 60%, /* 第三个颜色的结束位置 */
  #FFBB00,   /* 第四个颜色：浅橙色 */
  #FFBB00 80%, /* 第四个颜色的结束位置 */
  #FF6347    /* 第五个颜色：浅橙色 */
);


  font-family: "Arial", sans-serif;
}

.login-card {
  background: white;
  padding: 40px;
  border-radius: 15px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.2);
  width: 450px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.login-image img {
  width: 100px;
  height: 100px;
  margin-bottom: 20px;
}

.login-form {
  width: 100%;
}

.login-form h2 {
  text-align: center;
  font-size: 22px;
  margin-bottom: 20px;
  color: #333;
}

.el-form-item {
  margin-bottom: 20px;
}

.login-options {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 20px;
}

.forget-password {
  color: #409eff;
  text-decoration: none;
  font-size: 14px;
}

.forget-password:hover {
  color: #66b1ff;
}

.login-button {
  width: 100%;
  font-size: 16px;
  font-weight: bold;
}

.create-account {
  text-align: center;
  margin-top: 15px;
}

.create-account a {
  color: #409eff;
  text-decoration: none;
  font-size: 14px;
}

.create-account a:hover {
  color: #66b1ff;
}
</style>
