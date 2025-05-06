<template>
  <el-dialog
    v-model="dialogVisible"
    title="元素定位器验证"
    width="800px"
    :close-on-click-modal="false"
  >
    <div class="validate-dialog-container">
      <div class="validate-form-container">
        <el-form :model="validateForm" label-width="90px">
          <el-form-item label="定位方式">
            <el-select v-model="validateForm.locatorType" placeholder="请选择定位方式" style="width: 100%">
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
          
          <el-form-item label="定位值">
            <el-input 
              v-model="validateForm.locatorValue" 
              placeholder="请输入定位值"
              type="textarea"
              :rows="4"
            />
          </el-form-item>
          
          <el-form-item label="页面URL">
            <el-input 
              v-model="validateForm.pageUrl" 
              placeholder="请输入页面URL"
            />
          </el-form-item>
          
          <el-form-item>
            <el-button type="primary" @click="validateLocator" :loading="validating">
              验证定位器
            </el-button>
            <el-button @click="copyLocator" v-if="validationSuccess">
              复制定位器
            </el-button>
          </el-form-item>
        </el-form>
        
        <div v-if="validationResult" class="validation-result">
          <div :class="['result-header', validationSuccess ? 'success' : 'error']">
            <el-icon v-if="validationSuccess"><el-icon-check /></el-icon>
            <el-icon v-else><el-icon-close /></el-icon>
            <span>{{ validationSuccess ? '验证成功' : '验证失败' }}</span>
          </div>
          
          <div v-if="validationSuccess" class="result-content">
            <div class="element-info">
              <h4>元素信息</h4>
              <p><strong>标签名:</strong> {{ elementInfo.tagName }}</p>
              <div v-if="elementInfo.attributes">
                <p><strong>属性:</strong></p>
                <ul>
                  <li v-for="(value, key) in elementInfo.attributes" :key="key">
                    {{ key }}: {{ value }}
                  </li>
                </ul>
              </div>
              <p v-if="elementInfo.text"><strong>文本内容:</strong> {{ elementInfo.text }}</p>
            </div>
          </div>
          
          <div v-else class="result-content">
            <div class="error-info">
              <h4>错误信息</h4>
              <p>{{ validationResult }}</p>
              <div class="error-tips">
                <h5>常见错误解决方案：</h5>
                <ul>
                  <li>检查定位器语法是否正确</li>
                  <li>确认页面上是否存在该元素</li>
                  <li>尝试使用不同的定位方式</li>
                  <li>检查页面URL是否正确</li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <div class="validate-tips-container">
        <div class="tips-header">
          <span>定位器语法帮助</span>
        </div>
        
        <div class="tips-content">
          <el-collapse>
            <el-collapse-item title="ID 选择器" name="id">
              <div class="tip-item">
                <p><strong>语法:</strong> 元素的ID属性值</p>
                <p><strong>示例:</strong> <code>login-button</code></p>
                <p><strong>描述:</strong> 查找ID属性为"login-button"的元素</p>
              </div>
            </el-collapse-item>
            
            <el-collapse-item title="CSS 选择器" name="css">
              <div class="tip-item">
                <p><strong>语法:</strong> 标准CSS选择器</p>
                <p><strong>示例:</strong> <code>#login-form .submit-button</code></p>
                <p><strong>描述:</strong> 查找ID为"login-form"的元素下的class为"submit-button"的元素</p>
                <p><strong>更多示例:</strong></p>
                <ul>
                  <li><code>.nav-item:nth-child(2)</code> - 选择第二个导航项</li>
                  <li><code>button[type="submit"]</code> - 选择type为submit的按钮</li>
                  <li><code>div.container > p</code> - 选择class为container的div的直接子元素p</li>
                </ul>
              </div>
            </el-collapse-item>
            
            <el-collapse-item title="XPath 选择器" name="xpath">
              <div class="tip-item">
                <p><strong>语法:</strong> XPath表达式</p>
                <p><strong>示例:</strong> <code>//button[@id='submit-btn']</code></p>
                <p><strong>描述:</strong> 查找ID为"submit-btn"的按钮元素</p>
                <p><strong>更多示例:</strong></p>
                <ul>
                  <li><code>//div[@class='container']//li[contains(@class, 'item')]</code> - 选择class为container的div下包含class为item的li元素</li>
                  <li><code>//a[text()='登录']</code> - 选择文本为"登录"的a标签</li>
                  <li><code>//input[@name='username' and @type='text']</code> - 选择name为username且type为text的input</li>
                </ul>
              </div>
            </el-collapse-item>
            
            <el-collapse-item title="Name 选择器" name="name">
              <div class="tip-item">
                <p><strong>语法:</strong> 元素的name属性值</p>
                <p><strong>示例:</strong> <code>username</code></p>
                <p><strong>描述:</strong> 查找name属性为"username"的元素</p>
              </div>
            </el-collapse-item>
            
            <el-collapse-item title="其他选择器" name="others">
              <div class="tip-item">
                <p><strong>Class Name:</strong> 元素的class属性值，例如: <code>submit-button</code></p>
                <p><strong>Link Text:</strong> 链接的完整文本，例如: <code>点击这里</code></p>
                <p><strong>Tag Name:</strong> 元素的标签名，例如: <code>button</code></p>
                <p><strong>Accessibility ID:</strong> 元素的accessibility-id属性，例如: <code>login-btn</code></p>
              </div>
            </el-collapse-item>
          </el-collapse>
        </div>
      </div>
    </div>
    
    <div class="dialog-footer" style="margin-top: 20px; text-align: right;">
      <el-button @click="dialogVisible = false">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, defineProps, defineEmits, watch } from 'vue';
