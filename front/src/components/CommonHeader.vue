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
    <el-menu 
      mode="horizontal" 
      :ellipsis="false"  
      class="horizontal-menu"
      :default-active="activeSubMenu"
    >
      <el-menu-item 
        v-for="item in currentSubMenus" 
        :key="item.index" 
        :index="item.index"
        @click="setActiveMenu(activeMainMenu, item.index)"
      >
        {{ item.title }}
      </el-menu-item>
    </el-menu>

    <!-- 导航栏右侧内容 -->
    <div class="header-right">
      <!-- 用户信息部分：头像 + 用户名 + 下拉菜单 -->
      <el-dropdown trigger="click">
        <span class="el-dropdown-link">
          <el-avatar :src="image"/>
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
import { ref, onMounted , watch} from 'vue';
import { useMenuStore } from '@/store/menuStore';
import image from '@/assets/image.png';  
const { activeMainMenu, activeSubMenu, currentSubMenus, setActiveMenu, initializeActiveMenu } = useMenuStore();

// 模拟用户名
const username = ref('John Doe');

// 模拟退出登录方法
const logout = () => {
  console.log('Logout clicked');
  // 在此处处理退出登录的逻辑
};

const data = () => {
  return {
    image: 'image'
  };
};
onMounted(() => {
  initializeActiveMenu();
});
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


.el-dropdown-link {
  display: flex;    /* 设置为 Flex 容器 */
  align-items: center; /* 子元素垂直居中 */
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