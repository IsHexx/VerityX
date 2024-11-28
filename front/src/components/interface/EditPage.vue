<!-- EditPage.vue -->
<template>
    <div class="edit-page">

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
            <el-row>
              <el-col :span="24"
                ><el-form-item justify="end">
                  <el-button type="primary" @click="onSubmit">保存</el-button>
                  <el-button>取消</el-button>
                </el-form-item></el-col
              >
            </el-row>
          </el-form>
    </div>
  </template>
  
  <script setup>
import { reactive, ref, inject, watch, watchEffect, computed } from "vue";
import { ApiManageApi } from "@/api/apiManageService";
  
const editApiData = inject('editApiData', null);

const formSize = ref("default");
const ruleFormRef = ref(null);

const ruleForm = reactive({
  name: '',
  method: '',
  path: '',
  status: '',
  createdAt: '',
  updatedAt: '',
  mockUrl: '',
  responseBody: ''
});



// 监听 editApiData 变化
watch(
  () => editApiData?.value,
  (newData) => {
    if (newData) {
      console.log('编辑页数据更新:', newData.id);
      fetchApiDetails(newData.id);
    }
  },
  { immediate: true, deep: true }
);

// 获取 API 详情的方法
const fetchApiDetails = async (apiId) => {
  try {
    const response = await ApiManageApi.getApiDetail(apiId);
    const apiDetail = response.data;

    // 更新表单数据
    ruleForm.name = apiDetail.apiName;
    ruleForm.method = apiDetail.requestMethod;
    ruleForm.path = apiDetail.apiPath;
    ruleForm.status = apiDetail.status;
    ruleForm.createdAt = apiDetail.createdAt;
    ruleForm.updatedAt = apiDetail.updatedAt;
    ruleForm.mockUrl = apiDetail.mockUrl;
    ruleForm.responseBody = apiDetail.responseBody;
  } catch (error) {
    console.error('获取接口详情失败', error);
  }
};

  
  // Same script setup as PreviewPage.vue with additional methods
  const onSubmit = () => {
    // Handle form submission
    console.log('Form submitted');
  };
  </script>