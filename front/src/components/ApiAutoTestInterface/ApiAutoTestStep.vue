<template>
  <div class="test-step">
    <el-tab-pane label="测试步骤" name="preview">
      <!-- 使用flex布局将页面分为左右两部分 -->
      <div class="test-step-container">
        <!-- 左侧测试步骤区域 (70%) -->
        <div class="test-step-left">
          <!-- 测试步骤表格 -->
          <el-table
            :data="tableData"
            style="width: 100%"
            min-height="250"
            @selection-change="handleSelectionChange"
            row-class-name="custom-row-class"
          >
            <el-table-column type="selection" :selectable="selectable" width="55" />

            <el-table-column fixed prop="content">
              <template #header>
                <el-row :gutter="20" style="width: 100%; margin: 0;">
                  <el-col :span="12">
                    <div style="display: flex; align-items: center;">
                      <span style="white-space: nowrap; color: #666; font-size: 14px;">
                        已选 {{ selectedCount }} 项
                      </span>
                      <el-button
                        v-if="selectedCount === tableData.length"
                        type="danger"
                        size="small"
                        style="margin-left: 10px;"
                        @click="removeSelectedItems"
                      >
                        移除
                      </el-button>
                    </div>
                  </el-col>
                  <el-col :span="12">
                    <div style="display: flex; justify-content: flex-end;">
                      <el-input
                        v-model="input3"
                        size="small"
                        placeholder="请输入搜索内容"
                        :suffix-icon="Search"
                        style="min-width: 200px; max-width: 300px;"
                      />
                    </div>
                  </el-col>
                </el-row>
              </template>
              <template #default="scope">
                <div>
                  <el-row :gutter="20">
                    <el-col :span="20">
                      <!-- API导入类型 -->
                      <template v-if="scope.row.type === 'api_import'">
                        <div class="step-content">
                          <span v-if="!scope.row.isEditing">
                            <span
                              class="step-identifier"
                              :style="{
                                color:
                                  scope.row.method === 'GET'
                                    ? '#67C23A'
                                    : scope.row.method === 'POST'
                                    ? '#409EFF'
                                    : scope.row.method === 'DELETE'
                                    ? '#F56C6C'
                                    : '#E6A23C',
                              }"
                            >
                              {{ scope.row.method }}
                            </span>
                            {{ scope.row.content }}
                          </span>
                          <div v-else class="api-inputs">
                            <el-select
                              v-model="scope.row.method"
                              size="small"
                              style="width: 100px"
                            >
                              <el-option label="GET" value="GET" />
                              <el-option label="POST" value="POST" />
                              <el-option label="DELETE" value="DELETE" />
                              <el-option label="PUT" value="PUT" />
                            </el-select>
                            <el-input
                              v-model="scope.row.content"
                              size="small"
                              style="width: 200px; margin-left: 10px"
                            />
                          </div>
                        </div>
                      </template>

                      <!-- 等待时间类型 -->
                      <template v-if="scope.row.type === 'wait'">
                        <div class="step-content">
                          <span v-if="!scope.row.isEditing">
                            <span class="step-identifier" style="color: #e6a23c"
                              >Delay</span
                            >
                            等待 {{ scope.row.waitTime }} 毫秒
                          </span>
                          <div v-else>
                            <span class="step-identifier" style="color: #e6a23c"
                              >Delay</span
                            >
                            <el-input
                              v-model="scope.row.waitTime"
                              type="number"
                              size="small"
                              @blur="saveEdit(scope.row)"
                              style="width: 120px"
                            />
                          </div>
                        </div>
                      </template>

                      <!-- 条件分支类型 -->
                      <template v-if="scope.row.type === 'condition'">
                        <div class="step-content">
                          <span v-if="!scope.row.isEditing">
                            <span class="step-identifier" style="color: #409eff"
                              >if</span
                            >
                            {{ scope.row.condition.left }}
                            {{ scope.row.condition.operator }}
                            {{ scope.row.condition.right }}
                            <span class="step-identifier" style="color: #f56c6c"
                              >else</span
                            >
                            {{ scope.row.condition.else }}
                          </span>
                          <div v-else class="condition-inputs">
                            <span class="step-identifier" style="color: #409eff"
                              >if</span
                            >
                            <el-input
                              v-model="scope.row.condition.left"
                              size="small"
                              placeholder="变量"
                              style="width: 100px; margin-right: 5px"
                            />
                            <el-select
                              v-model="scope.row.condition.operator"
                              size="small"
                              style="width: 120px; margin-right: 5px"
                            >
                              <el-option label="等于" value="==" />
                              <el-option label="小于" value="<" />
                              <el-option label="小于等于" value="<=" />
                              <el-option label="大于" value=">" />
                              <el-option label="大于等于" value=">=" />
                            </el-select>
                            <el-input
                              v-model="scope.row.condition.right"
                              size="small"
                              placeholder="值"
                              style="width: 100px; margin-right: 5px"
                            />
                            <span class="step-identifier" style="color: #f56c6c"
                              >else</span
                            >
                            <el-input
                              v-model="scope.row.condition.else"
                              size="small"
                              placeholder="else条件"
                              style="width: 120px"
                            />
                          </div>
                        </div>
                      </template>
                      <!-- For循环类型 -->
                      <template v-if="scope.row.type === 'for_loop'">
                        <div class="step-content">
                          <span v-if="!scope.row.isEditing">
                            <span class="step-identifier" style="color: #67c23a"
                              >Loop</span
                            >
                            循环 {{ scope.row.loop.times }} 次 终止条件:
                            {{ scope.row.loop.exitCondition }}
                          </span>
                          <div v-else class="loop-inputs">
                            <span class="step-identifier" style="color: #67c23a"
                              >Loop</span
                            >
                            <el-input
                              v-model="scope.row.loop.times"
                              type="number"
                              size="small"
                              placeholder="循环次数"
                              style="width: 100px; margin-right: 5px"
                            />
                            <el-input
                              v-model="scope.row.loop.exitCondition"
                              size="small"
                              placeholder="终止条件"
                              style="width: 200px"
                            />
                          </div>
                        </div>
                      </template>

                      <!-- 其他类型保持原样 -->
                      <template v-if="['case_import'].includes(scope.row.type)">
                        <span>{{ scope.row.content }}</span>
                      </template>
                    </el-col>

                    <el-col :span="4" style ="text-align: center;">
                      <!-- 运行步骤按钮 -->
                      <el-icon
                        @click="runStep(scope.row, scope.$index)"
                        class="icon-button"
                        style="color: #67C23A; margin-left: 5px"
                        v-tooltip="{
                          content: '运行此步骤',
                          placement: 'top',
                        }"
                      >
                        <VideoPlay />
                      </el-icon>
                      
                      <!-- 编辑按钮 -->
                      <el-icon
                        @click.stop="editRow(scope.row, $event)"
                        style="color: #409eff; margin-left: 5px"
                        v-if="!scope.row.isEditing"
                      >
                        <Edit />
                      </el-icon>

                      <!-- 禁用按钮 -->
                      <el-icon
                        @click="toggleDisable(scope.row)"
                        class="icon-button"
                        :class="{ 'is-disabled': scope.row.disabled }"
                        style="color: #409eff; margin-left: 5px"
                      >
                        <TurnOff />
                      </el-icon>

                      <!-- 删除按钮 -->
                      <el-icon
                        @click="deleteRow(scope.$index)"
                        class="icon-button"
                        style="color: #409eff; margin-left: 5px"
                      >
                        <Delete />
                      </el-icon>

                      <!-- 增加按钮和弹出框 -->
                      <el-popover
                        placement="right"
                        :width="200"
                        trigger="click"
                        v-model="popoverVisible"
                        @show="handlePopoverShow"
                      >
                        <template #reference>
                          <el-icon
                            style="color: #409eff; margin-left: 5px"
                            class="icon-button"
                          >
                            <Plus />
                          </el-icon>
                        </template>
                        <div class="steps-menu">
                          <div
                            v-for="(step, index) in steps"
                            :key="index"
                            class="step-item"
                            @click="handleStepSelect(step, scope.$index)"
                          >
                            <el-icon
                              :style="{
                                color: getIconColor(step.icon),
                                marginLeft: '5px',
                              }"
                              class="step-icon"
                            >
                              <component :is="getIconComponent(step.icon)" />
                            </el-icon>
                            <span>{{ step.label }}</span>
                          </div>
                        </div>
                      </el-popover>
                    </el-col>
                  </el-row>

                  <!-- 运行结果行 -->
                  <el-row v-if="scope.row.lastRunResult" style="margin-top: 8px; padding-left: 20px;">
                    <el-col :span="24">
                      <div class="step-result" :class="scope.row.lastRunResult.status">
                        <el-icon class="result-icon">
                          <component :is="getResultIcon(scope.row.lastRunResult.status)" />
                        </el-icon>
                        <span class="result-text">{{ scope.row.lastRunResult.message }}</span>
                        <span class="result-time">{{ scope.row.lastRunResult.time }}</span>
                      </div>
                    </el-col>
                  </el-row>
                </div>
              </template>
            </el-table-column>

            <!-- 表格中添加运行结果列 -->
            <el-table-column label="执行结果" width="150">
              <template #default="scope">
                <div v-if="scope.row.lastRunResult" class="result-status" :class="scope.row.lastRunResult.status">
                  <el-icon class="result-icon">
                    <component :is="getResultIcon(scope.row.lastRunResult.status)" />
                  </el-icon>
                  <span>{{ scope.row.lastRunResult.status }}</span>
                </div>
                <span v-else>-</span>
              </template>
            </el-table-column>
          </el-table>

          <!-- 底部添加按钮 -->
          <el-popover
            placement="top"
            :width="200"
            trigger="click"
            v-model="addButtonPopoverVisible"
          >
            <template #reference>
              <el-button class="mt-4" style="width: 100%"> + 添加步骤 </el-button>
            </template>

            <div class="steps-menu">
              <div
                v-for="(step, index) in steps"
                :key="index"
                class="step-item"
                @click="handleAddStepSelect(step)"
              >
                <el-icon
                  :style="{ color: getIconColor(step.icon), marginLeft: '5px' }"
                  class="step-icon"
                >
                  <component :is="getIconComponent(step.icon)" />
                </el-icon>
                <span>{{ step.label }}</span>
              </div>
            </div>
          </el-popover>
        </div>

        <!-- 右侧运行配置区域 (30%) -->
        <div class="test-step-right">
          <!-- 功能测试配置区域 -->
          <el-card class="test-config-card">
            <div class="config-title">运行环境</div>
            <el-form :model="testConfig" label-position="top" size="default">
              <el-form-item>
                <el-select 
                  v-model="testConfig.environment" 
                  placeholder="请选择运行环境"
                  style="width: 100%"
                >
                  <el-option label="liys.online:8000" value="liys.online:8000"/>
                  <el-option label="localhost:8080" value="localhost:8080"/>
                  <el-option label="测试环境" value="test"/>
                  <el-option label="生产环境" value="prod"/>
                </el-select>
              </el-form-item>

              <div class="config-title">测试数据</div>
              <el-form-item>
                <el-select 
                  v-model="testConfig.testData" 
                  placeholder="请选择测试数据"
                  style="width: 100%"
                >
                  <el-option label="不使用测试数据" value="none"/>
                  <el-option label="测试数据集1" value="dataset1"/>
                  <el-option label="测试数据集2" value="dataset2"/>
                </el-select>
              </el-form-item>

              <div class="config-title">循环次数</div>
              <el-form-item>
                <el-input-number 
                  v-model="testConfig.loopCount" 
                  :min="1" 
                  :max="100"
                  style="width: 100%"
                />
              </el-form-item>

              <div class="config-title">线程数</div>
              <el-form-item>
                <el-input-number 
                  v-model="testConfig.threadCount" 
                  :min="1" 
                  :max="10"
                  style="width: 100%"
                />
              </el-form-item>

              <div class="config-title">运行于</div>
              <el-form-item>
                <el-select 
                  v-model="testConfig.runOn" 
                  placeholder="请选择运行设备"
                  style="width: 100%"
                >
                  <el-option label="本机" value="local"/>
                  <el-option label="测试服务器1" value="server1"/>
                  <el-option label="测试服务器2" value="server2"/>
                </el-select>
              </el-form-item>

              <div class="config-title">通知</div>
              <el-form-item>
                <el-switch 
                  v-model="testConfig.notification" 
                  active-text="开启" 
                  inactive-text="关闭"
                />
              </el-form-item>

              <el-collapse style="margin-top: 20px;">
                <el-collapse-item title="高级设置">
                  <el-form-item label="共享">
                    <el-switch v-model="testConfig.shared" />
                  </el-form-item>
                </el-collapse-item>
              </el-collapse>
              
              <el-form-item style="margin-top: 20px;">
                <el-button type="primary" @click="runAllSteps" style="width: 100%">
                  <el-icon><VideoPlay /></el-icon> 运行
                </el-button>
                <el-button @click="saveTestConfig" style="width: 100%; margin-left: 0; margin-top: 10px;">
                  保存
                </el-button>
              </el-form-item>
            </el-form>
          </el-card>
        </div>
      </div>
    </el-tab-pane>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from "vue";
