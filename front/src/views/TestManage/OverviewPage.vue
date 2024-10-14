<!-- src/components/MainContent.vue -->
<template>
  <div class="main-layout">
    <!-- 上部内容 -->
    <div class="upper-content">
      <el-card>
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
          <el-tab-pane label="优先级" name="casePriority">
            <div ref="priorityChartRef" class="chart-container"></div>
          </el-tab-pane>
          <el-tab-pane label="类型" name="casetype">
            <div ref="typeChartRef" class="chart-container" style="{width: containerwidth + px, height: containerHeight + px}"></div>
          </el-tab-pane>
          <el-tab-pane label="责任人" name="caseResponsiblePerson">
            <div ref="personChartRef" class="chart-container"></div>
          </el-tab-pane>
        </el-tabs>
      </el-card>
    </div>
    <!-- 下部内容 -->
    <div class="lower-content">下部内容</div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from "vue";
import * as echarts from "echarts";

const activeName = ref("casePriority");
const priorityChartRef = ref(null);
const typeChartRef = ref(null);
const personChartRef = ref(null);

let charts = {
  casePriority: null,
  casetype: null,
  caseResponsiblePerson: null
};

const initChart = (name) => {
  const chartRefs = {
    casePriority: priorityChartRef,
    casetype: typeChartRef,
    caseResponsiblePerson: personChartRef
  };

  const chartOptions = {
    casePriority: initPriorityChartOption,
    casetype: initTypeChartOption,
    caseResponsiblePerson: initPersonChartOption
  };

  if (chartRefs[name].value) {
    if (charts[name]) {
      charts[name].dispose();
    }
    charts[name] = echarts.init(chartRefs[name].value);
    charts[name].setOption(chartOptions[name]());
    charts[name].resize();
  }
};

const handleClick = (tab) => {
  nextTick(() => {
    switch (tab.props.name) {
      case "casePriority":
        console.log("casePriority");
        initPriorityChartRef();
        break;
      case "casetype":
        console.log("casetype");
        initTypeChartRef();
        break;
      case "caseResponsiblePerson":
        console.log("caseResponsiblePerson");
        initPersonChartRef();
        break;
    }
  });
};

const initPriorityChartRef = () => {
  if (priorityChartRef.value) {
    const myChart = echarts.init(priorityChartRef.value);
    const option = {
      tooltip: {
        trigger: "item",
      },
      color: ["#3aa1ff", "#4ecb73", "#fbd437", "#f04864"],
      legend: {
        orient: "vertical",
        right: 10,
        top: "center",
        padding: [0, 300],
        itemGap: 15,
      },

      series: [
        {
          name: "",
          type: "pie",
          radius: ["50%", "70%"],
          center: ["20%", "50%"], // 将图表向左移动
          avoidLabelOverlap: false,
          itemStyle: {
            borderRadius: 2,
            borderColor: "#fff",
            borderWidth: 2,
          },
          label: {
            show: false,
            position: "lefter",
          },
          emphasis: {
            label: {
              show: false,
              fontSize: 20,
              fontWeight: "",
            },
          },
          labelLine: {
            show: false,
          },
          data: [
            { value: 1048, name: "P1" },
            { value: 735, name: "P2" },
            { value: 580, name: "P3" },
            { value: 484, name: "P4" },
          ],
        },
      ],
      graphic: [
        {
          type: "text",
          left: "65%", // 将文本水平居中
          top: "19%", // 根据实际情况调整，确保文本显示在legend上方
          style: {
            // 使用富文本格式
            rich: {
              a: {
                // 为“80”设置的样式
                fontSize: 16, // 放大字体大小
                fontWeight: "bold", // 加粗
                color: "#000", // 字体颜色
              },
              b: {
                // 为“个/5800万”设置的样式
                fontSize: 14, // 字体大小
                color: "#929292",
              },
            },
            // text: '项目总数 {a|80} {b|个/5800万}', // 显示的文本内容，使用上面定义的样式标签
            textAlign: "center",
            fill: "#000", // 默认文本颜色，这里可能不会生效因为已经在rich中指定了颜色
          },
        },
        {
          type: "text",
          left: "17%",
          top: "40%",
          style: {
            text: "72%",
            textAlign: "center",
            fill: "#000",
            fontSize: 20,
          },
        },
        {
          type: "text",
          left: "17%",
          top: "55%",
          z: 100,
          style: {
            text: "转化率",
            textAlign: "center",
            fill: "#000",
            fontSize: 14,
          },
        },
      ],
    };
    myChart.setOption(option);
  }
};

