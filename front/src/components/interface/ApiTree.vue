<!-- ApiTree.vue -->
<template>
 <div class="api-tree">
    <el-card class="tree-card">
      <template #header>
        <div class="card-header">
          <span>接口列表</span>
          <div class="header-actions">
            <el-button
              size="small"
              type="primary"
              @click="handleAddRootFolder"
              :icon="Plus"
            >
              新增目录
            </el-button>
          </div>
        </div>
      </template>
      <el-input
        v-model="filterText"
        style="width: 240px"
        placeholder="请输入接口名称"
      />
      <div v-loading="loading" element-loading-text="加载中...">
        <el-tree
          ref="treeRef"
          class="filter-tree"
          :data="treeData"
          :props="defaultProps"
          default-expand-all
          :filter-node-method="filterNode"
          @node-click="handleNodeClick"
          :indent="0"
          :expand-on-click-node="false"
        >
        <template #default="{ node, data }">
          <div
            class="custom-tree-node"
            @mouseenter="handleMouseEnter(data)"
            @mouseleave="handleMouseLeave(data)"
          >
            <!-- 自定义展开按钮 -->
            <div class="expand-button-container" :style="{ paddingLeft: (node.level - 1) * 24 + 'px' }">
              <button
                v-if="data.children && data.children.length > 0"
                class="custom-expand-btn"
                @click.stop="toggleExpand(node)"
              >
                <el-icon :class="{ 'expanded': node.expanded }">
                  <ArrowRight />
                </el-icon>
              </button>
              <div v-else class="expand-placeholder"></div>
            </div>

            <div class="node-content" :class="`level-${node.level}`">
              <!-- 目录级别 (Level 1) -->
              <template v-if="node.level === 1">
                <div class="level-1-content">
                  <el-icon class="folder-icon">
                    <Folder />
                  </el-icon>
                  <span class="node-label folder-label">
                    {{ node.label }}
                    <span class="item-count">({{ data.children?.length || 0 }})</span>
                  </span>
                </div>
              </template>

              <!-- 接口级别 (Level 2) -->
              <template v-else-if="node.level === 2">
                <div class="level-2-content">
                  <span class="http-method" :class="data.method?.toLowerCase()">
                    {{ data.method || "GET" }}
                  </span>
                  <span class="node-label api-label">{{ node.label }}</span>
                </div>
              </template>

              <!-- 用例级别 (Level 3) -->
              <template v-else-if="node.level === 3">
                <div class="level-3-content">
                  <el-icon class="case-icon">
                    <CircleCheck />
                  </el-icon>
                  <span class="node-label case-label">{{ node.label }}</span>
                </div>
              </template>
            </div>

            <!-- 文件夹操作按钮 (level 1) -->
            <div
              v-if="node.level === 1"
              class="node-actions"
              :class="{ 'show': data.isHovered }"
            >
              <el-button
                size="small"
                @click.stop="handleAddInterface(node, data)"
                :icon="Plus"
              />
              <el-dropdown
                trigger="hover"
                @visible-change="handleDropdownVisibleChange"
                :hide-on-click="false"
                @command="(command) => handleCommand(command, node, data)"
              >
                <el-button
                  size="small"
                  :icon="More"
                />
                <template #dropdown>
                  <el-dropdown-menu
                    @mouseleave="handleDropdownMenuLeave(node)"
                  >
                    <el-dropdown-item command="addFolder">新增目录</el-dropdown-item>
                    <el-dropdown-item command="rename">重命名目录</el-dropdown-item>
                    <el-dropdown-item command="delete">删除目录</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>

            <!-- 接口操作按钮 (level 2) -->
            <div
              v-if="node.level === 2"
              class="node-actions"
              :class="{ 'show': data.isHovered }"
            >
              <el-button
                size="small"
                @click.stop="handleAddCase(node, data)"
                :icon="Plus"
              />
              <el-dropdown
                trigger="hover"
                @visible-change="handleDropdownVisibleChange"
                :hide-on-click="false"
                @command="(command) => handleCommand(command, node, data)"
              >
                <el-button
                  size="small"
                  :icon="More"
                />
                <template #dropdown>
                  <el-dropdown-menu
                    @mouseleave="handleDropdownMenuLeave(node)"
                  >
                    <el-dropdown-item command="delete">删除接口</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>

            <!-- 用例操作按钮 (level 3) -->
            <div
              v-if="node.level === 3"
              class="node-actions"
              :class="{ 'show': data.isHovered }"
            >
              <el-dropdown
                trigger="hover"
                @visible-change="handleDropdownVisibleChange"
                :hide-on-click="false"
                @command="(command) => handleCommand(command, node, data)"
              >
                <el-button
                  size="small"
                  :icon="More"
                />
                <template #dropdown>
                  <el-dropdown-menu
                    @mouseleave="handleDropdownMenuLeave(node)"
                  >
                    <el-dropdown-item command="rename">重命名用例</el-dropdown-item>
                    <el-dropdown-item command="delete">删除用例</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
          </div>
        </template>
      </el-tree>
      </div>
    </el-card>
  </div>
