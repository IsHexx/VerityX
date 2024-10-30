<template>
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
      <div 
        class="custom-tree-node"
        @mouseenter="handleMouseEnter(node)"
        @mouseleave="handleMouseLeave(node)"
      >
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
        
        <!-- 文件夹操作按钮 -->
        <div v-if="node.level === 1 && node.isHovered" class="folder-actions">
          <el-button
            type="text"
            size="small"
            @click.stop="handleAddInterface(node, data)"
          >
            <el-icon><Plus /></el-icon>
          </el-button>
          <!-- 修改下拉菜单部分 -->
          <el-dropdown 
            trigger="hover"
            @visible-change="handleDropdownVisibleChange"
            :hide-on-click="false"
            @command="(command) => handleFolderCommand(command, node, data)"
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
                <el-dropdown-item command="addFolder">新增目录</el-dropdown-item>
                <el-dropdown-item command="rename">重命名目录</el-dropdown-item>
                <el-dropdown-item command="delete">删除目录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </template>
  </el-tree>
</el-card>
</template>

<script>
// 新增状态管理
const mouseLeaveTimer = ref(null);
  const isDropdownOpen = ref(false);

  // 修改鼠标进入处理函数
  const handleMouseEnter = (node) => {
    cancelMouseLeave();
    node.isHovered = true;
  };

  // 修改鼠标离开处理函数
  const handleMouseLeaveWithDelay = (node) => {
    if (isDropdownOpen.value) return;
    
    mouseLeaveTimer.value = setTimeout(() => {
      node.isHovered = false;
    }, 100);
  };

  // 取消鼠标离开定时器
  const cancelMouseLeave = () => {
    if (mouseLeaveTimer.value) {
      clearTimeout(mouseLeaveTimer.value);
      mouseLeaveTimer.value = null;
    }
  };

  // 下拉菜单可见性变化处理
  const handleDropdownVisibleChange = (visible) => {
    isDropdownOpen.value = visible;
    if (visible) {
      cancelMouseLeave();
    }
  };

  // 下拉菜单区域离开处理
  const handleDropdownMenuLeave = (node) => {
    if (!isDropdownOpen.value) return;
    
    handleMouseLeaveWithDelay(node);
  };



  const handleAddInterface = (node, data) => {
    // 确保事件不会冒泡
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

  const handleFolderCommand = (command, node, data) => {
    // 确保事件不会冒泡
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
