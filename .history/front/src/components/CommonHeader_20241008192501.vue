<template>
  <el-header class="header">
    <!-- 导航栏左侧内容 -->
    <div class="header-left">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>Home</el-breadcrumb-item>
        <el-breadcrumb-item>Dashboard</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <!-- 中间的水平菜单 -->
    <el-menu mode="horizontal" :ellipsis="false"  class="horizontal-menu">
      <el-menu-item index="1">Home</el-menu-item>
      <el-menu-item index="2">About</el-menu-item>
      <el-menu-item index="3">Services</el-menu-item>
      <el-menu-item index="4">Contact</el-menu-item>
    </el-menu>

    <!-- 导航栏右侧内容 -->
    <div class="header-right">
      <!-- 用户信息部分：头像 + 用户名 + 下拉菜单 -->
      <el-dropdown trigger="click">
        <span class="el-dropdown-link">
          <el-avatar src="https://www.example.com/user-avatar.jpg" size="40"></el-avatar>
          <span class="username">{{ username }}</span>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item>Profile</el-dropdown-item>
            <el-dropdown-item>Settings</el-dropdown-item>
            <el-dropdown-item divided @click="logout">Logout</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </el-header>
</template>

<script setup>
import { ref } from 'vue';

// 模拟用户名
const username = ref('John Doe');

// 模拟退出登录方法
const logout = () => {
  console.log('Logout clicked');
  // 在此处处理退出登录的逻辑
};
</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  padding: 0 20px;
  background-color: #ffffff;
  border-bottom: #f5f5f5 1px solid;
  align-items: center;
}

.header-left {
  display: flex;
  align-items: center;
}

.header-right {
  display: flex;
  align-items: center;
}

.username {
  margin-left: 10px;
  font-size: 16px;
  color: #333;
}

.el-avatar {
  cursor: pointer;
}
</style>