</template>
<script setup>
import { ref, watch, onMounted, provide, inject } from 'vue';
import { Folder, Link, Plus, More, CircleCheck, ArrowRight } from "@element-plus/icons-vue";
import { ElMessage, ElMessageBox } from 'element-plus';
import { ApiManageApi } from "@/api/apiManageService";
import { useStore } from 'vuex';
import { useProjectStore } from '@/store/projectStore';

const treeRef = ref(null);
const mouseLeaveTimer = ref(null);
const isDropdownOpen = ref(false);
const filterText = ref("");


const previewApiData = inject('previewApiData', null);
const editApiData = inject('editApiData', null);
const syncApiData = inject('syncApiData', null);
// 根据当前激活的标签页来更新对应的数据
const activeTab = inject('activeName', ref('preview')); // 注入当前激活的标签页

// 修改节点点击处理函数
const handleNodeClick = (api) => {
  console.log('节点点击:', api);

  // 使用数据同步机制
  if (syncApiData) {
    syncApiData(api);
  } else {
    // 备用方案：直接设置数据
    if (previewApiData) {
      previewApiData.value = api;
    }
    if (editApiData) {
      editApiData.value = api;
    }
  }

  console.log('设置预览数据:', previewApiData?.value);
  console.log('设置编辑数据:', editApiData?.value);
};

// 获取项目存储
const projectStore = useProjectStore();

// 项目ID变化时重新加载数据
watch(() => projectStore.currentProjectId, (newProjectId, oldProjectId) => {
  if (newProjectId !== oldProjectId) {
    console.log("项目ID变化, 重新加载接口列表");
    fetchTreeData();
  }
});

// 树形结构数据
const treeData = ref([]);



// v2 API数据转换函数：将新的树形数据转换为前端需要的格式
const transformTreeData = (rawData) => {
  console.log('开始转换v2树形数据:', rawData);

  if (!rawData || !Array.isArray(rawData) || rawData.length === 0) {
    console.warn('原始数据为空或不是数组');
    return [];
  }

  // v2 API返回的数据已经是树形结构，只需要转换字段名
  const treeData = rawData.map(module => {
    console.log('🔄 处理模块:', module);
    console.log('🔄 模块ID:', module.id, '类型:', typeof module.id);
    console.log('🔄 模块名称:', module.name);

    const moduleNode = {
      id: module.id,
      label: module.name,
      type: 'directory', // 保持前端的类型名称
      children: [],
      apiData: {
        apiId: module.id,
        apiName: module.name,
        itemType: 'directory',
        description: module.description,
        status: module.status
      }
    };

    console.log('🔄 创建的模块节点:', moduleNode);
    console.log('🔄 模块节点apiData:', moduleNode.apiData);

    // 处理模块下的接口
    if (module.children && Array.isArray(module.children)) {
      moduleNode.children = module.children.map(endpoint => {
        console.log('处理接口:', endpoint);

        const endpointNode = {
          id: endpoint.id,
          label: endpoint.name,
          type: 'api', // 保持前端的类型名称
          method: endpoint.method,
          children: [],
          apiData: {
            apiId: endpoint.id,
            apiName: endpoint.name,
            itemType: 'api',
            requestMethod: endpoint.method,
            apiPath: endpoint.path,
            description: endpoint.description,
            status: endpoint.status
          }
        };

        // 处理接口下的测试用例
        if (endpoint.children && Array.isArray(endpoint.children)) {
          endpointNode.children = endpoint.children.map(testcase => {
            console.log('处理用例:', testcase);

            return {
              id: testcase.id,
              label: testcase.name,
              type: 'case',
              apiData: {
                apiId: testcase.id,
                apiName: testcase.name,
                itemType: 'case',
                description: testcase.description,
                status: testcase.status
              }
            };
          });
        }

        return endpointNode;
      });
    }

    return moduleNode;
  });

  console.log('转换后的树形数据:', treeData);
  console.log('第一个模块的详细结构:', JSON.stringify(treeData[0], null, 2));
  return treeData;
};

