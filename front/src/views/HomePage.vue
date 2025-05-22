<template>
  <div class="common-layout">
    <el-container>
      <!-- 侧边栏 -->
      <el-aside class="fixed-aside">
        <common-aside @update-top-menu="updateTopMenu"></common-aside>
      </el-aside>

      <!-- 右侧内容，Header 和 Main 为上下布局 -->
      <el-container direction="vertical">
        <!-- Header 部分 -->
        <el-header>
          <!-- 引用 CommonHeader，传递顶部菜单 -->
          <common-header :sub-menus="currentSubMenus"></common-header>
        </el-header>

        <!-- Main 部分 -->
        <el-main style="margin-top: 0;">
          <router-view></router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import CommonHeader from "@/components/CommonHeader.vue";
import CommonAside from "@/components/CommonAside.vue";
import { useMenuStore } from '@/store/menuStore';
import { useProjectStore } from '@/store/projectStore';
import { useRoute } from 'vue-router';

// 当前顶部菜单的子菜单
const currentSubMenus = ref([]);
const { menuItems } = useMenuStore();
const { currentProject, initProjectState } = useProjectStore();
const route = useRoute();

// 更新顶部菜单
const updateTopMenu = (mainMenuIndex) => {
  console.log('更新顶部菜单:', mainMenuIndex);
  const mainMenu = menuItems.find(item => item.index === mainMenuIndex);
  currentSubMenus.value = mainMenu ? mainMenu.subMenus : [];
};

// 在组件挂载时初始化项目状态
onMounted(() => {
  console.log('HomePage组件挂载');
  // 初始化项目状态
  initProjectState();
  
  // 打印当前项目信息
  console.log('当前项目信息(HomePage):', currentProject.value);
});

// 监听路由变化
watch(() => route.path, (newPath) => {
  console.log('路由变化:', newPath);
}, { immediate: true });
</script>

<style scoped>
.common-layout {
  height: 100vh; /* 设置整体布局占满整个视口高度 */
}

.el-header{
  padding: 0px;
  height: 100%;
}

.fixed-aside {
  background-color: #ffffff;
  color: #333;
  height: calc(100vh);
  overflow-y: auto;
  width: auto;
  max-width: 200px;
  border-radius: 20px 0px 0px 20px;
}

.common-layout > .el-container {
  height: 100%;
}

.common-layout > .el-container > .el-container > .el-header {
  background-color: #fff;
  height: 8%; /* Main 高度等于视口高度减去 Header 的高度 */
  margin-top: 0px;
}

.common-layout > .el-container > .el-container > .el-main {
  background-color: #fff;
  height: calc(100vh - 9%); /* Main 高度等于视口高度减去 Header 的高度 */
  margin-top: 0px;
  padding: 0px;
}

* {
  margin: 0;
  padding: 0;
}
</style>
