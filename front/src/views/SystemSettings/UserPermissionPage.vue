<template>
  <div class="user-permission-container">
    <el-tabs v-model="activeTab" class="system-tabs">
      <el-tab-pane label="用户管理" name="user">
        <div class="tab-content">
          <el-card shadow="hover" class="system-card">
            <template #header>
              <div class="card-header">
                <span>用户列表</span>
                <el-button type="primary" size="small" @click="handleAddUser">新增用户</el-button>
              </div>
            </template>
            <el-table :data="userList" style="width: 100%" v-loading="loading">
              <el-table-column prop="username" label="用户名" />
              <el-table-column prop="realName" label="姓名" />
              <el-table-column prop="email" label="邮箱" />
              <el-table-column prop="department" label="部门" />
              <el-table-column prop="role" label="角色" />
              <el-table-column prop="status" label="状态">
                <template #default="scope">
                  <el-tag :type="scope.row.status === '启用' ? 'success' : 'danger'">
                    {{ scope.row.status }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="200">
                <template #default="scope">
                  <el-button type="primary" link @click="handleEditUser(scope.row)">编辑</el-button>
                  <el-button 
                    type="primary" 
                    link 
                    @click="handleToggleUserStatus(scope.row)"
                  >
                    {{ scope.row.status === '启用' ? '禁用' : '启用' }}
                  </el-button>
                  <el-button type="primary" link @click="handleResetPassword(scope.row)">重置密码</el-button>
                </template>
              </el-table-column>
            </el-table>
            <div class="pagination-container">
              <el-pagination
                background
                layout="total, sizes, prev, pager, next"
                :total="total"
                :page-size="pageSize"
                :current-page="currentPage"
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
              />
            </div>
          </el-card>
        </div>
      </el-tab-pane>
      
      <el-tab-pane label="角色管理" name="role">
        <div class="tab-content">
          <el-card shadow="hover" class="system-card">
            <template #header>
              <div class="card-header">
                <span>角色列表</span>
                <el-button type="primary" size="small" @click="handleAddRole">新增角色</el-button>
              </div>
            </template>
            <el-table :data="roleList" style="width: 100%" v-loading="loading">
              <el-table-column prop="roleName" label="角色名称" />
              <el-table-column prop="description" label="描述" />
              <el-table-column prop="userCount" label="用户数量" />
              <el-table-column prop="createTime" label="创建时间" />
              <el-table-column label="操作" width="200">
                <template #default="scope">
                  <el-button type="primary" link @click="handleEditRole(scope.row)">编辑</el-button>
                  <el-button type="primary" link @click="handleAssignPermission(scope.row)">分配权限</el-button>
                  <el-button type="danger" link @click="handleDeleteRole(scope.row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </div>
      </el-tab-pane>
      
      <el-tab-pane label="部门管理" name="department">
        <div class="tab-content">
          <el-row :gutter="20">
            <el-col :span="8">
              <el-card shadow="hover" class="system-card">
                <template #header>
                  <div class="card-header">
                    <span>部门结构</span>
                    <el-button type="primary" size="small" @click="handleAddDepartment">新增部门</el-button>
                  </div>
                </template>
                <el-tree
                  :data="departmentTree"
                  :props="defaultProps"
                  @node-click="handleNodeClick"
                  default-expand-all
                  highlight-current
                ></el-tree>
              </el-card>
            </el-col>
            <el-col :span="16">
              <el-card shadow="hover" class="system-card">
                <template #header>
                  <div class="card-header">
                    <span>{{ currentDepartment ? currentDepartment.name + ' - 部门成员' : '部门成员' }}</span>
                  </div>
                </template>
                <el-table :data="departmentMembers" style="width: 100%" v-loading="loading">
                  <el-table-column prop="username" label="用户名" />
                  <el-table-column prop="realName" label="姓名" />
                  <el-table-column prop="email" label="邮箱" />
                  <el-table-column prop="role" label="角色" />
                  <el-table-column label="操作" width="150">
                    <template #default="scope">
                      <el-button type="primary" link @click="handleEditMember(scope.row)">编辑</el-button>
                      <el-button type="danger" link @click="handleRemoveMember(scope.row)">移除</el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </el-card>
            </el-col>
          </el-row>
        </div>
      </el-tab-pane>
      
      <el-tab-pane label="权限配置" name="permission">
        <div class="tab-content">
          <el-card shadow="hover" class="system-card">
            <template #header>
              <div class="card-header">
                <span>权限配置</span>
              </div>
            </template>
            <el-tabs v-model="permissionActiveTab" tab-position="left">
              <el-tab-pane label="功能权限" name="function">
                <el-tree
                  :data="functionPermissionTree"
                  show-checkbox
                  node-key="id"
                  :props="permissionProps"
                  default-expand-all
                ></el-tree>
              </el-tab-pane>
              <el-tab-pane label="数据权限" name="data">
                <el-table :data="dataPermissionList" style="width: 100%">
                  <el-table-column prop="name" label="权限名称" />
                  <el-table-column prop="description" label="描述" />
                  <el-table-column prop="type" label="类型" />
                  <el-table-column label="操作" width="150">
                    <template #default="scope">
                      <el-button type="primary" link @click="handleEditDataPermission(scope.row)">编辑</el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </el-tab-pane>
              <el-tab-pane label="审批权限" name="approval">
                <el-table :data="approvalPermissionList" style="width: 100%">
                  <el-table-column prop="name" label="审批流程" />
                  <el-table-column prop="description" label="描述" />
                  <el-table-column prop="roles" label="拥有权限角色" />
                  <el-table-column label="操作" width="150">
                    <template #default="scope">
                      <el-button type="primary" link @click="handleEditApprovalPermission(scope.row)">配置</el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </el-tab-pane>
            </el-tabs>
          </el-card>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';

// 选项卡状态
const activeTab = ref('user');
const permissionActiveTab = ref('function');
const loading = ref(false);

// 分页数据
const total = ref(0);
const pageSize = ref(10);
const currentPage = ref(1);

// 用户管理数据
const userList = ref([
  { id: 1, username: 'admin', realName: '管理员', email: 'admin@example.com', department: '技术部', role: '超级管理员', status: '启用' },
  { id: 2, username: 'tester1', realName: '测试人员1', email: 'tester1@example.com', department: '测试部', role: '测试人员', status: '启用' },
  { id: 3, username: 'dev1', realName: '开发人员1', email: 'dev1@example.com', department: '开发部', role: '开发人员', status: '启用' },
  { id: 4, username: 'pm1', realName: '项目经理1', email: 'pm1@example.com', department: '项目部', role: '项目经理', status: '禁用' },
]);

// 角色管理数据
const roleList = ref([
  { id: 1, roleName: '超级管理员', description: '拥有系统所有权限', userCount: 1, createTime: '2023-01-01' },
  { id: 2, roleName: '测试人员', description: '拥有测试相关权限', userCount: 5, createTime: '2023-01-02' },
  { id: 3, roleName: '开发人员', description: '拥有开发相关权限', userCount: 8, createTime: '2023-01-03' },
  { id: 4, roleName: '项目经理', description: '拥有项目管理相关权限', userCount: 3, createTime: '2023-01-04' },
]);

// 部门管理数据
const departmentTree = ref([
  {
    id: 1,
    name: '公司总部',
    children: [
      {
        id: 2,
        name: '技术部',
        children: [
          { id: 5, name: '开发组' },
          { id: 6, name: '测试组' },
          { id: 7, name: '运维组' },
        ]
      },
      {
        id: 3,
        name: '市场部',
        children: [
          { id: 8, name: '销售组' },
          { id: 9, name: '营销组' },
        ]
      },
      { id: 4, name: '人事部' },
    ]
  }
]);

const defaultProps = {
  children: 'children',
  label: 'name'
};

const currentDepartment = ref(null);
const departmentMembers = ref([]);

// 权限配置数据
const functionPermissionTree = ref([
  {
    id: 1,
    label: '系统管理',
    children: [
      { id: 11, label: '用户管理' },
      { id: 12, label: '角色管理' },
      { id: 13, label: '部门管理' },
      { id: 14, label: '权限配置' },
    ]
  },
  {
    id: 2,
    label: '测试管理',
    children: [
      { id: 21, label: '测试计划' },
      { id: 22, label: '测试用例' },
      { id: 23, label: '缺陷管理' },
      { id: 24, label: '测试报告' },
    ]
  },
  {
    id: 3,
    label: '接口测试',
    children: [
      { id: 31, label: '接口管理' },
      { id: 32, label: '接口自动化' },
      { id: 33, label: '测试报告' },
      { id: 34, label: '全局配置' },
    ]
  },
  {
    id: 4,
    label: 'UI测试',
    children: [
      { id: 41, label: '测试用例管理' },
      { id: 42, label: '测试套件管理' },
      { id: 43, label: '元素定位器管理' },
      { id: 44, label: '测试执行' },
      { id: 45, label: '测试报告' },
      { id: 46, label: '测试配置' },
    ]
  }
]);

const permissionProps = {
  children: 'children',
  label: 'label'
};

const dataPermissionList = ref([
  { id: 1, name: '项目数据权限', description: '控制用户可访问的项目范围', type: '项目' },
  { id: 2, name: '测试用例权限', description: '控制用户可访问的测试用例范围', type: '测试用例' },
  { id: 3, name: '测试报告权限', description: '控制用户可访问的测试报告范围', type: '测试报告' },
]);

const approvalPermissionList = ref([
  { id: 1, name: '测试计划审批', description: '测试计划创建与变更审批流程', roles: '项目经理、测试经理' },
  { id: 2, name: '测试报告审批', description: '测试报告发布审批流程', roles: '项目经理、测试经理、QA' },
  { id: 3, name: '发布审批', description: '系统发布上线审批流程', roles: '项目经理、运维经理、总监' },
]);

// 用户管理方法
const handleAddUser = () => {
  console.log('添加用户');
};

const handleEditUser = (row) => {
  console.log('编辑用户', row);
};

const handleToggleUserStatus = (row) => {
  console.log('切换用户状态', row);
};

const handleResetPassword = (row) => {
  console.log('重置密码', row);
};

// 角色管理方法
const handleAddRole = () => {
  console.log('添加角色');
};

const handleEditRole = (row) => {
  console.log('编辑角色', row);
};

const handleAssignPermission = (row) => {
  console.log('分配权限', row);
};

const handleDeleteRole = (row) => {
  console.log('删除角色', row);
};

// 部门管理方法
const handleNodeClick = (data) => {
  currentDepartment.value = data;
  // 模拟获取部门成员
  departmentMembers.value = [
    { id: 1, username: 'admin', realName: '管理员', email: 'admin@example.com', role: '超级管理员' },
    { id: 2, username: 'tester1', realName: '测试人员1', email: 'tester1@example.com', role: '测试人员' },
  ];
};

const handleAddDepartment = () => {
  console.log('添加部门');
};

const handleEditMember = (row) => {
  console.log('编辑成员', row);
};

const handleRemoveMember = (row) => {
  console.log('移除成员', row);
};

// 权限配置方法
const handleEditDataPermission = (row) => {
  console.log('编辑数据权限', row);
};

const handleEditApprovalPermission = (row) => {
  console.log('编辑审批权限', row);
};

// 分页方法
const handleSizeChange = (val) => {
  pageSize.value = val;
  // 获取数据
};

const handleCurrentChange = (val) => {
  currentPage.value = val;
  // 获取数据
};

onMounted(() => {
  // 初始化加载数据
});
</script>

<style scoped>
.user-permission-container {
  padding: 20px;
}

.system-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.tab-content {
  margin-top: 20px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.system-tabs {
  min-height: 500px;
}
</style> 