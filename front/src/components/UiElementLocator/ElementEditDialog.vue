<template>
  <el-dialog
    v-model="dialogVisible"
    :title="isEdit ? '编辑元素' : '添加元素'"
    width="700px"
    :close-on-click-modal="false"
    @close="resetForm"
  >
    <el-form :model="elementForm" label-width="120px" ref="elementFormRef" :rules="formRules">
      <el-form-item label="元素名称" prop="elementName">
        <el-input v-model="elementForm.elementName" placeholder="请输入元素名称" />
      </el-form-item>
      
      <el-form-item label="所属分组" prop="groupId">
        <el-cascader
          v-model="elementForm.groupId"
          :options="groupOptions"
          :props="{ checkStrictly: true }"
          placeholder="请选择所属分组"
          style="width: 100%"
        />
      </el-form-item>
      
      <el-form-item label="元素类型" prop="elementType">
        <el-select v-model="elementForm.elementType" placeholder="请选择元素类型" style="width: 100%">
          <el-option label="页面组件" value="page_component" />
          <el-option label="表单元素" value="form_element" />
          <el-option label="交互元素" value="interactive_element" />
          <el-option label="其他" value="other" />
        </el-select>
      </el-form-item>
      
      <el-form-item label="定位方式" prop="locatorType">
        <el-select v-model="elementForm.locatorType" placeholder="请选择定位方式" style="width: 100%">
          <el-option label="ID" value="ID" />
          <el-option label="CSS 选择器" value="CSS" />
          <el-option label="XPath" value="XPath" />
          <el-option label="Name" value="Name" />
          <el-option label="Class Name" value="ClassName" />
          <el-option label="Link Text" value="LinkText" />
          <el-option label="Tag Name" value="TagName" />
          <el-option label="Accessibility ID" value="AccessibilityId" />
        </el-select>
      </el-form-item>
      
      <el-form-item label="定位值" prop="locatorValue">
        <el-input 
          v-model="elementForm.locatorValue" 
          placeholder="请输入定位值"
          type="textarea"
          :rows="3"
        />
      </el-form-item>
      
      <el-form-item label="所属页面" prop="pageUrl">
        <el-input 
          v-model="elementForm.pageUrl" 
          placeholder="请输入元素所在页面URL" 
        />
      </el-form-item>
      
      <el-form-item label="页面描述">
        <el-input 
          v-model="elementForm.pageDescription" 
          placeholder="请输入页面描述"
        />
      </el-form-item>
      
      <el-form-item label="元素截图">
        <el-upload
          class="element-uploader"
          :action="uploadAction"
          :on-success="handleUploadSuccess"
          :on-error="handleUploadError"
          :on-preview="handlePreview"
          :before-upload="beforeUpload"
          :limit="1"
          list-type="picture-card"
          :file-list="fileList"
          :headers="uploadHeaders"
          :disabled="!elementForm.elementId"
        >
          <el-icon><Plus /></el-icon>
          <template #tip>
            <div class="el-upload__tip">
              请先保存元素信息，再上传截图。支持jpg/png文件，且不超过5MB
            </div>
          </template>
        </el-upload>
      </el-form-item>
      
      <el-form-item label="备注">
        <el-input 
          v-model="elementForm.remarks" 
          placeholder="请输入备注信息"
          type="textarea"
          :rows="2"
        />
      </el-form-item>
    </el-form>
    
    <div class="dialog-footer" style="margin-top: 20px; text-align: right;">
      <el-button type="primary" @click="validateLocator" :disabled="!elementForm.locatorValue">验证定位器</el-button>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" @click="submitForm">确定</el-button>
    </div>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, defineProps, defineEmits, watch } from 'vue';
import { ElMessage } from 'element-plus';
import { Plus } from '@element-plus/icons-vue';
import { UiElementLocatorApi } from '@/api/uiElementLocatorService';

const props = defineProps({
  visible: Boolean,
  element: Object,
  isEdit: {
    type: Boolean,
    default: false
  },
  groupOptions: {
    type: Array,
    default: () => []
  }
});

const emit = defineEmits(['update:visible', 'submit', 'validation-result']);

// 对话框可见性
const dialogVisible = ref(props.visible);

// 文件列表
const fileList = ref([]);

// 表单引用
const elementFormRef = ref(null);

// 元素表单数据
const elementForm = reactive({
  elementId: "",
  elementName: "",
  groupId: "",
  elementType: "page_component",
  locatorType: "CSS",
  locatorValue: "",
  pageUrl: "",
  pageDescription: "",
  remarks: "",
  hasScreenshot: false
});

