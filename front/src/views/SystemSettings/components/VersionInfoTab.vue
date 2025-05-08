<template>
  <div class="version-info-container">
    <!-- 当前版本信息卡片 -->
    <el-card class="mb-4">
      <template #header>
        <div class="card-header">
          <span>当前版本信息</span>
          <el-button 
            v-if="hasPermission"
            type="primary" 
            size="small" 
            @click="handleAddVersion"
          >
            添加新版本
          </el-button>
        </div>
      </template>
      
      <div v-if="loading" class="center-content">
        <el-skeleton animated :rows="3" />
      </div>
      
      <div v-else-if="!currentVersion" class="empty-data">
        <el-empty description="暂无版本信息" />
      </div>
      
      <div v-else class="version-detail">
        <div class="detail-item">
          <span class="label">版本号：</span>
          <span class="value">{{ currentVersion.version }}</span>
        </div>
        <div class="detail-item">
          <span class="label">发布日期：</span>
          <span class="value">{{ formatDate(currentVersion.releaseDate) }}</span>
        </div>
        <div class="detail-item">
          <span class="label">版本说明：</span>
          <div class="value release-notes">{{ currentVersion.releaseNotes || '暂无说明' }}</div>
        </div>
      </div>
    </el-card>
    
    <!-- 版本历史列表 -->
    <el-card>
      <template #header>
        <div class="card-header">
          <span>版本历史</span>
        </div>
      </template>
      
      <div v-if="historyLoading" class="center-content">
        <el-skeleton animated :rows="5" />
      </div>
      
      <div v-else-if="!versionHistory || versionHistory.length === 0" class="empty-data">
        <el-empty description="暂无版本历史记录" />
      </div>
      
      <el-table v-else :data="versionHistory" style="width: 100%">
        <el-table-column prop="version" label="版本号" width="150" />
        <el-table-column label="发布日期" width="180">
          <template #default="scope">
            {{ formatDate(scope.row.releaseDate) }}
          </template>
        </el-table-column>
        <el-table-column prop="releaseNotes" label="版本说明" show-overflow-tooltip />
        <el-table-column label="当前版本" width="100">
          <template #default="scope">
            <el-tag v-if="scope.row.isCurrent" type="success">是</el-tag>
            <el-tag v-else type="info">否</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" v-if="hasPermission">
          <template #default="scope">
            <el-button 
              v-if="!scope.row.isCurrent" 
              type="primary" 
              size="small" 
              @click="handleSetCurrent(scope.row.id)"
              :loading="actionLoading"
            >
              设为当前版本
            </el-button>
            <el-button 
              type="primary" 
              size="small" 
              plain
              @click="handleEditVersion(scope.row)"
            >
              编辑
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <!-- 添加/编辑版本对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'add' ? '添加新版本' : '编辑版本信息'"
      width="500px"
    >
      <el-form
        ref="versionForm"
        :model="versionForm"
        :rules="versionRules"
        label-width="100px"
      >
        <el-form-item label="版本号" prop="version">
          <el-input v-model="versionForm.version" placeholder="请输入版本号" />
        </el-form-item>
        <el-form-item label="发布日期" prop="releaseDate">
          <el-date-picker
            v-model="versionForm.releaseDate"
            type="datetime"
            placeholder="选择发布日期"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="版本说明" prop="releaseNotes">
          <el-input
            v-model="versionForm.releaseNotes"
            type="textarea"
            rows="4"
            placeholder="请输入版本说明"
          />
        </el-form-item>
        <el-form-item label="设为当前版本" prop="isCurrent">
          <el-switch v-model="versionForm.isCurrent" />
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
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { SysInfoApi } from '@/api/sysInfoService'
import { ElMessage, ElMessageBox } from 'element-plus'
import { checkPermission } from '@/utils/permission'
import { formatDateTime } from '@/utils/date'

// 数据状态
const currentVersion = ref(null)
const versionHistory = ref([])
const loading = ref(false)
const historyLoading = ref(false)
const actionLoading = ref(false)
const dialogVisible = ref(false)
const dialogType = ref('add') // 'add' 或 'edit'
const submitLoading = ref(false)
const hasPermission = checkPermission('system:info:edit')

// 表单数据
const versionForm = reactive({
  id: null,
  version: '',
  releaseDate: new Date(),
  releaseNotes: '',
  isCurrent: false
})

