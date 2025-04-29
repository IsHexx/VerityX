<template>
  <div class="case-selection">
    <h3>关联测试用例</h3>
    <p class="description">选择要包含在报告中的测试用例</p>
    
    <div class="filter-bar">
      <div class="filter-item">
        <el-select v-model="filter.status" placeholder="用例状态" clearable>
          <el-option label="全部" value="" />
          <el-option label="通过" value="passed" />
          <el-option label="失败" value="failed" />
          <el-option label="阻塞" value="blocked" />
          <el-option label="跳过" value="skipped" />
        </el-select>
      </div>
      <div class="filter-item">
        <el-select v-model="filter.priority" placeholder="优先级" clearable>
          <el-option label="全部" value="" />
          <el-option label="高" value="high" />
          <el-option label="中" value="medium" />
          <el-option label="低" value="low" />
        </el-select>
      </div>
      <div class="filter-item search">
        <el-input
          v-model="filter.keyword"
          placeholder="搜索用例名称/ID"
          clearable
          @keyup.enter="handleSearch"
        >
          <template #suffix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
      </div>
    </div>
    
    <div class="operation-bar">
      <el-button type="primary" @click="handleSelectAll">全选</el-button>
      <el-button @click="handleUnselectAll">取消全选</el-button>
      <el-button type="success" @click="handleFilterPassed">只选通过用例</el-button>
      <el-button type="danger" @click="handleFilterFailed">只选失败用例</el-button>
    </div>
    
    <el-table
      ref="tableRef"
      :data="filteredCases"
      border
      v-loading="loading"
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="caseId" label="用例ID" width="100" />
      <el-table-column prop="caseName" label="用例名称" min-width="180" />
      <el-table-column prop="module" label="所属模块" width="120" />
      <el-table-column prop="priority" label="优先级" width="80">
        <template #default="{ row }">
          <el-tag
            :type="getPriorityType(row.priority)"
            effect="plain"
            size="small"
          >
            {{ row.priority }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="执行状态" width="100">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)" size="small">
            {{ getStatusText(row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="executor" label="执行人" width="100" />
      <el-table-column prop="executeTime" label="执行时间" width="160" />
    </el-table>
    
    <div class="stats-bar">
      <div class="stats-item">
        <span class="label">已选用例:</span>
        <span class="value">{{ selectedCases.length }}</span>
      </div>
      <div class="stats-item">
        <span class="label">通过:</span>
        <span class="value success">{{ getStatusCount('passed') }}</span>
      </div>
      <div class="stats-item">
        <span class="label">失败:</span>
        <span class="value danger">{{ getStatusCount('failed') }}</span>
      </div>
      <div class="stats-item">
        <span class="label">阻塞:</span>
        <span class="value warning">{{ getStatusCount('blocked') }}</span>
      </div>
      <div class="stats-item">
        <span class="label">跳过:</span>
        <span class="value info">{{ getStatusCount('skipped') }}</span>
      </div>
    </div>
    
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
import { ref, reactive, computed, watch, onMounted } from 'vue';
import { Search } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';

const props = defineProps({
  planId: {
    type: String,
    required: true
  },
  selectedCases: {
    type: Array,
    default: () => []
  }
});

const emit = defineEmits(['cases-selected']);

// 引用和状态
const tableRef = ref(null);
const loading = ref(false);
const testCases = ref([]);
const selectedCases = ref([]);
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);

// 过滤条件
const filter = reactive({
  status: '',
  priority: '',
  keyword: ''
});

// 获取用例列表
const fetchTestCases = async () => {
  if (!props.planId) {
    ElMessage.warning('请先选择测试计划');
    return;
  }
  
  loading.value = true;
  try {
    // 实际应用中需要调用API
    // const response = await TestCaseApi.getPlanCases({
    //   planId: props.planId,
    //   page: currentPage.value,
    //   pageSize: pageSize.value,
    //   ...filter
    // });
    
    // 模拟数据
    setTimeout(() => {
      testCases.value = [
        {
          caseId: 'TC001',
          caseName: '验证用户登录功能 - 有效凭据',
          module: '用户登录',
          priority: '高',
          status: 'passed',
          executor: '张三',
          executeTime: '2023-09-15 10:30:45'
        },
        {
          caseId: 'TC002',
          caseName: '验证用户登录功能 - 无效凭据',
          module: '用户登录',
          priority: '高',
          status: 'passed',
          executor: '张三',
          executeTime: '2023-09-15 10:35:22'
        },
        {
          caseId: 'TC003',
          caseName: '验证密码重置功能',
          module: '用户登录',
          priority: '中',
          status: 'failed',
          executor: '李四',
          executeTime: '2023-09-16 14:20:10'
        },
        {
          caseId: 'TC004',
          caseName: '验证用户注册 - 有效数据',
          module: '用户注册',
          priority: '高',
          status: 'passed',
          executor: '王五',
          executeTime: '2023-09-17 09:15:33'
        },
        {
          caseId: 'TC005',
          caseName: '验证用户注册 - 无效数据',
          module: '用户注册',
          priority: '中',
          status: 'blocked',
          executor: '王五',
          executeTime: '2023-09-17 09:45:18'
        }
      ];
      total.value = 5;
      
      // 如果已有选中的用例，设置表格选中状态
      if (props.selectedCases && props.selectedCases.length > 0) {
        nextTick(() => {
          props.selectedCases.forEach(selected => {
            const targetCase = testCases.value.find(c => c.caseId === selected.caseId);
            if (targetCase) {
              tableRef.value.toggleRowSelection(targetCase, true);
            }
          });
        });
      }
      
      loading.value = false;
    }, 500);
  } catch (error) {
    ElMessage.error('获取测试用例失败');
    console.error(error);
    loading.value = false;
  }
};

// 过滤后的用例
const filteredCases = computed(() => {
  let result = [...testCases.value];
  
  if (filter.status) {
    result = result.filter(item => item.status === filter.status);
  }
  
  if (filter.priority) {
    result = result.filter(item => item.priority === filter.priority);
  }
  
  if (filter.keyword) {
    const keyword = filter.keyword.toLowerCase();
    result = result.filter(item => 
      item.caseId.toLowerCase().includes(keyword) || 
      item.caseName.toLowerCase().includes(keyword)
    );
  }
  
  return result;
});

// 处理搜索
const handleSearch = () => {
  currentPage.value = 1;
  fetchTestCases();
};

// 处理表格选择变化
const handleSelectionChange = (selection) => {
  selectedCases.value = selection;
  emit('cases-selected', selection);
};

// 处理分页变化
const handleSizeChange = (val) => {
  pageSize.value = val;
  fetchTestCases();
};

const handlePageChange = (val) => {
  currentPage.value = val;
  fetchTestCases();
};

// 全选/取消全选
const handleSelectAll = () => {
  testCases.value.forEach(row => {
    tableRef.value.toggleRowSelection(row, true);
  });
};

const handleUnselectAll = () => {
  tableRef.value.clearSelection();
};

// 只选择通过/失败的用例
const handleFilterPassed = () => {
  tableRef.value.clearSelection();
  const passedCases = testCases.value.filter(item => item.status === 'passed');
  passedCases.forEach(row => {
    tableRef.value.toggleRowSelection(row, true);
  });
};

const handleFilterFailed = () => {
  tableRef.value.clearSelection();
  const failedCases = testCases.value.filter(item => item.status === 'failed');
  failedCases.forEach(row => {
    tableRef.value.toggleRowSelection(row, true);
  });
};

// 获取优先级类型
const getPriorityType = (priority) => {
  const priorityMap = {
    '高': 'danger',
    '中': 'warning',
    '低': 'info'
  };
  return priorityMap[priority] || 'info';
};

// 获取状态类型
const getStatusType = (status) => {
  const statusMap = {
    'passed': 'success',
    'failed': 'danger',
    'blocked': 'warning',
    'skipped': 'info'
  };
  return statusMap[status] || 'info';
};

// 获取状态文本
const getStatusText = (status) => {
  const statusTextMap = {
    'passed': '通过',
    'failed': '失败',
    'blocked': '阻塞',
    'skipped': '跳过'
  };
  return statusTextMap[status] || status;
};

// 获取特定状态的用例数量
const getStatusCount = (status) => {
  return selectedCases.value.filter(item => item.status === status).length;
};

// 监听计划ID变化
watch(() => props.planId, (newVal) => {
  if (newVal) {
    fetchTestCases();
  }
});

// 组件挂载时加载数据
onMounted(() => {
  if (props.planId) {
    fetchTestCases();
  }
});
</script>

<style scoped>
.case-selection {
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

.filter-bar {
  display: flex;
  margin-bottom: 15px;
  gap: 15px;
}

.filter-item {
  min-width: 150px;
}

.filter-item.search {
  flex-grow: 1;
}

.operation-bar {
  display: flex;
  gap: 10px;
  margin-bottom: 15px;
}

.stats-bar {
  margin-top: 15px;
  display: flex;
  gap: 20px;
  align-items: center;
}

.stats-item {
  display: flex;
  align-items: center;
}

.stats-item .label {
  margin-right: 5px;
  color: #606266;
}

.stats-item .value {
  font-weight: bold;
}

.stats-item .success {
  color: #67c23a;
}

.stats-item .danger {
  color: #f56c6c;
}

.stats-item .warning {
  color: #e6a23c;
}

.stats-item .info {
  color: #909399;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style> 