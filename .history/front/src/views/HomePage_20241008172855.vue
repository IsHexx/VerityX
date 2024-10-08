<template>
    <div class="common-layout">
        <el-container> 
            <!-- 侧边栏 -->
            <el-aside class="fixed-aside">
                <common-aside></common-aside>
            </el-aside>

            <!-- 右侧内容，Header 和 Main 为上下布局 -->
            <el-container direction="vertical">
                <!-- Header 部分 -->
                <el-header height="60px">
                    <common-header></common-header>
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
    }
}
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


.common-layout > .el-container > .el-container > .el-header {
    background-color: #f5f5f5;
    padding: 0 20px;
    height: 7%; /* 固定 Header 的高度 */
}

.common-layout > .el-container > .el-container > .el-main {
    padding: 20px;
    background-color: #fff;
    height: calc(100vh - 7%); /* Main 高度等于视口高度减去 Header 的高度 */
}
</style>
