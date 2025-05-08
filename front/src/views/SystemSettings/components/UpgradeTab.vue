<template>
  <div class="upgrade-container">
    <!-- 当前版本信息卡片 -->
    <el-card class="mb-4">
      <template #header>
        <div class="card-header">
          <span>当前版本信息</span>
          <el-button 
            v-if="hasPermission"
            type="primary" 
            size="small" 
            @click="handleUpgrade"
          >
            执行升级
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
          <span class="label">当前版本：</span>
          <span class="value">{{ currentVersion.version }}</span>
        </div>
        <div class="detail-item">
          <span class="label">发布日期：</span>
          <span class="value">{{ formatDate(currentVersion.releaseDate) }}</span>
        </div>
        <div v-if="latestUpgrade" class="detail-item">
          <span class="label">最近升级：</span>
          <span class="value">{{ formatDate(latestUpgrade.upgradeTime) }}，从 {{ latestUpgrade.fromVersion }} 升级到 {{ latestUpgrade.toVersion }}</span>
        </div>
      </div>
    </el-card>
    
    <!-- 升级记录卡片 -->
    <el-card>
      <template #header>
        <div class="card-header">
          <span>升级记录</span>
          <el-select v-model="status" placeholder="过滤状态" style="width: 120px" @change="handleStatusChange">
            <el-option label="全部" value="" />
            <el-option label="成功" value="成功" />
            <el-option label="失败" value="失败" />
            <el-option label="进行中" value="进行中" />
          </el-select>
        </div>
      </template>
      
      <div v-if="recordLoading" class="center-content">
        <el-skeleton animated :rows="5" />
      </div>
      
      <div v-else-if="!recordList || recordList.length === 0" class="empty-data">
        <el-empty description="暂无升级记录" />
      </div>
      
      <el-table v-else :data="recordList" style="width: 100%">
        <el-table-column label="升级时间" width="180">
          <template #default="scope">
            {{ formatDate(scope.row.upgradeTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="fromVersion" label="原版本" width="120" />
        <el-table-column prop="toVersion" label="目标版本" width="120" />
        <el-table-column prop="upgradeUser" label="操作用户" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="duration" label="耗时" width="100">
          <template #default="scope">
            {{ scope.row.duration ? scope.row.duration + '秒' : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="remarks" label="备注" show-overflow-tooltip />
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button 
              type="primary" 
              size="small" 
              plain
              @click="handleViewDetail(scope.row)"
            >
              详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="pagination-container">
        <el-pagination
          background
          layout="total, prev, pager, next"
          :total="total"
          :current-page="currentPage"
          :page-size="pageSize"
          @current-change="handlePageChange"
        />
      </div>
    </el-card>
    
    <!-- 执行升级对话框 -->
    <el-dialog
      v-model="upgradeDialogVisible"
      title="执行系统升级"
      width="500px"
    >
      <el-form
        ref="upgradeFormRef"
        :model="upgradeForm"
        :rules="upgradeRules"
        label-width="100px"
      >
        <el-form-item label="目标版本" prop="toVersion">
          <el-input v-model="upgradeForm.toVersion" placeholder="请输入升级目标版本号" />
        </el-form-item>
        <el-form-item label="执行用户" prop="upgradeUser">
          <el-input v-model="upgradeForm.upgradeUser" placeholder="请输入执行升级的用户名" />
        </el-form-item>
        <el-form-item label="创建备份" prop="createBackup">
          <el-switch v-model="upgradeForm.createBackup" />
        </el-form-item>
        <el-form-item label="备份说明" prop="backupRemark" v-if="upgradeForm.createBackup">
          <el-input 
            v-model="upgradeForm.backupRemark" 
            type="textarea" 
            :rows="2" 
            placeholder="请输入备份说明"
          />
        </el-form-item>
        <el-form-item label="升级说明" prop="remarks">
          <el-input 
            v-model="upgradeForm.remarks" 
            type="textarea" 
            :rows="3" 
            placeholder="请输入升级说明"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="upgradeDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitUpgrade" :loading="upgradeLoading">
            开始升级
          </el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 升级详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="升级详情"
      width="650px"
    >
      <div v-if="detailLoading" class="center-content">
        <el-skeleton animated :rows="8" />
      </div>
      
      <div v-else-if="!upgradeDetail" class="empty-data">
        <el-empty description="无法获取升级详情" />
      </div>
      
      <div v-else>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="升级时间">{{ formatDate(upgradeDetail.upgradeTime) }}</el-descriptions-item>
          <el-descriptions-item label="操作用户">{{ upgradeDetail.upgradeUser || '-' }}</el-descriptions-item>
          <el-descriptions-item label="原版本">{{ upgradeDetail.fromVersion }}</el-descriptions-item>
          <el-descriptions-item label="目标版本">{{ upgradeDetail.toVersion }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusType(upgradeDetail.status)">{{ upgradeDetail.status }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="耗时">{{ upgradeDetail.duration ? upgradeDetail.duration + '秒' : '-' }}</el-descriptions-item>
          <el-descriptions-item label="关联备份" :span="2">
            <template v-if="upgradeDetail.backup">
              <div>ID: {{ upgradeDetail.backup.id }}</div>
              <div>时间: {{ formatDate(upgradeDetail.backup.createTime) }}</div>
              <div>说明: {{ upgradeDetail.backup.description || '-' }}</div>
            </template>
            <span v-else>无关联备份</span>
          </el-descriptions-item>
          <el-descriptions-item label="升级说明" :span="2">{{ upgradeDetail.remarks || '-' }}</el-descriptions-item>
        </el-descriptions>
        
        <div v-if="upgradeDetail.errorMessage" class="error-message">
          <div class="section-title">错误信息</div>
          <pre class="error-content">{{ upgradeDetail.errorMessage }}</pre>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { SysInfoApi } from '@/api/sysInfoService'
import { ElMessage, ElMessageBox } from 'element-plus'
import { checkPermission } from '@/utils/permission'
import { formatDateTime } from '@/utils/date'

// 数据状态
const currentVersion = ref(null)
const latestUpgrade = ref(null)
const recordList = ref([])
const upgradeDetail = ref(null)
const loading = ref(false)
const recordLoading = ref(false)
const detailLoading = ref(false)
const upgradeLoading = ref(false)
const upgradeDialogVisible = ref(false)
const detailDialogVisible = ref(false)
const hasPermission = checkPermission('system:info:upgrade')

// 分页状态
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const status = ref('')

// 升级表单
const upgradeFormRef = ref(null)
const upgradeForm = reactive({
  toVersion: '',
  upgradeUser: '',
  createBackup: true,
  backupRemark: '',
  remarks: ''
})

// 升级表单验证规则
const upgradeRules = {
  toVersion: [
    { required: true, message: '请输入目标版本号', trigger: 'blur' },
    { max: 50, message: '版本号不能超过50个字符', trigger: 'blur' }
  ],
  upgradeUser: [
    { required: true, message: '请输入执行升级的用户名', trigger: 'blur' },
    { max: 100, message: '用户名不能超过100个字符', trigger: 'blur' }
  ]
}

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

// 获取最近一次升级记录
const fetchLatestUpgrade = async () => {
  try {
    const res = await SysInfoApi.getLatestUpgradeRecord()
    console.log('最近升级记录数据:', res)
    // 直接使用data字段的数据
    latestUpgrade.value = res.data
  } catch (error) {
    console.error('获取最近升级记录失败', error)
  }
}

// 获取升级记录列表
const fetchUpgradeRecords = async () => {
  recordLoading.value = true
  try {
    const res = await SysInfoApi.getUpgradeRecordList(
      currentPage.value,
      pageSize.value,
      status.value
    )
    console.log('升级记录列表数据:', res)
    
    if (res.code === 200) {
      // 获取分页数据正确的结构
      recordList.value = res.data.records || []
      total.value = res.data.total || 0
    } else {
      ElMessage.error(res.message || '获取升级记录失败')
    }
  } catch (error) {
    ElMessage.error('获取升级记录失败')
    console.error(error)
  } finally {
    recordLoading.value = false
  }
}

// 获取升级详情
const fetchUpgradeDetail = async (id) => {
  detailLoading.value = true
  upgradeDetail.value = null
  
  try {
    const res = await SysInfoApi.getUpgradeRecordDetail(id)
    console.log('升级详情数据:', res)
    
    if (res.code === 200) {
      // 直接使用data字段的数据
      upgradeDetail.value = res.data
    } else {
      ElMessage.error(res.message || '获取升级详情失败')
    }
  } catch (error) {
    ElMessage.error('获取升级详情失败')
    console.error(error)
  } finally {
    detailLoading.value = false
  }
}

// 执行升级
const handleUpgrade = () => {
  upgradeForm.toVersion = ''
  upgradeForm.upgradeUser = ''
  upgradeForm.createBackup = true
  upgradeForm.backupRemark = ''
  upgradeForm.remarks = ''
  
  upgradeDialogVisible.value = true
}

// 提交升级
const submitUpgrade = async () => {
  if (!upgradeFormRef.value) return
  
  await upgradeFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        upgradeLoading.value = true
        
        // 确认是否继续
        try {
          await ElMessageBox.confirm(
            `确定要将系统从 ${currentVersion.value?.version || '当前版本'} 升级到 ${upgradeForm.toVersion} 吗？此操作不可逆。`,
            '升级确认',
            {
              confirmButtonText: '确定升级',
              cancelButtonText: '取消',
              type: 'warning'
            }
          )
        } catch (e) {
          // 用户取消
          upgradeLoading.value = false
          return
        }
        
        const { data } = await SysInfoApi.executeUpgrade(upgradeForm)
        
        if (data.code === 200) {
          ElMessage.success('系统升级任务已提交，请等待升级完成')
          upgradeDialogVisible.value = false
          
          // 刷新数据
          await fetchCurrentVersion()
          await fetchLatestUpgrade()
          await fetchUpgradeRecords()
        } else {
          ElMessage.error(data.message || '提交升级任务失败')
        }
      } catch (error) {
        ElMessage.error('提交升级任务失败')
        console.error(error)
      } finally {
        upgradeLoading.value = false
      }
    }
  })
}

// 查看详情
const handleViewDetail = (row) => {
  fetchUpgradeDetail(row.id)
  detailDialogVisible.value = true
}

// 状态变更
const handleStatusChange = () => {
  currentPage.value = 1
  fetchUpgradeRecords()
}

// 页码变更
const handlePageChange = (page) => {
  currentPage.value = page
  fetchUpgradeRecords()
}

// 获取状态类型
const getStatusType = (status) => {
  switch (status) {
    case '成功':
      return 'success'
    case '失败':
      return 'danger'
    case '进行中':
      return 'warning'
    default:
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
  await fetchCurrentVersion()
  await fetchLatestUpgrade()
  await fetchUpgradeRecords()
})

// 暴露给父组件的方法，用于在标签切换时刷新数据
const fetchData = async () => {
  await fetchCurrentVersion()
  await fetchLatestUpgrade()
  await fetchUpgradeRecords()
}

// 将方法暴露给父组件
defineExpose({
  fetchData,
  fetchCurrentVersion,
  fetchLatestUpgrade,
  fetchUpgradeRecords
})
</script>

<style scoped>
.upgrade-container {
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

.center-content {
  display: flex;
  justify-content: center;
  padding: 20px 0;
}

.empty-data {
  padding: 20px 0;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.error-message {
  margin-top: 20px;
  border-top: 1px solid #EBEEF5;
  padding-top: 20px;
}

.section-title {
  font-weight: bold;
  margin-bottom: 10px;
  color: #E6A23C;
}

.error-content {
  background-color: #FEF0F0;
  padding: 10px;
  border: 1px solid #FCDEE2;
  border-radius: 4px;
  color: #F56C6C;
  white-space: pre-wrap;
  font-family: monospace;
  max-height: 300px;
  overflow: auto;
}
</style> 