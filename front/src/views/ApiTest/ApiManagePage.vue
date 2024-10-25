<template>
  <div class="content">
    <el-card style="width: 23%; min-height:660px; height: 100%">
      <template #header>
        <div class="card-header">
          <span>接口列表</span>
        </div>
      </template>
      <el-input v-model="filterText" style="width: 240px" placeholder="Filter keyword" />
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
            <!-- 一级菜单显示彩色文件夹 -->
            <el-icon 
              v-if="node.level === 1" 
              class="folder-icon"
              :style="{ color: getFolderColor(data.id) }"
            >
              <Folder />
            </el-icon>
            <!-- 二级菜单显示 HTTP 方法标签 -->
            <span 
              v-else-if="node.level === 2" 
              class="http-method"
              :class="data.method?.toLowerCase()"
            >
              {{ data.method || 'GET' }}
            </span>
            <!-- 三级菜单的图标 -->
            <el-icon v-else-if="node.level === 3"><Link /></el-icon>
            <span>{{ node.label }}</span>
          </span>
        </template>
      </el-tree>
    </el-card>
    <el-card style="width: 76%;">
    <template #header>
      <div class="card-header">
        <span>Card name</span>
      </div>
    </template>
    ...
    <template #footer>Footer content</template>
  </el-card>
  </div>
</template>

<script setup>
import { ref, watch } from "vue";
import { ElTree } from "element-plus";
import { Folder, Link } from '@element-plus/icons-vue'

const filterText = ref("");
const treeRef = ref(null);

const defaultProps = {
  children: "children",
  label: "label",
};

// 获取文件夹颜色的函数
const getFolderColor = (id) => {
  const colors = ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399'];
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

// 修改数据结构，添加 method 属性
const data = [
  {
    id: 1,
    label: "用户管理",
    children: [
      {
        id: 4,
        label: "获取用户列表",
        method: "GET",
        children: [
          {
            id: 9,
            label: "接口详情 1",
          },
        ],
      },
      {
        id: 5,
        label: "创建用户",
        method: "POST",
        children: [
          {
            id: 10,
            label: "接口详情 2",
          },
        ],
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
        children: [
          {
            id: 11,
            label: "接口详情 3",
          },
        ],
      },
      {
        id: 7,
        label: "删除订单",
        method: "DELETE",
        children: [
          {
            id: 12,
            label: "接口详情 4",
          },
        ],
      },
    ],
  },
];
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
  gap: 4px;
}

.folder-icon {
  font-size: 18px;
}

.http-method {
  padding: 1px 4px;
  border-radius: 4px;
  font-size: 8px;
  font-weight:normal;
  text-transform: uppercase;
  min-width: 10px;
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

:deep(.el-icon) {
  margin-right: 4px;
}

::v-deep .el-tree-node__content>.el-tree-node__expand-icon {
  position: absolute;
  opacity: 0;
}

::v-deep .el-tree-node__content {
  position: relative;
}
</style>