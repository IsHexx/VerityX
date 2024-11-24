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
        :data="tableData"
        :border="true"
        :resizable="true"
        :fit="true"
        style="width: 100%; height: 100%; min-height: 560px; border: 0px"
        class="customer-table"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="ID" min-width="100" />
        <el-table-column prop="name" label="BUG标题" min-width="150" />
        <el-table-column prop="P" label="优先级" min-width="100">
          <template #default="scope">
            <el-tag :type="getPTagType(scope.row.P)" :effect="'light'">
              {{ scope.row.P }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="bug_level" label="缺陷等级" min-width="130">
          <template #default="scope">
            <el-tag
              :type="getLevelTagType(scope.row.bug_level)"
              :effect="'light'"
            >
              {{ scope.row.bug_level }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="Person" label="创建人" min-width="220" />
        <el-table-column prop="startDate" label="创建日期" min-width="220" />
        <el-table-column prop="bug_Fixer" label="指派给" min-width="220" />
        <el-table-column prop="bug_type" label="缺陷类型" min-width="220">
          <template #default="scope">
            <el-tag
              :type="getBugTypeTagType(scope.row.bug_type)"
              :effect="'light'"
            >
              {{ scope.row.bug_type }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="state" label="状态" min-width="100">
          <template #default="scope">
            <el-tag :type="getStateTagType(scope.row.state)" :effect="'light'">
              {{ scope.row.state }}
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
        title="提BUG"
        style="font-size: large; height: auto;"
        width="650px"
      >
        <el-form :model="form" label-width="auto" >
          <el-form-item label="所属项目">
            <el-select v-model="form.name" placeholder="请选择所属项目">
              <el-option label="项目A" value="shanghai" />
              <el-option label="项目B" value="beijing" />
            </el-select>
          </el-form-item>
          <el-form-item label="所属计划">
            <el-select v-model="form.stat" placeholder="请选择所属计划">
              <el-option label="计划A" value="shanghai" />
            </el-select>
          </el-form-item>
          <el-form-item label="缺陷标题">
            <el-input v-model="form.bugname" placeholder="请输入缺陷标题" />
          </el-form-item>

          <el-form-item>
            <el-col :span="11">
                <el-form-item label="严重程度"> 
                    <el-select v-model="form.stat" placeholder="请选择缺陷状态">
                        <el-option label="阻塞" value="shanghai" />
                        <el-option label="严重" value="shanghai" />
                        <el-option label="一般" value="shanghai" />
                        <el-option label="轻微" value="shanghai" />
                    </el-select>
                </el-form-item>
            </el-col>
            <el-col :span="2" style="text-align: center">
              <span class="text-gray-500">优先级</span>
            </el-col>
            <el-col :span="11">
                <el-select v-model="form.stat" placeholder="请选择修复优先级">
                    <el-option label="①" value="priority1" />
                    <el-option label="②" value="priority2" />
                    <el-option label="③" value="priority3" />
                    <el-option label="④" value="priority4" />
                </el-select>
            </el-col>
          </el-form-item>

          <el-form-item label="BUG类型">
            <el-select v-model="form.region" placeholder="请选择BUG类型">
              <el-option label="功能" value="function_case" />
              <el-option label="接口" value="api_case" />
              <el-option label="性能" value="benchmark_case" />
              <el-option label="安全" value="security_case" />
            </el-select>
          </el-form-item>
          <el-form-item label="指派给">
            <el-col :span="10">
              <el-select
                v-model="form.date1"
                placeholder="指派给"
                style="width: 100%"
              />
            </el-col>
            <el-col :span="4" style="text-align: right">
              <span class="text-gray-500">创建日期</span>
            </el-col>
            <el-col :span="10">
              <el-date-picker
                v-model="form.date2"
                type="date"
                placeholder="创建日期"
                style="width: 100%; padding-left: 10px"
              />
            </el-col>
          </el-form-item>
          <el-form-item label="选项">
            <el-switch v-model="form.delivery" />
          </el-form-item>

          <el-form-item label="重现步骤">
            <el-input
              v-model="form.desc"
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
      <PaginationPage></PaginationPage>
    </el-card>
  </div>
</template>
    
    <script setup>
import PaginationPage from "@/components/PaginationPage.vue";
import { ref, reactive } from "vue";
import { Search } from "@element-plus/icons-vue";

const input2 = ref("");

const activeTab = ref("all_bug");

const handleClick = (tab, event) => {
  console.log(tab, event);
};

const dialogVisible = ref(false);

// 在现有的 ref 声明附近添加
const placeholderText = ref("输入查询关键字⏎");

// 添加事件处理函数
const handleFocus = () => {
  placeholderText.value = ""; // 获得焦点时清空placeholder
};

const handleBlur = () => {
  placeholderText.value = "输入查询关键字⏎"; // 失去焦点时恢复placeholder
};

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

// BUG状态标签类型
const getLevelTagType = (level) => {
  const levelMap = {
    阻塞: "success",
    严重: "warning",
    一般: "danger",
    轻微: "info",
  };
  return levelMap[level] || "";
};

// BUG状态标签类型
const getStateTagType = (state) => {
  const stateMap = {
    已修复: "success",
    已确认: "warning",
    复测不通过: "danger",
    已验证: "info",
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

// BUG标签类型
const getBugTypeTagType = (type) => {
  const typeMap = {
    功能: "primary",
    接口: "success",
    性能: "warning",
    安全: "danger",
    兼容性: "info",
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
    name: "Home",
    P: "①",
    bug_level: "阻塞",
    Person: "Tom",
    startDate: "2016-05-03",
    bug_Fixer: "Jerry",
    bug_type: "功能",
    state: "已修复",
  },
  {
    id: "2",
    name: "Home",
    P: "②",
    bug_level: "严重",
    Person: "Tom",
    startDate: "2016-05-03",
    bug_Fixer: "Jerry",
    bug_type: "接口",
    state: "已确认",
  },
  {
    id: "3",
    name: "Home",
    P: "③",
    bug_level: "一般",
    Person: "Tom",
    startDate: "2016-05-03",
    bug_Fixer: "Jerry",
    bug_type: "性能",
    state: "复测不通过",
  },
  {
    id: "4",
    name: "Home",
    P: "④",
    bug_level: "轻微",
    Person: "Tom",
    startDate: "2016-05-03",
    bug_Fixer: "Jerry",
    bug_type: "兼容性",
    state: "已验证",
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