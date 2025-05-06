<template>
  <el-dialog
    v-model="dialogVisible"
    title="管理元素分组"
    width="600px"
    :close-on-click-modal="false"
  >
    <div class="group-dialog-container">
      <div class="group-tree-container">
        <div class="tree-header">
          <span>分组结构</span>
          <div class="tree-actions">
            <el-button size="small" @click="handleAddRootGroup">添加根分组</el-button>
          </div>
        </div>
        <el-tree
          ref="groupTree"
          :data="groupData"
          :props="defaultProps"
          node-key="id"
          default-expand-all
          highlight-current
          :expand-on-click-node="false"
          @node-click="handleNodeClick"
        >
          <template #default="{ node, data }">
            <div class="custom-tree-node">
              <span>{{ node.label }}</span>
              <span class="tree-node-actions">
                <el-button 
                  size="small" 
                  type="primary" 
                  link
                  @click.stop="handleAddChildGroup(data)"
                >
                  添加子分组
                </el-button>
                <el-button 
                  size="small" 
                  type="primary" 
                  link
                  @click.stop="handleEditGroup(data)"
                >
                  编辑
                </el-button>
                <el-button 
                  size="small" 
                  type="primary" 
                  link
                  @click.stop="handleDeleteGroup(node, data)"
                >
                  删除
                </el-button>
              </span>
            </div>
          </template>
        </el-tree>
      </div>
      
      <div class="group-form-container" v-if="showGroupForm">
        <div class="form-header">
          <span>{{ isEditGroup ? '编辑分组' : '添加分组' }}</span>
        </div>
        <el-form :model="groupForm" label-width="80px" ref="groupFormRef" :rules="formRules">
          <el-form-item label="上级分组">
            <el-input v-model="parentGroupName" disabled />
          </el-form-item>
          <el-form-item label="分组名称" prop="name">
            <el-input v-model="groupForm.name" placeholder="请输入分组名称" />
          </el-form-item>
          <el-form-item label="分组描述">
            <el-input 
              v-model="groupForm.description" 
              placeholder="请输入分组描述"
              type="textarea"
              :rows="3"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submitGroupForm">确定</el-button>
            <el-button @click="cancelGroupForm">取消</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div class="group-info-container" v-else>
        <div class="info-header">
          <span>分组详情</span>
        </div>
        <div class="info-content" v-if="currentGroup">
          <p><strong>分组名称:</strong> {{ currentGroup.label }}</p>
          <p><strong>分组ID:</strong> {{ currentGroup.id }}</p>
          <p><strong>包含子分组:</strong> {{ currentGroup.children ? currentGroup.children.length : 0 }}</p>
          <p><strong>描述:</strong> {{ currentGroup.description || '无' }}</p>
        </div>
        <div class="info-placeholder" v-else>
          <p>请选择一个分组查看详情</p>
        </div>
      </div>
    </div>
    
    <div class="dialog-footer" style="margin-top: 20px; text-align: right;">
      <el-button @click="dialogVisible = false">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, defineProps, defineEmits, watch, computed } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { UiElementLocatorApi } from '@/api/uiElementLocatorService';

const props = defineProps({
  visible: Boolean,
  groups: {
    type: Array,
    default: () => []
  }
});

const emit = defineEmits(['update:visible', 'groups-updated']);

// 对话框可见性
const dialogVisible = ref(props.visible);

// 组树引用
const groupTree = ref(null);

// 分组数据
const groupData = ref([]);

// 树形配置
const defaultProps = {
  children: 'children',
  label: 'label'
};

// 当前选中的分组
const currentGroup = ref(null);

// 显示分组表单
const showGroupForm = ref(false);
const isEditGroup = ref(false);

// 分组表单
const groupFormRef = ref(null);
const groupForm = reactive({
  id: '',
  name: '',
  description: '',
  parentId: null
});