// 加载状态
const loading = ref(false);

// 请求数据并处理为树形结构
const fetchTreeData = async () => {
  loading.value = true;
  try {
    console.log("开始加载树形数据...");

    // 使用树形结构API
    const response = await ApiManageApi.getTreeStructure();
    console.log("树形结构API响应:", response);

    if (response && response.code === 200) {
      const rawData = response.data || [];
      // 使用标准的三层树形转换
      const transformedData = transformTreeData(rawData);
      console.log("转换后的树形数据:", transformedData);
      treeData.value = transformedData;
    } else {
      console.error("获取树形数据失败:", response?.message || '未知错误');
      ElMessage.error('获取树形数据失败');
      treeData.value = [];
    }
  } catch (error) {
    console.error("请求数据失败:", error);
    ElMessage.error('加载树形数据失败');
    treeData.value = [];
  } finally {
    loading.value = false;
  }
};

// 提供刷新树数据的方法给其他组件使用
provide('refreshTreeData', fetchTreeData);

const defaultProps = {
  children: "children",
  label: "label",
};


// 处理各种操作命令
const handleCommand = (command, node, rawData) => {
  event?.stopPropagation();
  switch (command) {
    case 'addFolder':
      handleAddFolder(node, rawData);
      break;
    case 'rename':
      handleRename(node, rawData);
      break;

    case 'delete':
      handleDelete(node, rawData);
      break;
  }
};

// 新增用例
const handleAddCase = (node, data) => {
  event?.stopPropagation();
  ElMessageBox.prompt('请输入用例名称', '新增用例', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
  }).then(({ value }) => {
    if (value) {
      const newCase = {
        id: Date.now(),
        label: value,
      };
      data.children.push(newCase);
      ElMessage.success('用例添加成功');
    }
  });
};



// 根级别新增目录
const handleAddRootFolder = async () => {
  try {
    const { value } = await ElMessageBox.prompt('请输入目录名称', '新增目录', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
    });

    if (value) {
      const trimmedValue = value.trim();

      // 检查目录名是否已存在
      const existingDir = treeData.value.find(item => item.label === trimmedValue);
      if (existingDir) {
        ElMessage.warning('目录名称已存在');
        return;
      }

      console.log('====== 创建新目录 ======');
      console.log('目录名称:', trimmedValue);

      // 调用新的v2 API创建模块
      const response = await ApiManageApi.createModule({
        name: trimmedValue,
        description: `模块：${trimmedValue}`
      });

      if (response && response.code === 200) {
        // 重新加载数据
        await fetchTreeData();
        ElMessage.success('目录创建成功');
      } else {
        ElMessage.error('目录创建失败：' + (response?.message || '未知错误'));
      }
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('新增目录失败:', error);
      ElMessage.error('目录创建失败');
    }
  }
};

// 新增文件夹
const handleAddFolder = async (node, data) => {
  try {
    const { value } = await ElMessageBox.prompt('请输入目录名称', '新增目录', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
    });

    if (value) {
      const trimmedValue = value.trim();

      // 检查目录名是否已存在
      const existingDir = treeData.value.find(item => item.label === trimmedValue);
      if (existingDir) {
        ElMessage.warning('目录名称已存在');
        return;
      }

      console.log('====== 创建新目录 ======');
      console.log('目录名称:', trimmedValue);

      // 调用新的v2 API创建模块
      const response = await ApiManageApi.createModule({
        name: trimmedValue,
        description: `模块：${trimmedValue}`
      });

      if (response && response.code === 200) {
        // 重新加载数据
        await fetchTreeData();
        ElMessage.success('目录创建成功');
      } else {
        ElMessage.error('目录创建失败：' + (response?.message || '未知错误'));
      }


    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('新增目录失败:', error);
      ElMessage.error('目录创建失败');
    }
  }
};

