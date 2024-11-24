<template>
    <div class="content">
      <!-- eltree -->
      <api-auto-test-tree></api-auto-test-tree>
      <!-- eltree end-->
  
      <el-card style="width: 76%">
        <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
          <!-- 测试步骤 -->
          <test-step ></test-step>

          <!-- 测试步骤 end-->
  
          <!-- 测试报告 -->
          <api-test-report> </api-test-report>
          <!-- 测试报告 end-->
  
        </el-tabs>
      </el-card>
    </div>
  </template>
  
  <script setup>
  import { reactive, ref, watch } from "vue";
  import { Folder, Link, Plus, More } from "@element-plus/icons-vue";
  import { ElMessage, ElMessageBox } from "element-plus";
  import TestStep from "../../components/ApiAutoTestInterface/TestStep.vue";
  import ApiTestReport from "../../components/ApiAutoTestInterface/ApiTestReport.vue";
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
  