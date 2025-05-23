<template>
  <div class="bug-detail-page">
    <el-card shadow="always" class="mb-4">
      <template #header>
        <div class="flex justify-between items-center">
          <h2 class="text-xl font-bold">缺陷详情</h2>
          <div>
            <el-button type="primary" @click="handleEdit">编辑</el-button>
            <el-button @click="goBack">返回</el-button>
          </div>
        </div>
      </template>
      
      <div v-loading="loading">
        <div class="grid grid-cols-2 gap-4 mb-4">
          <div>
            <p><strong>缺陷ID:</strong> {{ bug.bugId }}</p>
            <p><strong>缺陷标题:</strong> {{ bug.bugTitle }}</p>
            <p><strong>严重程度:</strong> 
              <el-tag :type="getSeverityTagType(bug.severity)">{{ bug.severity }}</el-tag>
            </p>
            <p><strong>优先级:</strong> 
              <el-tag :type="getLevelTagType(bug.level)">{{ bug.level }}</el-tag>
            </p>
          </div>
          <div>
            <p><strong>状态:</strong> 
              <el-tag :type="getStatusTagType(bug.status)">{{ bug.status }}</el-tag>
            </p>
            <p><strong>缺陷类型:</strong> 
              <el-tag :type="getBugTypeTagType(bug.bugType)">{{ bug.bugType }}</el-tag>
            </p>
            <p><strong>创建人:</strong> {{ bug.createdBy }}</p>
            <p><strong>创建时间:</strong> {{ formatDateTime(bug.createdAt) }}</p>
          </div>
        </div>
        
        <div class="mb-4">
          <p><strong>指派给:</strong> {{ bug.assignedTo }}</p>
        </div>
        
        <div class="mb-4">
          <p><strong>所属项目:</strong> {{ bug.projectId }}</p>
          <p><strong>所属计划:</strong> {{ bug.planId }}</p>
          <p v-if="bug.caseId"><strong>关联测试用例:</strong> {{ bug.caseId }}</p>
        </div>
        
        <div class="mb-4">
          <h3 class="text-lg font-bold mb-2">重现步骤</h3>
          <el-card shadow="hover" class="bg-gray-50">
            <pre class="whitespace-pre-wrap">{{ bug.description || '无' }}</pre>
          </el-card>
        </div>
      </div>
    </el-card>
    
    <!-- 历史记录 -->
    <el-card shadow="always" class="mb-4">
      <template #header>
        <h3 class="text-lg font-bold">历史记录</h3>
      </template>
      
      <el-timeline>
        <el-timeline-item
          v-for="(activity, index) in activities"
          :key="index"
          :timestamp="activity.timestamp"
          :type="activity.type"
          :color="activity.color"
        >
          {{ activity.content }}
        </el-timeline-item>
      </el-timeline>
      
      <div class="flex justify-center mt-4" v-if="activities.length === 0">
        <el-empty description="暂无历史记录" />
      </div>
    </el-card>
    
    <!-- 评论区 -->
    <el-card shadow="always">
      <template #header>
        <h3 class="text-lg font-bold">评论</h3>
      </template>
      
      <div class="comments-section">
        <div v-for="(comment, index) in comments" :key="index" class="comment-item mb-4 pb-4 border-b">
          <div class="flex justify-between items-start">
            <div class="flex items-center">
              <el-avatar :size="40" class="mr-3">{{ comment.author.charAt(0) }}</el-avatar>
              <div>
                <div class="font-bold">{{ comment.author }}</div>
                <div class="text-gray-500 text-sm">{{ comment.time }}</div>
              </div>
            </div>
            <el-dropdown v-if="comment.author === currentUser">
              <span class="el-dropdown-link cursor-pointer">
                <el-icon><more /></el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="editComment(index)">编辑</el-dropdown-item>
                  <el-dropdown-item @click="deleteComment(index)">删除</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
          <div class="mt-3 ml-12">{{ comment.content }}</div>
        </div>
        
        <div class="flex justify-center mt-4" v-if="comments.length === 0">
          <el-empty description="暂无评论" />
        </div>
        
        <div class="add-comment mt-4">
          <el-input
            v-model="newComment"
            type="textarea"
            :rows="3"
            placeholder="添加评论..."
          />
          <div class="flex justify-end mt-2">
            <el-button type="primary" @click="addComment">发表评论</el-button>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { BugApi } from '@/api/bugService'