import {
  Edit,
  TurnOff,
  Delete,
  Plus,
  Download,
  Timer,
  Switch,
  Connection,
  List,
  Search,
  VideoPlay,
  SuccessFilled,
  CircleCloseFilled,
  WarningFilled,
  Clock,
} from "@element-plus/icons-vue";
import { ElMessage, ElMessageBox, ElLoading } from "element-plus";

const selectedCount = ref(0);
const input3 = ref("");
const popoverVisible = ref(false);
const addButtonPopoverVisible = ref(false);
const currentEditingRow = ref(null);

const steps = [
  {
    label: "从接口导入",
    icon: "Download",
    type: "api_import",
  },
  {
    label: "从接口用例导入",
    icon: "Connection",
    type: "case_import",
  },
  {
    label: "等待时间",
    icon: "Timer",
    type: "wait",
  },
  {
    label: "条件分支",
    icon: "Switch",
    type: "condition",
  },
  {
    label: "For循环",
    icon: "List",
    type: "for_loop",
  },
];

// 创建新行的默认数据
const createNewRowData = (type) => {
  const baseRow = {
    isEditing: false,
    disabled: false,
    type,
    lastRunResult: null
  };

  switch (type) {
    case "api_import":
      return {
        ...baseRow,
        method: "GET", // 默认使用GET方法
        content: "接口地址",
      };
    case "wait":
      return {
        ...baseRow,
        waitTime: 1000,
        content: "等待 1000 毫秒",
      };
    case "condition":
      return {
        ...baseRow,
        condition: {
          left: "aa",
          operator: "==",
          right: "bb",
          else: "执行其他操作",
        },
        content: "if aa == bb else 执行其他操作",
      };
    case "for_loop":
      return {
        ...baseRow,
        loop: {
          times: 1,
          exitCondition: "i > 10",
        },
        content: "循环 1 次，终止条件: i > 10",
      };
    default:
      return {
        ...baseRow,
        content: `${type === "api_import" ? "从接口导入" : "从接口用例导入"}`,
      };
  }
};
const tableData = ref([]);

