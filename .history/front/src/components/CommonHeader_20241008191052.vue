<template>
  <div class="header-container">
    <!-- 左侧 Logo 区域 -->
    <div class="header-logo">MyApp</div>

    <!-- 中间的水平菜单 -->
    <el-menu mode="horizontal" default-active="1" class="horizontal-menu">
      <el-menu-item index="1">Home</el-menu-item>
      <el-menu-item index="2">About</el-menu-item>
      <el-menu-item index="3">Services</el-menu-item>
      <el-menu-item index="4">Contact</el-menu-item>
    </el-menu>

    <!-- 右侧的用户信息部分 -->
    <div class="user-info">
      <el-avatar
        src="https://avatars.githubusercontent.com/u/1?v=4"
        size="medium"
      ></el-avatar>
      <span class="username">John Doe</span>
    </div>
  </div>
</template>

<script>
export default {
  name: "CommonHeader",
};
</script>

<style scoped>
.header-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
  padding: 0 20px;
  background-color: #f5f5f5;
}

.header-logo {
  font-size: 24px;
  font-weight: bold;
}

.horizontal-menu {
  flex-grow: 1;
  margin-left: 40px;
}

.user-info {
  display: flex;
  align-items: center;
}

.username {
  margin-left: 10px;
}
</style>
