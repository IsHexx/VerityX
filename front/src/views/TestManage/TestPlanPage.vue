<template>
  <div class="flex flex-wrap gap-4">
    <el-card shadow="always" class="w-full">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-tabs v-model="activeTab" @tab-click="handleTabClick">
            <el-tab-pane label="全部" name="all_plan"></el-tab-pane>
            <el-tab-pane label="测试中测试单" name="under_plan"></el-tab-pane>
            <el-tab-pane label="已完成测试" name="over_plan"></el-tab-pane>
          </el-tabs>
        </el-col>
        <el-col :span="18" style="text-align: right">
          <el-button type="primary" @click="handleAddTestplan">添加计划</el-button>
        </el-col>
      </el-row>

      <el-table
        :data="testplans"
        border
        v-loading="loading"
        style="width: 100%; min-height: 560px"
        class="customer-table"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="planName" label="名称" min-width="150" />
        <el-table-column prop="planVersion" label="版本" min-width="220" />
        <el-table-column prop="status" label="状态" min-width="220" />
        <el-table-column prop="participants" label="参与人" min-width="220" />
        <el-table-column prop="startDate" label="开始日期" min-width="220" />
        <el-table-column prop="endDate" label="结束日期" min-width="220" />
        <el-table-column fixed="right" label="操作" min-width="120">
        <template #default="{ row }">
          <el-popconfirm
            title="确认删除该测试计划吗?"
            @confirm="handleDelete(row)"
            confirm-button-text="确定"
            cancel-button-text="取消"
          >
            <template #reference>
              <el-button link type="primary" size="small">删除</el-button>
            </template>
          </el-popconfirm>
          <el-button link type="primary" size="small" @click="handleEditTestplan(row)">
            编辑
          </el-button>
        </template>
      </el-table-column>
      </el-table>

      <el-dialog
        v-model="dialogVisible"
        :title="dialogTitle || '添加计划'"
        width="650px"
      >
        <el-form
          ref="testplanFormRef"
          :model="form"
          label-width="auto"
          style="max-width: 600px"
        >
          <el-form-item label="计划名称">
            <el-input v-model="form.planName" placeholder="请输入计划名称" />
          </el-form-item>

          <el-form-item label="所属项目">
            <el-select v-model="form.projectId" placeholder="请选择所属项目">
              <el-option label="项目A" value="shanghai" />
              <el-option label="项目B" value="beijing" />
            </el-select>
          </el-form-item>

          <el-form-item label="状态">
            <el-select v-model="form.status" placeholder="请选择计划状态">
              <el-option label="待审批" value="01" />
              <el-option label="待执行" value="02" />
            </el-select>
          </el-form-item>

          <el-form-item label="版本">
            <el-input v-model="form.planVersion" placeholder="请输入版本号" />
          </el-form-item>

          <el-form-item label="参与人">
            <el-input v-model="form.participants" placeholder="请输入参与人" />
          </el-form-item>

          <el-form-item label="日期">
            <el-col :span="11">
              <el-date-picker
                v-model="form.startDate"
                type="date"
                placeholder="开始时间"
                value-format="YYYY-MM-DD"
                style="width: 100%"
              />
            </el-col>
            <el-col :span="2" class="text-center">
              <span class="text-gray-500">-</span>
            </el-col>
            <el-col :span="11">
              <el-date-picker
                v-model="form.endDate"
                type="date"
                placeholder="结束时间"
                value-format="YYYY-MM-DD"
                style="width: 100%"
              />
            </el-col>
          </el-form-item>

          <el-form-item label="描述">
            <el-input
              v-model="form.description"
              type="textarea"
              placeholder="请输入描述"
            />
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="onSubmit">
              {{ form.planId ? '保存' : '创建' }}
            </el-button>
            <el-button @click="dialogVisible = false">取消</el-button>
          </el-form-item>
        </el-form>
      </el-dialog>

      <PaginationPage 
        :total="total" 
        @update:pagination="handlePaginationChange"
      />
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import PaginationPage from '@/components/PaginationPage.vue'
import { testplanApi } from '@/api/testplanService'
import axios from "axios";

