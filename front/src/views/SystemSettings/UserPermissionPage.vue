<template>
  <div class="user-permission-container">
    <el-tabs v-model="activeTab" class="system-tabs" @tab-change="handleTabChange">
      <el-tab-pane label="用户管理" name="user">
        <div class="tab-content">
          <el-card shadow="hover" class="system-card">
            <template #header>
              <div class="card-header">
                <span>用户列表</span>
                <div>
                  <el-button type="primary" size="small" @click="handleAddUser">新增用户</el-button>
                  <el-button type="danger" size="small" @click="handleBatchDeleteUsers" :disabled="selectedUsers.length === 0">批量删除</el-button>
                </div>
              </div>
            </template>
            <el-table 
              :data="userList" 
              style="width: 100%" 
              v-loading="loading"
              @selection-change="handleSelectionChange"
            >
              <el-table-column type="selection" width="55" />
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
              <el-table-column label="操作" width="250">
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
                  <el-button type="danger" link @click="handleDeleteUser(scope.row)">删除</el-button>
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
                >
                  <template #default="{ node, data }">
                    <div class="custom-tree-node">
                      <span>{{ node.label }}</span>
                      <span class="node-actions">
                        <el-button type="primary" link @click.stop="handleEditDepartment(data)">编辑</el-button>
                        <el-button type="danger" link @click.stop="handleDeleteDepartment(data)">删除</el-button>
                      </span>
                    </div>
                  </template>
                </el-tree>
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
                <div class="permission-toolbar">
                  <el-button type="primary" size="small" @click="handleAddPermission">新增权限</el-button>
                </div>
                <el-tree
                  :data="functionPermissionTree"
                  show-checkbox
                  node-key="id"
                  :props="permissionProps"
                  default-expand-all
                  ref="permissionTreeRef"
                >
                  <template #default="{ node, data }">
                    <div class="custom-tree-node">
                      <span>{{ node.label }}</span>
                      <span class="node-actions">
                        <el-button type="primary" link @click.stop="handleEditPermission(data)">编辑</el-button>
                        <el-button type="danger" link @click.stop="handleDeletePermission(data)">删除</el-button>
                      </span>
                    </div>
                  </template>
                </el-tree>
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
    
    <!-- 用户表单对话框 -->
    <el-dialog v-model="userFormVisible" :title="userFormTitle" width="500px">
      <el-form :model="userForm" label-width="80px">
        <el-form-item label="用户名" required>
          <el-input v-model="userForm.username" placeholder="请输入用户名" :disabled="userForm.id !== null" />
        </el-form-item>
        <el-form-item label="密码" v-if="userForm.id === null" required>
          <el-input v-model="userForm.password" type="password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="姓名" required>
          <el-input v-model="userForm.realName" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="userForm.nickname" placeholder="请输入昵称" />
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="userForm.gender" placeholder="请选择性别" style="width: 100%;">
            <el-option label="男" value="0" />
            <el-option label="女" value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="userForm.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="userForm.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="部门">
          <el-select v-model="userForm.deptId" placeholder="请选择部门" style="width: 100%;">
            <el-option v-for="dept in departmentTree" :key="dept.id" :label="dept.deptName" :value="dept.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="userForm.roleIds" multiple placeholder="请选择角色" style="width: 100%;">
            <el-option v-for="role in roleList" :key="role.id" :label="role.roleName" :value="role.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="userForm.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="userFormVisible = false">取消</el-button>
          <el-button type="primary" @click="submitUserForm">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 角色表单对话框 -->
    <el-dialog v-model="roleFormVisible" :title="roleFormTitle" width="500px">
      <el-form :model="roleForm" label-width="80px">
        <el-form-item label="角色名称" required>
          <el-input v-model="roleForm.roleName" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="角色编码" required>
          <el-input v-model="roleForm.roleCode" placeholder="请输入角色编码" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="roleForm.description" placeholder="请输入描述" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="roleForm.sort" :min="0" :max="99" />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="roleForm.isActive" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="roleForm.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="roleFormVisible = false">取消</el-button>
          <el-button type="primary" @click="submitRoleForm">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 部门表单对话框 -->
    <el-dialog v-model="deptFormVisible" :title="deptFormTitle" width="500px">
      <el-form :model="deptForm" label-width="80px">
        <el-form-item label="部门名称" required>
          <el-input v-model="deptForm.deptName" placeholder="请输入部门名称" />
        </el-form-item>
        <el-form-item label="部门编码" required>
          <el-input v-model="deptForm.deptCode" placeholder="请输入部门编码" />
        </el-form-item>
        <el-form-item label="上级部门">
          <el-select v-model="deptForm.parentId" clearable placeholder="请选择上级部门" style="width: 100%;">
            <el-option v-for="dept in departmentTree" :key="dept.id" :label="dept.deptName" :value="dept.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="负责人">
          <el-input v-model="deptForm.leader" placeholder="请输入负责人" />
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="deptForm.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="deptForm.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="deptForm.sort" :min="0" :max="99" />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="deptForm.isActive" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="deptForm.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="deptFormVisible = false">取消</el-button>
          <el-button type="primary" @click="submitDeptForm">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 分配权限对话框 -->
    <el-dialog v-model="assignPermissionVisible" title="分配权限" width="500px">
      <el-form label-width="80px">
        <el-form-item label="角色名称">
          <span>{{ currentRole ? currentRole.roleName : '' }}</span>
        </el-form-item>
        <el-form-item label="权限">
          <el-tree
            :data="functionPermissionTree"
            show-checkbox
            node-key="id"
            :props="permissionProps"
            :default-checked-keys="selectedPermissions"
            ref="permissionTree"
            default-expand-all
            @check="handlePermissionCheck"
          ></el-tree>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="assignPermissionVisible = false">取消</el-button>
          <el-button type="primary" @click="submitPermissionAssign">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 权限表单对话框 -->
    <el-dialog v-model="permissionFormVisible" :title="permissionFormTitle" width="500px">
      <el-form :model="permissionForm" label-width="100px">
        <el-form-item label="权限名称" required>
          <el-input v-model="permissionForm.permissionName" placeholder="请输入权限名称" />
        </el-form-item>
        <el-form-item label="权限编码" required>
          <el-input v-model="permissionForm.permissionCode" placeholder="请输入权限编码" />
        </el-form-item>
        <el-form-item label="权限类型">
          <el-select v-model="permissionForm.permissionType" placeholder="请选择权限类型" style="width: 100%;">
            <el-option label="菜单" value="menu" />
            <el-option label="按钮" value="button" />
            <el-option label="接口" value="api" />
          </el-select>
        </el-form-item>
        <el-form-item label="上级权限">
          <el-select v-model="permissionForm.parentId" clearable placeholder="请选择上级权限" style="width: 100%;">
            <el-option key="0" label="无上级权限" :value="0" />
            <el-option 
              v-for="perm in flatPermissionList" 
              :key="perm.id" 
              :label="perm.permissionName" 
              :value="perm.id"
              :disabled="perm.id === permissionForm.id" 
            />
          </el-select>
        </el-form-item>
        <el-form-item label="路由地址" v-if="permissionForm.permissionType === 'menu'">
          <el-input v-model="permissionForm.routePath" placeholder="请输入路由地址" />
        </el-form-item>
        <el-form-item label="图标" v-if="permissionForm.permissionType === 'menu'">
          <el-input v-model="permissionForm.icon" placeholder="请输入图标" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="permissionForm.sort" :min="0" :max="99" />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="permissionForm.isActive" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="permissionForm.description" type="textarea" :rows="2" placeholder="请输入描述" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="permissionFormVisible = false">取消</el-button>
          <el-button type="primary" @click="submitPermissionForm">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive, watch } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import SysUserApi from '@/api/sysUserService';
