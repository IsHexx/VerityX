<template>
  <div class="content">
    <!-- eltree -->
    <api-tree></api-tree>
    <!-- eltree end-->

    <el-card style="width: 76%">
      <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
        <!-- 预览 -->

        <preview-page> </preview-page>
        <!-- 预览 end-->

        <!-- 编辑 -->
        <edit-page> </edit-page>
        <!-- 编辑 end-->

        <!-- 运行 -->
        <run-page> </run-page>
        <!-- 运行 end-->
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref, watch } from "vue";
import { Folder, Link, Plus, More } from "@element-plus/icons-vue";
import { ElMessage, ElMessageBox } from "element-plus";
import ApiTree from "../../components/interface/ApiTree.vue";
import PreviewPage from "../../components/interface/PreviewPage.vue";
import EditPage from "../../components/interface/EditPage.vue";
import RunPage from "../../components/interface/RunPage.vue";

const activeName = ref("preview");


// const ruleFormRef = ref(null);

// const submitForm = async (formEl) => {
//   if (!formEl) return;
//   await formEl.validate((valid, fields) => {
//     if (valid) {
//       console.log("submit!");
//     } else {
//       console.log("error submit!", fields);
//     }
//   });
// };


</script>

<style scoped>
.content {
  flex: 2;
  background-color: #f5f5f5;
  display: flex;
  justify-content: space-between;
}
</style>
