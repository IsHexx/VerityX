<template>
    <div class="flex flex-wrap gap-4">
      <el-card shadow="always" class="w-full">
        <el-row :gutter="20">
          <el-col :span="6"
            ><el-tabs v-model="activeTab" type="" @tab-click="handleTabClick">
              <el-tab-pane label="报告列表" name="all_report" default></el-tab-pane>
               </el-tabs
          ></el-col>
          <el-col :span="18" style="text-align: right; align-content: center;"
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
              >生成报告</el-button
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
          <el-table-column prop="name" label="报告标题" min-width="150" />
          <el-table-column prop="Person" label="创建人" min-width="220" />
          <el-table-column prop="startDate" label="创建日期" min-width="220" />
          <el-table-column prop="beingPlan" label="所属计划" min-width="220"/>
          <el-table-column prop="version" label="版本" min-width="220"/>
          <el-table-column fixed="right" label="操作" min-width="120">
            <template #default>
              <el-button link type="primary" size="small" @click="handleClick">
                详细
              </el-button>
              <el-button link type="primary" size="small">编辑</el-button>
            </template>
          </el-table-column>
        </el-table>
        <PaginationPage></PaginationPage>
      </el-card>
    </div>
  </template>
    
    <script setup>
  import PaginationPage from "@/components/PaginationPage.vue";
  import { ref, reactive } from "vue";
  import { Search } from '@element-plus/icons-vue'
  
  
  const input2 = ref('')
  
  const activeTab = ref("all_report");
  
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
      name: "00001标题",
      Person: "Tom",
      startDate: "2016-05-03",
      case_state: "已评审",
      beingPlan: "100年前的计划",
      version: "V1.0.01",
    },
    {
        id: "2",
      name: "00002标题",
      Person: "Tom",
      startDate: "2016-05-03",
      case_state: "已评审",
      beingPlan: "100年前的计划",
      version: "V1.0.02",
    },
    {
      id: "3",
      name: "00003标题",
      Person: "Tom",
      startDate: "2016-05-03",
      case_state: "已评审",
      beingPlan: "100年前的计划",
      version: "V1.0.03",
    },
    {
      id: "4",
      name: "00004标题",
      Person: "Tom",
      startDate: "2016-05-03",
      case_state: "已评审",
      beingPlan: "100年前的计划",
      version: "V1.0.04",
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