const handleAddStepSelect = (step) => {
  const newRow = createNewRowData(step.type);
  tableData.value.push(newRow);
  addButtonPopoverVisible.value = false;
};

const handleStepSelect = (step, index) => {
  const newRow = createNewRowData(step.type);
  tableData.value.splice(index + 1, 0, newRow);
  popoverVisible.value = false;
};

const editRow = (row, event) => {
  event.stopPropagation();
  if (currentEditingRow.value && currentEditingRow.value !== row) {
    saveEdit(currentEditingRow.value);
  }
  row.isEditing = true;
  currentEditingRow.value = row;
};

const saveEdit = (row) => {
  if (row.isEditing) {
    // 根据类型更新content
    switch (row.type) {
      case "wait":
        row.content = `等待 ${row.waitTime} 毫秒`;
        break;
      case "condition":
        row.content = `if ${row.condition.left} ${row.condition.operator} ${row.condition.right} else ${row.condition.else}`;
        break;
      case "for_loop":
        row.content = `循环 ${row.loop.times} 次，终止条件: ${row.loop.exitCondition}`;
        break;
    }
    row.isEditing = false;
    currentEditingRow.value = null;
  }
};

const handleClickOutside = (event) => {
  if (currentEditingRow.value) {
    const isClickedOnInput =
      event.target.closest(".el-input") ||
      event.target.closest(".el-icon") ||
      event.target.closest(".el-select");
    if (!isClickedOnInput) {
      saveEdit(currentEditingRow.value);
    }
  }
};

