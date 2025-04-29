<template>
  <div class="report-form">
    <h3>填写报告信息</h3>
    <p class="description">请填写测试报告的详细信息</p>
    
    <el-form 
      :model="localFormData" 
      label-position="top" 
      :rules="rules"
      ref="formRef"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="报告标题" prop="reportTitle">
            <el-input 
              v-model="localFormData.reportTitle" 
              placeholder="请输入报告标题"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="测试版本" prop="testVersion">
            <el-input 
              v-model="localFormData.testVersion" 
              placeholder="请输入测试版本"
              clearable
            />
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="创建人" prop="createdBy">
            <el-input 
              v-model="localFormData.createdBy" 
              placeholder="请输入创建人"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建日期" prop="createdAt">
            <el-date-picker
              v-model="localFormData.createdAt"
              type="date"
              placeholder="选择日期"
              style="width: 100%"
              value-format="YYYY-MM-DD"
            />
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-form-item label="测试环境" prop="environment">
        <el-select
          v-model="localFormData.environment"
          placeholder="请选择测试环境"
          style="width: 100%"
        >
          <el-option label="开发环境" value="development" />
          <el-option label="测试环境" value="testing" />
          <el-option label="预发布环境" value="staging" />
          <el-option label="生产环境" value="production" />
          <el-option label="其他" value="other" />
        </el-select>
      </el-form-item>
      
      <el-form-item label="测试范围" prop="testScope">
        <el-input 
          v-model="localFormData.testScope" 
          placeholder="请输入测试范围"
          clearable
        />
      </el-form-item>
      
      <el-form-item label="报告描述" prop="description">
        <el-input
          v-model="localFormData.description"
          type="textarea"
          :rows="5"
          placeholder="请输入报告描述，包括测试目的、测试重点等"
        />
      </el-form-item>
      
      <el-form-item label="测试结论" prop="conclusion">
        <el-select
          v-model="localFormData.conclusion"
          placeholder="请选择测试结论"
          style="width: 100%"
        >
          <el-option label="通过，可以发布" value="pass" />
          <el-option label="有条件通过，修复后可以发布" value="conditional_pass" />
          <el-option label="不通过，需要修复重测" value="fail" />
        </el-select>
      </el-form-item>
      
      <el-form-item label="测试总结" prop="summary">
        <el-input
          v-model="localFormData.summary"
          type="textarea"
          :rows="5"
          placeholder="请输入测试总结，包括测试结果分析、问题总结等"
        />
      </el-form-item>
      
      <el-form-item label="存在问题与建议" prop="recommendations">
        <el-input
          v-model="localFormData.recommendations"
          type="textarea"
          :rows="5"
          placeholder="请输入存在的问题和改进建议"
        />
      </el-form-item>
    </el-form>
    
    <div class="summary-section">
      <h4>测试统计信息</h4>
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-title">测试用例总数</div>
            <div class="stat-value">{{ localFormData.statistics.totalCases }}</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card success">
            <div class="stat-title">通过用例数</div>
            <div class="stat-value">{{ localFormData.statistics.passedCases }}</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card danger">
            <div class="stat-title">失败用例数</div>
            <div class="stat-value">{{ localFormData.statistics.failedCases }}</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card warning">
            <div class="stat-title">阻塞用例数</div>
            <div class="stat-value">{{ localFormData.statistics.blockedCases }}</div>
          </div>
        </el-col>
      </el-row>
      
      <div class="pass-rate-section">
        <div class="pass-rate-label">通过率:</div>
        <el-progress 
          :percentage="passRate" 
          :color="getPassRateColor(passRate)"
          :format="format"
        ></el-progress>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, watch, onMounted } from 'vue';
import { ElMessage } from 'element-plus';

const props = defineProps({
  formData: {
    type: Object,
    required: true
  }
});

const emit = defineEmits(['form-updated']);

// 本地表单数据
const localFormData = reactive({
  reportTitle: '',
  testVersion: '',
  createdBy: '',
  createdAt: '',
  description: '',
  environment: 'testing',
  testScope: '',
  conclusion: '',
  summary: '',
  recommendations: '',
  statistics: {
    totalCases: 0,
    passedCases: 0,
    failedCases: 0,
    blockedCases: 0
  }
});

