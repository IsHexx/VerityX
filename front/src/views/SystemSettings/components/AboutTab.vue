<template>
  <div class="about-container">
    <!-- 系统基本信息卡片 -->
    <el-card class="mb-4">
      <template #header>
        <div class="card-header">
          <span>系统基本信息</span>
          <el-button
            v-if="hasPermission"
            type="primary"
            size="small"
            @click="handleEdit"
          >
            编辑信息
          </el-button>
        </div>
      </template>

      <div v-if="loading" class="center-content">
        <el-skeleton animated :rows="5" />
      </div>

      <div v-else-if="!systemInfo" class="empty-data">
        <el-empty description="暂无系统信息" />
      </div>

      <div v-else class="about-detail">
        <div class="system-header">
          <div class="logo-container">
            <!-- <img v-if="systemInfo.systemLogo" :src="systemInfo.systemLogo" alt="系统Logo" class="system-logo" />
            <img v-else src="/assetslogo.png" alt="系统Logo" class="system-logo" /> -->
            <img  src="@/assets/logo.png" alt="系统Logo" class="system-logo" />
          </div>
          <div class="system-title">
            <h2>{{ systemInfo.systemName }}</h2>
          </div>
        </div>

        <el-descriptions :column="2" border>
          <el-descriptions-item label="公司名称">{{ systemInfo.companyName || '未设置' }}</el-descriptions-item>
          <el-descriptions-item label="公司网站">
            <a v-if="systemInfo.companyWebsite" :href="systemInfo.companyWebsite" target="_blank">{{ systemInfo.companyWebsite }}</a>
            <span v-else>未设置</span>
          </el-descriptions-item>
          <el-descriptions-item label="联系邮箱">
            <a v-if="systemInfo.contactEmail" :href="`mailto:${systemInfo.contactEmail}`">{{ systemInfo.contactEmail }}</a>
            <span v-else>未设置</span>
          </el-descriptions-item>
          <el-descriptions-item label="版权信息">{{ systemInfo.copyright || '未设置' }}</el-descriptions-item>
        </el-descriptions>

        <div v-if="systemInfo.description" class="description-section">
          <div class="section-title">系统描述</div>
          <div class="section-content">{{ systemInfo.description }}</div>
        </div>
      </div>
    </el-card>

    <!-- 服务条款和隐私政策 -->
    <el-card>
      <template #header>
        <div class="card-header">
          <span>服务条款与隐私政策</span>
        </div>
      </template>

      <el-tabs v-model="activeTab">
        <el-tab-pane label="服务条款" name="terms">
          <div v-if="termsLoading" class="center-content">
            <el-skeleton animated :rows="8" />
          </div>

          <div v-else-if="!termsContent" class="empty-data">
            <el-empty description="暂无服务条款内容" />
          </div>

          <div v-else class="terms-content content-box">
            <div v-html="formattedTerms"></div>
          </div>
        </el-tab-pane>

        <el-tab-pane label="隐私政策" name="privacy">
          <div v-if="privacyLoading" class="center-content">
            <el-skeleton animated :rows="8" />
          </div>

          <div v-else-if="!privacyContent" class="empty-data">
            <el-empty description="暂无隐私政策内容" />
          </div>

          <div v-else class="privacy-content content-box">
            <div v-html="formattedPrivacy"></div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 编辑系统信息对话框 -->
    <el-dialog
      v-model="dialogVisible"
      title="编辑系统信息"
      width="650px"
    >
      <el-form
        ref="aboutFormRef"
        :model="aboutForm"
        :rules="aboutRules"
        label-width="100px"
      >
        <el-form-item label="系统名称" prop="systemName">
          <el-input v-model="aboutForm.systemName" placeholder="请输入系统名称" />
        </el-form-item>


          <div v-if="logoType === 'default'" class="default-logo-preview">
            <img src="@/assets/logo.png" alt="默认Logo" class="logo-preview" />
            <span class="logo-hint">使用系统默认Logo</span>
          </div>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="公司名称" prop="companyName">
              <el-input v-model="aboutForm.companyName" placeholder="请输入公司名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="公司网站" prop="companyWebsite">
              <el-input v-model="aboutForm.companyWebsite" placeholder="请输入公司网站" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="联系邮箱" prop="contactEmail">
              <el-input v-model="aboutForm.contactEmail" placeholder="请输入联系邮箱" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="版权信息" prop="copyright">
              <el-input v-model="aboutForm.copyright" placeholder="请输入版权信息" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="系统描述" prop="description">
          <el-input
            v-model="aboutForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入系统描述"
          />
        </el-form-item>
        <el-tabs v-model="activeEditTab" class="form-tabs">
          <el-tab-pane label="服务条款" name="terms">
            <el-form-item label="服务条款" prop="termsOfService" label-width="0">
              <el-input
                v-model="aboutForm.termsOfService"
                type="textarea"
                :rows="10"
                placeholder="请输入服务条款内容"
              />
            </el-form-item>
          </el-tab-pane>
          <el-tab-pane label="隐私政策" name="privacy">
            <el-form-item label="隐私政策" prop="privacyPolicy" label-width="0">
              <el-input
                v-model="aboutForm.privacyPolicy"
                type="textarea"
                :rows="10"
                placeholder="请输入隐私政策内容"
              />
            </el-form-item>
          </el-tab-pane>
        </el-tabs>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm" :loading="submitLoading">
            保存
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { SysInfoApi } from '@/api/sysInfoService'
import { ElMessage } from 'element-plus'
import { checkPermission } from '@/utils/permission'
import { formatDateTime } from '@/utils/date'
import { marked } from 'marked'

