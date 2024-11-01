<template>
    <div class="content">
      <!-- eltree -->
      <api-auto-test-tree></api-auto-test-tree>
      <!-- eltree end-->
  
      <el-card style="width: 76%">
        <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
          <!-- 预览 -->
  
          <preview-page> </preview-page>
          <!-- 预览 end-->
  
          <!-- 编辑 -->
          <edit-page> </edit-page>
          <!-- 编辑 end-->
  
          <!-- 运行 -->
          <run-page> </run-page>
          <!-- 运行 end-->
        </el-tabs>
      </el-card>
    </div>
  </template>
  
  <script setup>
  import { reactive, ref, watch } from "vue";
  import { Folder, Link, Plus, More } from "@element-plus/icons-vue";
  import { ElMessage, ElMessageBox } from "element-plus";
  import PreviewPage from "../../components/interface/PreviewPage.vue";
  import EditPage from "../../components/interface/EditPage.vue";
  import ApiAutoTestTree from "../../components/ApiAutoTestInterface/ApiAutoTestTree.vue";

  
  const activeName = ref("preview");
  
  </script>
  
  <style scoped>
  .content {
    flex: 2;
    background-color: #f5f5f5;
    display: flex;
    justify-content: space-between;
  }
  </style>
  