const deleteRow = (index) => {
  tableData.value.splice(index, 1);
};

const toggleDisable = (row) => {
  row.disabled = !row.disabled;
};

const selectable = (row) => !row.disabled;

const handleSelectionChange = (selection) => {
  selectedCount.value = selection.length;
};

const removeSelectedItems = () => {
  tableData.value = tableData.value.filter((row) => !row.selected);
};

const getIconColor = (icon) => {
  const colors = {
    Download: "#cbbbf7",
    Connection: "#67C23A",
    Timer: "#E6A23C",
    Switch: "#409EFF",
    List: "#F56C6C",
  };
  return colors[icon] || "#000";
};

const getIconComponent = (iconName) => {
  const icons = {
    Download: Download,
    Connection: Connection,
    Timer: Timer,
    Switch: Switch,
    List: List,
  };
  return icons[iconName] || null;
};

const handlePopoverShow = () => {};

// 获取结果状态图标
const getResultIcon = (status) => {
  switch (status) {
    case 'success':
      return 'SuccessFilled';
    case 'error':
      return 'CircleCloseFilled';
    case 'warning':
      return 'WarningFilled';
    default:
      return 'Clock';
  }
};

// 运行单个步骤
const runStep = async (row, index) => {
  const loadingInstance = ElLoading.service({
    text: `正在运行步骤: ${row.content}`,
    background: 'rgba(0, 0, 0, 0.7)',
  });

  try {
    // 模拟异步执行
    await new Promise(resolve => setTimeout(resolve, 1000));
    
    // 根据步骤类型设置不同的执行结果
    switch (row.type) {
      case 'api_import':
        row.lastRunResult = {
          status: Math.random() > 0.3 ? 'success' : 'error',
          message: Math.random() > 0.3 ? '接口调用成功' : '接口调用失败: 404 Not Found',
          time: new Date().toLocaleTimeString(),
        };
        break;
      case 'wait':
        row.lastRunResult = {
          status: 'success',
          message: `等待 ${row.waitTime} 毫秒完成`,
          time: new Date().toLocaleTimeString(),
        };
        break;
      case 'condition':
        row.lastRunResult = {
          status: 'warning',
          message: '条件判断: 进入else分支',
          time: new Date().toLocaleTimeString(),
        };
        break;
      case 'for_loop':
        row.lastRunResult = {
          status: 'success',
          message: `循环执行完成，共循环 ${row.loop?.times || 1} 次`,
          time: new Date().toLocaleTimeString(),
        };
        break;
      default:
        row.lastRunResult = {
          status: 'pending',
          message: '未知步骤类型',
          time: new Date().toLocaleTimeString(),
        };
    }

    // 通知用户执行结果
    ElMessage({
      type: row.lastRunResult.status === 'success' ? 'success' : 
            row.lastRunResult.status === 'warning' ? 'warning' : 'error',
      message: row.lastRunResult.message,
      duration: 3000,
    });
  } catch (error) {
    console.error('执行步骤时发生错误:', error);
    row.lastRunResult = {
      status: 'error',
      message: `执行错误: ${error.message || '未知错误'}`,
      time: new Date().toLocaleTimeString(),
    };
    
    ElMessage.error(`步骤执行失败: ${error.message || '未知错误'}`);
  } finally {
    loadingInstance.close();
  }
};

