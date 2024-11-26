<template>
  <div class="flex flex-wrap gap-4">
    <el-card shadow="always" class="w-full">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-tabs v-model="activeTab" type="" @tab-click="handleTabClick">
            <el-tab-pane label="全部" name="all_case" default></el-tab-pane>
            <el-tab-pane label="已评审" name="over_audit"></el-tab-pane>
            <el-tab-pane label="待评审" name="wait_audit"></el-tab-pane>
          </el-tabs>
        </el-col>
        <el-col :span="18" style="text-align: right; align-content: center;">
          <el-input
            v-model="input2"
            style="width: 240px"
            :placeholder="placeholderText"
            :suffix-icon="Search"
            @focus="handleFocus"
            @blur="handleBlur"
          />
          <el-button type="primary" @click="handleAddTestcase">+ 添加用例</el-button>
        </el-col>
      </el-row>
      
      <el-table
        :data="testCaseData"
        :border="true"
        :resizable="true"
        :fit="true"
        style="width: 100%; height: 100%; min-height: 560px; border: 0px"
        class="customer-table"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="caseId" label="用例ID" min-width="100" />
        <el-table-column prop="caseTitle" label="用例标题" min-width="150" />
        <el-table-column prop="importanceLevel" label="Priority" min-width="100">
          <template #default="scope">
            <el-tag :type="getPTagType(scope.row.importanceLevel)" :effect="'light'">
              {{ scope.row.importanceLevel }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="caseStatus" label="用例状态" min-width="130">
          <template #default="scope">
            <el-tag
              :type="getStateTagType(scope.row.caseStatus)"
              :effect="'light'"
            >
              {{ scope.row.caseStatus }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdBy" label="创建人" min-width="220" />
        <el-table-column prop="createdAt" label="创建日期" min-width="220" />
        <el-table-column prop="caseType" label="用例类型" min-width="220">
          <template #default="scope">
            <el-tag
              :type="getTypeTagType(scope.row.caseType)"
              :effect="'light'"
            >
              {{ scope.row.caseType }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="操作" min-width="120">
          <template #default="{ row }">
            <el-popconfirm
              title="确认删除该测试用例吗?"
              @confirm="handleDeleteTestcase(row)"
              confirm-button-text="确定"
              cancel-button-text="取消"
            >
              <template #reference>
                <el-button link type="primary" size="small">删除</el-button>
              </template>
            </el-popconfirm>
            <el-button 
              link 
              type="primary" 
              size="small" 
              @click="handleEditTestcase(row)"
            >
              编辑
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 弹窗表单 -->
      <el-dialog
        v-model="dialogVisible"
        title="添加用例"
        style="font-size: large"
        width="650px"
      >
        <el-form :model="testCaseform" label-width="auto" style="max-width: 600px">
          <el-form-item label="用例标题">
            <el-input v-model="testCaseform.caseTitle" placeholder="请输入用例标题" />
          </el-form-item>
          <el-form-item label="所属项目">
            <el-select v-model="testCaseform.projectName" placeholder="请选择所属项目">
              <el-option label="项目A" value="shanghai" />
              <el-option label="项目B" value="beijing" />
            </el-select>
          </el-form-item>
          <el-form-item label="用例类型">
            <el-select v-model="testCaseform.caseType" placeholder="请选择用例类型">
              <el-option label="功能测试" value="功能测试" />
              <el-option label="接口测试" value="接口测试" />
              <el-option label="性能测试" value="性能测试" />
              <el-option label="安全测试" value="安全测试" />
            </el-select>
          </el-form-item>
          <el-form-item label="优先级">
            <el-select v-model="testCaseform.importanceLevel" placeholder="请选择用例优先级">
              <el-option label="P1" value="1" />
              <el-option label="P2" value="2" />
              <el-option label="P3" value="3" />
              <el-option label="P4" value="4" />
            </el-select>
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="testCaseform.caseStatus" placeholder="请选择计划状态">
              <el-option label="待审批" value="shanghai" />
            </el-select>
          </el-form-item>
          <el-form-item label="创建者">
            <el-input v-model="testCaseform.createdBy" placeholder="请输入用例创建人" />
          </el-form-item>
          
          <el-form-item label="日期">
            <el-col :span="11">
              <el-date-picker
                v-model="testCaseform.createdAt"
                type="date"
                placeholder="开始时间"
                style="width: 100%"
              />
            </el-col>
          </el-form-item>

          <el-form-item label="描述">
            <el-input
              v-model="testCaseform.description"
              type="textarea"
              placeholder="请输入描述"
            />
          </el-form-item>
          <el-row type="flex" justify="end">
            <el-button type="primary" @click="onSubmit">
              {{ testCaseform.id ? '保存' : '创建' }}
            </el-button>
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
import { ref, reactive, onMounted } from "vue";
import { Search } from '@element-plus/icons-vue';
import { TestcaseApi } from '@/api/testcaseService'
import { ElMessage } from 'element-plus'

const input2 = ref('')
const activeTab = ref("all_case");
const dialogVisible = ref(false);
const placeholderText = ref('输入查询关键字⏎')

// 表单数据
const testCaseform = reactive({
  caseId: "",
  caseTitle: "",
  projectName: "",
  caseType: "",
  importanceLevel: "",
  caseStatus: "",
  startDate: "",
  description: "",
});

// 列表数据
const testCaseData = ref([])

// 焦点事件处理
const handleFocus = () => {
  placeholderText.value = ''
}

const handleBlur = () => {
  placeholderText.value = '输入查询关键字⏎'
}

// 标签颜色映射
const getStateTagType = (state) => {
  const stateMap = {
    已评审: "success",
    待评审: "warning",
    评审未通过: "danger",
    草稿: "info",
  };
  return stateMap[state] || "";
};

const getPTagType = (P) => {
  const importanceLevelMap = {
    "1": "success",
    "2": "warning",
    "3": "danger",
    "4": "info",
  };
  return importanceLevelMap[P] || "";
};

const getTypeTagType = (caseType) => {
  const typeMap = {
    功能测试: "primary",
    接口测试: "success",
    性能测试: "warning",
    安全测试: "danger",
    兼容性测试: "info",
  };
  return typeMap[caseType] || "";
};

// 添加用例
const handleAddTestcase = () => {
  dialogVisible.value = true
  Object.assign(testCaseform, {
    caseTitle: "",
    projectName: "",
    caseType: "",
    importanceLevel: "",
    caseStatus: "",
    createdBy: "",
    createdAt: "",
    description: ""
  })
}

// 编辑用例
const handleEditTestcase = (row) => {
  Object.assign(testCaseform, {
    caseId: row.caseId,
    caseTitle: row.caseTitle,
    projectName: row.projectName,
    caseType: row.caseType,
    importanceLevel: row.importanceLevel,
    caseStatus: row.caseStatus,
    createdBy: row.createdBy,
    createdAt: row.createdAt,
    description: row.description
  })
  dialogVisible.value = true
}

// 删除用例
const handleDeleteTestcase = async (row) => {
  try {
    await TestcaseApi.deleteTestcase(row.caseId)
    ElMessage.success('删除成功')
    await fetchTestcase()
  } catch (error) {
    ElMessage.error(error.message || '删除失败')
    console.error('删除失败:', error)
  }
}

// 提交表单
const onSubmit = async () => {
  try {
    const data = {
      caseTitle: testCaseform.caseTitle,
      projectId: '1',
      caseType: testCaseform.caseType,
      importanceLevel: testCaseform.importanceLevel,
      caseStatus: testCaseform.caseStatus,
      createdBy: testCaseform.createdBy,
      createdAt: testCaseform.createdAt,
      description: testCaseform.description
    }

    if (testCaseform.caseId) {
      // 更新用例
      await TestcaseApi.updateTestcase(testCaseform.caseId, data)
      ElMessage.success('用例更新成功')
    } else {
      // 创建用例
      await TestcaseApi.createTestcase(data)
      ElMessage.success('用例创建成功')
    }

    dialogVisible.value = false
    await fetchTestcase()
  } catch (error) {
    ElMessage.error(error.message || '保存失败')
    console.error('保存失败:', error)
  }
}

// 获取用例列表
const fetchTestcase = async (caseStatus = '') => {
  loading.value = true
  try {
    const res = await TestcaseApi.getTestcases({
      page: pagination.page,
      pageSize: pagination.pageSize,
      caseStatus: caseStatus,
      keyword: input2.value  // 添加关键字搜索
    })
    testCaseData.value = res.data.data
    total.value = res.data.total
  } catch (error) {
    ElMessage.error('获取用例列表失败')
    console.error('获取用例列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 标签页切换处理
const handleTabClick = (tab) => {
  const tabStatusMap = {
    all_case: '',
    over_audit: '已评审',
    wait_audit: '待评审'
  }
  
  const caseStatus = tabStatusMap[tab.props.name] || ''
  activeTab.value = tab.props.name
  console.log('activeTab.value:', activeTab.value); 
  console.log('fetchTestplan调用前status:', caseStatus); 
  pagination.page = 1
  fetchTestcase(caseStatus)
}

// 新增分页相关状态
const loading = ref(false)
const total = ref(0)
const pagination = reactive({
  page: 1,
  pageSize: 10
})

// 分页处理
const handlePaginationChange = ({ page, pageSize }) => {
  pagination.page = page
  pagination.pageSize = pageSize
  fetchTestcase()
}

// 组件挂载时获取数据
onMounted(() => {
  fetchTestcase()
})
</script>
  
<style scoped>
.flex-wrap {
  width: 100%;
  height: 100%;
}
.w-full {
  width: 100%;
  height: 100%;
}
.customer-table :deep(.el-table__cell) {
  border-right: none;
}

.demo-tabs > .el-tabs__content {
  padding: 32px;
  color: #6b778c;
  font-size: 32px }
  </style>