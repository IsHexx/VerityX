<template>
  <div class="plan-selection">
    <h3>选择测试计划</h3>
    <p class="description">请选择要生成报告的测试计划</p>
    
    <el-table
      :data="plans"
      border
      highlight-current-row
      @current-change="handleCurrentChange"
      v-loading="loading"
      style="width: 100%"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="planId" label="计划ID" width="120" />
      <el-table-column prop="planName" label="计划名称" min-width="180" />
      <el-table-column prop="version" label="版本" width="120" />
      <el-table-column prop="startDate" label="开始日期" width="160" />
      <el-table-column prop="endDate" label="结束日期" width="160" />
      <el-table-column prop="creator" label="创建人" width="120" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)">{{ row.status }}</el-tag>
        </template>
      </el-table-column>
    </el-table>
    
    <div class="pagination-container">
      <el-pagination
        v-model:currentPage="currentPage"
        v-model:pageSize="pageSize"
        :page-sizes="[10, 20, 30, 50]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handlePageChange"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import { ElMessage } from 'element-plus';

// 定义props和emits
const props = defineProps({
  selectedPlan: {
    type: String,
    default: ''
  }
});

const emit = defineEmits(['plan-selected']);

// 状态变量
const loading = ref(false);
const plans = ref([]);
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);
const currentRow = ref(null);

// 获取测试计划列表
const fetchPlans = async () => {
  loading.value = true;
  try {
    // 这里需要集成实际的API调用
    // const res = await TestPlanApi.getTestPlans({
    //   page: currentPage.value,
    //   pageSize: pageSize.value
    // });
    
    // 模拟数据
    setTimeout(() => {
      plans.value = [
        {
          planId: 'TP001',
          planName: '登录模块测试计划',
          version: 'v1.0.0',
          startDate: '2023-09-01',
          endDate: '2023-09-30',
          creator: '张三',
          status: '已完成'
        },
        {
          planId: 'TP002',
          planName: '用户管理模块测试计划',
          version: 'v1.1.0',
          startDate: '2023-10-01',
          endDate: '2023-10-31',
          creator: '李四',
          status: '进行中'
        },
        {
          planId: 'TP003',
          planName: '报表模块测试计划',
          version: 'v1.2.0',
          startDate: '2023-11-01',
          endDate: '2023-11-30',
          creator: '王五',
          status: '未开始'
        }
      ];
      total.value = 3;
      
      // 如果有选中的计划，设置当前行
      if (props.selectedPlan) {
        currentRow.value = plans.value.find(p => p.planId === props.selectedPlan);
      }
      
      loading.value = false;
    }, 500);
  } catch (error) {
    ElMessage.error('获取测试计划列表失败');
    console.error(error);
    loading.value = false;
  }
};

// 处理当前行变化
const handleCurrentChange = (row) => {
  currentRow.value = row;
  if (row) {
    emit('plan-selected', row.planId);
  }
};

// 处理分页变化
const handleSizeChange = (val) => {
  pageSize.value = val;
  fetchPlans();
};

const handlePageChange = (val) => {
  currentPage.value = val;
  fetchPlans();
};

// 获取状态类型
const getStatusType = (status) => {
  const statusMap = {
    '已完成': 'success',
    '进行中': 'primary',
    '未开始': 'info',
    '已取消': 'danger'
  };
  return statusMap[status] || 'info';
};

// 监听selectedPlan变化
watch(() => props.selectedPlan, (newVal) => {
  if (newVal && plans.value.length) {
    const row = plans.value.find(p => p.planId === newVal);
    if (row) {
      currentRow.value = row;
    }
  }
});

// 组件挂载时获取计划列表
onMounted(() => {
  fetchPlans();
});
</script>

<style scoped>
.plan-selection {
  width: 100%;
}

h3 {
  margin-top: 0;
  margin-bottom: 10px;
}

.description {
  color: #666;
  margin-bottom: 20px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style> 