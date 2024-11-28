<!-- PreviewPage.vue -->
<template>
  <div class="preview-page">
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
          <el-col :span="12">
            <el-form-item label="接口名" prop="name">
              <el-input
                v-model="ruleForm.name"
                style="border: none; box-shadow: none"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="请求方法" prop="method">
              <el-input
                v-model="ruleForm.method"
                style="border: none; box-shadow: none"
                disabled
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <!-- 其他表单项保持不变 -->
        <el-row>
          <el-col :span="12">
            <el-form-item label="接口路径" prop="path">
              <el-input v-model="ruleForm.path" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-input v-model="ruleForm.status" disabled />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="创建时间" prop="createdAt">
              <el-input v-model="ruleForm.createdAt" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="更新时间" prop="updatedAt">
              <el-input v-model="ruleForm.updatedAt" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="MOCK地址" prop="mockUrl">
              <el-input v-model="ruleForm.mockUrl" disabled />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="返回body" prop="responseBody">
          <el-input v-model="ruleForm.responseBody" type="textarea" disabled />
        </el-form-item>
      </el-form>
  </div>
</template>
  
<script setup>
import { reactive, ref, inject, watch, watchEffect, computed } from "vue";
import { ApiManageApi } from "@/api/apiManageService";
import { useStore } from 'vuex'


// 使用 previewApiData 替代 selectedApiData
const previewApiData = inject('previewApiData', null);


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




// 监听 previewApiData 变化
watch(
  () => previewApiData?.value,
  (newData) => {
    if (newData) {
      console.log('预览页数据更新:', newData.id);
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

const rules = reactive({});
</script>