// 重命名
const handleRename = async (node, data) => {
  const typeMap = {
    1: '目录',
    2: '接口',
    3: '用例'
  };
  const type = typeMap[node.level];
  const title = `重命名${type}`;

  try {
    const { value } = await ElMessageBox.prompt('请输入新名称', title, {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      inputValue: data.label,
    });

    if (value) {
      // 如果是接口级别（level 2），调用后端API更新
      if (node.level === 2) {
        try {
          console.log('====== 重命名接口 ======');
          console.log('接口数据:', data);
          console.log('data.apiData:', data.apiData);
          console.log('data.id:', data.id);

          // 使用正确的接口ID
          let apiId = data.apiData?.apiId || data.id;

          // 如果还是没有，尝试从node中获取
          if (!apiId && node.data) {
            apiId = node.data.apiData?.apiId || node.data.id;
          }

          // 确保ID是数字类型
          if (typeof apiId === 'string' && !isNaN(apiId)) {
            apiId = parseInt(apiId);
          }

          console.log('获取到的接口ID:', apiId, '类型:', typeof apiId, '新名称:', value);

          if (!apiId || apiId === 0) {
            console.error('无法获取接口ID，完整数据:', JSON.stringify(data, null, 2));
            ElMessage.error('无法获取接口ID');
            return;
          }

          // 使用新的v2 API更新接口
          const updateData = {
            name: value
          };

          // 调用v2更新接口API
          const response = await ApiManageApi.updateEndpoint(apiId, updateData);

          if (response && response.code === 200) {
            // 重新加载数据
            await fetchTreeData();
            ElMessage.success('接口重命名成功');
          } else {
            ElMessage.error('接口重命名失败：' + (response?.message || '未知错误'));
          }
        } catch (error) {
          console.error('重命名接口失败:', error);
          ElMessage.error('接口重命名失败');
        }
      } else if (node.level === 1) {
        // 目录重命名：更新目录名称
        console.log('====== 重命名目录 ======');
        console.log('目录数据:', data);
        console.log('data.apiData:', data.apiData);
        console.log('data.id:', data.id);

        let directoryId = data.apiData?.apiId || data.id;

        // 确保ID是数字类型
        if (typeof directoryId === 'string' && !isNaN(directoryId)) {
          directoryId = parseInt(directoryId);
        }

        console.log('获取到的目录ID:', directoryId, '类型:', typeof directoryId);

        if (!directoryId || directoryId === 0) {
          console.error('无法获取目录ID，完整数据:', JSON.stringify(data, null, 2));
          ElMessage.error('无法获取目录ID');
          return;
        }

        try {
          // 使用新的v2 API更新模块
          const updateData = {
            name: value
          };

          // 调用v2更新模块API
          const response = await ApiManageApi.updateModule(directoryId, updateData);
          if (response && response.code === 200) {
            // 重新加载数据以反映更改
            await fetchTreeData();
            ElMessage.success('目录重命名成功');
          } else {
            ElMessage.error('目录重命名失败：' + (response?.message || '未知错误'));
          }
        } catch (error) {
          console.error('重命名目录异常:', error);
          ElMessage.error('目录重命名失败');
        }
      } else {
        // 用例：暂时只做本地更新
        data.label = value;
        ElMessage.success(`${type}重命名成功`);
      }
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('重命名失败:', error);
      ElMessage.error(`${type}重命名失败`);
    }
  }
};