import SysRoleApi from '@/api/sysRoleService';
import SysPermissionApi from '@/api/sysPermissionService';
import SysDeptApi from '@/api/sysDeptService';

// 选项卡状态
const activeTab = ref('user');
const permissionActiveTab = ref('function');
const loading = ref(false);

// 分页数据
const total = ref(0);
const pageSize = ref(10);
const currentPage = ref(1);

// 数据列表
const userList = ref([]);
const roleList = ref([]);
const departmentTree = ref([]);
const departmentMembers = ref([]);
const functionPermissionTree = ref([]);
const dataPermissionList = ref([]);
const approvalPermissionList = ref([]);

// 权限表单数据
const permissionFormVisible = ref(false);
const permissionFormTitle = ref('新增权限');
const permissionForm = reactive({
  id: null,
  permissionName: '',
  permissionCode: '',
  permissionType: 'menu',
  parentId: 0,
  routePath: '',
  icon: '',
  sort: 0,
  isActive: true,
  description: ''
});

// 用于存储扁平化的权限列表，方便选择上级权限
const flatPermissionList = ref([]);

// 用户管理数据
const loadUserList = async (page = currentPage.value, size = pageSize.value) => {
  loading.value = true;
  try {
    // 直接传递参数，不用包装在params中
    const res = await SysUserApi.getUserList({
      page,
      size
    });
    if (res.code === 200) {
      userList.value = res.data.records;
      total.value = res.data.total;
    } else {
      ElMessage.error(res.message || '获取用户列表失败');
    }
  } catch (error) {
    console.error('获取用户列表失败:', error);
    ElMessage.error('获取用户列表失败，请检查网络连接');
  } finally {
    loading.value = false;
  }
};

