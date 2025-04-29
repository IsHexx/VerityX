<!-- ApiAutoTestTree.vue -->
<template>
    <div class="api-tree">
      <el-card style="min-height: 660px; height: 100%">
        <template #header>
          <div class="card-header">
            <span>场景列表</span>
          </div>
        </template>
        <el-input
          v-model="filterText"
          style="width: 240px"
          placeholder="请输入接口名称"
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
            <div 
              class="custom-tree-node"
              @mouseenter="handleMouseEnter(node)"
              @mouseleave="handleMouseLeaveWithDelay(node)"
            >
              <span class="icon-container">
                <el-icon
                  v-if="node.level === 1"
                  class="folder-icon"
                  :style="{ color: getFolderColor(data.id) }"
                >
                  <Folder />
                </el-icon>
                <el-icon v-else-if="node.level === 2" style="color: #45c8dc"><Postcard /></el-icon>
              </span>
              <span>{{ node.label }}</span>
              
              <!-- 文件夹操作按钮 (level 1) -->
              <div v-if="node.level === 1 && node.isHovered" class="node-actions">
                <el-button
                  type="text"
                  size="small"
                  @click.stop="handleAddInterface(node, data)"
                >
                  <el-icon><Plus /></el-icon>
                </el-button>
                <el-dropdown 
                  trigger="hover"
                  @visible-change="handleDropdownVisibleChange"
                  :hide-on-click="false"
                  @command="(command) => handleCommand(command, node, data)"
                >
                  <el-button 
                    type="text" 
                    size="small" 
                    @mouseenter="cancelMouseLeave"
                  >
                    <el-icon><More /></el-icon>
                  </el-button>
                  <template #dropdown>
                    <el-dropdown-menu 
                      @mouseenter="cancelMouseLeave"
                      @mouseleave="handleDropdownMenuLeave(node)"
                    >
                      <el-dropdown-item command="addFolder">新增用例</el-dropdown-item>
                      <el-dropdown-item command="rename">重命名目录</el-dropdown-item>
                      <el-dropdown-item command="delete">删除目录</el-dropdown-item>
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>
              </div>
  
              <!-- 用例组按钮 (level 2) -->
              <div v-if="node.level === 2 && node.isHovered" class="node-actions">
                <el-dropdown 
                  trigger="hover"
                  @visible-change="handleDropdownVisibleChange"
                  :hide-on-click="false"
                  @command="(command) => handleCommand(command, node, data)"
                >
                  <el-button 
                    type="text" 
                    size="small" 
                    @mouseenter="cancelMouseLeave"
                  >
                    <el-icon ><More /></el-icon>
                  </el-button>
                  <template #dropdown>
                    <el-dropdown-menu 
                      @mouseenter="cancelMouseLeave"
                      @mouseleave="handleDropdownMenuLeave(node)"
                    >
                      <el-dropdown-item command="delete">删除用例</el-dropdown-item>
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>
              </div>
            </div>
          </template>
        </el-tree>
      </el-card>
    </div>
  </template>
  <script setup>
  import { ref, watch } from 'vue';
  import { Folder, Link, Plus, More, Postcard } from "@element-plus/icons-vue";
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
      label: "根目录",
      children: [
        {
          id: 4,
          label: "获取用户列表",
        },
        {
          id: 5,
          label: "创建用户",
        },
      ],
    },
    {
      id: 2,
      label: "用户接口",
      children: [
        {
          id: 6,
          label: "更新订单",
        },
        {
          id: 7,
          label: "删除订单",
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
    ElMessageBox.prompt('请输入子目录名称', '新增目录', {
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