import { More } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const bugId = route.params.id

// 缺陷数据
const bug = ref({})
const loading = ref(false)

// 历史记录
const activities = ref([
  {
    content: '创建了缺陷',
    timestamp: '2023-05-20 10:00',
    type: 'primary',
    color: '#0bbd87'
  },
  {
    content: '更新了状态为"待修复"',
    timestamp: '2023-05-21 14:30',
    type: 'warning',
    color: '#e6a23c'
  },
  {
    content: '指派给了开发人员',
    timestamp: '2023-05-21 15:00',
    type: 'info',
    color: '#909399'
  }
])

// 评论
const currentUser = ref('当前用户')
const comments = ref([
  {
    author: '测试人员',
    time: '2023-05-20 10:30',
    content: '这个问题在Chrome浏览器上复现率很高'
  },
  {
    author: '开发人员',
    time: '2023-05-21 16:45',
    content: '已经定位到问题，正在修复中'
  },
  {
    author: '当前用户',
    time: '2023-05-22 09:15',
    content: '请尽快修复，这个问题影响了用户体验'
  }
])
const newComment = ref('')

// 获取缺陷详情
const fetchBugDetail = async () => {
  loading.value = true
  try {
    const res = await BugApi.getBug(bugId)
    bug.value = res.data
    loading.value = false
  } catch (error) {
    ElMessage.error('获取缺陷详情失败')
    console.error('获取缺陷详情失败:', error)
    loading.value = false
  }
}

// 编辑缺陷
const handleEdit = () => {
  router.push(`/bug/edit/${bugId}`)
}

// 返回上一页
const goBack = () => {
  router.go(-1)
}

// 添加评论
const addComment = () => {
  if (!newComment.value.trim()) {
    ElMessage.warning('评论内容不能为空')
    return
  }
  
  const now = new Date()
  const formattedTime = now.toLocaleString()
  
  comments.value.push({
    author: currentUser.value,
    time: formattedTime,
    content: newComment.value
  })
  
  newComment.value = ''
  ElMessage.success('评论发表成功')
}

// 编辑评论
const editComment = (index) => {
  ElMessageBox.prompt('请输入新的评论内容', '编辑评论', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    inputValue: comments.value[index].content
  }).then(({ value }) => {
    comments.value[index].content = value
    ElMessage.success('评论已更新')
  }).catch(() => {})
}

// 删除评论
const deleteComment = (index) => {
  ElMessageBox.confirm('确定要删除这条评论吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    comments.value.splice(index, 1)
    ElMessage.success('评论已删除')
  }).catch(() => {})
}

// 格式化日期时间
const formatDateTime = (dateTime) => {
  if (!dateTime) return ''
  const date = new Date(dateTime)
  return date.toLocaleString()
}

// 获取严重程度标签类型
const getSeverityTagType = (severity) => {
  const severityMap = {
    '阻塞': 'danger',
    '严重': 'warning',
    '一般': 'info',
    '轻微': 'success'
  }
  return severityMap[severity] || ''
}

// 获取优先级标签类型
const getLevelTagType = (level) => {
  const levelMap = {
    '1': 'danger',
    '2': 'warning',
    '3': 'info',
    '4': 'success'
  }
  return levelMap[level] || ''
}

// 获取状态标签类型
const getStatusTagType = (status) => {
  const statusMap = {
    '已创建': 'info',
    '待修复': 'warning',
    '已修复': 'success',
    '已关闭': 'info'
  }
  return statusMap[status] || ''
}

// 获取缺陷类型标签类型
const getBugTypeTagType = (type) => {
  const typeMap = {
    '功能问题': 'primary',
    '接口问题': 'success',
    '性能问题': 'warning',
    '安全问题': 'danger',
    '兼容性问题': 'info'
  }
  return typeMap[type] || ''
}

onMounted(() => {
  fetchBugDetail()
})
</script>

<style scoped>
.bug-detail-page {
  padding: 20px;
}

.comment-item {
  border-bottom-color: #ebeef5;
}
</style>
