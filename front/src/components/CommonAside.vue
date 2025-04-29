<template>
  <div class="aside">
    <el-menu
      :default-active="activeMainMenu"
      class="el-menu-vertical-demo"
      :collapse="isCollapse"
      @open="handleOpen"
      @close="handleClose"
    >
    <div style="width: 100%; text-align: left;">
        <!-- 添加click事件处理器 -->
        <el-avatar 
          shape="square" 
          :src="logo"
          style="margin-left: 15px; cursor: pointer;" 
          @click="handleAvatarClick"
        />
      </div>
      
      <el-menu-item
        v-for="item in filteredMenuItems"
        :key="item.index"
        :index="item.index"
        @click="handleMenuClick(item.index)"
      >
        <el-icon>
          <component :is="getIconComponent(item.icon)" />
        </el-icon>
        <template  #title>{{ item.title }}</template>
      </el-menu-item>
      <el-button @click="toggleCollapse" style="width: 55px; border: 0px">
        <el-icon>
          <component :is="isCollapse ? Expand : Fold" />
        </el-icon>
        {{ isCollapse ? "" : "" }}
      </el-button>
    </el-menu>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch, nextTick } from "vue";
import logo from "@/assets/logo.png";
import { useRouter, useRoute } from 'vue-router';

import {
  Menu as House,
  Edit,
  Notification,
  Setting,
  Expand,
  Fold,
} from "@element-plus/icons-vue";
import { useMenuStore } from "../store/menuStore";

const data = () => {
  return {
    logo: "logo",
  };
};

// 使用计算属性过滤出 title 有值的项
const filteredMenuItems = computed(() => {
  return menuItems.filter(item => item.title); // 只保留有 title 的项
});

const router = useRouter();
const route = useRoute();
const { menuItems, activeMainMenu, setActiveMenu, initializeActiveMenu, shouldUpdateMenu } = useMenuStore();

const isCollapse = ref(true);

// 检测是否是页面刷新，如果是，不进行自动菜单跳转
const isPageRefresh = history.state && history.state.state && history.state.state.position === 0;
if (isPageRefresh) {
  shouldUpdateMenu.value = false;
  
  // 延迟一点再启用，以防止初始化时的跳转
  setTimeout(() => {
    shouldUpdateMenu.value = true;
  }, 500);
}

// 监听路由变化，更新激活菜单
watch(() => route.path, () => {
  // 使用 nextTick 确保路由完全更新后再处理菜单
  nextTick(() => {
    if (shouldUpdateMenu.value) {
      initializeActiveMenu();
    }
  });
}, { immediate: true });

// 侧边菜单点击处理
const handleMenuClick = (index) => {
  // 显式启用菜单更新
  shouldUpdateMenu.value = true;
  setActiveMenu(index);
};

// 添加头像点击处理函数
const handleAvatarClick = () => {
  // 跳转到项目管理页面并更新菜单状态
  shouldUpdateMenu.value = true;
  setActiveMenu('5'); // '5'是项目管理菜单的index
  router.push('/projectmanage');
};

const toggleCollapse = () => {
  isCollapse.value = !isCollapse.value;
};

// 返回图标组件
const getIconComponent = (iconName) => {
  switch (iconName) {
    case "House":
      return House;
    case "Edit":
      return Edit;
    case "Notification":
      return Notification;
    case "Setting":
      return Setting;
    default:
      return null;
  }
};

const handleOpen = (key) => {
  console.log(key);
};
const handleClose = (key) => {
  console.log(key);
};

onMounted(() => {
  // 初始化菜单状态，但不触发跳转
  initializeActiveMenu();
});
</script>
  
  <style>
.aside {
  background-color: #ffffff;
}
/* .el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 100%;
  height: 100%; 
  background-color: #ffffff;
} */

/* 添加鼠标悬停样式 */
.el-avatar {
  transition: opacity 0.3s;
}
.el-avatar:hover {
  opacity: 0.8;
  cursor: pointer;
}
</style>
  