// 角色管理数据
const loadRoleList = async () => {
  loading.value = true;
  try {
    // 直接传递参数，不用包装在params中
    const res = await SysRoleApi.getRoleList({
      page: 1,
      size: 999
    });
    if (res.code === 200) {
      roleList.value = res.data.records;
    } else {
      ElMessage.error(res.message || '获取角色列表失败');
    }
  } catch (error) {
    console.error('获取角色列表失败:', error);
    ElMessage.error('获取角色列表失败，请检查网络连接');
  } finally {
    loading.value = false;
  }
};

// 部门管理数据
const loadDepartmentTree = async () => {
  loading.value = true;
  try {
    const res = await SysDeptApi.getAllDepts();
    if (res.code === 200) {
      departmentTree.value = res.data;
    } else {
      ElMessage.error(res.message || '获取部门列表失败');
    }
  } catch (error) {
    console.error('获取部门列表失败:', error);
    ElMessage.error('获取部门列表失败，请检查网络连接');
  } finally {
    loading.value = false;
  }
};

const defaultProps = {
  children: 'children',
  label: 'deptName'
};

const currentDepartment = ref(null);

const loadDepartmentMembers = async (deptId) => {
  if (!deptId) return;
  
  loading.value = true;
  try {
    const res = await SysUserApi.getUsersByDeptId(deptId);
    if (res.code === 200) {
      departmentMembers.value = res.data;
    } else {
      ElMessage.error(res.message || '获取部门成员失败');
    }
  } catch (error) {
    console.error('获取部门成员失败:', error);
    ElMessage.error('获取部门成员失败，请检查网络连接');
  } finally {
    loading.value = false;
  }
};

// 权限配置数据
const loadFunctionPermissionTree = async () => {
  loading.value = true;
  try {
    const res = await SysPermissionApi.getMenuPermissions();
    if (res.code === 200) {
      functionPermissionTree.value = res.data;
      
      // 同时创建扁平化的权限列表，用于选择上级权限
      flatPermissionList.value = [];
      const flattenPermissions = (permissions, result = []) => {
        permissions.forEach(permission => {
          result.push({
            id: permission.id,
            permissionName: permission.permissionName,
            permissionType: permission.permissionType
          });
          if (permission.children && permission.children.length > 0) {
            flattenPermissions(permission.children, result);
          }
        });
        return result;
      };
      flatPermissionList.value = flattenPermissions(res.data);
    } else {
      ElMessage.error(res.message || '获取功能权限列表失败');
    }
  } catch (error) {
    console.error('获取功能权限列表失败:', error);
    ElMessage.error('获取功能权限列表失败，请检查网络连接');
  } finally {
    loading.value = false;
  }
};

const permissionProps = {
  children: 'children',
  label: 'permissionName'
};

// 用户表单数据
const userFormVisible = ref(false);
const userFormTitle = ref('新增用户');
const userForm = reactive({
  id: null,
  username: '',
  password: '',
  realName: '',
  nickname: '',
  gender: 0,
  email: '',
  phone: '',
  avatar: '',
  deptId: null,
  roleIds: [],
  remark: ''
});

