<!-- RunPage.vue -->
<template>
    <div class="run-page">
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
            <el-row :gutter="20">
              <el-col :span="3" style="padding-right: 0px; padding-left: 10px"
                ><el-form-item label="">
                  <el-select v-model="ruleForm.meth" placeholder="GET">
                    <el-option label="GET" value="GET" />
                    <el-option label="POST" value="POST" />
                  </el-select> </el-form-item
              ></el-col>
              <el-col :span="9" style="padding-right: 0px; padding-left: 0px">
                <el-form-item label="" prop="environment">
                  <el-input
                    v-model="ruleForm.environment"
                    placeholder="请选择环境"
                  /> </el-form-item
              ></el-col>
              <el-col :span="8" style="padding-right: 0px; padding-left: 0px">
                <el-form-item label="" prop="uri">
                  <el-input
                    v-model="ruleForm.uri"
                    placeholder="请填写路径"
                  /> </el-form-item
              ></el-col>
              <el-col
                :span="4"
                style="
                  text-align: right;
                  padding-right: 10px;
                  padding-left: 0px;
                "
              >
                <el-button
                  style="background-color: #95d475; color: #ffffff"
                  @click="Submit"
                  >发送</el-button
                >
                <el-button
                  style="
                    margin-left: 0px;
                    background-color: #409eff;
                    color: #ffffff;
                  "
                  >保存</el-button
                ></el-col
              >
            </el-row>
            <!-- QUERY PARAMETERS参数填写 -->
            <el-divider style="margin-top: 5px" />
            <el-row :gutter="20">
              <el-col
                :span="24"
                style="padding-right: 10px; padding-left: 10px"
              >
                <el-label style="font-size: larger; font-weight: bold"
                  >QUERY PARAMETERS</el-label
                >
                <el-divider style="margin-top: 10px" />
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="2" style="padding-right: 10px; padding-left: 10px">
                <el-form-item label="" prop="code">
                  <el-input
                    v-model="ruleForm.code"
                    placeholder="code"
                  /> </el-form-item
              ></el-col>

              <el-col :span="1" style="padding-right: 0px; padding-left: 0px">
                <el-checkbox value="Promotion activities" name="type">
                </el-checkbox
                ><el-text class="mx-1">=</el-text></el-col
              >
              <el-col :span="21" style="padding-right: 10px; padding-left: 0px">
                <el-form-item label="" prop="value">
                  <el-input
                    v-model="ruleForm.value"
                    placeholder="value"
                  /> </el-form-item
              ></el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="2" style="padding-right: 10px; padding-left: 10px">
                <el-form-item label="" prop="user">
                  <el-input
                    v-model="ruleForm.user"
                    placeholder="user"
                  /> </el-form-item
              ></el-col>

              <el-col :span="1" style="padding-right: 0px; padding-left: 0px">
                <el-checkbox value="Promotion activities" name="type">
                </el-checkbox
                ><el-text class="mx-1">=</el-text></el-col
              >
              <el-col :span="21" style="padding-right: 10px; padding-left: 0px">
                <el-form-item label="" prop="value">
                  <el-input
                    v-model="ruleForm.value"
                    placeholder="value"
                  /> </el-form-item
              ></el-col>
            </el-row>

            <!-- HEADER参数填写 -->
            <el-divider style="margin-top: 5px" />
            <el-row :gutter="20">
              <el-col
                :span="24"
                style="padding-right: 10px; padding-left: 10px"
              >
                <el-label style="font-size: larger; font-weight: bold"
                  >HEADER</el-label
                >
                <el-divider style="margin-top: 10px" />
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="4" style="padding-right: 10px; padding-left: 10px">
                <el-form-item label="" prop="Content-type">
                  <el-input
                    v-model="ruleForm.ContentType"
                    placeholder="ContentType"
                  /> </el-form-item
              ></el-col>

              <el-col :span="1" style="padding-right: 0px; padding-left: 0px">
                <el-text class="mx-1">=</el-text></el-col
              >
              <el-col :span="19" style="padding-right: 10px; padding-left: 0px">
                <el-form-item label="" prop="value">
                  <el-input
                    v-model="ruleForm.value"
                    placeholder="value"
                  /> </el-form-item
              ></el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="4" style="padding-right: 10px; padding-left: 10px">
                <el-form-item label="" prop="Cookies">
                  <el-input
                    v-model="ruleForm.Cookies"
                    placeholder="Cookies"
                  /> </el-form-item
              ></el-col>

              <el-col :span="1" style="padding-right: 0px; padding-left: 0px">
                <el-text class="mx-1">=</el-text></el-col
              >
              <el-col :span="19" style="padding-right: 10px; padding-left: 0px">
                <el-form-item label="" prop="value">
                  <el-input
                    v-model="ruleForm.value"
                    placeholder="value"
                  /> </el-form-item
              ></el-col>
            </el-row>

            <!-- Body参数填写 -->
            <el-divider style="margin-top: 5px" />
            <el-row :gutter="20">
              <el-col
                :span="24"
                style="padding-right: 10px; padding-left: 10px"
              >
                <el-label style="font-size: larger; font-weight: bold"
                  >BODY</el-label
                >
                <el-divider style="margin-top: 10px" />
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="2" style="padding-right: 10px; padding-left: 10px">
                <el-form-item label="" prop="aaa">
                  <el-input
                    v-model="ruleForm.aaa"
                    placeholder="aaa"
                  /> </el-form-item
              ></el-col>

              <el-col :span="1" style="padding-right: 0px; padding-left: 0px">
                <el-checkbox value="Promotion activities" name="type">
                </el-checkbox
                ><el-text class="mx-1">=</el-text></el-col
              >
              <el-col :span="21" style="padding-right: 10px; padding-left: 0px">
                <el-form-item label="" prop="value">
                  <el-input
                    v-model="ruleForm.value"
                    placeholder="value"
                  /> </el-form-item
              ></el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="2" style="padding-right: 10px; padding-left: 10px">
                <el-form-item label="" prop="user">
                  <el-input
                    v-model="ruleForm.user"
                    placeholder="bbb"
                  /> </el-form-item
              ></el-col>

              <el-col :span="1" style="padding-right: 0px; padding-left: 0px">
                <el-checkbox value="Promotion activities" name="type">
                </el-checkbox
                ><el-text class="mx-1">=</el-text></el-col
              >
              <el-col :span="21" style="padding-right: 10px; padding-left: 0px">
                <el-form-item label="" prop="value">
                  <el-input
                    v-model="ruleForm.value"
                    placeholder="value"
                  /> </el-form-item
              ></el-col>
            </el-row>

            <!-- respose  -->
            <el-divider style="margin-top: 5px" />
            <el-row :gutter="20">
              <el-col
                :span="24"
                style="padding-right: 10px; padding-left: 10px"
              >
                <el-label style="font-size: larger; font-weight: bold"
                  >Respose</el-label
                >
                <el-divider style="margin-top: 10px" />
              </el-col>
            </el-row>
            <div class="upper-content">
              <div style="min-width: 480px; width: 49%; min-height: 300px">
                <el-label>Header</el-label>
                <el-divider style="margin: 5px 0px 10px 0px" />
                <el-card style="height: 80%">
                  <el-form-item label="" prop="desc" >
                    <el-input v-model="ruleForm.body" type="textarea" />
                  </el-form-item>
                </el-card>
              </div>
              <div style="min-width: 480px; width: 49%; min-height: 300px">
                <el-label>Body</el-label>
                <el-divider style="margin: 5px 0px 10px 0px" />
                <el-card style="height: 80%">
                  <el-form-item label="" prop="desc" >
                    <el-input v-model="ruleForm.body" type="textarea" />
                  </el-form-item>
                </el-card>
              </div>
            </div> </el-form
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
  
  
  </script>

  <style scoped>
  .upper-content {
  flex: 2;
  background-color: #fffcfc;
  display: flex;
  justify-content: space-between;
}
  </style>