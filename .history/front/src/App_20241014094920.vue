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

<script>
import CommonHeader from "./components/CommonHeader.vue";
import CommonAside from "./components/CommonAside.vue";
import { useMenuStore } from '@/store/menuStore';

export default {
  components: {
    CommonAside,
    CommonHeader,
  },
  data() {
    return {
      currentSubMenus: [] // 用于存储当前顶部菜单的子菜单
    }
  },
  methods: {
    updateTopMenu(mainMenuIndex) {
      const { menuItems } = useMenuStore();
      const mainMenu = menuItems.find(item => item.index === mainMenuIndex);
      this.currentSubMenus = mainMenu ? mainMenu.subMenus : [];
    }
  }
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
  height: calc(100vh - 7%); /* Main 高度等于视口高度减去 Header 的高度 */
  margin-top: 20px;
  padding: 20px;
}

* {
  margin-top: -5px;
  padding: 0;
}
</style>