const resetUserForm = () => {
  userForm.id = null;
  userForm.username = '';
  userForm.password = '';
  userForm.realName = '';
  userForm.nickname = '';
  userForm.gender = 0;
  userForm.email = '';
  userForm.phone = '';
  userForm.avatar = '';
  userForm.deptId = null;
  userForm.roleIds = [];
  userForm.remark = '';
};

// 角色表单数据
const roleFormVisible = ref(false);
const roleFormTitle = ref('新增角色');
const roleForm = reactive({
  id: null,
  roleName: '',
  roleCode: '',
  description: '',
  sort: 0,
  isActive: true,
  isSystem: false,
  remark: ''
});

const resetRoleForm = () => {
  roleForm.id = null;
  roleForm.roleName = '';
  roleForm.roleCode = '';
  roleForm.description = '';
  roleForm.sort = 0;
  roleForm.isActive = true;
  roleForm.isSystem = false;
  roleForm.remark = '';
};

// 部门表单数据
const deptFormVisible = ref(false);
const deptFormTitle = ref('新增部门');
const deptForm = reactive({
  id: null,
  deptName: '',
  deptCode: '',
  parentId: null,
  leader: '',
  phone: '',
  email: '',
  sort: 0,
  isActive: true,
  remark: ''
});

const resetDeptForm = () => {
  deptForm.id = null;
  deptForm.deptName = '';
  deptForm.deptCode = '';
  deptForm.parentId = currentDepartment.value ? currentDepartment.value.id : null;
  deptForm.leader = '';
  deptForm.phone = '';
  deptForm.email = '';
  deptForm.sort = 0;
  deptForm.isActive = true;
  deptForm.remark = '';
};

// 分配权限数据
const assignPermissionVisible = ref(false);
const currentRole = ref(null);
const selectedPermissions = ref([]);

// 加载角色权限
const loadRolePermissions = async (roleId) => {
  if (!roleId) return;
  
  loading.value = true;
  try {
    const res = await SysRoleApi.getRolePermissionIds(roleId);
    if (res.code === 200) {
      selectedPermissions.value = res.data;
    } else {
      ElMessage.error(res.message || '获取角色权限失败');
    }
  } catch (error) {
    console.error('获取角色权限失败:', error);
    ElMessage.error('获取角色权限失败，请检查网络连接');
  } finally {
    loading.value = false;
  }
};

// 权限树复选框变化处理
const permissionTree = ref(null);
const handlePermissionCheck = (data, checked) => {
  selectedPermissions.value = permissionTree.value.getCheckedKeys();
};

// ====== 用户管理事件处理 ======
const handleAddUser = () => {
  resetUserForm();
  userFormTitle.value = '新增用户';
  userFormVisible.value = true;
};

const handleEditUser = (row) => {
  resetUserForm();
  userFormTitle.value = '编辑用户';
  
  // 实际开发中应该调用API获取用户详情
  SysUserApi.getUserById(row.id).then(res => {
    if (res.code === 200) {
      const userData = res.data;
      userForm.id = userData.id;
      userForm.username = userData.username;
      userForm.realName = userData.realName;
      userForm.nickname = userData.nickname;
      userForm.gender = typeof userData.gender === 'number' ? userData.gender : (userData.gender === '男' ? 0 : (userData.gender === '女' ? 1 : 2));
      userForm.email = userData.email;
      userForm.phone = userData.phone;
      userForm.avatar = userData.avatar;
      userForm.deptId = userData.deptId;
      userForm.roleIds = userData.roleIds || [];
      userForm.remark = userData.remark;
      userFormVisible.value = true;
    } else {
      ElMessage.error(res.message || '获取用户详情失败');
    }
  }).catch(error => {
    console.error('获取用户详情失败:', error);
    ElMessage.error('获取用户详情失败，请检查网络连接');
  });
};

