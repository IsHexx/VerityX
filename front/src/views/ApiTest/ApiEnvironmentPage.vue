<template>
    <div class="api-environment-container">    <!-- Removed duplicate header -->        
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>API环境配置管理</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="handleAdd">新增环境配置</el-button>
      </div>
      
      <el-table
        v-loading="loading"
        :data="environmentList"
        style="width: 100%"
        border
      >
        <el-table-column
          prop="envName"
          label="环境名称"
          min-width="120"
        />
        <el-table-column
          prop="baseUrl"
          label="基础URL"
          min-width="200"
        />
        <el-table-column
          prop="description"
          label="环境描述"
          min-width="150"
        />
        <el-table-column
          prop="createTime"
          label="创建时间"
          min-width="150"
        />
        <el-table-column
          prop="isDefault"
          label="是否默认"
          min-width="100"
        >
          <template #default="scope">
            <el-tag v-if="scope.row.isDefault" type="success">是</el-tag>
            <el-tag v-else type="info">否</el-tag>
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          width="280"
        >
          <template #default="scope">
            <el-button
              size="mini"
              type="primary"
              @click="handleEdit(scope.row)"
            >编辑</el-button>
            <el-button
              size="mini"
              type="success"
              @click="handleSetDefault(scope.row)"
              :disabled="scope.row.isDefault"
            >设为默认</el-button>
            <el-button
              size="mini"
              type="danger"
              @click="handleDelete(scope.row)"
              :disabled="scope.row.isDefault"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="queryParams.page"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="queryParams.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        class="pagination"
      />
    </el-card>

    <!-- 编辑/新增环境配置对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="600px"
      @close="resetForm"
    >
      <el-form
        ref="envFormRef"
        :model="envForm"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="环境名称" prop="envName">
          <el-input v-model="envForm.envName" placeholder="请输入环境名称" />
        </el-form-item>
        <el-form-item label="基础URL" prop="baseUrl">
          <el-input v-model="envForm.baseUrl" placeholder="请输入基础URL，如 https://api.example.com" />
        </el-form-item>
        <el-form-item label="环境描述" prop="description">
          <el-input
            v-model="envForm.description"
            type="textarea"
            placeholder="请输入环境描述"
            :rows="3"
          />
        </el-form-item>
        <el-form-item label="全局请求头" prop="headers">
          <el-table
            :data="envForm.headers"
            border
            style="width: 100%"
          >
            <el-table-column label="名称" prop="name">
              <template #default="scope">
                <el-input v-model="scope.row.name" placeholder="名称" size="mini" />
              </template>
            </el-table-column>
            <el-table-column label="值" prop="value">
              <template #default="scope">
                <el-input v-model="scope.row.value" placeholder="值" size="mini" />
              </template>
            </el-table-column>
            <el-table-column width="50">
              <template #default="scope">
                <el-button
                  type="danger"
                  icon="el-icon-delete"
                  size="mini"
                  circle
                  @click="removeHeader(scope.$index)"
                />
              </template>
            </el-table-column>
          </el-table>
          <div style="margin-top: 5px;">
            <el-button type="primary" size="mini" @click="addHeader">添加请求头</el-button>
          </div>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm" :loading="submitLoading">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue';import { ElMessage } from 'element-plus';import ApiEnvironmentApi from '@/api/ApiEnvironment';import { useProjectStore } from '@/store/projectStore';

// 状态定义
const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const environmentList = ref([])
const total = ref(0)
const envFormRef = ref(null)

// 查询参数
const queryParams = reactive({
  page: 1,
  pageSize: 10,
  keyword: ''
})

// 表单数据
const envForm = reactive({
  id: undefined,
  envName: '',
  baseUrl: '',
  description: '',
  isDefault: false,
  globalHeaders: '',
  globalParams: '',
  headers: []
})