// 删除
const handleDelete = async (node, data) => {
  const typeMap = {
    1: '目录',
    2: '接口',
    3: '用例'
  };
  const type = typeMap[node.level];

  try {
    // 如果是接口级别（level 2），调用后端API删除
    if (node.level === 2) {
      try {
        // 确认删除接口
        await ElMessageBox.confirm(`确认删除接口"${data.label}"吗？`, '删除确认', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        });

        console.log('====== 删除接口 ======');
        console.log('接口数据:', data);
        console.log('data.apiData:', data.apiData);
        console.log('data.id:', data.id);

        // 使用正确的接口ID，优先使用apiData中的apiId，然后是data.id
        let apiId = data.apiData?.apiId || data.id;

        // 如果还是没有，尝试从node中获取
        if (!apiId && node.data) {
          apiId = node.data.apiData?.apiId || node.data.id;
        }

        // 确保ID是数字类型
        if (typeof apiId === 'string' && !isNaN(apiId)) {
          apiId = parseInt(apiId);
        }

        console.log('获取到的接口ID:', apiId, '类型:', typeof apiId);

        if (!apiId || apiId === 0) {
          console.error('无法获取接口ID，完整数据:', JSON.stringify(data, null, 2));
          ElMessage.error('无法获取接口ID');
          return;
        }

        // 使用新的v2 API删除接口
        const response = await ApiManageApi.deleteEndpoint(apiId);

        if (response && response.code === 200) {
          // 重新加载数据
          await fetchTreeData();
          ElMessage.success('接口删除成功');
        } else {
          ElMessage.error('接口删除失败：' + (response?.message || '未知错误'));
        }
      } catch (error) {
        console.error('删除接口失败:', error);
        ElMessage.error('接口删除失败');
      }
    } else if (node.level === 1) {
      // 目录删除：删除目录及其下的所有接口和用例
      try {
        console.log('====== 删除目录 ======');
        console.log('目录数据:', data);
        console.log('data.apiData:', data.apiData);
        console.log('data.id:', data.id);
        console.log('node.level:', node.level);
        console.log('node对象:', node);
        console.log('完整的data对象:', JSON.stringify(data, null, 2));

        // 检查data的所有属性
        console.log('data的所有属性:', Object.keys(data));
        console.log('data.apiData是否存在:', !!data.apiData);
        if (data.apiData) {
          console.log('data.apiData的所有属性:', Object.keys(data.apiData));
          console.log('data.apiData.apiId:', data.apiData.apiId);
        }

        // 尝试多种方式获取目录ID
        let directoryId = data.apiData?.apiId || data.id;

        // 如果还是没有ID，尝试从树形数据中查找
        if (!directoryId) {
          console.log('从data中无法获取ID，尝试从树形数据中查找...');
          const foundNode = treeData.value.find(item => item.label === data.label);
          if (foundNode) {
            directoryId = foundNode.apiData?.apiId || foundNode.id;
            console.log('从树形数据中找到的ID:', directoryId);
          }
        }

        // 如果还是没有，尝试从node中获取
        if (!directoryId && node.data) {
          directoryId = node.data.apiData?.apiId || node.data.id;
          console.log('从node.data中获取的ID:', directoryId);
        }

        // 确保ID是数字类型
        if (typeof directoryId === 'string' && !isNaN(directoryId)) {
          directoryId = parseInt(directoryId);
        }

        console.log('最终获取到的目录ID:', directoryId, '类型:', typeof directoryId);

        // 更详细的ID检查
        console.log('ID检查详情:');
        console.log('- directoryId:', directoryId);
        console.log('- directoryId === null:', directoryId === null);
        console.log('- directoryId === undefined:', directoryId === undefined);
        console.log('- directoryId === 0:', directoryId === 0);
        console.log('- !directoryId:', !directoryId);
        console.log('- Boolean(directoryId):', Boolean(directoryId));

        if (!directoryId || directoryId === 0) {
          console.error('❌ 无法获取目录ID');
          console.error('❌ 完整数据:', JSON.stringify(data, null, 2));
          console.error('❌ node对象:', JSON.stringify(node, null, 2));
          console.error('❌ 当前树形数据:', JSON.stringify(treeData.value, null, 2));
          ElMessage.error('无法获取目录ID，请刷新页面后重试');
          return;
        }

        console.log('✅ 目录ID获取成功，准备调用删除API');

        // 统计要删除的内容
        const apiCount = data.children ? data.children.filter(child => child.type === 'api').length : 0;
        const caseCount = data.children ? data.children.reduce((total, api) => {
          return total + (api.children ? api.children.filter(child => child.type === 'case').length : 0);
        }, 0) : 0;

        // 确认删除
        let confirmMessage = `确定要删除目录"${data.label}"吗？`;
        if (apiCount > 0 || caseCount > 0) {
          confirmMessage += `\n这将同时删除：\n- ${apiCount}个接口\n- ${caseCount}个用例\n\n此操作不可恢复。`;
        }

        await ElMessageBox.confirm(confirmMessage, '删除确认', {
          confirmButtonText: '确定删除',
          cancelButtonText: '取消',
          type: 'warning',
        });

        // 调用新的v2 API删除模块（后端应该级联删除所有子项）
        const response = await ApiManageApi.deleteModule(directoryId);

        if (response && response.code === 200) {
          // 重新加载数据
          await fetchTreeData();
          ElMessage.success(`目录删除成功${apiCount > 0 ? `，共删除${apiCount}个接口` : ''}`);
        } else {
          ElMessage.error('目录删除失败：' + (response?.message || '未知错误'));
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除目录失败:', error);
          ElMessage.error('目录删除失败');
        }
      }
    } else {
      // 用例删除
      try {
        // 确认删除用例
        await ElMessageBox.confirm(`确认删除用例"${data.label}"吗？`, '删除确认', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        });

        const parent = node.parent;
        const children = parent.data.children || parent.data;
        const index = children.findIndex(d => d.id === data.id);
        if (index !== -1) {
          children.splice(index, 1);
          ElMessage.success(`${type}删除成功`);
        } else {
          ElMessage.error(`${type}删除失败：未找到项目`);
        }
      } catch (error) {
        console.error('删除用例失败:', error);
        ElMessage.error('用例删除失败');
      }
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error);
      ElMessage.error(`${type}删除失败`);
    }
  }
};
// 当前悬停的节点
const currentHoveredNode = ref(null);

