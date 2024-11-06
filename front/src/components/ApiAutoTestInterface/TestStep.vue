<template>
  <div class="test-step">
    <el-tab-pane label="测试步骤" name="preview">
      <!-- 表格 -->
      <el-table
        :data="tableData"
        style="width: 100%"
        min-height="250"
        @selection-change="handleSelectionChange"
        row-class-name="custom-row-class"
      >
        <!-- 选中列 -->
        <el-table-column type="selection" :selectable="selectable" width="55" />

        <!-- 数据列（只修改本行本列） -->
        <el-table-column fixed prop="date">
          <template #header>
            <el-row :gutter="20">
              <el-col :span="8"
                ><div style="display: flex; align-items: center">
                  <span style="margin-left: 10px; font-size: 14px; color: #666">
                    已选 {{ selectedCount }} 项
                  </span>
                </div></el-col
              >
              <el-col :span="8"
                ><el-button
                  v-if="selectedCount === tableData.length"
                  style="
                    width: 10%;
                    height: 20px;
                    margin-left: 10px;
                    font-size: 12px;
                  "
                  @click="removeSelectedItems"
                >
                  移除
                </el-button></el-col
              >
              <el-col :span="4"
                ><div style="text-align: right">
                  <el-input
                    v-model="input3"
                    style="width: 100%"
                    size="small"
                    placeholder="Please Input"
                    :suffix-icon="Search"
                  /></div
              ></el-col>
            </el-row>
          </template>
          <template #default="scope">
            <div>
              <el-row :gutter="20">
                <el-col :span="8"
                  ><!-- 显示日期 -->
                  <span v-if="!scope.row.isEditingDate">{{
                    scope.row.date
                  }}</span>
                  <!-- 编辑框 -->
                  <el-input
                    v-else
                    v-model="scope.row.date"
                    size="small"
                    @blur="saveEdit(scope.row, 'date')"
                    style="width: 80px"
                /></el-col>
                <el-col :span="8"
                  ><!-- 修改按钮 -->
                  <el-icon
                    v-if="!scope.row.isEditingDate"
                    @click.stop="editRow(scope.row, 'date', $event)"
                    style="margin-left: 10px"
                  >
                    <Edit /> </el-icon
                ></el-col>
                <el-col :span="4"
                  ><!-- 禁用按钮 -->
                  <el-icon
                    v-if="!scope.row.isEditingDate"
                    @click="toggleDisable(scope.row)"
                    class="icon-button"
                    :class="{ 'is-disabled': scope.row.disabled }"
                    style="color: #cbbbf7; margin-left: 5px; font-size: large;"
                  >
                    <TurnOff />
                  </el-icon>
                  <!-- 删除按钮 -->
                  <el-icon
                    v-if="!scope.row.isEditingDate"
                    @click="deleteRow(scope.row)"
                    class="icon-button"
                    style="color: #cbbbf7; margin-left: 5px;"
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
                        v-if="!scope.row.isEditingDate"
                        class="icon-button"
                        style="color: #cbbbf7; margin-left: 5px;"
                      >
                        <Plus />
                      </el-icon>
                    </template>

                    <!-- 弹出框内容 -->
                    <div class="steps-menu">
                      <div
                        v-for="(step, index) in steps"
                        :key="index"
                        class="step-item"
                        @click="handleStepSelect(step)"
                      >
                      <el-icon :style="{ color: getIconColor(step.icon), marginLeft: '5px' }" class="step-icon">
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

      <!-- 修改底部添加按钮，添加 popover -->
      <el-popover
        placement="top"
        :width="200"
        trigger="click"
        v-model="addButtonPopoverVisible"
      >
        <template #reference>
          <el-button class="mt-4" style="width: 100%">
            + 添加步骤
          </el-button>
        </template>

        <!-- 弹出框内容，与之前的弹出框内容相同 -->
        <div class="steps-menu">
          <div
            v-for="(step, index) in steps"
            :key="index"
            class="step-item"
            @click="handleAddStepSelect(step)"
          >
            <el-icon :style="{ color: getIconColor(step.icon), marginLeft: '5px' }" class="step-icon">
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
import { ref, onMounted, onBeforeUnmount, nextTick } from "vue";
import dayjs from "dayjs";
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
} from "@element-plus/icons-vue";

// 自定义指令：自动聚焦
const vFocus = {
  mounted: (el) => {
    nextTick(() => {
      el.querySelector("input").focus();
    });
  },
};