// 表单校验规则
const rules = {
  envName: [
    { required: true, message: '请输入环境名称', trigger: 'blur' },
    { min: 2, max: 50, message: '环境名称长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  baseUrl: [
    { required: true, message: '请输入基础URL', trigger: 'blur' },
    { pattern: /^https?:\/\/.*$/, message: 'URL格式不正确，必须以http://或https://开头', trigger: 'blur' }
  ],
  description: [
    { max: 200, message: '描述长度不能超过 200 个字符', trigger: 'blur' }
  ]
}

// 获取项目存储
const projectStore = useProjectStore();

// 监听项目ID变化
watch(() => projectStore.currentProjectId, (newProjectId, oldProjectId) => {
  if (newProjectId !== oldProjectId) {
    console.log("项目ID变化, 重新加载API环境配置数据");
    fetchEnvironmentList();
  }
});

// 获取环境配置列表
const fetchEnvironmentList = async () => {
  loading.value = true
  try {
    const res = await ApiEnvironmentApi.getEnvironmentList(queryParams)
    if (!res || !res.data) {
      console.warn('API返回数据为空')
      environmentList.value = []
      total.value = 0
      return
    }
    environmentList.value = res.data.data || []
    total.value = res.data.total || 0
  } catch (error) {
    console.error('获取环境配置列表失败', error)
    ElMessage.error('获取环境配置列表失败')
    environmentList.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

// 处理页码变化
const handleCurrentChange = (current) => {
  queryParams.page = current
  fetchEnvironmentList()
}

// 处理每页条数变化
const handleSizeChange = (size) => {
  queryParams.pageSize = size
  queryParams.page = 1
  fetchEnvironmentList()
}

// 新增环境
const handleAdd = () => {
  dialogTitle.value = '新增环境配置'
  Object.assign(envForm, {
    id: undefined,
    envName: '',
    baseUrl: '',
    description: '',
    isDefault: false,
    globalHeaders: '',
    globalParams: '',
    headers: []
  })
  dialogVisible.value = true
}

// 编辑环境
const handleEdit = async (row) => {
  dialogTitle.value = '编辑环境配置'
  loading.value = true
  try {
    const res = await ApiEnvironmentApi.getEnvironmentById(row.id)
    const data = res.data
    Object.assign(envForm, {
      id: data.id,
      envName: data.envName,
      baseUrl: data.baseUrl,
      description: data.description,
      isDefault: data.isDefault,
      globalHeaders: data.globalHeaders,
      globalParams: data.globalParams,
      headers: parseGlobalHeaders(data.globalHeaders) || []
    })
    dialogVisible.value = true
  } catch (error) {
    console.error('获取环境详情失败', error)
    ElMessage.error('获取环境详情失败')
  } finally {
    loading.value = false
  }
}

// 设置默认环境
const handleSetDefault = async (row) => {
  try {
    await ElMessageBox.confirm('确认将该环境设置为默认环境?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await ApiEnvironmentApi.setDefaultEnvironment(row.id)
    ElMessage.success('设置默认环境成功')
    fetchEnvironmentList()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('设置默认环境失败', error)
      ElMessage.error('设置默认环境失败')
    }
  }
}

// 删除环境
const handleDelete = async (row) => {
  if (row.isDefault) {
    ElMessage.warning('默认环境不能删除')
    return
  }
  
  try {
    await ElMessageBox.confirm('确认删除该环境配置?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await ApiEnvironmentApi.deleteEnvironment(row.id)
    ElMessage.success('删除环境配置成功')
    fetchEnvironmentList()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除环境配置失败', error)
      ElMessage.error('删除环境配置失败')
    }
  }
}

// 添加请求头
const addHeader = () => {
  envForm.headers.push({
    name: '',
    value: ''
  })
}

// 移除请求头
const removeHeader = (index) => {
  envForm.headers.splice(index, 1)
}

// 解析全局请求头字符串为数组
const parseGlobalHeaders = (headersStr) => {
  if (!headersStr) return []
  try {
    const headers = JSON.parse(headersStr)
    return Object.keys(headers).map(key => ({
      name: key,
      value: headers[key]
    }))
  } catch (error) {
    console.error('解析请求头失败', error)
    return []
  }
}

// 将请求头数组转为JSON字符串
const stringifyGlobalHeaders = (headers) => {
  const result = {}
  headers.forEach(h => {
    if (h.name && h.value) {
      result[h.name] = h.value
    }
  })
  return JSON.stringify(result)
}

// 提交表单
const submitForm = async () => {
  if (!envFormRef.value) return
  
  try {
    await envFormRef.value.validate()
    submitLoading.value = true
    
    // 过滤掉空的请求头
    const headers = envForm.headers.filter(h => h.name.trim() !== '' && h.value.trim() !== '')
    const formData = {
      ...envForm,
      globalHeaders: stringifyGlobalHeaders(headers)
    }
    
    if (formData.id) {
      await ApiEnvironmentApi.updateEnvironment(formData.id, formData)
      ElMessage.success('更新环境配置成功')
    } else {
      await ApiEnvironmentApi.createEnvironment(formData)
      ElMessage.success('创建环境配置成功')
    }
    
    dialogVisible.value = false
    fetchEnvironmentList()
  } catch (error) {
    console.error(formData.id ? '更新环境配置失败' : '创建环境配置失败', error)
    ElMessage.error(formData.id ? '更新环境配置失败' : '创建环境配置失败')
  } finally {
    submitLoading.value = false
  }
}

// 重置表单
const resetForm = () => {
  if (envFormRef.value) {
    envFormRef.value.resetFields()
  }
  Object.assign(envForm, {
    id: undefined,
    envName: '',
    baseUrl: '',
    description: '',
    isDefault: false,
    globalHeaders: '',
    globalParams: '',
    headers: []
  })
}

// 组件挂载时获取数据
onMounted(() => {
  fetchEnvironmentList()
})
</script>

<style scoped>
.api-environment-container {
  padding: 20px;
}
.pagination {
  margin-top: 20px;
  text-align: right;
}
</style> 