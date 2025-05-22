<!-- ApiManagePage.vue --><template>  <div class="api-manage-container">    <!-- Removed duplicate header -->    <div class="content">
      <!-- Api Tree -->
      <api-tree></api-tree>
      <!-- Api Tree End -->

      <!-- Main Content -->
      <el-card style="width: 76%">
        <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
          <!-- 预览 -->
          <el-tab-pane label="预览" name="preview">
            <preview-page></preview-page>
          </el-tab-pane>

          <!-- 编辑 -->
          <el-tab-pane label="编辑" name="edit">
            <edit-page></edit-page>
          </el-tab-pane>

          <!-- 运行 -->
          <el-tab-pane label="运行" name="run">
            <run-page></run-page>
          </el-tab-pane>
        </el-tabs>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, provide, watch } from "vue";
import ApiTree from "../../components/interface/ApiTree.vue";import PreviewPage from "../../components/interface/PreviewPage.vue";import EditPage from "../../components/interface/EditPage.vue";import RunPage from "../../components/interface/RunPage.vue";import { useProjectStore } from "@/store/projectStore";

// 获取项目存储
const projectStore = useProjectStore();

// 当前激活的标签页
const activeName = ref("preview");

// 定义响应式数据
const previewApiData = ref(null);
const editApiData = ref(null);
provide("activeName", activeName);
provide("previewApiData", previewApiData);
provide("editApiData", editApiData);
provide("projectStore", projectStore);

// 监听项目ID变化
watch(() => projectStore.currentProjectId, (newProjectId, oldProjectId) => {
  if (newProjectId !== oldProjectId) {
    console.log("项目ID变化, 需要更新接口数据");
    // 可以在这里添加重新加载数据的逻辑，或者由子组件监听处理
  }
});

// Tab 切换事件（可选）
// const handleClick = (tab) => {
  
//   if (tab.props.name === "preview") {
//     console.log("当前选中标签页:", tab.props.name);
//     provide("selectedApiData", selectedApiData);
//   } else if (tab.props.name === "edit") {
//     console.log("当前选中标签页:", tab.props.name);
//     provide("editApiData", editApiData);
//   }
  
// };
</script>

<style scoped>
.api-manage-container {
  display: flex;
  flex-direction: column;
}

.content {
  flex: 2;
  background-color: #f5f5f5;
  display: flex;
  justify-content: space-between;
}
</style>
