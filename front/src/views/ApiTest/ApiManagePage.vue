<template>
  <div class="content">
    <el-card style="width: 23%; min-height: 660px; height: 100%">
      <template #header>
        <div class="card-header">
          <span>接口列表</span>
        </div>
      </template>
      <el-input
        v-model="filterText"
        style="width: 240px"
        placeholder="Filter keyword"
      />
      <el-tree
        ref="treeRef"
        class="filter-tree"
        :data="data"
        :props="defaultProps"
        default-expand-all
        :filter-node-method="filterNode"
      >
        <template #default="{ node, data }">
          <span class="custom-tree-node">
            <span class="icon-container">
              <el-icon
                v-if="node.level === 1"
                class="folder-icon"
                :style="{ color: getFolderColor(data.id) }"
              >
                <Folder />
              </el-icon>
              <span
                v-else-if="node.level === 2"
                class="http-method"
                :class="data.method?.toLowerCase()"
              >
                {{ data.method || "GET" }}
              </span>
              <el-icon v-else-if="node.level === 3"><Link /></el-icon>
            </span>
            <span>{{ node.label }}</span>
          </span>
        </template>
      </el-tree>
    </el-card>

    <el-card style="width: 76%">
      <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
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
        <el-tab-pane label="运行" name="third"
          ><el-form
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
            <el-divider  style="margin-top: 5px;" />
            <el-row :gutter="20">
              <el-col :span="24" style="padding-right: 10px; padding-left: 10px">
                  <el-label style="font-size:larger; font-weight: bold">QUERY PARAMETERS</el-label>
                  <el-divider  style="margin-top: 10px;" />
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

              <el-col :span="1" style="padding-right: 0px; padding-left: 0px" >
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

              <el-col :span="1" style="padding-right: 0px; padding-left: 0px" >
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
            <el-divider  style="margin-top: 5px;" />
            <el-row :gutter="20">
              <el-col :span="24" style="padding-right: 10px; padding-left: 10px">
                  <el-label style="font-size:larger; font-weight: bold">BODY</el-label>
                  <el-divider  style="margin-top: 10px;" />
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

              <el-col :span="1" style="padding-right: 0px; padding-left: 0px" >
                <el-text class="mx-1">=</el-text></el-col>
              <el-col :span="19" style="padding-right: 10px; padding-left: 0px">
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

              <el-col :span="1" style="padding-right: 0px; padding-left: 0px" >
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
            </el-form-item> </el-form
        ></el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref, watch } from "vue";
import { Folder, Link } from "@element-plus/icons-vue";

const activeName = ref("preview");
const handleClick = (tab, event) => {
  console.log(tab, event);
};

const filterText = ref("");
const treeRef = ref(null);
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

const locationOptions = ["Home", "Company", "School"];

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

const submitForm = async (formEl) => {
  if (!formEl) return;
  await formEl.validate((valid, fields) => {
    if (valid) {
      console.log("submit!");
    } else {
      console.log("error submit!", fields);
    }
  });
};

const resetForm = (formEl) => {
  if (!formEl) return;
  formEl.resetFields();
};

const data = [
  {
    id: 1,
    label: "用户管理",
    children: [
      {
        id: 4,
        label: "获取用户列表",
        method: "GET",
        children: [{ id: 9, label: "接口详情 1" }],
      },
      {
        id: 5,
        label: "创建用户",
        method: "POST",
        children: [{ id: 10, label: "接口详情 2" }],
      },
    ],
  },
  {
    id: 2,
    label: "订单管理",
    children: [
      {
        id: 6,
        label: "更新订单",
        method: "PUT",
        children: [{ id: 11, label: "接口详情 3" }],
      },
      {
        id: 7,
        label: "删除订单",
        method: "DELETE",
        children: [{ id: 12, label: "接口详情 4" }],
      },
    ],
  },
];

const defaultProps = {
  children: "children",
  label: "label",
};

const getFolderColor = (id) => {
  const colors = ["#409EFF", "#67C23A", "#E6A23C", "#F56C6C", "#909399"];
  return colors[id % colors.length];
};

watch(filterText, (val) => {
  if (treeRef.value) {
    treeRef.value.filter(val);
  }
});

const filterNode = (value, data) => {
  if (!value) return true;
  return data.label.includes(value);
};
</script>

<style scoped>
.content {
  flex: 2;
  padding: 2px 20px 4px 20px;
  background-color: #f5f5f5;
  display: flex;
  justify-content: space-between;
}

.custom-tree-node {
  display: flex;
  align-items: center;
}

.icon-container {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  margin-right: 4px;
}

.folder-icon {
  font-size: 18px;
}

.http-method {
  padding: 1px 4px;
  border-radius: 4px;
  font-size: 8px;
  font-weight: normal;
  text-transform: uppercase;
  min-width: 32px;
  text-align: center;
}

.get {
  background-color: #95d475;
  color: #fff;
}

.post {
  background-color: #409eff;
  color: #fff;
}

.put {
  background-color: #e6a23c;
  color: #fff;
}

.delete {
  background-color: #f56c6c;
  color: #fff;
}

:deep(.el-tree-node__content) {
  height: 32px;
}

:deep(.el-tree-node__content) {
  position: relative;
}

:deep(.el-tree-node__content > .el-tree-node__expand-icon) {
  position: absolute;
  opacity: 0;
}
</style>
