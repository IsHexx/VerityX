<template>
  <div class="project-list-container">
    <el-row :gutter="20">
      <!-- 项目卡片 -->
      <el-col
        :xs="12"
        :sm="8"
        :md="6"
        :lg="6"
        v-for="project in projects"
        :key="project.id"
        style="margin-bottom: 5px"
      >
        <el-card class="project-card mb-4" :body-style="{ padding: '12px' }">
          <div class="card-content">
            <!-- 右上角图标和下拉菜单 -->
            <div class="card-actions">
              <el-dropdown
                trigger="hover"
                @command="handleCommand($event, project)"
              >
                <el-icon class="action-icon"><MoreFilled /></el-icon>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="edit">
                      <el-icon style="color: #cbbbf7"><Edit /></el-icon>
                      编辑项目
                    </el-dropdown-item>
                    <el-dropdown-item command="enter">
                      <el-icon style="color: #67c23a"><Right /></el-icon>
                      进入项目
                    </el-dropdown-item>
                    <el-dropdown-item command="members">
                      <el-icon style="color: #e6a23c"><User /></el-icon>
                      成员管理
                    </el-dropdown-item>
                    <el-dropdown-item command="delete">
                      <el-icon style="color: #e34c26"><Delete /></el-icon>
                      删除项目
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>

            <!-- 项目主要内容 -->
            <div class="card-main">
              <!-- 项目图标 -->
              <div class="project-icon">
                <el-image :src="project.image" fit="cover" class="icon-image">
                  <template #error>
                    <div class="folder-icon-wrapper">
                      <el-icon><Folder /></el-icon>
                    </div>
                  </template>
                </el-image>
              </div>

              <!-- 项目信息 -->
              <div class="project-info">
                <el-tooltip
                  :content="project.projectName"
                  :show-after="200"
                  :hide-after="0"
                  popper-class="custom-tooltip"
                >
                  <h3 class="project-title">{{ project.projectName }}</h3>
                </el-tooltip>

                <el-tooltip
                  :content="project.description"
                  :show-after="200"
                  :hide-after="0"
                  popper-class="custom-tooltip"
                >
                  <p class="project-overview">{{ project.description }}</p>
                </el-tooltip>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 添加项目卡片 -->
      <el-col :xs="12" :sm="8" :md="6" :lg="6">
        <el-card
          class="add-project-card mb-4 cursor-pointer"
          :body-style="{ padding: '12px' }"
          @click="handleAddProject"
        >
          <div class="add-project-content">
            <el-icon class="add-icon"><Plus /></el-icon>
            <span>新增项目</span>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 项目表单对话框 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="400px">
      <el-form
        ref="projectFormRef"
        :model="projectForm"
        :rules="rules"
        label-width="80px"
      >
        <el-form-item label="项目名称" prop="name">
          <el-input v-model="projectForm.name" placeholder="请输入项目名称" />
        </el-form-item>
        <el-form-item label="项目描述" prop="description">
          <el-input
            v-model="projectForm.description"
            placeholder="请输入项目描述"
          />
        </el-form-item>
        <el-form-item label="开始时间" prop="startDate">
          <el-date-picker
            v-model="projectForm.startDate"
            type="date"
            placeholder="项目开始时间"
            value-format="YYYY-MM-DD"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="结束时间" prop="endDate">
          <el-date-picker
            v-model="projectForm.endDate"
            type="date"
            placeholder="项目结束时间"
            value-format="YYYY-MM-DD"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="projectForm.status" clearable placeholder="状态">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="项目图标" prop="image">
          <el-upload
            class="project-icon-upload"
            action="/api/upload"
            :show-file-list="false"
            :on-success="handleUploadSuccess"
            :before-upload="beforeUpload"
          >
            <div class="icon-upload-area">
              <el-image
                v-if="projectForm.image"
                :src="projectForm.image"
                class="upload-preview"
              />
              <div v-else class="upload-placeholder">
                <el-icon><Plus /></el-icon>
                <span>上传图标</span>
              </div>
            </div>
          </el-upload>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>

    <!-- 成员管理对话框 -->
    <el-dialog title="成员管理" v-model="memberDialogVisible" width="500px">
      <div class="member-management">
        <div class="member-list">
          <el-table :data="projectMembers" stripe>
            <el-table-column prop="name" label="成员名称" />
            <el-table-column prop="role" label="角色" />
            <el-table-column label="操作" width="80">
              <template #default="{ row }">
                <el-button type="danger" link @click="removeMember(row)">
                  移除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <div class="add-member mt-4">
          <el-button type="primary" @click="showAddMemberForm">
            添加成员
          </el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>
  
