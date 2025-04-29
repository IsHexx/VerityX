<template>
  <div class="report-audit-page">
    <h2 class="page-title">测试报告生成</h2>
    <test-report-workflow />
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import TestReportWorkflow from "./TestReportWorkflow.vue";

// 使用我们创建的工作流组件替代原有的表单
</script>

<style scoped>
.report-audit-page {
  padding: 20px;
}

.page-title {
  margin-bottom: 20px;
  color: #303133;
  font-size: 22px;
}
</style>