// 鼠标进入处理函数
const handleMouseEnter = (data) => {
  // 清除之前的悬停状态
  if (currentHoveredNode.value && currentHoveredNode.value !== data) {
    currentHoveredNode.value.isHovered = false;
  }

  // 设置当前悬停状态
  if (!data.hasOwnProperty('isHovered')) {
    data.isHovered = false;
  }
  data.isHovered = true;
  currentHoveredNode.value = data;
};

// 鼠标离开处理函数
const handleMouseLeave = (data) => {
  // 立即隐藏按钮，不管下拉菜单是否打开
  data.isHovered = false;
  if (currentHoveredNode.value === data) {
    currentHoveredNode.value = null;
  }
};

// 下拉菜单可见性变化处理
const handleDropdownVisibleChange = (visible) => {
  isDropdownOpen.value = visible;
  // 无论下拉菜单打开还是关闭，都不影响按钮显示
  // 按钮显示完全由鼠标是否在行内决定
};

// 下拉菜单区域离开处理
const handleDropdownMenuLeave = (node) => {
  isDropdownOpen.value = false;
  // 离开下拉菜单时，立即隐藏按钮
  if (currentHoveredNode.value) {
    currentHoveredNode.value.isHovered = false;
    currentHoveredNode.value = null;
  }
};

// 展开/折叠节点
const toggleExpand = (node) => {
  node.expanded = !node.expanded;
};

// 添加接口
const handleAddInterface = async (node, data) => {
  event?.stopPropagation();

  try {
    // 确定目录名称
    let directoryName = '默认目录';

    if (node.level === 1) {
      // 如果是目录级别，使用当前节点的label作为目录名
      directoryName = data.label || '默认目录';
    } else {
      // 其他情况使用默认目录
      directoryName = '默认目录';
    }

    console.log('添加接口 - 节点级别:', node.level, '节点数据:', data, '目录名称:', directoryName);

    // 生成默认接口名称
    const timestamp = Date.now();
    const defaultApiName = `新建接口_${timestamp}`;

    // 获取父级模块ID
    let moduleId = null;
    if (node.level === 1) {
      // 如果是在目录级别添加接口，获取模块的真实ID
      moduleId = data.apiData?.apiId;
      console.log('在模块中创建接口，模块ID:', moduleId);
    }

    if (!moduleId) {
      ElMessage.error('无法获取模块ID');
      return;
    }

    const newEndpointData = {
      moduleId: moduleId,
      name: defaultApiName,
      path: `/api/new-interface-${timestamp}`,
      method: 'GET',
      description: ''
    };

    console.log('====== 创建接口数据 ======');
    console.log('接口名称:', defaultApiName);
    console.log('模块ID:', moduleId);
    console.log('接口数据:', newEndpointData);

    // 调用新的v2 API创建接口
    const response = await ApiManageApi.createEndpoint(newEndpointData);

    if (response && response.code === 200) {
      // 如果是在空目录中添加接口，标记目录不再为空
      if (node.level === 1 && data.isEmpty) {
        data.isEmpty = false;
      }

      // 重新加载数据以获取最新的接口列表
      await fetchTreeData();

      // 创建成功后，自动切换到编辑tab并选中新创建的接口
      const createdEndpoint = response.data;
      console.log('====== 创建接口成功，准备切换tab ======');
      console.log('创建的接口数据:', createdEndpoint);
      console.log('当前activeTab:', activeTab?.value);
      console.log('editApiData是否存在:', !!editApiData);

      if (createdEndpoint && createdEndpoint.id) {
        // 切换到编辑tab
        if (activeTab) {
          console.log('切换前activeTab值:', activeTab.value);
          activeTab.value = 'edit';
          console.log('切换后activeTab值:', activeTab.value);
        } else {
          console.error('activeTab未正确注入');
        }

        // 设置编辑数据为新创建的接口
        if (editApiData) {
          const newEditData = {
            id: createdEndpoint.id,
            label: createdEndpoint.name,
            type: 'api',
            apiData: {
              apiId: createdEndpoint.id,
              apiName: createdEndpoint.name,
              requestMethod: createdEndpoint.method,
              apiPath: createdEndpoint.path,
              description: createdEndpoint.description,
              status: createdEndpoint.status
            }
          };
          console.log('设置编辑数据:', newEditData);
          editApiData.value = newEditData;
        } else {
          console.error('editApiData未正确注入');
        }

        ElMessage.success('接口创建成功，请在编辑页面完善接口信息');
      } else {
        console.error('创建接口响应数据异常:', response);
        ElMessage.success('接口创建成功');
      }
    } else {
      ElMessage.error('接口创建失败：' + (response?.message || '未知错误'));
    }
  } catch (error) {
    console.error('创建接口失败:', error);
    ElMessage.error('接口创建失败');
  }
};

