<!-- ApiTree.vue -->
<template>
  <div class="api-tree">
    <el-card style="min-height: 660px; height: 100%">
      <template #header>
        <div class="card-header">
          <span>接口列表</span>
        </div>
      </template>
      <el-input
        v-model="filterText"
        style="width: 240px"
        placeholder="请输入接口名称"
      />
      <el-tree :data="data" node-key="label">
    <template #default="{ node, data }">
      <span class="custom-tree-node" @mouseover="mouseover(data)" @mouseleave="mouseout(data)">
        <span>{{ node.label }}</span>
        <div v-show="data.dropdownShow" @click.stop>
          <el-dropdown placement="right-start" trigger="click">
            <i class="el-icon-s-operation"></i>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item icon="el-icon-plus" @click="addPeerTree(node, data)">新增同级</el-dropdown-item>
                <el-dropdown-item icon="el-icon-circle-plus" @click="addChildrenTree(node, data)">新增下级</el-dropdown-item>
                <el-dropdown-item icon="el-icon-edit-outline" @click="updateTree(node, data)">修改</el-dropdown-item>
                <el-dropdown-item icon="el-icon-delete-solid" @click="delTree(node, data)">删除</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </span>
    </template>
  </el-tree>
    </el-card>
  </div>
</template>
<script setup>
import { ref, watch } from 'vue';
import { Folder, Link, Plus, More } from "@element-plus/icons-vue";
import { ElMessage, ElMessageBox } from 'element-plus';

const treeRef = ref(null);
const mouseLeaveTimer = ref(null);
const isDropdownOpen = ref(false);
const filterText = ref("");

// 事件监听
const handleClick = (tab, event) => {
  console.log(tab, event);
};

//
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


// 处理各种操作命令
const handleCommand = (command, node, data) => {
  event?.stopPropagation();
  switch (command) {
    case 'addFolder':
      handleAddFolder(node, data);
      break;
    case 'rename':
      handleRename(node, data);
      break;
    case 'delete':
      handleDelete(node, data);
      break;
  }
};

// 新增用例
const handleAddCase = (node, data) => {
  event?.stopPropagation();
  ElMessageBox.prompt('请输入用例名称', '新增用例', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
  }).then(({ value }) => {
    if (value) {
      const newCase = {
        id: Date.now(),
        label: value,
      };
      data.children.push(newCase);
      ElMessage.success('用例添加成功');
    }
  });
};

// 新增文件夹
const handleAddFolder = (node, data) => {
  ElMessageBox.prompt('请输入目录名称', '新增目录', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
  }).then(({ value }) => {
    if (value) {
      const newFolder = {
        id: Date.now(),
        label: value,
        children: [],
      };
      data.children = [...(data.children || []), newFolder];
      ElMessage.success('目录添加成功');
    }
  });
};

// 重命名
const handleRename = (node, data) => {
  const title = node.level === 3 ? '重命名用例' : '重命名目录';
  ElMessageBox.prompt('请输入新名称', title, {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    inputValue: data.label,
  }).then(({ value }) => {
    if (value) {
      data.label = value;
      ElMessage.success('重命名成功');
    }
  });
};

// 删除
const handleDelete = (node, data) => {
  const typeMap = {
    1: '目录',
    2: '接口',
    3: '用例'
  };
  const type = typeMap[node.level];
  
  ElMessageBox.confirm(`确认删除该${type}吗？`, '删除确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(() => {
    const parent = node.parent;
    const children = parent.data.children || parent.data;
    const index = children.findIndex(d => d.id === data.id);
    children.splice(index, 1);
    ElMessage.success('删除成功');
  });
};
// // 鼠标进入处理函数
const handleMouseEnter = (node) => {
  cancelMouseLeave();
  node.isHovered = true;
};
// 鼠标离开处理函数
const handleMouseLeaveWithDelay = (node) => {
  mouseLeaveTimer.value = setTimeout(() => {
    if (!isDropdownOpen.value) {
      node.isHovered = false;
    }
  }, 100);
};
// 取消鼠标离开处理函数
const cancelMouseLeave = () => {
  if (mouseLeaveTimer.value) {
    clearTimeout(mouseLeaveTimer.value);
    mouseLeaveTimer.value = null;
  }
};

