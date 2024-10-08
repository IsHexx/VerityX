<template>
    <div class="common-layout">
        <el-container>
            <el-aside width="180px">
                <!-- 要注意这里引用CommonAside组件的方法，拆分了两个字符，用中短线连接的-->
                <common-aside></common-aside>
            </el-aside>
            <el-container>
                <el-header>Header</el-header>
                <el-main>Main</el-main>
            </el-container>
        </el-container>
    </div>
</template>

<script>
import CommonAside from "../components/CommonAside.vue";
export default {
    components: {
        CommonAside,
    }
}
</script>

<style   scoped>
.common-layout {
    height: 100%;
}

.common-layout > .el-container {
    height: 100%;
}

.common-layout > .el-container > .el-aside {
    height: 100%;
    background: #f2f2f3;
}

.common-layout > .el-container > .el-container > .el-header > .el-main {
    padding: 0%;
}
</style>