// 注意：我们直接在模板中使用静态路径 /assets/images/logo.png
// 因为DOM中显示的路径是这个，所以我们直接使用这个路径

// 数据状态
const systemInfo = ref(null)
const termsContent = ref('')
const privacyContent = ref('')
const loading = ref(false)
const termsLoading = ref(false)
const privacyLoading = ref(false)
const dialogVisible = ref(false)
const submitLoading = ref(false)
const hasPermission = checkPermission('system:info:edit')

// 选项卡状态
const activeTab = ref('terms')
const activeEditTab = ref('terms')

// Logo选择类型：default-使用默认logo，custom-使用自定义URL
const logoType = ref('default')

// 表单数据
const aboutFormRef = ref(null)
const aboutForm = reactive({
  id: null,
  systemName: '',
  systemLogo: '',
  companyName: '',
  companyWebsite: '',
  contactEmail: '',
  copyright: '',
  description: '',
  termsOfService: '',
  privacyPolicy: ''
})

// 表单验证规则
const aboutRules = {
  systemName: [
    { required: true, message: '请输入系统名称', trigger: 'blur' },
    { max: 100, message: '系统名称不能超过100个字符', trigger: 'blur' }
  ],
  contactEmail: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ]
}

// 处理文本，分段并去除特殊字符
const processText = (text) => {
  if (!text) return ''

  // 按段落分割（保留空行作为段落分隔）
  const paragraphs = text.split(/\n+/)
    .map(paragraph => paragraph.trim())
    // 过滤掉空段落
    .filter(paragraph => paragraph.length > 0)
    // 去除特殊符号（保留基本标点符号）
    .map(paragraph => paragraph.replace(/[^\w\s\u4e00-\u9fa5.,?!;:()"'-]/g, ''))

  // 添加Markdown格式的段落标记
  // 每个段落前后添加空行，确保Markdown渲染时正确分段
  return paragraphs.map(p => `${p}\n\n`).join('')
}

// 格式化的服务条款和隐私政策 - 支持Markdown格式
const formattedTerms = computed(() => {
  if (!termsContent.value) return ''

  try {
    // 先处理文本，再转换为Markdown
    const processedText = processText(termsContent.value)
    return marked(processedText)
  } catch (e) {
    console.error('解析服务条款Markdown失败', e)
    return processText(termsContent.value)
  }
})

const formattedPrivacy = computed(() => {
  if (!privacyContent.value) return ''

  try {
    // 先处理文本，再转换为Markdown
    const processedText = processText(privacyContent.value)
    return marked(processedText)
  } catch (e) {
    console.error('解析隐私政策Markdown失败', e)
    return processText(privacyContent.value)
  }
})

// 获取系统信息
const fetchSystemInfo = async () => {
  loading.value = true
  try {
    const res = await SysInfoApi.getSystemInfo()
    console.log('系统信息数据:', res)
    // 直接使用data字段的数据
    systemInfo.value = res.data
  } catch (error) {
    ElMessage.error('获取系统信息失败')
    console.error(error)
  } finally {
    loading.value = false
  }
}

// 获取服务条款
const fetchTermsOfService = async () => {
  termsLoading.value = true
  try {
    const res = await SysInfoApi.getTermsOfService()
    console.log('服务条款数据:', res)
    // 注意：这里的响应可能是字符串而不是对象
    if (typeof res.data === 'string') {
      termsContent.value = res.data
    } else if (res.data && typeof res.data.data === 'string') {
      termsContent.value = res.data.data
    } else {
      termsContent.value = ''
    }
  } catch (error) {
    ElMessage.error('获取服务条款失败')
    console.error(error)
  } finally {
    termsLoading.value = false
  }
}

// 获取隐私政策
const fetchPrivacyPolicy = async () => {
  privacyLoading.value = true
  try {
    const res = await SysInfoApi.getPrivacyPolicy()
    console.log('隐私政策数据:', res)
    // 注意：这里的响应可能是字符串而不是对象
    if (typeof res.data === 'string') {
      privacyContent.value = res.data
    } else if (res.data && typeof res.data.data === 'string') {
      privacyContent.value = res.data.data
    } else {
      privacyContent.value = ''
    }
  } catch (error) {
    ElMessage.error('获取隐私政策失败')
    console.error(error)
  } finally {
    privacyLoading.value = false
  }
}

// 编辑系统信息
const handleEdit = () => {
  // 填充表单数据
  if (systemInfo.value) {
    Object.keys(aboutForm).forEach(key => {
      if (key in systemInfo.value) {
        aboutForm[key] = systemInfo.value[key];
      }
    });

    // 设置Logo类型
    logoType.value = systemInfo.value.systemLogo && systemInfo.value.systemLogo.trim() !== ''
      ? 'custom'
      : 'default';
  } else {
    // 如果没有系统信息，默认使用默认Logo
    logoType.value = 'default';
  }

  // 填充服务条款和隐私政策
  aboutForm.termsOfService = termsContent.value;
  aboutForm.privacyPolicy = privacyContent.value;

  dialogVisible.value = true;
}

// 提交表单
const submitForm = async () => {
  if (!aboutFormRef.value) return

  await aboutFormRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        // 如果使用默认Logo，清空systemLogo字段，这样系统会使用默认的/assets/images/logo.png
        if (logoType.value === 'default') {
          aboutForm.systemLogo = ''
        }

        // 处理服务条款和隐私政策文本
        aboutForm.termsOfService = processText(aboutForm.termsOfService)
        aboutForm.privacyPolicy = processText(aboutForm.privacyPolicy)

        const { data } = await SysInfoApi.updateSystemInfo(aboutForm)

        if (data.code === 200) {
          ElMessage.success('保存成功')
          dialogVisible.value = false

          // 刷新数据
          await fetchSystemInfo()
          await fetchTermsOfService()
          await fetchPrivacyPolicy()
        } else {
          ElMessage.error(data.message || '保存失败')
        }
      } catch (error) {
        ElMessage.error('保存失败')
        console.error(error)
      } finally {
        submitLoading.value = false
      }
    }
  })
}

