<template>
  <el-header class="header">
    <!-- 导航栏左侧内容 -->
    <div class="header-left">
      <!-- 项目路径信息 -->
      <div class="project-header">
        <span class="project-name">项目</span>
        <span class="project-path-separator">＞</span>
      </div>
      
      <!-- 项目选择器 -->
      <ProjectSelector />
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
import { ref, onMounted, watch, nextTick, computed, onBeforeMount } from 'vue';
import { useMenuStore } from '@/store/menuStore';
import { useProjectStore } from '@/store/projectStore'; // 导入项目存储
import ProjectSelector from '@/components/ProjectSelector.vue'; // 导入项目选择器
import image from '@/assets/image.png';  
import { useRouter, useRoute } from 'vue-router'
import { authState } from '@/auth';

const route = useRoute();
const { activeMainMenu, activeSubMenu, currentSubMenus, setActiveMenu, initializeActiveMenu, shouldUpdateMenu } = useMenuStore();
const projectStore = useProjectStore(); // 获取项目Store
const { currentProject } = projectStore; // 获取当前项目

// 计算当前项目名称，如果没有选择项目则显示"项目"
const currentProjectName = computed(() => {
  console.log("当前项目信息:", currentProject.value);
  // 尝试不同可能的属性名
  if (currentProject.value) {
    // 优先使用projectName，如果不存在则尝试name
    return currentProject.value.projectName || 
           currentProject.value.name || 
           currentProject.value.id && `项目 ${currentProject.value.id}` || 
           '项目';
  }
  return '项目';
});

// 计算当前页面名称，从路由获取
const currentPageName = computed(() => {
  // 找到与当前路由匹配的菜单
  for (const mainMenu of currentSubMenus.value) {
    if (mainMenu.route === route.path) {
      return mainMenu.title;
    }
  }
  // 如果没有匹配，默认显示Dashboard
  return 'Dashboard';
});

// 监听项目变更事件，刷新页面内容
const handleProjectChanged = (event) => {
  console.log('项目已变更:', event.detail);
  // 刷新当前页面数据
  window.location.reload();
};

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

onBeforeMount(() => {
  // 确保项目状态初始化
  projectStore.initProjectState();
  
  // 添加项目变更事件监听
  window.addEventListener('project-changed', handleProjectChanged);
});

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
  min-width: 200px;
}

.project-header {
  display: flex;
  align-items: center;
  height: 100%;
  margin-right: 8px; /* 增加右边距，与项目选择器保持距离 */
}

.project-name {
  font-weight: 600;
  font-size: 14px;
  color: #333;
}

.project-path-separator {
  margin: 0 8px;
  color: #909399;
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

:deep(.el-breadcrumb__item) {
  color: #333 !important;
}

:deep(.el-breadcrumb__inner) {
  color: inherit !important;
}
</style>