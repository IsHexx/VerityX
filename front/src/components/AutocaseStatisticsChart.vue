<!-- src/components/StatisticsChart.vue -->
<template>
  <el-card class="statistics-card">
    <el-tabs v-model="activeTab" @tab-click="handleTabClick">
      <el-tab-pane label="接口自动化占比" name="api"></el-tab-pane>
      <el-tab-pane label="UI自动化占比" name="ui"></el-tab-pane>
    </el-tabs>

    <!-- 左右布局容器 -->
    <div class="statistics-layout">
      <!-- 用例统计信息 -->
      <div class="statistics-summary">
        <div class="flex flex-wrap gap-4">
          <el-card shadow="always"
            ><el-statistic title="总用例" :value="totalCases">
              <template #suffix>
                <span class="change">
                  周同比
                  <span
                    :class="totalCasesChange >= 0 ? 'increase' : 'decrease'"
                  >
                    {{ totalCasesChange >= 0 ? "+" : ""
                    }}{{ totalCasesChange }}%
                  </span>
                </span>
              </template>
            </el-statistic>
          </el-card>
          <el-card style="margin-top: 40px" shadow="hover"
            ><el-statistic title="自动化用例" :value="automatedCases">
              <template #suffix>
                <span class="change">
                  周同比
                  <span
                    :class="automatedCasesChange >= 0 ? 'increase' : 'decrease'"
                  >
                    {{ automatedCasesChange >= 0 ? "+" : ""
                    }}{{ automatedCasesChange }}%
                  </span>
                </span>
              </template>
            </el-statistic>
          </el-card>
        </div>
      </div>

      <!-- 日期范围和图表 -->
      <div class="date-range">
        <el-row :gutter="20">
          <el-col :span="14"
            ><el-radio-group
              v-model="dateRangeType"
              @change="handleDateRangeChange"
            >
              <el-radio-button label="全年">全年</el-radio-button>
              <el-radio-button label="custom">自定义</el-radio-button>
            </el-radio-group>
          </el-col>
          <el-col :span="10" style="text-align: left"
            ><el-date-picker
              v-model="dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              :disabled="dateRangeType !== 'custom'"
              @change="handleDateRangeChange"
            ></el-date-picker>
          </el-col>
        </el-row>
        <!-- 图表容器 -->
        <div class="chart-container" ref="chartContainer"></div>
      </div>
    </div>
  </el-card>
</template>
  
  <script>
import { ref, onMounted, watch } from "vue";
import * as echarts from "echarts";
import {
  ElCard,
  ElTabs,
  ElTabPane,
  ElStatistic,
  ElDatePicker,
  ElRadioGroup,
  ElRadioButton,
  ElRow,
  ElCol,
} from "element-plus";

export default {
  components: {
    ElCard,
    ElTabs,
    ElTabPane,
    ElStatistic,
    ElDatePicker,
    ElRadioGroup,
    ElRadioButton,
    ElRow,
    ElCol,
  },
  setup() {
    const chartContainer = ref(null);
    const chart = ref(null);
    const activeTab = ref("api");
    const totalCases = ref(4320);
    const totalCasesChange = ref(12);
    const automatedCases = ref(342);
    const automatedCasesChange = ref(-10);
    const dateRangeType = ref("全年");
    const dateRange = ref([new Date(2015, 9, 2), new Date(2015, 9, 10)]);

    const handleTabClick = (tab) => {
      updateChart();
    };

    const initChart = () => {
      if (chartContainer.value) {
        chart.value = echarts.init(chartContainer.value);
        updateChart();
      }
    };

    const updateChart = () => {
      const option = {
        tooltip: {
          trigger: "axis",
          backgroundColor: "rgba(32, 33, 36,.7)",
          borderColor: "rgba(32, 33, 36,0.20)",
          borderWidth: 1,
          textStyle: {
            // 文字提示样式
            color: "#fff",
            fontSize: "12",
          },
          axisPointer: {
            // 坐标轴虚线
            type: "cross",
            label: {
              backgroundColor: "#6a7985",
            },
          },
          formatter: function (params) {
            const dataPoint = params[0];
            const percentage = dataPoint.value;
            // 假设总数是100,你可能需要根据实际情况调整这个计算
            const count = Math.round(percentage); // 这里假设百分比就是实际数量,你可能需要根据实际数据调整
            return `${dataPoint.axisValue}<br/>
                ${dataPoint.marker}${dataPoint.seriesName}: ${percentage}%<br/>
                数量: ${count}`;
          },
        },
        grid: {
          x: 30,
          y: 30,
          x2: 30,
          y2: 30,
          borderWidth: 10,
        },
        xAxis: {
          type: "category",
          data: [
            "10-23周日",
            "10-24周一",
            "10-25周二",
            "10-26周三",
            "10-27周四",
            "10-28周五",
            "10-29周六",
          ],
          axisLine: { show: false },
          axisTick: { show: false },
        },
        yAxis: {
          type: "value",
          axisLine: { show: false },
          axisTick: { show: false },
          splitLine: { lineStyle: { type: "dashed" } },
        },
        series: [
          {
            name: activeTab.value === "api" ? "接口自动化占比" : "UI自动化占比",
            type: "line",
            smooth: true,
            data: [35, 28, 45, 57, 33, 25, 46],
            areaStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: "#e8f4ff" },
                { offset: 1, color: "#e8f4ff" },
              ]),
            },
            itemStyle: { color: "#1890ff" },
            lineStyle: { color: "#1890ff" },
          },
        ],
      };

      chart.value.setOption(option);
    };

    const handleDateRangeChange = () => {
      // 在这里处理日期范围变化，更新图表数据
      updateChart();
    };

    onMounted(initChart);
    watch(activeTab, updateChart);

    return {
      chartContainer,
      activeTab,
      handleTabClick,
      totalCases,
      totalCasesChange,
      automatedCases,
      automatedCasesChange,
      dateRangeType,
      dateRange,
      handleDateRangeChange,
    };
  },
};
</script>
  
  <style scoped>
.grid-content {
  border-radius: 4px;
  min-height: 36px;
}
.statistics-layout {
  display: flex;
}

.statistics-summary {
  flex: 0 0 20%; /* 占 20% 宽度 */
  margin-right: 20px;
  margin-top: 20px;
}

.flex.gap-2.align-top {
  width: 100%;
}

.date-range {
  flex: 1; /* 占剩余空间 */
  text-align: right;
}

.chart-container {
  height: 200px;
  width: 100%;
  margin-top: 0px;
}

.change {
  font-size: 14px;
  margin-left: 8px;
}

.increase {
  color: #67c23a;
}

.decrease {
  color: #f56c6c;
}
</style>