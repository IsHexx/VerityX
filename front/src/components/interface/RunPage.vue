<template>
  <div class="api-run-page">
    <!-- 请求URL区域 -->
    <div class="request-url-section">
      <div class="url-input-group">
        <div class="method-wrapper">
          <el-form-item prop="method" class="method-form-item">
            <!-- HTTP方法选择器 -->
            <el-select
              v-model="requestConfig.method"
              size="small"
              placeholder="选择方法"
              :style="{
                width: '120px',
                '--el-input-bg-color': getMethodColor(requestConfig.method),
                '--el-input-border-color': getMethodColor(requestConfig.method),
                '--el-input-text-color': getMethodTextColor(requestConfig.method)
              }"
            >
              <el-option
                v-for="method in methodOptions"
                :key="method.value"
                :label="method.label"
                :value="method.value"
              />
            </el-select>
          </el-form-item>
        </div>

        <el-input
          v-model="requestConfig.baseUrl"
          placeholder="请选择环境"
          class="base-url-input"
        />

        <el-input
          v-model="requestConfig.path"
          placeholder="/api/path"
          class="path-input"
        />

        <el-button
          type="primary"
          @click="sendRequest"
          :loading="loading"
          class="send-button"
        >
          {{ loading ? '发送中...' : '发送' }}
        </el-button>

        <el-button class="save-button">保存</el-button>
        <el-button class="more-button">更多功能</el-button>
      </div>
    </div>

    <!-- 参数标签页区域 - 固定高度 -->
    <div class="param-tabs-section">
      <div class="param-tabs">
        <el-tabs v-model="activeTab" class="param-tabs-container">
          <el-tab-pane label="Params" name="params">
            <!-- Query 参数 -->
            <div class="param-content">
              <div class="param-header">
                <span class="param-subtitle">Query 参数</span>
                <el-button size="small" type="primary" @click="addQueryParam">批量编辑</el-button>
              </div>

                    <div class="param-table">
                      <div class="param-table-header">
                        <div class="param-col-checkbox"></div>
                        <div class="param-col-name">参数名</div>
                        <div class="param-col-value">参数值</div>
                        <div class="param-col-type">类型</div>
                        <div class="param-col-desc">说明</div>
                        <div class="param-col-actions"></div>
                      </div>

                      <div
                        v-for="(param, index) in queryParams"
                        :key="index"
                        class="param-table-row"
                      >
                        <div class="param-col-checkbox">
                          <el-checkbox v-model="param.enabled" />
                        </div>
                        <div class="param-col-name">
                          <el-input v-model="param.name" placeholder="参数名" size="small" />
                        </div>
                        <div class="param-col-value">
                          <el-input v-model="param.value" placeholder="参数值" size="small" />
                        </div>
                        <div class="param-col-type">
                          <span class="param-type">{{ param.type || 'string' }}</span>
                        </div>
                        <div class="param-col-desc">
                          <el-input v-model="param.description" placeholder="说明" size="small" />
                        </div>
                        <div class="param-col-actions">
                          <el-button
                            size="small"
                            type="danger"
                            @click="removeQueryParam(index)"
                            icon="Delete"
                          />
                        </div>
                      </div>

                      <!-- 添加参数行 -->
                      <div class="param-table-row add-row">
                        <div class="param-col-checkbox"></div>
                        <div class="param-col-name">
                          <el-input placeholder="添加参数" size="small" @focus="addQueryParam" />
                        </div>
                        <div class="param-col-value"></div>
                        <div class="param-col-type"></div>
                        <div class="param-col-desc"></div>
                        <div class="param-col-actions"></div>
                      </div>
                    </div>

                    <!-- Path参数 -->
                    <div class="param-header" style="margin-top: 24px;">
                      <span class="param-subtitle">Path 参数</span>
                    </div>

                    <div class="param-table">
                      <div class="param-table-header">
                        <div class="param-col-checkbox"></div>
                        <div class="param-col-name">参数名</div>
                        <div class="param-col-value">参数值</div>
                        <div class="param-col-type">类型</div>
                        <div class="param-col-desc">说明</div>
                      </div>

                      <div
                        v-for="(param, index) in pathParams"
                        :key="index"
                        class="param-table-row"
                      >
                        <div class="param-col-checkbox">
                          <el-checkbox v-model="param.enabled" />
                        </div>
                        <div class="param-col-name">
                          <span class="path-param-name">{{ param.name }}</span>
                        </div>
                        <div class="param-col-value">
                          <el-input v-model="param.value" placeholder="参数值" size="small" />
                        </div>
                        <div class="param-col-type">
                          <span class="param-type">{{ param.type || 'string' }}</span>
                        </div>
                        <div class="param-col-desc">
                          <el-input v-model="param.description" placeholder="说明" size="small" />
                        </div>
                      </div>
                    </div>
                  </div>
                </el-tab-pane>

                <el-tab-pane label="Body" name="body">
                  <div class="param-content">
                    <div class="placeholder-text">
                      <p>请求体配置</p>
                    </div>
                  </div>
                </el-tab-pane>

                <el-tab-pane label="Headers" name="headers">
                  <div class="param-content">
                    <div class="placeholder-text">
                      <p>请求头配置</p>
                    </div>
                  </div>
                </el-tab-pane>

                <el-tab-pane label="Cookies" name="cookies">
                  <div class="param-content">
                    <div class="placeholder-text">
                      <p>Cookie配置</p>
                    </div>
                  </div>
                </el-tab-pane>

                <el-tab-pane label="Auth" name="auth">
                  <div class="param-content">
                    <div class="placeholder-text">
                      <p>认证配置</p>
                    </div>
                  </div>
                </el-tab-pane>

                <el-tab-pane label="前置脚本" name="pre-script">
                  <div class="param-content">
                    <div class="placeholder-text">
                      <p>前置脚本配置</p>
                    </div>
                  </div>
                </el-tab-pane>

                <el-tab-pane label="后置脚本" name="post-script">
                  <div class="param-content">
                    <div class="placeholder-text">
                      <p>后置脚本配置</p>
                    </div>
                  </div>
                </el-tab-pane>

                <el-tab-pane label="设置" name="settings">
                  <div class="param-content">
                    <div class="placeholder-text">
                      <p>请求设置</p>
                    </div>
                  </div>
                </el-tab-pane>
              </el-tabs>
            </div>
          </div>

    <!-- 响应结果区域 - 使用Element Plus组件 -->
    <div class="response-section">
      <el-card class="response-card" shadow="never">
        <template #header>
          <div class="response-header">
            <span class="response-title">返回响应</span>
            <div v-if="responseData" class="response-status">
              <el-tag :type="getStatusType(responseData.status)" size="small">
                {{ responseData.status }}
              </el-tag>
              <span class="response-time">{{ responseData.time }}ms</span>
              <span class="response-size">{{ formatSize(responseData.size) }}</span>
            </div>
          </div>
        </template>

        <div class="response-content">
          <el-empty v-if="!responseData" description="点击发送按钮获取响应结果" :image-size="80" />
          <el-scrollbar v-else height="220px">
            <el-input
              v-model="formattedResponse"
              type="textarea"
              :rows="20"
              readonly
              class="response-textarea"
            />
          </el-scrollbar>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, inject, watch, computed } from 'vue'
