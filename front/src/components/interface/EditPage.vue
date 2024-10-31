<!-- EditPage.vue -->
<template>
    <div class="edit-page">
        <el-tab-pane label="编辑" name="second"
          ><!-- Embed the form here -->
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
          </el-form></el-tab-pane
        >
    </div>
  </template>
  
  <script setup>
  import { reactive, ref } from 'vue';
  
  const formSize = ref('default');
  const ruleFormRef = ref(null);
  
  const ruleForm = reactive({
    meth: 'GET',
    environment: '',
    uri: '',
    code: '',
    value: '',
    user: '',
    ContentType: '',
    Cookies: '',
    responseHeader: '',
    responseBody: '',
  });
  
  // Same script setup as PreviewPage.vue with additional methods
  const onSubmit = () => {
    // Handle form submission
    console.log('Form submitted');
  };
  </script>