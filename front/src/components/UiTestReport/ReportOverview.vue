<template>
  <div class="report-overview">
    <el-card class="overview-card">
      <template #header>
        <div class="card-header">
          <span>执行概览</span>
        </div>
      </template>
      
      <el-row :gutter="20">
        <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
          <el-descriptions title="基础信息" :column="1" border>
            <el-descriptions-item label="测试名称">{{ reportData.testName }}</el-descriptions-item>
            <el-descriptions-item label="执行ID">{{ reportData.executionId }}</el-descriptions-item>
            <el-descriptions-item label="环境">{{ reportData.environment }}</el-descriptions-item>
            <el-descriptions-item label="浏览器">
              <div class="browser-info">
                <i :class="getBrowserIcon(reportData.browser)"></i>
                {{ reportData.browser }} {{ reportData.browserVersion }}
              </div>
            </el-descriptions-item>
            <el-descriptions-item label="分辨率">
              {{ reportData.resolution }}
            </el-descriptions-item>
            <el-descriptions-item label="执行人">{{ reportData.executor }}</el-descriptions-item>
            <el-descriptions-item label="执行类型">{{ reportData.executionType }}</el-descriptions-item>
            <el-descriptions-item label="开始时间">{{ reportData.startTime }}</el-descriptions-item>
            <el-descriptions-item label="结束时间">{{ reportData.endTime }}</el-descriptions-item>
            <el-descriptions-item label="总耗时">{{ reportData.duration }} 秒</el-descriptions-item>
          </el-descriptions>
        </el-col>
        
        <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
          <div class="statistics-container">
            <div class="statistics-header">执行结果统计</div>
            <div class="statistics-charts">
              <div class="pie-chart-container">
                <div id="result-pie-chart" class="chart"></div>
              </div>
              <div class="statistics-details">
                <div class="stat-item">
                  <span class="stat-label">总步骤数:</span>
                  <span class="stat-value">{{ getTotalCount() }}</span>
                </div>
                <div class="stat-item passed">
                  <span class="stat-label">通过:</span>
                  <span class="stat-value">{{ reportData.statistics?.passed || 0 }}</span>
                </div>
                <div class="stat-item failed">
                  <span class="stat-label">失败:</span>
                  <span class="stat-value">{{ reportData.statistics?.failed || 0 }}</span>
                </div>
                <div class="stat-item skipped">
                  <span class="stat-label">跳过:</span>
                  <span class="stat-value">{{ reportData.statistics?.skipped || 0 }}</span>
                </div>
              </div>
            </div>
            
            <div class="duration-chart-container">
              <div class="duration-header">步骤执行耗时分布</div>
              <div id="duration-chart" class="chart"></div>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-card>
    
    <el-card class="failed-steps-card" v-if="reportData.failedSteps && reportData.failedSteps.length > 0">
      <template #header>
        <div class="card-header">
          <span>失败步骤摘要</span>
        </div>
      </template>
      
      <el-table
        :data="reportData.failedSteps"
        :border="true"
        style="width: 100%"
      >
        <el-table-column prop="stepNumber" label="步骤" width="70" />
        <el-table-column prop="description" label="步骤描述" min-width="200" />
        <el-table-column prop="errorMessage" label="错误信息" min-width="300" />
        <el-table-column fixed="right" label="操作" width="120">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="goToStep(row.stepNumber)">
              定位步骤
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { defineProps, onMounted, ref, defineEmits } from 'vue';
import * as echarts from 'echarts/core';
import { PieChart, BarChart } from 'echarts/charts';
import { TitleComponent, TooltipComponent, LegendComponent, GridComponent } from 'echarts/components';
import { CanvasRenderer } from 'echarts/renderers';

echarts.use([
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent,
  PieChart,
  BarChart,
  CanvasRenderer
]);

const props = defineProps({
  reportData: {
    type: Object,
    required: true
  }
});

const emit = defineEmits(['locate-step']);

// 计算总步骤数
const getTotalCount = () => {
  const stats = props.reportData.statistics || {};
  return (stats.passed || 0) + (stats.failed || 0) + (stats.skipped || 0);
};