import { ElMessage } from 'element-plus'

// 接收props
const props = defineProps({
  editPageRef: {
    type: Object,
    default: null
  }
})

// 获取编辑页面的接口数据
const editApiData = inject('editApiData', null)

// 请求配置
const requestConfig = reactive({
  method: 'GET',
  baseUrl: 'http://localhost:8089',
  path: '',
})

// 当前激活的标签页
const activeTab = ref('params')

// 加载状态
const loading = ref(false)

// 响应数据
const responseData = ref(null)

// 移除折叠和拖拽相关变量

// Query参数
const queryParams = ref([])

// Path参数
const pathParams = ref([])

// Header参数
const headerParams = ref([])

// HTTP方法选项数据
const methodOptions = ref([
  { value: 'GET', label: 'GET' },
  { value: 'POST', label: 'POST' },
  { value: 'PUT', label: 'PUT' },
  { value: 'DELETE', label: 'DELETE' },
  { value: 'PATCH', label: 'PATCH' }
])

// 获取方法背景色
const getMethodColor = (method) => {
  const colors = {
    'GET': '#f6ffed',
    'POST': '#e6f7ff',
    'PUT': '#fff7e6',
    'DELETE': '#fff1f0',
    'PATCH': '#f9f0ff'
  }
  return colors[method] || '#ffffff'
}

