<template>
  <div class="system-params-container">
    <el-tabs v-model="activeTab" class="system-tabs">
      <el-tab-pane label="全局参数设置" name="global">
        <div class="tab-content">
          <el-card shadow="hover" class="system-card">
            <template #header>
              <div class="card-header">
                <span>全局参数设置</span>
                <el-button type="primary" size="small" @click="handleSaveParams">保存设置</el-button>
              </div>
            </template>
            <el-form :model="globalParams" label-width="180px" label-position="left">
              <el-divider content-position="left">测试环境参数</el-divider>
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="默认测试环境">
                    <el-select v-model="globalParams.defaultTestEnv" placeholder="请选择默认测试环境">
                      <el-option label="开发环境" value="dev"></el-option>
                      <el-option label="测试环境" value="test"></el-option>
                      <el-option label="预发布环境" value="stage"></el-option>
                      <el-option label="生产环境" value="prod"></el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="默认请求超时时间">
                    <el-input-number v-model="globalParams.requestTimeout" :min="1" :max="60" placeholder="秒"></el-input-number>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="默认重试次数">
                    <el-input-number v-model="globalParams.retryCount" :min="0" :max="5"></el-input-number>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="测试执行并发数">
                    <el-input-number v-model="globalParams.concurrentCount" :min="1" :max="10"></el-input-number>
                  </el-form-item>
                </el-col>
              </el-row>
              
              <el-divider content-position="left">系统显示设置</el-divider>
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="默认分页大小">
                    <el-select v-model="globalParams.defaultPageSize" placeholder="请选择默认分页大小">
                      <el-option label="10 条/页" :value="10"></el-option>
                      <el-option label="20 条/页" :value="20"></el-option>
                      <el-option label="50 条/页" :value="50"></el-option>
                      <el-option label="100 条/页" :value="100"></el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="默认日期格式">
                    <el-select v-model="globalParams.dateFormat" placeholder="请选择默认日期格式">
                      <el-option label="YYYY-MM-DD" value="YYYY-MM-DD"></el-option>
                      <el-option label="YYYY/MM/DD" value="YYYY/MM/DD"></el-option>
                      <el-option label="DD/MM/YYYY" value="DD/MM/YYYY"></el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="默认主题">
                    <el-select v-model="globalParams.theme" placeholder="请选择默认主题">
                      <el-option label="浅色主题" value="light"></el-option>
                      <el-option label="深色主题" value="dark"></el-option>
                      <el-option label="跟随系统" value="system"></el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="侧边栏模式">
                    <el-radio-group v-model="globalParams.sidebarMode">
                      <el-radio label="expanded">默认展开</el-radio>
                      <el-radio label="collapsed">默认收起</el-radio>
                    </el-radio-group>
                  </el-form-item>
                </el-col>
              </el-row>
              
              <el-divider content-position="left">默认配置项</el-divider>
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="默认项目">
                    <el-select v-model="globalParams.defaultProject" placeholder="请选择默认项目">
                      <el-option label="所有项目" value="all"></el-option>
                      <el-option label="项目A" value="project-a"></el-option>
                      <el-option label="项目B" value="project-b"></el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="默认测试报告模板">
                    <el-select v-model="globalParams.defaultReportTemplate" placeholder="请选择默认测试报告模板">
                      <el-option label="标准模板" value="standard"></el-option>
                      <el-option label="简洁模板" value="simple"></el-option>
                      <el-option label="详细模板" value="detailed"></el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form>
          </el-card>
        </div>
      </el-tab-pane>
      
      <el-tab-pane label="数据字典管理" name="dictionary">
        <div class="tab-content">
          <el-row :gutter="20">
            <el-col :span="6">
              <el-card shadow="hover" class="system-card">
                <template #header>
                  <div class="card-header">
                    <span>字典类型</span>
                    <el-button type="primary" size="small" @click="handleAddDictType">新增</el-button>
                  </div>
                </template>
                <el-menu
                  :default-active="activeDictType"
                  @select="handleDictTypeSelect"
                >
                  <el-menu-item v-for="item in dictTypeList" :key="item.id" :index="item.id.toString()">
                    <span>{{ item.name }}</span>
                  </el-menu-item>
                </el-menu>
              </el-card>
            </el-col>
            <el-col :span="18">
              <el-card shadow="hover" class="system-card">
                <template #header>
                  <div class="card-header">
                    <span>{{ activeDictTypeName || '字典数据' }}</span>
                    <el-button 
                      type="primary" 
                      size="small" 
                      @click="handleAddDictData" 
                      :disabled="!activeDictType"
                    >
                      新增
                    </el-button>
                  </div>
                </template>
                <el-table :data="dictDataList" style="width: 100%" v-loading="loading">
                  <el-table-column prop="label" label="显示名称" />
                  <el-table-column prop="value" label="值" />
                  <el-table-column prop="cssClass" label="样式类名" />
                  <el-table-column prop="sort" label="排序" width="80" />
                  <el-table-column prop="status" label="状态" width="100">
                    <template #default="scope">
                      <el-tag :type="scope.row.status === '启用' ? 'success' : 'danger'">
                        {{ scope.row.status }}
                      </el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column label="操作" width="180">
                    <template #default="scope">
                      <el-button type="primary" link @click="handleEditDictData(scope.row)">编辑</el-button>
                      <el-button type="primary" link @click="handleToggleDictDataStatus(scope.row)">
                        {{ scope.row.status === '启用' ? '禁用' : '启用' }}
                      </el-button>
                      <el-button type="danger" link @click="handleDeleteDictData(scope.row)">删除</el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </el-card>
            </el-col>
          </el-row>
        </div>
      </el-tab-pane>
      
      <el-tab-pane label="邮件服务配置" name="email">
        <div class="tab-content">
          <el-card shadow="hover" class="system-card">
            <template #header>
              <div class="card-header">
                <span>SMTP服务器设置</span>
                <el-button type="primary" size="small" @click="handleSaveEmailConfig">保存设置</el-button>
              </div>
            </template>
            <el-form :model="emailConfig" label-width="120px" label-position="left">
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="SMTP服务器">
                    <el-input v-model="emailConfig.smtpServer" placeholder="例如：smtp.example.com"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="SMTP端口">
                    <el-input-number v-model="emailConfig.smtpPort" :min="1" :max="65535"></el-input-number>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="发件人邮箱">
                    <el-input v-model="emailConfig.fromEmail" placeholder="例如：system@example.com"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="发件人名称">
                    <el-input v-model="emailConfig.fromName" placeholder="例如：VerityX测试平台"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="认证用户名">
                    <el-input v-model="emailConfig.username" placeholder="SMTP认证用户名"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="认证密码">
                    <el-input v-model="emailConfig.password" type="password" placeholder="SMTP认证密码"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24">
                  <el-form-item label="加密方式">
                    <el-radio-group v-model="emailConfig.encryptionType">
                      <el-radio label="none">无</el-radio>
                      <el-radio label="ssl">SSL</el-radio>
                      <el-radio label="tls">TLS</el-radio>
                    </el-radio-group>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-divider></el-divider>
              <el-row>
                <el-col :span="24" style="display: flex; justify-content: center;">
                  <el-button type="primary" @click="handleTestEmailConnection">测试连接</el-button>
                  <el-button @click="handleSendTestEmail">发送测试邮件</el-button>
                </el-col>
              </el-row>
            </el-form>
          </el-card>
          
          <el-card shadow="hover" class="system-card">
            <template #header>
              <div class="card-header">
                <span>邮件模板管理</span>
                <el-button type="primary" size="small" @click="handleAddEmailTemplate">新增模板</el-button>
              </div>
            </template>
            <el-table :data="emailTemplateList" style="width: 100%" v-loading="loading">
              <el-table-column prop="name" label="模板名称" />
              <el-table-column prop="description" label="描述" />
              <el-table-column prop="subject" label="邮件主题" />
              <el-table-column prop="updateTime" label="更新时间" />
              <el-table-column label="操作" width="180">
                <template #default="scope">
                  <el-button type="primary" link @click="handleEditEmailTemplate(scope.row)">编辑</el-button>
                  <el-button type="primary" link @click="handleViewEmailTemplate(scope.row)">预览</el-button>
                  <el-button type="danger" link @click="handleDeleteEmailTemplate(scope.row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </div>
      </el-tab-pane>
      
      <el-tab-pane label="安全策略" name="security">
        <div class="tab-content">
          <el-card shadow="hover" class="system-card">
            <template #header>
              <div class="card-header">
                <span>密码策略</span>
                <el-button type="primary" size="small" @click="handleSaveSecurityPolicy">保存设置</el-button>
              </div>
            </template>
            <el-form :model="securityPolicy" label-width="180px" label-position="left">
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="密码最小长度">
                    <el-input-number v-model="securityPolicy.passwordMinLength" :min="6" :max="20"></el-input-number>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="密码复杂度要求">
                    <el-select v-model="securityPolicy.passwordComplexity" placeholder="请选择密码复杂度要求">
                      <el-option label="低（仅字母和数字）" value="low"></el-option>
                      <el-option label="中（必须包含字母和数字）" value="medium"></el-option>
                      <el-option label="高（必须包含大小写字母、数字和特殊字符）" value="high"></el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="密码过期时间">
                    <el-input-number v-model="securityPolicy.passwordExpiryDays" :min="0" :max="365" placeholder="天"></el-input-number>
                    <span class="form-hint">0表示永不过期</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="密码历史记录">
                    <el-input-number v-model="securityPolicy.passwordHistoryCount" :min="0" :max="10"></el-input-number>
                    <span class="form-hint">禁止重复使用历史密码</span>
                  </el-form-item>
                </el-col>
              </el-row>
              
              <el-divider content-position="left">登录策略</el-divider>
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="登录失败锁定阈值">
                    <el-input-number v-model="securityPolicy.loginFailLockThreshold" :min="0" :max="10"></el-input-number>
                    <span class="form-hint">0表示不锁定</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="账户锁定持续时间">
                    <el-input-number v-model="securityPolicy.accountLockDuration" :min="1" :max="1440" placeholder="分钟"></el-input-number>
                  </el-form-item>
                </el-col>
              </el-row>
              
              <el-divider content-position="left">会话设置</el-divider>
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="会话超时时间">
                    <el-input-number v-model="securityPolicy.sessionTimeout" :min="5" :max="1440" placeholder="分钟"></el-input-number>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="并发登录控制">
                    <el-select v-model="securityPolicy.concurrentLoginControl" placeholder="请选择并发登录控制方式">
                      <el-option label="允许多处登录" value="allow"></el-option>
                      <el-option label="限制单处登录" value="single"></el-option>
                      <el-option label="新登录踢出旧登录" value="kickout"></el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form>
          </el-card>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';