const initTypeChartRef = () => {
  if (typeChartRef.value) {
    const myChart = echarts.init(typeChartRef.value);
    const option = {
      tooltip: {
        trigger: "item",
      },
      color: ["#3aa1ff", "#4ecb73", "#fbd437", "#f04864"],
      legend: {
        orient: "vertical",
        right: 10,
        top: "center",
        padding: [0, 300],
        itemGap: 15,
      },

      series: [
        {
          name: "",
          type: "pie",
          radius: ["50%", "70%"],
          center: ["20%", "50%"], // 将图表向左移动
          avoidLabelOverlap: false,
          itemStyle: {
            borderRadius: 2,
            borderColor: "#fff",
            borderWidth: 2,
          },
          label: {
            show: false,
            position: "lefter",
          },
          emphasis: {
            label: {
              show: false,
              fontSize: 20,
              fontWeight: "",
            },
          },
          labelLine: {
            show: false,
          },
          data: [
            { value: 1048, name: "P1" },
            { value: 735, name: "P2" },
            { value: 580, name: "P3" },
            { value: 484, name: "P4" },
          ],
        },
      ],
      graphic: [
        {
          type: "text",
          left: "65%", // 将文本水平居中
          top: "19%", // 根据实际情况调整，确保文本显示在legend上方
          style: {
            // 使用富文本格式
            rich: {
              a: {
                // 为“80”设置的样式
                fontSize: 16, // 放大字体大小
                fontWeight: "bold", // 加粗
                color: "#000", // 字体颜色
              },
              b: {
                // 为“个/5800万”设置的样式
                fontSize: 14, // 字体大小
                color: "#929292",
              },
            },
            // text: '项目总数 {a|80} {b|个/5800万}', // 显示的文本内容，使用上面定义的样式标签
            textAlign: "center",
            fill: "#000", // 默认文本颜色，这里可能不会生效因为已经在rich中指定了颜色
          },
        },
        {
          type: "text",
          left: "17%",
          top: "40%",
          style: {
            text: "72%",
            textAlign: "center",
            fill: "#000",
            fontSize: 20,
          },
        },
        {
          type: "text",
          left: "17%",
          top: "55%",
          z: 100,
          style: {
            text: "转化率",
            textAlign: "center",
            fill: "#000",
            fontSize: 14,
          },
        },
      ],
    };
    myChart.setOption(option);
  }
};

const initPersonChartRef = () => {
  if (personChartRef.value) {
    const myChart = echarts.init(personChartRef.value);
    const option = {
      tooltip: {
        trigger: "item",
      },
      color: ["#3aa1ff", "#4ecb73", "#fbd437", "#f04864"],
      legend: {
        orient: "vertical",
        right: 10,
        top: "center",
        padding: [0, 300],
        itemGap: 15,
      },

      series: [
        {
          name: "",
          type: "pie",
          radius: ["50%", "70%"],
          center: ["20%", "50%"], // 将图表向左移动
          avoidLabelOverlap: false,
          itemStyle: {
            borderRadius: 2,
            borderColor: "#fff",
            borderWidth: 2,
          },
          label: {
            show: false,
            position: "lefter",
          },
          emphasis: {
            label: {
              show: false,
              fontSize: 20,
              fontWeight: "",
            },
          },
          labelLine: {
            show: false,
          },
          data: [
            { value: 1048, name: "P1" },
            { value: 735, name: "P2" },
            { value: 580, name: "P3" },
            { value: 484, name: "P4" },
          ],
        },
      ],
      graphic: [
        {
          type: "text",
          left: "65%", // 将文本水平居中
          top: "19%", // 根据实际情况调整，确保文本显示在legend上方
          style: {
            // 使用富文本格式
            rich: {
              a: {
                // 为“80”设置的样式
                fontSize: 16, // 放大字体大小
                fontWeight: "bold", // 加粗
                color: "#000", // 字体颜色
              },
              b: {
                // 为“个/5800万”设置的样式
                fontSize: 14, // 字体大小
                color: "#929292",
              },
            },
            // text: '项目总数 {a|80} {b|个/5800万}', // 显示的文本内容，使用上面定义的样式标签
            textAlign: "center",
            fill: "#000", // 默认文本颜色，这里可能不会生效因为已经在rich中指定了颜色
          },
        },
        {
          type: "text",
          left: "17%",
          top: "40%",
          style: {
            text: "72%",
            textAlign: "center",
            fill: "#000",
            fontSize: 20,
          },
        },
        {
          type: "text",
          left: "17%",
          top: "55%",
          z: 100,
          style: {
            text: "转化率",
            textAlign: "center",
            fill: "#000",
            fontSize: 14,
          },
        },
      ],
    };
    myChart.setOption(option);
  }
};

watch(activeName, (newVal) => {
  nextTick(() => {
    initChart(newVal);
  });
});

onMounted(() => {
  nextTick(() => {
    initChart(activeName.value);
    window.addEventListener('resize', resizeCharts);
  });
});

onMounted(() => {
  nextTick(() => {
    initPriorityChartRef();
  });
});
</script>


<style scoped>
.main-layout {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.chart-container {
  width: 100%;
  height: 300px;
}

.el-card {
  width: 48%;
}

.upper-content {
  flex: 2; /* 上部区域占 2/3 高度 */
  background-color: #f5f5f5;
  padding: 20px;
  width: 100%;
  text-align: left;
  border-radius: 4px;
}

.lower-content {
  flex: 1; /* 下部区域占 1/3 高度 */
  background-color: #e0e0e0;
  padding: 20px;
  width: 100%;
  text-align: center;
  border-radius: 4px;
}
</style>
