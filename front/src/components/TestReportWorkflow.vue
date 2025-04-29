<template>
  <div class="report-workflow">
    <el-steps :active="currentStep" finish-status="success" simple style="margin-bottom: 20px">
      <el-step title="选择测试计划" />
      <el-step title="关联测试用例" />
      <el-step title="选择缺陷" />
      <el-step title="填写报告信息" />
      <el-step title="报告预览" />
    </el-steps>
    
    <div class="step-content">
      <!-- 步骤1: 选择测试计划 -->
      <div v-if="currentStep === 0" class="step-container">
        <plan-selection 
          :selected-plan="formData.planId" 
          @plan-selected="handlePlanSelected"
        />
        <div class="step-actions">
          <el-button type="primary" @click="nextStep" :disabled="!formData.planId">下一步</el-button>
        </div>
      </div>
      
      <!-- 步骤2: 关联测试用例 -->
      <div v-if="currentStep === 1" class="step-container">
        <case-selection 
          :plan-id="formData.planId"
          :selected-cases="formData.testCases"
          @cases-selected="handleCasesSelected"
        />
        <div class="step-actions">
          <el-button @click="prevStep">上一步</el-button>
          <el-button type="primary" @click="nextStep" :disabled="!formData.testCases.length">下一步</el-button>
        </div>
      </div>
      
      <!-- 步骤3: 选择缺陷 -->
      <div v-if="currentStep === 2" class="step-container">
        <bug-selection 
          :plan-id="formData.planId"
          :selected-bugs="formData.bugs"
          @bugs-selected="handleBugsSelected"
        />
        <div class="step-actions">
          <el-button @click="prevStep">上一步</el-button>
          <el-button type="primary" @click="nextStep">下一步</el-button>
        </div>
      </div>
      
      <!-- 步骤4: 填写报告信息 -->
      <div v-if="currentStep === 3" class="step-container">
        <report-form 
          :form-data="formData"
          @form-updated="handleFormUpdated"
        />
        <div class="step-actions">
          <el-button @click="prevStep">上一步</el-button>
          <el-button type="primary" @click="nextStep" :disabled="!isFormValid">下一步</el-button>
        </div>
      </div>
      
      <!-- 步骤5: 报告预览 -->
      <div v-if="currentStep === 4" class="step-container">
        <report-preview :form-data="formData" />
        <div class="step-actions">
          <el-button @click="prevStep">上一步</el-button>
          <el-button type="primary" @click="handleGenerate">生成报告</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue';
import { ElMessage } from 'element-plus';
import { useRouter } from 'vue-router';
import { TestReportApi } from '@/api/testReportService';

// 导入子组件
import PlanSelection from './ReportWorkflow/PlanSelection.vue';
import CaseSelection from './ReportWorkflow/CaseSelection.vue';
import BugSelection from './ReportWorkflow/BugSelection.vue';
import ReportForm from './ReportWorkflow/ReportForm.vue';
import ReportPreview from './ReportWorkflow/ReportPreview.vue';

const router = useRouter();
const currentStep = ref(0);

// 报告数据
const formData = reactive({
  reportTitle: '',
  planId: '',
  testVersion: '',
  createdBy: localStorage.getItem('userName') || '',
  createdAt: new Date(),
  description: '',
  environment: 'testing',
  testScope: '',
  conclusion: '',
  summary: '',
  recommendations: '',
  testCases: [],
  bugs: [],
  statistics: {
    totalCases: 0,
    passedCases: 0,
    failedCases: 0,
    blockedCases: 0
  }
});

// 表单验证
const isFormValid = computed(() => {
  return formData.reportTitle && formData.testVersion;
});

// 步骤控制
const nextStep = () => {
  if (currentStep.value < 4) {
    currentStep.value += 1;
  }
};

const prevStep = () => {
  if (currentStep.value > 0) {
    currentStep.value -= 1;
  }
};

// 处理选择事件
const handlePlanSelected = (planId) => {
  formData.planId = planId;
};

const handleCasesSelected = (cases) => {
  formData.testCases = cases;
  // 更新统计信息
  formData.statistics.totalCases = cases.length;
  formData.statistics.passedCases = cases.filter(c => c.status === 'passed').length;
  formData.statistics.failedCases = cases.filter(c => c.status === 'failed').length;
  formData.statistics.blockedCases = cases.filter(c => c.status === 'blocked').length;
};

const handleBugsSelected = (bugs) => {
  formData.bugs = bugs;
};

const handleFormUpdated = (updatedForm) => {
  Object.assign(formData, updatedForm);
};

// 生成报告
const handleGenerate = async () => {
  try {
    await TestReportApi.createTestReport(formData);
    ElMessage.success('报告生成成功');
    router.push('/testreport');
  } catch (error) {
    ElMessage.error(error.message || '报告生成失败');
  }
};
</script>

<style scoped>
.report-workflow {
  padding: 20px;
}

.step-container {
  margin-top: 20px;
  min-height: 400px;
  display: flex;
  flex-direction: column;
}

.step-actions {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.step-content {
  background-color: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}
</style> 