<!-- src/components/StatisticsChart.vue -->
<template>
    <el-card class="statistics-card">
      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <el-tab-pane label="接口自动化占比" name="api"></el-tab-pane>
        <el-tab-pane label="UI自动化占比" name="ui"></el-tab-pane>
      </el-tabs>
      
      <div class="statistics-summary">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-statistic title="总用例" :value="totalCases">
              <template #suffix>
                <span class="change">
                  周同比
                  <span :class="totalCasesChange >= 0 ? 'increase' : 'decrease'">
                    {{ totalCasesChange >= 0 ? '+' : '' }}{{ totalCasesChange }}%
                  </span>
                </span>
              </template>
            </el-statistic>
          </el-col>
          <el-col :span="12">
            <el-statistic title="自动化用例" :value="automatedCases">
              <template #suffix>
                <span class="change">
                  周环比
                  <span :class="automatedCasesChange >= 0 ? 'increase' : 'decrease'">
                    {{ automatedCasesChange >= 0 ? '+' : '' }}{{ automatedCasesChange }}%
                  </span>
                </span>
              </template>
            </el-statistic>
          </el-col>
        </el-row>
      </div>
  
      <div class="chart-container" ref="chartContainer"></div>
  
      <div class="date-range">
        <el-radio-group v-model="dateRangeType" @change="handleDateRangeChange">
          <el-radio-button label="全年">全年</el-radio-button>
          <el-radio-button label="custom">自定义</el-radio-button>
        </el-radio-group>
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :disabled="dateRangeType !== 'custom'"
          @change="handleDateRangeChange"
        ></el-date-picker>
      </div>
    </el-card>
  </template>
  
  <script>
  import { ref, onMounted, watch } from 'vue';
  import * as echarts from 'echarts';
  import { ElCard, ElTabs, ElTabPane, ElStatistic, ElDatePicker, ElRadioGroup, ElRadioButton, ElRow, ElCol } from 'element-plus';
  
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
      ElCol
    },
    setup() {
      const chartContainer = ref(null);
      const chart = ref(null);
      const activeTab = ref('api');
      const totalCases = ref(4320);
      const totalCasesChange = ref(12);
      const automatedCases = ref(342);
      const automatedCasesChange = ref(-10);
      const dateRangeType = ref('全年');
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
            trigger: 'axis',
            formatter: function(params) {
              return `${params[0].axisValue}<br/>${params[0].marker}${params[0].seriesName}: ${params[0].value}`;
            }
          },
          xAxis: {
            type: 'category',
            data: ['10-23周日', '10-24周一', '10-25周二', '10-26周三', '10-27周四', '10-28周五', '10-29周六'],
            axisLine: { show: false },
            axisTick: { show: false },
          },
          yAxis: {
            type: 'value',
            axisLine: { show: false },
            axisTick: { show: false },
            splitLine: { lineStyle: { type: 'dashed' } },
          },
          series: [{
            name: activeTab.value === 'api' ? '接口自动化占比' : 'UI自动化占比',
            type: 'line',
            smooth: true,
            data: [35, 28, 45, 57, 33, 15, 26],
            areaStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: 'rgba(58,77,233,0.8)' },
                { offset: 1, color: 'rgba(58,77,233,0.1)' }
              ])
            },
            itemStyle: { color: '#3a4de9' },
            lineStyle: { color: '#3a4de9' },
          }]
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
        handleDateRangeChange
      };
    }
  }
  </script>
  
  <style scoped>
  .statistics-card {
    margin-bottom: 20px;
  }
  .statistics-summary {
    margin: 20px 0;
  }
  .chart-container {
    height: 300px;
    width: 100%;
    margin: 20px 0;
  }
  .date-range {
    text-align: right;
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