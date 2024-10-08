<template>
  <div class="homeHeader-menus-bg" style="margin-top:0px; height: auto;">
  <el-header class="header">
    <!-- 面包屑导航 -->
    <el-breadcrumb separator="/">
      <el-breadcrumb-item to="/">Home</el-breadcrumb-item>
      <el-breadcrumb-item>Dashboard</el-breadcrumb-item>
      <el-breadcrumb-item>Settings</el-breadcrumb-item>
    </el-breadcrumb>
    <el-menu class="el-menu-demo">
      <el-menu-item index="1">Processing Center</el-menu-item>
    </el-menu>
    <!-- 用户名 + 下拉菜单 -->
    <div class="user-menu">
      <el-dropdown>
        <span class="el-dropdown-link">
          <el-avatar size="small" :src="userAvatar" class="avatar"></el-avatar>
          <span>{{ username }}</span>
          <i class="el-icon-arrow-down el-icon--right"></i>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="handleProfile">Profile</el-dropdown-item>
            <el-dropdown-item @click="handleLogout">Logout</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </el-header>
</div>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();

// 假设从 Vuex 或其他状态管理中获取用户名和头像
const username = ref("John Doe");
const userAvatar = ref("https://example.com/avatar.png");

const handleProfile = () => {
  // 跳转到用户资料页面
  router.push("/profile");
};

const handleLogout = () => {
  // 处理登出逻辑，比如清除用户状态并跳转到登录页面
  console.log("User logged out");
  router.push("/login");
};
</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 93%;
  background-color: #f5f5f5;
  border-bottom: 1px solid #eaeaea;
}

.el-menu-demo{
  height: 90%;
}
.user-menu {
  display: flex;
  align-items: center;
}

.avatar {
  margin-right: 0px;
}
</style>
