<template>
  <el-header class="header">
    <!-- 导航栏左侧内容 -->
    <div class="header-left">
      <el-breadcrumb separator="＞">
        <el-breadcrumb-item style="font-weight: 800;">项目</el-breadcrumb-item>
        <el-breadcrumb-item>Dashboard</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <!-- 中间的水平菜单 -->
     <el-menu
      mode="horizontal"
      :ellipsis="false"
      class="horizontal-menu"
      :default-active="activeSubMenu"
      style="height: 100%;"
    >
      <el-menu-item
        v-for="item in currentSubMenus"
        :key="item.index"
        :index="item.index"
        @click="handleMenuClick(item)"
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
import { ref, onMounted, watch, nextTick } from 'vue';
import { useMenuStore } from '@/store/menuStore';
import image from '@/assets/image.png';  
import { useRouter, useRoute } from 'vue-router'
import { authState } from '@/auth';

const route = useRoute();
const { activeMainMenu, activeSubMenu, currentSubMenus, setActiveMenu, initializeActiveMenu, shouldUpdateMenu } = useMenuStore();

// 模拟用户名
const username = ref('John Doe');
const isAuthenticated = ref(false)
const router = useRouter();

// 退出登录方法
const logout = () => {
  // 清理本地存储
  localStorage.removeItem('token');
  localStorage.removeItem('userInfo');

  // 更新全局登录状态
  authState.isAuthenticated = false;

  // 跳转到登录页面
  router.push('/login');
};

// 修改菜单点击处理函数
const handleMenuClick = (item) => {
  // 设置为true，允许菜单点击跳转
  shouldUpdateMenu.value = true;
  // 直接调用 setActiveMenu 并传入当前激活的主菜单和被点击的子菜单的 index
  setActiveMenu(activeMainMenu.value, item.index);
}

// 首次加载或刷新页面时，禁用自动跳转
if (history.state && history.state.state && history.state.state.position === 0) {
  // 这表示是刷新页面，不是导航
  shouldUpdateMenu.value = false;
  
  // 延迟一点再启用，以防止初始化时的跳转
  setTimeout(() => {
    shouldUpdateMenu.value = true;
  }, 500);
}

// 监听路由变化，确保菜单状态同步更新
// 但不自动跳转，避免刷新时干扰
watch(() => route.path, () => {
  nextTick(() => {
    initializeActiveMenu();
  });
}, { immediate: true });

onMounted(() => {
  // 确保组件挂载时菜单状态正确
  initializeActiveMenu();
  
  // 只在非刷新的普通导航中处理菜单点击
  if (shouldUpdateMenu.value) {
    const currentItem = currentSubMenus.value.find(item => item.index === activeSubMenu.value);
    if (currentItem) {
      // 不触发路由跳转，只更新激活状态
      activeSubMenu.value = currentItem.index;
    }
  }
});

const data = () => {
  return {
    image: 'image'
  };
};
</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  padding: 0 0px;
  background-color: #ffffff;
  border-bottom: #dcdfe6 1px solid;
  align-items: center;
}

/* 确保菜单项激活状态的样式正确显示 */
/* :deep(.el-menu-item.is-active) {
  color: var(--el-menu-active-color) !important;
  border-bottom: 2px solid var(--el-menu-active-color) !important;
} */

.header-left {
  display: flex;
  align-items: center;
  padding-left: 20px;
}

.header-right{
  padding-right: 10px;
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