// 下拉菜单可见性变化处理
const handleDropdownVisibleChange = (visible) => {
  isDropdownOpen.value = visible;
  if (!visible) {
    const node = document.querySelector('.is-hovering')?.parentNode.__vueParentComponent.props.node;
    if (node) {
      node.isHovered = false;
    }
  }
};

// 下拉菜单区域离开处理
const handleDropdownMenuLeave = (node) => {
  isDropdownOpen.value = false;
  node.isHovered = false;
};

// 添加接口
const handleAddInterface = (node, data) => {
  event?.stopPropagation();
  ElMessageBox.prompt('请输入接口名称', '新增接口', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
  }).then(({ value }) => {
    if (value) {
      const newInterface = {
        id: Date.now(),
        label: value,
        method: 'GET',
        children: [{ id: Date.now() + 1, label: '接口详情' }],
      };
      data.children.push(newInterface);
      ElMessage.success('接口添加成功');
    }
  });
};

// 文件夹操作
const handleFolderCommand = (command, node, data) => {
  event?.stopPropagation();
  switch (command) {
    case 'addFolder':
      ElMessageBox.prompt('请输入目录名称', '新增目录', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
      }).then(({ value }) => {
        if (value) {
          const newFolder = {
            id: Date.now(),
            label: value,
            children: [],
          };
          data.children = [...(data.children || []), newFolder];
          ElMessage.success('目录添加成功');
        }
      });
      break;
    case 'rename':
      ElMessageBox.prompt('请输入新的目录名称', '重命名目录', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputValue: data.label,
      }).then(({ value }) => {
        if (value) {
          data.label = value;
          ElMessage.success('重命名成功');
        }
      });
      break;
    case 'delete':
      ElMessageBox.confirm('确认删除该目录吗？', '删除确认', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
        const parent = node.parent;
        const children = parent.data.children || parent.data;
        const index = children.findIndex(d => d.id === data.id);
        children.splice(index, 1);
        ElMessage.success('删除成功');
      });
      break;
  }
};

// 获取文件夹颜色
const getFolderColor = (id) => {
  const colors = ["#409EFF", "#67C23A", "#E6A23C", "#F56C6C", "#909399"];
  return colors[id % colors.length];
};

// 过滤节点
const filterNode = (value, data) => {
  if (!value) return true;
  return data.label.includes(value);
};
// 重置表单
const resetForm = (formEl) => {
  if (!formEl) return;
  formEl.resetFields();
};

watch(filterText, (val) => {
  if (treeRef.value) {
    treeRef.value.filter(val);
  }
});

</script>

<style scoped>
.api-tree {
  padding: 0px;
}

:deep(.el-tree-node__content:hover) {
  background-color: #f5f7fa;
}

.custom-tree-node:hover .folder-actions {
  display: flex;
  align-items: center;
}

.folder-actions {
  display: none;
  position: absolute;
  right: 8px;
}

.folder-actions .el-button {
  padding: 2px 4px;
  margin-left: 4px;
}

.folder-actions {
  display: flex;
  position: absolute;
  right: 8px;
}

.custom-tree-node:hover .node-actions {
  display: flex;
}

.custom-tree-node {
  position: relative;
  display: flex;
  align-items: center;
  width: calc(100% - 24px); /* 减去树的缩进空间 */
}

.node-actions {
  display: none;
  align-items: center;
  position: absolute;
  right: -24px;
}

.node-actions .el-button {
  padding: 2px 4px;
  margin-left: 4px;
}

:deep(.el-tree-node__content) {
  padding-right: 24px !important; /* 确保有足够空间显示按钮 */
  position: relative;
  height: 32px;
}

.folder-icon {
  font-size: 18px;
}

.icon-container {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  margin-right: 4px;
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

.get { background-color: #95d475; color: #fff; }
.post { background-color: #409eff; color: #fff; }
.put { background-color: #e6a23c; color: #fff; }
.delete { background-color: #f56c6c; color: #fff; }

.custom-tree-node {
  display: flex;
  align-items: center;
}

.folder-actions {
  display: none;
  position: absolute;
  right: 8px;
}

.custom-tree-node {
  position: relative;
  display: flex;
  align-items: center;
  width: 100%;
}

.custom-tree-node:hover .folder-actions {
  display: flex;
  align-items: center;
}

.folder-actions .el-button {
  padding: 2px 4px;
  margin-left: 4px;
}

:deep(.el-tree-node__content:hover) {
  background-color: #f5f7fa;
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