// 获取方法文字色
const getMethodTextColor = (method) => {
  const colors = {
    'GET': '#389e0d',
    'POST': '#1890ff',
    'PUT': '#fa8c16',
    'DELETE': '#ff4d4f',
    'PATCH': '#722ed1'
  }
  return colors[method] || '#606266'
}

// 监听编辑页面的接口数据变化
watch(() => editApiData?.value, (newApiData) => {
  if (newApiData) {
    console.log('运行页面接收到接口数据:', newApiData)
    requestConfig.method = newApiData.method || newApiData.requestMethod || 'GET'
    requestConfig.path = newApiData.path || newApiData.apiPath || ''
    loadRealApiParams(newApiData)
  }
}, { immediate: true, deep: true })

// 从真实的后端接口数据中加载参数
const loadRealApiParams = (apiData) => {
  console.log('加载真实接口参数数据:', apiData)
  try {
    // 处理Path参数
    if (apiData.pathParams) {
      const pathParamsData = typeof apiData.pathParams === 'string'
        ? JSON.parse(apiData.pathParams)
        : apiData.pathParams
      
      if (Array.isArray(pathParamsData) && pathParamsData.length > 0) {
        pathParams.value = pathParamsData.map(param => ({
          name: param.name || '',
          type: param.type || 'string',
          description: param.description || '',
          value: '',
          enabled: true,
          required: param.required || false
        }))
      } else {
        pathParams.value = []
      }
    }

    // 处理Query参数
    if (apiData.queryParams) {
      const queryParamsData = typeof apiData.queryParams === 'string'
        ? JSON.parse(apiData.queryParams)
        : apiData.queryParams
      
      if (Array.isArray(queryParamsData) && queryParamsData.length > 0) {
        queryParams.value = queryParamsData.map(param => ({
          name: param.name || '',
          type: param.type || 'string',
          description: param.description || '',
          value: '',
          enabled: true,
          required: param.required || false
        }))
      } else {
        queryParams.value = []
      }
    }

    // 处理Header参数
    if (apiData.headerParams) {
      const headerParamsData = typeof apiData.headerParams === 'string'
        ? JSON.parse(apiData.headerParams)
        : apiData.headerParams
      
      if (Array.isArray(headerParamsData) && headerParamsData.length > 0) {
        headerParams.value = headerParamsData.map(param => ({
          name: param.name || '',
          type: param.type || 'string',
          description: param.description || '',
          value: '',
          enabled: true,
          required: param.required || false
        }))
      } else {
        headerParams.value = []
      }
    }
  } catch (error) {
    console.error('解析真实接口参数数据失败:', error)
    pathParams.value = []
    queryParams.value = []
    headerParams.value = []
  }
}

// 添加Query参数
const addQueryParam = () => {
  queryParams.value.push({
    name: '',
    value: '',
    type: 'string',
    description: '',
    enabled: true
  })
}

// 删除Query参数
const removeQueryParam = (index) => {
  queryParams.value.splice(index, 1)
}

