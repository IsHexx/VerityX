<template>
  <div class="flex flex-wrap gap-4">
    <el-card shadow="always" class="w-full">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-tabs v-model="activeTab" type="" @tab-click="handleTabClick">
            <el-tab-pane label="全部" name="all_suites"></el-tab-pane>
            <el-tab-pane label="运行中" name="running"></el-tab-pane>
            <el-tab-pane label="已完成" name="completed"></el-tab-pane>
          </el-tabs>
        </el-col>
        <el-col :span="18" style="text-align: right; align-content: center">
          <el-input
            v-model="searchKeyword"
            style="width: 240px"
            :placeholder="placeholderText"
            :suffix-icon="Search"
            @focus="handleFocus"
            @blur="handleBlur"
            @keyup.enter="handleSearch"
          />
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button type="primary" @click="handleAddTestSuite">+ 添加套件</el-button>
        </el-col>
      </el-row>
      
      <el-table
        :data="uiTestSuiteData"
        :border="true"
        v-loading="loading"
        style="width: 100%; min-height: 560px"
        class="customer-table"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="suiteId" label="ID" min-width="100" />
        <el-table-column prop="suiteName" label="套件名称" min-width="150" />
        <el-table-column prop="caseCount" label="用例数量" min-width="100" />
        <el-table-column prop="suiteStatus" label="状态" min-width="120">
          <template #default="scope">
            <el-tag
              :type="getStatusTagType(scope.row.suiteStatus)"
              :effect="'light'"
            >
              {{ scope.row.suiteStatus }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastResult" label="最后执行结果" min-width="120">
          <template #default="scope">
            <el-tag
              :type="getResultTagType(scope.row.lastResult)"
              :effect="'light'"
            >
              {{ scope.row.lastResult }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdBy" label="创建人" min-width="120" />
        <el-table-column prop="createdAt" label="创建日期" min-width="150" />
        <el-table-column prop="lastRunAt" label="最后执行时间" min-width="150" />
        <el-table-column prop="triggerMode" label="触发方式" min-width="100" />
        
        <el-table-column fixed="right" label="操作" min-width="180">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleEditTestSuite(row)">
              编辑
            </el-button>
            <el-button link type="primary" size="small" @click="handleManageCases(row)">
              管理用例
            </el-button>
            <el-button link type="primary" size="small" @click="handleRunTestSuite(row)">
              执行
            </el-button>
            <el-button link type="primary" size="small" @click="handleConcurrencyConfig(row)">
              并发配置
            </el-button>
            <el-button link type="primary" size="small" @click="handleScheduleConfig(row)">
              定时任务
            </el-button>
            <el-popconfirm
              title="确认删除该测试套件吗?"
              @confirm="handleDeleteTestSuite(row)"
              confirm-button-text="确定"
              cancel-button-text="取消"
            >
              <template #reference>
                <el-button link type="primary" size="small">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <!-- 套件详情/编辑弹窗 -->
      <el-dialog
        v-model="dialogVisible"
        :title="dialogTitle"
        width="650px"
      >
        <el-form :model="testSuiteForm" label-width="auto">
          <el-form-item label="套件名称" required>
            <el-input v-model="testSuiteForm.suiteName" placeholder="请输入套件名称" />
          </el-form-item>
          <el-form-item label="所属项目">
            <el-select v-model="testSuiteForm.projectId" placeholder="请选择所属项目" style="width: 100%">
              <el-option 
                v-for="item in projectOptions" 
                :key="item.value" 
                :label="item.label" 
                :value="item.value" 
              />
            </el-select>
          </el-form-item>
          <el-form-item label="并发配置">
            <el-select v-model="testSuiteForm.concurrencyLevel" placeholder="请选择并发级别" style="width: 100%">
              <el-option label="串行执行 (1)" value="1" />
              <el-option label="低并发 (2)" value="2" />
              <el-option label="中并发 (5)" value="5" />
              <el-option label="高并发 (10)" value="10" />
            </el-select>
          </el-form-item>
          <el-form-item label="执行环境">
            <el-select v-model="testSuiteForm.environment" placeholder="请选择执行环境" style="width: 100%">
              <el-option label="Chrome" value="Chrome" />
              <el-option label="Firefox" value="Firefox" />
              <el-option label="Edge" value="Edge" />
              <el-option label="Safari" value="Safari" />
            </el-select>
          </el-form-item>
          <el-form-item label="超时时间(秒)">
            <el-input-number v-model="testSuiteForm.timeout" :min="10" :max="300" />
          </el-form-item>
          <el-form-item label="重试次数">
            <el-input-number v-model="testSuiteForm.retryCount" :min="0" :max="3" />
          </el-form-item>
          <el-form-item label="截图策略">
            <el-select v-model="testSuiteForm.screenshotPolicy" placeholder="请选择截图策略" style="width: 100%">
              <el-option label="仅失败时" value="failureOnly" />
              <el-option label="每个步骤" value="eachStep" />
              <el-option label="不截图" value="none" />
            </el-select>
          </el-form-item>
          <el-form-item label="套件描述">
            <el-input
              v-model="testSuiteForm.description"
              type="textarea"
              placeholder="请输入套件描述"
            />
          </el-form-item>
          <el-row type="flex" justify="end">
            <el-button type="primary" @click="onSubmit">确定</el-button>
            <el-button @click="dialogVisible = false">取消</el-button>
          </el-row>
        </el-form>
      </el-dialog>

      <!-- 用例管理弹窗 -->
      <el-dialog
        v-model="casesDialogVisible"
        title="管理测试用例"
        width="800px"
      >
        <div class="mb-4">
          <el-input
            v-model="caseSearchKeyword"
            placeholder="搜索测试用例"
            style="width: 250px"
            class="mr-2"
          />
          <el-button type="primary" @click="handleSearchCase">搜索</el-button>
        </div>
        
        <el-tabs v-model="caseTabActive">
          <el-tab-pane label="已添加用例" name="added">
            <el-table
              :data="addedCases"
              height="400"
              v-loading="caseLoading"
            >
              <el-table-column prop="caseId" label="ID" width="80" />
              <el-table-column prop="caseTitle" label="用例标题" />
              <el-table-column prop="importanceLevel" label="优先级" width="100">
                <template #default="scope">
                  <el-tag :type="getPriorityTagType(scope.row.importanceLevel)">
                    {{ scope.row.importanceLevel }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="180">
                <template #default="{ row, $index }">
                  <el-button 
                    link 
                    type="primary" 
                    size="small" 
                    @click="moveUp($index)" 
                    :disabled="$index === 0"
                  >
                    上移
                  </el-button>
                  <el-button 
                    link 
                    type="primary" 
                    size="small" 
                    @click="moveDown($index)" 
                    :disabled="$index === addedCases.length - 1"
                  >
                    下移
                  </el-button>
                  <el-button 
                    link 
                    type="danger" 
                    size="small" 
                    @click="removeCase(row)"
                  >
                    移除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
          
          <el-tab-pane label="可选用例" name="available">
            <el-table
              :data="availableCases"
              height="400"
              v-loading="caseLoading"
            >
              <el-table-column prop="caseId" label="ID" width="80" />
              <el-table-column prop="caseTitle" label="用例标题" />
              <el-table-column prop="importanceLevel" label="优先级" width="100">
                <template #default="scope">
                  <el-tag :type="getPriorityTagType(scope.row.importanceLevel)">
                    {{ scope.row.importanceLevel }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="100">
                <template #default="{ row }">
                  <el-button 
                    link 
                    type="primary" 
                    size="small" 
                    @click="addCase(row)"
                  >
                    添加
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
        </el-tabs>
        
        <div class="dialog-footer mt-4" style="text-align: right;">
          <el-button @click="casesDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveCases">保存</el-button>
        </div>
      </el-dialog>

      <!-- 并发配置弹窗 -->
      <el-dialog
        v-model="concurrencyDialogVisible"
        title="并发配置"
        width="600px"
      >
        <el-form :model="concurrencyForm" label-width="140px">
          <el-form-item label="并发级别">
            <el-select v-model="concurrencyForm.concurrencyLevel" placeholder="请选择并发级别" style="width: 100%">
              <el-option label="串行执行 (1)" value="1" />
              <el-option label="低并发 (2)" value="2" />
              <el-option label="中并发 (5)" value="5" />
              <el-option label="高并发 (10)" value="10" />
              <el-option label="自定义" value="custom" />
            </el-select>
          </el-form-item>
          
          <el-form-item label="自定义并发数" v-if="concurrencyForm.concurrencyLevel === 'custom'">
            <el-input-number v-model="concurrencyForm.customConcurrency" :min="1" :max="20" />
          </el-form-item>
          
          <el-form-item label="资源分配策略">
            <el-select v-model="concurrencyForm.resourceStrategy" placeholder="请选择资源分配策略" style="width: 100%">
              <el-option label="平均分配" value="even" />
              <el-option label="优先级分配" value="priority" />
              <el-option label="动态调整" value="dynamic" />
            </el-select>
          </el-form-item>
          
          <el-form-item label="失败时行为">
            <el-select v-model="concurrencyForm.failureBehavior" placeholder="请选择失败时行为" style="width: 100%">
              <el-option label="继续执行" value="continue" />
              <el-option label="停止执行" value="stop" />
              <el-option label="仅停止当前分支" value="stop_branch" />
            </el-select>
          </el-form-item>
          
          <el-form-item label="最大等待时间(分钟)">
            <el-input-number v-model="concurrencyForm.maxWaitTime" :min="1" :max="120" />
          </el-form-item>
          
          <el-form-item label="优化设置">
            <el-checkbox v-model="concurrencyForm.enableResourceOptimization">启用资源优化</el-checkbox>
          </el-form-item>
          
          <el-form-item label="并发执行说明" v-if="concurrencyForm.concurrencyLevel !== '1'">
            <el-alert
              title="并发执行会同时运行多个测试用例，可能导致资源竞争"
              type="warning"
              :closable="false"
              show-icon
            />
          </el-form-item>
        </el-form>
        <div style="text-align: right; margin-top: 20px;">
          <el-button @click="concurrencyDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveConcurrencyConfig">保存</el-button>
        </div>
      </el-dialog>

      <!-- 定时任务设置弹窗 -->
      <el-dialog
        v-model="scheduleDialogVisible"
        title="定时任务设置"
        width="650px"
      >
        <el-form :model="scheduleForm" label-width="120px">
          <el-form-item label="启用定时任务">
            <el-switch v-model="scheduleForm.enabled" />
          </el-form-item>
          
          <template v-if="scheduleForm.enabled">
            <el-form-item label="任务名称">
              <el-input v-model="scheduleForm.name" placeholder="请输入任务名称" />
            </el-form-item>
            
            <el-form-item label="执行频率">
              <el-select v-model="scheduleForm.frequency" placeholder="请选择执行频率" style="width: 100%">
                <el-option label="每天" value="daily" />
                <el-option label="每周" value="weekly" />
                <el-option label="每月" value="monthly" />
                <el-option label="自定义" value="custom" />
              </el-select>
            </el-form-item>
            
            <el-form-item label="执行时间" v-if="scheduleForm.frequency === 'daily'">
              <el-time-picker
                v-model="scheduleForm.dailyTime"
                format="HH:mm"
                placeholder="选择时间"
                style="width: 100%"
              />
            </el-form-item>
            
            <el-form-item label="星期几" v-if="scheduleForm.frequency === 'weekly'">
              <el-select v-model="scheduleForm.weekDay" placeholder="请选择星期几" style="width: 100%">
                <el-option label="星期一" value="1" />
                <el-option label="星期二" value="2" />
                <el-option label="星期三" value="3" />
                <el-option label="星期四" value="4" />
                <el-option label="星期五" value="5" />
                <el-option label="星期六" value="6" />
                <el-option label="星期日" value="0" />
              </el-select>
            </el-form-item>
            
            <el-form-item label="日期" v-if="scheduleForm.frequency === 'monthly'">
              <el-input-number v-model="scheduleForm.monthDay" :min="1" :max="31" />
            </el-form-item>
            
            <el-form-item label="Cron表达式" v-if="scheduleForm.frequency === 'custom'">
              <el-input 
                v-model="scheduleForm.cronExpression" 
                placeholder="例如: 0 0 10 * * ?"
                :title="cronExpressionTips"
              />
              <div class="text-xs text-gray-500 mt-1">
                格式: 秒 分 时 日 月 星期 [年]
              </div>
            </el-form-item>
            
            <el-form-item label="执行环境" v-if="scheduleForm.enabled">
              <el-select v-model="scheduleForm.environment" placeholder="请选择执行环境" style="width: 100%">
                <el-option label="Chrome" value="Chrome" />
                <el-option label="Firefox" value="Firefox" />
                <el-option label="Edge" value="Edge" />
                <el-option label="Safari" value="Safari" />
              </el-select>
            </el-form-item>
            
            <el-form-item label="超时时间(分钟)">
              <el-input-number v-model="scheduleForm.timeout" :min="5" :max="60" />
            </el-form-item>
            
            <el-form-item label="通知设置">
              <el-checkbox-group v-model="scheduleForm.notifications">
                <el-checkbox label="email">邮件通知</el-checkbox>
                <el-checkbox label="system">系统通知</el-checkbox>
                <el-checkbox label="webhook">Webhook通知</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
            
            <el-form-item label="邮件接收人" v-if="scheduleForm.notifications.includes('email')">
              <el-input 
                v-model="scheduleForm.emailRecipients" 
                placeholder="多个邮箱使用逗号分隔"
              />
            </el-form-item>
            
            <el-form-item label="下次执行时间">
              <div>
                {{ getNextExecutionTime() }}
              </div>
            </el-form-item>
          </template>
        </el-form>
        
        <div style="text-align: right; margin-top: 20px;">
          <el-button @click="scheduleDialogVisible = false">取消</el-button>
          <el-button 
            type="danger" 
            v-if="scheduleForm.id && scheduleForm.enabled" 
            @click="deleteSchedule"
          >
            删除任务
          </el-button>
          <el-button type="primary" @click="saveScheduleConfig">保存</el-button>
        </div>
      </el-dialog>

      <PaginationPage 
        :total="total" 
        @update:pagination="handlePaginationChange"
      />
    </el-card>
  </div>
</template>

<script setup>
import PaginationPage from "@/components/PaginationPage.vue";
import { ref, reactive, onMounted, computed } from "vue";
import { Search } from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";
import { UiTestSuiteApi } from "@/api/uiTestSuiteService";
import { UiTestCaseApi } from "@/api/uiTestCaseService";

// 状态管理
const loading = ref(false);
const dialogVisible = ref(false);
const casesDialogVisible = ref(false);
const concurrencyDialogVisible = ref(false);
const scheduleDialogVisible = ref(false);
const dialogTitle = ref("添加套件");
const placeholderText = ref("输入查询关键字⏎");
const searchKeyword = ref("");
const activeTab = ref("all_suites");
const uiTestSuiteData = ref([]);
const total = ref(0);
const caseLoading = ref(false);
const caseSearchKeyword = ref("");
const caseTabActive = ref("added");
const addedCases = ref([]);
const availableCases = ref([]);
const currentSuiteId = ref(null);

// 分页
const pagination = reactive({
  page: 1,
  pageSize: 10,
});

// 项目选项
const projectOptions = ref([
  { value: '1', label: '电商系统项目' },
  { value: '2', label: '金融系统项目' },
  { value: '3', label: '社交媒体项目' }
]);

// 表单数据
const testSuiteForm = reactive({
  id: "",
  suiteName: "",
  projectId: "",
  concurrencyLevel: "1",
  environment: "Chrome",
  timeout: 60,
  retryCount: 1,
  screenshotPolicy: "failureOnly",
  description: ""
});

// 并发配置表单
const concurrencyForm = reactive({
  suiteId: "",
  concurrencyLevel: "1",
  customConcurrency: 3,
  resourceStrategy: "even",
  failureBehavior: "continue",
  maxWaitTime: 30,
  enableResourceOptimization: false
});

// 定时任务表单
const scheduleForm = reactive({
  id: "",
  suiteId: "",
  enabled: false,
  name: "",
  frequency: "daily",
  dailyTime: new Date(2023, 0, 1, 10, 0), // 默认早上10点
  weekDay: "1", // 默认周一
  monthDay: 1, // 默认每月1号
  cronExpression: "0 0 10 * * ?", // 默认每天早上10点
  environment: "Chrome",
  timeout: 30,
  notifications: ["system"],
  emailRecipients: ""
});

const cronExpressionTips = computed(() => {
  return `
    Cron表达式示例:
    0 0 10 * * ? - 每天上午10点执行
    0 0 */2 * * ? - 每隔2小时执行一次
    0 0 9-18 * * ? - 每天上午9点到下午6点，每小时执行一次
    0 0/30 9-18 * * ? - 每天上午9点到下午6点，每半小时执行一次
    0 0 9 ? * MON-FRI - 每周一到周五的上午9点执行
  `;
});

// 重置表单
const resetForm = () => {
  Object.assign(testSuiteForm, {
    id: "",
    suiteName: "",
    projectId: "",
    concurrencyLevel: "1",
    environment: "Chrome",
    timeout: 60,
    retryCount: 1,
    screenshotPolicy: "failureOnly",
    description: ""
  });
};

// 重置并发配置表单
const resetConcurrencyForm = () => {
  Object.assign(concurrencyForm, {
    suiteId: "",
    concurrencyLevel: "1",
    customConcurrency: 3,
    resourceStrategy: "even",
    failureBehavior: "continue",
    maxWaitTime: 30,
    enableResourceOptimization: false
  });
};

// 重置定时任务表单
const resetScheduleForm = () => {
  Object.assign(scheduleForm, {
    id: "",
    suiteId: "",
    enabled: false,
    name: "",
    frequency: "daily",
    dailyTime: new Date(2023, 0, 1, 10, 0),
    weekDay: "1",
    monthDay: 1,
    cronExpression: "0 0 10 * * ?",
    environment: "Chrome",
    timeout: 30,
    notifications: ["system"],
    emailRecipients: ""
  });
};

// 获取测试套件列表
const fetchTestSuiteList = async (tabName = activeTab.value, page = pagination.page, pageSize = pagination.pageSize, keyword = searchKeyword.value) => {
  loading.value = true;
  try {
    // 根据tab名称确定status值
    let status = null;
    if (tabName === 'running') {
      console.log("加载运行中套件数据");
      status = 'running';
    } else if (tabName === 'completed') {
      console.log("加载已完成套件数据");
      status = 'completed';
    } else {
      console.log("加载全部套件数据");
    }

    // 构造请求参数
    const params = { page, pageSize };
    if (keyword) params.keyword = keyword;
    if (status) params.status = status;

    console.log("请求参数:", params);
    
    const res = await UiTestSuiteApi.getUiTestSuiteList(params);
    if (res.code === 200) {
      uiTestSuiteData.value = res.data.list;
      total.value = res.data.total;
    } else {
      ElMessage.error(res.message || '获取测试套件列表失败');
    }
  } catch (error) {
    ElMessage.error("获取测试套件列表失败");
    console.error("获取测试套件列表失败:", error);
  } finally {
    loading.value = false;
  }
};

// 打开套件添加弹窗
const handleOpenDialog = () => {
  dialogTitle.value = "添加套件";
  resetForm();
  dialogVisible.value = true;
};

// 编辑套件
const handleEditTestSuite = async (row) => {
  dialogTitle.value = "编辑套件";
  try {
    const res = await UiTestSuiteApi.getUiTestSuiteDetail(row.suiteId);
    if (res.code === 200) {
      Object.assign(testSuiteForm, res.data);
      dialogVisible.value = true;
    } else {
      ElMessage.error(res.message || '获取套件详情失败');
    }
  } catch (error) {
    ElMessage.error("获取套件详情失败");
  }
};

// 删除套件
const handleDeleteTestSuite = async (row) => {
  try {
    const res = await UiTestSuiteApi.deleteUiTestSuite(row.suiteId);
    if (res.code === 200) {
      ElMessage.success("删除成功");
      await fetchTestSuiteList();
    } else {
      ElMessage.error(res.message || '删除失败');
    }
  } catch (error) {
    ElMessage.error("删除失败");
  }
};

// 执行测试套件
const handleRunTestSuite = async (row) => {
  try {
    const res = await UiTestSuiteApi.executeUiTestSuite(row.suiteId);
    if (res.code === 200) {
      ElMessage.success("执行成功");
      await fetchTestSuiteList();
    } else {
      ElMessage.error(res.message || '执行失败');
    }
  } catch (error) {
    ElMessage.error("执行失败");
  }
};

// 提交表单
const onSubmit = async () => {
  if (!testSuiteForm.suiteName) {
    ElMessage.warning('请输入套件名称');
    return;
  }

  try {
    const data = { ...testSuiteForm };
    let res;
    if (testSuiteForm.id) {
      res = await UiTestSuiteApi.updateUiTestSuite(testSuiteForm.id, data);
    } else {
      res = await UiTestSuiteApi.createUiTestSuite(data);
    }

    if (res.code === 200) {
      ElMessage.success(testSuiteForm.id ? "更新成功" : "创建成功");
      dialogVisible.value = false;
      await fetchTestSuiteList();
    } else {
      ElMessage.error(res.message || '操作失败');
    }
  } catch (error) {
    ElMessage.error("操作失败");
  }
};

// 处理搜索
const handleSearch = () => {
  pagination.page = 1;
  fetchTestSuiteList();
};

// 处理焦点事件
const handleFocus = () => {
  placeholderText.value = "";
};

const handleBlur = () => {
  placeholderText.value = "输入查询关键字⏎";
};

// 处理Tab切换
const handleTabClick = (tab) => {
  console.log("Tab切换, 新的tab值:", tab.props.name);
  pagination.page = 1;
  pagination.pageSize = 10;
  fetchTestSuiteList(tab.props.name);
};

// 处理分页变化
const handlePaginationChange = ({ page, pageSize }) => {
  pagination.page = page;
  pagination.pageSize = pageSize;
  fetchTestSuiteList();
};

// 添加套件
const handleAddTestSuite = () => {
  handleOpenDialog();
};

// 管理测试用例
const handleManageCases = async (row) => {
  currentSuiteId.value = row.suiteId;
  caseLoading.value = true;
  try {
    // 获取已添加的用例
    const addedRes = await UiTestSuiteApi.getSuiteCases(row.suiteId);
    if (addedRes.code === 200) {
      addedCases.value = addedRes.data || [];
    } else {
      ElMessage.error(addedRes.message || '获取套件用例失败');
    }
    
    // 获取可用的用例
    const availableRes = await UiTestCaseApi.getUiTestCaseList({
      page: 1,
      pageSize: 100,
      filter: 'not_in_suite',
      suiteId: row.suiteId
    });
    if (availableRes.code === 200) {
      availableCases.value = availableRes.data.list || [];
    } else {
      ElMessage.error(availableRes.message || '获取可用用例失败');
    }
    
    casesDialogVisible.value = true;
  } catch (error) {
    ElMessage.error("获取用例数据失败");
    console.error(error);
  } finally {
    caseLoading.value = false;
  }
};

// 处理用例搜索
const handleSearchCase = async () => {
  if (caseTabActive.value === 'added') {
    // 搜索已添加用例
    const keyword = caseSearchKeyword.value.toLowerCase();
    if (!keyword) {
      const res = await UiTestSuiteApi.getSuiteCases(currentSuiteId.value);
      if (res.code === 200) {
        addedCases.value = res.data || [];
      }
    } else {
      // 前端过滤
      const res = await UiTestSuiteApi.getSuiteCases(currentSuiteId.value);
      if (res.code === 200) {
        const allCases = res.data || [];
        addedCases.value = allCases.filter(c => 
          c.caseId.toString().includes(keyword) || 
          c.caseTitle.toLowerCase().includes(keyword)
        );
      }
    }
  } else {
    // 搜索可用用例
    caseLoading.value = true;
    try {
      const res = await UiTestCaseApi.getUiTestCaseList({
        page: 1,
        pageSize: 100,
        filter: 'not_in_suite',
        suiteId: currentSuiteId.value,
        keyword: caseSearchKeyword.value
      });
      if (res.code === 200) {
        availableCases.value = res.data.list || [];
      } else {
        ElMessage.error(res.message || '搜索用例失败');
      }
    } catch (error) {
      ElMessage.error("搜索用例失败");
    } finally {
      caseLoading.value = false;
    }
  }
};

// 移除测试用例
const removeCase = (row) => {
  const index = addedCases.value.findIndex(c => c.caseId === row.caseId);
  if (index !== -1) {
    const removed = addedCases.value.splice(index, 1)[0];
    availableCases.value.push(removed);
  }
};

// 添加测试用例
const addCase = (row) => {
  const index = availableCases.value.findIndex(c => c.caseId === row.caseId);
  if (index !== -1) {
    const added = availableCases.value.splice(index, 1)[0];
    addedCases.value.push(added);
  }
};

// 移动用例顺序
const moveUp = (index) => {
  if (index > 0) {
    const temp = addedCases.value[index];
    addedCases.value[index] = addedCases.value[index - 1];
    addedCases.value[index - 1] = temp;
  }
};

const moveDown = (index) => {
  if (index < addedCases.value.length - 1) {
    const temp = addedCases.value[index];
    addedCases.value[index] = addedCases.value[index + 1];
    addedCases.value[index + 1] = temp;
  }
};

// 保存用例
const saveCases = async () => {
  try {
    const caseIds = addedCases.value.map(c => c.caseId);
    const res = await UiTestSuiteApi.updateSuiteCases(currentSuiteId.value, { caseIds });
    if (res.code === 200) {
      // 保存用例执行顺序
      const casesOrder = addedCases.value.map((c, index) => ({
        caseId: c.caseId,
        orderIndex: index + 1
      }));
      
      const orderRes = await UiTestSuiteApi.updateCasesOrder(currentSuiteId.value, { casesOrder });
      if (orderRes.code === 200) {
        ElMessage.success("保存成功");
        casesDialogVisible.value = false;
        await fetchTestSuiteList();
      } else {
        ElMessage.warning("用例已保存，但执行顺序保存失败");
      }
    } else {
      ElMessage.error(res.message || '保存失败');
    }
  } catch (error) {
    ElMessage.error("保存失败");
  }
};

// 标签类型映射
const getStatusTagType = (status) => {
  const statusMap = {
    "运行中": "warning",
    "已完成": "success",
    "未执行": "info",
    "失败": "danger"
  };
  return statusMap[status] || "";
};

const getResultTagType = (result) => {
  const resultMap = {
    "通过": "success",
    "失败": "danger",
    "部分通过": "warning",
    "未执行": "info",
    "执行中": "primary"
  };
  return resultMap[result] || "";
};

const getPriorityTagType = (priority) => {
  const priorityMap = {
    "P0": "danger",
    "P1": "warning",
    "P2": "success",
    "P3": "info"
  };
  return priorityMap[priority] || "";
};

// 编辑套件并发配置
const handleConcurrencyConfig = async (row) => {
  try {
    resetConcurrencyForm();
    concurrencyForm.suiteId = row.suiteId;
    
    // 获取套件详情以获取当前的并发配置
    const res = await UiTestSuiteApi.getUiTestSuiteDetail(row.suiteId);
    if (res.code === 200) {
      // 更新基本的并发级别
      concurrencyForm.concurrencyLevel = res.data.concurrencyLevel || "1";
      
      // 获取高级并发配置
      const concurrencyRes = await UiTestSuiteApi.updateConcurrencyConfig(row.suiteId, {});
      if (concurrencyRes.code === 200 && concurrencyRes.data) {
        // 填充高级配置
        const config = concurrencyRes.data;
        if (config.customConcurrency) concurrencyForm.customConcurrency = config.customConcurrency;
        if (config.resourceStrategy) concurrencyForm.resourceStrategy = config.resourceStrategy;
        if (config.failureBehavior) concurrencyForm.failureBehavior = config.failureBehavior;
        if (config.maxWaitTime) concurrencyForm.maxWaitTime = config.maxWaitTime;
        if (config.enableResourceOptimization !== undefined) 
          concurrencyForm.enableResourceOptimization = config.enableResourceOptimization;
      }
      
      concurrencyDialogVisible.value = true;
    } else {
      ElMessage.error(res.message || '获取套件详情失败');
    }
  } catch (error) {
    ElMessage.error("获取套件并发配置失败");
    console.error(error);
  }
};

// 保存并发配置
const saveConcurrencyConfig = async () => {
  try {
    // 构建配置数据
    const configData = { ...concurrencyForm };
    
    // 如果是自定义并发数，使用customConcurrency的值
    if (configData.concurrencyLevel === 'custom') {
      configData.effectiveConcurrency = configData.customConcurrency;
    } else {
      configData.effectiveConcurrency = parseInt(configData.concurrencyLevel);
    }
    
    const res = await UiTestSuiteApi.updateConcurrencyConfig(concurrencyForm.suiteId, configData);
    if (res.code === 200) {
      ElMessage.success("并发配置保存成功");
      concurrencyDialogVisible.value = false;
      await fetchTestSuiteList();
    } else {
      ElMessage.error(res.message || '保存并发配置失败');
    }
  } catch (error) {
    ElMessage.error("保存并发配置失败");
    console.error(error);
  }
};

// 获取下次执行时间（简单模拟，实际应由后端计算）
const getNextExecutionTime = () => {
  if (!scheduleForm.enabled) return '任务未启用';
  
  const now = new Date();
  let nextDate = new Date(now);
  
  try {
    if (scheduleForm.frequency === 'daily') {
      const hours = scheduleForm.dailyTime.getHours();
      const minutes = scheduleForm.dailyTime.getMinutes();
      
      nextDate.setHours(hours, minutes, 0, 0);
      if (nextDate <= now) {
        nextDate.setDate(nextDate.getDate() + 1);
      }
    } else if (scheduleForm.frequency === 'weekly') {
      const targetDay = parseInt(scheduleForm.weekDay);
      const currentDay = nextDate.getDay(); // 0是周日，1-6是周一到周六
      
      const daysToAdd = (targetDay - currentDay + 7) % 7;
      nextDate.setDate(nextDate.getDate() + (daysToAdd === 0 ? 7 : daysToAdd));
      
      // 设置为当天的10点
      nextDate.setHours(10, 0, 0, 0);
    } else if (scheduleForm.frequency === 'monthly') {
      const targetDay = parseInt(scheduleForm.monthDay);
      
      // 设置为下个月的指定日期
      nextDate.setDate(1); // 设置为当月1号
      nextDate.setMonth(nextDate.getMonth() + 1); // 下个月
      nextDate.setDate(Math.min(targetDay, new Date(nextDate.getFullYear(), nextDate.getMonth() + 1, 0).getDate()));
      
      // 设置为当天的10点
      nextDate.setHours(10, 0, 0, 0);
    } else {
      // 自定义cron无法简单计算，返回提示信息
      return '使用自定义Cron表达式，无法简单预测下次执行时间';
    }
    
    return nextDate.toLocaleString();
  } catch (error) {
    console.error('计算下次执行时间出错:', error);
    return '计算错误，请检查配置';
  }
};

// 编辑定时任务设置
const handleScheduleConfig = async (row) => {
  try {
    resetScheduleForm();
    scheduleForm.suiteId = row.suiteId;
    
    // 获取现有定时任务配置
    const res = await UiTestSuiteApi.getScheduleConfig(row.suiteId);
    if (res.code === 200 && res.data) {
      // 填充配置
      const config = res.data;
      scheduleForm.id = config.id || "";
      scheduleForm.enabled = config.enabled || false;
      scheduleForm.name = config.name || `${row.suiteName} 定时任务`;
      
      if (config.frequency) {
        scheduleForm.frequency = config.frequency;
        
        // 根据不同频率设置相应字段
        if (config.frequency === 'daily' && config.dailyTime) {
          scheduleForm.dailyTime = new Date(config.dailyTime);
        } else if (config.frequency === 'weekly' && config.weekDay) {
          scheduleForm.weekDay = config.weekDay;
        } else if (config.frequency === 'monthly' && config.monthDay) {
          scheduleForm.monthDay = config.monthDay;
        } else if (config.frequency === 'custom' && config.cronExpression) {
          scheduleForm.cronExpression = config.cronExpression;
        }
      }
      
      if (config.environment) scheduleForm.environment = config.environment;
      if (config.timeout) scheduleForm.timeout = config.timeout;
      if (config.notifications) scheduleForm.notifications = config.notifications;
      if (config.emailRecipients) scheduleForm.emailRecipients = config.emailRecipients;
    } else {
      // 如果没有现有配置，设置默认值
      scheduleForm.name = `${row.suiteName} 定时任务`;
    }
    
    scheduleDialogVisible.value = true;
  } catch (error) {
    ElMessage.error("获取定时任务配置失败");
    console.error(error);
  }
};

// 保存定时任务配置
const saveScheduleConfig = async () => {
  try {
    // 验证表单
    if (scheduleForm.enabled) {
      if (!scheduleForm.name) {
        ElMessage.warning("请输入任务名称");
        return;
      }
      
      if (scheduleForm.frequency === 'custom' && !scheduleForm.cronExpression) {
        ElMessage.warning("请输入Cron表达式");
        return;
      }
      
      if (scheduleForm.notifications.includes('email') && !scheduleForm.emailRecipients) {
        ElMessage.warning("请输入邮件接收人");
        return;
      }
    }
    
    // 构建配置数据
    const configData = {
      suiteId: scheduleForm.suiteId,
      enabled: scheduleForm.enabled,
      name: scheduleForm.name,
      frequency: scheduleForm.frequency
    };
    
    // 根据频率类型添加不同的参数
    if (scheduleForm.frequency === 'daily') {
      configData.dailyTime = scheduleForm.dailyTime;
    } else if (scheduleForm.frequency === 'weekly') {
      configData.weekDay = scheduleForm.weekDay;
    } else if (scheduleForm.frequency === 'monthly') {
      configData.monthDay = scheduleForm.monthDay;
    } else if (scheduleForm.frequency === 'custom') {
      configData.cronExpression = scheduleForm.cronExpression;
    }
    
    configData.environment = scheduleForm.environment;
    configData.timeout = scheduleForm.timeout;
    configData.notifications = scheduleForm.notifications;
    
    if (scheduleForm.notifications.includes('email')) {
      configData.emailRecipients = scheduleForm.emailRecipients;
    }
    
    // 保存配置
    const res = await UiTestSuiteApi.saveScheduleConfig(scheduleForm.suiteId, configData);
    if (res.code === 200) {
      ElMessage.success("定时任务配置保存成功");
      scheduleDialogVisible.value = false;
      await fetchTestSuiteList();
    } else {
      ElMessage.error(res.message || '保存定时任务配置失败');
    }
  } catch (error) {
    ElMessage.error("保存定时任务配置失败");
    console.error(error);
  }
};

// 删除定时任务
const deleteSchedule = async () => {
  try {
    const res = await UiTestSuiteApi.deleteScheduleConfig(scheduleForm.suiteId);
    if (res.code === 200) {
      ElMessage.success("定时任务已删除");
      scheduleDialogVisible.value = false;
      await fetchTestSuiteList();
    } else {
      ElMessage.error(res.message || '删除定时任务失败');
    }
  } catch (error) {
    ElMessage.error("删除定时任务失败");
    console.error(error);
  }
};

// 组件挂载时获取数据
onMounted(() => {
  fetchTestSuiteList();
});
</script>

<style scoped>
.customer-table :deep(.el-table__cell) {
  border-right: none; /* 隐藏表格纵向边框*/
}

.customer-table {
  overflow: hidden; /* 隐藏溢出的内容 */
}

.customer-table :deep(.el-table__inner-wrapper) {
  overflow-x: auto; /* 仅在需要时显示横向滚动条 */
  overflow-y: hidden; /* 隐藏纵向滚动条 */
}

.customer-table :deep(.el-table__body-wrapper) {
  overflow-y: auto; /* 允许内容区域纵向滚动 */
  max-height: 560px; /* 限制最大高度 */
}
</style> 