// 状态定义
const activeTab = ref('all_plan')
const dialogVisible = ref(false)
const dialogTitle = ref('')
const testplanFormRef = ref(null)
const testplans = ref([])

// 表单数据
const form = reactive({
  planId: '',
  planName: '',
  planVersion: '',
  status: '',
  participants: '',
  startDate: '',
  endDate: '',
  description: '',
  projectId: ''
})

// 重置表单
const resetForm = () => {
  Object.assign(form, {
    planId: '',
    planName: '',
    planVersion: '',
    status: '',
    participants: '',
    startDate: '',
    endDate: '',
    description: '',
    projectId: ''
  })
}

// 添加计划
const handleAddTestplan = () => {
  dialogTitle.value = '添加计划'
  resetForm()
  dialogVisible.value = true
}

// 编辑计划
const handleEditTestplan = (row) => {
  console.log('正在编辑的testplans', row.planName)
  dialogTitle.value = '编辑测试计划'
  Object.assign(form, {
    planId: row.planId,
    planName: row.planName,
    planVersion: row.planVersion,
    status: row.status,
    participants: row.participants,
    startDate: row.startDate,
    endDate: row.endDate,
    description: row.description,
    projectId: row.projectId
  })
  dialogVisible.value = true
}
// 删除测试计划
const handleDelete = async (row) => {
  try {
    await testplanApi.deleteTestplan(row.planId)
    ElMessage({
      type: 'success',
      message: '删除成功'
    })
    // 刷新列表
    await fetchTestplan()
  } catch (error) {
    ElMessage({
      type: 'error',
      message: error.message || '删除失败'
    })
    console.error('删除失败:', error)
  }
}

// 修改标签页切换处理
const handleTabClick = (tab) => {
  pagination.page = 1 // 切换标签时重置到第一页
  fetchTestplan()
}

// 提交表单
const onSubmit = async () => {
  if (!testplanFormRef.value) return
  
  try {
    const data = {
      projectId: form.projectId || '1',
      planName: form.planName,
      description: form.description,
      planVersion: form.planVersion,
      status: form.status,
      startDate: form.startDate,
      endDate: form.endDate,
      participants: form.participants
    }

    if (form.planId) {
      await testplanApi.updateTestplan(form.planId, data)
      ElMessage.success('计划更新成功')
    } else {
      await testplanApi.createTestplan(data)
      ElMessage.success('计划创建成功')
    }

    dialogVisible.value = false
    await fetchTestplan()
  } catch (error) {
    ElMessage.error(error.message || '保存失败')
    console.error('保存失败:', error)
  }
}

// 修改获取测试计划列表方法
const fetchTestplan = async () => {
  loading.value = true
  try {
    const res = await testplanApi.getTestplans({
      page: pagination.page,
      pageSize: pagination.pageSize,
      // 可以添加其他查询参数，比如状态筛选
      status: activeTab.value === 'all_plan' ? '' : activeTab.value
    })
    testplans.value = res.data.list // 假设后端返回 { list: [], total: number }
    total.value = res.data.total
  } catch (error) {
    ElMessage.error('获取计划列表失败')
    console.error('获取计划列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 新增分页相关状态
const loading = ref(false)
const total = ref(0)
const pagination = reactive({
  page: 1,
  pageSize: 10
})

// 处理分页变化
const handlePaginationChange = ({ page, pageSize }) => {
  pagination.page = page
  pagination.pageSize = pageSize
  fetchTestplan()
}

// 组件挂载时获取数据
onMounted(() => {
  fetchTestplan()
})
</script>

<style scoped>
.customer-table :deep(.el-table__cell) {
  border-right: none;
}
</style>