// 移除折叠切换函数

// 发送请求
const sendRequest = async () => {
  loading.value = true

  try {
    // 构建完整URL
    let fullUrl = requestConfig.baseUrl + requestConfig.path

    // 替换路径参数
    pathParams.value.forEach(param => {
      if (param.enabled && param.value) {
        fullUrl = fullUrl.replace(`{${param.name}}`, param.value)
      }
    })

    // 添加Query参数
    const enabledQueryParams = queryParams.value.filter(p => p.enabled && p.name)
    if (enabledQueryParams.length > 0) {
      const queryString = enabledQueryParams
        .map(p => `${encodeURIComponent(p.name)}=${encodeURIComponent(p.value)}`)
        .join('&')
      fullUrl += '?' + queryString
    }

    const startTime = Date.now()

    // 构建请求头
    const headers = {
      'Content-Type': 'application/json'
    }

    // 添加Header参数
    headerParams.value.forEach(param => {
      if (param.enabled && param.name && param.value) {
        headers[param.name] = param.value
      }
    })

    // 模拟请求
    await new Promise(resolve => setTimeout(resolve, 1000))

    const endTime = Date.now()
    
    // 模拟响应数据
    const mockResponse = {
      status: 200,
      message: "Success",
      data: {
        houseid: pathParams.value.find(p => p.name === 'houseid')?.value || '123',
        userid: pathParams.value.find(p => p.name === 'userid')?.value || '456',
        result: "API调用成功",
        timestamp: new Date().toISOString()
      }
    }

    responseData.value = {
      status: 200,
      time: endTime - startTime,
      size: new Blob([JSON.stringify(mockResponse)]).size,
      data: mockResponse
    }

    ElMessage.success('请求发送成功')

  } catch (error) {
    console.error('请求失败:', error)
    ElMessage.error('请求发送失败: ' + error.message)

    responseData.value = {
      status: 0,
      time: 0,
      size: 0,
      data: {
        error: error.message,
        type: 'Network Error'
      }
    }
  } finally {
    loading.value = false
  }
}

// 移除拖拽相关函数，改为固定高度布局

// 获取状态码类型（用于Element Plus Tag组件）
const getStatusType = (status) => {
  if (status >= 200 && status < 300) return 'success'
  if (status >= 400 && status < 500) return 'warning'
  if (status >= 500) return 'danger'
  return 'info'
}

