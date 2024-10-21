<!-- src/components/BugStatisticsCard.vue -->
<template>
    <el-card class="chart-card">
      <template #header>
        <div class="card-header">
          <span>缺陷数量统计</span>
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
          <div class="chart-wrapper">
            <div
              :ref="
                (el) => {
                  if (el) chartRefs[tab.name] = el;
                }
              "
              class="chart"
            ></div>
             <!-- 图例的容器 -->
          <div class="legend-container">
            <div
              v-for="item in chartData[tab.name]"
              :key="item.name"
              class="legend-item"
            >
              <span class="legend-color" :style="{ backgroundColor: getColor(item.name) }"></span>
              <span class="legend-text">
                <span class="legend-name">{{ item.name }}</span>
                <span class="legend-value">{{ item.value.toLocaleString() }}</span>
                <span class="legend-percentage">{{ getPercentage(item, tab.name) }}%</span>
              </span>
            </div>
          </div>
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
    { name: "casePriority", label: "遗留缺陷" },
    { name: "casetype", label: "严重级别" },
    { name: "caseResponsiblePerson", label: "责任人" },
  ];
  
  const chartData = {
    casePriority: [
      { value: 44316, name: "已修复" },
      { value: 24645, name: "已验证" },
      { value: 19716, name: "重新打开" },
    ],
    casetype: [
      { value: 50000, name: "阻塞" },
      { value: 30000, name: "严重" },
      { value: 25000, name: "一般" },
      { value: 18224, name: "轻微" },
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

  const getPercentage = (item, tabName) => {
  const totalCount = getTotalCount(tabName);
  return ((item.value / totalCount) * 100).toFixed(0);
};

const getColor = (name) => {
  const colorMapping = {
    "已修复": "#4e7bfd",
    "已验证": "#45c8dc",
    "重新打开": "#f5d36a",
    "阻塞": "#4e7bfd",
    "严重": "#45c8dc",
    "一般": "#f5d36a",
    "轻微": "#f76c85",
    "张三": "#4e7bfd",
    "李四": "#45c8dc",
    "王五": "#f5d36a",
    "赵六": "#f76c85",
  };
  return colorMapping[name] || "#ccc";
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
        color: ["#4e7bfd", "#45c8dc", "#f5d36a", "#f76c85", "#4a9d7f"],
        series: [
          {
            name: tabName,
            type: "pie",
            radius: ["70%", "90%"],
            center: ["50%", "50%"],
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
            left: "center",
            top: "30%",
            style: {
              text: "缺陷数量",
              textAlign: "center",
              fill: "#999",
              fontSize: 14,
            },
          },
          {
            type: "text",
            left: "center",
            top: "center",
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
            left: "center",
            top: "60%",
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

.chart-wrapper {
  display: flex;
  height: 100%;
}

.el-card__body {
  height: 80%;
  align-content: center;
}

.el-tabs__content {
  align-content: center;
  height: 100%;
}


.demo-tabs {
  height: 100%;
  align-content: center;
}

.chart {
  min-width: 200px;
  min-height: 200px;
  flex:  60%; /* 占 80% 宽度 */
}

.legend-container {
  flex: 40%; /* 占剩余空间 */
  padding-left: 20px;
  align-content: center;
}

.legend-color {
  width: 16px;
  height: 16px;
  margin-right: 10px;
  display: inline-block;
  border-radius: 50%;  /* 使手动渲染的图例图标为圆形 */
}

.legend-item {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.legend-color {
  width: 8px;
  height: 8px;
  margin-right: 10px;
  margin-top: 5px;
  display: inline-block;
}

.legend-text {
  font-size: 14px;
  margin-top: 5px;
}

.legend-name {
  display: inline-block;
  width: 60px; /* 根据需求调整宽度 */
  text-align: left;
}

.legend-value {
  display: inline-block;
  width: 50px; /* 根据需求调整宽度 */
  text-align: left;
  margin-left: 10px;
}

.legend-percentage {
  display: inline-block;
  width: 30px; /* 根据需求调整宽度 */
  text-align: left;
  margin-left: 10px;
}
</style>