const now = new Date();
const selectedCount = ref(0); // 记录已选中行数
const input3 = ref("");
const selectable = (row) => true; // 控制行是否可选择
const currentEditingRow = ref(null);
const popoverVisible = ref(false);
// 添加新的 ref 用于控制底部按钮的 popover
const addButtonPopoverVisible = ref(false);

// 修改处理步骤选择的方法
const handleAddStepSelect = (step) => {
  // 处理步骤选择
  console.log("Selected step for add:", step.type);
  
  // 根据选择的步骤类型添加不同的新行
  const newRow = {
    date: dayjs(new Date()).format("YYYY-MM-DD"),
    name: "Tom",
    state: "California",
    city: "Los Angeles",
    address: "No. 189, Grove St, Los Angeles",
    zip: "CA 90036",
    isEditingDate: false,
    type: step.type // 添加步骤类型
  };

  // 将新行添加到表格数据中
  tableData.value.push(newRow);
  
  // 关闭弹出框
  addButtonPopoverVisible.value = false;
};

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

const getIconColor = (icon) => {
  switch (icon) {
    case 'Download':
      return '#cbbbf7';
    case 'Connection':
      return '#67C23A';
    case 'Timer':
      return '#E6A23C';
    case 'Switch':
      return '#409EFF';
    case 'List':
      return '#F56C6C';
    default:
      return '#000'; // 默认颜色
  }
};

// 返回图标组件
const getIconComponent = (iconName) => {
  switch (iconName) {
    case "Download":
      return Download;
    case "Connection":
      return Connection;
    case "Timer":
      return Timer;
    case "Switch":
      return Switch;
    case "List":
      return List;
    default:
      return null;
  }
};

const handleStepSelect = (step) => {
  // 处理步骤选择
  console.log("Selected step:", step.type);
  // 这里添加新步骤的逻辑
  popoverVisible.value = false;
};

const handlePopoverShow = () => {
  // 弹出框显示时的处理逻辑
};

// 表格数据
const tableData = ref([
  {
    date: "2016-05-01",
    name: "Tom",
    state: "California",
    city: "Los Angeles",
    address: "No. 189, Grove St, Los Angeles",
    zip: "CA 90036",
    isEditingDate: false, // 是否正在编辑日期
  },
  {
    date: "2016-05-02",
    name: "Tom",
    state: "California",
    city: "Los Angeles",
    address: "No. 189, Grove St, Los Angeles",
    zip: "CA 90036",
    isEditingDate: false,
  },
  {
    date: "2016-05-03",
    name: "Tom",
    state: "California",
    city: "Los Angeles",
    address: "No. 189, Grove St, Los Angeles",
    zip: "CA 90036",
    isEditingDate: false,
  },
]);

// 删除行方法
const deleteRow = (index) => {
  tableData.value.splice(index, 1);
};


// 监听选中项变化
const handleSelectionChange = (selection) => {
  selectedCount.value = selection.length;
};

// 修改处理点击外部的逻辑
const handleClickOutside = (event) => {
  if (currentEditingRow.value) {
    const isClickedOnInput =
      event.target.closest(".el-input") || event.target.closest(".el-icon");
    if (!isClickedOnInput) {
      saveEdit(currentEditingRow.value, "date");
    }
  }
};

// 在组件挂载时添加事件监听
onMounted(() => {
  document.addEventListener("click", handleClickOutside);
});

// 在组件卸载前移除事件监听
onBeforeUnmount(() => {
  document.removeEventListener("click", handleClickOutside);
});
// 修改列内容
const editRow = (row, column, event) => {
  event.stopPropagation(); // 阻止事件冒泡
  // 如果有其他行正在编辑，先保存它
  if (currentEditingRow.value && currentEditingRow.value !== row) {
    saveEdit(currentEditingRow.value, column);
  }
  row.isEditingDate = true;
  currentEditingRow.value = row;
};

const handleBlur = (row) => {
  // 这里不直接保存，让 handleClickOutside 来处理保存逻辑
  // 这样可以避免与点击事件的冲突
};
// 保存编辑后的数据
const saveEdit = (row, column) => {
  if (row.isEditingDate) {
    row.isEditingDate = false;
    currentEditingRow.value = null;
    // 这里可以添加数据验证和后端保存逻辑
  }
};

// 移除已选中项
const removeSelectedItems = () => {
  // 执行移除逻辑
};
</script>
  
  <style scoped>
.custom-row-class {
  margin-bottom: 10px; /* 行间距 */
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
</style>
  