// 格式化文件大小
const formatSize = (bytes) => {
  if (bytes === 0) return '0 B'
  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

// 格式化响应数据为字符串
const formattedResponse = computed(() => {
  if (!responseData.value) return ''
  return JSON.stringify(responseData.value.data, null, 2)
})
</script>

<style scoped>
.api-run-page {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f8f9fa;
  overflow: hidden; /* 防止整体页面滚动 */
}

/* 请求URL区域 */
.request-url-section {
  background: white;
  padding: 12px 20px;
  border-bottom: 1px solid #e9ecef;
  flex-shrink: 0;
}

.url-input-group {
  display: flex;
  align-items: center;
  gap: 12px;
}

/* 参数标签页区域 - 70%高度 */
.param-tabs-section {
  background: white;
  border-bottom: 1px solid #e9ecef;
  flex-shrink: 0;
  height: 70vh; /* 占用70%视口高度 */
  overflow: hidden;
}

/* 移除主内容区域，改为直接布局 */



/* HTTP方法选择器样式 */
.method-wrapper {
  display: flex;
  align-items: center;
}

.method-form-item {
  margin-bottom: 0;
}

.method-form-item :deep(.el-form-item__content) {
  margin-left: 0 !important;
}

.el-select :deep(.el-select__wrapper) {
  background-color: var(--el-input-bg-color, #ffffff) !important;
  border-color: var(--el-input-border-color, #dcdfe6) !important;
  box-shadow: 0 0 0 1px var(--el-input-border-color, #dcdfe6) inset !important;
}

.method-form-item :deep(.el-input__inner) {
  color: var(--el-input-text-color);
  font-weight: 700;
  font-size: 12px;
  text-align: center;
}

.el-select :deep(.el-select__selected-item) {
  font-size: 12px !important;
  font-weight: 700 !important;
}

.base-url-input {
  width: 200px;
}

.path-input {
  flex: 1;
}

.send-button {
  background-color: #409eff;
  border-color: #409eff;
  color: white;
  font-weight: 600;
}

.save-button, .more-button {
  color: #606266;
  border-color: #dcdfe6;
}

/* 参数配置区域 */
.params-section {
  height: 100%;
  background: white;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.param-tabs {
  height: 100%;
  display: flex;
  flex-direction: column;
  position: relative;
}

.param-tabs-container {
  position: relative;
}

.param-tabs-container :deep(.el-tabs__header) {
  margin: 0;
  border-bottom: 1px solid #e4e7ed;
  background: white;
}

.param-tabs-container :deep(.el-tabs__nav-wrap) {
  padding: 0 24px;
  background: white;
}

.param-tabs-container :deep(.el-tabs__item) {
  padding: 0 16px;
  height: 40px;
  line-height: 40px;
  font-size: 14px;
}

.param-content {
  padding: 24px;
  height: calc(70vh - 40px); /* 减去标签页头部高度 */
  overflow-y: auto; /* 独立滚动条 */
  box-sizing: border-box;
}

.param-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.param-subtitle {
  font-size: 14px;
  font-weight: 600;
  color: #303133;
}

/* 参数表格 */
.param-table {
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  overflow: hidden;
}

.param-table-header {
  display: grid;
  grid-template-columns: 40px 1fr 1fr 100px 1fr 40px;
  background-color: #f5f7fa;
  border-bottom: 1px solid #e4e7ed;
  font-weight: 600;
  color: #606266;
  font-size: 14px;
}

.param-table-row {
  display: grid;
  grid-template-columns: 40px 1fr 1fr 100px 1fr 40px;
  border-bottom: 1px solid #f0f0f0;
  align-items: center;
  background-color: #ffffff;
}

.param-table-row:last-child {
  border-bottom: none;
}

.param-col-checkbox,
.param-col-name,
.param-col-value,
.param-col-type,
.param-col-desc,
.param-col-actions {
  padding: 8px 12px;
  display: flex;
  align-items: center;
}

.param-type {
  color: #409eff;
  font-size: 12px;
  font-weight: 600;
}

.path-param-name {
  color: #409eff;
  font-weight: 500;
}

.placeholder-text {
  color: #909399;
  font-size: 14px;
  margin: 0;
}

/* 响应结果区域 - 使用Element Plus组件 */
.response-section {
  height: 320px; /* 调高到320px，提供更多响应查看空间 */
  flex-shrink: 0;
}

.response-card {
  height: 100%;
  border: none;
  border-top: 1px solid #e9ecef;
  border-radius: 0;
}

.response-card :deep(.el-card__header) {
  padding: 12px 20px;
  background-color: #fafafa;
  border-bottom: 1px solid #e4e7ed;
}

.response-card :deep(.el-card__body) {
  padding: 0;
  height: calc(100% - 60px);
}

.response-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.response-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.response-status {
  display: flex;
  align-items: center;
  gap: 12px;
}

.response-time,
.response-size {
  font-size: 12px;
  color: #909399;
}

.response-content {
  height: calc(320px - 60px); /* 320px减去卡片头部高度 */
  padding: 16px;
  box-sizing: border-box;
}

.response-textarea :deep(.el-textarea__inner) {
  font-family: 'Courier New', monospace;
  font-size: 12px;
  line-height: 1.5;
  background-color: #f8f9fa;
  border: none;
  resize: none;
}


</style>