// 选项卡状态
const activeTab = ref('global');
const loading = ref(false);

// 全局参数数据
const globalParams = ref({
  defaultTestEnv: 'test',
  requestTimeout: 10,
  retryCount: 3,
  concurrentCount: 5,
  defaultPageSize: 20,
  dateFormat: 'YYYY-MM-DD',
  theme: 'light',
  sidebarMode: 'expanded',
  defaultProject: 'all',
  defaultReportTemplate: 'standard'
});

// 数据字典管理数据
const dictTypeList = ref([
  { id: 1, name: '缺陷类型' },
  { id: 2, name: '测试用例类型' },
  { id: 3, name: '测试计划状态' },
  { id: 4, name: '缺陷严重程度' },
  { id: 5, name: '缺陷优先级' },
]);

const activeDictType = ref('1');
const activeDictTypeName = ref('缺陷类型');

const dictDataList = ref([
  { id: 1, label: '功能缺陷', value: 'functional', cssClass: 'danger', sort: 1, status: '启用' },
  { id: 2, label: '性能问题', value: 'performance', cssClass: 'warning', sort: 2, status: '启用' },
  { id: 3, label: 'UI问题', value: 'ui', cssClass: 'info', sort: 3, status: '启用' },
  { id: 4, label: '安全问题', value: 'security', cssClass: 'danger', sort: 4, status: '启用' },
  { id: 5, label: '兼容性问题', value: 'compatibility', cssClass: 'warning', sort: 5, status: '启用' },
]);

