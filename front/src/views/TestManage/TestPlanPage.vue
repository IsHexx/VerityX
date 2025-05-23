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
        <el-table-column fixed="right" label="操作" min-width="180">
        <template #default="{ row }">
          <el-button link type="primary" size="small" @click="handleViewDetail(row)">
            详情
          </el-button>
          <el-button link type="primary" size="small" @click="handleEditTestplan(row)">
            编辑
          </el-button>
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
              <el-option label="项目A" value="01" />
              <el-option label="项目B" value="02" />
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
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import PaginationPage from '@/components/PaginationPage.vue'
import { testplanApi } from '@/api/testplanService'
import { useProjectStore } from '@/store/projectStore'
import axios from "axios";

// 使用路由
const router = useRouter();

// 使用项目Store
const projectStore = useProjectStore();
// 确保初始化项目状态
projectStore.initProjectState();

// 计算当前项目ID
const currentProjectId = computed(() => projectStore.getCurrentProjectId());

// 状态定义
const activeTab = ref('all_plan')
const dialogVisible = ref(false)
const dialogTitle = ref('')
const testplanFormRef = ref(null)
const testplans = ref([])
const projectOptions = ref([]) // 项目选项列表

// 表单数据，默认使用当前项目ID
const form = reactive({
  planId: '',
  planName: '',
  planVersion: '',
  status: '',
  participants: '',
  startDate: '',
  endDate: '',
  description: '',
  projectId: currentProjectId.value || '' // 默认使用当前项目ID
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
    projectId: currentProjectId.value || '' // 默认使用当前项目ID
  })
}

// 获取项目列表
const fetchProjects = async () => {
  try {
    // 设置当前项目为选项
    if (currentProjectId.value) {
      const currentProject = {
        value: currentProjectId.value,
        label: projectStore.currentProject.value?.projectName || `项目 ${currentProjectId.value}`
      };
      projectOptions.value = [currentProject];
    } else {
      projectOptions.value = [];
    }
  } catch (error) {
    console.error('获取项目列表失败:', error);
  }
};

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
// 查看测试计划详情
const handleViewDetail = (row) => {
  router.push(`/testplan/${row.planId}`);
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
  // 映射标签页的值到状态
  const tabStatusMap = {
    all_plan: '', // 默认查询所有
    under_plan: 'Active',
    over_plan: 'Completed',
  };
  console.log('tab.props.name', tab.props.name);
  // 根据选中的标签页动态设置状态
  activeTab.value = tab.props.name;
  console.log('activeTab.value:', activeTab.value);
  const status = tabStatusMap[tab.props.name] || '';
  console.log('fetchTestplan调用前status:', status);

  pagination.page = 1; // 切换标签时重置到第一页
  fetchTestplan(status); // 传入状态进行数据加载
};


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

const fetchTestplan = async (status = '') => {

  loading.value = true;
  try {
    const res = await testplanApi.getTestplans({
      page: pagination.page,
      pageSize: pagination.pageSize,
      status: status, // 根据状态动态加载数据
    });
    console.log('获取到的测试计划数据:', res);

    // 修正数据绑定，使用res.data.list而不是res.data.data
    testplans.value = res.data.list || [];
    total.value = res.data.total || 0;

  } catch (error) {
    ElMessage.error('获取计划列表失败');
    console.error('获取计划列表失败:', error);
  } finally {
    loading.value = false;
  }
};

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