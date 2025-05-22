<template>
  <div class="project-selector">
    <el-select
      v-model="selectedProjectId"
      placeholder="请选择项目"
      @change="handleProjectChange"
      style="width: 200px"
    >
      <el-option
        v-for="project in projectList"
        :key="project.id"
        :label="project.projectName"
        :value="project.id"
      >
        <div class="project-option">
          <span>{{ project.projectName }}</span>
          <span class="project-detail" v-if="project.id">
            (ID: {{ project.id }})
          </span>
          <span class="project-status" v-if="project.status">
            <el-tag size="small" :type="getStatusType(project.status)">
              {{ project.status }}
            </el-tag>
          </span>
        </div>
      </el-option>
    </el-select>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { ElMessage } from 'element-plus';
import { useProjectStore } from '@/store/projectStore';
import { ProjectApi } from '@/api/projectService';

// 使用项目store
const projectStore = useProjectStore();

// 确保初始化项目状态
projectStore.initProjectState();

// 选中的项目ID
const selectedProjectId = computed({
  get: () => {
    const currentId = projectStore.getCurrentProjectId();
    return currentId;
  },
  set: (val) => {
    // 选中项目后会触发handleProjectChange，这里不需要额外处理
  }
});

// 项目列表
const projectList = ref([]);

// 获取项目列表
const fetchProjectList = async () => {
  try {
    const res = await ProjectApi.getProjectList();
    if (res.code === 200) {
      console.log("获取项目列表成功, 原始数据:", res.data);
      
      // 检查返回数据的格式
      let projectData = [];
      if (Array.isArray(res.data)) {
        // 数据直接是数组
        projectData = res.data;
      } else if (res.data && Array.isArray(res.data.list)) {
        // 数据在list属性中
        projectData = res.data.list;
      } else if (res.data && typeof res.data === 'object') {
        // 尝试转换对象格式为数组
        projectData = Object.values(res.data);
      }
      
      // 标准化项目数据结构
      projectList.value = projectData.map(project => {
        // 确保所有必要字段
        return {
          id: project.id || project.projectId,
          projectId: project.id || project.projectId,
          projectName: project.projectName || project.name || `项目 ${project.id || project.projectId}`,
          status: project.status || '进行中'
        };
      });
      
      console.log("处理后的项目列表:", projectList.value);
      
      // 如果仍然没有项目数据，使用模拟数据
      if (projectList.value.length === 0) {
        console.warn("没有找到有效的项目数据，使用模拟数据");
        useMockData();
      }
    } else {
      // 如果API调用失败，使用模拟数据
      console.warn("API调用失败，使用模拟数据:", res.message);
      useMockData();
    }
    
    // 如果没有选中项目，且有项目列表，自动选择第一个
    if (!selectedProjectId.value && projectList.value.length > 0) {
      handleProjectChange(projectList.value[0].id);
    }
  } catch (error) {
    console.error('获取项目列表失败:', error);
    ElMessage.error('获取项目列表失败，使用模拟数据');
    useMockData();
  }
};

// 使用模拟数据
const useMockData = () => {
  // 模拟数据
  projectList.value = [
    { id: '1', projectId: '1', projectName: '电商系统项目', status: '进行中' },
    { id: '2', projectId: '2', projectName: '金融系统项目', status: '已完成' },
    { id: '3', projectId: '3', projectName: '社交媒体项目', status: '规划中' }
  ];
  
  console.log("使用模拟数据:", projectList.value);
  
  // 如果没有选中项目，选择第一个
  if (!selectedProjectId.value && projectList.value.length > 0) {
    handleProjectChange(projectList.value[0].id);
  }
};

// 处理项目变更
const handleProjectChange = (projectId) => {
  if (!projectId) return;
  
  console.log("切换到项目ID:", projectId);
  
  // 根据ID找到对应的项目对象
  const selectedProject = projectList.value.find(p => p.id == projectId);
  
  if (selectedProject) {
    console.log("找到项目:", selectedProject);
    // 设置当前项目
    projectStore.setCurrentProject(selectedProject);
    ElMessage.success(`已切换到项目: ${selectedProject.projectName}`);
    
    // 触发页面刷新，或通过事件通知其他组件
    window.dispatchEvent(new CustomEvent('project-changed', { 
      detail: selectedProject 
    }));
  } else {
    console.warn(`未找到ID为${projectId}的项目`);
  }
};

// 获取状态标签样式
const getStatusType = (status) => {
  const statusMap = {
    '进行中': 'primary',
    '已完成': 'success',
    '规划中': 'info',
    '暂停': 'warning',
    '取消': 'danger'
  };
  return statusMap[status] || 'info';
};

// 组件挂载时获取项目列表
onMounted(() => {
  fetchProjectList();
});
</script>

<style scoped>
.project-selector {
  display: inline-block;
  margin-right: 16px;
}

.project-option {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.project-detail {
  font-size: 12px;
  color: #909399;
  margin-left: 5px;
}

.project-status {
  margin-left: 8px;
}
</style> 