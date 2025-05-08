<template>
  <div class="license-container">
    <!-- 当前许可证信息卡片 -->
    <el-card class="mb-4">
      <template #header>
        <div class="card-header">
          <span>当前许可证信息</span>
          <el-button 
            v-if="hasPermission"
            type="primary" 
            size="small" 
            @click="handleAddLicense"
          >
            添加许可证
          </el-button>
        </div>
      </template>
      
      <div v-if="loading" class="center-content">
        <el-skeleton animated :rows="5" />
      </div>
      
      <div v-else-if="!currentLicense" class="empty-data">
        <el-empty description="暂无许可证信息">
          <template #extra>
            <p class="text-warning">系统当前处于未授权状态，请添加有效的许可证</p>
          </template>
        </el-empty>
      </div>
      
      <div v-else class="license-detail">
        <div class="license-header">
          <div class="license-type" :class="{'expired': currentLicense.expired}">
            {{ currentLicense.licenseType }}
            <el-tag v-if="currentLicense.expired" type="danger" effect="dark" size="small">已过期</el-tag>
            <el-tag v-else-if="currentLicense.remainingDays < 30" type="warning" effect="dark" size="small">
              即将到期 (剩余{{ currentLicense.remainingDays }}天)
            </el-tag>
            <el-tag v-else-if="currentLicense.expirationDate" type="success" effect="dark" size="small">
              有效 (剩余{{ currentLicense.remainingDays }}天)
            </el-tag>
            <el-tag v-else type="success" effect="dark" size="small">永久有效</el-tag>
          </div>
        </div>
        
        <el-descriptions :column="2" border>
          <el-descriptions-item label="许可证密钥">{{ currentLicense.licenseKey }}</el-descriptions-item>
          <el-descriptions-item label="颁发给">{{ currentLicense.issuedTo || '未指定' }}</el-descriptions-item>
          <el-descriptions-item label="颁发者">{{ currentLicense.issuedBy || '未指定' }}</el-descriptions-item>
          <el-descriptions-item label="颁发日期">{{ formatDate(currentLicense.issueDate) }}</el-descriptions-item>
          <el-descriptions-item label="过期日期">
            {{ currentLicense.expirationDate ? formatDate(currentLicense.expirationDate) : '永久有效' }}
          </el-descriptions-item>
          <el-descriptions-item label="最大用户数">{{ currentLicense.maxUsers || '无限制' }}</el-descriptions-item>
          <el-descriptions-item label="状态">{{ currentLicense.status }}</el-descriptions-item>
          <el-descriptions-item label="备注">{{ currentLicense.remarks || '无' }}</el-descriptions-item>
        </el-descriptions>
        
        <div v-if="currentLicense.features" class="features-section">
          <div class="section-title">功能权限</div>
          <div class="features-list">
            <el-tag 
              v-for="(value, key) in parsedFeatures" 
              :key="key"
              :type="value ? 'success' : 'info'"
              class="feature-tag"
              effect="light"
              size="large"
            >
              <el-tooltip :content="key" placement="top" :disabled="formatFeatureName(key) !== key">
                <span>{{ formatFeatureName(key) }} {{ value ? '✓' : '✗' }}</span>
              </el-tooltip>
            </el-tag>
          </div>
        </div>
      </div>
    </el-card>
    
    <!-- 许可证历史记录 -->
    <el-card v-if="hasPermission">
      <template #header>
        <div class="card-header">
          <span>许可证历史记录</span>
        </div>
      </template>
      
      <div v-if="listLoading" class="center-content">
        <el-skeleton animated :rows="5" />
      </div>
      
      <div v-else-if="!licenseList || licenseList.length === 0" class="empty-data">
        <el-empty description="暂无许可证历史记录" />
      </div>
      
      <el-table v-else :data="licenseList" style="width: 100%">
        <el-table-column prop="licenseKey" label="许可证密钥" width="220" show-overflow-tooltip />
        <el-table-column prop="licenseType" label="类型" width="120" />
        <el-table-column prop="issuedTo" label="颁发给" width="150" show-overflow-tooltip />
        <el-table-column label="颁发日期" width="180">
          <template #default="scope">
            {{ formatDate(scope.row.issueDate) }}
          </template>
        </el-table-column>
        <el-table-column label="过期日期" width="180">
          <template #default="scope">
            {{ scope.row.expirationDate ? formatDate(scope.row.expirationDate) : '永久有效' }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getLicenseStatusType(scope.row)">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button 
              type="primary" 
              size="small" 
              plain
              @click="handleEditLicense(scope.row)"
            >
              编辑
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <!-- 添加/编辑许可证对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'add' ? '添加许可证' : '编辑许可证信息'"
      width="650px"
    >
      <el-form
        ref="licenseFormRef"
        :model="licenseForm"
        :rules="licenseRules"
        label-width="100px"
      >
        <el-form-item label="许可证密钥" prop="licenseKey">
          <el-input v-model="licenseForm.licenseKey" placeholder="请输入许可证密钥" />
        </el-form-item>
        <el-form-item label="许可证类型" prop="licenseType">
          <el-select v-model="licenseForm.licenseType" placeholder="请选择许可证类型" style="width: 100%">
            <el-option label="试用版" value="试用版" />
            <el-option label="标准版" value="标准版" />
            <el-option label="专业版" value="专业版" />
            <el-option label="企业版" value="企业版" />
          </el-select>
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="颁发给" prop="issuedTo">
              <el-input v-model="licenseForm.issuedTo" placeholder="请输入颁发对象" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="颁发者" prop="issuedBy">
              <el-input v-model="licenseForm.issuedBy" placeholder="请输入颁发者" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="颁发日期" prop="issueDate">
              <el-date-picker
                v-model="licenseForm.issueDate"
                type="datetime"
                placeholder="选择颁发日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="过期日期" prop="expirationDate">
              <el-date-picker
                v-model="licenseForm.expirationDate"
                type="datetime"
                placeholder="选择过期日期，不设置则永久有效"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="最大用户数" prop="maxUsers">
              <el-input-number v-model="licenseForm.maxUsers" :min="0" placeholder="最大用户数，0表示无限制" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="licenseForm.status" placeholder="请选择状态" style="width: 100%">
                <el-option label="有效" value="有效" />
                <el-option label="过期" value="过期" />
                <el-option label="禁用" value="禁用" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="功能权限" prop="features">
          <div class="feature-checkboxes">
            <el-checkbox v-model="featureForm.advancedReporting">高级报表</el-checkbox>
            <el-checkbox v-model="featureForm.multipleEnvironments">多环境支持</el-checkbox>
            <el-checkbox v-model="featureForm.cicdIntegration">CI/CD集成</el-checkbox>
            <el-checkbox v-model="featureForm.apiAutomation">API自动化</el-checkbox>
            <el-checkbox v-model="featureForm.uiAutomation">UI自动化</el-checkbox>
            <el-checkbox v-model="featureForm.dataImportExport">数据导入导出</el-checkbox>
          </div>
        </el-form-item>
        <el-form-item label="备注" prop="remarks">
          <el-input
            v-model="licenseForm.remarks"
            type="textarea"
            rows="3"
            placeholder="请输入备注信息"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm" :loading="submitLoading">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 验证许可证 -->
    <el-dialog
      v-if="false" <!-- 暂时隐藏该功能 -->
      v-model="validateDialogVisible"
      title="验证许可证"
      width="500px"
    >
      <el-form
        ref="validateFormRef"
        :model="validateForm"
        :rules="validateRules"
        label-width="100px"
      >
        <el-form-item label="许可证密钥" prop="licenseKey">
          <el-input v-model="validateForm.licenseKey" placeholder="请输入要验证的许可证密钥" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="validateDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="validateLicense" :loading="validateLoading">
            验证
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { SysInfoApi } from '@/api/sysInfoService'
import { ElMessage, ElMessageBox } from 'element-plus'
import { checkPermission } from '@/utils/permission'
import { formatDateTime } from '@/utils/date'

// 数据状态
const currentLicense = ref(null)
const licenseList = ref([])
const loading = ref(false)
const listLoading = ref(false)
const dialogVisible = ref(false)
const validateDialogVisible = ref(false)
const dialogType = ref('add') // 'add' 或 'edit'
const submitLoading = ref(false)
const validateLoading = ref(false)
const hasPermission = checkPermission('system:info:edit')

// 表单引用
const licenseFormRef = ref(null)
const validateFormRef = ref(null)

// 表单数据
const licenseForm = reactive({
  id: null,
  licenseKey: '',
  licenseType: '',
  issuedTo: '',
  issuedBy: '',
  issueDate: new Date(),
  expirationDate: null,
  maxUsers: 0,
  features: '',
  status: '有效',
  remarks: ''
})

// 功能权限表单
const featureForm = reactive({
  advancedReporting: false,
  multipleEnvironments: false,
  cicdIntegration: false,
  apiAutomation: false,
  uiAutomation: false,
  dataImportExport: false
})

// 将功能权限表单同步到licenseForm.features
const syncFeaturesToForm = () => {
  licenseForm.features = JSON.stringify(featureForm)
}

// 从licenseForm.features更新功能权限表单
const updateFeatureFormFromLicense = () => {
  if (!licenseForm.features) {
    Object.keys(featureForm).forEach(key => {
      featureForm[key] = false
    })
    return
  }
  
  try {
    const features = JSON.parse(licenseForm.features)
    Object.keys(featureForm).forEach(key => {
      featureForm[key] = features[key] === true
    })
  } catch (e) {
    console.error('解析功能权限失败', e)
  }
}

// 验证表单
const validateForm = reactive({
  licenseKey: ''
})

// 表单验证规则
const licenseRules = {
  licenseKey: [
    { required: true, message: '请输入许可证密钥', trigger: 'blur' },
    { max: 255, message: '许可证密钥不能超过255个字符', trigger: 'blur' }
  ],
  licenseType: [
    { required: true, message: '请选择许可证类型', trigger: 'change' }
  ],
  issueDate: [
    { required: true, message: '请选择颁发日期', trigger: 'change' }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ]
}

const validateRules = {
  licenseKey: [
    { required: true, message: '请输入许可证密钥', trigger: 'blur' }
  ]
}

// 获取当前许可证信息
const fetchCurrentLicense = async () => {
  loading.value = true
  try {
    const res = await SysInfoApi.getCurrentLicense()
    console.log('许可证数据:', res)
    // 直接使用data字段的数据
    currentLicense.value = res.data
  } catch (error) {
    ElMessage.error('获取当前许可证信息失败')
    console.error(error)
  } finally {
    loading.value = false
  }
}

// 获取许可证列表
const fetchLicenseList = async () => {
  if (!hasPermission) return
  
  listLoading.value = true
  try {
    const res = await SysInfoApi.getLicenseList()
    console.log('许可证列表数据:', res)
    // 直接使用data字段的数据
    licenseList.value = res.data || []
  } catch (error) {
    ElMessage.error('获取许可证列表失败')
    console.error(error)
  } finally {
    listLoading.value = false
  }
}

// 添加许可证
const handleAddLicense = () => {
  dialogType.value = 'add'
  resetForm()
  dialogVisible.value = true
}

// 编辑许可证
const handleEditLicense = (row) => {
  dialogType.value = 'edit'
  resetForm()
  
  Object.keys(licenseForm).forEach(key => {
    if (key in row) {
      licenseForm[key] = row[key]
    }
  })
  
  updateFeatureFormFromLicense()
  dialogVisible.value = true
}

// 重置表单
const resetForm = () => {
  licenseForm.id = null
  licenseForm.licenseKey = ''
  licenseForm.licenseType = ''
  licenseForm.issuedTo = ''
  licenseForm.issuedBy = ''
  licenseForm.issueDate = new Date()
  licenseForm.expirationDate = null
  licenseForm.maxUsers = 0
  licenseForm.features = ''
  licenseForm.status = '有效'
  licenseForm.remarks = ''
  
  // 重置功能权限
  Object.keys(featureForm).forEach(key => {
    featureForm[key] = false
  })
  
  if (licenseFormRef.value) {
    licenseFormRef.value.resetFields()
  }
}

// 提交表单
const submitForm = async () => {
  // 同步功能权限到表单
  syncFeaturesToForm()
  
  if (licenseFormRef.value) {
    await licenseFormRef.value.validate(async (valid) => {
      if (valid) {
        submitLoading.value = true
        try {
          const apiMethod = dialogType.value === 'add' 
            ? SysInfoApi.addLicense 
            : SysInfoApi.updateLicense
          
          const { data } = await apiMethod(licenseForm)
          
          if (data.code === 200) {
            ElMessage.success(dialogType.value === 'add' ? '添加成功' : '更新成功')
            dialogVisible.value = false
            
            // 刷新数据
            await fetchCurrentLicense()
            await fetchLicenseList()
          } else {
            ElMessage.error(data.message || '操作失败')
          }
        } catch (error) {
          ElMessage.error('操作失败')
          console.error(error)
        } finally {
          submitLoading.value = false
        }
      }
    })
  }
}

// 验证许可证
const validateLicense = async () => {
  if (validateFormRef.value) {
    await validateFormRef.value.validate(async (valid) => {
      if (valid) {
        validateLoading.value = true
        try {
          const { data } = await SysInfoApi.validateLicenseKey(validateForm.licenseKey)
          
          if (data.code === 200) {
            if (data.data) {
              ElMessage.success('许可证有效')
            } else {
              ElMessage.warning('许可证无效')
            }
            validateDialogVisible.value = false
          } else {
            ElMessage.error(data.message || '验证失败')
          }
        } catch (error) {
          ElMessage.error('验证失败')
          console.error(error)
        } finally {
          validateLoading.value = false
        }
      }
    })
  }
}

// 解析功能权限
const parsedFeatures = computed(() => {
  if (!currentLicense.value || !currentLicense.value.features) {
    return {}
  }
  
  try {
    return JSON.parse(currentLicense.value.features)
  } catch (e) {
    console.error('解析功能权限失败', e)
    return {}
  }
})

// 格式化功能权限名称
const formatFeatureName = (key) => {
  const nameMap = {
    advancedReporting: '高级报表',
    multipleEnvironments: '多环境支持',
    cicdIntegration: 'CI/CD集成',
    apiAutomation: 'API自动化',
    uiAutomation: 'UI自动化',
    dataImportExport: '数据导入导出',
    // 添加后端返回的功能键映射
    ui_test: 'UI测试',
    api_test: 'API测试',
    performance_test: '性能测试',
    mobile_test: '移动测试',
    integration: '第三方集成',
    advanced_reporting: '高级报表'
  }
  
  return nameMap[key] || key
}

// 获取许可证状态标签类型
const getLicenseStatusType = (license) => {
  if (license.status === '有效') {
    if (license.expired) {
      return 'danger'
    }
    return 'success'
  } else if (license.status === '过期') {
    return 'danger'
  } else {
    return 'info'
  }
}

// 格式化日期
const formatDate = (dateStr) => {
  if (!dateStr) return '暂无'
  return formatDateTime(new Date(dateStr))
}

// 生命周期钩子
onMounted(async () => {
  await fetchCurrentLicense()
  await fetchLicenseList()
})

// 暴露给父组件的方法，用于在标签切换时刷新数据
const fetchData = async () => {
  await fetchCurrentLicense()
  await fetchLicenseList()
}

// 将方法暴露给父组件
defineExpose({
  fetchData,
  fetchCurrentLicense,
  fetchLicenseList
})
</script>

<style scoped>
.license-container {
  padding: 10px;
}

.mb-4 {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.license-detail {
  padding: 10px;
}

.license-header {
  margin-bottom: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.license-type {
  font-size: 18px;
  font-weight: bold;
  color: #409EFF;
  display: flex;
  align-items: center;
  gap: 10px;
}

.license-type.expired {
  color: #F56C6C;
}

.center-content {
  display: flex;
  justify-content: center;
  padding: 20px 0;
}

.empty-data {
  padding: 20px 0;
}

.text-warning {
  color: #E6A23C;
  text-align: center;
  margin-top: 10px;
}

.features-section {
  margin-top: 20px;
  border-top: 1px solid #EBEEF5;
  padding-top: 20px;
}

.section-title {
  font-weight: bold;
  margin-bottom: 10px;
}

.features-list {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-top: 15px;
}

.feature-tag {
  margin-right: 0;
  border-radius: 4px;
  font-size: 14px;
  padding: 8px 12px;
}

.feature-checkboxes {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
}
</style> 