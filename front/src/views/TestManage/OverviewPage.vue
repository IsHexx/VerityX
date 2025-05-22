<!-- src/components/MainContent.vue -->
<template>
  
  <div class="main-layout">
    <div class="upper-content">
      <CaseStatisticsCard />
      <BugStatisticsCard />
    </div>
    <div class="lower-content"><AutocaseStatisticsChart /></div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick, watch, computed } from "vue";
import * as echarts from "echarts";
import CaseStatisticsCard from "@/components/CaseStatisticsCard.vue";
import BugStatisticsCard from "@/components/BugStatisticsCard.vue";
import AutocaseStatisticsChart from "@/components/AutocaseStatisticsChart.vue";
import { useProjectStore } from "@/store/projectStore";

// 使用项目Store
const projectStore = useProjectStore();
// 确保初始化项目状态
projectStore.initProjectState();

// 计算当前项目ID
const currentProjectId = computed(() => projectStore.getCurrentProjectId());

// 监听项目变化，刷新数据
watch(currentProjectId, (newProjectId) => {
  if (newProjectId) {
    console.log('概览页面检测到项目变更:', newProjectId);
    // 这里可以添加刷新概览数据的逻辑
  }
});

</script>

<style scoped>
.main-layout {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.upper-content {
  flex: 2;
  padding: 2px 10px 4px 10px;
  background-color: #ffffff;
  display: flex;
  justify-content: space-between;
}


.chart-card {
  height: 100%;
  width: 49%;
}

.chart {
  width: 100%;
  height: 100%;
}

.lower-content {
  flex: 1;
  background-color: #ffffff;
  padding: 0px 10px 0px 10px;
  text-align: center;
}
</style>