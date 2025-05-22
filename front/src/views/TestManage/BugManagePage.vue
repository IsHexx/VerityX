<!-- src/components/BugManagePage.vue -->
<template>
  <div class="flex flex-wrap gap-4">
    <el-card shadow="always" class="w-full">
      <el-row :gutter="20">
        <el-col :span="6"
          ><el-tabs v-model="activeTab" type="" @tab-click="handleTabClick">
            <el-tab-pane label="全部" name="all_bug" default></el-tab-pane>
            <el-tab-pane label="已修复" name="resolve_Issues"></el-tab-pane>
            <el-tab-pane
              label="待修复"
              name="pending_Issues"
            ></el-tab-pane> </el-tabs
        ></el-col>
        <el-col :span="18" style="text-align: right; align-content: center">
          <el-input
            v-model="input2"
            style="width: 240px"
            :placeholder="placeholderText"
            :suffix-icon="Search"
            @focus="handleFocus"
            @blur="handleBlur"
          />
          <el-button type="primary" @click="dialogVisible = true"
            >提BUG</el-button
          ></el-col
        >
      </el-row>
      <el-table
      :data="bugData"
        border
        v-loading="loading"
        style="width: 100%; min-height: 560px"
        class="customer-table"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="bugId" label="ID" min-width="100" />
        <el-table-column prop="bugTitle" label="BUG标题" min-width="150" />
        <el-table-column prop="severity" label="严重级别" min-width="100">
          <template #default="scope">
            <el-tag :type="getPTagType(scope.row.severity)" :effect="'light'">
              {{ scope.row.severity }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="level" label="缺陷等级" min-width="130">
          <template #default="scope">
            <el-tag
              :type="getLevelTagType(scope.row.level)"
              :effect="'light'"
            >
              {{ scope.row.level }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdBy" label="创建人" min-width="220" />
        <el-table-column prop="createdAt" label="创建日期" min-width="220" />
        <el-table-column prop="assignedTo" label="指派给" min-width="220" />
        <el-table-column prop="bugType" label="缺陷类型" min-width="220">
          <template #default="scope">
            <el-tag
              :type="getBugTypeTagType(scope.row.bugType)"
              :effect="'light'"
            >
              {{ scope.row.bugType }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" min-width="100">
          <template #default="scope">
            <el-tag :type="getStateTagType(scope.row.status)" :effect="'light'">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <!-- 原有列保持不变 -->
        <el-table-column fixed="right" label="操作" min-width="150">
          <template #default="{ row }">
            <el-button
              link
              type="primary"
              size="small"
              @click="handleBugDetail(row)"
            >
              详细
            </el-button>
            <el-button
              link
              type="primary"
              size="small"
              @click="handleEditBug(row)"
            >
              编辑
            </el-button>
            <el-popconfirm
              title="确认删除该 Bug 吗?"
              @confirm="handleDeleteBug(row)"
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
      <!-- 弹窗表单 -->
      <el-dialog
        v-model="dialogVisible"
        title="提BUG"
        style="font-size: large; height: auto"
        width="650px"
      >
        <el-form :model="form" label-width="auto">
          <el-form-item label="所属项目">
            <el-select v-model="form.projectName" placeholder="请选择所属项目">
              <el-option label="项目A" value="1" />
              <el-option label="项目B" value="1" />
            </el-select>
          </el-form-item>
          <el-form-item label="所属计划">
            <el-select v-model="form.planId" placeholder="请选择所属计划">
              <el-option label="计划A" value="222" />
            </el-select>
          </el-form-item>
          <el-form-item label="缺陷标题">
            <el-input v-model="form.bugTitle" placeholder="请输入缺陷标题" />
          </el-form-item>
          <el-form-item label="创建人">
            <el-input v-model="form.createdBy" placeholder="请输入创建人" />
          </el-form-item>
          <el-form-item>
            <el-col :span="11">
              <el-form-item label="严重程度">
                <el-select v-model="form.severity" placeholder="请选择缺陷状态">
                  <el-option label="阻塞" value="阻塞" />
                  <el-option label="严重" value="严重" />
                  <el-option label="一般" value="一般" />
                  <el-option label="轻微" value="轻微" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="3" style="text-align: center">
              <span class="text-gray-500">缺陷等级</span>
            </el-col>
            <el-col :span="10">
              <el-select v-model="form.level" placeholder="请选择缺陷等级">
                <el-option label="①" value="priority1" />
                <el-option label="②" value="priority2" />
                <el-option label="③" value="priority3" />
                <el-option label="④" value="priority4" />
              </el-select>
            </el-col>
          </el-form-item>

          <el-form-item label="BUG类型">
            <el-select v-model="form.bugType" placeholder="请选择BUG类型">
              <el-option label="功能" value="功能问题" />
              <el-option label="接口" value="接口问题" />
              <el-option label="性能" value="性能问题" />
              <el-option label="安全" value="安全问题" />
            </el-select>
          </el-form-item>
          <el-form-item label="缺陷状态">
            <el-select v-model="form.status" placeholder="请选择BUG类型">
              <el-option label="已创建" value="已创建" />
              <el-option label="已修复" value="已修复" />
              <el-option label="待修复" value="待修复" />
            </el-select>
          </el-form-item>
          <el-form-item label="指派给">
            <el-col :span="10">
              <el-select
                v-model="form.AssignedTo"
                placeholder="指派给"
                style="width: 100%"
              />
            </el-col>
            <el-col :span="4" style="text-align: right">
              <span class="text-gray-500">创建日期</span>
            </el-col>
            <el-col :span="10">
              <el-date-picker
                v-model="form.createdAt"
                type="date"
                placeholder="创建日期"
                style="width: 100%; padding-left: 10px"
              />
            </el-col>
          </el-form-item>

          <el-form-item label="重现步骤">
            <el-input
              v-model="form.description"
              type="textarea"
              placeholder="【复现步骤】 【实际结果】 【期望结果】"
            />
          </el-form-item>
          <el-row type="flex" justify="end">
            <el-button type="primary" @click="onSubmit">创建</el-button>
            <el-button @click="dialogVisible = false">返回</el-button>
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
import { ref, reactive, onMounted, computed, watch } from "vue";
import { Search } from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";
import { BugApi } from "@/api/bugService";
import { useProjectStore } from '@/store/projectStore';

// 使用项目Store
const projectStore = useProjectStore();
// 确保初始化项目状态
projectStore.initProjectState();

// 计算当前项目ID
const currentProjectId = computed(() => projectStore.getCurrentProjectId());

const input2 = ref("");
const searchKeyword = ref("");
const activeTab = ref("all_bug");
const dialogVisible = ref(false);
const dialogTitle = ref("提BUG");
const placeholderText = ref("输入查询关键字⏎");

// 加载状态和分页
const loading = ref(false);
const total = ref(0);
const bugData = ref([]);

const pagination = reactive({
  page: 1,
  pageSize: 10,
});

// 表单数据
const form = reactive({
  id: "",
  projectId: currentProjectId.value || "",
  planId: "",
  bugTitle: "",
  createdBy: "",
  level: "",
  severity: "",
  assignedTo: "",
  status: "",
  bugType: "",
  createdAt: '',
  description: "",
});

// 重置表单
const resetForm = () => {
  Object.assign(form, {
    id: "",
    projectId: currentProjectId.value || "",
    planId: "",
    bugTitle: "",
    createdBy: "",
    level: "",
    severity: "",
    assignedTo: "",
    status: "",
    bugType: "",
    createdAt: '',
    description: "",
  });
};

// 标签处理方法保持不变
const getLevelTagType = (level) => {
  console.log('当前level值:', level); 
  const levelMap = {
    "1": "success",
    "2": "warning",
    "3": "danger",
    "4": "info",
  };
  return levelMap[level] || "";
};

const getStateTagType = (status) => {
  console.log('当前status值:', status); 
  const stateMap = {
    已修复: "success",
    已确认: "warning",
    复测不通过: "danger",
    已验证: "info",
  };
  return stateMap[status] || "";
};

const getPTagType = (severity) => {
  console.log('当前Severity值:', severity); 
  const PMap = {
    阻塞: "success",
    严重: "warning",
    一般: "danger",
    轻微: "info",
  };
  return PMap[severity] || "";
};

const getBugTypeTagType = (type) => {
  
  const typeMap = {
    功能问题: "primary",
    接口问题: "success",
    性能问题: "warning",
    安全问题: "danger",
    兼容性问题: "info",
  };
  return typeMap[type] || "";
};

// 事件处理函数
const handleFocus = () => {
  placeholderText.value = "";
};

const handleBlur = () => {
  placeholderText.value = "输入查询关键字⏎";
};

// 获取 bug 列表
const fetchBugList = async (status = "") => {
  loading.value = true;
  try {
    const res = await BugApi.getBugs({
      page: pagination.page,
      pageSize: pagination.pageSize,
      status: status,
      projectId: currentProjectId.value
    });
    bugData.value = res.data.data;
    total.value = res.data.total;
  } catch (error) {
    ElMessage.error("获取 Bug 列表失败");
    console.error("获取 Bug 列表失败:", error);
  } finally {
    loading.value = false;
  }
};

// 处理 Tab 切换
const handleTabClick = (tab) => {
  const tabStatusMap = {
    all_bug: "",
    resolve_Issues: "已修复",
    pending_Issues: "待修复",
  };
  console.log("tab.props.name 切换", tab.props.name);
  const status = tabStatusMap[tab.props.name] || "";
  console.log("status", status);
  pagination.page = 1;
  fetchBugList(status);
};

// 提BUG
// const handleAddBug = () => {
//   dialogTitle.value = "提BUG";
//   resetForm();
//   dialogVisible.value = true;
// };

// 编辑 Bug
const handleEditBug = (row) => {
  dialogTitle.value = "编辑BUG";
  Object.assign(form, {
    id: row.id,
    projectId: row.projectId,
    planId: row.planId,
    bugTitle: row.bugTitle,
    level: row.level,
    severity: row.severity,
    bugType: row.bugType,
    assignedTo: row.assignedTo,
    status: row.status,
    createdBy: row.createdBy,
    createdAt: row.createdAt,
    description: row.description,
  });
  dialogVisible.value = true;
};

// Bug 详情
const handleBugDetail = (row) => {
  // TODO: 实现查看详情的逻辑
  console.log("查看详情", row);
};

// 删除 Bug
const handleDeleteBug = async (row) => {
  try {
    await BugApi.deleteBug(row.bugId);
    ElMessage.success("删除成功");
    await fetchBugList();
  } catch (error) {
    ElMessage.error(error.message || "删除失败");
  }
};

// 提交表单
const onSubmit = async () => {
  try {
    const data = { ...form };
    if (form.bugId) {
      // 更新
      await BugApi.updateBug(form.bugId, data);
      ElMessage.success("更新成功");
    } else {
      // 创建
      await BugApi.createBug(data);
      ElMessage.success("创建成功");
    }
    dialogVisible.value = false;
    await fetchBugList();
  } catch (error) {
    ElMessage.error(error.message || "操作失败");
  }
};

// 组件挂载时获取数据
onMounted(() => {
  fetchBugList();
});

// 处理分页变化
const handlePaginationChange = ({ page, pageSize }) => {
  pagination.page = page;
  pagination.pageSize = pageSize;
  fetchBugList();
};

// 监听项目变化，刷新数据
watch(currentProjectId, () => {
  pagination.page = 1;
  fetchBugList();
});
</script>
    
<style scoped>
.customer-table :deep(.el-table__cell) {
  border-right: none; /* 隐藏表格纵向边框*/
}

.customer-table {
  overflow: hidden;   /* 隐藏溢出的内容 */
}

.customer-table :deep(.el-table__inner-wrapper) {
  overflow-x: auto;  /* 仅在需要时显示横向滚动条 */
  overflow-y: hidden;  /* 隐藏纵向滚动条 */
}

.customer-table :deep(.el-table__body-wrapper) {
  overflow-y: auto;  /* 允许内容区域纵向滚动 */
  max-height: 560px;  /* 限制最大高度 */
}
  
</style>