// 文件夹操作
const handleFolderCommand = (command, node, rawData) => {
  event?.stopPropagation();
  switch (command) {
    case 'addFolder':
      ElMessageBox.prompt('请输入目录名称', '新增目录', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
      }).then(({ value }) => {
        if (value) {
          const newFolder = {
            id: Date.now(),
            label: value,
            children: [],
          };
          rawData.children = [...(rawData.children || []), newFolder];
          ElMessage.success('目录添加成功');
        }
      });
      break;
    case 'rename':
      ElMessageBox.prompt('请输入新的目录名称', '重命名目录', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputValue: rawData.label,
      }).then(({ value }) => {
        if (value) {
          data.label = value;
          ElMessage.success('重命名成功');
        }
      });
      break;
    case 'delete':
      ElMessageBox.confirm('确认删除该目录吗？', '删除确认', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
        const parent = node.parent;
        const children = parent.rawData.children || parent.rawData;
        const index = children.findIndex(d => d.id === rawData.id);
        children.splice(index, 1);
        ElMessage.success('删除成功');
      });
      break;
  }
};

// 获取文件夹颜色
const getFolderColor = (id) => {
  const colors = ["#409EFF", "#67C23A", "#E6A23C", "#F56C6C", "#909399"];
  return colors[id % colors.length];
};

// 过滤节点
const filterNode = (value, rawData) => {
  if (!value) return true;
  return rawData.label.includes(value);
};
// 重置表单
const resetForm = (formEl) => {
  if (!formEl) return;
  formEl.resetFields();
};

watch(filterText, (val) => {
  if (treeRef.value) {
    treeRef.value.filter(val);
  }
});

// 组件挂载时获取数据
onMounted(() => {
  fetchTreeData();
});

</script>

<style scoped>
.api-tree {
  height: 100%;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  min-width: 0; /* 允许收缩 */
}

