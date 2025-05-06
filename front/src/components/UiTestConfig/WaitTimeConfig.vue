<template>
  <div class="wait-time-config-container">
    <div class="header">
      <h3>等待时间配置</h3>
      <el-button type="primary" @click="showAddDialog">添加配置</el-button>
    </div>
    
    <el-table
      v-loading="loading"
      :data="waitTimeConfigs"
      border
      style="width: 100%"
    >
      <el-table-column prop="configName" label="配置名称" width="150" />
      <el-table-column prop="defaultWaitTimeout" label="默认等待时间(秒)" width="150" />
      <el-table-column prop="pageLoadTimeout" label="页面加载超时(秒)" width="150" />
      <el-table-column prop="scriptTimeout" label="脚本超时(秒)" width="150" />
      <el-table-column label="默认" width="80">
        <template #default="scope">
          <el-tag v-if="scope.row.isDefault" type="success">默认</el-tag>
          <el-button 
            v-else 
            type="text" 
            size="small" 
            @click="handleSetDefault(scope.row)"
          >
            设为默认
          </el-button>
        </template>
      </el-table-column>
      <el-table-column label="启用状态" width="80">
        <template #default="scope">
          <el-switch
            v-model="scope.row.isActive"
            @change="handleStatusChange(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template #default="scope">
          <el-button 
            type="primary" 
            size="small" 
            @click="handleEdit(scope.row)"
            :disabled="!scope.row.isActive"
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
    
    <!-- 添加/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑等待时间配置' : '添加等待时间配置'"
      width="500px"
    >
      <el-form :model="formData" :rules="rules" ref="formRef" label-width="120px">
        <el-form-item label="配置名称" prop="configName">
          <el-input v-model="formData.configName" placeholder="请输入配置名称" />
        </el-form-item>
        <el-form-item label="默认等待时间" prop="defaultWaitTimeout">
          <el-input-number
            v-model="formData.defaultWaitTimeout"
            :min="1"
            :max="60"
            controls-position="right"
          />
          <span class="unit">秒</span>
        </el-form-item>
        <el-form-item label="页面加载超时" prop="pageLoadTimeout">
          <el-input-number
            v-model="formData.pageLoadTimeout"
            :min="5"
            :max="300"
            controls-position="right"
          />
          <span class="unit">秒</span>
        </el-form-item>
        <el-form-item label="脚本执行超时" prop="scriptTimeout">
          <el-input-number
            v-model="formData.scriptTimeout"
            :min="5"
            :max="300"
            controls-position="right"
          />
          <span class="unit">秒</span>
        </el-form-item>
        <el-form-item label="是否启用" prop="isActive">
          <el-switch v-model="formData.isActive" />
        </el-form-item>
        <el-form-item label="设为默认" prop="isDefault">
          <el-switch v-model="formData.isDefault" />
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
import { ref, reactive, onMounted, defineEmits } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { UiTestConfigApi } from '@/api/uiTestConfigService';

// 定义事件
const emit = defineEmits(['refresh']);

// 状态管理
const loading = ref(false);
const dialogVisible = ref(false);
const isEdit = ref(false);
const currentId = ref(null);
const formRef = ref(null);
const waitTimeConfigs = ref([]);

// 表单数据
const formData = reactive({
  configName: '',
  configType: 'WAIT_TIME',
  defaultWaitTimeout: 10,
  pageLoadTimeout: 30,
  scriptTimeout: 30,
  isDefault: false,
  isActive: true
});

// 表单验证规则
const rules = {
  configName: [
    { required: true, message: '请输入配置名称', trigger: 'blur' },
    { min: 1, max: 50, message: '长度在 1 到 50 个字符', trigger: 'blur' }
  ],
  defaultWaitTimeout: [
    { required: true, message: '请设置默认等待超时时间', trigger: 'blur' }
  ],
  pageLoadTimeout: [
    { required: true, message: '请设置页面加载超时时间', trigger: 'blur' }
  ],
  scriptTimeout: [
    { required: true, message: '请设置脚本超时时间', trigger: 'blur' }
  ]
};