// 邮件服务配置数据
const emailConfig = ref({
  smtpServer: 'smtp.example.com',
  smtpPort: 587,
  fromEmail: 'system@example.com',
  fromName: 'VerityX测试平台',
  username: 'system@example.com',
  password: '******',
  encryptionType: 'tls'
});

const emailTemplateList = ref([
  { id: 1, name: '测试报告通知', description: '测试执行完成后发送测试报告', subject: 'VerityX测试报告', updateTime: '2023-06-10' },
  { id: 2, name: '缺陷通知', description: '新缺陷创建或缺陷状态更新通知', subject: '缺陷状态更新', updateTime: '2023-06-12' },
  { id: 3, name: '密码重置', description: '用户密码重置邮件', subject: '密码重置', updateTime: '2023-06-15' },
]);

// 安全策略数据
const securityPolicy = ref({
  passwordMinLength: 8,
  passwordComplexity: 'medium',
  passwordExpiryDays: 90,
  passwordHistoryCount: 3,
  loginFailLockThreshold: 5,
  accountLockDuration: 30,
  sessionTimeout: 60,
  concurrentLoginControl: 'kickout'
});

// 全局参数方法
const handleSaveParams = () => {
  console.log('保存全局参数', globalParams.value);
};

// 数据字典管理方法
const handleDictTypeSelect = (index) => {
  activeDictType.value = index;
  const selectedType = dictTypeList.value.find(item => item.id.toString() === index);
  activeDictTypeName.value = selectedType ? selectedType.name : '';
  // 加载字典数据
};