// 运行所有步骤
const runAllSteps = async () => {
  if (tableData.value.length === 0) {
    ElMessage.warning('没有可执行的步骤');
    return;
  }

  const loadingInstance = ElLoading.service({
    text: '正在执行所有步骤...',
    background: 'rgba(0, 0, 0, 0.7)',
  });

  try {
    for (let i = 0; i < tableData.value.length; i++) {
      const row = tableData.value[i];
      if (row.disabled) continue;
      
      // 更新加载提示
      loadingInstance.setText(`正在执行步骤 ${i + 1}/${tableData.value.length}: ${row.content}`);
      
      // 执行步骤
      await runStep(row, i);
      
      // 如果步骤执行失败且不是警告，则停止执行后续步骤
      if (row.lastRunResult.status === 'error') {
        ElMessage.error(`步骤 ${i + 1} 执行失败，停止执行后续步骤`);
        break;
      }
      
      // 添加步骤之间的延迟以便用户可以看到执行过程
      await new Promise(resolve => setTimeout(resolve, 500));
    }
    
    ElMessage.success('所有步骤执行完成');
  } catch (error) {
    console.error('执行所有步骤时发生错误:', error);
    ElMessage.error(`执行所有步骤失败: ${error.message || '未知错误'}`);
  } finally {
    loadingInstance.close();
  }
};

