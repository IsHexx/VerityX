<template>
  <div class="flex flex-wrap gap-4">
    <el-card shadow="always" class="w-full">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-tabs v-model="activeTab" type="" @tab-click="handleTabClick">
            <el-tab-pane label="全部元素" name="all_elements"></el-tab-pane>
            <el-tab-pane label="页面组件" name="page_components"></el-tab-pane>
            <el-tab-pane label="表单元素" name="form_elements"></el-tab-pane>
            <el-tab-pane label="交互元素" name="interactive_elements"></el-tab-pane>
          </el-tabs>
        </el-col>
        <el-col :span="18" style="text-align: right; align-content: center">
          <el-input
            v-model="searchKeyword"
            style="width: 240px"
            placeholder="搜索元素..."
            :suffix-icon="Search"
            @keyup.enter="handleSearch"
          />
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button type="primary" @click="handleAddElement">+ 添加元素</el-button>
          <el-button type="primary" @click="handleManageGroups">管理分组</el-button>
        </el-col>
      </el-row>
      
      <el-row :gutter="20" class="mt-4">
        <el-col :span="4">
          <!-- 元素分组树 -->
          <div class="element-group-tree">
            <div class="group-header">
              <span>元素分组</span>
            </div>
            <el-tree
              ref="elementGroupTree"
              :data="groupTreeData"
              :props="defaultProps"
              default-expand-all
              highlight-current
              @node-click="handleNodeClick"
            ></el-tree>
          </div>
        </el-col>
        
        <el-col :span="20">
          <!-- 元素列表表格 -->
          <el-table
            :data="elementData"
            :border="true"
            v-loading="loading"
            style="width: 100%; min-height: 540px"
            class="customer-table"
          >
            <el-table-column type="selection" width="55" />
            <el-table-column prop="elementId" label="ID" min-width="80" />
            <el-table-column prop="elementName" label="元素名称" min-width="150" />
            <el-table-column prop="groupName" label="所属分组" min-width="120" />
            <el-table-column prop="locatorType" label="定位类型" min-width="100">
              <template #default="scope">
                <el-tag
                  :type="getLocatorTagType(scope.row.locatorType)"
                  :effect="'light'"
                >
                  {{ scope.row.locatorType }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="locatorValue" label="定位值" min-width="200">
              <template #default="scope">
                <el-tooltip
                  :content="scope.row.locatorValue"
                  placement="top"
                  :show-after="500"
                >
                  <div class="truncate-text">{{ scope.row.locatorValue }}</div>
                </el-tooltip>
              </template>
            </el-table-column>
            <el-table-column prop="pageUrl" label="所属页面" min-width="180">
              <template #default="scope">
                <el-tooltip
                  :content="scope.row.pageUrl"
                  placement="top"
                  :show-after="500"
                >
                  <div class="truncate-text">{{ scope.row.pageUrl }}</div>
                </el-tooltip>
              </template>
            </el-table-column>
            <el-table-column prop="createdBy" label="创建人" min-width="100" />
            <el-table-column prop="createdAt" label="创建日期" min-width="150" />
            <el-table-column prop="hasScreenshot" label="截图" min-width="80">
              <template #default="scope">
                <el-icon v-if="scope.row.hasScreenshot" color="#67C23A">
                  <el-icon-check />
                </el-icon>
                <el-icon v-else color="#909399">
                  <el-icon-close />
                </el-icon>
              </template>
            </el-table-column>
            
            <el-table-column fixed="right" label="操作" min-width="180">
              <template #default="{ row }">
                <el-button link type="primary" size="small" @click="handleEditElement(row)">
                  编辑
                </el-button>
                <el-button link type="primary" size="small" @click="handleValidateElement(row)">
                  验证
                </el-button>
                <el-button link type="primary" size="small" @click="handleViewScreenshot(row)" :disabled="!row.hasScreenshot">
                  查看截图
                </el-button>
                <el-popconfirm
                  title="确认删除该元素吗?"
                  @confirm="handleDeleteElement(row)"
                  confirm-button-text="确定"
                  cancel-button-text="取消"
                >
                  <template #reference>
                    <el-button link type="primary" size="small">删除</el-button>
                  </template>
                </el-popconfirm>
              </template>
            </el-table-column>
          </el-table>
          
          <PaginationPage 
            :total="total" 
            @update:pagination="handlePaginationChange"
          />
        </el-col>
      </el-row>
    </el-card>
    
    <!-- 元素编辑对话框 -->
    <ElementEditDialog
      :visible="elementDialogVisible"
      @update:visible="elementDialogVisible = $event"
      :element="currentElement"
      :is-edit="isEditElement"
      :group-options="groupOptions"
      @submit="handleElementSubmit"
      @validation-result="handleValidationResult"
    />
    
    <!-- 元素分组管理对话框 -->
    <ElementGroupDialog
      :visible="groupDialogVisible"
      @update:visible="groupDialogVisible = $event"
      :groups="groupTreeData[0].children"
      @groups-updated="handleGroupsUpdated"
    />
    
    <!-- 元素定位器验证对话框 -->
    <ElementValidateDialog
      :visible="validateDialogVisible"
      @update:visible="validateDialogVisible = $event"
      :locator="currentLocator"
      @validation-success="handleValidationSuccess"
    />
    
    <!-- 元素截图查看对话框 -->
    <el-dialog
      v-model="screenshotDialogVisible"
      title="元素截图查看"
      width="800px"
    >
      <div v-if="currentElement && currentElement.hasScreenshot" class="screenshot-container">
        <img 
          :src="`/api/ui-elements/${currentElement.elementId}/screenshot`" 
          alt="元素截图"
          class="element-screenshot"
        />
      </div>
      <div v-else class="no-screenshot">
        <p>该元素没有截图</p>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import PaginationPage from "@/components/PaginationPage.vue";
import ElementEditDialog from "@/components/UiElementLocator/ElementEditDialog.vue";
import ElementGroupDialog from "@/components/UiElementLocator/ElementGroupDialog.vue";
import ElementValidateDialog from "@/components/UiElementLocator/ElementValidateDialog.vue";
import { ref, reactive, onMounted, computed } from "vue";
import { Search, Check, Close } from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";
import { UiElementLocatorApi } from "@/api/uiElementLocatorService";

// 状态管理
const loading = ref(false);
const searchKeyword = ref("");
const activeTab = ref("all_elements");
const elementData = ref([]);
const total = ref(0);
const currentGroupId = ref(null);

// 对话框状态
const elementDialogVisible = ref(false);
const groupDialogVisible = ref(false);
const validateDialogVisible = ref(false);
const screenshotDialogVisible = ref(false);
const isEditElement = ref(false);
const currentElement = ref(null);
const currentLocator = ref(null);

// 分页
const pagination = reactive({
  page: 1,
  pageSize: 10,
});

// 分组树
const elementGroupTree = ref(null);
const groupTreeData = ref([
  {
    id: 'all',
    label: '全部元素',
    children: []
  }
]);

const defaultProps = {
  children: 'children',
  label: 'label',
}

// 计算属性：分组选项，用于级联选择器
const groupOptions = computed(() => {
  // 将树形数据转换为级联选择器需要的格式
  const convertToOptions = (nodes) => {
    return nodes.map(node => ({
      value: node.id,
      label: node.label,
      children: node.children && node.children.length > 0 ? convertToOptions(node.children) : undefined
    }));
  };
  
  // 返回除"全部元素"外的所有分组
  return convertToOptions(groupTreeData.value[0].children);
});

// 获取元素列表
const fetchElementList = async (tabName = activeTab.value, page = pagination.page, pageSize = pagination.pageSize, keyword = searchKeyword.value, groupId = currentGroupId.value) => {
  loading.value = true;
  try {
    // 构造请求参数
    const params = { page, pageSize };
    
    if (keyword) params.keyword = keyword;
    if (groupId && groupId !== 'all') params.groupId = groupId;
    
    // 根据tab名称添加元素类型过滤
    if (tabName === 'page_components') {
      params.elementType = 'page_component';
    } else if (tabName === 'form_elements') {
      params.elementType = 'form_element';
    } else if (tabName === 'interactive_elements') {
      params.elementType = 'interactive_element';
    }
    
    console.log("请求参数:", params);
    
    // 调用后端API获取数据
    const res = await UiElementLocatorApi.getElementList(params);
    if (res.code === 200) {
      elementData.value = res.data.list;
      total.value = res.data.total;
    } else {
      ElMessage.error(res.message || '获取元素列表失败');
    }
    loading.value = false;
  } catch (error) {
    ElMessage.error("获取元素列表失败");
    console.error("获取元素列表失败:", error);
    loading.value = false;
  }
};

// 获取元素分组
const fetchElementGroups = async () => {
  try {
    // 调用后端API获取分组数据
    const res = await UiElementLocatorApi.getElementGroups();
    if (res.code === 200) {
      // 构建树形结构
      const groups = res.data || [];
      const treeData = [
        {
          id: 'all',
          label: '全部元素',
          children: []
        }
      ];
      
      // 将API返回的分组数据转换为树形结构
      groups.forEach(group => {
        treeData[0].children.push({
          id: group.id,
          label: group.groupName,
          children: group.subGroups ? group.subGroups.map(sub => ({
            id: sub.id,
            label: sub.groupName
          })) : []
        });
      });
      
      groupTreeData.value = treeData;
    } else {
      ElMessage.error(res.message || '获取元素分组失败');
    }
  } catch (error) {
    ElMessage.error("获取元素分组失败");
    console.error("获取元素分组失败:", error);
  }
};

// 标签类型映射
const getLocatorTagType = (locatorType) => {
  const typeMap = {
    "ID": "success",
    "CSS": "primary",
    "XPath": "warning",
    "Name": "info",
    "ClassName": "danger",
    "LinkText": "success",
    "TagName": "info",
    "AccessibilityId": "primary"
  };
  return typeMap[locatorType] || "";
};

// 处理分组节点点击
const handleNodeClick = (data) => {
  currentGroupId.value = data.id;
  pagination.page = 1;
  fetchElementList(activeTab.value, pagination.page, pagination.pageSize, searchKeyword.value, data.id);
};

// 处理Tab切换
const handleTabClick = (tab) => {
  console.log("Tab切换, 新的tab值:", tab.props.name);
  pagination.page = 1;
  pagination.pageSize = 10;
  fetchElementList(tab.props.name);
};

// 处理分页变化
const handlePaginationChange = ({ page, pageSize }) => {
  pagination.page = page;
  pagination.pageSize = pageSize;
  fetchElementList();
};

// 处理搜索
const handleSearch = () => {
  pagination.page = 1;
  fetchElementList();
};

// 添加元素
const handleAddElement = () => {
  isEditElement.value = false;
  currentElement.value = {
    elementId: "",
    elementName: "",
    groupId: "",
    elementType: "page_component",
    locatorType: "CSS",
    locatorValue: "",
    pageUrl: "",
    hasScreenshot: false
  };
  elementDialogVisible.value = true;
};

// 管理分组
const handleManageGroups = () => {
  groupDialogVisible.value = true;
};

// 编辑元素
const handleEditElement = (row) => {
  isEditElement.value = true;
  currentElement.value = { ...row };
  elementDialogVisible.value = true;
};

// 验证元素
const handleValidateElement = (row) => {
  currentLocator.value = {
    locatorType: row.locatorType,
    locatorValue: row.locatorValue,
    pageUrl: row.pageUrl
  };
  validateDialogVisible.value = true;
};

// 查看截图
const handleViewScreenshot = (row) => {
  if (!row.hasScreenshot) {
    ElMessage.warning("该元素没有截图");
    return;
  }
  
  currentElement.value = row;
  screenshotDialogVisible.value = true;
};

// 删除元素
const handleDeleteElement = async (row) => {
  try {
    const res = await UiElementLocatorApi.deleteElement(row.elementId);
    if (res.code === 200) {
      ElMessage.success("删除成功");
      fetchElementList();
    } else {
      ElMessage.error(res.message || '删除失败');
    }
  } catch (error) {
    ElMessage.error("删除失败");
    console.error(error);
  }
};

// 处理元素提交结果
const handleElementSubmit = (formData) => {
  // 表单提交后刷新列表
  fetchElementList();
};

// 处理验证结果
const handleValidationResult = (success, result) => {
  console.log("验证结果:", success, result);
};

// 处理验证成功
const handleValidationSuccess = (data) => {
  console.log("验证成功:", data);
};

// 处理分组更新
const handleGroupsUpdated = (groups) => {
  // 更新分组树
  groupTreeData.value[0].children = groups;
};

// 组件挂载时获取数据
onMounted(() => {
  fetchElementGroups();
  fetchElementList();
});
</script>

<style scoped>
.element-group-tree {
  border: 1px solid #ebeef5;
  border-radius: 4px;
  padding: 10px;
  height: 100%;
  min-height: 600px;
}

.group-header {
  font-weight: bold;
  margin-bottom: 10px;
  padding-bottom: 10px;
  border-bottom: 1px solid #ebeef5;
}

.truncate-text {
  max-width: 100%;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.customer-table :deep(.el-table__cell) {
  border-right: none; /* 隐藏表格纵向边框*/
}

.customer-table {
  overflow: hidden; /* 隐藏溢出的内容 */
}

.customer-table :deep(.el-table__inner-wrapper) {
  overflow-x: auto; /* 仅在需要时显示横向滚动条 */
  overflow-y: hidden; /* 隐藏纵向滚动条 */
}

.customer-table :deep(.el-table__body-wrapper) {
  overflow-y: auto; /* 允许内容区域纵向滚动 */
  max-height: 560px; /* 限制最大高度 */
}

.screenshot-container {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 10px;
}

.element-screenshot {
  max-width: 100%;
  max-height: 500px;
  border: 1px solid #ebeef5;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.no-screenshot {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 200px;
  color: #909399;
  font-size: 16px;
}
</style> 