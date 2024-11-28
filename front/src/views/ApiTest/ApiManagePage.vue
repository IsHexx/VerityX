<!-- ApiManagePage.vue -->
<template>
  <div class="content">
    <!-- Api Tree -->
    <api-tree></api-tree>
    <!-- Api Tree End -->

    <!-- Main Content -->
    <el-card style="width: 76%">
      <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
         <!-- 预览 -->
         <el-tab-pane label="预览" name="preview">
          <preview-page></preview-page>
        </el-tab-pane>

        <!-- 编辑 -->
        <el-tab-pane label="编辑" name="edit">
          <edit-page></edit-page>
        </el-tab-pane>

        <!-- 运行 -->
        <el-tab-pane label="运行" name="run">
          <run-page></run-page>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, provide, watch } from "vue";
import ApiTree from "../../components/interface/ApiTree.vue";
import PreviewPage from "../../components/interface/PreviewPage.vue";
import EditPage from "../../components/interface/EditPage.vue";
import RunPage from "../../components/interface/RunPage.vue";

// 当前激活的标签页
const activeName = ref("preview");

// 定义响应式数据
const previewApiData = ref(null);
const editApiData = ref(null);
provide("activeName", activeName);

provide("previewApiData", previewApiData);

provide("editApiData", editApiData);


// Tab 切换事件（可选）
// const handleClick = (tab) => {
  
//   if (tab.props.name === "preview") {
//     console.log("当前选中标签页:", tab.props.name);
//     provide("selectedApiData", selectedApiData);
//   } else if (tab.props.name === "edit") {
//     console.log("当前选中标签页:", tab.props.name);
//     provide("editApiData", editApiData);
//   }
  
// };
</script>

<style scoped>
.content {
  flex: 2;
  background-color: #f5f5f5;
  display: flex;
  justify-content: space-between;
}
</style>