// 获取浏览器图标
const getBrowserIcon = (browser) => {
  const browserIcons = {
    'chrome': 'fas fa-chrome',
    'firefox': 'fas fa-firefox',
    'edge': 'fas fa-edge',
    'safari': 'fas fa-safari',
    'ie': 'fas fa-internet-explorer'
  };
  
  return browserIcons[browser?.toLowerCase()] || 'fas fa-globe';
};

// 定位到步骤
const goToStep = (stepNumber) => {
  emit('locate-step', stepNumber);
};

onMounted(() => {
  // 初始化结果饼图
  const resultPieChart = echarts.init(document.getElementById('result-pie-chart'));
  const pieOption = {
    tooltip: {
      trigger: 'item',
      formatter: '{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 10,
      data: ['通过', '失败', '跳过']
    },
    series: [
      {
        name: '执行结果',
        type: 'pie',
        radius: '70%',
        center: ['50%', '50%'],
        data: [
          { value: props.reportData.statistics?.passed || 0, name: '通过', itemStyle: { color: '#67C23A' } },
          { value: props.reportData.statistics?.failed || 0, name: '失败', itemStyle: { color: '#F56C6C' } },
          { value: props.reportData.statistics?.skipped || 0, name: '跳过', itemStyle: { color: '#909399' } }
        ],
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  };
  resultPieChart.setOption(pieOption);

  // 初始化步骤耗时条形图
  const durationChart = echarts.init(document.getElementById('duration-chart'));
  
  // 准备耗时数据，这里使用模拟数据
  // 实际项目中应该从reportData中获取
  const stepsData = props.reportData.steps || [];
  const stepNumbers = stepsData.map(step => `步骤${step.stepNumber}`);
  const durations = stepsData.map(step => step.duration);
  const barColorMap = stepsData.map(step => {
    if (step.status === 'failed') return '#F56C6C';
    if (step.status === 'passed') return '#67C23A';
    return '#909399';
  });

  const barOption = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      },
      formatter: '{b}: {c}ms'
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: stepNumbers,
      axisLabel: {
        interval: 0,
        rotate: 45
      }
    },
    yAxis: {
      type: 'value',
      name: '耗时(ms)'
    },
    series: [
      {
        name: '执行耗时',
        type: 'bar',
        data: durations,
        itemStyle: {
          color: function(params) {
            return barColorMap[params.dataIndex];
          }
        }
      }
    ]
  };
  durationChart.setOption(barOption);

  // 窗口大小变化时重绘图表
  window.addEventListener('resize', () => {
    resultPieChart.resize();
    durationChart.resize();
  });
});
</script>

<style scoped>
.overview-card, .failed-steps-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.browser-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.browser-info i {
  font-size: 18px;
}

.statistics-container {
  height: 100%;
  display: flex;
  flex-direction: column;
  border: 1px solid #EBEEF5;
  border-radius: 4px;
}

.statistics-header, .duration-header {
  padding: 12px 20px;
  font-weight: bold;
  background-color: #F2F6FC;
  border-bottom: 1px solid #EBEEF5;
  color: #303133;
}

.statistics-charts {
  display: flex;
  padding: 20px;
  flex: 1;
}

.pie-chart-container {
  flex: 1;
}

.chart {
  width: 100%;
  height: 200px;
}

.statistics-details {
  width: 140px;
  padding-left: 15px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 10px;
}

.stat-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.stat-label {
  font-weight: bold;
}

.stat-value {
  font-size: 16px;
}

.passed .stat-value {
  color: #67C23A;
}

.failed .stat-value {
  color: #F56C6C;
}

.skipped .stat-value {
  color: #909399;
}

.duration-chart-container {
  margin-top: auto;
}

@media (max-width: 768px) {
  .statistics-charts {
    flex-direction: column;
    align-items: center;
  }
  
  .statistics-details {
    width: 100%;
    padding-left: 0;
    margin-top: 20px;
    flex-direction: row;
    flex-wrap: wrap;
    justify-content: space-around;
  }
  
  .stat-item {
    width: 45%;
  }
}
</style> 