// 测试配置
const testConfig = ref({
  environment: 'liys.online:8000',
  testData: 'none',
  loopCount: 1,
  threadCount: 1,
  runOn: 'local',
  notification: false,
  shared: false
});

// 保存测试配置
const saveTestConfig = () => {
  ElMessage.success('测试配置已保存');
  // 实际项目中这里应该调用API保存配置
};

onMounted(() => {
  document.addEventListener("click", handleClickOutside);
});

onBeforeUnmount(() => {
  document.removeEventListener("click", handleClickOutside);
});
</script>

<style scoped>
.test-step-container {
  display: flex;
  gap: 20px;
}

.test-step-left {
  flex: 0 0 70%;
  max-width: 70%;
}

.test-step-right {
  flex: 0 0 30%;
  max-width: 30%;
}

.config-title {
  font-size: 14px;
  font-weight: 500;
  color: #606266;
  margin-bottom: 10px;
  margin-top: 15px;
}

.test-config-card {
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  height: calc(100% - 20px);
}

.test-config-card :deep(.el-form-item__label) {
  font-weight: 500;
  color: #606266;
}

.test-config-card :deep(.el-card__body) {
  padding: 15px 20px;
}

.test-config-card :deep(.el-collapse-item__header) {
  font-size: 14px;
  color: #606266;
}

.custom-row-class {
  margin-bottom: 10px;
}

.icon-group {
  display: flex;
  gap: 10px;
  align-items: center;
}

.icon-button {
  cursor: pointer;
  font-size: 16px;
  transition: all 0.3s;
}

.icon-button:hover {
  color: #409eff;
  transform: scale(1.1);
}

