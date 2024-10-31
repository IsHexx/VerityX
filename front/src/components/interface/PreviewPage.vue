<!-- PreviewPage.vue -->
<template>
  <div class="preview-page">
    <el-tab-pane label="预览" name="preview">
      <!-- Embed the form here -->
      <el-form
        ref="ruleFormRef"
        :model="ruleForm"
        :rules="rules"
        label-width="auto"
        class="ruleForm"
        style="border: 0px"
        :size="formSize"
        status-icon
      >
        <el-row>
          <el-col :span="12"
            ><el-form-item label="接口名" prop="name">
              <el-input
                v-model="ruleForm.name"
                style="border: none; box-shadow: none" /></el-form-item
          ></el-col>
          <el-col :span="12">
            <el-form-item label="请求方法" prop="methed">
              <el-input
                v-model="ruleForm.methed"
                style="border: none; box-shadow: none"
              /> </el-form-item
          ></el-col>
        </el-row>
        <el-row>
          <el-col :span="12"
            ><el-form-item label="接口路径" prop="path">
              <el-input v-model="ruleForm.path" /> </el-form-item
          ></el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="state">
              <el-input v-model="ruleForm.state" /> </el-form-item
          ></el-col>
        </el-row>

        <el-row>
          <el-col :span="12"
            ><el-form-item label="创建时间" prop="createTime">
              <el-input v-model="ruleForm.createTime" /> </el-form-item
          ></el-col>
          <el-col :span="12">
            <el-form-item label="更新时间" prop="updateTime">
              <el-input v-model="ruleForm.updateTime" /> </el-form-item
          ></el-col>
        </el-row>
        <el-row>
          <el-col :span="24"
            ><el-form-item label="MOCK地址" prop="mockkUrl">
              <el-input v-model="ruleForm.mockkUrl" /> </el-form-item
          ></el-col>
        </el-row>

        <el-form-item label="返回body" prop="desc">
          <el-input v-model="ruleForm.body" type="textarea" />
        </el-form-item>
      </el-form>
    </el-tab-pane>
  </div>
</template>
  
<script setup>
import { reactive, ref} from "vue";

const formSize = ref("default");
const ruleFormRef = ref(null);



const ruleForm = reactive({
  name: "获取用户状态",
  methed: "GET",
  path: "/hack/api/user/1",
  state: "已完成",
  createTime: "2023-10-01 14:30:00",
  updateTime: "2024-02-07 11:21:00",
  mockkUrl: "http://127.0.0.1:8080/mock/10/hack/api/user/1",
  body: `{"code":0,"data":{"id":1,"name":"张三","status":1},"message":"success"}`,
});



const rules = reactive({
  name: [
    { required: true, message: "Please input Activity name", trigger: "blur" },
    { min: 3, max: 100, message: "Length should be 3 to 5", trigger: "blur" },
  ],
  region: [
    {
      required: true,
      message: "Please select Activity zone",
      trigger: "change",
    },
  ],
  count: [
    {
      required: true,
      message: "Please select Activity count",
      trigger: "change",
    },
  ],
  date1: [
    {
      type: "date",
      required: true,
      message: "Please pick a date",
      trigger: "change",
    },
  ],
  date2: [
    {
      type: "date",
      required: true,
      message: "Please pick a time",
      trigger: "change",
    },
  ],
  location: [
    { required: true, message: "Please select a location", trigger: "change" },
  ],
  type: [
    {
      type: "array",
      required: true,
      message: "Please select at least one activity type",
      trigger: "change",
    },
  ],
  resource: [
    {
      required: true,
      message: "Please select activity resource",
      trigger: "change",
    },
  ],
  desc: [
    { required: true, message: "Please input activity form", trigger: "blur" },
  ],
});

</script>