import { ElMessage } from 'element-plus';
import { Check, Close } from '@element-plus/icons-vue';
import { UiElementLocatorApi } from '@/api/uiElementLocatorService';

const props = defineProps({
  visible: Boolean,
  locator: {
    type: Object,
    default: () => ({
      locatorType: 'CSS',
      locatorValue: '',
      pageUrl: ''
    })
  }
});

const emit = defineEmits(['update:visible', 'validation-success']);

// 对话框可见性
const dialogVisible = ref(props.visible);

// 表单数据
const validateForm = reactive({
  locatorType: 'CSS',
  locatorValue: '',
  pageUrl: ''
});

// 验证状态
const validating = ref(false);
const validationSuccess = ref(false);
const validationResult = ref(null);
const elementInfo = ref({});

// 监听对话框可见性变化
watch(() => props.visible, (newVal) => {
  dialogVisible.value = newVal;
  if (newVal && props.locator) {
    validateForm.locatorType = props.locator.locatorType || 'CSS';
    validateForm.locatorValue = props.locator.locatorValue || '';
    validateForm.pageUrl = props.locator.pageUrl || '';
  }
});

// 监听dialogVisible变化
watch(dialogVisible, (newVal) => {
  emit('update:visible', newVal);
});

// 验证定位器
const validateLocator = async () => {
  if (!validateForm.locatorValue) {
    ElMessage.warning('请输入定位值');
    return;
  }
  
  validating.value = true;
  validationResult.value = null;
  
  try {
    const data = {
      locatorType: validateForm.locatorType,
      locatorValue: validateForm.locatorValue,
      url: validateForm.pageUrl
    };
    
    // 调用后端API进行验证
    const res = await UiElementLocatorApi.validateLocator(data);
    if (res.code === 200) {
      validationSuccess.value = res.data.valid;
      if (validationSuccess.value) {
        // 构造元素信息对象
        elementInfo.value = {
          tagName: 'div', // 实际应从响应中获取
          attributes: {
            // 实际属性应从响应中获取
            'data-locator': validateForm.locatorValue
          },
          text: '' // 实际文本应从响应中获取
        };
        validationResult.value = res.data.message || "元素验证成功";
      } else {
        validationResult.value = res.data.message || "未找到匹配的元素";
      }
    } else {
      validationSuccess.value = false;
      validationResult.value = res.message || '验证失败';
    }
    
    validating.value = false;
    
    if (validationSuccess.value) {
      emit('validation-success', {
        locatorType: validateForm.locatorType,
        locatorValue: validateForm.locatorValue,
        elementInfo: elementInfo.value
      });
    }
  } catch (error) {
    validationSuccess.value = false;
    validationResult.value = "验证过程发生错误: " + error.message;
    validating.value = false;
    console.error("验证定位器出错:", error);
  }
};

// 复制定位器
const copyLocator = () => {
  const textToCopy = `${validateForm.locatorType}: ${validateForm.locatorValue}`;
  navigator.clipboard.writeText(textToCopy)
    .then(() => {
      ElMessage.success('已复制定位器到剪贴板');
    })
    .catch(() => {
      // 降级方案
      const textarea = document.createElement('textarea');
      textarea.value = textToCopy;
      document.body.appendChild(textarea);
      textarea.select();
      document.execCommand('copy');
      document.body.removeChild(textarea);
      ElMessage.success('已复制定位器到剪贴板');
    });
};
</script>

<style scoped>
.validate-dialog-container {
  display: flex;
  gap: 20px;
}

.validate-form-container, .validate-tips-container {
  flex: 1;
  overflow: auto;
}

.validation-result {
  margin-top: 20px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
}

.result-header {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px;
  font-weight: bold;
}

.result-header.success {
  background-color: #f0f9eb;
  color: #67c23a;
  border-bottom: 1px solid #e1f3d8;
}

.result-header.error {
  background-color: #fef0f0;
  color: #f56c6c;
  border-bottom: 1px solid #fde2e2;
}

.result-content {
  padding: 15px;
}

.element-info, .error-info {
  margin-bottom: 10px;
}

.element-info h4, .error-info h4, .error-tips h5 {
  margin-top: 0;
  margin-bottom: 10px;
}

.element-info ul, .error-tips ul {
  margin-top: 5px;
  padding-left: 20px;
}

.tips-header {
  font-weight: bold;
  margin-bottom: 10px;
  padding-bottom: 10px;
  border-bottom: 1px solid #ebeef5;
}

.tips-content {
  margin-top: 10px;
}

.tip-item {
  margin-bottom: 10px;
}

.tip-item code {
  background-color: #f5f7fa;
  padding: 2px 4px;
  border-radius: 3px;
  font-family: monospace;
}

.tip-item ul {
  padding-left: 20px;
  margin-top: 5px;
}
</style> 