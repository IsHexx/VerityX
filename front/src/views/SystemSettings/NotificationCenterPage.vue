<template>
  <div class="notification-center-container">
    <el-tabs v-model="activeTab" class="notification-tabs">
      <el-tab-pane label="通知模板管理" name="templates">
        <div class="tab-content">
          <el-card shadow="hover" class="notification-card">
            <template #header>
              <div class="card-header">
                <span>通知模板</span>
                <el-button type="primary" size="small" @click="handleAddTemplate">新增模板</el-button>
              </div>
            </template>
            <el-row :gutter="20">
              <el-col :span="6">
                <el-card shadow="hover" class="template-category-card">
                  <el-radio-group v-model="activeTemplateType" @change="handleTemplateTypeChange">
                    <el-radio-button label="email">邮件模板</el-radio-button>
                    <el-radio-button label="message">站内信模板</el-radio-button>
                    <el-radio-button label="alert">提醒模板</el-radio-button>
                  </el-radio-group>
                  <el-menu 
                    :default-active="activeTemplateId" 
                    @select="handleTemplateSelect" 
                    class="template-menu"
                  >
                    <el-menu-item 
                      v-for="template in filteredTemplates" 
                      :key="template.id" 
                      :index="String(template.id)"
                    >
                      {{ template.name }}
                    </el-menu-item>
                  </el-menu>
                </el-card>
              </el-col>
              <el-col :span="18">
                <el-card shadow="hover" class="template-content-card" v-loading="loading">
                  <template #header>
                    <div class="card-header">
                      <span>{{ currentTemplate ? currentTemplate.name : '请选择模板' }}</span>
                      <div v-if="currentTemplate">
                        <el-button type="primary" size="small" @click="handleSaveTemplate">保存</el-button>
                        <el-button size="small" @click="handlePreviewTemplate">预览</el-button>
                      </div>
                    </div>
                  </template>
                  <div v-if="currentTemplate" class="template-editor">
                    <el-form :model="currentTemplate" label-width="100px" label-position="left">
                      <el-form-item label="模板名称">
                        <el-input v-model="currentTemplate.name"></el-input>
                      </el-form-item>
                      <el-form-item label="模板标题" v-if="activeTemplateType !== 'alert'">
                        <el-input v-model="currentTemplate.subject"></el-input>
                      </el-form-item>
                      <el-form-item label="模板内容">
                        <el-input 
                          v-model="currentTemplate.content" 
                          type="textarea" 
                          :rows="12"
                          placeholder="编辑模板内容，可使用变量如 {userName}, {projectName}, {testCaseName} 等"
                        ></el-input>
                      </el-form-item>
                      <el-form-item label="可用变量">
                        <el-tag 
                          v-for="variable in availableVariables" 
                          :key="variable"
                          class="variable-tag"
                          @click="insertVariable(variable)"
                        >
                          {{ variable }}
                        </el-tag>
                      </el-form-item>
                    </el-form>
                  </div>
                  <div v-else class="template-placeholder">
                    <el-empty description="请从左侧选择模板或创建新模板"></el-empty>
                  </div>
                </el-card>
              </el-col>
            </el-row>
          </el-card>
        </div>
      </el-tab-pane>
      
      <el-tab-pane label="通知规则设置" name="rules">
        <div class="tab-content">
          <el-card shadow="hover" class="notification-card">
            <template #header>
              <div class="card-header">
                <span>通知规则</span>
                <el-button type="primary" size="small" @click="handleAddRule">新增规则</el-button>
              </div>
            </template>
            <el-tabs v-model="activeRuleTab" type="card">
              <el-tab-pane label="测试计划通知" name="testPlan">
                <el-table :data="testPlanRules" style="width: 100%" v-loading="loading">
                  <el-table-column prop="name" label="规则名称" />
                  <el-table-column prop="trigger" label="触发条件" />
                  <el-table-column prop="recipients" label="接收人" />
                  <el-table-column prop="channels" label="通知渠道">
                    <template #default="scope">
                      <el-tag 
                        v-for="channel in scope.row.channels" 
                        :key="channel" 
                        class="channel-tag"
                      >
                        {{ getChannelName(channel) }}
                      </el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column prop="enabled" label="状态" width="100">
                    <template #default="scope">
                      <el-switch
                        v-model="scope.row.enabled"
                        @change="handleToggleRuleStatus(scope.row)"
                      ></el-switch>
                    </template>
                  </el-table-column>
                  <el-table-column label="操作" width="150">
                    <template #default="scope">
                      <el-button type="primary" link @click="handleEditRule(scope.row)">编辑</el-button>
                      <el-button type="danger" link @click="handleDeleteRule(scope.row)">删除</el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </el-tab-pane>
              
              <el-tab-pane label="缺陷通知" name="bug">
                <el-table :data="bugRules" style="width: 100%" v-loading="loading">
                  <el-table-column prop="name" label="规则名称" />
                  <el-table-column prop="trigger" label="触发条件" />
                  <el-table-column prop="recipients" label="接收人" />
                  <el-table-column prop="channels" label="通知渠道">
                    <template #default="scope">
                      <el-tag 
                        v-for="channel in scope.row.channels" 
                        :key="channel" 
                        class="channel-tag"
                      >
                        {{ getChannelName(channel) }}
                      </el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column prop="enabled" label="状态" width="100">
                    <template #default="scope">
                      <el-switch
                        v-model="scope.row.enabled"
                        @change="handleToggleRuleStatus(scope.row)"
                      ></el-switch>
                    </template>
                  </el-table-column>
                  <el-table-column label="操作" width="150">
                    <template #default="scope">
                      <el-button type="primary" link @click="handleEditRule(scope.row)">编辑</el-button>
                      <el-button type="danger" link @click="handleDeleteRule(scope.row)">删除</el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </el-tab-pane>
              
              <el-tab-pane label="报告通知" name="report">
                <el-table :data="reportRules" style="width: 100%" v-loading="loading">
                  <el-table-column prop="name" label="规则名称" />
                  <el-table-column prop="trigger" label="触发条件" />
                  <el-table-column prop="recipients" label="接收人" />
                  <el-table-column prop="channels" label="通知渠道">
                    <template #default="scope">
                      <el-tag 
                        v-for="channel in scope.row.channels" 
                        :key="channel" 
                        class="channel-tag"
                      >
                        {{ getChannelName(channel) }}
                      </el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column prop="enabled" label="状态" width="100">
                    <template #default="scope">
                      <el-switch
                        v-model="scope.row.enabled"
                        @change="handleToggleRuleStatus(scope.row)"
                      ></el-switch>
                    </template>
                  </el-table-column>
                  <el-table-column label="操作" width="150">
                    <template #default="scope">
                      <el-button type="primary" link @click="handleEditRule(scope.row)">编辑</el-button>
                      <el-button type="danger" link @click="handleDeleteRule(scope.row)">删除</el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </el-tab-pane>
            </el-tabs>
          </el-card>
        </div>
      </el-tab-pane>
      
      <el-tab-pane label="订阅管理" name="subscriptions">
        <div class="tab-content">
          <el-card shadow="hover" class="notification-card">
            <template #header>
              <div class="card-header">
                <span>用户订阅设置</span>
              </div>
            </template>
            <el-table :data="userSubscriptions" style="width: 100%" v-loading="loading">
              <el-table-column prop="username" label="用户名" />
              <el-table-column prop="email" label="邮箱" />
              <el-table-column label="接收测试计划通知">
                <template #default="scope">
                  <el-switch
                    v-model="scope.row.subscriptions.testPlan"
                    @change="() => handleUpdateSubscription(scope.row, 'testPlan')"
                  ></el-switch>
                </template>
              </el-table-column>
              <el-table-column label="接收缺陷通知">
                <template #default="scope">
                  <el-switch
                    v-model="scope.row.subscriptions.bug"
                    @change="() => handleUpdateSubscription(scope.row, 'bug')"
                  ></el-switch>
                </template>
              </el-table-column>
              <el-table-column label="接收报告通知">
                <template #default="scope">
                  <el-switch
                    v-model="scope.row.subscriptions.report"
                    @change="() => handleUpdateSubscription(scope.row, 'report')"
                  ></el-switch>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="100">
                <template #default="scope">
                  <el-button type="primary" link @click="handleEditSubscription(scope.row)">编辑</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
          
          <el-card shadow="hover" class="notification-card">
            <template #header>
              <div class="card-header">
                <span>团队订阅设置</span>
                <el-button type="primary" size="small" @click="handleAddTeamSubscription">新增订阅</el-button>
              </div>
            </template>
            <el-table :data="teamSubscriptions" style="width: 100%" v-loading="loading">
              <el-table-column prop="teamName" label="团队名称" />
              <el-table-column prop="description" label="描述" />
              <el-table-column prop="memberCount" label="成员数量" />
              <el-table-column label="接收测试计划通知">
                <template #default="scope">
                  <el-switch
                    v-model="scope.row.subscriptions.testPlan"
                    @change="() => handleUpdateTeamSubscription(scope.row, 'testPlan')"
                  ></el-switch>
                </template>
              </el-table-column>
              <el-table-column label="接收缺陷通知">
                <template #default="scope">
                  <el-switch
                    v-model="scope.row.subscriptions.bug"
                    @change="() => handleUpdateTeamSubscription(scope.row, 'bug')"
                  ></el-switch>
                </template>
              </el-table-column>
              <el-table-column label="接收报告通知">
                <template #default="scope">
                  <el-switch
                    v-model="scope.row.subscriptions.report"
                    @change="() => handleUpdateTeamSubscription(scope.row, 'report')"
                  ></el-switch>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="180">
                <template #default="scope">
                  <el-button type="primary" link @click="handleEditTeamSubscription(scope.row)">编辑</el-button>
                  <el-button type="danger" link @click="handleDeleteTeamSubscription(scope.row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';

