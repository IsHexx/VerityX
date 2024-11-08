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
        @click="setActiveMenu(item.index)"
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
import { ref, onMounted, computed  } from "vue";
import logo from "@/assets/logo.png";
import { useRouter } from 'vue-router'; // 添加这行

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

const router = useRouter(); // 添加这行
const { menuItems, activeMainMenu, setActiveMenu, initializeActiveMenu } = useMenuStore();

const isCollapse = ref(true);

// 添加头像点击处理函数
const handleAvatarClick = () => {
  // 跳转到项目管理页面并更新菜单状态
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
  