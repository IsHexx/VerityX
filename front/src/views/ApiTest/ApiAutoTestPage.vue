<template>
  <div class="api-autotest-container">
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
  </div>
</template>
  
<script setup>
import { reactive, ref, watch, provide, onMounted } from "vue";
import { Folder, Link, Plus, More } from "@element-plus/icons-vue";
import { ElMessage, ElMessageBox } from "element-plus";
import ApiAutoTestStep from "../../components/ApiAutoTestInterface/ApiAutoTestStep.vue";
import ApiAutoTestReport from "../../components/ApiAutoTestInterface/ApiAutoTestReport.vue";
import ApiAutoTestTree from "../../components/ApiAutoTestInterface/ApiAutoTestTree.vue";
import { useProjectStore } from "@/store/projectStore";
import ApiAutomationApi from "@/api/apiAutomationService";

// 当前激活的标签页
const activeName = ref("preview");

// 获取项目存储
const projectStore = useProjectStore();

// 加载数据
const loadAutomationData = () => {
  console.log("加载自动化测试数据，项目ID:", projectStore.currentProjectId);
  ApiAutomationApi.getAllScenes()
    .then(response => {
      console.log("自动化测试数据加载成功:", response);
      // 数据会在子组件内处理
    })
    .catch(error => {
      console.error("自动化测试数据加载失败:", error);
      ElMessage.error("自动化测试数据加载失败");
    });
};

// 组件挂载时加载数据
onMounted(() => {
  loadAutomationData();
});

// 项目ID变化时重新加载数据
watch(() => projectStore.currentProjectId, (newProjectId, oldProjectId) => {
  if (newProjectId !== oldProjectId) {
    console.log("项目ID变化, 重新加载自动化测试数据");
    loadAutomationData();
  }
});

// 传递项目ID给子组件
provide("projectStore", projectStore);

// 切换标签页
const handleClick = (tab, event) => {
  console.log("Tab clicked:", tab.props.label);
};
</script>
  
<style scoped>
.api-autotest-container {
  display: flex;
  flex-direction: column;
}

.content {
  flex: 2;
  background-color: #f5f5f5;
  display: flex;
  justify-content: space-between;
}
</style>
  