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
      <el-table-column prop="configName" label="配置名称" width="150" />
      <el-table-column prop="browserType" label="浏览器类型" width="120" />
      <el-table-column prop="browserVersion" label="版本" width="100" />
      <el-table-column prop="deviceType" label="设备类型" width="120" />
      <el-table-column prop="platformName" label="平台" width="120" />
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
      <el-table-column label="无头模式" width="80">
        <template #default="scope">
          <el-switch
            v-model="scope.row.headless"
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

    <!-- 添加/编辑浏览器对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑浏览器配置' : '添加浏览器配置'"
      width="500px"
    >
      <el-form :model="browserForm" :rules="rules" ref="browserFormRef" label-width="120px">
        <el-form-item label="配置名称" prop="configName">
          <el-input v-model="browserForm.configName" placeholder="请输入配置名称" />
        </el-form-item>
        <el-form-item label="浏览器类型" prop="browserType">
          <el-select v-model="browserForm.browserType" placeholder="请选择浏览器类型">
            <el-option label="Chrome" value="CHROME" />
            <el-option label="Firefox" value="FIREFOX" />
            <el-option label="Edge" value="EDGE" />
            <el-option label="Safari" value="SAFARI" />
          </el-select>
        </el-form-item>
        <el-form-item label="浏览器版本" prop="browserVersion">
          <el-input v-model="browserForm.browserVersion" placeholder="请输入版本号" />
        </el-form-item>
        <el-form-item label="设备类型" prop="deviceType">
          <el-select v-model="browserForm.deviceType" placeholder="请选择设备类型">
            <el-option label="桌面" value="DESKTOP" />
            <el-option label="移动设备" value="MOBILE" />
            <el-option label="平板" value="TABLET" />
          </el-select>
        </el-form-item>
        <el-form-item label="平台" prop="platformName">
          <el-select v-model="browserForm.platformName" placeholder="请选择平台">
            <el-option label="Windows" value="WINDOWS" />
            <el-option label="MacOS" value="MAC" />
            <el-option label="Linux" value="LINUX" />
            <el-option label="Android" value="ANDROID" />
            <el-option label="iOS" value="IOS" />
          </el-select>
        </el-form-item>
        <el-form-item label="平台版本" prop="platformVersion">
          <el-input v-model="browserForm.platformVersion" placeholder="请输入平台版本" />
        </el-form-item>
        <el-form-item label="是否启用" prop="isActive">
          <el-switch v-model="browserForm.isActive" />
        </el-form-item>
        <el-form-item label="无头模式" prop="headless">
          <el-switch v-model="browserForm.headless" />
        </el-form-item>
        <el-form-item label="设为默认" prop="isDefault">
          <el-switch v-model="browserForm.isDefault" />
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
import { ref, reactive, defineProps, defineEmits, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { UiTestConfigApi } from '@/api/uiTestConfigService';

// 定义接收的属性
const props = defineProps({
  // 移除 browserConfigs 属性，我们将直接从API获取数据
});

// 定义向父组件发送的事件
const emit = defineEmits(['refresh']);

// 状态管理
const loading = ref(false);
const dialogVisible = ref(false);
const isEdit = ref(false);
const currentId = ref(null);
const browserFormRef = ref(null);
const browserConfigs = ref([]);

// 表单数据
const browserForm = reactive({
  configName: '',
  configType: 'BROWSER',
  browserType: '',
  browserVersion: '',
  deviceType: 'DESKTOP',
  platformName: 'WINDOWS',
  platformVersion: '',
  headless: false,
  isDefault: false,
  isActive: true,
  description: ''
});

// 表单验证规则
const rules = {
  configName: [
    { required: true, message: '请输入配置名称', trigger: 'blur' },
    { min: 1, max: 50, message: '长度在 1 到 50 个字符', trigger: 'blur' }
  ],
  browserType: [
    { required: true, message: '请选择浏览器类型', trigger: 'change' }
  ],
  browserVersion: [
    { required: true, message: '请输入浏览器版本', trigger: 'blur' }
  ]
};

// 加载数据
const loadData = async () => {
  loading.value = true;
  try {
    const res = await UiTestConfigApi.getBrowserConfigs();
    console.log('浏览器配置响应:', res);
    if (res.success) {
      browserConfigs.value = res.data || [];
    } else {
      ElMessage.error(res.message || '获取浏览器配置失败');
    }
  } catch (error) {
    console.error('获取浏览器配置失败:', error);
    ElMessage.error('获取浏览器配置失败');
  } finally {
    loading.value = false;
  }
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

  browserForm.configName = '';
  browserForm.configType = 'BROWSER';
  browserForm.browserType = '';
  browserForm.browserVersion = '';
  browserForm.deviceType = 'DESKTOP';
  browserForm.platformName = 'WINDOWS';
  browserForm.platformVersion = '';
  browserForm.headless = false;
  browserForm.isDefault = false;
  browserForm.isActive = true;
  browserForm.description = '';
  currentId.value = null;
};

// 处理表单提交
const handleSubmit = async () => {
  if (!browserFormRef.value) return;

  await browserFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        loading.value = true;

        if (isEdit.value && currentId.value !== null) {
          // 更新配置
          const res = await UiTestConfigApi.updateConfig(currentId.value, browserForm);
          if (res.success) {
            ElMessage.success('更新浏览器配置成功');
            loadData(); // 重新加载数据
          } else {
            ElMessage.error(res.message || '更新浏览器配置失败');
          }
        } else {
          // 创建配置
          const res = await UiTestConfigApi.createConfig(browserForm);
          if (res.success) {
            ElMessage.success('添加浏览器配置成功');
            loadData(); // 重新加载数据
          } else {
            ElMessage.error(res.message || '添加浏览器配置失败');
          }
        }

        dialogVisible.value = false;
        resetForm();
        emit('refresh');
      } catch (error) {
        console.error('保存浏览器配置失败:', error);
        ElMessage.error('保存浏览器配置失败');
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

  browserForm.configName = row.configName;
  browserForm.configType = 'BROWSER';
  browserForm.browserType = row.browserType;
  browserForm.browserVersion = row.browserVersion;
  browserForm.deviceType = row.deviceType || 'DESKTOP';
  browserForm.platformName = row.platformName || 'WINDOWS';
  browserForm.platformVersion = row.platformVersion || '';
  browserForm.headless = row.headless;
  browserForm.isDefault = row.isDefault;
  browserForm.isActive = row.isActive;
  // 添加其他需要编辑的字段

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

    loading.value = true;
    const res = await UiTestConfigApi.deleteConfig(row.id);
    if (res.success) {
      ElMessage.success('删除浏览器配置成功');
      loadData(); // 重新加载数据
      emit('refresh');
    } else {
      ElMessage.error(res.message || '删除浏览器配置失败');
    }
  } catch (error) {
    // 用户取消删除操作或出现错误
    if (error !== 'cancel' && error !== 'close') {
      console.error('删除浏览器配置失败:', error);
      ElMessage.error('删除浏览器配置失败');
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
      ElMessage.success(row.isActive ? '浏览器配置已启用' : '浏览器配置已禁用');
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

// 处理无头模式变更
const handleHeadlessChange = async (row) => {
  try {
    loading.value = true;
    const updateData = { ...row, headless: row.isHeadless };
    const res = await UiTestConfigApi.updateConfig(row.id, updateData);
    if (res.success) {
      ElMessage.success('无头模式设置已更新');
      emit('refresh');
    } else {
      ElMessage.error(res.message || '更新无头模式失败');
      // 恢复原状态
      row.isHeadless = !row.isHeadless;
    }
  } catch (error) {
    console.error('更新无头模式失败:', error);
    ElMessage.error('更新无头模式失败');
    // 恢复原状态
    row.isHeadless = !row.isHeadless;
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