// 生命周期钩子
onMounted(async () => {
  await fetchSystemInfo()
  await fetchTermsOfService()
  await fetchPrivacyPolicy()
})

// 暴露给父组件的方法，用于在标签切换时刷新数据
const fetchData = async () => {
  await fetchSystemInfo()
  await fetchTermsOfService()
  await fetchPrivacyPolicy()
}

// 将方法暴露给父组件
defineExpose({
  fetchData,
  fetchSystemInfo,
  fetchTermsOfService,
  fetchPrivacyPolicy
})
</script>

<style scoped>
.about-container {
  padding: 10px;
}

.mb-4 {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.about-detail {
  padding: 10px;
}

.system-header {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 20px;
}

.logo-container {
  margin-right: 20px;
}

.system-logo {
  max-width: 100px;
  max-height: 100px;
  object-fit: contain;
}

.system-title h2 {
  margin: 0;
  color: #303133;
  font-size: 24px;
  font-weight: 600;
}

.description-section {
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #EBEEF5;
}

.section-title {
  font-weight: bold;
  margin-bottom: 10px;
  font-size: 16px;
  color: #409EFF;
}

.section-content {
  line-height: 1.8;
  color: #606266;
  font-size: 14px;
}

.content-box {
  padding: 25px;
  background-color: #f8f8f8;
  border-radius: 8px;
  line-height: 1.8;
  max-height: 550px;
  overflow-y: auto;
  box-shadow: inset 0 0 5px rgba(0,0,0,0.05);
  font-size: 14px;
}

.center-content {
  display: flex;
  justify-content: center;
  padding: 20px 0;
}

.empty-data {
  padding: 20px 0;
}

.form-tabs {
  margin-top: 20px;
}

:deep(.form-tabs .el-tabs__header) {
  margin-bottom: 15px;
}

:deep(.content-box h1),
:deep(.content-box h2) {
  margin-top: 24px;
  margin-bottom: 16px;
  color: #303133;
  font-weight: 600;
  border-bottom: 1px solid #eaecef;
  padding-bottom: 8px;
}

:deep(.content-box h3) {
  margin-top: 20px;
  margin-bottom: 12px;
  color: #303133;
  font-weight: 600;
}

:deep(.content-box p) {
  margin-bottom: 16px;
  line-height: 1.8;
  white-space: pre-line; /* 保留换行符 */
}

:deep(.content-box ul),
:deep(.content-box ol) {
  padding-left: 24px;
  margin-bottom: 16px;
  line-height: 1.8;
}

:deep(.content-box li) {
  margin-bottom: 8px;
}

:deep(.content-box a) {
  color: #409EFF;
  text-decoration: none;
  font-weight: 500;
}

:deep(.content-box a:hover) {
  text-decoration: underline;
}

:deep(.content-box blockquote) {
  border-left: 4px solid #dfe2e5;
  padding: 0 16px;
  color: #6a737d;
  margin: 16px 0;
}

:deep(.content-box code) {
  background-color: rgba(27,31,35,0.05);
  border-radius: 3px;
  padding: 0.2em 0.4em;
  font-family: SFMono-Regular, Consolas, Liberation Mono, Menlo, monospace;
}

/* Logo选择器样式 */
.logo-selector {
  margin-bottom: 15px;
}

.default-logo-preview {
  display: flex;
  align-items: center;
  margin-top: 10px;
}

.logo-preview {
  width: 80px;
  height: 80px;
  object-fit: contain;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  padding: 5px;
  margin-right: 15px;
}

.logo-hint {
  color: #606266;
  font-size: 14px;
}
</style>