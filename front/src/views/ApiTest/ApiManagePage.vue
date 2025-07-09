<template>
  <div class="api-manage-container">
    <div class="content">
      <!-- 左侧接口树 -->
      <div class="left-panel">
        <api-tree></api-tree>
      </div>

      <!-- 右侧内容区域 -->
      <div class="right-panel">
        <div class="tabs-container">
          <!-- 手动创建标签页头部 -->
          <div class="custom-tabs-header">
            <div class="custom-tab-nav">
              <button
                class="custom-tab-button"
                :class="{ active: activeName === 'preview' }"
                @click="activeName = 'preview'"
              >
                预览
              </button>
              <button
                class="custom-tab-button"
                :class="{ active: activeName === 'edit' }"
                @click="activeName = 'edit'"
              >
                编辑
              </button>
              <button
                class="custom-tab-button"
                :class="{ active: activeName === 'run' }"
                @click="activeName = 'run'"
              >
                运行
              </button>
            </div>
          </div>

          <!-- 标签页内容 -->
          <div class="custom-tabs-content">
            <div v-show="activeName === 'preview'" class="tab-content">
              <preview-page></preview-page>
            </div>
            <div v-show="activeName === 'edit'" class="tab-content">
              <edit-page ref="editPageRef"></edit-page>
            </div>
            <div v-show="activeName === 'run'" class="tab-content">
              <run-page ref="runPageRef" :edit-page-ref="editPageRef"></run-page>
            </div>
          </div>
        </div>
      </div>
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

// 组件引用
const editPageRef = ref(null);
const runPageRef = ref(null);

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

// Tab 切换事件（现在使用自定义标签页，不需要特殊处理）
const handleTabClick = (tabName) => {
  console.log("当前选中标签页:", tabName);
  activeName.value = tabName;
};
</script>

<style scoped>
.api-manage-container {
  height: 100%;
  display: flex;
  flex-direction: column;
  background-color: #f5f5f5;
  overflow: hidden; /* 防止整个页面滚动 */
}

.content {
  flex: 1;
  display: flex;
  gap: 16px;
  padding: 16px;
  min-height: 0; /* 重要：允许flex子项收缩 */
  overflow: hidden; /* 防止内容区域滚动 */
}

.left-panel {
  width: 300px;
  min-width: 250px;
  max-width: 400px;
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  overflow: hidden; /* 防止左侧面板滚动 */
}

.right-panel {
  flex: 1;
  min-width: 0; /* 重要：允许flex子项收缩 */
  display: flex;
  flex-direction: column;
  overflow: hidden; /* 防止右侧面板滚动 */
}

.tabs-container {
  height: 100%;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

/* 自定义标签页样式 */
.custom-tabs-header {
  flex-shrink: 0;
  background: white;
  border-bottom: 1px solid #e4e7ed;
  padding: 16px 20px 0 20px;
}

.custom-tab-nav {
  display: flex;
  gap: 0;
}

.custom-tab-button {
  padding: 12px 20px;
  border: none;
  background: transparent;
  color: #606266;
  cursor: pointer;
  font-size: 14px;
  border-bottom: 2px solid transparent;
  transition: all 0.3s;
  position: relative;
}

.custom-tab-button:hover {
  color: #409eff;
}

.custom-tab-button.active {
  color: #409eff;
  border-bottom-color: #409eff;
  font-weight: 500;
}

.custom-tabs-content {
  flex: 1;
  overflow-y: auto; /* 整个内容区域滚动 */
  overflow-x: hidden;
  padding: 0;
}

.tab-content {
  min-height: 100%;
  overflow: visible; /* 允许内容自然展开 */
}

/* 移除Element Plus相关样式，使用自定义标签页 */

/* 自定义滚动条样式 */
:deep(*::-webkit-scrollbar) {
  width: 6px;
  height: 6px;
}

:deep(*::-webkit-scrollbar-track) {
  background: transparent;
}

:deep(*::-webkit-scrollbar-thumb) {
  background-color: #d1d5db;
  border-radius: 3px;
  transition: background-color 0.3s;
}

:deep(*::-webkit-scrollbar-thumb:hover) {
  background-color: #9ca3af;
}

:deep(*::-webkit-scrollbar-corner) {
  background: transparent;
}

/* Firefox 滚动条样式 */
:deep(*) {
  scrollbar-width: thin;
  scrollbar-color: #d1d5db transparent;
}

/* 防止横向滚动 */
.api-manage-container {
  overflow-x: hidden;
}

.content {
  overflow-x: hidden;
}

.left-panel,
.right-panel {
  overflow-x: hidden;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .left-panel {
    width: 280px;
    min-width: 220px;
  }
}

@media (max-width: 768px) {
  .content {
    flex-direction: column;
    gap: 12px;
    padding: 12px;
  }

  .left-panel {
    width: 100%;
    min-width: auto;
    max-width: none;
    height: 300px;
  }
}
</style>