// 表单引用
const formRef = ref(null);

// 表单验证规则
const rules = {
  reportTitle: [
    { required: true, message: '请输入报告标题', trigger: 'blur' },
    { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
  ],
  testVersion: [
    { required: true, message: '请输入测试版本', trigger: 'blur' }
  ],
  createdBy: [
    { required: true, message: '请输入创建人', trigger: 'blur' }
  ],
  createdAt: [
    { required: true, message: '请选择创建日期', trigger: 'change' }
  ],
  environment: [
    { required: true, message: '请选择测试环境', trigger: 'change' }
  ],
  conclusion: [
    { required: true, message: '请选择测试结论', trigger: 'change' }
  ]
};

// 通过率计算
const passRate = computed(() => {
  const { totalCases, passedCases } = localFormData.statistics;
  if (totalCases === 0) return 0;
  return Math.round((passedCases / totalCases) * 100);
});

// 格式化通过率
const format = (percentage) => {
  return `${percentage}%`;
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

// 初始化表单数据
const initFormData = () => {
  // 填充默认值
  if (props.formData) {
    Object.keys(localFormData).forEach(key => {
      if (props.formData[key] !== undefined) {
        if (key === 'statistics') {
          Object.assign(localFormData.statistics, props.formData.statistics);
        } else {
          localFormData[key] = props.formData[key];
        }
      }
    });
  }
  
  // 设置默认值
  if (!localFormData.createdBy) {
    localFormData.createdBy = localStorage.getItem('userName') || '';
  }
  
  if (!localFormData.createdAt) {
    localFormData.createdAt = new Date();
  }
};

// 表单变化处理
const handleFormChange = () => {
  emit('form-updated', {
    reportTitle: localFormData.reportTitle,
    testVersion: localFormData.testVersion,
    createdBy: localFormData.createdBy,
    createdAt: localFormData.createdAt,
    description: localFormData.description,
    environment: localFormData.environment,
    testScope: localFormData.testScope,
    conclusion: localFormData.conclusion,
    summary: localFormData.summary,
    recommendations: localFormData.recommendations
  });
};

// 监听表单变化
watch(() => ({
  reportTitle: localFormData.reportTitle,
  testVersion: localFormData.testVersion,
  createdBy: localFormData.createdBy,
  createdAt: localFormData.createdAt,
  description: localFormData.description,
  environment: localFormData.environment,
  testScope: localFormData.testScope,
  conclusion: localFormData.conclusion,
  summary: localFormData.summary,
  recommendations: localFormData.recommendations
}), handleFormChange, { deep: true });

// 监听外部表单数据变化
watch(() => props.formData, (newVal) => {
  if (newVal) {
    initFormData();
  }
}, { deep: true });

// 组件挂载时初始化
onMounted(() => {
  initFormData();
});
</script>

<style scoped>
.report-form {
  width: 100%;
}

h3 {
  margin-top: 0;
  margin-bottom: 10px;
}

h4 {
  margin-top: 20px;
  margin-bottom: 15px;
  font-size: 16px;
  color: #333;
}

.description {
  color: #666;
  margin-bottom: 20px;
}

.summary-section {
  margin-top: 30px;
  border-top: 1px solid #ebeef5;
  padding-top: 20px;
}

.stat-card {
  background-color: #f5f7fa;
  border-radius: 8px;
  padding: 15px;
  text-align: center;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  margin-bottom: 15px;
  transition: all 0.3s;
}

.stat-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 15px 0 rgba(0, 0, 0, 0.1);
}

.stat-card.success {
  background-color: rgba(103, 194, 58, 0.1);
  border-left: 4px solid #67c23a;
}

.stat-card.danger {
  background-color: rgba(245, 108, 108, 0.1);
  border-left: 4px solid #f56c6c;
}

.stat-card.warning {
  background-color: rgba(230, 162, 60, 0.1);
  border-left: 4px solid #e6a23c;
}

.stat-title {
  font-size: 14px;
  color: #606266;
  margin-bottom: 10px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

.pass-rate-section {
  margin-top: 20px;
  display: flex;
  align-items: center;
}

.pass-rate-label {
  width: 80px;
  color: #606266;
  font-size: 14px;
}

.pass-rate-section .el-progress {
  flex: 1;
}
</style> 