.icon-button.is-disabled {
  color: #c0c4cc;
  cursor: not-allowed;
}

.steps-menu {
  padding: 4px 0;
}

.step-item {
  display: flex;
  align-items: center;
  padding: 8px 12px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.step-item:hover {
  background-color: #f5f7fa;
}

.step-icon {
  margin-right: 8px;
  font-size: 16px;
}

.condition-inputs,
.loop-inputs {
  display: flex;
  align-items: center;
  gap: 5px;
}

.el-input.is-disabled .el-input__inner {
  color: #c0c4cc;
}

.step-identifier {
  font-weight: bold;
  margin-right: 8px;
  font-size: 14px;
}

.step-content {
  display: flex;
  align-items: center;
}

.api-inputs,
.condition-inputs,
.loop-inputs {
  display: flex;
  align-items: center;
  gap: 8px;
}

/* 添加动态效果 */
@keyframes pulse {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.05);
  }
  100% {
    transform: scale(1);
  }
}

.el-button--primary {
  position: relative;
  overflow: hidden;
}

.el-button--primary:active {
  animation: pulse 0.3s;
}

.disabled-step {
  opacity: 0.5;
  background-color: #f5f7fa;
}

.step-result {
  padding: 8px 12px;
  border-radius: 4px;
  margin-bottom: 8px;
  display: flex;
  align-items: center;
  font-size: 13px;
}

.step-result.success {
  background-color: rgba(103, 194, 58, 0.1);
  border-left: 3px solid #67C23A;
}

.step-result.error {
  background-color: rgba(245, 108, 108, 0.1);
  border-left: 3px solid #F56C6C;
}

.step-result.warning {
  background-color: rgba(230, 162, 60, 0.1);
  border-left: 3px solid #E6A23C;
}

.step-result.pending {
  background-color: rgba(144, 147, 153, 0.1);
  border-left: 3px solid #909399;
}

.result-icon {
  margin-right: 8px;
}

.result-icon svg {
  font-size: 16px;
}

.step-result.success .result-icon {
  color: #67C23A;
}

.step-result.error .result-icon {
  color: #F56C6C;
}

.step-result.warning .result-icon {
  color: #E6A23C;
}

.step-result.pending .result-icon {
  color: #909399;
}

.result-text {
  flex: 1;
}

.result-time {
  color: #909399;
  font-size: 12px;
}

@keyframes pulse {
  0% {
    box-shadow: 0 0 0 0 rgba(103, 194, 58, 0.4);
  }
  70% {
    box-shadow: 0 0 0 10px rgba(103, 194, 58, 0);
  }
  100% {
    box-shadow: 0 0 0 0 rgba(103, 194, 58, 0);
  }
}

.primary-button:not(:disabled) {
  animation: pulse 2s infinite;
}

.result-status {
  display: flex;
  align-items: center;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.result-status.success {
  color: #67C23A;
  background-color: rgba(103, 194, 58, 0.1);
}

.result-status.error {
  color: #F56C6C;
  background-color: rgba(245, 108, 108, 0.1);
}

.result-status.warning {
  color: #E6A23C;
  background-color: rgba(230, 162, 60, 0.1);
}

.result-status.pending {
  color: #909399;
  background-color: rgba(144, 147, 153, 0.1);
}

.result-icon {
  margin-right: 4px;
}

.step-result {
  display: flex;
  align-items: center;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  background-color: #f8f8f8;
  margin-top: 5px;
}

.step-result.success {
  border-left: 3px solid #67C23A;
}

.step-result.error {
  border-left: 3px solid #F56C6C;
}

.step-result.warning {
  border-left: 3px solid #E6A23C;
}

.step-result.pending {
  border-left: 3px solid #909399;
}

.result-text {
  margin-left: 5px;
  flex: 1;
}

.result-time {
  font-size: 11px;
  color: #909399;
}
</style>
