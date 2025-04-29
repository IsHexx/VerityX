<template>
  <div class="report-preview">
    <h3>报告预览</h3>
    <p class="description">请确认报告信息正确，然后点击"生成报告"按钮</p>
    
    <div class="preview-container">
      <div class="report-header">
        <h1 class="report-title">{{ formData.reportTitle }}</h1>
        <div class="report-meta">
          <div class="meta-item">
            <span class="label">版本:</span>
            <span class="value">{{ formData.testVersion }}</span>
          </div>
          <div class="meta-item">
            <span class="label">创建人:</span>
            <span class="value">{{ formData.createdBy }}</span>
          </div>
          <div class="meta-item">
            <span class="label">创建日期:</span>
            <span class="value">{{ formatDate(formData.createdAt) }}</span>
          </div>
          <div class="meta-item">
            <span class="label">测试环境:</span>
            <span class="value">{{ getEnvironmentName(formData.environment) }}</span>
          </div>
        </div>
      </div>
      
      <el-divider />
      
      <section class="report-section">
        <h2 class="section-title">1. 测试概述</h2>
        <div class="section-content">
          <h3 class="subsection-title">1.1 测试范围</h3>
          <div class="text-content">{{ formData.testScope || '无' }}</div>
          
          <h3 class="subsection-title">1.2 测试描述</h3>
          <div class="text-content">{{ formData.description || '无' }}</div>
        </div>
      </section>
      
      <section class="report-section">
        <h2 class="section-title">2. 测试结果摘要</h2>
        <div class="section-content">
          <div class="stats-section">
            <div class="stat-item">
              <div class="stat-value">{{ formData.statistics.totalCases }}</div>
              <div class="stat-label">总用例数</div>
            </div>
            <div class="stat-item success">
              <div class="stat-value">{{ formData.statistics.passedCases }}</div>
              <div class="stat-label">通过</div>
            </div>
            <div class="stat-item danger">
              <div class="stat-value">{{ formData.statistics.failedCases }}</div>
              <div class="stat-label">失败</div>
            </div>
            <div class="stat-item warning">
              <div class="stat-value">{{ formData.statistics.blockedCases }}</div>
              <div class="stat-label">阻塞</div>
            </div>
          </div>
          
          <div class="pass-rate-container">
            <div class="pass-rate-label">通过率:</div>
            <el-progress 
              :percentage="passRate" 
              :color="getPassRateColor(passRate)"
            ></el-progress>
          </div>
          
          <div class="chart-container" ref="chartContainer">
            <div ref="pieChart" style="width: 100%; height: 300px;"></div>
          </div>
        </div>
        
        <h3 class="subsection-title">2.1 测试结论</h3>
        <div class="conclusion-section">
          <el-tag
            :type="getConclusionType(formData.conclusion)"
            effect="dark"
            size="large"
            class="conclusion-tag"
          >
            {{ getConclusionText(formData.conclusion) }}
          </el-tag>
          <div class="text-content">{{ formData.summary || '无' }}</div>
        </div>
      </section>
      
      <section class="report-section">
        <h2 class="section-title">3. 测试用例执行情况</h2>
        <div class="section-content">
          <el-table
            :data="formData.testCases"
            border
            style="width: 100%"
            max-height="300"
          >
            <el-table-column prop="caseId" label="用例ID" width="100" />
            <el-table-column prop="caseName" label="用例名称" min-width="200" show-overflow-tooltip />
            <el-table-column prop="module" label="所属模块" width="120" />
            <el-table-column prop="priority" label="优先级" width="80" />
            <el-table-column prop="status" label="执行状态" width="100">
              <template #default="{ row }">
                <el-tag :type="getStatusType(row.status)" size="small">
                  {{ getStatusText(row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="executor" label="执行人" width="100" />
          </el-table>
        </div>
      </section>
      
      <section class="report-section" v-if="formData.bugs && formData.bugs.length > 0">
        <h2 class="section-title">4. 遗留缺陷</h2>
        <div class="section-content">
          <el-table
            :data="openBugs"
            border
            style="width: 100%"
            max-height="300"
          >
            <el-table-column prop="bugId" label="缺陷ID" width="100" />
            <el-table-column prop="title" label="缺陷标题" min-width="200" show-overflow-tooltip />
            <el-table-column prop="severity" label="严重程度" width="100">
              <template #default="{ row }">
                <el-tag
                  :type="getSeverityType(row.severity)"
                  effect="dark"
                  size="small"
                >
                  {{ getSeverityText(row.severity) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="priority" label="优先级" width="100" />
            <el-table-column prop="status" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="getBugStatusType(row.status)" size="small">
                  {{ getBugStatusText(row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="assignee" label="处理人" width="100" />
          </el-table>
        </div>
      </section>
      
      <section class="report-section">
        <h2 class="section-title">5. 问题与建议</h2>
        <div class="section-content">
          <div class="text-content">{{ formData.recommendations || '无' }}</div>
        </div>
      </section>
    </div>
    
    <div class="print-actions">
      <el-button type="primary" @click="handlePrint">
        <el-icon><Printer /></el-icon>
        打印报告
      </el-button>
      <el-button type="success" @click="handleExport">
        <el-icon><Download /></el-icon>
        导出PDF
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import { ElMessage } from 'element-plus';
import { Printer, Download } from '@element-plus/icons-vue';
import * as echarts from 'echarts';

const props = defineProps({
  formData: {
    type: Object,
    required: true
  }
});

// 图表引用
const pieChart = ref(null);
let chart = null;

// 通过率计算
const passRate = computed(() => {
  const { totalCases, passedCases } = props.formData.statistics || { totalCases: 0, passedCases: 0 };
  if (totalCases === 0) return 0;
  return Math.round((passedCases / totalCases) * 100);
});

// 遗留的缺陷
const openBugs = computed(() => {
  if (!props.formData.bugs) return [];
  return props.formData.bugs.filter(bug => 
    ['open', 'reopened'].includes(bug.status)
  );
});

// 日期格式化
const formatDate = (date) => {
  if (!date) return '';
  if (typeof date === 'string') return date;
  
  const d = new Date(date);
  const year = d.getFullYear();
  const month = String(d.getMonth() + 1).padStart(2, '0');
  const day = String(d.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
};

// 获取环境名称
const getEnvironmentName = (env) => {
  const envMap = {
    'development': '开发环境',
    'testing': '测试环境',
    'staging': '预发布环境',
    'production': '生产环境',
    'other': '其他'
  };
  return envMap[env] || env;
};

// 获取测试结论类型
const getConclusionType = (conclusion) => {
  const conclusionMap = {
    'pass': 'success',
    'conditional_pass': 'warning',
    'fail': 'danger'
  };
  return conclusionMap[conclusion] || 'info';
};

// 获取测试结论文本
const getConclusionText = (conclusion) => {
  const conclusionTextMap = {
    'pass': '通过，可以发布',
    'conditional_pass': '有条件通过，修复后可以发布',
    'fail': '不通过，需要修复重测'
  };
  return conclusionTextMap[conclusion] || '未设置';
};

// 获取严重程度类型
const getSeverityType = (severity) => {
  const severityMap = {
    'critical': 'danger',
    'major': 'warning',
    'minor': 'info',
    'trivial': 'success'
  };
  return severityMap[severity] || 'info';
};

// 获取严重程度文本
const getSeverityText = (severity) => {
  const severityTextMap = {
    'critical': '致命',
    'major': '严重',
    'minor': '一般',
    'trivial': '轻微'
  };
  return severityTextMap[severity] || severity;
};

// 获取用例状态类型
const getStatusType = (status) => {
  const statusMap = {
    'passed': 'success',
    'failed': 'danger',
    'blocked': 'warning',
    'skipped': 'info'
  };
  return statusMap[status] || 'info';
};

// 获取用例状态文本
const getStatusText = (status) => {
  const statusTextMap = {
    'passed': '通过',
    'failed': '失败',
    'blocked': '阻塞',
    'skipped': '跳过'
  };
  return statusTextMap[status] || status;
};

// 获取缺陷状态类型
const getBugStatusType = (status) => {
  const statusMap = {
    'open': 'danger',
    'reopened': 'warning',
    'fixed': 'success',
    'verified': 'success',
    'closed': 'info'
  };
  return statusMap[status] || 'info';
};

// 获取缺陷状态文本
const getBugStatusText = (status) => {
  const statusTextMap = {
    'open': '待修复',
    'reopened': '重新打开',
    'fixed': '已修复',
    'verified': '已验证',
    'closed': '已关闭'
  };
  return statusTextMap[status] || status;
};

// 获取通过率颜色
const getPassRateColor = (percentage) => {
  if (percentage < 60) {
    return '#F56C6C'; // 红色
  } else if (percentage < 85) {
    return '#E6A23C'; // 黄色
  } else {
    return '#67C23A'; // 绿色
  }
};

// 初始化图表
const initChart = () => {
  if (!pieChart.value) return;
  
  // 创建图表实例
  chart = echarts.init(pieChart.value);
  
  // 设置图表选项
  const option = {
    title: {
      text: '测试结果统计',
      left: 'center'
    },
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left',
      data: ['通过', '失败', '阻塞', '跳过']
    },
    series: [
      {
        name: '测试结果',
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        label: {
          show: true,
          formatter: '{b}: {c} ({d}%)'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: '18',
            fontWeight: 'bold'
          }
        },
        data: [
          { 
            value: props.formData.statistics.passedCases, 
            name: '通过',
            itemStyle: { color: '#67C23A' }
          },
          { 
            value: props.formData.statistics.failedCases, 
            name: '失败',
            itemStyle: { color: '#F56C6C' }
          },
          { 
            value: props.formData.statistics.blockedCases, 
            name: '阻塞',
            itemStyle: { color: '#E6A23C' }
          },
          { 
            value: 0, 
            name: '跳过',
            itemStyle: { color: '#909399' }
          }
        ]
      }
    ]
  };
  
  // 应用选项
  chart.setOption(option);
};

// 更新图表数据
const updateChart = () => {
  if (!chart) return;
  
  const { totalCases, passedCases, failedCases, blockedCases } = props.formData.statistics || { totalCases: 0, passedCases: 0, failedCases: 0, blockedCases: 0 };
  const skippedCases = totalCases - passedCases - failedCases - blockedCases;
  
  const option = {
    series: [
      {
        data: [
          { value: passedCases, name: '通过' },
          { value: failedCases, name: '失败' },
          { value: blockedCases, name: '阻塞' },
          { value: skippedCases, name: '跳过' }
        ]
      }
    ]
  };
  
  chart.setOption(option);
};

// 打印报告
const handlePrint = () => {
  window.print();
};

// 导出PDF (简化版，实际应用中需要使用PDF生成库)
const handleExport = () => {
  ElMessage.success('PDF导出功能将在后续版本中实现');
  // 实际实现可以使用html2canvas + jsPDF等库
};

// 监听窗口大小变化以调整图表大小
window.addEventListener('resize', () => {
  if (chart) {
    chart.resize();
  }
});

// 监听数据变化
watch(() => props.formData, () => {
  if (chart) {
    updateChart();
  }
}, { deep: true });

// 组件挂载时初始化图表
onMounted(() => {
  initChart();
});
</script>

<style scoped>
.report-preview {
  width: 100%;
}

h3 {
  margin-top: 0;
  margin-bottom: 10px;
}

.description {
  color: #666;
  margin-bottom: 20px;
}

.preview-container {
  background-color: #fff;
  padding: 30px;
  border: 1px solid #ebeef5;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}

.report-header {
  text-align: center;
  margin-bottom: 20px;
}

.report-title {
  font-size: 24px;
  margin-bottom: 15px;
}

.report-meta {
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  gap: 20px;
}

.meta-item {
  display: flex;
  align-items: center;
}

.meta-item .label {
  font-weight: bold;
  margin-right: 5px;
  color: #606266;
}

.report-section {
  margin-bottom: 30px;
}

.section-title {
  font-size: 18px;
  margin-bottom: 15px;
  color: #303133;
  border-left: 3px solid #409EFF;
  padding-left: 10px;
}

.subsection-title {
  font-size: 16px;
  margin: 15px 0 10px;
  color: #606266;
}

.section-content {
  padding: 0 15px;
}

.text-content {
  line-height: 1.6;
  color: #606266;
  white-space: pre-line;
  margin-bottom: 15px;
}

.stats-section {
  display: flex;
  justify-content: space-around;
  margin: 20px 0;
  flex-wrap: wrap;
  gap: 20px;
}

.stat-item {
  text-align: center;
  width: 100px;
  padding: 15px;
  border-radius: 8px;
  background-color: #f5f7fa;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}

.stat-item.success {
  background-color: rgba(103, 194, 58, 0.1);
}

.stat-item.danger {
  background-color: rgba(245, 108, 108, 0.1);
}

.stat-item.warning {
  background-color: rgba(230, 162, 60, 0.1);
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #606266;
}

.pass-rate-container {
  display: flex;
  align-items: center;
  margin: 20px 0;
}

.pass-rate-label {
  width: 70px;
  color: #606266;
  font-size: 14px;
}

.chart-container {
  height: 300px;
  margin: 20px 0;
}

.conclusion-section {
  margin: 10px 0 20px;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 8px;
}

.conclusion-tag {
  margin-bottom: 15px;
}

.print-actions {
  margin-top: 20px;
  display: flex;
  justify-content: center;
  gap: 20px;
}

@media print {
  .print-actions {
    display: none;
  }
  
  .preview-container {
    box-shadow: none;
    border: none;
  }
}
</style> 