<template>
  <div class="test-step">
    <el-tab-pane label="测试步骤" name="preview">
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
            <el-row :gutter="20">
              <el-col :span="4">
                <div style="display: flex; align-items: center">
                  <span
                    style="
                      margin-left: 0px;
                      margin-right: 0px;
                      font-size: 14px;
                      color: #666;
                    "
                  >
                    已选 {{ selectedCount }} 项
                  </span>
                  <el-button
                    v-if="selectedCount === tableData.length"
                    style="
                      width: 10%;
                      height: 20px;
                      margin-left: 10px;
                      margin-right: 0px;
                      font-size: 12px;
                    "
                    @click="removeSelectedItems"
                  >
                    移除
                  </el-button>
                </div>
              </el-col>
              <el-col :span="16"
                ><div class="grid-content ep-bg-purple"
              /></el-col>
              <el-col :span="4">
                <div style="text-align: right">
                  <el-input
                    v-model="input3"
                    style="width: 100%"
                    size="small"
                    placeholder="Please Input"
                    :suffix-icon="Search"
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
                    @show="handlePopoverShow"
                    v-model="popoverVisible"
                  >
                    <template #reference>
                      <el-icon
                        class="icon-button"
                        style="color: #409eff; margin-left: 5px"
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
            </div>
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
} from "@element-plus/icons-vue";

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

onMounted(() => {
  document.addEventListener("click", handleClickOutside);
});

onBeforeUnmount(() => {
  document.removeEventListener("click", handleClickOutside);
});
</script>

<style scoped>
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
}

.icon-button:hover {
  color: #409eff;
}

.icon-button.is-disabled {
  color: #c0c4cc;
  cursor: not-allowed;
}

.steps-menu {
  padding: 4px 0;
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
</style>
