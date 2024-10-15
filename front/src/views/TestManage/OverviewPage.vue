<!-- src/components/MainContent.vue -->
<template>
  <div class="main-layout">
    <div class="upper-content">
      <CaseStatisticsCard />
      <BugStatisticsCard />
    </div>
    <div class="lower-content"><StatisticsChart /></div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick, watch } from "vue";
import * as echarts from "echarts";
import CaseStatisticsCard from "@/components/CaseStatisticsCard.vue";
import BugStatisticsCard from "@/components/BugStatisticsCard.vue";
import StatisticsChart from "@/components/StatisticsChart.vue";


</script>

<style scoped>
.main-layout {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.upper-content {
  flex: 2;
  padding: 20px;
  background-color: #f5f5f5;
  display: flex;
  justify-content: space-between;
}


.chart-card {
  height: 100%;
  width: 49%;
}

.chart-container {
  height: 300px;
}

.chart {
  width: 100%;
  height: 100%;
}

.lower-content {
  flex: 1;
  background-color: #f4f4f4;
  padding: 20px;
  text-align: center;
}
</style>