const handleAddDictType = () => {
  console.log('添加字典类型');
};

const handleAddDictData = () => {
  console.log('添加字典数据');
};

const handleEditDictData = (row) => {
  console.log('编辑字典数据', row);
};

const handleToggleDictDataStatus = (row) => {
  console.log('切换字典数据状态', row);
};

const handleDeleteDictData = (row) => {
  console.log('删除字典数据', row);
};

// 邮件服务配置方法
const handleSaveEmailConfig = () => {
  console.log('保存邮件配置', emailConfig.value);
};

const handleTestEmailConnection = () => {
  console.log('测试邮件连接');
};

const handleSendTestEmail = () => {
  console.log('发送测试邮件');
};

const handleAddEmailTemplate = () => {
  console.log('添加邮件模板');
};

const handleEditEmailTemplate = (row) => {
  console.log('编辑邮件模板', row);
};

const handleViewEmailTemplate = (row) => {
  console.log('预览邮件模板', row);
};

const handleDeleteEmailTemplate = (row) => {
  console.log('删除邮件模板', row);
};

// 安全策略方法
const handleSaveSecurityPolicy = () => {
  console.log('保存安全策略', securityPolicy.value);
};

onMounted(() => {
  // 初始化加载数据
});
</script>

<style scoped>
.system-params-container {
  padding: 20px;
}

.system-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.tab-content {
  margin-top: 20px;
}

.form-hint {
  font-size: 12px;
  color: #999;
  margin-left: 8px;
}

.system-tabs {
  min-height: 500px;
}
</style> 