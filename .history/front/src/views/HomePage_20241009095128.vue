<template>
    <div class="common-layout">
      <el-container>
        <!-- 侧边栏 -->
        <el-aside class="fixed-aside">
            <common-aside :activeMenu="activeMenu" @menuSelect="handleMenuSelect"></common-aside>
        </el-aside>
  
        <!-- 右侧内容，Header 和 Main 为上下布局 -->
        <el-container direction="vertical">
          <!-- Header 部分 -->
          <el-header>
            <!-- 引用 CommonHeader -->
            <common-header :activeMenu="activeMenu" @menuSelect="handleMenuSelect"></common-header>
          </el-header>
  
          <!-- Main 部分 -->
          <el-main>
            Main Content
          </el-main>
        </el-container>
      </el-container>
    </div>
  </template>
  
  <script>
  import CommonHeader from "../components/CommonHeader.vue";
  import CommonAside from "../components/CommonAside.vue";
  
  export default {
    components: {
      CommonAside,
      CommonHeader,
    },
  };
  // 当前选中的菜单项，父组件管理选中状态
const activeMenu = ref('1');

// 处理菜单选择
const handleMenuSelect = (index) => {
  activeMenu.value = index;
};
  </script>
  
  <style scoped>
  .common-layout {
    height: 100vh; /* 设置整体布局占满整个视口高度 */
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
  

  
  .common-layout > .el-container > .el-container > .el-main {
    background-color: #fff;
    height: calc(100vh - 6%); /* Main 高度等于视口高度减去 Header 的高度 */
    margin-top: 20px;
    padding: 20px;
  }
  </style>
  