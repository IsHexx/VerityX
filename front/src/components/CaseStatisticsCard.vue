<!-- src/components/DefectStatisticsCard.vue -->
<template>
  <el-card class="chart-card">
    <template #header>
      <div class="card-header">
        <span>用例数据统计</span>
      </div>
    </template>
    <el-tabs
      v-model="activeName"
      type="card"
      class="demo-tabs"
      @tab-click="handleClick"
    >
      <el-tab-pane
        v-for="tab in tabs"
        :key="tab.name"
        :label="tab.label"
        :name="tab.name"
      >
        <div class="chart-container">
          <div
            :ref="
              (el) => {
                if (el) chartRefs[tab.name] = el;
              }
            "
            class="chart"
          ></div>
        </div>
      </el-tab-pane>
    </el-tabs>
  </el-card>
</template>
  
  <script setup>
import { ref, onMounted, nextTick, watch } from "vue";
import * as echarts from "echarts";

const activeName = ref("casePriority");
const chartRefs = ref({});
const charts = ref({});

const tabs = [
  { name: "casePriority", label: "优先级" },
  { name: "casetype", label: "类型" },
  { name: "caseResponsiblePerson", label: "责任人" },
];

const chartData = {
  casePriority: [
    { value: 44316, name: "P0" },
    { value: 24645, name: "P1" },
    { value: 19716, name: "P2" },
    { value: 11090, name: "P3" },
    { value: 23457, name: "其他" },
  ],
  casetype: [
    { value: 50000, name: "功能" },
    { value: 30000, name: "性能" },
    { value: 25000, name: "安全" },
    { value: 18224, name: "界面" },
  ],
  caseResponsiblePerson: [
    { value: 40000, name: "张三" },
    { value: 35000, name: "李四" },
    { value: 30000, name: "王五" },
    { value: 18224, name: "赵六" },
  ],
};

const handleClick = (tab) => {
  nextTick(() => {
    initChart(tab.props.name);
  });
};

const getTotalCount = (tabName) => {
  return chartData[tabName].reduce((sum, item) => sum + item.value, 0);
};

const initChart = (tabName) => {
  const chartDom = chartRefs.value[tabName];
  if (chartDom) {
    if (charts.value[tabName]) {
      charts.value[tabName].dispose();
    }
    charts.value[tabName] = echarts.init(chartDom);

    const totalCount = getTotalCount(tabName);
    const formattedData = chartData[tabName].map((item) => ({
      ...item,
      percentage: ((item.value / totalCount) * 100).toFixed(0),
    }));

    const option = {
      tooltip: {
        trigger: "item",
        formatter: "{b}: {c} ({d}%)",
      },
      legend: {
        orient: "vertical",
        right: "23%",
        top: "middle",
        itemWidth: 10,
        itemHeight: 10,
        icon: "circle",
        itemGap: 20,
        formatter: (name) => {
          const item = formattedData.find((item) => item.name === name);
          return `${name.padEnd(4)}    ${item.value.toLocaleString()}     ${
            item.percentage
          }%`;
        },
      },
      color: ["#4e7bfd", "#45c8dc", "#f5d36a", "#f76c85", "#4a9d7f"],
      series: [
        {
          name: tabName,
          type: "pie",
          radius: ["50%", "70%"],
          center: ["30%", "50%"],
          avoidLabelOverlap: false,
          label: {
            show: false,
          },
          emphasis: {
            label: {
              show: false,
              fontSize: "18",
              fontWeight: "bold",
            },
          },
          itemStyle: {
            borderRadius: 10,
            borderColor: "#fff",
            borderWidth: 1,
          },
          labelLine: {
            show: false,
          },
          data: formattedData,
        },
      ],
      graphic: [
        {
          type: "text",
          left: "25%",
          top: "38%",
          style: {
            text: "用例数量",
            textAlign: "center",
            fill: "#999",
            fontSize: 14,
          },
        },
        {
          type: "text",
          left: "22%",
          top: "48%",
          style: {
            text: totalCount.toLocaleString(),
            textAlign: "center",
            fill: "#333",
            fontSize: 24,
            fontWeight: "bold",
          },
        },
        {
          type: "text",
          left: "25%",
          top: "58%",
          style: {
            text: "本周 +0>",
            textAlign: "center",
            fill: "#45c8dc",
            fontSize: 14,
          },
        },
      ],
    };
    charts.value[tabName].setOption(option);
  }
};

const resizeAllCharts = () => {
  Object.values(charts.value).forEach((chart) => {
    if (chart) {
      chart.resize();
    }
  });
};

onMounted(() => {
  nextTick(() => {
    tabs.forEach((tab) => {
      initChart(tab.name);
    });
    window.addEventListener("resize", resizeAllCharts);
  });
});

watch(activeName, (newValue) => {
  nextTick(() => {
    initChart(newValue);
  });
});
</script>
  
  <style scoped>
.chart-card {
  height: 100%;
  width: 47%;
}

.chart-container {
  height: 300px;
}

.chart {
  width: 100%;
  height: 100%;
}

</style>