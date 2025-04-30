<template>
  <div class="wait-time-config-container">
    <div class="header">
      <h3>全局等待时间配置</h3>
      <el-button type="primary" @click="handleSave">保存配置</el-button>
    </div>
    
    <el-form :model="formData" :rules="rules" ref="formRef" label-width="150px">
      <el-form-item label="页面加载超时(秒)" prop="pageLoadTimeout">
        <el-input-number 
          v-model="formData.pageLoadTimeout"
          :min="1"
          :max="300"
          controls-position="right"
        />
      </el-form-item>
      
      <el-form-item label="隐式等待时间(秒)" prop="implicitWait">
        <el-input-number 
          v-model="formData.implicitWait"
          :min="0"
          :max="60"
          controls-position="right"
        />
      </el-form-item>
      
      <el-form-item label="显式等待时间(秒)" prop="explicitWait">
        <el-input-number 
          v-model="formData.explicitWait"
          :min="1"
          :max="120"
          controls-position="right"
        />
      </el-form-item>
      
      <el-form-item label="脚本超时时间(秒)" prop="scriptTimeout">
        <el-input-number 
          v-model="formData.scriptTimeout"
          :min="1"
          :max="120"
          controls-position="right"
        />
      </el-form-item>
      
      <el-form-item label="Ajax请求超时(秒)" prop="ajaxTimeout">
        <el-input-number 
          v-model="formData.ajaxTimeout"
          :min="1"
          :max="120"
          controls-position="right"
        />
      </el-form-item>
      
      <el-form-item label="轮询间隔(毫秒)" prop="pollingInterval">
        <el-input-number 
          v-model="formData.pollingInterval"
          :min="100"
          :max="10000"
          :step="100"
          controls-position="right"
        />
      </el-form-item>
      
      <el-divider content-position="left">自定义等待</el-divider>
      
      <div class="custom-waits-header">
        <span>自定义等待配置</span>
        <el-button type="primary" size="small" @click="addCustomWait">添加自定义等待</el-button>
      </div>
      
      <el-table
        :data="formData.customWaits"
        border
        style="width: 100%"
        class="custom-waits-table"
      >
        <el-table-column prop="name" label="名称">
          <template #default="scope">
            <el-input v-model="scope.row.name" placeholder="等待名称" />
          </template>
        </el-table-column>
        
        <el-table-column prop="selector" label="元素选择器">
          <template #default="scope">
            <el-input v-model="scope.row.selector" placeholder="CSS选择器" />
          </template>
        </el-table-column>
        
        <el-table-column prop="timeout" label="超时时间(秒)" width="150">
          <template #default="scope">
            <el-input-number 
              v-model="scope.row.timeout"
              :min="1"
              :max="60"
              controls-position="right"
            />
          </template>
        </el-table-column>
        
        <el-table-column label="操作" width="120">
          <template #default="scope">
            <el-button 
              type="danger" 
              size="small" 
              circle
              @click="removeCustomWait(scope.$index)"
            >
              <el-icon><Delete /></el-icon>
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-form>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, defineProps, defineEmits, watch } from 'vue';
import { ElMessage } from 'element-plus';
import { Delete } from '@element-plus/icons-vue';

// 定义接收的属性
const props = defineProps({
  waitTimeConfig: {
    type: Object,
    required: true
  }
});

// 定义向父组件发送的事件
const emit = defineEmits(['update-wait-time']);

// 表单引用
const formRef = ref(null);

// 表单数据
const formData = reactive({
  pageLoadTimeout: 30,
  implicitWait: 10,
  explicitWait: 20,
  scriptTimeout: 15,
  ajaxTimeout: 25,
  pollingInterval: 500,
  customWaits: []
});

// 表单验证规则
const rules = {
  pageLoadTimeout: [
    { required: true, message: '请输入页面加载超时时间', trigger: 'blur' },
    { type: 'number', min: 1, max: 300, message: '超时时间必须在1-300秒之间', trigger: 'blur' }
  ],
  implicitWait: [
    { required: true, message: '请输入隐式等待时间', trigger: 'blur' },
    { type: 'number', min: 0, max: 60, message: '等待时间必须在0-60秒之间', trigger: 'blur' }
  ],
  explicitWait: [
    { required: true, message: '请输入显式等待时间', trigger: 'blur' },
    { type: 'number', min: 1, max: 120, message: '等待时间必须在1-120秒之间', trigger: 'blur' }
  ],
  scriptTimeout: [
    { required: true, message: '请输入脚本超时时间', trigger: 'blur' },
    { type: 'number', min: 1, max: 120, message: '超时时间必须在1-120秒之间', trigger: 'blur' }
  ],
  ajaxTimeout: [
    { required: true, message: '请输入Ajax请求超时时间', trigger: 'blur' },
    { type: 'number', min: 1, max: 120, message: '超时时间必须在1-120秒之间', trigger: 'blur' }
  ],
  pollingInterval: [
    { required: true, message: '请输入轮询间隔', trigger: 'blur' },
    { type: 'number', min: 100, max: 10000, message: '轮询间隔必须在100-10000毫秒之间', trigger: 'blur' }
  ]
};

// 监听属性变化
watch(() => props.waitTimeConfig, (newConfig) => {
  if (newConfig) {
    Object.assign(formData, newConfig);
    
    // 确保customWaits是数组
    if (!Array.isArray(formData.customWaits)) {
      formData.customWaits = [];
    }
  }
}, { deep: true, immediate: true });

// 添加自定义等待
const addCustomWait = () => {
  formData.customWaits.push({
    name: '',
    selector: '',
    timeout: 10
  });
};

// 移除自定义等待
const removeCustomWait = (index) => {
  formData.customWaits.splice(index, 1);
};

// 保存配置
const handleSave = async () => {
  if (!formRef.value) return;
  
  await formRef.value.validate((valid) => {
    if (valid) {
      emit('update-wait-time', { ...formData });
      ElMessage.success('等待时间配置已保存');
    } else {
      ElMessage.error('表单验证失败，请检查输入');
    }
  });
};

// 表单验证方法
const validate = () => {
  if (!formRef.value) return true;
  
  let isValid = false;
  formRef.value.validate((valid) => {
    isValid = valid;
  });
  return isValid;
};

// 对外暴露方法
defineExpose({
  validate
});

// 组件挂载时处理
onMounted(() => {
  if (props.waitTimeConfig) {
    Object.assign(formData, props.waitTimeConfig);
  }
});
</script>

<style scoped>
.wait-time-config-container {
  width: 100%;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header h3 {
  margin: 0;
}

.custom-waits-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.custom-waits-table {
  margin-bottom: 20px;
}
</style> 