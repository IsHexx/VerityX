<!-- PreviewPage.vue -->
<template>
  <div class="api-preview-page">
    <!-- API标题栏 -->
    <div class="api-header">
      <div class="api-title-section">
        <h1 class="api-title">{{ ruleForm.name || '接口名称' }}</h1>
        <div class="api-meta">
          <el-tag
            :type="getMethodTagType(ruleForm.method)"
            size="large"
            class="method-tag"
          >
            {{ ruleForm.method }}
          </el-tag>
          <span class="api-path">{{ ruleForm.path || '/api/path' }}</span>
          <el-tag
            :type="getStatusTagType(ruleForm.status)"
            size="large"
            class="status-tag"
          >
            {{ getStatusText(ruleForm.status) }}
          </el-tag>
        </div>
        <div class="api-info">
          <span class="info-item">创建时间: {{ formatDate(ruleForm.createdAt) }}</span>
          <span class="info-item">修改时间: {{ formatDate(ruleForm.updatedAt) }}</span>
          <span class="info-item">状态: {{ getStatusText(ruleForm.status) }}</span>
          <span class="info-item">目录: {{ ruleForm.apiDirectory || '未分类' }}</span>
          <span class="info-item">项目ID: {{ ruleForm.projectId || '未设置' }}</span>
          <span class="info-item" v-if="ruleForm.relatedTestCases">关联用例: {{ ruleForm.relatedTestCases }}</span>
        </div>
      </div>
    </div>

    <!-- 接口说明 -->
    <div class="api-section">
      <h3 class="section-title">接口说明</h3>
      <div class="description-content">
        {{ ruleForm.description || '暂无接口说明' }}
      </div>
    </div>

    <!-- 模拟数据 -->
    <div class="api-section">
      <h3 class="section-title">模拟</h3>
      <div class="mock-tabs">
        <el-button type="primary" size="small">本地</el-button>
        <el-button size="small">云端</el-button>
        <el-button size="small">自定义</el-button>
      </div>

      <div class="mock-table">
        <el-table :data="mockData" style="width: 100%">
          <el-table-column prop="code" label="名称" width="120">
            <template #default="scope">
              <el-tag :type="scope.row.code === '200' ? 'success' : 'danger'">
                {{ scope.row.name }}({{ scope.row.code }})
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="description" label="来源" width="150" />
          <el-table-column prop="url" label="URL / 参数" />
        </el-table>
      </div>
    </div>

    <!-- 请求参数 -->
    <div class="api-section">
      <h3 class="section-title">请求参数</h3>

      <!-- Path 参数 -->
      <div class="param-section">
        <h4 class="param-title">Path 参数</h4>
        <div class="param-table">
          <el-table :data="pathParams" style="width: 100%">
            <el-table-column prop="name" label="参数名" width="150">
              <template #default="scope">
                <el-link type="primary">{{ scope.row.name }}</el-link>
              </template>
            </el-table-column>
            <el-table-column prop="type" label="字符串" width="100" />
            <el-table-column prop="required" label="必需" width="80">
              <template #default="scope">
                <el-tag :type="scope.row.required ? 'danger' : 'info'" size="small">
                  {{ scope.row.required ? '必需' : '可选' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>

      <!-- Query 参数 -->
      <div class="param-section">
        <h4 class="param-title">Query 参数</h4>
        <div class="param-table">
          <el-table :data="queryParams" style="width: 100%">
            <el-table-column prop="name" label="参数名" width="150">
              <template #default="scope">
                <el-link type="primary">{{ scope.row.name }}</el-link>
              </template>
            </el-table-column>
            <el-table-column prop="type" label="类型" width="100" />
            <el-table-column prop="description" label="说明" />
            <el-table-column prop="required" label="必需" width="80">
              <template #default="scope">
                <el-tag :type="scope.row.required ? 'danger' : 'info'" size="small">
                  {{ scope.row.required ? '必需' : '可选' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>

      <!-- Header 参数 -->
      <div class="param-section">
        <h4 class="param-title">Header 参数</h4>
        <div class="param-table">
          <el-table :data="headerParams" style="width: 100%">
            <el-table-column prop="name" label="参数名" width="150">
              <template #default="scope">
                <el-link type="primary">{{ scope.row.name }}</el-link>
              </template>
            </el-table-column>
            <el-table-column prop="value" label="参数值" />
            <el-table-column prop="required" label="必需" width="80">
              <template #default="scope">
                <el-tag :type="scope.row.required ? 'danger' : 'info'" size="small">
                  {{ scope.row.required ? '必需' : '可选' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </div>

    <!-- 返回响应 -->
    <div class="api-section">
      <h3 class="section-title">返回响应</h3>
      <div class="response-tabs">
        <el-button type="success" size="small">成功(200)</el-button>
        <el-button size="small">404(404)</el-button>
      </div>

      <div class="response-info">
        <div class="response-meta">
          <span class="response-code">HTTP 状态码: 200</span>
          <span class="response-type">内容格式: JSON</span>
          <span class="response-content-type">application/json</span>
        </div>
      </div>

      <div class="response-body">
        <div class="response-header">
          <h4>数据结构</h4>
          <div class="response-actions">
            <el-button type="primary" size="small">示例</el-button>
            <el-button size="small">字符串</el-button>
            <el-button size="small">原始</el-button>
          </div>
        </div>
        <div class="json-viewer">
          <pre class="json-content">{{ formatJson(ruleForm.responseBody) }}</pre>
        </div>
      </div>
    </div>
  </div>
</template>
  
<script setup>
import { reactive, ref, inject, watch, watchEffect, computed } from "vue";
import { ApiManageApi } from "@/api/apiManageService";
import { useStore } from 'vuex'


// 使用 previewApiData 替代 selectedApiData
const previewApiData = inject('previewApiData', null);


const formSize = ref("default");
const ruleFormRef = ref(null);

const ruleForm = reactive({
  name: '',
  method: 'GET',
  path: '',
  status: 'active',
  description: '',
  createdAt: '',
  updatedAt: '',
  mockUrl: '',
  responseBody: '{\n  "message": "string"\n}',
  apiDirectory: '',
  relatedTestCases: '',
  projectId: 1
});

// 从后端获取的真实数据
const mockData = ref([]);
const pathParams = ref([]);
const queryParams = ref([]);
const headerParams = ref([]);

// 接口详细信息
const apiDetail = ref(null);




// 监听 previewApiData 变化
watch(
  () => previewApiData?.value,
  (newData) => {
    if (newData) {
      console.log('预览页数据更新:', newData);
      console.log('节点类型:', newData.type);
      console.log('节点ID:', newData.id);

      // 只有当选中的是接口类型时才获取详情
      if (newData.type === 'api' && typeof newData.id === 'number') {
        fetchApiDetails(newData.id);
      } else if (newData.type === 'api' && newData.apiData) {
        // 如果已经有接口数据，直接使用
        loadApiDataToForm(newData.apiData);
      } else {
        // 清空表单
        clearForm();
      }
    }
  },
  { immediate: true, deep: true }
);

// 获取 API 详情的方法
const fetchApiDetails = async (apiId) => {
  try {
    console.log('获取接口详情，ID:', apiId);
    const response = await ApiManageApi.getApiDetail(apiId);

    if (response && response.code === 200) {
      const apiDetail = response.data;
      loadApiDataToForm(apiDetail);
    } else {
      console.error('获取接口详情失败:', response);
    }
  } catch (error) {
    console.error('获取接口详情失败', error);
  }
};

// 将接口数据加载到表单
const loadApiDataToForm = (apiData) => {
  if (!apiData) return;

  console.log('预览页加载接口数据:', apiData);

  // 保存完整的接口详情
  apiDetail.value = apiData;

  // 基本信息
  ruleForm.name = apiData.apiName || '';
  ruleForm.method = apiData.requestMethod || 'GET';
  ruleForm.path = apiData.apiPath || '';
  ruleForm.status = apiData.status || 'active';
  ruleForm.description = apiData.description || '';
  ruleForm.createdAt = apiData.createdAt || '';
  ruleForm.updatedAt = apiData.updatedAt || '';
  ruleForm.mockUrl = apiData.mockUrl || '';
  ruleForm.responseBody = apiData.responseBody || '{\n  "message": "string"\n}';
  ruleForm.apiDirectory = apiData.apiDirectory || '';
  ruleForm.relatedTestCases = apiData.relatedTestCases || '';
  ruleForm.projectId = apiData.projectId || 1;

  // 解析参数数据（如果后端有提供的话）
  try {
    // 解析Path参数
    if (apiData.pathParams) {
      const pathParamsData = typeof apiData.pathParams === 'string'
        ? JSON.parse(apiData.pathParams)
        : apiData.pathParams;
      pathParams.value = Array.isArray(pathParamsData) ? pathParamsData : [];
    } else {
      pathParams.value = [];
    }

    // 解析Query参数
    if (apiData.queryParams) {
      const queryParamsData = typeof apiData.queryParams === 'string'
        ? JSON.parse(apiData.queryParams)
        : apiData.queryParams;
      queryParams.value = Array.isArray(queryParamsData) ? queryParamsData : [];
    } else {
      queryParams.value = [];
    }

    // 解析Header参数
    if (apiData.headerParams) {
      const headerParamsData = typeof apiData.headerParams === 'string'
        ? JSON.parse(apiData.headerParams)
        : apiData.headerParams;
      headerParams.value = Array.isArray(headerParamsData) ? headerParamsData : [];
    } else {
      headerParams.value = [];
    }

    // Mock数据处理
    if (apiData.mockData) {
      const mockDataParsed = typeof apiData.mockData === 'string'
        ? JSON.parse(apiData.mockData)
        : apiData.mockData;
      mockData.value = Array.isArray(mockDataParsed) ? mockDataParsed : [];
    } else {
      // 默认Mock数据
      mockData.value = [
        {
          name: '成功',
          code: '200',
          description: '本地Mock',
          url: ruleForm.mockUrl || 'WEB 端不支持手机 Mock，可使用云端 Mock'
        }
      ];
    }

    console.log('预览页解析后的参数数据:');
    console.log('pathParams:', pathParams.value);
    console.log('queryParams:', queryParams.value);
    console.log('headerParams:', headerParams.value);
  } catch (error) {
    console.error('预览页解析参数数据失败:', error);
    console.error('原始数据:', {
      pathParams: apiData.pathParams,
      queryParams: apiData.queryParams,
      headerParams: apiData.headerParams
    });
    // 使用默认值
    pathParams.value = [];
    queryParams.value = [];
    headerParams.value = [];
    mockData.value = [
      {
        name: '成功',
        code: '200',
        description: '本地Mock',
        url: ruleForm.mockUrl || 'WEB 端不支持手机 Mock，可使用云端 Mock'
      }
    ];
  }
};

// 清空表单
const clearForm = () => {
  // 清空接口详情
  apiDetail.value = null;

  // 清空表单数据
  ruleForm.name = '';
  ruleForm.method = 'GET';
  ruleForm.path = '';
  ruleForm.status = 'active';
  ruleForm.description = '';
  ruleForm.createdAt = '';
  ruleForm.updatedAt = '';
  ruleForm.mockUrl = '';
  ruleForm.responseBody = '{\n  "message": "string"\n}';
  ruleForm.apiDirectory = '';
  ruleForm.relatedTestCases = '';
  ruleForm.projectId = 1;

  // 重置所有参数和数据
  pathParams.value = [];
  queryParams.value = [];
  headerParams.value = [];
  mockData.value = [];
};

// 格式化日期
const formatDate = (dateStr) => {
  if (!dateStr) return '未设置';
  try {
    return new Date(dateStr).toLocaleString('zh-CN');
  } catch {
    return dateStr;
  }
};

// 格式化JSON
const formatJson = (jsonStr) => {
  if (!jsonStr) return '{}';
  try {
    return JSON.stringify(JSON.parse(jsonStr), null, 2);
  } catch {
    return jsonStr;
  }
};

// 获取请求方法标签类型
const getMethodTagType = (method) => {
  const methodTypes = {
    'GET': 'success',
    'POST': 'primary',
    'PUT': 'warning',
    'DELETE': 'danger',
    'PATCH': 'info'
  };
  return methodTypes[method] || 'info';
};

// 获取状态标签类型
const getStatusTagType = (status) => {
  const statusTypes = {
    'active': 'success',
    'inactive': 'warning',
    'deprecated': 'danger'
  };
  return statusTypes[status] || 'info';
};

// 获取状态文本
const getStatusText = (status) => {
  const statusTexts = {
    'active': '活跃',
    'inactive': '非活跃',
    'deprecated': '已废弃'
  };
  return statusTexts[status] || status;
};

const rules = reactive({});
</script>

<style scoped>
.api-preview-page {
  min-height: 100%;
  background-color: #f8f9fa;
  overflow: visible; /* 允许内容自然展开 */
  padding: 0;
  min-width: 0; /* 允许收缩 */
}

/* API标题栏 */
.api-header {
  background: white;
  padding: 20px 24px;
  border-bottom: 1px solid #e4e7ed;
  overflow-x: hidden; /* 防止横向滚动 */
  min-width: 0; /* 允许收缩 */
}

.api-title-section {
  width: 100%;
  max-width: none;
  margin: 0;
}

.api-title {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 12px 0;
}

.api-meta {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}

.method-tag {
  font-weight: 600;
  padding: 6px 12px;
  border-radius: 4px;
  min-width: 60px;
  text-align: center;
}

.api-path {
  font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
  font-size: 14px;
  color: #606266;
  background-color: #f5f7fa;
  padding: 6px 12px;
  border-radius: 4px;
  border: 1px solid #e4e7ed;
}

.status-tag {
  font-weight: 600;
  padding: 6px 12px;
  border-radius: 4px;
}

.api-info {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  font-size: 13px;
  color: #909399;
}

.info-item {
  display: flex;
  align-items: center;
}

/* 内容区域 */
.api-section {
  background: white;
  margin: 16px 24px;
  padding: 24px;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  width: calc(100% - 48px);
  max-width: none;
  overflow-x: hidden; /* 防止横向滚动 */
  min-width: 0; /* 允许收缩 */
  box-sizing: border-box; /* 确保padding包含在宽度内 */
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 16px 0;
  padding-bottom: 8px;
  border-bottom: 1px solid #e4e7ed;
}

.description-content {
  color: #606266;
  line-height: 1.6;
  padding: 12px 16px;
  background-color: #f5f7fa;
  border-radius: 4px;
  border-left: 4px solid #409eff;
}

/* 模拟数据区域 */
.mock-tabs {
  margin-bottom: 16px;
}

.mock-tabs .el-button {
  margin-right: 8px;
  border-radius: 4px;
}

.mock-table {
  border: 1px solid #e4e7ed;
  border-radius: 4px;
}

/* 参数区域 */
.param-section {
  margin-bottom: 24px;
}

.param-section:last-child {
  margin-bottom: 0;
}

.param-title {
  font-size: 14px;
  font-weight: 600;
  color: #606266;
  margin: 0 0 12px 0;
}

.param-table {
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  overflow: visible; /* 移除表格滚动 */
}

.param-table :deep(.el-table) {
  border: none;
  width: 100%;
}

.param-table :deep(.el-table__header) {
  background-color: #f5f7fa;
}

.param-table :deep(.el-table td),
.param-table :deep(.el-table th) {
  border-bottom: 1px solid #e4e7ed;
  word-wrap: break-word; /* 允许文字换行 */
  overflow: visible;
}

/* 响应区域 */
.response-tabs {
  margin-bottom: 16px;
}

.response-tabs .el-button {
  margin-right: 8px;
  border-radius: 4px;
}

.response-info {
  margin-bottom: 16px;
}

.response-meta {
  display: flex;
  gap: 24px;
  padding: 12px 16px;
  background-color: #f5f7fa;
  border-radius: 4px;
  font-size: 14px;
  color: #606266;
}

.response-code {
  font-weight: 600;
}

.response-body {
  border: 1px solid #e4e7ed;
  border-radius: 4px;
}

.response-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  background-color: #f5f7fa;
  border-bottom: 1px solid #e4e7ed;
}

.response-header h4 {
  margin: 0;
  font-size: 14px;
  font-weight: 600;
  color: #303133;
}

.response-actions {
  display: flex;
  gap: 8px;
}

.json-viewer {
  padding: 16px;
  background-color: #fafafa;
}

.json-content {
  margin: 0;
  font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
  font-size: 13px;
  line-height: 1.5;
  color: #303133;
  white-space: pre-wrap;
  word-break: break-all;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .api-header {
    padding: 16px;
  }

  .api-section {
    margin: 12px 16px;
    padding: 16px;
  }

  .api-meta {
    flex-direction: column;
    align-items: stretch;
    gap: 8px;
  }

  .api-info {
    flex-direction: column;
    gap: 8px;
  }

  .response-meta {
    flex-direction: column;
    gap: 8px;
  }

  .response-header {
    flex-direction: column;
    align-items: stretch;
    gap: 12px;
  }
}
</style>