// 表单验证规则
const formRules = {
  elementName: [
    { required: true, message: '请输入元素名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  groupId: [
    { required: true, message: '请选择所属分组', trigger: 'change' }
  ],
  elementType: [
    { required: true, message: '请选择元素类型', trigger: 'change' }
  ],
  locatorType: [
    { required: true, message: '请选择定位方式', trigger: 'change' }
  ],
  locatorValue: [
    { required: true, message: '请输入定位值', trigger: 'blur' }
  ],
  pageUrl: [
    { required: true, message: '请输入元素所在页面URL', trigger: 'blur' }
  ]
};

// 上传地址
const uploadAction = ref('');
const uploadHeaders = reactive({
  Authorization: 'Bearer ' + localStorage.getItem('token')
});

// 监听对话框可见性变化
watch(() => props.visible, (newVal) => {
  dialogVisible.value = newVal;
});

// 监听dialogVisible变化
watch(dialogVisible, (newVal) => {
  emit('update:visible', newVal);
});

// 监听element变化
watch(() => props.element, (newVal) => {
  if (newVal) {
    // 填充表单数据
    Object.keys(elementForm).forEach(key => {
      if (newVal[key] !== undefined) {
        elementForm[key] = newVal[key];
      }
    });
    
    // 如果有截图，更新文件列表
    if (newVal.hasScreenshot && newVal.elementId) {
      fileList.value = [{
        name: `${newVal.elementName}的截图`,
        url: `/api/ui-elements/${newVal.elementId}/screenshot`
      }];
    } else {
      fileList.value = [];
    }
    
    // 更新上传地址
    if (newVal.elementId) {
      uploadAction.value = `/api/ui-elements/${newVal.elementId}/screenshot`;
    }
  }
});

// 重置表单
const resetForm = () => {
  elementFormRef.value?.resetFields();
  Object.assign(elementForm, {
    elementId: "",
    elementName: "",
    groupId: "",
    elementType: "page_component",
    locatorType: "CSS",
    locatorValue: "",
    pageUrl: "",
    pageDescription: "",
    remarks: "",
    hasScreenshot: false
  });
  fileList.value = [];
};

// 上传成功处理
const handleUploadSuccess = (response, file) => {
  ElMessage.success('截图上传成功');
  elementForm.hasScreenshot = true;
};

// 上传失败处理
const handleUploadError = (error) => {
  ElMessage.error('截图上传失败');
  console.error('上传失败:', error);
};

// 预览处理
const handlePreview = (file) => {
  window.open(file.url);
};

// 上传前验证
const beforeUpload = (file) => {
  const isImage = file.type === 'image/jpeg' || file.type === 'image/png';
  const isLt5M = file.size / 1024 / 1024 < 5;

  if (!isImage) {
    ElMessage.error('只能上传JPG/PNG格式的图片!');
    return false;
  }
  if (!isLt5M) {
    ElMessage.error('图片大小不能超过5MB!');
    return false;
  }
  return true;
};

// 验证定位器
const validateLocator = async () => {
  if (!elementForm.locatorValue || !elementForm.locatorType) {
    ElMessage.warning('请先输入定位值和选择定位方式');
    return;
  }
  
  try {
    const data = {
      locatorType: elementForm.locatorType,
      locatorValue: elementForm.locatorValue,
      pageUrl: elementForm.pageUrl
    };
    
    // 这里实际上需要调用后端API进行验证
    // 由于现在只实现前端，我们使用模拟数据
    
    /*
    const res = await UiElementLocatorApi.validateLocator(data);
    if (res.code === 200) {
      emit('validation-result', true, res.data);
      ElMessage.success('定位器验证成功');
    } else {
      emit('validation-result', false, res.message);
      ElMessage.error(res.message || '验证失败');
    }
    */
    
    // 模拟验证结果
    const success = Math.random() > 0.3; // 70%的概率验证成功
    if (success) {
      emit('validation-result', true, {
        found: true,
        elementInfo: {
          tagName: 'div',
          attributes: {
            class: 'el-button__content',
            id: 'btn-submit'
          },
          text: '提交'
        }
      });
      ElMessage.success('定位器验证成功');
    } else {
      emit('validation-result', false, '未找到匹配的元素，请检查定位器语法或值');
      ElMessage.error('验证失败: 未找到匹配的元素，请检查定位器语法或值');
    }
    
  } catch (error) {
    emit('validation-result', false, error.message);
    ElMessage.error('验证过程发生错误');
    console.error('验证定位器出错:', error);
  }
};

// 提交表单
const submitForm = async () => {
  if (!elementFormRef.value) return;
  
  elementFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const formData = { ...elementForm };
        
        // 这里实际上需要调用后端API保存数据
        // 由于现在只实现前端，我们使用模拟数据
        
        /*
        let res;
        if (props.isEdit) {
          res = await UiElementLocatorApi.updateElement(formData.elementId, formData);
        } else {
          res = await UiElementLocatorApi.createElement(formData);
        }
        
        if (res.code === 200) {
          ElMessage.success(props.isEdit ? '更新成功' : '创建成功');
          dialogVisible.value = false;
          emit('submit', formData);
        } else {
          ElMessage.error(res.message || '操作失败');
        }
        */
        
        // 模拟保存成功
        setTimeout(() => {
          if (!props.isEdit) {
            // 模拟生成ID
            formData.elementId = 'EL' + Math.floor(Math.random() * 10000);
          }
          
          ElMessage.success(props.isEdit ? '更新成功' : '创建成功');
          dialogVisible.value = false;
          emit('submit', formData);
        }, 500);
        
      } catch (error) {
        ElMessage.error('操作失败');
        console.error('保存元素出错:', error);
      }
    } else {
      return false;
    }
  });
};
</script>

<style scoped>
.element-uploader :deep(.el-upload--picture-card) {
  width: 200px;
  height: 150px;
  line-height: 150px;
}

.el-upload__tip {
  font-size: 12px;
  color: #606266;
  margin-top: 5px;
}
</style> 