// 加载数据
const loadData = async () => {
  loading.value = true;
  try {
    const res = await UiTestConfigApi.getWaitTimeConfigs();
    if (res.success) {
      waitTimeConfigs.value = res.data || [];
    } else {
      ElMessage.error(res.message || '获取等待时间配置失败');
    }
  } catch (error) {
    console.error('获取等待时间配置失败:', error);
    ElMessage.error('获取等待时间配置失败');
  } finally {
    loading.value = false;
  }
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

// 显示添加对话框
const showAddDialog = () => {
  isEdit.value = false;
  resetForm();
  dialogVisible.value = true;
};

// 重置表单
const resetForm = () => {
  if (formRef.value) {
    formRef.value.resetFields();
  }
  
  formData.configName = '';
  formData.configType = 'WAIT_TIME';
  formData.defaultWaitTimeout = 10;
  formData.pageLoadTimeout = 30;
  formData.scriptTimeout = 30;
  formData.isDefault = false;
  formData.isActive = true;
  currentId.value = null;
};

// 处理表单提交
const handleSubmit = async () => {
  if (!formRef.value) return;
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        loading.value = true;
        
        if (isEdit.value && currentId.value !== null) {
          // 更新配置
          const res = await UiTestConfigApi.updateConfig(currentId.value, formData);
          if (res.success) {
            ElMessage.success('更新等待时间配置成功');
            loadData(); // 重新加载数据
          } else {
            ElMessage.error(res.message || '更新等待时间配置失败');
          }
        } else {
          // 创建配置
          const res = await UiTestConfigApi.createConfig(formData);
          if (res.success) {
            ElMessage.success('添加等待时间配置成功');
            loadData(); // 重新加载数据
          } else {
            ElMessage.error(res.message || '添加等待时间配置失败');
          }
        }
        
        dialogVisible.value = false;
        resetForm();
        emit('refresh');
      } catch (error) {
        console.error('保存等待时间配置失败:', error);
        ElMessage.error('保存等待时间配置失败');
      } finally {
        loading.value = false;
      }
    } else {
      ElMessage.error('表单验证失败，请检查输入');
    }
  });
};

// 处理编辑操作
const handleEdit = (row) => {
  isEdit.value = true;
  currentId.value = row.id;
  
  formData.configName = row.configName;
  formData.configType = 'WAIT_TIME';
  formData.defaultWaitTimeout = row.defaultWaitTimeout;
  formData.pageLoadTimeout = row.pageLoadTimeout;
  formData.scriptTimeout = row.scriptTimeout;
  formData.isDefault = row.isDefault;
  formData.isActive = row.isActive;
  
  dialogVisible.value = true;
};

// 处理删除操作
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
      '确定要删除该等待时间配置吗？',
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    );
    
    loading.value = true;
    const res = await UiTestConfigApi.deleteConfig(row.id);
    if (res.success) {
      ElMessage.success('删除等待时间配置成功');
      loadData(); // 重新加载数据
      emit('refresh');
    } else {
      ElMessage.error(res.message || '删除等待时间配置失败');
    }
  } catch (error) {
    // 用户取消删除操作或出现错误
    if (error !== 'cancel' && error !== 'close') {
      console.error('删除等待时间配置失败:', error);
      ElMessage.error('删除等待时间配置失败');
    }
  } finally {
    loading.value = false;
  }
};

// 处理启用状态变更
const handleStatusChange = async (row) => {
  try {
    loading.value = true;
    const res = await UiTestConfigApi.toggleConfigStatus(row.id, row.isActive);
    if (res.success) {
      ElMessage.success(row.isActive ? '等待时间配置已启用' : '等待时间配置已禁用');
      emit('refresh');
    } else {
      ElMessage.error(res.message || '更新状态失败');
      // 恢复原状态
      row.isActive = !row.isActive;
    }
  } catch (error) {
    console.error('更新状态失败:', error);
    ElMessage.error('更新状态失败');
    // 恢复原状态
    row.isActive = !row.isActive;
  } finally {
    loading.value = false;
  }
};

// 设置为默认配置
const handleSetDefault = async (row) => {
  try {
    loading.value = true;
    const res = await UiTestConfigApi.setDefaultConfig(row.id);
    if (res.success) {
      ElMessage.success('已设置为默认配置');
      loadData(); // 重新加载数据
      emit('refresh');
    } else {
      ElMessage.error(res.message || '设置默认配置失败');
    }
  } catch (error) {
    console.error('设置默认配置失败:', error);
    ElMessage.error('设置默认配置失败');
  } finally {
    loading.value = false;
  }
};

// 对外暴露验证方法
defineExpose({
  validate,
  loadData
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

.unit {
  margin-left: 8px;
}
</style> 