<template>
  <div class="bug-selection">
    <h3>选择关联的缺陷</h3>
    <p class="description">选择要包含在报告中的缺陷记录</p>
    
    <div class="filter-bar">
      <div class="filter-item">
        <el-select v-model="filter.severity" placeholder="严重程度" clearable>
          <el-option label="全部" value="" />
          <el-option label="致命" value="critical" />
          <el-option label="严重" value="major" />
          <el-option label="一般" value="minor" />
          <el-option label="轻微" value="trivial" />
        </el-select>
      </div>
      <div class="filter-item">
        <el-select v-model="filter.status" placeholder="状态" clearable>
          <el-option label="全部" value="" />
          <el-option label="待修复" value="open" />
          <el-option label="已修复" value="fixed" />
          <el-option label="已关闭" value="closed" />
          <el-option label="已验证" value="verified" />
          <el-option label="重新打开" value="reopened" />
        </el-select>
      </div>
      <div class="filter-item search">
        <el-input
          v-model="filter.keyword"
          placeholder="搜索缺陷标题/ID"
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
      <el-radio-group v-model="bugType" size="small" @change="handleBugTypeChange">
        <el-radio-button label="all">全部缺陷</el-radio-button>
        <el-radio-button label="open">遗留缺陷</el-radio-button>
        <el-radio-button label="fixed">已修复缺陷</el-radio-button>
      </el-radio-group>
      <div class="right-operations">
        <el-button type="primary" @click="handleSelectAll">全选</el-button>
        <el-button @click="handleUnselectAll">取消全选</el-button>
      </div>
    </div>
    
    <el-table
      ref="tableRef"
      :data="filteredBugs"
      border
      v-loading="loading"
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="bugId" label="缺陷ID" width="100" />
      <el-table-column prop="title" label="缺陷标题" min-width="180" show-overflow-tooltip />
      <el-table-column prop="severity" label="严重程度" width="100">
        <template #default="{ row }">
          <el-tag
            :type="getSeverityType(row.severity)"
            effect="dark"
            size="small"
          >
            {{ getSeverityText(row.severity) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="priority" label="优先级" width="100">
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
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)" size="small">
            {{ getStatusText(row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="creator" label="报告人" width="100" />
      <el-table-column prop="assignee" label="处理人" width="100" />
      <el-table-column prop="createTime" label="创建时间" width="160" />
    </el-table>
    
    <div class="stats-bar">
      <div class="stats-item">
        <span class="label">已选缺陷:</span>
        <span class="value">{{ selectedBugs.length }}</span>
      </div>
      <div class="stats-item">
        <span class="label">遗留:</span>
        <span class="value danger">{{ getStatusCount('open') }}</span>
      </div>
      <div class="stats-item">
        <span class="label">已修复:</span>
        <span class="value success">{{ getStatusCount('fixed') }}</span>
      </div>
      <div class="stats-item">
        <span class="label">已关闭:</span>
        <span class="value info">{{ getStatusCount('closed') }}</span>
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
  selectedBugs: {
    type: Array,
    default: () => []
  }
});

const emit = defineEmits(['bugs-selected']);

// 引用和状态
const tableRef = ref(null);
const loading = ref(false);
const bugs = ref([]);
const selectedBugs = ref([]);
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);
const bugType = ref('all'); // 缺陷类型过滤

// 过滤条件
const filter = reactive({
  severity: '',
  status: '',
  keyword: ''
});

// 获取缺陷列表
const fetchBugs = async () => {
  if (!props.planId) {
    ElMessage.warning('请先选择测试计划');
    return;
  }
  
  loading.value = true;
  try {
    // 实际应用中需要调用API
    // const response = await BugApi.getPlanBugs({
    //   planId: props.planId,
    //   page: currentPage.value,
    //   pageSize: pageSize.value,
    //   ...filter,
    //   type: bugType.value
    // });
    
    // 模拟数据
    setTimeout(() => {
      bugs.value = [
        {
          bugId: 'BUG001',
          title: '登录页面用户名输入框无法输入特殊字符',
          severity: 'minor',
          priority: '中',
          status: 'fixed',
          creator: '张三',
          assignee: '李四',
          createTime: '2023-09-10 14:30:22'
        },
        {
          bugId: 'BUG002',
          title: '重置密码邮件链接失效时间过短',
          severity: 'major',
          priority: '高',
          status: 'open',
          creator: '王五',
          assignee: '赵六',
          createTime: '2023-09-12 09:15:44'
        },
        {
          bugId: 'BUG003',
          title: '用户注册成功后未收到确认邮件',
          severity: 'critical',
          priority: '高',
          status: 'fixed',
          creator: '李四',
          assignee: '张三',
          createTime: '2023-09-15 16:45:10'
        },
        {
          bugId: 'BUG004',
          title: '移动端页面布局在某些设备上显示异常',
          severity: 'minor',
          priority: '低',
          status: 'verified',
          creator: '赵六',
          assignee: '王五',
          createTime: '2023-09-18 11:20:35'
        },
        {
          bugId: 'BUG005',
          title: '系统在高并发下偶尔出现数据不一致问题',
          severity: 'critical',
          priority: '高',
          status: 'open',
          creator: '张三',
          assignee: '李四',
          createTime: '2023-09-20 15:10:28'
        }
      ];
      total.value = 5;
      
      // 如果已有选中的缺陷，设置表格选中状态
      if (props.selectedBugs && props.selectedBugs.length > 0) {
        nextTick(() => {
          props.selectedBugs.forEach(selected => {
            const targetBug = bugs.value.find(b => b.bugId === selected.bugId);
            if (targetBug) {
              tableRef.value.toggleRowSelection(targetBug, true);
            }
          });
        });
      }
      
      loading.value = false;
    }, 500);
  } catch (error) {
    ElMessage.error('获取缺陷列表失败');
    console.error(error);
    loading.value = false;
  }
};

// 过滤后的缺陷
const filteredBugs = computed(() => {
  let result = [...bugs.value];
  
  // 按缺陷类型过滤
  if (bugType.value === 'open') {
    result = result.filter(item => ['open', 'reopened'].includes(item.status));
  } else if (bugType.value === 'fixed') {
    result = result.filter(item => ['fixed', 'verified', 'closed'].includes(item.status));
  }
  
  // 按严重程度过滤
  if (filter.severity) {
    result = result.filter(item => item.severity === filter.severity);
  }
  
  // 按状态过滤
  if (filter.status) {
    result = result.filter(item => item.status === filter.status);
  }
  
  // 按关键字过滤
  if (filter.keyword) {
    const keyword = filter.keyword.toLowerCase();
    result = result.filter(item => 
      item.bugId.toLowerCase().includes(keyword) || 
      item.title.toLowerCase().includes(keyword)
    );
  }
  
  return result;
});

// 处理搜索
const handleSearch = () => {
  currentPage.value = 1;
  fetchBugs();
};

// 处理表格选择变化
const handleSelectionChange = (selection) => {
  selectedBugs.value = selection;
  emit('bugs-selected', selection);
};

// 处理分页变化
const handleSizeChange = (val) => {
  pageSize.value = val;
  fetchBugs();
};

const handlePageChange = (val) => {
  currentPage.value = val;
  fetchBugs();
};

// 全选/取消全选
const handleSelectAll = () => {
  filteredBugs.value.forEach(row => {
    tableRef.value.toggleRowSelection(row, true);
  });
};

const handleUnselectAll = () => {
  tableRef.value.clearSelection();
};

// 处理缺陷类型变化
const handleBugTypeChange = () => {
  currentPage.value = 1;
  fetchBugs();
};

// 获取严重程度类型
const getSeverityType = (severity) => {
  const severityMap = {
    'critical': 'danger',
    'major': 'warning',
    'minor': 'info',
    'trivial': 'success'
  };
  return severityMap[severity] || 'info';
};

// 获取严重程度文本
const getSeverityText = (severity) => {
  const severityTextMap = {
    'critical': '致命',
    'major': '严重',
    'minor': '一般',
    'trivial': '轻微'
  };
  return severityTextMap[severity] || severity;
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
    'open': 'danger',
    'reopened': 'warning',
    'fixed': 'success',
    'verified': 'success',
    'closed': 'info'
  };
  return statusMap[status] || 'info';
};

// 获取状态文本
const getStatusText = (status) => {
  const statusTextMap = {
    'open': '待修复',
    'reopened': '重新打开',
    'fixed': '已修复',
    'verified': '已验证',
    'closed': '已关闭'
  };
  return statusTextMap[status] || status;
};

// 获取特定状态的缺陷数量
const getStatusCount = (status) => {
  if (status === 'open') {
    return selectedBugs.value.filter(item => ['open', 'reopened'].includes(item.status)).length;
  } else if (status === 'fixed') {
    return selectedBugs.value.filter(item => ['fixed', 'verified'].includes(item.status)).length;
  } else if (status === 'closed') {
    return selectedBugs.value.filter(item => item.status === 'closed').length;
  }
  return 0;
};

// 监听计划ID变化
watch(() => props.planId, (newVal) => {
  if (newVal) {
    fetchBugs();
  }
});

// 组件挂载时加载数据
onMounted(() => {
  if (props.planId) {
    fetchBugs();
  }
});
</script>

<style scoped>
.bug-selection {
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
  justify-content: space-between;
  margin-bottom: 15px;
}

.right-operations {
  display: flex;
  gap: 10px;
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