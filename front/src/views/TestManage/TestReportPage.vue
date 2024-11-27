<template>
  <div class="flex flex-wrap gap-4">
    <el-card shadow="always" class="w-full">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-tabs v-model="activeTab" type="" @tab-click="handleTabClick">
            <el-tab-pane label="报告列表" name="all_report" default></el-tab-pane>
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
          />
          <el-button type="primary" @click="handleGenerateReport">生成报告</el-button>
        </el-col>
      </el-row>
      
      <el-table
        :data="tableData"
        :border="true"
        v-loading="loading"
        style="width: 100%; min-height: 560px"
        class="customer-table"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="reportId" label="ID" min-width="100" />
        <el-table-column prop="reportTitle" label="报告标题" min-width="150" />
        <el-table-column prop="createdBy" label="创建人" min-width="220" />
        <el-table-column prop="createdAt" label="创建日期" min-width="220" />
        <el-table-column prop="planId" label="所属计划" min-width="220" />
        <el-table-column prop="testVersion" label="版本" min-width="220" />
        
        <el-table-column fixed="right" label="操作" min-width="150">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleReportDetail(row)">
              详细
            </el-button>
            <el-button link type="primary" size="small" @click="handleEditReport(row)">
              编辑
            </el-button>
            <el-popconfirm
              title="确认删除该测试报告吗?"
              @confirm="handleDeleteReport(row)"
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

      <!-- 报告详情/编辑弹窗 -->
      <el-dialog
        v-model="dialogVisible"
        :title="dialogTitle"
        width="650px"
      >
        <el-form :model="form" label-width="auto">
          <el-form-item label="报告标题">
            <el-input v-model="form.reportTitle" placeholder="请输入报告标题" />
          </el-form-item>
          <el-form-item label="所属计划">
            <el-select v-model="form.planId" placeholder="请选择所属计划">
              <el-option label="计划A" value="planA" />
              <el-option label="计划B" value="planB" />
            </el-select>
          </el-form-item>
          <el-form-item label="版本">
            <el-input v-model="form.testVersion" placeholder="请输入版本号" />
          </el-form-item>
          <el-form-item label="创建人">
            <el-input v-model="form.createdBy" placeholder="请输入创建人" />
          </el-form-item>
          <el-form-item label="创建日期">
            <el-date-picker
              v-model="form.createdAt"
              type="date"
              placeholder="选择日期"
            />
          </el-form-item>
          <el-form-item label="报告描述">
            <el-input
              v-model="form.description"
              type="textarea"
              placeholder="请输入报告描述"
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
import { useRouter } from "vue-router";
import { TestReportApi } from "@/api/testReportService";

const router = useRouter();

// 状态管理
const loading = ref(false);
const dialogVisible = ref(false);
const dialogTitle = ref("生成报告");
const placeholderText = ref("输入查询关键字⏎");
const searchKeyword = ref("");
const activeTab = ref("all_report");
const tableData = ref([]);
const total = ref(0);


// 分页
const pagination = reactive({
  page: 1,
  pageSize: 10,
});

// 表单数据
const form = reactive({
  reportId: "",
  reportTitle: "",
  createdBy: "",
  createdAt: "",
  planId: "",
  testVersion: "",
});

// 重置表单
const resetForm = () => {
  Object.assign(form, {
    reportId: "",
    reportTitle: "",
    createdBy: "",
    createdAt: "",
    planId: "",
    testVersion: "",
  });
};

// 获取报告列表
const fetchTestReportList = async () => {
  loading.value = true;
  try {
    const res = await TestReportApi.getTestreports({
      page: pagination.page,
      pageSize: pagination.pageSize,
      keyword: searchKeyword.value,
    });
    tableData.value = res.data.data;
    total.value = res.data.total;
  } catch (error) {
    ElMessage.error("获取测试报告列表失败");
    console.error("获取测试报告列表失败:", error);
  } finally {
    loading.value = false;
  }
};

// 打开报告生成/编辑弹窗
const handleOpenReportDialog = () => {
  dialogTitle.value = "生成报告";
  resetForm();
  dialogVisible.value = true;
};

// 编辑报告
const handleEditReport = (row) => {
  dialogTitle.value = "编辑报告";
  Object.assign(form, {
    reportId: row.reportId,
    reportTitle: row.reportTitle,
    createdBy: row.createdBy,
    createdAt: row.createdAt,
    planId: row.planId,
    testVersion: row.testVersion,
  });
  dialogVisible.value = true;
};

// 报告详情
const handleReportDetail = async (row) => {
  try {
    const res = await TestReportApi.getTestReportDetail(row.id);
    // TODO: 实现查看详情的逻辑，可以是打开新页面或弹窗显示详细信息
    console.log("报告详情:", res.data);
  } catch (error) {
    ElMessage.error("获取报告详情失败");
  }
};

// 删除报告
const handleDeleteReport = async (row) => {
  try {
    await TestReportApi.deleteTestreport(row.reportId);
    ElMessage.success("删除成功");
    await fetchTestReportList();
  } catch (error) {
    ElMessage.error(error.message || "删除失败");
  }
};

// 提交表单（创建/更新）
const onSubmit = async () => {
  try {
    const data = { ...form };
    if (form.reportId) {
      // 更新
      await TestReportApi.updateTestreport(form.reportId, data);
      ElMessage.success("更新成功");
    } else {
      // 创建
      await TestReportApi.createTestReport(data);
      ElMessage.success("创建成功");
    }
    dialogVisible.value = false;
    await fetchTestReportList();
  } catch (error) {
    ElMessage.error(error.message || "操作失败");
  }
};

// 生成报告
const handleGenerateReport = () => {
  router.push("/testreport/audit");
};

// 处理焦点事件
const handleFocus = () => {
  placeholderText.value = "";
};

const handleBlur = () => {
  placeholderText.value = "输入查询关键字⏎";
};

// 处理Tab切换
const handleTabClick = () => {
  pagination.page = 1;
  fetchTestReportList();
};

// 处理分页变化
const handlePaginationChange = ({ page, pageSize }) => {
  pagination.page = page;
  pagination.pageSize = pageSize;
  fetchTestReportList();
};

// 组件挂载时获取数据
onMounted(() => {
  fetchTestReportList();
});
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