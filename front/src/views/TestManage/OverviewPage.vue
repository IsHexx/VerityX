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
import { ref, onMounted, nextTick, watch } from "vue";
import * as echarts from "echarts";
import CaseStatisticsCard from "@/components/CaseStatisticsCard.vue";
import BugStatisticsCard from "@/components/BugStatisticsCard.vue";
import AutocaseStatisticsChart from "@/components/AutocaseStatisticsChart.vue";


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