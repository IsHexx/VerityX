<template>
    <div class="content">
      <!-- eltree -->
      <api-auto-test-tree></api-auto-test-tree>
      <!-- eltree end-->
  
      <el-card style="width: 76%">
        <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
          <!-- 测试步骤 -->
          <api-auto-test-step></api-auto-test-step>

          <!-- 测试步骤 end-->
  
          <!-- 测试报告 -->
          <api-auto-test-report></api-auto-test-report>
          <!-- 测试报告 end-->
  
        </el-tabs>
      </el-card>
    </div>
  </template>
  
  <script setup>
  import { reactive, ref, watch } from "vue";
  import { Folder, Link, Plus, More } from "@element-plus/icons-vue";
  import { ElMessage, ElMessageBox } from "element-plus";
  import ApiAutoTestStep from "../../components/ApiAutoTestInterface/ApiAutoTestStep.vue";
  import ApiAutoTestReport from "../../components/ApiAutoTestInterface/ApiAutoTestReport.vue";
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
  