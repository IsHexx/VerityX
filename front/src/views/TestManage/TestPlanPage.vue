<template>
  <div class="flex flex-wrap gap-4">
    <el-card shadow="always" class="w-full">
      <el-row :gutter="20">
        <el-col :span="22"
          ><el-tabs v-model="activeTab" type="" @tab-click="handleTabClick">
            <el-tab-pane label="全部" name="all_plan" default></el-tab-pane>
            <el-tab-pane label="测试中测试单" name="under_plan"></el-tab-pane>
            <el-tab-pane
              label="已完成测试"
              name="over_plan"
            ></el-tab-pane> </el-tabs
        ></el-col>
        <el-col :span="2" style="text-align: right"
          ><el-button type="primary" @click="dialogVisible = true"
            >添加计划</el-button
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
        <el-table-column prop="name" label="名称" min-width="150" />
        <el-table-column prop="version" label="版本" min-width="220" />
        <el-table-column prop="state" label="状态" min-width="220" />
        <el-table-column prop="Person" label="参与人" min-width="220" />
        <el-table-column prop="startDate" label="开始日期" min-width="220" />
        <el-table-column prop="endData" label="结束日期" min-width="220" />
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
      <el-dialog v-model="dialogVisible" title="添加计划" style="font-size: large;" width="650px">
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
              <el-option label="待执行" value="beijing" />
            </el-select>
          </el-form-item>
          <el-form-item label="版本">
              <el-input v-model="form.region"  placeholder="请输入版本号"/>
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
            <el-col :span="2" class="参与人">
              <span class="text-gray-500">-</span>
            </el-col>
            <el-col :span="11">
              <el-time-picker
                v-model="form.date2"
                placeholder="结束时间"
                style="width: 100%"
              />
            </el-col>
          </el-form-item>
          <el-form-item label="选项">
            <el-switch v-model="form.delivery" />
          </el-form-item>

          <el-form-item label="描述">
            <el-input v-model="form.desc" type="textarea" placeholder="请输入描述"/>
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

const activeTab = ref("all_plan");

const handleClick = (tab, event) => {
  console.log(tab, event);
};

const dialogVisible = ref(false);

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

// 提交表单
const onSubmit = () => {
  console.log("form submitted:", form);
  dialogVisible.value = false; // 提交后关闭弹窗
};

const tableData = [
  {
    startDate: "2016-05-03",
    Person: "Tom",
    state: "California",
    city: "Los Angeles",
    endData: "2019-07-13",
    version: "CA 90036",
    name: "Home",
  },
  {
    startDate: "2016-05-03",
    Person: "Tom",
    state: "California",
    city: "Los Angeles",
    endData: "2019-07-13",
    version: "CA 90036",
    name: "Home",
  },
  {
    startDate: "2016-05-03",
    Person: "Tom",
    state: "California",
    city: "Los Angeles",
    endData: "2019-07-13",
    version: "CA 90036",
    name: "Home",
  },
  {
    startDate: "2016-05-03",
    Person: "Tom",
    state: "California",
    city: "Los Angeles",
    endData: "2019-07-13",
    version: "CA 90036",
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