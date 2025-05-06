<template>
  <el-dialog
    v-model="dialogVisible"
    title="测试执行设置"
    width="700px"
    :close-on-click-modal="false"
    @close="resetForm"
  >
    <el-form :model="executionForm" label-width="120px" ref="executionFormRef" :rules="formRules">
      <el-form-item label="执行名称" prop="executionName">
        <el-input v-model="executionForm.executionName" placeholder="请输入执行名称" />
      </el-form-item>
      
      <el-form-item label="执行类型" prop="executionType">
        <el-radio-group v-model="executionForm.executionType">
          <el-radio label="case">测试用例</el-radio>
          <el-radio label="suite">测试套件</el-radio>
        </el-radio-group>
      </el-form-item>
      
      <el-form-item
        :label="executionForm.executionType === 'case' ? '选择用例' : '选择套件'"
        prop="targetId"
      >
        <el-select
          v-model="executionForm.targetId"
          filterable
          remote
          placeholder="请输入关键字搜索"
          :remote-method="remoteSearch"
          :loading="searchLoading"
          style="width: 100%"
        >
          <el-option
            v-for="item in options"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          >
            <div class="option-item">
              <span>{{ item.name }}</span>
              <span class="option-desc">{{ item.description }}</span>
            </div>
          </el-option>
        </el-select>
      </el-form-item>
      
      <el-form-item label="执行环境" prop="environment">
        <el-select v-model="executionForm.environment" placeholder="请选择执行环境" style="width: 100%">
          <el-option label="开发环境" value="DEV" />
          <el-option label="测试环境" value="TEST" />
          <el-option label="预发布环境" value="STAGE" />
          <el-option label="生产环境" value="PROD" />
        </el-select>
      </el-form-item>
      
      <el-form-item label="浏览器" prop="browser">
        <el-select v-model="executionForm.browser" placeholder="请选择浏览器" style="width: 100%">
          <el-option label="Chrome" value="CHROME" />
          <el-option label="Firefox" value="FIREFOX" />
          <el-option label="Edge" value="EDGE" />
          <el-option label="Safari" value="SAFARI" />
          <el-option label="IE" value="IE" />
        </el-select>
      </el-form-item>
      
      <el-form-item label="浏览器尺寸" prop="browserSize">
        <el-select v-model="executionForm.browserSize" placeholder="请选择浏览器尺寸" style="width: 100%">
          <el-option label="1920x1080 (桌面)" value="1920x1080" />
          <el-option label="1366x768 (笔记本)" value="1366x768" />
          <el-option label="768x1024 (平板-竖屏)" value="768x1024" />
          <el-option label="1024x768 (平板-横屏)" value="1024x768" />
          <el-option label="375x812 (iPhone X)" value="375x812" />
          <el-option label="414x896 (iPhone XR)" value="414x896" />
          <el-option label="自定义" value="custom" />
        </el-select>
        
        <div v-if="executionForm.browserSize === 'custom'" class="custom-size-inputs">
          <el-input v-model="customWidth" type="number" placeholder="宽度" style="width: 100px" />
          <span class="separator">x</span>
          <el-input v-model="customHeight" type="number" placeholder="高度" style="width: 100px" />
        </div>
      </el-form-item>
      
      <el-form-item label="超时设置" prop="timeout">
        <el-input-number
          v-model="executionForm.timeout"
          :min="5"
          :max="180"
          :step="5"
          controls-position="right"
        />
        <span class="unit-label">秒</span>
      </el-form-item>
      
      <el-form-item label="失败重试" prop="retryCount">
        <el-input-number
          v-model="executionForm.retryCount"
          :min="0"
          :max="3"
          :step="1"
          controls-position="right"
        />
        <span class="unit-label">次</span>
      </el-form-item>
      
      <el-divider>高级设置</el-divider>
      
      <el-form-item label="截图策略" prop="screenshotPolicy">
        <el-select v-model="executionForm.screenshotPolicy" placeholder="请选择截图策略" style="width: 100%">
          <el-option label="仅失败时" value="ONLY_FAILURE" />
          <el-option label="每个步骤" value="EACH_STEP" />
          <el-option label="不截图" value="NONE" />
        </el-select>
      </el-form-item>
      
      <el-form-item label="参数设置">
        <el-collapse>
          <el-collapse-item title="添加/覆盖参数" name="1">
            <div class="param-list">
              <div v-for="(param, index) in executionForm.params" :key="index" class="param-item">
                <el-input v-model="param.name" placeholder="参数名" style="width: 200px" />
                <el-input v-model="param.value" placeholder="参数值" style="width: 200px" />
                <el-button type="danger" icon="Delete" circle @click="removeParam(index)" />
              </div>
              <el-button type="primary" plain @click="addParam">添加参数</el-button>
            </div>
          </el-collapse-item>
        </el-collapse>
      </el-form-item>
    </el-form>
    
    <div class="dialog-footer" style="margin-top: 20px; text-align: right;">
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" @click="submitForm">立即执行</el-button>
      <el-dropdown trigger="click" @command="handleScheduled">
        <el-button type="primary">
          更多执行 <el-icon><arrow-down /></el-icon>
        </el-button>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="scheduled">定时执行</el-dropdown-item>
            <el-dropdown-item command="parallel">并行执行</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, defineProps, defineEmits, watch } from 'vue';
import { ElMessage } from 'element-plus';
import { ArrowDown } from '@element-plus/icons-vue';
import { UiTestExecutionApi } from '@/api/uiTestExecutionService';

const props = defineProps({
  visible: Boolean
});

const emit = defineEmits(['update:visible', 'execution-started']);

