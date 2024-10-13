<template>
  <div class="aside">
    <el-menu
      :default-active="activeMainMenu"
      class="el-menu-vertical-demo"
      :collapse="isCollapse"
      @open="handleOpen"
      @close="handleClose"
    >
      <el-button @click="toggleCollapse" style="width: 55px; border: 0px">
        <el-icon>
          <component :is="isCollapse ? Expand : Fold" />
        </el-icon>
        {{ isCollapse ? "" : "" }}
      </el-button>
      <el-menu-item
        v-for="item in menuItems"
        :key="item.index"
        :index="item.index"
        @click="setActiveMenu(item.index)"
      >
        <el-icon>
          <component :is="getIconComponent(item.icon)" />
        </el-icon>
        <template #title>{{ item.title }}</template>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import {
  Menu as House,
  Edit,
  Notification,
  Setting,
  Expand,
  Fold,
} from "@element-plus/icons-vue";
import { useMenuStore } from "../store/menuStore";

const { menuItems, activeMainMenu, setActiveMenu, initializeActiveMenu } =
  useMenuStore();

const isCollapse = ref(true);
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
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 100%;
  height: 100%; /* 确保菜单占据整个父容器高度 */
  /*overflow-y: clip; !* 使菜单内容滚动 *!*/
  /* min-height: 400px; */
  background-color: #ffffff;
}
</style>
  