// 表单验证规则
const versionRules = {
  version: [
    { required: true, message: '请输入版本号', trigger: 'blur' },
    { max: 50, message: '版本号不能超过50个字符', trigger: 'blur' }
  ],
  releaseDate: [
    { required: true, message: '请选择发布日期', trigger: 'change' }
  ]
}

const versionFormRef = ref(null)

// 获取当前版本信息
const fetchCurrentVersion = async () => {
  loading.value = true
  try {
    const res = await SysInfoApi.getCurrentVersion()
    console.log('当前版本数据:', res)
    // 直接使用data字段的数据
    currentVersion.value = res.data
  } catch (error) {
    ElMessage.error('获取当前版本信息失败')
    console.error(error)
  } finally {
    loading.value = false
  }
}

// 获取版本历史
const fetchVersionHistory = async () => {
  historyLoading.value = true
  try {
    const res = await SysInfoApi.getVersionHistory()
    console.log('版本历史数据:', res)
    // 直接使用data字段的数据
    versionHistory.value = res.data || []
  } catch (error) {
    ElMessage.error('获取版本历史失败')
    console.error(error)
  } finally {
    historyLoading.value = false
  }
}

// 设置当前版本
const handleSetCurrent = async (id) => {
  actionLoading.value = true
  try {
    const res = await SysInfoApi.setCurrentVersion(id)
    console.log('设置当前版本响应:', res)
    
    if (res.code === 200) {
      ElMessage.success('设置当前版本成功')
      // 刷新数据
      await fetchCurrentVersion()
      await fetchVersionHistory()
    } else {
      ElMessage.error(res.message || '设置当前版本失败')
    }
  } catch (error) {
    ElMessage.error('设置当前版本失败')
    console.error(error)
  } finally {
    actionLoading.value = false
  }
}

// 添加新版本
const handleAddVersion = () => {
  dialogType.value = 'add'
  resetForm()
  dialogVisible.value = true
}

// 编辑版本
const handleEditVersion = (row) => {
  dialogType.value = 'edit'
  Object.keys(versionForm).forEach(key => {
    if (key in row) {
      versionForm[key] = row[key]
    }
  })
  dialogVisible.value = true
}

// 重置表单
const resetForm = () => {
  versionForm.id = null
  versionForm.version = ''
  versionForm.releaseDate = new Date()
  versionForm.releaseNotes = ''
  versionForm.isCurrent = false
  if (versionFormRef.value) {
    versionFormRef.value.resetFields()
  }
}

// 提交表单
const submitForm = async () => {
  if (versionFormRef.value) {
    await versionFormRef.value.validate(async (valid) => {
      if (valid) {
        submitLoading.value = true
        try {
          const apiMethod = dialogType.value === 'add' 
            ? SysInfoApi.addVersionInfo 
            : SysInfoApi.updateVersionInfo
          
          const res = await apiMethod(versionForm)
          console.log('提交版本表单响应:', res)
          
          if (res.code === 200) {
            ElMessage.success(dialogType.value === 'add' ? '添加成功' : '更新成功')
            dialogVisible.value = false
            
            // 刷新数据
            await fetchCurrentVersion()
            await fetchVersionHistory()
          } else {
            ElMessage.error(res.message || '操作失败')
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

// 格式化日期
const formatDate = (dateStr) => {
  if (!dateStr) return '暂无'
  return formatDateTime(new Date(dateStr))
}

// 生命周期钩子
onMounted(async () => {
  await fetchCurrentVersion()
  await fetchVersionHistory()
})

// 暴露给父组件的方法，用于在标签切换时刷新数据
const fetchData = async () => {
  await fetchCurrentVersion()
  await fetchVersionHistory()
}

// 将方法暴露给父组件
defineExpose({
  fetchData,
  fetchCurrentVersion,
  fetchVersionHistory
})
</script>

<style scoped>
.version-info-container {
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

.version-detail {
  padding: 10px;
}

.detail-item {
  display: flex;
  margin-bottom: 10px;
}

.label {
  font-weight: bold;
  width: 100px;
}

.value {
  flex: 1;
}

.release-notes {
  white-space: pre-line;
}

.center-content {
  display: flex;
  justify-content: center;
  padding: 20px 0;
}

.empty-data {
  padding: 20px 0;
}
</style> 