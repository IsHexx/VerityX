<template>
  <div class="flex flex-wrap gap-4">
    <el-card shadow="always" class="w-full">
      <el-row :gutter="20">
        <el-col :span="8"
          ><el-tabs v-model="activeTab" type="" @tab-click="handleTabClick">
            <el-tab-pane label="全部" name="all_case" default></el-tab-pane>
            <el-tab-pane label="已评审" name="over_audit"></el-tab-pane>
            <el-tab-pane
              label="待评审"
              name="wait_audit"
            ></el-tab-pane> </el-tabs
        ></el-col>
        <el-col :span="16" style="text-align: right; align-content: center;"
          >
          <el-input
            v-model="input2"
            style="width: 240px"
            :placeholder="placeholderText"
            :suffix-icon="Search"
            @focus="handleFocus"
            @blur="handleBlur"
            />
          <el-button type="primary" @click="dialogVisible = true"
            >+ 添加用例</el-button
          ></el-col
        >
      </el-row>
      <el-table
        :data="tableData"
        :border="true"
        :resizable="true"
        :fit="true"
        style="width: 100%; height: 100%; min-height: 560px; border: 0px"
        class="customer-table"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="ID" min-width="100" />
        <el-table-column prop="name" label="用例标题" min-width="150" />
        <el-table-column prop="P" label="P" min-width="100">
          <template #default="scope">
            <el-tag :type="getPTagType(scope.row.P)" :effect="'light'">
              {{ scope.row.P }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="case_state" label="用例状态" min-width="130">
          <template #default="scope">
            <el-tag
              :type="getStateTagType(scope.row.case_state)"
              :effect="'light'"
            >
              {{ scope.row.case_state }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="Person" label="创建人" min-width="220" />
        <el-table-column prop="startDate" label="创建日期" min-width="220" />
        <el-table-column prop="case_type" label="用例类型" min-width="220">
          <template #default="scope">
            <el-tag
              :type="getTypeTagType(scope.row.case_type)"
              :effect="'light'"
            >
              {{ scope.row.case_type }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="操作" min-width="120">
          <template #default>
            <el-button link type="primary" size="small" @click="handleClick">
              详细
            </el-button>
            <el-button link type="primary" size="small">编辑</el-button>
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
        <el-form :model="form" label-width="auto" style="max-width: 600px">
          <el-form-item label="所属项目">
            <el-select v-model="form.name" placeholder="请选择所属项目">
              <el-option label="项目A" value="shanghai" />
              <el-option label="项目B" value="beijing" />
            </el-select>
          </el-form-item>

          <el-form-item label="状态">
            <el-select v-model="form.stat" placeholder="请选择计划状态">
              <el-option label="待审批" value="shanghai" />
            </el-select>
          </el-form-item>
          <el-form-item label="用例类型">
            <el-select v-model="form.region" placeholder="请选择用例类型">
              <el-option label="功能测试" value="function_case" />
              <el-option label="接口测试" value="api_case" />
              <el-option label="性能测试" value="benchmark_case" />
              <el-option label="安全测试" value="security_case" />
            </el-select>
          </el-form-item>
          <el-form-item label="日期">
            <el-col :span="11">
              <el-date-picker
                v-model="form.date1"
                type="date"
                placeholder="开始时间"
                style="width: 100%"
              />
            </el-col>
            <el-col :span="2" style="text-align: center">
              <span class="text-gray-500">至</span>
            </el-col>
            <el-col :span="11">
              <el-date-picker
                v-model="form.date2"
                type="date"
                placeholder="结束时间"
                style="width: 100%"
              />
            </el-col>
          </el-form-item>
          <el-form-item label="选项">
            <el-switch v-model="form.delivery" />
          </el-form-item>

          <el-form-item label="描述">
            <el-input
              v-model="form.desc"
              type="textarea"
              placeholder="请输入描述"
            />
          </el-form-item>
          <el-row type="flex" justify="end">
            <el-button type="primary" @click="onSubmit">创建</el-button>
            <el-button @click="dialogVisible = false">返回</el-button>
          </el-row>
        </el-form>
      </el-dialog>
      <PaginationPage></PaginationPage>
    </el-card>
  </div>
</template>
  
  <script setup>
import PaginationPage from "@/components/PaginationPage.vue";
import { ref, reactive } from "vue";
import { Search } from '@element-plus/icons-vue'


const input2 = ref('')

const activeTab = ref("all_plan");

const handleClick = (tab, event) => {
  console.log(tab, event);
};

const dialogVisible = ref(false);

// 在现有的 ref 声明附近添加
const placeholderText = ref('输入查询关键字⏎')

// 添加事件处理函数
const handleFocus = () => {
  placeholderText.value = '' // 获得焦点时清空placeholder
}

const handleBlur = () => {
  placeholderText.value = '输入查询关键字⏎' // 失去焦点时恢复placeholder
}

// 表单数据
const form = reactive({
  name: "",
  region: "",
  date1: "",
  date2: "",
  delivery: false,
  type: [],
  resource: "",
  desc: "",
});

// 用例状态标签类型
const getStateTagType = (state) => {
  const stateMap = {
    已评审: "success",
    待评审: "warning",
    评审未通过: "danger",
    草稿: "info",
  };
  return stateMap[state] || "";
};

// 用例状态标签类型
const getPTagType = (P) => {
  const PMap = {
    "①": "success",
    "②": "warning",
    "③": "danger",
    "④": "info",
  };
  return PMap[P] || "";
};

// 用例类型标签类型
const getTypeTagType = (type) => {
  const typeMap = {
    功能测试: "primary",
    接口测试: "success",
    性能测试: "warning",
    安全测试: "danger",
    兼容性测试: "info",
  };
  return typeMap[type] || "";
};

// 提交表单
const onSubmit = () => {
  console.log("form submitted:", form);
  dialogVisible.value = false; // 提交后关闭弹窗
};

const tableData = [
  {
    id: "1",
    startDate: "2016-05-03",
    Person: "Tom",
    case_state: "已评审",
    case_type: "功能测试",
    endData: "2019-07-13",
    P: "①",
    name: "Home",
  },
  {
    id: "2",
    startDate: "2016-05-03",
    Person: "Tom",
    case_state: "待评审",
    case_type: "接口测试",
    endData: "2019-07-13",
    P: "②",
    name: "Home",
  },
  {
    id: "3",
    startDate: "2016-05-03",
    Person: "Tom",
    case_state: "已评审未通过",
    case_type: "性能测试",
    endData: "2019-07-13",
    P: "③",
    name: "Home",
  },
  {
    id: "4",
    startDate: "2016-05-03",
    Person: "Tom",
    case_state: "草稿",
    case_type: "兼容性测试",
    endData: "2019-07-13",
    P: "④",
    name: "Home",
  },
];
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
  font-size: 32px;
  font-weight: 600;
}

</style>