<script setup>
import { ref, reactive, onMounted, watch } from "vue";
import {
  Edit,
  Right,
  User,
  Picture,
  Plus,
  MoreFilled,
  Folder,
} from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";
import { useRouter } from "vue-router";
import { ProjectApi } from "@/api/projectService";
import axios from "axios";
import { useProjectStore } from "@/store/projectStore"; // 导入项目存储

const router = useRouter();
// 使用项目存储
const { setCurrentProject } = useProjectStore();
// 表单相关
const dialogVisible = ref(false);
const dialogTitle = ref("");
const projectFormRef = ref(null);
const projectForm = reactive({
  name: "",
  description: "",
  startDate: "",
  endDate: "",
  status: "",
});

const options = [
  {
    value: "pending",
    label: "未开始",
  },
  {
    value: "ongoing",
    label: "正在进行",
  },
  {
    value: "completed",
    label: "已完成",
  },
];

// 项目列表数据
const projects = ref([]);

const fetchProjects = async () => {
  try {
    const res = await ProjectApi.getProjectList();
    // const res = await axios.get("/api/projects");

    projects.value = res.data;
  } catch (error) {
    ElMessage.error("获取项目列表失败");
    console.error("获取项目列表失败:", error);
  } finally {
    console.log("项目列表数据加载完成");
  }
};

// 在组件挂载时获取项目列表
onMounted(() => {
  fetchProjects();
});

// 表单校验规则
const rules = {
  name: [
    { required: true, message: "请输入项目名称", trigger: "blur" },
    { min: 2, max: 50, message: "长度在 2 到 50 个字符", trigger: "blur" },
  ],
  image: [{ required: false, message: "请上传项目图片", trigger: "change" }],
};

// 处理项目操作命令
const handleCommand = (command, project) => {
  switch (command) {
    case "edit":
      editProject(project);
      break;
    case "enter":
      enterProject(project);
      break;
    case "members":
      openMemberDialog(project);
      break;
    case "delete":
      deleteProject(project);
      break;
  }
};

// 成员管理相关
const memberDialogVisible = ref(false);
const projectMembers = ref([]);
const currentProject = ref(null);

// 添加项目
const handleAddProject = () => {
  dialogTitle.value = "添加项目";
  projectId : null,
  projectForm.name = "";
  projectForm.description = "";
  projectForm.startDate = "";
  projectForm.endDate = "";
  projectForm.status = "";
  dialogVisible.value = true;
};

// 编辑项目
const editProject = (project) => {
  console.log("正在编辑的project", project);
  dialogTitle.value = "编辑项目";
  projectForm.projectId = project.projectId; 
  projectForm.name = project.projectName;
  projectForm.description = project.description;
  projectForm.startDate = project.startDate;
  projectForm.endDate = project.endDate;
  projectForm.status = project.status;
  dialogVisible.value = true;
};

// 进入项目
const enterProject = (project) => {
  // 打印项目对象，查看其结构
  console.log("进入项目:", project);
  
  // 设置当前项目
  setCurrentProject(project);
  // 导航到概览页面
  router.push("/overview");
};

// 删除项目
const deleteProject = async (projectId) => {
  try {
    await projectApi.deleteProject(projectId);
    ElMessage.success("项目删除成功");
    // 刷新项目列表
    fetchProjects();
  } catch (error) {
    ElMessage.error("删除项目失败");
    console.error("删除项目失败:", error);
  }
};

// 处理成员管理
const openMemberDialog = (project) => {
  currentProject.value = project;
  projectMembers.value = [
    { name: "张三", role: "管理员" },
    { name: "李四", role: "开发者" },
  ];
  memberDialogVisible.value = true;
};

// 上传相关方法
const handleUploadSuccess = (response) => {
  projectForm.image = response.url;
  ElMessage.success("上传成功");
};

const beforeUpload = (file) => {
  const isImage = file.type.startsWith("image/");
  const isLt2M = file.size / 1024 / 1024 < 2;

  if (!isImage) {
    ElMessage.error("只能上传图片文件！");
    return false;
  }
  if (!isLt2M) {
    ElMessage.error("图片大小不能超过 2MB！");
    return false;
  }
  return true;
};

