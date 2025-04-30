<template>
  <div class="browser-config-container">
    <div class="header">
      <h3>浏览器配置</h3>
      <el-button type="primary" @click="showAddBrowserDialog">添加浏览器</el-button>
    </div>
    
    <el-table
      v-loading="loading"
      :data="browserConfigs"
      border
      style="width: 100%"
    >
      <el-table-column prop="name" label="浏览器名称" width="150" />
      <el-table-column prop="version" label="版本" width="150" />
      <el-table-column prop="description" label="描述" />
      <el-table-column label="启用状态" width="120">
        <template #default="scope">
          <el-switch
            v-model="scope.row.isEnabled"
            @change="handleStatusChange(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column label="无头模式" width="120">
        <template #default="scope">
          <el-switch
            v-model="scope.row.isHeadless"
            @change="handleHeadlessChange(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template #default="scope">
          <el-button 
            type="primary" 
            size="small" 
            @click="handleEdit(scope.row)"
            :disabled="!scope.row.isEnabled"
          >
            编辑
          </el-button>
          <el-button 
            type="danger" 
            size="small" 
            @click="handleDelete(scope.row)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <!-- 添加/编辑浏览器对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑浏览器' : '添加浏览器'"
      width="500px"
    >
      <el-form :model="browserForm" :rules="rules" ref="browserFormRef" label-width="120px">
        <el-form-item label="浏览器名称" prop="name">
          <el-input v-model="browserForm.name" placeholder="请输入浏览器名称" />
        </el-form-item>
        <el-form-item label="版本" prop="version">
          <el-input v-model="browserForm.version" placeholder="请输入版本号" />
        </el-form-item>
        <el-form-item label="是否启用" prop="isEnabled">
          <el-switch v-model="browserForm.isEnabled" />
        </el-form-item>
        <el-form-item label="无头模式" prop="isHeadless">
          <el-switch v-model="browserForm.isHeadless" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input
            v-model="browserForm.description"
            type="textarea"
            placeholder="请输入浏览器描述"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确认</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, defineProps, defineEmits } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';

// 定义接收的属性
const props = defineProps({
  browserConfigs: {
    type: Array,
    required: true
  }
});

// 定义向父组件发送的事件
const emit = defineEmits(['add-browser', 'delete-browser', 'update-browser']);

// 状态管理
const loading = ref(false);
const dialogVisible = ref(false);
const isEdit = ref(false);
const currentId = ref(null);
const browserFormRef = ref(null);

// 表单数据
const browserForm = reactive({
  name: '',
  version: '',
  isEnabled: true,
  isHeadless: false,
  description: ''
});

// 表单验证规则
const rules = {
  name: [
    { required: true, message: '请输入浏览器名称', trigger: 'blur' },
    { min: 1, max: 50, message: '长度在 1 到 50 个字符', trigger: 'blur' }
  ],
  version: [
    { required: true, message: '请输入版本号', trigger: 'blur' }
  ]
};

// 表单验证方法
const validate = () => {
  if (!browserFormRef.value) return true;
  
  let isValid = false;
  browserFormRef.value.validate((valid) => {
    isValid = valid;
  });
  return isValid;
};

// 显示添加浏览器对话框
const showAddBrowserDialog = () => {
  isEdit.value = false;
  resetForm();
  dialogVisible.value = true;
};

// 重置表单
const resetForm = () => {
  if (browserFormRef.value) {
    browserFormRef.value.resetFields();
  }
  
  browserForm.name = '';
  browserForm.version = '';
  browserForm.isEnabled = true;
  browserForm.isHeadless = false;
  browserForm.description = '';
  currentId.value = null;
};

// 处理表单提交
const handleSubmit = async () => {
  if (!browserFormRef.value) return;
  
  await browserFormRef.value.validate(async (valid) => {
    if (valid) {
      const browser = { ...browserForm };
      
      if (isEdit.value && currentId.value !== null) {
        browser.id = currentId.value;
        emit('update-browser', browser);
      } else {
        emit('add-browser', browser);
      }
      
      dialogVisible.value = false;
      resetForm();
    } else {
      ElMessage.error('表单验证失败，请检查输入');
    }
  });
};

// 处理编辑操作
const handleEdit = (row) => {
  isEdit.value = true;
  currentId.value = row.id;
  
  browserForm.name = row.name;
  browserForm.version = row.version;
  browserForm.isEnabled = row.isEnabled;
  browserForm.isHeadless = row.isHeadless;
  browserForm.description = row.description;
  
  dialogVisible.value = true;
};

// 处理删除操作
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
      '确定要删除该浏览器配置吗？',
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    );
    
    emit('delete-browser', row.id);
  } catch (error) {
    // 用户取消删除操作
    console.log('取消删除操作');
  }
};

// 处理启用状态变更
const handleStatusChange = (row) => {
  emit('update-browser', { ...row });
};

// 处理无头模式变更
const handleHeadlessChange = (row) => {
  emit('update-browser', { ...row });
};

// 对外暴露验证方法
defineExpose({
  validate
});
</script>

<style scoped>
.browser-config-container {
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

.dialog-footer {
  display: flex;
  justify-content: flex-end;
}
</style> 