// 选项卡状态
const activeTab = ref('templates');
const activeRuleTab = ref('testPlan');
const loading = ref(false);

// 通知模板管理
const activeTemplateType = ref('email');
const activeTemplateId = ref('');
const currentTemplate = ref(null);

// 模板数据
const templates = ref([
  { id: 1, type: 'email', name: '测试计划创建通知', subject: '新测试计划已创建', content: '您好 {userName}，\n\n新的测试计划 {testPlanName} 已创建。\n\n详情请查看：{testPlanUrl}' },
  { id: 2, type: 'email', name: '测试报告完成通知', subject: '测试报告已生成', content: '您好 {userName}，\n\n测试计划 {testPlanName} 的测试报告已生成。\n\n详情请查看：{reportUrl}' },
  { id: 3, type: 'email', name: '缺陷创建通知', subject: '新缺陷已创建', content: '您好 {userName}，\n\n新的缺陷 {bugTitle} 已创建并分配给您。\n\n详情请查看：{bugUrl}' },
  { id: 4, type: 'message', name: '系统公告', subject: '系统公告', content: '亲爱的用户，\n\n系统将于 {maintenanceTime} 进行维护升级，预计停机时间 {duration} 小时。' },
  { id: 5, type: 'message', name: '欢迎消息', subject: '欢迎使用VerityX测试平台', content: '欢迎 {userName}，\n\n感谢您使用VerityX测试平台。如有问题请联系管理员。' },
  { id: 6, type: 'alert', name: '测试失败提醒', subject: '', content: '测试计划 {testPlanName} 执行失败，失败率 {failRate}%。' },
  { id: 7, type: 'alert', name: '系统异常提醒', subject: '', content: '系统检测到异常：{errorMessage}，请及时处理。' },
]);