// 提交表单
const submitForm = async (project) => {
  
  if (!projectFormRef.value) return;

  try {
    await projectFormRef.value.validate();
    
    if (projectForm.projectId) {
      // 更新项目
      
      await projectApi.updateProject(projectForm.projectId, {
        projectId: projectForm.projectId,
        projectName: projectForm.name,
        description: projectForm.description,
        startDate: projectForm.startDate,
        endDate: projectForm.endDate,
        status: projectForm.status
      });
      ElMessage.success("项目更新成功");
    } else {
      // 创建新项目
      await projectApi.createProject({
        projectName: projectForm.name,
        description: projectForm.description,
        startDate: projectForm.startDate,
        endDate: projectForm.endDate,
        status: projectForm.status,
        // image: projectForm.image
      });
      ElMessage.success("项目创建成功");
    }

    dialogVisible.value = false;
    fetchProjects(); // 刷新项目列表
  } catch (error) {
    ElMessage.error(error.message || "保存失败");
    console.error("保存项目失败:", error);
  }
};

const resetForm = () => {
  projectForm.projectId = null;
  projectForm.name = "";
  projectForm.description = "";
  projectForm.startDate = "";
  projectForm.endDate = "";
  projectForm.status = "";
  projectForm.image = "";
};

// 修改对话框的关闭处理
watch(dialogVisible, (newVal) => {
  if (!newVal) {
    resetForm();
  }
});

// 成员管理相关方法
const removeMember = (member) => {
  ElMessage.success(`已移除成员：${member.name}`);
};

const showAddMemberForm = () => {
  // 实现添加成员的逻辑
};
</script>
  


  
<style>
.project-list-container {
  padding: 20px;
}

/* 卡片基础样式 */
.project-card {
  position: relative;
  min-height: 200px;
  transition: all 0.3s;
}

.project-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

/* 卡片内容布局 */
.card-content {
  position: relative;
  height: 100%;
}

/* 右上角操作区域 */
.card-actions {
  position: absolute;
  top: 0;
  right: 0;
  z-index: 1;
}

.action-icon {
  padding: 8px;
  font-size: 16px;
  color: #909399;
  cursor: pointer;
  transition: color 0.3s;
}

.action-icon:hover {
  color: #409eff;
}

/* 卡片主要内容区域 */
.card-main {
  padding-top: 8px;
}

/* 项目图标样式 */
.project-icon {
  width: 60px;
  height: 60px;
  border-radius: 6px;
  overflow: hidden;
  background-color: #f5f7fa;
  margin-bottom: 12px;
}

.icon-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.folder-icon-wrapper {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  color: #909399;
}

/* 项目信息样式 */
.project-info {
  flex: 1;
}

.project-title {
  margin: 0 0 8px 0;
  font-size: 14px;
  font-weight: bold;
  /* font-weight: 500; */
  color: #303133;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  cursor: pointer;
}

.project-overview {
  margin: 10px;
  font-size: 12px;
  color: #606266;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 4;
  -webkit-box-orient: vertical;
  cursor: pointer;
}

/* 添加项目卡片样式 */
.add-project-card {
  min-height: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s;
}

.add-project-card:hover {
  border-color: #409eff;
}

.add-project-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  color: #909399;
}

.add-icon {
  font-size: 24px;
}

/* 上传区域样式 */
.project-icon-upload {
  text-align: center;
}

.icon-upload-area {
  width: 64px;
  height: 64px;
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  transition: border-color 0.3s;
}

.icon-upload-area:hover {
  border-color: #409eff;
}

.upload-preview {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.upload-placeholder {
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 4px;
  color: #909399;
}

.upload-placeholder span {
  font-size: 12px;
}

/* 成员管理对话框样式 */
.member-management {
  max-height: 400px;
  overflow-y: auto;
}

/* 自定义tooltip样式 */
.custom-tooltip {
  max-width: 300px !important; /* 设置最大宽度 */
  background: white !important; /* 白色背景 */
  color: #303133 !important; /* 文字颜色改为深色 */
  border: 1px solid #121213 !important; /* 添加边框 */
  padding: 8px 12px !important; /* 调整内边距 */
  line-height: 1.4 !important; /* 调整行高 */
}

/* 修改tooltip箭头样式 */
.custom-tooltip .el-popper__arrow::before {
  background: white !important;
  border: 1px solid #dcdfe6 !important;
}
</style>