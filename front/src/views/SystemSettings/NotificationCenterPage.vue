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
                    <el-radio-button label="EMAIL">邮件模板</el-radio-button>
                    <el-radio-button label="SMS">短信模板</el-radio-button>
                    <el-radio-button label="SYSTEM">系统通知</el-radio-button>
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
                      <el-form-item label="模板标题" v-if="activeTemplateType !== 'SYSTEM'">
                        <el-input v-model="currentTemplate.title"></el-input>
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
                <el-button type="primary" size="small" @click="handleAddUserSubscription">添加用户订阅</el-button>
              </div>
            </template>
            <el-table :data="userSubscriptions" style="width: 100%" v-loading="loading">
              <el-table-column prop="username" label="用户名" />
              <el-table-column prop="email" label="邮箱" />
              <el-table-column prop="ruleName" label="订阅规则" />
              <el-table-column prop="ruleType" label="规则类型">
                <template #default="scope">
                  <el-tag
                    :type="scope.row.ruleType === 'TEST_PLAN' ? 'success' : 
                          scope.row.ruleType === 'BUG' ? 'danger' : 'warning'"
                  >
                    {{ scope.row.ruleType === 'TEST_PLAN' ? '测试计划' : 
                       scope.row.ruleType === 'BUG' ? '缺陷' : '报告' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="通知方式">
                <template #default="scope">
                  <el-tag v-for="type in scope.row.notifyType" :key="type" class="mx-1">
                    {{ type === 'email' ? '邮件' : 
                       type === 'message' ? '站内信' : '系统提醒' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="status" label="状态" width="100">
                <template #default="scope">
                  <el-switch
                    v-model="scope.row.status"
                    @change="() => handleToggleSubscriptionStatus(scope.row)"
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
              <el-table-column prop="deptName" label="团队名称" />
              <el-table-column prop="description" label="描述" />
              <el-table-column prop="ruleName" label="订阅规则" />
              <el-table-column prop="ruleType" label="规则类型">
                <template #default="scope">
                  <el-tag
                    :type="scope.row.ruleType === 'TEST_PLAN' ? 'success' : 
                          scope.row.ruleType === 'BUG' ? 'danger' : 'warning'"
                  >
                    {{ scope.row.ruleType === 'TEST_PLAN' ? '测试计划' : 
                      scope.row.ruleType === 'BUG' ? '缺陷' : '报告' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="通知方式">
                <template #default="scope">
                  <el-tag v-for="type in scope.row.notifyType" :key="type" class="mx-1">
                    {{ type === 'email' ? '邮件' : 
                       type === 'message' ? '站内信' : '系统提醒' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="status" label="状态" width="100">
                <template #default="scope">
                  <el-switch
                    v-model="scope.row.status"
                    @change="() => handleToggleTeamSubscriptionStatus(scope.row)"
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

  <!-- 通知规则编辑对话框 -->
  <el-dialog
    :title="ruleForm.id ? '编辑通知规则' : '新增通知规则'"
    v-model="ruleDialogVisible"
    width="600px"
  >
    <el-form :model="ruleForm" label-width="100px" label-position="right">
      <el-form-item label="规则名称">
        <el-input v-model="ruleForm.name" placeholder="请输入规则名称"></el-input>
      </el-form-item>
      <el-form-item label="规则编码">
        <el-input v-model="ruleForm.code" placeholder="请输入规则编码"></el-input>
      </el-form-item>
      <el-form-item label="事件类型">
        <el-select v-model="ruleForm.eventType" placeholder="请选择事件类型">
          <el-option label="测试计划" value="TEST_PLAN"></el-option>
          <el-option label="缺陷" value="BUG"></el-option>
          <el-option label="报告" value="REPORT"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="事件动作">
        <el-select v-model="ruleForm.eventAction" placeholder="请选择事件动作">
          <el-option label="创建" value="CREATE"></el-option>
          <el-option label="更新" value="UPDATE"></el-option>
          <el-option label="删除" value="DELETE"></el-option>
          <el-option label="执行" value="EXECUTE"></el-option>
          <el-option label="完成" value="COMPLETE"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="通知模板">
        <el-select v-model="ruleForm.templateId" placeholder="请选择通知模板">
          <el-option 
            v-for="template in templates" 
            :key="template.id" 
            :label="template.name" 
            :value="template.id"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="通知方式">
        <el-checkbox-group v-model="ruleForm.notifyType">
          <el-checkbox label="email">邮件通知</el-checkbox>
          <el-checkbox label="message">站内信</el-checkbox>
          <el-checkbox label="alert">系统提醒</el-checkbox>
        </el-checkbox-group>
      </el-form-item>
      <el-form-item label="状态">
        <el-switch v-model="ruleForm.status"></el-switch>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="ruleDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveRule">确定</el-button>
      </span>
    </template>
  </el-dialog>

  <!-- 用户订阅编辑对话框 -->
  <el-dialog
    :title="subscriptionForm.id ? '编辑用户订阅' : '添加用户订阅'"
    v-model="subscriptionDialogVisible"
    width="500px"
  >
    <el-form :model="subscriptionForm" label-width="120px" label-position="right">
      <el-form-item label="选择用户" v-if="!subscriptionForm.id">
        <el-select 
          v-model="subscriptionForm.userId" 
          filterable 
          placeholder="请选择用户"
          @change="handleUserSelect"
        >
          <el-option
            v-for="user in availableUsers"
            :key="user.id"
            :label="user.username"
            :value="user.id"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="用户名">
        <el-input v-model="subscriptionForm.username" disabled></el-input>
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="subscriptionForm.email" disabled></el-input>
      </el-form-item>
      <el-form-item label="选择通知规则" required>
        <el-select 
          v-model="subscriptionForm.ruleId" 
          filterable 
          placeholder="请选择通知规则"
        >
          <el-option-group label="测试计划规则">
            <el-option
              v-for="rule in testPlanRules"
              :key="rule.id"
              :label="rule.name"
              :value="rule.id"
            ></el-option>
          </el-option-group>
          <el-option-group label="缺陷规则">
            <el-option
              v-for="rule in bugRules"
              :key="rule.id"
              :label="rule.name"
              :value="rule.id"
            ></el-option>
          </el-option-group>
          <el-option-group label="报告规则">
            <el-option
              v-for="rule in reportRules"
              :key="rule.id"
              :label="rule.name"
              :value="rule.id"
            ></el-option>
          </el-option-group>
        </el-select>
      </el-form-item>
      <el-form-item label="通知方式" required>
        <el-checkbox-group v-model="subscriptionForm.notifyType">
          <el-checkbox label="email">邮件通知</el-checkbox>
          <el-checkbox label="message">站内信</el-checkbox>
          <el-checkbox label="alert">系统提醒</el-checkbox>
        </el-checkbox-group>
      </el-form-item>
      <el-form-item label="订阅状态">
        <el-switch v-model="subscriptionForm.status" active-text="启用" inactive-text="禁用"></el-switch>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="subscriptionDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveSubscription">确定</el-button>
      </span>
    </template>
  </el-dialog>

  <!-- 团队订阅编辑对话框 -->
  <el-dialog
    :title="teamSubscriptionForm.id ? '编辑团队订阅' : '新增团队订阅'"
    v-model="teamSubscriptionDialogVisible"
    width="500px"
  >
    <el-form :model="teamSubscriptionForm" label-width="120px" label-position="right">
      <el-form-item label="团队名称">
        <el-input v-model="teamSubscriptionForm.deptName" placeholder="请输入团队名称"></el-input>
      </el-form-item>
      <el-form-item label="描述">
        <el-input 
          v-model="teamSubscriptionForm.description" 
          type="textarea" 
          placeholder="请输入团队描述"
        ></el-input>
      </el-form-item>
      <el-form-item label="选择通知规则" required>
        <el-select 
          v-model="teamSubscriptionForm.ruleId" 
          filterable 
          placeholder="请选择通知规则"
        >
          <el-option-group label="测试计划规则">
            <el-option
              v-for="rule in testPlanRules"
              :key="rule.id"
              :label="rule.name"
              :value="rule.id"
            ></el-option>
          </el-option-group>
          <el-option-group label="缺陷规则">
            <el-option
              v-for="rule in bugRules"
              :key="rule.id"
              :label="rule.name"
              :value="rule.id"
            ></el-option>
          </el-option-group>
          <el-option-group label="报告规则">
            <el-option
              v-for="rule in reportRules"
              :key="rule.id"
              :label="rule.name"
              :value="rule.id"
            ></el-option>
          </el-option-group>
        </el-select>
      </el-form-item>
      <el-form-item label="通知方式" required>
        <el-checkbox-group v-model="teamSubscriptionForm.notifyType">
          <el-checkbox label="email">邮件通知</el-checkbox>
          <el-checkbox label="message">站内信</el-checkbox>
          <el-checkbox label="alert">系统提醒</el-checkbox>
        </el-checkbox-group>
      </el-form-item>
      <el-form-item label="订阅状态">
        <el-switch v-model="teamSubscriptionForm.status" active-text="启用" inactive-text="禁用"></el-switch>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="teamSubscriptionDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveTeamSubscription">确定</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { NotificationTemplateApi, NotificationRuleApi, UserSubscriptionApi, DeptSubscriptionApi, UserApi } from '@/api/sysNotificationService';
import { ElMessage, ElMessageBox } from 'element-plus';

// 选项卡状态
const activeTab = ref('templates');
const activeRuleTab = ref('testPlan');
const loading = ref(false);

// 通知模板管理
const activeTemplateType = ref('EMAIL');
const activeTemplateId = ref('');
const currentTemplate = ref(null);

// 模板数据
const templates = ref([]);

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
const testPlanRules = ref([]);
const bugRules = ref([]);
const reportRules = ref([]);

// 订阅管理数据
const userSubscriptions = ref([]);
const teamSubscriptions = ref([]);

// 对话框显示控制
const ruleDialogVisible = ref(false);
const subscriptionDialogVisible = ref(false);
const teamSubscriptionDialogVisible = ref(false);

// 编辑表单数据
const ruleForm = ref({
  id: null,
  name: '',
  code: '',
  eventType: '',
  eventAction: '',
  templateId: null,
  notifyType: [],
  status: true
});

const subscriptionForm = ref({
  id: null,
  userId: null,
  username: '',
  email: '',
  ruleId: null,
  notifyType: [],
  status: true
});

const teamSubscriptionForm = ref({
  id: null,
  deptId: null,
  deptName: '',
  description: '',
  ruleId: null,
  notifyType: ['email'],
  status: true
});

// 可选用户列表
const availableUsers = ref([]);

// 计算属性
const filteredTemplates = computed(() => {
  return templates.value.filter(template => template.type === activeTemplateType.value);
});

// 加载模板数据
const loadTemplateData = async () => {
  loading.value = true;
  try {
    const response = await NotificationTemplateApi.getTemplateList({
      page: 1,
      size: 100
    });
    
    if (response.code === 200) {
      templates.value = response.data.records || [];
    } else {
      ElMessage.error(response.message || '获取模板列表失败');
    }
  } catch (error) {
    console.error('获取模板列表失败:', error);
    ElMessage.error('获取模板列表失败');
  } finally {
    loading.value = false;
  }
};

// 加载通知规则数据
const loadRuleData = async () => {
  loading.value = true;
  try {
    // 加载测试计划通知规则
    const testPlanResponse = await NotificationRuleApi.getRuleList({
      page: 1,
      size: 100,
      eventType: 'TEST_PLAN'
    });
    if (testPlanResponse.code === 200) {
      testPlanRules.value = (testPlanResponse.data.records || []).map(rule => ({
        ...rule,
        notifyType: rule.notifyType ? rule.notifyType.split(',') : []
      }));
    }

    // 加载缺陷通知规则
    const bugResponse = await NotificationRuleApi.getRuleList({
      page: 1,
      size: 100,
      eventType: 'BUG'
    });
    if (bugResponse.code === 200) {
      bugRules.value = (bugResponse.data.records || []).map(rule => ({
        ...rule,
        notifyType: rule.notifyType ? rule.notifyType.split(',') : []
      }));
    }

    // 加载报告通知规则
    const reportResponse = await NotificationRuleApi.getRuleList({
      page: 1,
      size: 100,
      eventType: 'REPORT'
    });
    if (reportResponse.code === 200) {
      reportRules.value = (reportResponse.data.records || []).map(rule => ({
        ...rule,
        notifyType: rule.notifyType ? rule.notifyType.split(',') : []
      }));
    }
  } catch (error) {
    console.error('获取通知规则失败:', error);
    ElMessage.error('获取通知规则失败');
  } finally {
    loading.value = false;
  }
};

// 加载用户订阅数据
const loadUserSubscriptionData = async () => {
  loading.value = true;
  try {
    const response = await UserSubscriptionApi.getUserSubscriptionList({
      page: 1,
      size: 100
    });
    if (response.code === 200) {
      // 将获取的订阅数据转换为前端所需格式
      userSubscriptions.value = (response.data.records || []).map(item => ({
        ...item,
        notifyType: item.notifyType ? item.notifyType.split(',') : []
      }));
    } else {
      ElMessage.error(response.message || '获取用户订阅列表失败');
    }
  } catch (error) {
    console.error('获取用户订阅列表失败:', error);
    ElMessage.error('获取用户订阅列表失败');
  } finally {
    loading.value = false;
  }
};

// 加载部门订阅数据
const loadDeptSubscriptionData = async () => {
  loading.value = true;
  try {
    const response = await DeptSubscriptionApi.getDeptSubscriptionList({
      page: 1,
      size: 100
    });
    if (response.code === 200) {
      // 将获取的订阅数据转换为前端所需格式
      teamSubscriptions.value = (response.data.records || []).map(item => ({
        ...item,
        notifyType: item.notifyType ? item.notifyType.split(',') : []
      }));
    } else {
      ElMessage.error(response.message || '获取部门订阅列表失败');
    }
  } catch (error) {
    console.error('获取部门订阅列表失败:', error);
    ElMessage.error('获取部门订阅列表失败');
  } finally {
    loading.value = false;
  }
};

// 加载可选用户列表
const loadAvailableUsers = async () => {
  try {
    const response = await UserApi.getUserList({
      page: 1,
      size: 1000
    });
    if (response.code === 200) {
      availableUsers.value = response.data.records || [];
    }
  } catch (error) {
    console.error('获取用户列表失败:', error);
  }
};

// 模板管理方法
const handleTemplateTypeChange = (type) => {
  activeTemplateType.value = type;
  activeTemplateId.value = '';
  currentTemplate.value = null;
};

const handleTemplateSelect = async (id) => {
  activeTemplateId.value = id;
  loading.value = true;
  
  try {
    const response = await NotificationTemplateApi.getTemplate(parseInt(id));
    if (response.code === 200) {
      currentTemplate.value = response.data;
    } else {
      ElMessage.error(response.message || '获取模板详情失败');
    }
  } catch (error) {
    console.error('获取模板详情失败:', error);
    ElMessage.error('获取模板详情失败');
  } finally {
    loading.value = false;
  }
};

const handleAddTemplate = () => {
  // 创建新模板
  currentTemplate.value = {
    id: null,
    name: '新模板',
    code: `${activeTemplateType.value}_TEMPLATE_${new Date().getTime()}`.toUpperCase(),
    type: activeTemplateType.value,
    title: activeTemplateType.value !== 'SYSTEM' ? '新主题' : '',
    content: '',
    params: JSON.stringify({}),
    status: true
  };
  
  activeTemplateId.value = '';
};

const handleSaveTemplate = async () => {
  if (!currentTemplate.value) return;
  
  loading.value = true;
  try {
    let response;
    if (currentTemplate.value.id) {
      // 更新模板
      response = await NotificationTemplateApi.updateTemplate(currentTemplate.value);
    } else {
      // 创建模板
      response = await NotificationTemplateApi.createTemplate(currentTemplate.value);
    }
    
    if (response.code === 200) {
      ElMessage.success(currentTemplate.value.id ? '模板更新成功' : '模板创建成功');
      await loadTemplateData();
      if (!currentTemplate.value.id) {
        // 如果是新创建的模板，设置ID
        currentTemplate.value.id = response.data;
        activeTemplateId.value = response.data.toString();
      }
    } else {
      ElMessage.error(response.message || (currentTemplate.value.id ? '更新模板失败' : '创建模板失败'));
  }
  } catch (error) {
    console.error(currentTemplate.value.id ? '更新模板失败:' : '创建模板失败:', error);
    ElMessage.error(currentTemplate.value.id ? '更新模板失败' : '创建模板失败');
  } finally {
    loading.value = false;
  }
};

const handlePreviewTemplate = () => {
  if (!currentTemplate.value) return;
  
  ElMessageBox.alert(
    `<strong>标题:</strong> ${currentTemplate.value.title}<br><br>
    <strong>内容:</strong><br> ${currentTemplate.value.content.replace(/\n/g, '<br>')}`,
    '模板预览',
    {
      dangerouslyUseHTMLString: true,
      confirmButtonText: '关闭',
      callback: () => {}
    }
  );
};

const handleDeleteTemplate = (id) => {
  ElMessageBox.confirm('确定要删除此模板吗？删除后无法恢复。', '删除确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    loading.value = true;
    try {
      const response = await NotificationTemplateApi.deleteTemplate(id);
      if (response.code === 200) {
        ElMessage.success('模板删除成功');
        await loadTemplateData();
        if (activeTemplateId.value === id.toString()) {
          activeTemplateId.value = '';
          currentTemplate.value = null;
        }
      } else {
        ElMessage.error(response.message || '删除模板失败');
      }
    } catch (error) {
      console.error('删除模板失败:', error);
      ElMessage.error('删除模板失败');
    } finally {
      loading.value = false;
    }
  }).catch(() => {});
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
  ruleForm.value = {
    id: null,
    name: '',
    code: '',
    eventType: activeRuleTab.value.toUpperCase(),
    eventAction: '',
    templateId: null,
    notifyType: ['email'],
    status: true
  };
  ruleDialogVisible.value = true;
};

const handleEditRule = (rule) => {
  ruleForm.value = {
    ...rule,
    notifyType: rule.notifyType || []
  };
  ruleDialogVisible.value = true;
};

const handleToggleRuleStatus = async (rule) => {
  try {
    const response = await NotificationRuleApi.updateRuleStatus(rule.id, rule.enabled);
    if (response.code === 200) {
      ElMessage.success('规则状态更新成功');
    } else {
      rule.enabled = !rule.enabled; // 恢复状态
      ElMessage.error(response.message || '更新规则状态失败');
    }
  } catch (error) {
    rule.enabled = !rule.enabled; // 恢复状态
    console.error('更新规则状态失败:', error);
    ElMessage.error('更新规则状态失败');
  }
};

const handleDeleteRule = (rule) => {
  ElMessageBox.confirm('确定要删除此通知规则吗？删除后无法恢复。', '删除确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    loading.value = true;
    try {
      const response = await NotificationRuleApi.deleteRule(rule.id);
      if (response.code === 200) {
        ElMessage.success('规则删除成功');
        loadRuleData(); // 重新加载规则数据
      } else {
        ElMessage.error(response.message || '删除规则失败');
      }
    } catch (error) {
      console.error('删除规则失败:', error);
      ElMessage.error('删除规则失败');
    } finally {
      loading.value = false;
    }
  }).catch(() => {});
};

const handleSaveRule = async () => {
  loading.value = true;
  try {
    // 转换通知类型数组为逗号分隔的字符串
    const ruleData = {
      ...ruleForm.value,
      notifyType: ruleForm.value.notifyType.join(',')
    };

    let response;
    if (ruleForm.value.id) {
      response = await NotificationRuleApi.updateRule(ruleData);
    } else {
      response = await NotificationRuleApi.createRule(ruleData);
    }
    
    if (response.code === 200) {
      ElMessage.success(ruleForm.value.id ? '规则更新成功' : '规则创建成功');
      ruleDialogVisible.value = false;
      loadRuleData(); // 重新加载规则数据
    } else {
      ElMessage.error(response.message || (ruleForm.value.id ? '更新规则失败' : '创建规则失败'));
    }
  } catch (error) {
    console.error(ruleForm.value.id ? '更新规则失败:' : '创建规则失败:', error);
    ElMessage.error(ruleForm.value.id ? '更新规则失败' : '创建规则失败');
  } finally {
    loading.value = false;
  }
};

// 订阅管理方法
const handleToggleSubscriptionStatus = async (subscription) => {
  try {
    const response = await UserSubscriptionApi.updateUserSubscriptionStatus(
      subscription.id, 
      subscription.status
    );
    if (response.code === 200) {
      ElMessage.success('订阅状态更新成功');
    } else {
      subscription.status = !subscription.status; // 恢复状态
      ElMessage.error(response.message || '更新订阅状态失败');
    }
  } catch (error) {
    subscription.status = !subscription.status; // 恢复状态
    console.error('更新订阅状态失败:', error);
    ElMessage.error('更新订阅状态失败');
  }
};

const handleEditSubscription = (user) => {
  subscriptionForm.value = {
    id: user.id,
    userId: user.userId,
    username: user.username,
    email: user.email,
    ruleId: user.ruleId,
    notifyType: user.notifyType ? user.notifyType.split(',') : ['email'],
    status: user.status
  };
  subscriptionDialogVisible.value = true;
};

const handleSaveSubscription = async () => {
  if (!subscriptionForm.value.userId && !subscriptionForm.value.id) {
    ElMessage.warning('请选择用户');
    return;
  }
  
  if (!subscriptionForm.value.ruleId) {
    ElMessage.warning('请选择通知规则');
    return;
  }
  
  if (subscriptionForm.value.notifyType.length === 0) {
    ElMessage.warning('请选择至少一种通知方式');
    return;
  }

  // 将通知方式数组转换为逗号分隔的字符串
  const subscriptionData = {
    ...subscriptionForm.value,
    notifyType: subscriptionForm.value.notifyType.join(',')
  };

  loading.value = true;
  try {
    let response;
    if (subscriptionForm.value.id) {
      response = await UserSubscriptionApi.updateUserSubscription(subscriptionData);
    } else {
      response = await UserSubscriptionApi.createUserSubscription(subscriptionData);
    }
    
    if (response.code === 200) {
      ElMessage.success(subscriptionForm.value.id ? '用户订阅更新成功' : '用户订阅创建成功');
      subscriptionDialogVisible.value = false;
      loadUserSubscriptionData(); // 重新加载用户订阅数据
    } else {
      ElMessage.error(response.message || (subscriptionForm.value.id ? '更新用户订阅失败' : '创建用户订阅失败'));
    }
  } catch (error) {
    console.error(subscriptionForm.value.id ? '更新用户订阅失败:' : '创建用户订阅失败:', error);
    ElMessage.error(subscriptionForm.value.id ? '更新用户订阅失败' : '创建用户订阅失败');
  } finally {
    loading.value = false;
  }
};

const handleAddTeamSubscription = () => {
  teamSubscriptionForm.value = {
    id: null,
    deptId: null,
    deptName: '',
    description: '',
    ruleId: null,
    notifyType: ['email'],
    status: true
  };
  teamSubscriptionDialogVisible.value = true;
};

const handleEditTeamSubscription = (team) => {
  teamSubscriptionForm.value = {
    id: team.id,
    deptId: team.deptId,
    deptName: team.deptName,
    description: team.description,
    ruleId: team.ruleId,
    notifyType: team.notifyType ? team.notifyType.split(',') : ['email'],
    status: team.status
  };
  teamSubscriptionDialogVisible.value = true;
};

const handleSaveTeamSubscription = async () => {
  if (!teamSubscriptionForm.value.deptName) {
    ElMessage.warning('请输入团队名称');
    return;
  }
  
  if (!teamSubscriptionForm.value.ruleId) {
    ElMessage.warning('请选择通知规则');
    return;
  }
  
  if (teamSubscriptionForm.value.notifyType.length === 0) {
    ElMessage.warning('请选择至少一种通知方式');
    return;
  }

  // 将通知方式数组转换为逗号分隔的字符串
  const teamSubscriptionData = {
    ...teamSubscriptionForm.value,
    notifyType: teamSubscriptionForm.value.notifyType.join(',')
  };

  loading.value = true;
  try {
    let response;
    if (teamSubscriptionForm.value.id) {
      response = await DeptSubscriptionApi.updateDeptSubscription(teamSubscriptionData);
    } else {
      response = await DeptSubscriptionApi.createDeptSubscription(teamSubscriptionData);
    }
    
    if (response.code === 200) {
      ElMessage.success(teamSubscriptionForm.value.id ? '团队订阅更新成功' : '团队订阅创建成功');
      teamSubscriptionDialogVisible.value = false;
      loadDeptSubscriptionData(); // 重新加载部门订阅数据
    } else {
      ElMessage.error(response.message || (teamSubscriptionForm.value.id ? '更新团队订阅失败' : '创建团队订阅失败'));
    }
  } catch (error) {
    console.error(teamSubscriptionForm.value.id ? '更新团队订阅失败:' : '创建团队订阅失败:', error);
    ElMessage.error(teamSubscriptionForm.value.id ? '更新团队订阅失败' : '创建团队订阅失败');
  } finally {
    loading.value = false;
  }
};

const handleDeleteTeamSubscription = (team) => {
  ElMessageBox.confirm('确定要删除此团队订阅吗？删除后无法恢复。', '删除确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    loading.value = true;
    try {
      const response = await DeptSubscriptionApi.deleteDeptSubscription(team.id);
      if (response.code === 200) {
        ElMessage.success('团队订阅删除成功');
        loadDeptSubscriptionData(); // 重新加载部门订阅数据
      } else {
        ElMessage.error(response.message || '删除团队订阅失败');
      }
    } catch (error) {
      console.error('删除团队订阅失败:', error);
      ElMessage.error('删除团队订阅失败');
    } finally {
      loading.value = false;
    }
  }).catch(() => {});
};

// 添加用户订阅
const handleAddUserSubscription = () => {
  subscriptionForm.value = {
    id: null,
    userId: null,
    username: '',
    email: '',
    ruleId: null,
    notifyType: ['email'],
    status: true
  };
  loadAvailableUsers(); // 加载可选用户列表
  subscriptionDialogVisible.value = true;
};

// 用户选择事件处理
const handleUserSelect = (userId) => {
  const selectedUser = availableUsers.value.find(user => user.id === userId);
  if (selectedUser) {
    subscriptionForm.value.username = selectedUser.username;
    subscriptionForm.value.email = selectedUser.email;
  }
};

// 修改团队订阅状态
const handleToggleTeamSubscriptionStatus = async (subscription) => {
  try {
    const response = await DeptSubscriptionApi.updateDeptSubscriptionStatus(
      subscription.id, 
      subscription.status
    );
    if (response.code === 200) {
      ElMessage.success('团队订阅状态更新成功');
    } else {
      subscription.status = !subscription.status; // 恢复状态
      ElMessage.error(response.message || '更新团队订阅状态失败');
    }
  } catch (error) {
    subscription.status = !subscription.status; // 恢复状态
    console.error('更新团队订阅状态失败:', error);
    ElMessage.error('更新团队订阅状态失败');
  }
};

onMounted(() => {
  // 初始化加载数据
  loadTemplateData();
  loadRuleData();
  loadUserSubscriptionData();
  loadDeptSubscriptionData();
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