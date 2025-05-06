<template>
  <div class="flex flex-wrap gap-4">
    <el-card shadow="always" class="w-full">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-tabs v-model="activeTab" type="" @tab-click="handleTabClick">
            <el-tab-pane label="全部" name="all_cases"></el-tab-pane>
            <el-tab-pane label="已执行" name="executed"></el-tab-pane>
            <el-tab-pane label="未执行" name="unexecuted"></el-tab-pane>
          </el-tabs>
        </el-col>
        <el-col :span="18" style="text-align: right; align-content: center">
          <el-input
            v-model="searchKeyword"
            style="width: 240px"
            :placeholder="placeholderText"
            :suffix-icon="Search"
            @focus="handleFocus"
            @blur="handleBlur"
            @keyup.enter="handleSearch"
          />
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button type="primary" @click="handleAddTestCase">+ 添加用例</el-button>
        </el-col>
      </el-row>
      
      <el-table
        :data="uiTestCaseData"
        :border="true"
        v-loading="loading"
        style="width: 100%; min-height: 560px"
        class="customer-table"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="caseId" label="ID" min-width="100" />
        <el-table-column prop="caseTitle" label="用例标题" min-width="150" />
        <el-table-column prop="importanceLevel" label="优先级" min-width="100">
          <template #default="scope">
            <el-tag :type="getPriorityTagType(scope.row.importanceLevel)" :effect="'light'">
              {{ scope.row.importanceLevel }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="caseStatus" label="执行状态" min-width="120">
          <template #default="scope">
            <el-tag
              :type="getStatusTagType(scope.row.caseStatus)"
              :effect="'light'"
            >
              {{ scope.row.caseStatus }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastResult" label="最后执行结果" min-width="120">
          <template #default="scope">
            <el-tag
              :type="getResultTagType(scope.row.lastResult)"
              :effect="'light'"
            >
              {{ scope.row.lastResult }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdBy" label="创建人" min-width="120" />
        <el-table-column prop="createdAt" label="创建日期" min-width="150" />
        <el-table-column prop="lastRunAt" label="最后执行时间" min-width="150" />
        <el-table-column prop="stepCount" label="步骤数" min-width="100" />
        
        <el-table-column fixed="right" label="操作" min-width="150">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleEditTestCase(row)">
              编辑
            </el-button>
            <el-button link type="primary" size="small" @click="handleRunTestCase(row)">
              执行
            </el-button>
            <el-popconfirm
              title="确认删除该测试用例吗?"
              @confirm="handleDeleteTestCase(row)"
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

      <!-- 用例详情/编辑弹窗 -->
      <el-dialog
        v-model="dialogVisible"
        :title="dialogTitle"
        width="650px"
      >
        <el-form :model="testCaseForm" label-width="auto">
          <el-form-item label="用例标题" required>
            <el-input v-model="testCaseForm.caseTitle" placeholder="请输入用例标题" />
          </el-form-item>
          <el-form-item label="所属项目">
            <el-select v-model="testCaseForm.projectId" placeholder="请选择所属项目" style="width: 100%">
              <el-option 
                v-for="item in projectOptions" 
                :key="item.value" 
                :label="item.label" 
                :value="item.value" 
              />
            </el-select>
          </el-form-item>
          <el-form-item label="优先级">
            <el-select v-model="testCaseForm.importanceLevel" placeholder="请选择优先级" style="width: 100%">
              <el-option label="P0 - 最高" value="P0" />
              <el-option label="P1 - 高" value="P1" />
              <el-option label="P2 - 中" value="P2" />
              <el-option label="P3 - 低" value="P3" />
            </el-select>
          </el-form-item>
          <el-form-item label="测试环境">
            <el-select v-model="testCaseForm.environment" placeholder="请选择测试环境" style="width: 100%">
              <el-option label="Chrome" value="Chrome" />
              <el-option label="Firefox" value="Firefox" />
              <el-option label="Edge" value="Edge" />
              <el-option label="Safari" value="Safari" />
            </el-select>
          </el-form-item>
          <el-form-item label="前置条件">
            <el-input
              v-model="testCaseForm.precondition"
              type="textarea"
              placeholder="请输入测试前置条件"
            />
          </el-form-item>
          <el-form-item label="用例描述">
            <el-input
              v-model="testCaseForm.description"
              type="textarea"
              placeholder="请输入用例描述"
            />
          </el-form-item>
          <el-row type="flex" justify="end">
            <el-button type="primary" @click="onSubmit">确定</el-button>
            <el-button @click="dialogVisible = false">取消</el-button>
          </el-row>
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
import PaginationPage from "@/components/PaginationPage.vue";
import { ref, reactive, onMounted } from "vue";
import { Search } from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";
import { UiTestCaseApi } from "@/api/uiTestCaseService";

// 状态管理
const loading = ref(false);
const dialogVisible = ref(false);
const dialogTitle = ref("添加用例");
const placeholderText = ref("输入查询关键字⏎");
const searchKeyword = ref("");
const activeTab = ref("all_cases");
const uiTestCaseData = ref([]);
const total = ref(0);

// 分页
const pagination = reactive({
  page: 1,
  pageSize: 10,
});

// 项目选项
const projectOptions = ref([
  { value: '1', label: '电商系统项目' },
  { value: '2', label: '金融系统项目' },
  { value: '3', label: '社交媒体项目' }
]);

// 表单数据
const testCaseForm = reactive({
  id: "",
  caseTitle: "",
  projectId: "",
  importanceLevel: "P2",
  environment: "Chrome",
  precondition: "",
  description: "",
  steps: [],
  timeout: 30,
  retryCount: 1,
  screenshotPolicy: "failureOnly",
  parameters: []
});

// 重置表单
const resetForm = () => {
  Object.assign(testCaseForm, {
    id: "",
    caseTitle: "",
    projectId: "",
    importanceLevel: "P2",
    environment: "Chrome",
    precondition: "",
    description: "",
    steps: [],
    timeout: 30,
    retryCount: 1,
    screenshotPolicy: "failureOnly",
    parameters: []
  });
};

// 获取用例列表
const fetchTestCaseList = async (tabName = activeTab.value, page = pagination.page, pageSize = pagination.pageSize, keyword = searchKeyword.value) => {
  loading.value = true;
  try {
    // 根据tab名称确定status值
    let status = null;
    if (tabName === 'executed') {
      console.log("加载已执行用例数据");
      status = 'executed';
    } else if (tabName === 'unexecuted') {
      console.log("加载未执行用例数据");
      status = 'unexecuted';
    } else {
      console.log("加载全部用例数据");
    }

    // 构造请求参数
    const params = { page, pageSize };
    if (keyword) params.keyword = keyword;
    if (status) params.status = status;

    console.log("请求参数:", params);

    const res = await UiTestCaseApi.getUiTestCaseList(params);
    if (res.code === 200) {
      uiTestCaseData.value = res.data.list;
      total.value = res.data.total;
    } else {
      ElMessage.error(res.message || '获取测试用例列表失败');
    }
  } catch (error) {
    ElMessage.error("获取测试用例列表失败");
    console.error("获取测试用例列表失败:", error);
  } finally {
    loading.value = false;
  }
};

// 打开用例添加/编辑弹窗
const handleOpenDialog = () => {
  dialogTitle.value = "添加用例";
  resetForm();
  dialogVisible.value = true;
};

// 编辑用例
const handleEditTestCase = async (row) => {
  dialogTitle.value = "编辑用例";
  try {
    const res = await UiTestCaseApi.getUiTestCaseDetail(row.caseId);
    if (res.code === 200) {
      Object.assign(testCaseForm, res.data);
      dialogVisible.value = true;
    } else {
      ElMessage.error(res.message || '获取用例详情失败');
    }
  } catch (error) {
    ElMessage.error("获取用例详情失败");
  }
};

// 删除用例
const handleDeleteTestCase = async (row) => {
  try {
    const res = await UiTestCaseApi.deleteUiTestCase(row.caseId);
    if (res.code === 200) {
      ElMessage.success("删除成功");
      await fetchTestCaseList();
    } else {
      ElMessage.error(res.message || '删除失败');
    }
  } catch (error) {
    ElMessage.error("删除失败");
  }
};

// 执行用例
const handleRunTestCase = async (row) => {
  try {
    const res = await UiTestCaseApi.executeUiTestCase(row.caseId);
    if (res.code === 200) {
      ElMessage.success("执行成功");
      await fetchTestCaseList();
    } else {
      ElMessage.error(res.message || '执行失败');
    }
  } catch (error) {
    ElMessage.error("执行失败");
  }
};

// 提交表单
const onSubmit = async () => {
  if (!testCaseForm.caseTitle) {
    ElMessage.warning('请输入用例标题');
    return;
  }

  try {
    const data = { ...testCaseForm };
    let res;
    if (testCaseForm.id) {
      res = await UiTestCaseApi.updateUiTestCase(testCaseForm.id, data);
    } else {
      res = await UiTestCaseApi.createUiTestCase(data);
    }

    if (res.code === 200) {
      ElMessage.success(testCaseForm.id ? "更新成功" : "创建成功");
      dialogVisible.value = false;
      await fetchTestCaseList();
    } else {
      ElMessage.error(res.message || '操作失败');
    }
  } catch (error) {
    ElMessage.error("操作失败");
  }
};

// 处理搜索
const handleSearch = () => {
  pagination.page = 1;
  fetchTestCaseList();
};

// 处理焦点事件
const handleFocus = () => {
  placeholderText.value = "";
};

const handleBlur = () => {
  placeholderText.value = "输入查询关键字⏎";
};

// 处理Tab切换
const handleTabClick = (tab) => {
  console.log("Tab切换, 新的tab值:", tab.props.name);
  pagination.page = 1;
  pagination.pageSize = 10;
  fetchTestCaseList(tab.props.name);
};

// 处理分页变化
const handlePaginationChange = ({ page, pageSize }) => {
  pagination.page = page;
  pagination.pageSize = pageSize;
  fetchTestCaseList();
};

// 添加用例
const handleAddTestCase = () => {
  handleOpenDialog();
};

// 组件挂载时获取数据
onMounted(() => {
  fetchTestCaseList();
});

// 标签类型映射
const getPriorityTagType = (priority) => {
  const priorityMap = {
    "P0": "danger",
    "P1": "warning",
    "P2": "success",
    "P3": "info"
  };
  return priorityMap[priority] || "";
};

const getStatusTagType = (status) => {
  const statusMap = {
    "已执行": "success",
    "未执行": "info",
    "执行中": "warning"
  };
  return statusMap[status] || "";
};

const getResultTagType = (result) => {
  const resultMap = {
    "通过": "success",
    "失败": "danger",
    "部分通过": "warning",
    "未执行": "info",
    "执行中": "primary"
  };
  return resultMap[result] || "";
};
</script>

<style scoped>
.customer-table :deep(.el-table__cell) {
  border-right: none; /* 隐藏表格纵向边框*/
}

.customer-table {
  overflow: hidden; /* 隐藏溢出的内容 */
}

.customer-table :deep(.el-table__inner-wrapper) {
  overflow-x: auto; /* 仅在需要时显示横向滚动条 */
  overflow-y: hidden; /* 隐藏纵向滚动条 */
}

.customer-table :deep(.el-table__body-wrapper) {
  overflow-y: auto; /* 允许内容区域纵向滚动 */
  max-height: 560px; /* 限制最大高度 */
}
</style> 