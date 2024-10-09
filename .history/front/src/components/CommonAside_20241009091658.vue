<template>
  <div class="aside">
    <el-menu
      default-active="2"
      class="el-menu-vertical-demo"
      :collapse="isCollapse"
      @open="handleOpen"
      @close="handleClose"
    >
      <el-button
        :icon="isCollapse ? 'el-icon-arrow-right' : 'el-icon-arrow-down'"
        @click="toggleCollapse"
        style="width: 55px; border: 0px"
      >
        {{ isCollapse ? "展开" : "折叠" }}
      </el-button>
      <el-menu-item  index="1">
        <el-icon><House /></el-icon>
        <template #title>Navigator One</template>
      </el-menu-item>
      <el-menu-item index="2">
        <el-icon><Notification /></el-icon>
        <template #title>Navigator Two</template>
      </el-menu-item>
      <el-menu-item index="3">
        <el-icon><Edit /></el-icon>
        <template #title>Navigator Three</template>
      </el-menu-item>
      <el-menu-item index="4">
        <el-icon><setting /></el-icon>
        <template #title>Navigator Four</template>
      </el-menu-item>
    </el-menu>
  </div>
</template>
  
  <script  setup>
import { ref } from "vue";
import {
  Menu as House,
  Edit,
  Notification,
  Setting,
} from "@element-plus/icons-vue";

const isCollapse = ref(true);

const toggleCollapse = () => {
  isCollapse.value = !isCollapse.value;
};
const handleOpen = (key) => {
  console.log(key);
};
const handleClose = (key) => {
  console.log(key);
};
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
  