.tree-card {
  height: 100%;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.tree-card :deep(.el-card__header) {
  flex-shrink: 0;
  padding: 16px 20px;
  border-bottom: 1px solid #e4e7ed;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-actions {
  display: flex;
  gap: 8px;
}

.tree-card :deep(.el-card__body) {
  flex: 1;
  padding: 16px 20px;
  overflow-y: auto;
  overflow-x: hidden; /* 防止横向滚动 */
  display: flex;
  flex-direction: column;
  min-width: 0; /* 允许收缩 */
}

.tree-card :deep(.el-input) {
  margin-bottom: 16px;
  flex-shrink: 0;
}

.tree-card :deep(.filter-tree) {
  flex: 1;
  overflow-y: auto;
}

:deep(.el-tree-node__content:hover) {
  background-color: #f5f7fa;
}

/* 移除重复的folder-actions样式 */

/* 树节点布局 */
.custom-tree-node {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  height: 28px;
  padding-right: 8px;
}

/* 自定义展开按钮容器 */
.expand-button-container {
  display: flex;
  align-items: center;
  height: 100%;
}

/* 自定义展开按钮 */
.custom-expand-btn {
  width: 16px;
  height: 16px;
  border: none;
  background: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #c0c4cc;
  margin-right: 4px;
  transition: transform 0.2s ease;
}

.custom-expand-btn:hover {
  color: #909399;
}

.custom-expand-btn .el-icon.expanded {
  transform: rotate(90deg);
}

/* 展开按钮占位符 */
.expand-placeholder {
  width: 16px;
  height: 16px;
  margin-right: 4px;
}

.node-content {
  display: flex;
  align-items: center;
  flex: 1;
  min-width: 0;
  overflow: hidden;
}

/* 不同层级的内容布局 */
.level-1-content {
  display: flex;
  align-items: center;
  width: 100%;
  padding-left: 0;
}

.level-2-content {
  display: flex;
  align-items: center;
  width: 100%;
  padding-left: 0; /* 缩进由节点级别控制 */
}

.level-3-content {
  display: flex;
  align-items: center;
  width: 100%;
  padding-left: 0; /* 缩进由节点级别控制 */
}

.node-label {
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  min-width: 0;
}

/* 操作按钮样式 */
.node-actions {
  display: flex;
  align-items: center;
  gap: 4px;
  opacity: 0;
  visibility: hidden;
  transition: all 0.2s ease;
  flex-shrink: 0;
}

.node-actions.show {
  opacity: 1;
  visibility: visible;
}

.node-actions .el-button {
  padding: 4px;
  min-width: 24px;
  height: 24px;
  border: none;
  background: transparent;
  color: #606266;
}

.node-actions .el-button:hover {
  background: #f5f7fa;
  color: #409eff;
}

:deep(.el-tree-node__content) {
  padding: 0 !important;
  position: relative;
  height: 32px;
  overflow: visible;
}

/* 图标样式 */
.folder-icon {
  font-size: 14px;
  color: #606266;
  margin-right: 6px;
  flex-shrink: 0;
}

.case-icon {
  font-size: 12px;
  color: #67c23a;
  margin-right: 6px;
  flex-shrink: 0;
}

/* 标签样式 */
.folder-label {
  font-weight: 500;
  color: #303133;
  font-size: 14px;
}

.api-label {
  color: #606266;
  font-size: 13px;
}

.case-label {
  color: #909399;
  font-size: 12px;
}

.item-count {
  color: #909399;
  font-size: 11px;
  margin-left: 2px;
  flex-shrink: 0;
  font-weight: normal;
}

.virtual-tag {
  color: #f56c6c;
  font-size: 10px;
  margin-left: 4px;
  padding: 1px 4px;
  background-color: #fef0f0;
  border-radius: 2px;
  border: 1px solid #fbc4c4;
}

/* HTTP方法标签 */
.http-method {
  display: inline-block;
  padding: 1px 5px;
  border-radius: 2px;
  font-size: 9px;
  font-weight: 600;
  text-transform: uppercase;
  min-width: 32px;
  text-align: center;
  margin-right: 6px;
  line-height: 1.2;
  flex-shrink: 0;
}

.get {
  background-color: #52c41a;
  color: #fff;
}

.post {
  background-color: #1890ff;
  color: #fff;
}

.put {
  background-color: #fa8c16;
  color: #fff;
}

.delete {
  background-color: #ff4d4f;
  color: #fff;
}

.patch {
  background-color: #722ed1;
  color: #fff;
}

/* 树节点样式 */
:deep(.el-tree-node__content) {
  height: 26px;
  position: relative;
  border-radius: 3px;
  margin: 1px 0;
  padding: 0 4px;
}

:deep(.el-tree-node__content:hover) {
  background-color: #f5f7fa;
}

/* 隐藏默认的展开图标 */
:deep(.el-tree-node__expand-icon) {
  display: none !important;
}

/* 不同层级的树节点样式 */
:deep(.el-tree-node[aria-level="1"] > .el-tree-node__content) {
  font-weight: 500;
  background-color: transparent;
}

:deep(.el-tree-node[aria-level="2"] > .el-tree-node__content) {

}

:deep(.el-tree-node[aria-level="3"] > .el-tree-node__content) {

}

/* 选中状态 */
:deep(.el-tree-node.is-current > .el-tree-node__content) {
  background-color: #e6f7ff;
  color: #1890ff;
}

/* 树的缩进控制 */
:deep(.el-tree-node__children) {
  overflow: visible;
}

/* 层级1的子节点缩进 */
:deep(.el-tree-node[data-level="1"] > .el-tree-node__children) {
  padding-left: 0;
}

/* 层级2的子节点缩进 */
:deep(.el-tree-node[data-level="2"] > .el-tree-node__children) {
  padding-left: 0;
}


</style>