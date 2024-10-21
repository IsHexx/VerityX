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
        <div class="chart-wrapper">
          <!-- 环形图表的容器 -->
          <div
            :ref="(el) => {
              if (el) chartRefs[tab.name] = el;
            }"
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

const getPercentage = (item, tabName) => {
  const totalCount = getTotalCount(tabName);
  return ((item.value / totalCount) * 100).toFixed(0);
};

const getColor = (name) => {
  const colorMapping = {
    "P0": "#4e7bfd",
    "P1": "#45c8dc",
    "P2": "#f5d36a",
    "P3": "#f76c85",
    "其他": "#4a9d7f",
    "功能": "#4e7bfd",
    "性能": "#45c8dc",
    "安全": "#f5d36a",
    "界面": "#f76c85",
    "张三": "#4e7bfd",
    "李四": "#45c8dc",
    "王五": "#f5d36a",
    "赵六": "#f76c85",
  };
  return colorMapping[name] || "#ccc";
};

const getIcon = (name) => {
  const iconMapping = {
    circle,
  };
  return iconMapping[name] || "circle";
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
      percentage: getPercentage(item, tabName),
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
            borderRadius: 5,
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
            text: "用例数量",
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
  justify-content: space-between;
}



.chart {
  min-width: 200px;
  min-height: 200px;
  flex:  60%; /* 占 80% 宽度 */
}

.legend-container {
  flex: 40%; /* 占剩余空间 */
  padding-left: 20px;
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
  width: 30px; /* 根据需求调整宽度 */
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