// 可用变量
const availableVariables = [
  '{userName}', 
  '{testPlanName}', 
  '{testPlanUrl}', 
  '{reportUrl}', 
  '{bugTitle}', 
  '{bugUrl}', 
  '{projectName}',
  '{maintenanceTime}',
  '{duration}',
  '{failRate}',
  '{errorMessage}'
];

// 通知规则数据
const testPlanRules = ref([
  { id: 1, name: '测试计划创建通知', trigger: '测试计划创建时', recipients: '项目成员', channels: ['email', 'message'], enabled: true },
  { id: 2, name: '测试计划开始通知', trigger: '测试计划开始执行时', recipients: '测试人员', channels: ['email', 'message'], enabled: true },
  { id: 3, name: '测试计划完成通知', trigger: '测试计划执行完成时', recipients: '项目经理, 测试经理', channels: ['email', 'message', 'alert'], enabled: true },
]);

const bugRules = ref([
  { id: 4, name: '缺陷创建通知', trigger: '缺陷创建时', recipients: '开发人员, 测试经理', channels: ['email', 'message'], enabled: true },
  { id: 5, name: '缺陷状态变更通知', trigger: '缺陷状态变更时', recipients: '相关人员', channels: ['email', 'message'], enabled: true },
  { id: 6, name: '缺陷关闭通知', trigger: '缺陷关闭时', recipients: '测试人员', channels: ['email'], enabled: false },
]);

const reportRules = ref([
  { id: 7, name: '测试报告生成通知', trigger: '测试报告生成时', recipients: '项目成员', channels: ['email'], enabled: true },
  { id: 8, name: '测试报告失败率通知', trigger: '测试失败率超过阈值时', recipients: '项目经理, 测试经理', channels: ['email', 'message', 'alert'], enabled: true },
]);