// 对话框可见性
const dialogVisible = ref(props.visible);

// 表单引用
const executionFormRef = ref(null);

// 搜索状态
const searchLoading = ref(false);
const options = ref([]);

// 自定义尺寸
const customWidth = ref(1024);
const customHeight = ref(768);

// 测试执行表单数据
const executionForm = reactive({
  executionName: '',
  executionType: 'case',
  targetId: '',
  environment: 'TEST',
  browser: 'CHROME',
  browserSize: '1920x1080',
  timeout: 30,
  retryCount: 0,
  screenshotPolicy: 'ONLY_FAILURE',
  params: []
});

// 表单验证规则
const formRules = {
  executionName: [
    { required: true, message: '请输入执行名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  targetId: [
    { required: true, message: '请选择测试对象', trigger: 'change' }
  ],
  environment: [
    { required: true, message: '请选择执行环境', trigger: 'change' }
  ],
  browser: [
    { required: true, message: '请选择浏览器', trigger: 'change' }
  ]
};

// 监听对话框可见性变化
watch(() => props.visible, (newVal) => {
  dialogVisible.value = newVal;
  if (newVal) {
    // 初始化数据
    executionForm.executionName = `UI测试执行_${new Date().toLocaleString('zh-CN', { hour12: false }).replace(/[/:]/g, '')}`;
  }
});

// 监听dialogVisible变化
watch(dialogVisible, (newVal) => {
  emit('update:visible', newVal);
});

// 重置表单
const resetForm = () => {
  executionFormRef.value?.resetFields();
  executionForm.executionName = '';
  executionForm.executionType = 'case';
  executionForm.targetId = '';
  executionForm.environment = 'TEST';
  executionForm.browser = 'CHROME';
  executionForm.browserSize = '1920x1080';
  executionForm.timeout = 30;
  executionForm.retryCount = 0;
  executionForm.screenshotPolicy = 'ONLY_FAILURE';
  executionForm.params = [];
  options.value = [];
};

// 远程搜索
const remoteSearch = async (query) => {
  if (query) {
    searchLoading.value = true;
    
    // 模拟搜索结果
    setTimeout(() => {
      options.value = generateMockOptions(query, executionForm.executionType);
      searchLoading.value = false;
    }, 500);
    
    // 实际实现时使用API调用
    /*
    try {
      const type = executionForm.executionType;
      const url = type === 'case' ? '/api/ui-test-cases/search' : '/api/ui-test-suites/search';
      
      const res = await fetch(url + `?keyword=${query}`);
      const data = await res.json();
      
      if (data.code === 200) {
        options.value = data.data;
      } else {
        ElMessage.error(data.message || '搜索失败');
      }
    } catch (error) {
      ElMessage.error('搜索失败');
      console.error('搜索失败:', error);
    } finally {
      searchLoading.value = false;
    }
    */
  } else {
    options.value = [];
  }
};

// 添加参数
const addParam = () => {
  executionForm.params.push({ name: '', value: '' });
};

// 移除参数
const removeParam = (index) => {
  executionForm.params.splice(index, 1);
};

// 提交表单
const submitForm = async () => {
  if (!executionFormRef.value) return;
  
  executionFormRef.value.validate(async (valid) => {
    if (valid) {
      // 处理自定义尺寸
      if (executionForm.browserSize === 'custom') {
        executionForm.browserSize = `${customWidth.value}x${customHeight.value}`;
      }
      
      // 构造请求数据
      const requestData = {
        ...executionForm,
        executionType: executionForm.executionType === 'case' ? 'CASE' : 'SUITE',
        // 只保留有效的参数
        params: executionForm.params.filter(p => p.name && p.value)
      };
      
      try {
        // 模拟API请求
        setTimeout(() => {
          ElMessage.success('执行任务已成功提交');
          emit('execution-started', { 
            id: 'E' + Math.floor(Math.random() * 10000),
            name: executionForm.executionName,
            type: executionForm.executionType === 'case' ? '用例' : '测试套件'
          });
          dialogVisible.value = false;
        }, 1000);
        
        // 实际实现时使用API调用
        /*
        const res = await UiTestExecutionApi.startExecution(requestData);
        if (res.code === 200) {
          ElMessage.success('执行任务已成功提交');
          emit('execution-started', res.data);
          dialogVisible.value = false;
        } else {
          ElMessage.error(res.message || '提交执行任务失败');
        }
        */
      } catch (error) {
        ElMessage.error('提交执行任务失败');
        console.error('提交执行任务失败:', error);
      }
    }
  });
};

// 处理其他执行方式
const handleScheduled = (command) => {
  if (command === 'scheduled') {
    ElMessage.info('定时执行功能尚未实现');
  } else if (command === 'parallel') {
    ElMessage.info('并行执行功能尚未实现');
  }
};

// 生成模拟选项数据
const generateMockOptions = (query, type) => {
  const prefix = type === 'case' ? '测试用例' : '测试套件';
  return Array.from({ length: 5 }, (_, i) => ({
    id: `${type.charAt(0).toUpperCase()}${1000 + i}`,
    name: `${prefix}_${query}_${i + 1}`,
    description: `这是一个包含"${query}"的${prefix}示例描述`
  }));
};
</script>

<style scoped>
.unit-label {
  margin-left: 10px;
  color: #606266;
}

.custom-size-inputs {
  display: flex;
  align-items: center;
  margin-top: 10px;
}

.separator {
  margin: 0 10px;
  color: #606266;
}

.param-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.param-item {
  display: flex;
  align-items: center;
  gap: 10px;
}

.option-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.option-desc {
  font-size: 12px;
  color: #909399;
  margin-left: 10px;
}
</style> 