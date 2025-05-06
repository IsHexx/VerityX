<template>
  <div class="failure-analysis">
    <el-card class="analysis-card">
      <template #header>
        <div class="card-header">
          <span>失败分析</span>
        </div>
      </template>
      
      <div class="no-failures" v-if="!hasFailures">
        <el-empty description="本次测试没有失败的步骤" />
      </div>
      
      <div v-else>
        <el-row :gutter="20">
          <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
            <div class="analysis-chart-container">
              <div id="failure-type-chart" class="chart"></div>
            </div>
          </el-col>
          <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
            <div class="analysis-summary">
              <h3>失败概述</h3>
              <ul class="failure-summary-list">
                <li>
                  <span class="summary-label">失败步骤数:</span>
                  <span class="summary-value">{{ failureData.totalFailures }}</span>
                </li>
                <li>
                  <span class="summary-label">失败率:</span>
                  <span class="summary-value">{{ failureData.failureRate.toFixed(2) }}%</span>
                </li>
                <li>
                  <span class="summary-label">最常见错误:</span>
                  <span class="summary-value">{{ failureData.mostCommonError }}</span>
                </li>
                <li v-if="failureData.criticalFailures > 0">
                  <span class="summary-label">关键错误数:</span>
                  <span class="summary-value critical">{{ failureData.criticalFailures }}</span>
                </li>
              </ul>
            </div>
          </el-col>
        </el-row>
        
        <el-divider content-position="center">错误类型分析</el-divider>
        
        <el-table
          :data="failureData.errorTypes"
          :border="true"
          style="width: 100%"
        >
          <el-table-column prop="type" label="错误类型" min-width="180">
            <template #default="{ row }">
              <el-tag :type="getErrorTagType(row.severity)" effect="dark">
                {{ row.type }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="count" label="出现次数" width="100" />
          <el-table-column prop="percentage" label="占比" width="100">
            <template #default="{ row }">
              {{ row.percentage.toFixed(2) }}%
            </template>
          </el-table-column>
          <el-table-column prop="severity" label="严重程度" width="120">
            <template #default="{ row }">
              <el-tag :type="getSeverityTagType(row.severity)">
                {{ getSeverityText(row.severity) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="description" label="描述" min-width="250" />
          <el-table-column prop="suggestion" label="解决建议" min-width="250" />
        </el-table>
        
        <el-divider content-position="center">失败步骤定位</el-divider>
        
        <el-collapse accordion>
          <el-collapse-item 
            v-for="(failure, index) in failureData.failures" 
            :key="index"
            :title="`步骤 ${failure.stepNumber}: ${failure.description}`"
            :name="index"
          >
            <div class="failure-detail">
              <div class="error-message">
                <div class="detail-label">错误信息:</div>
                <div class="detail-value error">{{ failure.errorMessage }}</div>
              </div>
              
              <div class="location" v-if="failure.location">
                <div class="detail-label">定位信息:</div>
                <div class="detail-value">{{ failure.location }}</div>
              </div>
              
              <div class="suggestion">
                <div class="detail-label">解决建议:</div>
                <div class="detail-value">{{ failure.suggestion }}</div>
              </div>
              
              <div class="actions">
                <el-button type="primary" size="small" @click="goToStep(failure.stepNumber)">
                  查看详情
                </el-button>
                <el-button type="success" size="small" @click="showScreenshot(failure.screenshot)" v-if="failure.screenshot">
                  查看截图
                </el-button>
              </div>
            </div>
          </el-collapse-item>
        </el-collapse>
      </div>
    </el-card>
    
    <!-- 截图预览对话框 -->
    <el-dialog
      v-model="screenshotDialogVisible"
      title="失败时截图"
      width="70%"
    >
      <div class="screenshot-dialog-content">
        <el-image
          style="width: 100%"
          :src="currentScreenshot"
          fit="contain"
          :preview-src-list="[currentScreenshot]"
        />
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { defineProps, ref, computed, onMounted, defineEmits } from 'vue';
import * as echarts from 'echarts/core';
import { PieChart } from 'echarts/charts';
import { TitleComponent, TooltipComponent, LegendComponent } from 'echarts/components';
import { CanvasRenderer } from 'echarts/renderers';

echarts.use([
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  PieChart,
  CanvasRenderer
]);

const props = defineProps({
  failureData: {
    type: Object,
    required: true,
    default: () => ({
      totalFailures: 0,
      failureRate: 0,
      mostCommonError: '',
      criticalFailures: 0,
      errorTypes: [],
      failures: []
    })
  }
});

const emit = defineEmits(['locate-step']);

// 状态管理
const screenshotDialogVisible = ref(false);
const currentScreenshot = ref('');

// 判断是否有失败步骤
const hasFailures = computed(() => {
  return props.failureData.totalFailures > 0;
});

// 根据错误严重程度返回对应的标签类型
const getSeverityTagType = (severity) => {
  const severityMap = {
    'critical': 'danger',
    'high': 'warning',
    'medium': 'info',
    'low': 'success'
  };
  return severityMap[severity] || 'info';
};

// 根据错误类型返回对应的标签类型
const getErrorTagType = (severity) => {
  const severityMap = {
    'critical': 'danger',
    'high': 'warning',
    'medium': 'primary',
    'low': 'info'
  };
  return severityMap[severity] || 'info';
};

// 严重程度文本转换
const getSeverityText = (severity) => {
  const severityTextMap = {
    'critical': '严重',
    'high': '高',
    'medium': '中',
    'low': '低'
  };
  return severityTextMap[severity] || severity;
};

// 跳转到特定步骤
const goToStep = (stepNumber) => {
  emit('locate-step', stepNumber);
};

// 显示截图
const showScreenshot = (screenshot) => {
  currentScreenshot.value = screenshot;
  screenshotDialogVisible.value = true;
};

onMounted(() => {
  if (props.failureData.errorTypes.length > 0) {
    // 初始化错误类型饼图
    const failureTypeChart = echarts.init(document.getElementById('failure-type-chart'));
    
    // 准备图表数据
    const chartData = props.failureData.errorTypes.map(item => ({
      name: item.type,
      value: item.count
    }));
    
    // 配置颜色映射
    const colorMap = {
      '元素未找到': '#F56C6C',
      '断言失败': '#E6A23C',
      '超时': '#409EFF',
      '脚本错误': '#909399',
      '网络错误': '#67C23A'
    };
    
    // 为数据项设置颜色
    const dataWithColors = chartData.map(item => ({
      ...item,
      itemStyle: {
        color: colorMap[item.name] || undefined
      }
    }));
    
    const option = {
      title: {
        text: '错误类型分布',
        left: 'center'
      },
      tooltip: {
        trigger: 'item',
        formatter: '{b}: {c} ({d}%)'
      },
      legend: {
        orient: 'vertical',
        left: 'left',
        data: chartData.map(item => item.name)
      },
      series: [
        {
          name: '错误类型',
          type: 'pie',
          radius: '70%',
          center: ['50%', '60%'],
          data: dataWithColors,
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          },
          label: {
            show: true,
            formatter: '{b}: {c} ({d}%)'
          }
        }
      ]
    };
    
    failureTypeChart.setOption(option);
    
    // 窗口大小变化时重绘图表
    window.addEventListener('resize', () => {
      failureTypeChart.resize();
    });
  }
});
</script>

<style scoped>
.analysis-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.no-failures {
  height: 200px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.analysis-chart-container {
  height: 300px;
}

.chart {
  width: 100%;
  height: 100%;
}

.analysis-summary {
  height: 100%;
  padding: 20px;
  border: 1px solid #EBEEF5;
  border-radius: 4px;
  background-color: #F8F9FA;
}

.analysis-summary h3 {
  margin-top: 0;
  margin-bottom: 20px;
  color: #303133;
  border-bottom: 1px solid #EBEEF5;
  padding-bottom: 10px;
}

.failure-summary-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.failure-summary-list li {
  margin-bottom: 15px;
  display: flex;
  align-items: center;
}

.summary-label {
  font-weight: bold;
  margin-right: 10px;
  min-width: 100px;
}

.summary-value {
  font-size: 16px;
}

.summary-value.critical {
  color: #F56C6C;
  font-weight: bold;
}

.failure-detail {
  padding: 15px;
  background-color: #F8F9FA;
  border-radius: 4px;
}

.error-message, .location, .suggestion {
  margin-bottom: 15px;
}

.detail-label {
  font-weight: bold;
  margin-bottom: 5px;
}

.detail-value {
  padding: 10px;
  background-color: white;
  border: 1px solid #EBEEF5;
  border-radius: 4px;
  word-break: break-word;
}

.detail-value.error {
  color: #F56C6C;
  background-color: rgba(245, 108, 108, 0.1);
}

.actions {
  margin-top: 20px;
  display: flex;
  gap: 10px;
}

.screenshot-dialog-content {
  display: flex;
  justify-content: center;
}
</style> 