// 订阅管理数据
const userSubscriptions = ref([
  { 
    id: 1, 
    username: 'admin', 
    email: 'admin@example.com', 
    subscriptions: { testPlan: true, bug: true, report: true } 
  },
  { 
    id: 2, 
    username: 'tester1', 
    email: 'tester1@example.com', 
    subscriptions: { testPlan: true, bug: true, report: false } 
  },
  { 
    id: 3, 
    username: 'dev1', 
    email: 'dev1@example.com', 
    subscriptions: { testPlan: false, bug: true, report: false } 
  },
]);

const teamSubscriptions = ref([
  { 
    id: 1, 
    teamName: '测试团队', 
    description: '负责系统测试', 
    memberCount: 5, 
    subscriptions: { testPlan: true, bug: true, report: true } 
  },
  { 
    id: 2, 
    teamName: '开发团队', 
    description: '负责系统开发', 
    memberCount: 8, 
    subscriptions: { testPlan: false, bug: true, report: false } 
  },
  { 
    id: 3, 
    teamName: '项目管理团队', 
    description: '负责项目管理', 
    memberCount: 3, 
    subscriptions: { testPlan: true, bug: false, report: true } 
  },
]);

// 计算属性
const filteredTemplates = computed(() => {
  return templates.value.filter(template => template.type === activeTemplateType.value);
});

// 模板管理方法
const handleTemplateTypeChange = (type) => {
  activeTemplateType.value = type;
  activeTemplateId.value = '';
  currentTemplate.value = null;
};

const handleTemplateSelect = (id) => {
  activeTemplateId.value = id;
  const template = templates.value.find(t => t.id === parseInt(id));
  if (template) {
    // 创建克隆以避免直接修改原始对象
    currentTemplate.value = JSON.parse(JSON.stringify(template));
  }
};

const handleAddTemplate = () => {
  // 创建新模板
  const newTemplate = {
    id: templates.value.length + 1,
    type: activeTemplateType.value,
    name: '新模板',
    subject: activeTemplateType.value !== 'alert' ? '新主题' : '',
    content: ''
  };
  
  templates.value.push(newTemplate);
  activeTemplateId.value = newTemplate.id.toString();
  currentTemplate.value = JSON.parse(JSON.stringify(newTemplate));
};

const handleSaveTemplate = () => {
  // 保存模板
  const index = templates.value.findIndex(t => t.id === currentTemplate.value.id);
  if (index !== -1) {
    templates.value[index] = JSON.parse(JSON.stringify(currentTemplate.value));
  }
  console.log('保存模板', currentTemplate.value);
};

const handlePreviewTemplate = () => {
  console.log('预览模板', currentTemplate.value);
};

const insertVariable = (variable) => {
  if (currentTemplate.value) {
    currentTemplate.value.content += variable;
  }
};

// 通知规则方法
const getChannelName = (channel) => {
  const channelMap = {
    'email': '邮件',
    'message': '站内信',
    'alert': '系统提醒'
  };
  return channelMap[channel] || channel;
};

const handleAddRule = () => {
  console.log('添加通知规则');
};

const handleEditRule = (rule) => {
  console.log('编辑通知规则', rule);
};

const handleToggleRuleStatus = (rule) => {
  console.log('切换通知规则状态', rule);
};

const handleDeleteRule = (rule) => {
  console.log('删除通知规则', rule);
};

// 订阅管理方法
const handleUpdateSubscription = (user, type) => {
  console.log('更新用户订阅', user, type);
};

const handleEditSubscription = (user) => {
  console.log('编辑用户订阅', user);
};

const handleAddTeamSubscription = () => {
  console.log('添加团队订阅');
};

const handleUpdateTeamSubscription = (team, type) => {
  console.log('更新团队订阅', team, type);
};

const handleEditTeamSubscription = (team) => {
  console.log('编辑团队订阅', team);
};

const handleDeleteTeamSubscription = (team) => {
  console.log('删除团队订阅', team);
};

onMounted(() => {
  // 初始化加载数据
});
</script>

<style scoped>
.notification-center-container {
  padding: 20px;
}

.notification-card {
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

.template-category-card {
  height: 100%;
}

.template-menu {
  margin-top: 20px;
  border-right: none;
}

.template-content-card {
  min-height: 500px;
}

.template-editor {
  padding: 20px 0;
}

.template-placeholder {
  height: 400px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.variable-tag {
  margin: 5px;
  cursor: pointer;
}

.channel-tag {
  margin-right: 5px;
}

.notification-tabs {
  min-height: 500px;
}
</style> 