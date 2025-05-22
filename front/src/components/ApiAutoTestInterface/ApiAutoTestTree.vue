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
          v-loading="loading"
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
  import { ref, watch, inject, onMounted } from 'vue';
  import { Folder, Link, Plus, More, Postcard } from "@element-plus/icons-vue";
  import { ElMessage, ElMessageBox } from 'element-plus';
  import ApiAutomationApi from '@/api/apiAutomationService';
  
  const treeRef = ref(null);
  const mouseLeaveTimer = ref(null);
  const isDropdownOpen = ref(false);
  const filterText = ref("");
  const loading = ref(false);
  
  // 获取项目存储
  const projectStore = inject('projectStore');
  
  // 事件监听
  const handleClick = (tab, event) => {
    console.log(tab, event);
  };
  
  // 数据结构
  const data = ref([]);
  
  // 加载场景数据
  const loadScenes = () => {
    loading.value = true;
    console.log("加载场景数据, 项目ID:", projectStore.currentProjectId);
    
    ApiAutomationApi.getAllScenes()
      .then(response => {
        console.log("场景数据加载成功:", response);
        
        if (response && response.data) {
          // 将API数据转换为树形结构
          transformApiDataToTree(response.data);
        } else {
          // 如果没有数据，显示默认空结构
          data.value = [
            {
              id: 1,
              label: "默认目录",
              children: []
            }
          ];
        }
      })
      .catch(error => {
        console.error("场景数据加载失败:", error);
        ElMessage.error("场景数据加载失败");
        // 加载失败时显示默认结构
        data.value = [
          {
            id: 1,
            label: "默认目录",
            children: []
          }
        ];
      })
      .finally(() => {
        loading.value = false;
      });
  };
  
  // 转换API数据为树形结构
  const transformApiDataToTree = (apiData) => {
    // 如果没有数据，使用默认结构
    if (!apiData || apiData.length === 0) {
      data.value = [
        {
          id: 1,
          label: "默认目录",
          children: []
        }
      ];
      return;
    }
    
    // 简单实现 - 可以根据实际数据结构调整
    const folders = {};
    
    // 第一遍遍历，创建所有目录
    apiData.forEach(scene => {
      if (scene.parentId === 0 || !scene.parentId) {
        // 这是顶级场景，作为文件夹
        folders[scene.sceneId] = {
          id: scene.sceneId,
          label: scene.sceneName,
          children: []
        };
      }
    });
    
    // 第二遍遍历，将场景添加到对应的目录中
    apiData.forEach(scene => {
      if (scene.parentId && scene.parentId !== 0 && folders[scene.parentId]) {
        // 这是子场景，添加到对应的父场景下
        folders[scene.parentId].children.push({
          id: scene.sceneId,
          label: scene.sceneName
        });
      }
    });
    
    // 转换为数组
    data.value = Object.values(folders);
    
    // 如果没有数据，添加默认文件夹
    if (data.value.length === 0) {
      data.value = [
        {
          id: 1,
          label: "默认目录",
          children: []
        }
      ];
    }
  };
  
  // 组件挂载时加载数据
  onMounted(() => {
    loadScenes();
  });
  
  // 监听项目ID变化，重新加载数据
  watch(() => projectStore.currentProjectId, (newVal, oldVal) => {
    if (newVal !== oldVal) {
      loadScenes();
    }
  });
  
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
        
        // 创建场景
        const sceneData = {
          sceneName: value,
          parentId: data.id,
          projectId: projectStore.currentProjectId
        };
        
        ApiAutomationApi.createScene(sceneData)
          .then(response => {
            console.log("用例创建成功:", response);
            if (response && response.data) {
              newCase.id = response.data.sceneId;
              data.children.push(newCase);
              ElMessage.success('用例添加成功');
            }
          })
          .catch(error => {
            console.error("用例创建失败:", error);
            ElMessage.error('用例添加失败');
          });
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
        
        // 创建场景
        const sceneData = {
          sceneName: value,
          parentId: 0, // 顶级目录
          projectId: projectStore.currentProjectId
        };
        
        ApiAutomationApi.createScene(sceneData)
          .then(response => {
            console.log("目录创建成功:", response);
            if (response && response.data) {
              newFolder.id = response.data.sceneId;
              data.children = [...(data.children || []), newFolder];
              ElMessage.success('目录添加成功');
            }
          })
          .catch(error => {
            console.error("目录创建失败:", error);
            ElMessage.error('目录添加失败');
          });
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
        // 更新场景
        const sceneData = {
          sceneId: data.id,
          sceneName: value,
          projectId: projectStore.currentProjectId
        };
        
        ApiAutomationApi.updateScene(data.id, sceneData)
          .then(() => {
            data.label = value;
            ElMessage.success('重命名成功');
          })
          .catch(error => {
            console.error("重命名失败:", error);
            ElMessage.error('重命名失败');
          });
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
      ApiAutomationApi.deleteScene(data.id)
        .then(() => {
          const parent = node.parent;
          const children = parent.data.children || parent.data;
          const index = children.findIndex(d => d.id === data.id);
          children.splice(index, 1);
          ElMessage.success('删除成功');
        })
        .catch(error => {
          console.error("删除失败:", error);
          ElMessage.error('删除失败');
        });
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
        
        // 创建场景
        const sceneData = {
          sceneName: value,
          parentId: data.id,
          projectId: projectStore.currentProjectId
        };
        
        ApiAutomationApi.createScene(sceneData)
          .then(response => {
            console.log("接口添加成功:", response);
            if (response && response.data) {
              newInterface.id = response.data.sceneId;
              data.children.push(newInterface);
              ElMessage.success('接口添加成功');
            }
          })
          .catch(error => {
            console.error("接口添加失败:", error);
            ElMessage.error('接口添加失败');
          });
      }
    });
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