const handleToggleUserStatus = (row) => {
  const newStatus = row.status === '启用' ? false : true;
  
  ElMessageBox.confirm(`确定要${newStatus ? '启用' : '禁用'}该用户吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    SysUserApi.updateUserStatus(row.id, newStatus).then(res => {
      if (res.code === 200) {
        ElMessage.success(`${newStatus ? '启用' : '禁用'}用户成功`);
        loadUserList(); // 重新加载用户列表
      } else {
        ElMessage.error(res.message || `${newStatus ? '启用' : '禁用'}用户失败`);
      }
    }).catch(error => {
      console.error(`${newStatus ? '启用' : '禁用'}用户失败:`, error);
      ElMessage.error(`${newStatus ? '启用' : '禁用'}用户失败，请检查网络连接`);
    });
  }).catch(() => {});
};

const handleResetPassword = (row) => {
  ElMessageBox.prompt('请输入新密码', '重置密码', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    inputType: 'password',
    inputPattern: /^.{6,20}$/,
    inputErrorMessage: '密码长度应为6-20位'
  }).then(({ value }) => {
    const data = {
      userId: row.id,
      newPassword: value
    };
    
    SysUserApi.resetPassword(data).then(res => {
      if (res.code === 200) {
        ElMessage.success('重置密码成功');
      } else {
        ElMessage.error(res.message || '重置密码失败');
      }
    }).catch(error => {
      console.error('重置密码失败:', error);
      ElMessage.error('重置密码失败，请检查网络连接');
    });
  }).catch(() => {});
};

const handleDeleteUser = (row) => {
  ElMessageBox.confirm('确定要删除该用户吗？删除后不可恢复', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    SysUserApi.deleteUser(row.id).then(res => {
      if (res.code === 200) {
        ElMessage.success('删除用户成功');
        loadUserList(); // 重新加载用户列表
      } else {
        ElMessage.error(res.message || '删除用户失败');
      }
    }).catch(error => {
      console.error('删除用户失败:', error);
      ElMessage.error('删除用户失败，请检查网络连接');
    });
  }).catch(() => {});
};

const submitUserForm = () => {
  // 表单验证
  if (!userForm.username || !userForm.realName) {
    ElMessage.warning('请填写必填项');
    return;
  }

  // 确保类型正确
  if (userForm.deptId) {
    userForm.deptId = Number(userForm.deptId);
  }
  
  // 确保roleIds中的元素都是数字
  if (userForm.roleIds && userForm.roleIds.length > 0) {
    userForm.roleIds = userForm.roleIds.map(id => Number(id));
  }
  
  console.log('提交的用户表单数据:', JSON.stringify(userForm));
  
  // 新增或更新用户
  const request = userForm.id ? SysUserApi.updateUser(userForm) : SysUserApi.createUser(userForm);
  
  request.then(res => {
    if (res.code === 200) {
      ElMessage.success(userForm.id ? '更新用户成功' : '新增用户成功');
      userFormVisible.value = false;
      loadUserList(); // 重新加载用户列表
    } else {
      ElMessage.error(res.message || (userForm.id ? '更新用户失败' : '新增用户失败'));
    }
  }).catch(error => {
    console.error(userForm.id ? '更新用户失败:' : '新增用户失败:', error);
    if (error.response && error.response.data) {
      console.error('错误详情:', error.response.data);
      ElMessage.error((userForm.id ? '更新用户失败' : '新增用户失败') + '：' + (error.response.data.message || '服务器错误'));
    } else {
      ElMessage.error((userForm.id ? '更新用户失败' : '新增用户失败') + '，请检查网络连接');
    }
  });
};

// ====== 角色管理事件处理 ======
const handleAddRole = () => {
  resetRoleForm();
  roleFormTitle.value = '新增角色';
  roleFormVisible.value = true;
};

const handleEditRole = (row) => {
  resetRoleForm();
  roleFormTitle.value = '编辑角色';
  
  // 实际开发中应该调用API获取角色详情
  SysRoleApi.getRoleById(row.id).then(res => {
    if (res.code === 200) {
      const roleData = res.data;
      roleForm.id = roleData.id;
      roleForm.roleName = roleData.roleName;
      roleForm.roleCode = roleData.roleCode;
      roleForm.description = roleData.description;
      roleForm.sort = roleData.sort;
      roleForm.isActive = roleData.isActive;
      roleForm.isSystem = roleData.isSystem;
      roleForm.remark = roleData.remark;
      roleFormVisible.value = true;
    } else {
      ElMessage.error(res.message || '获取角色详情失败');
    }
  }).catch(error => {
    console.error('获取角色详情失败:', error);
    ElMessage.error('获取角色详情失败，请检查网络连接');
  });
};

const handleDeleteRole = (row) => {
  ElMessageBox.confirm('确定要删除该角色吗？删除后不可恢复', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    SysRoleApi.deleteRole(row.id).then(res => {
      if (res.code === 200) {
        ElMessage.success('删除角色成功');
        loadRoleList(); // 重新加载角色列表
      } else {
        ElMessage.error(res.message || '删除角色失败');
      }
    }).catch(error => {
      console.error('删除角色失败:', error);
      ElMessage.error('删除角色失败，请检查网络连接');
    });
  }).catch(() => {});
};

const handleAssignPermission = (row) => {
  currentRole.value = row;
  loadRolePermissions(row.id);
  assignPermissionVisible.value = true;
};

const submitRoleForm = () => {
  // 表单验证
  if (!roleForm.roleName || !roleForm.roleCode) {
    ElMessage.warning('请填写必填项');
    return;
  }
  
  // 新增或更新角色
  const request = roleForm.id ? SysRoleApi.updateRole(roleForm) : SysRoleApi.createRole(roleForm);
  
  request.then(res => {
    if (res.code === 200) {
      ElMessage.success(roleForm.id ? '更新角色成功' : '新增角色成功');
      roleFormVisible.value = false;
      loadRoleList(); // 重新加载角色列表
    } else {
      ElMessage.error(res.message || (roleForm.id ? '更新角色失败' : '新增角色失败'));
    }
  }).catch(error => {
    console.error(roleForm.id ? '更新角色失败:' : '新增角色失败:', error);
    ElMessage.error((roleForm.id ? '更新角色失败' : '新增角色失败') + '，请检查网络连接');
  });
};

const submitPermissionAssign = () => {
  if (!currentRole.value || !currentRole.value.id) {
    ElMessage.warning('未选择角色');
    return;
  }
  
  SysRoleApi.assignRolePermissions(currentRole.value.id, selectedPermissions.value).then(res => {
    if (res.code === 200) {
      ElMessage.success('分配权限成功');
      assignPermissionVisible.value = false;
    } else {
      ElMessage.error(res.message || '分配权限失败');
    }
  }).catch(error => {
    console.error('分配权限失败:', error);
    ElMessage.error('分配权限失败，请检查网络连接');
  });
};

// ====== 部门管理事件处理 ======
const handleNodeClick = (data) => {
  currentDepartment.value = data;
  loadDepartmentMembers(data.id);
};

const handleAddDepartment = () => {
  resetDeptForm();
  deptFormTitle.value = '新增部门';
  deptFormVisible.value = true;
};

const handleEditDepartment = (dept) => {
  resetDeptForm();
  deptFormTitle.value = '编辑部门';
  
  // 填充表单数据
  deptForm.id = dept.id;
  deptForm.deptName = dept.deptName;
  deptForm.deptCode = dept.deptCode;
  deptForm.parentId = dept.parentId;
  deptForm.leader = dept.leader;
  deptForm.phone = dept.phone;
  deptForm.email = dept.email;
  deptForm.sort = dept.sort || 0;
  deptForm.isActive = dept.isActive !== false; // 默认为true
  deptForm.remark = dept.remark;
  
  deptFormVisible.value = true;
};

const handleDeleteDepartment = (dept) => {
  // 检查是否有子部门
  if (dept.children && dept.children.length > 0) {
    ElMessage.warning('该部门下有子部门，无法直接删除');
    return;
  }
  
  ElMessageBox.confirm('确定要删除该部门吗？删除后不可恢复', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    SysDeptApi.deleteDept(dept.id).then(res => {
      if (res.code === 200) {
        ElMessage.success('删除部门成功');
        loadDepartmentTree(); // 重新加载部门树
        
        // 如果删除的是当前选中的部门，清空当前选中
        if (currentDepartment.value && currentDepartment.value.id === dept.id) {
          currentDepartment.value = null;
          departmentMembers.value = [];
        }
      } else {
        ElMessage.error(res.message || '删除部门失败');
      }
    }).catch(error => {
      console.error('删除部门失败:', error);
      ElMessage.error('删除部门失败，请检查网络连接');
    });
  }).catch(() => {});
};

const handleEditMember = (row) => {
  // 直接调用用户编辑
  handleEditUser(row);
};

const handleRemoveMember = (row) => {
  // 实际中，这里应该调用移除用户部门关系的API
  ElMessageBox.confirm('确定要将该用户从部门中移除吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // 模拟移除操作，实际应调用API
    ElMessage.success('从部门移除用户成功');
    loadDepartmentMembers(currentDepartment.value.id); // 重新加载部门成员
  }).catch(() => {});
};

// 提交部门表单
const submitDeptForm = () => {
  // 表单验证
  if (!deptForm.deptName || !deptForm.deptCode) {
    ElMessage.warning('请填写必填项');
    return;
  }
  
  // 避免选择自己作为父部门
  if (deptForm.id && deptForm.parentId === deptForm.id) {
    ElMessage.warning('不能选择自己作为上级部门');
    return;
  }
  
  console.log('提交的部门表单数据:', JSON.stringify(deptForm));
  
  // 新增或更新部门
  const request = deptForm.id ? SysDeptApi.updateDept(deptForm) : SysDeptApi.createDept(deptForm);
  
  request.then(res => {
    if (res.code === 200) {
      ElMessage.success(deptForm.id ? '更新部门成功' : '新增部门成功');
      deptFormVisible.value = false;
      loadDepartmentTree(); // 重新加载部门树
      
      // 如果是编辑当前选中部门，重新加载部门成员
      if (deptForm.id && currentDepartment.value && currentDepartment.value.id === deptForm.id) {
        loadDepartmentMembers(deptForm.id);
      }
    } else {
      ElMessage.error(res.message || (deptForm.id ? '更新部门失败' : '新增部门失败'));
    }
  }).catch(error => {
    console.error(deptForm.id ? '更新部门失败:' : '新增部门失败:', error);
    if (error.response && error.response.data) {
      console.error('错误详情:', error.response.data);
      ElMessage.error((deptForm.id ? '更新部门失败' : '新增部门失败') + '：' + (error.response.data.message || '服务器错误'));
    } else {
      ElMessage.error((deptForm.id ? '更新部门失败' : '新增部门失败') + '，请检查网络连接');
    }
  });
};

// 选项卡切换处理，只在切换到对应选项卡时加载数据
const handleTabChange = (tabName) => {
  console.log('切换到标签页:', tabName);
  switch (tabName) {
    case 'user':
      loadUserList();
      break;
    case 'role':
      loadRoleList();
      break;
    case 'department':
      loadDepartmentTree();
      break;
    case 'permission':
      loadFunctionPermissionTree();
      break;
  }
};

// 权限选项卡变化处理
watch(permissionActiveTab, (newVal) => {
  if (newVal === 'function') {
    loadFunctionPermissionTree();
  }
  // 可以根据需要加载其他权限数据
});

// ====== 初始化 ======
onMounted(() => {
  // 初始化只加载当前选项卡数据，通过调用handleTabChange来避免代码重复
  handleTabChange(activeTab.value);
});

// 分页处理
const handleSizeChange = (size) => {
  pageSize.value = size;
  loadUserList(1, size);
};

const handleCurrentChange = (page) => {
  currentPage.value = page;
  loadUserList(page);
};

// 新增批量删除用户功能
const selectedUsers = ref([]);

// 表格选择变化处理
const handleSelectionChange = (selected) => {
  selectedUsers.value = selected.map(user => user.id);
};

// 批量删除用户实现
const handleBatchDeleteUsers = () => {
  if (selectedUsers.value.length === 0) {
    ElMessage.warning('请至少选择一个用户');
    return;
  }
  
  ElMessageBox.confirm(`确定要删除选中的 ${selectedUsers.value.length} 个用户吗？删除后不可恢复`, '批量删除', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    SysUserApi.batchDeleteUsers(selectedUsers.value).then(res => {
      if (res.code === 200) {
        ElMessage.success('批量删除用户成功');
        loadUserList(); // 重新加载用户列表
      } else {
        ElMessage.error(res.message || '批量删除用户失败');
      }
    }).catch(error => {
      console.error('批量删除用户失败:', error);
      ElMessage.error('批量删除用户失败，请检查网络连接');
    });
  }).catch(() => {});
};

// ====== 权限管理事件处理 ======
// 重置权限表单
const resetPermissionForm = () => {
  permissionForm.id = null;
  permissionForm.permissionName = '';
  permissionForm.permissionCode = '';
  permissionForm.permissionType = 'menu';
  permissionForm.parentId = 0;
  permissionForm.routePath = '';
  permissionForm.icon = '';
  permissionForm.sort = 0;
  permissionForm.isActive = true;
  permissionForm.description = '';
};

// 添加权限
const handleAddPermission = () => {
  resetPermissionForm();
  permissionFormTitle.value = '新增权限';
  permissionFormVisible.value = true;
};

// 编辑权限
const handleEditPermission = (permission) => {
  resetPermissionForm();
  permissionFormTitle.value = '编辑权限';
  
  // 填充表单数据
  permissionForm.id = permission.id;
  permissionForm.permissionName = permission.permissionName;
  permissionForm.permissionCode = permission.permissionCode;
  permissionForm.permissionType = permission.permissionType || 'menu';
  permissionForm.parentId = permission.parentId || 0;
  permissionForm.routePath = permission.routePath || '';
  permissionForm.icon = permission.icon || '';
  permissionForm.sort = permission.sort || 0;
  permissionForm.isActive = permission.isActive !== false;
  permissionForm.description = permission.description || '';
  
  permissionFormVisible.value = true;
};

// 删除权限
const handleDeletePermission = (permission) => {
  // 检查是否有子权限
  if (permission.children && permission.children.length > 0) {
    ElMessage.warning('该权限下有子权限，无法直接删除');
    return;
  }
  
  ElMessageBox.confirm('确定要删除该权限吗？删除后不可恢复', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    SysPermissionApi.deletePermission(permission.id).then(res => {
      if (res.code === 200) {
        ElMessage.success('删除权限成功');
        loadFunctionPermissionTree(); // 重新加载权限树
      } else {
        ElMessage.error(res.message || '删除权限失败');
      }
    }).catch(error => {
      console.error('删除权限失败:', error);
      ElMessage.error('删除权限失败，请检查网络连接');
    });
  }).catch(() => {});
};

// 提交权限表单
const submitPermissionForm = () => {
  // 表单验证
  if (!permissionForm.permissionName || !permissionForm.permissionCode) {
    ElMessage.warning('请填写必填项');
    return;
  }
  
  // 避免选择自己作为父权限
  if (permissionForm.id && permissionForm.parentId === permissionForm.id) {
    ElMessage.warning('不能选择自己作为上级权限');
    return;
  }
  
  console.log('提交的权限表单数据:', JSON.stringify(permissionForm));
  
  // 新增或更新权限
  const request = permissionForm.id ? 
    SysPermissionApi.updatePermission(permissionForm) : 
    SysPermissionApi.createPermission(permissionForm);
  
  request.then(res => {
    if (res.code === 200) {
      ElMessage.success(permissionForm.id ? '更新权限成功' : '新增权限成功');
      permissionFormVisible.value = false;
      loadFunctionPermissionTree(); // 重新加载权限树
    } else {
      ElMessage.error(res.message || (permissionForm.id ? '更新权限失败' : '新增权限失败'));
    }
  }).catch(error => {
    console.error(permissionForm.id ? '更新权限失败:' : '新增权限失败:', error);
    if (error.response && error.response.data) {
      console.error('错误详情:', error.response.data);
      ElMessage.error((permissionForm.id ? '更新权限失败' : '新增权限失败') + '：' + (error.response.data.message || '服务器错误'));
    } else {
      ElMessage.error((permissionForm.id ? '更新权限失败' : '新增权限失败') + '，请检查网络连接');
    }
  });
};
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

.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}

.node-actions {
  margin-left: 10px;
  display: none;
}

.custom-tree-node:hover .node-actions {
  display: inline-block;
}

.permission-toolbar {
  margin-bottom: 15px;
}
</style> 