// 表单验证规则
const formRules = {
  name: [
    { required: true, message: '请输入分组名称', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
  ]
};

// 父分组名称
const parentGroupName = computed(() => {
  if (!groupForm.parentId) return '无 (根分组)';
  
  // 递归查找父分组
  const findParent = (groups, id) => {
    for (const group of groups) {
      if (group.id === id) {
        return group.label;
      }
      if (group.children && group.children.length > 0) {
        const result = findParent(group.children, id);
        if (result) return result;
      }
    }
    return null;
  };
  
  return findParent(groupData.value, groupForm.parentId) || '无 (根分组)';
});

// 监听对话框可见性变化
watch(() => props.visible, (newVal) => {
  dialogVisible.value = newVal;
  if (newVal) {
    // 重新初始化分组数据
    initGroupData();
  }
});

// 监听dialogVisible变化
watch(dialogVisible, (newVal) => {
  emit('update:visible', newVal);
});

// 初始化分组数据
const initGroupData = () => {
  // 深拷贝分组数据，避免直接修改props
  groupData.value = JSON.parse(JSON.stringify(props.groups));
};

// 处理节点点击
const handleNodeClick = (data) => {
  currentGroup.value = data;
  showGroupForm.value = false;
};

// 添加根分组
const handleAddRootGroup = () => {
  resetGroupForm();
  groupForm.parentId = null;
  isEditGroup.value = false;
  showGroupForm.value = true;
};

// 添加子分组
const handleAddChildGroup = (data) => {
  resetGroupForm();
  groupForm.parentId = data.id;
  isEditGroup.value = false;
  showGroupForm.value = true;
};

// 编辑分组
const handleEditGroup = (data) => {
  resetGroupForm();
  groupForm.id = data.id;
  groupForm.name = data.label;
  groupForm.description = data.description || '';
  groupForm.parentId = data.parentId;
  isEditGroup.value = true;
  showGroupForm.value = true;
};

// 删除分组
const handleDeleteGroup = (node, data) => {
  if (data.children && data.children.length > 0) {
    ElMessage.warning('该分组包含子分组，不能删除');
    return;
  }
  
  ElMessageBox.confirm(
    '确定要删除该分组吗？删除后不可恢复。',
    '删除确认',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      // 调用API删除分组
      const res = await UiElementLocatorApi.deleteElementGroup(data.id);
      
      if (res.code === 200) {
        // 从父节点的children中删除
        const parent = node.parent;
        const children = parent.data.children || parent.data;
        const index = children.findIndex(d => d.id === data.id);
        if (index > -1) {
          children.splice(index, 1);
        }
        
        ElMessage.success('删除成功');
        emit('groups-updated', groupData.value);
        
        // 如果删除的是当前选中的分组，清空选中状态
        if (currentGroup.value && currentGroup.value.id === data.id) {
          currentGroup.value = null;
        }
      } else {
        ElMessage.error(res.message || '删除分组失败');
      }
    } catch (error) {
      ElMessage.error('删除分组失败');
      console.error('删除分组出错:', error);
    }
  }).catch(() => {
    // 取消删除
  });
};

// 重置分组表单
const resetGroupForm = () => {
  Object.assign(groupForm, {
    id: '',
    name: '',
    description: '',
    parentId: null
  });
  groupFormRef.value?.resetFields();
};

// 取消分组表单
const cancelGroupForm = () => {
  showGroupForm.value = false;
  resetGroupForm();
};

// 提交分组表单
const submitGroupForm = async () => {
  if (!groupFormRef.value) return;
  
  groupFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        // 调用API保存分组
        let res;
        if (isEditGroup.value) {
          const requestData = {
            groupId: groupForm.id,
            groupName: groupForm.name,
            description: groupForm.description,
            parentId: groupForm.parentId
          };
          res = await UiElementLocatorApi.updateElementGroup(groupForm.id, requestData);
        } else {
          const requestData = {
            groupName: groupForm.name,
            description: groupForm.description,
            parentId: groupForm.parentId
          };
          res = await UiElementLocatorApi.createElementGroup(requestData);
        }
        
        if (res.code === 200) {
          ElMessage.success(isEditGroup.value ? '编辑成功' : '创建成功');
          
          // 刷新分组列表
          const newGroupsRes = await UiElementLocatorApi.getElementGroups();
          if (newGroupsRes.code === 200) {
            // 深拷贝并转换API返回的分组数据
            const formattedGroups = newGroupsRes.data.map(group => ({
              id: group.id,
              label: group.groupName,
              description: group.description,
              parentId: group.parentId,
              children: group.subGroups ? group.subGroups.map(sub => ({
                id: sub.id,
                label: sub.groupName,
                description: sub.description,
                parentId: sub.parentId,
                children: []
              })) : []
            }));
            
            groupData.value = formattedGroups;
            emit('groups-updated', groupData.value);
          }
          
          // 关闭表单，重置数据
          showGroupForm.value = false;
          resetGroupForm();
        } else {
          ElMessage.error(res.message || (isEditGroup.value ? '编辑失败' : '创建失败'));
        }
      } catch (error) {
        ElMessage.error(isEditGroup.value ? '编辑分组失败' : '创建分组失败');
        console.error('保存分组出错:', error);
      }
    }
  });
};

// 初始化
initGroupData();
</script>

<style scoped>
.group-dialog-container {
  display: flex;
  gap: 20px;
  max-height: 500px;
}

.group-tree-container, .group-form-container, .group-info-container {
  flex: 1;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  padding: 10px;
  overflow: auto;
  max-height: 500px;
}

.tree-header, .form-header, .info-header {
  font-weight: bold;
  margin-bottom: 10px;
  padding-bottom: 10px;
  border-bottom: 1px solid #ebeef5;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.tree-actions {
  display: flex;
  gap: 5px;
}

.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}

.tree-node-actions {
  opacity: 0;
  transition: opacity 0.2s;
}

.custom-tree-node:hover .tree-node-actions {
  opacity: 1;
}

.info-content {
  padding: 10px;
}

.info-placeholder {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 200px;
  color: #909399;
}
</style> 