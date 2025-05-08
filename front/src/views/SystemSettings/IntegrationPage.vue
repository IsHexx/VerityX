<template>
  <div class="integration-container">
    <el-tabs v-model="activeTab" class="integration-tabs">
      <el-tab-pane label="第三方工具集成" name="tools">
        <div class="tab-content">
          <el-card shadow="hover" class="integration-card">
            <template #header>
              <div class="card-header">
                <span>第三方工具集成</span>
              </div>
            </template>
            
            <!-- Jira集成 -->
            <el-collapse v-model="activeTools">
              <el-collapse-item title="Jira 集成" name="jira">
                <el-form :model="jiraConfig" label-width="120px" label-position="left">
                  <el-form-item label="连接状态">
                    <el-tag :type="jiraConfig.connected ? 'success' : 'danger'">
                      {{ jiraConfig.connected ? '已连接' : '未连接' }}
                    </el-tag>
                  </el-form-item>
                  <el-form-item label="Jira URL">
                    <el-input v-model="jiraConfig.url" placeholder="例如：https://your-domain.atlassian.net"></el-input>
                  </el-form-item>
                  <el-form-item label="用户名">
                    <el-input v-model="jiraConfig.username" placeholder="Jira 用户名/邮箱"></el-input>
                  </el-form-item>
                  <el-form-item label="API Token">
                    <el-input v-model="jiraConfig.apiToken" placeholder="Jira API Token" type="password"></el-input>
                  </el-form-item>
                  <el-form-item label="默认项目">
                    <el-select v-model="jiraConfig.defaultProject" placeholder="选择默认Jira项目">
                      <el-option label="项目A" value="PROJA"></el-option>
                      <el-option label="项目B" value="PROJB"></el-option>
                    </el-select>
                  </el-form-item>
                  <el-form-item>
                    <el-button type="primary" @click="handleSaveJiraConfig">保存配置</el-button>
                    <el-button @click="handleTestJiraConnection">测试连接</el-button>
                  </el-form-item>
                </el-form>
              </el-collapse-item>
              
              <!-- GitHub/GitLab集成 -->
              <el-collapse-item title="GitHub/GitLab 集成" name="git">
                <el-form :model="gitConfig" label-width="120px" label-position="left">
                  <el-form-item label="服务类型">
                    <el-radio-group v-model="gitConfig.type">
                      <el-radio label="github">GitHub</el-radio>
                      <el-radio label="gitlab">GitLab</el-radio>
                    </el-radio-group>
                  </el-form-item>
                  <el-form-item label="连接状态">
                    <el-tag :type="gitConfig.connected ? 'success' : 'danger'">
                      {{ gitConfig.connected ? '已连接' : '未连接' }}
                    </el-tag>
                  </el-form-item>
                  <el-form-item label="服务器URL" v-if="gitConfig.type === 'gitlab'">
                    <el-input v-model="gitConfig.url" placeholder="例如：https://gitlab.example.com"></el-input>
                  </el-form-item>
                  <el-form-item label="Access Token">
                    <el-input v-model="gitConfig.token" placeholder="填写个人访问令牌" type="password"></el-input>
                  </el-form-item>
                  <el-form-item label="默认仓库">
                    <el-input v-model="gitConfig.repository" placeholder="例如：username/repository"></el-input>
                  </el-form-item>
                  <el-form-item>
                    <el-button type="primary" @click="handleSaveGitConfig">保存配置</el-button>
                    <el-button @click="handleTestGitConnection">测试连接</el-button>
                  </el-form-item>
                </el-form>
              </el-collapse-item>
              
              <!-- Jenkins集成 -->
              <el-collapse-item title="Jenkins 集成" name="jenkins">
                <el-form :model="jenkinsConfig" label-width="120px" label-position="left">
                  <el-form-item label="连接状态">
                    <el-tag :type="jenkinsConfig.connected ? 'success' : 'danger'">
                      {{ jenkinsConfig.connected ? '已连接' : '未连接' }}
                    </el-tag>
                  </el-form-item>
                  <el-form-item label="Jenkins URL">
                    <el-input v-model="jenkinsConfig.url" placeholder="例如：https://jenkins.example.com"></el-input>
                  </el-form-item>
                  <el-form-item label="用户名">
                    <el-input v-model="jenkinsConfig.username" placeholder="Jenkins 用户名"></el-input>
                  </el-form-item>
                  <el-form-item label="API Token">
                    <el-input v-model="jenkinsConfig.apiToken" placeholder="Jenkins API Token" type="password"></el-input>
                  </el-form-item>
                  <el-form-item>
                    <el-button type="primary" @click="handleSaveJenkinsConfig">保存配置</el-button>
                    <el-button @click="handleTestJenkinsConnection">测试连接</el-button>
                  </el-form-item>
                </el-form>
              </el-collapse-item>
              
              <!-- Selenium Grid配置 -->
              <el-collapse-item title="Selenium Grid 配置" name="selenium">
                <el-form :model="seleniumConfig" label-width="120px" label-position="left">
                  <el-form-item label="连接状态">
                    <el-tag :type="seleniumConfig.connected ? 'success' : 'danger'">
                      {{ seleniumConfig.connected ? '已连接' : '未连接' }}
                    </el-tag>
                  </el-form-item>
                  <el-form-item label="Selenium URL">
                    <el-input v-model="seleniumConfig.url" placeholder="例如：http://localhost:4444/wd/hub"></el-input>
                  </el-form-item>
                  <el-form-item label="最大并发数">
                    <el-input-number v-model="seleniumConfig.maxSessions" :min="1" :max="50"></el-input-number>
                  </el-form-item>
                  <el-form-item>
                    <el-button type="primary" @click="handleSaveSeleniumConfig">保存配置</el-button>
                    <el-button @click="handleTestSeleniumConnection">测试连接</el-button>
                  </el-form-item>
                </el-form>
              </el-collapse-item>
            </el-collapse>
          </el-card>
        </div>
      </el-tab-pane>
      
      <el-tab-pane label="API管理" name="api">
        <div class="tab-content">
          <el-card shadow="hover" class="integration-card">
            <template #header>
              <div class="card-header">
                <span>API密钥管理</span>
                <el-button type="primary" size="small" @click="handleGenerateApiKey">生成新密钥</el-button>
              </div>
            </template>
            <el-table :data="apiKeyList" style="width: 100%" v-loading="loading">
              <el-table-column prop="name" label="名称" />
              <el-table-column prop="key" label="API密钥">
                <template #default="scope">
                  <el-input :value="scope.row.key" :disabled="true" style="width: 300px;">
                    <template #append>
                      <el-button @click="handleCopyApiKey(scope.row.key)">复制</el-button>
                    </template>
                  </el-input>
                </template>
              </el-table-column>
              <el-table-column prop="createdAt" label="创建时间" />
              <el-table-column prop="expiresAt" label="过期时间" />
              <el-table-column prop="status" label="状态">
                <template #default="scope">
                  <el-tag :type="scope.row.status === '有效' ? 'success' : 'danger'">
                    {{ scope.row.status }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="150">
                <template #default="scope">
                  <el-button type="danger" link @click="handleRevokeApiKey(scope.row)">撤销</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
          
          <el-card shadow="hover" class="integration-card">
            <template #header>
              <div class="card-header">
                <span>回调URL配置</span>
                <el-button type="primary" size="small" @click="handleAddCallbackUrl">新增URL</el-button>
              </div>
            </template>
            <el-table :data="callbackUrlList" style="width: 100%" v-loading="loading">
              <el-table-column prop="name" label="名称" />
              <el-table-column prop="url" label="回调URL" />
              <el-table-column prop="eventType" label="事件类型" />
              <el-table-column prop="status" label="状态">
                <template #default="scope">
                  <el-switch
                    v-model="scope.row.enabled"
                    :active-value="true"
                    :inactive-value="false"
                    @change="handleToggleCallbackStatus(scope.row)"
                  ></el-switch>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="180">
                <template #default="scope">
                  <el-button type="primary" link @click="handleEditCallbackUrl(scope.row)">编辑</el-button>
                  <el-button type="primary" link @click="handleTestCallback(scope.row)">测试</el-button>
                  <el-button type="danger" link @click="handleDeleteCallbackUrl(scope.row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </div>
      </el-tab-pane>
      
      <el-tab-pane label="单点登录配置" name="sso">
        <div class="tab-content">
          <el-card shadow="hover" class="integration-card">
            <template #header>
              <div class="card-header">
                <span>LDAP配置</span>
                <el-switch
                  v-model="ldapConfig.enabled"
                  active-text="启用"
                  inactive-text="禁用"
                ></el-switch>
              </div>
            </template>
            <el-form :model="ldapConfig" label-width="120px" label-position="left">
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="LDAP服务器">
                    <el-input v-model="ldapConfig.server" placeholder="例如：ldap://ldap.example.com:389"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="绑定DN">
                    <el-input v-model="ldapConfig.bindDn" placeholder="例如：cn=admin,dc=example,dc=com"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="绑定密码">
                    <el-input v-model="ldapConfig.bindPassword" type="password"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="用户搜索基础">
                    <el-input v-model="ldapConfig.userSearchBase" placeholder="例如：ou=users,dc=example,dc=com"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="用户过滤器">
                    <el-input v-model="ldapConfig.userFilter" placeholder="例如：(uid={0})"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="用户ID属性">
                    <el-input v-model="ldapConfig.userIdAttribute" placeholder="例如：uid"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-form-item>
                <el-button type="primary" @click="handleSaveLdapConfig">保存配置</el-button>
                <el-button @click="handleTestLdapConnection">测试连接</el-button>
              </el-form-item>
            </el-form>
          </el-card>
          
          <el-card shadow="hover" class="integration-card">
            <template #header>
              <div class="card-header">
                <span>OAuth配置</span>
                <el-switch
                  v-model="oauthConfig.enabled"
                  active-text="启用"
                  inactive-text="禁用"
                ></el-switch>
              </div>
            </template>
            <el-tabs v-model="oauthActiveProvider">
              <el-tab-pane label="Google" name="google">
                <el-form :model="oauthConfig.google" label-width="120px" label-position="left">
                  <el-form-item label="客户端ID">
                    <el-input v-model="oauthConfig.google.clientId"></el-input>
                  </el-form-item>
                  <el-form-item label="客户端密钥">
                    <el-input v-model="oauthConfig.google.clientSecret" type="password"></el-input>
                  </el-form-item>
                  <el-form-item label="重定向URL">
                    <el-input v-model="oauthConfig.google.redirectUri" disabled></el-input>
                  </el-form-item>
                </el-form>
              </el-tab-pane>
              <el-tab-pane label="GitHub" name="github">
                <el-form :model="oauthConfig.github" label-width="120px" label-position="left">
                  <el-form-item label="客户端ID">
                    <el-input v-model="oauthConfig.github.clientId"></el-input>
                  </el-form-item>
                  <el-form-item label="客户端密钥">
                    <el-input v-model="oauthConfig.github.clientSecret" type="password"></el-input>
                  </el-form-item>
                  <el-form-item label="重定向URL">
                    <el-input v-model="oauthConfig.github.redirectUri" disabled></el-input>
                  </el-form-item>
                </el-form>
              </el-tab-pane>
            </el-tabs>
            <div style="margin-top: 20px; text-align: center;">
              <el-button type="primary" @click="handleSaveOauthConfig">保存配置</el-button>
            </div>
          </el-card>
          
          <el-card shadow="hover" class="integration-card">
            <template #header>
              <div class="card-header">
                <span>SAML配置</span>
                <el-switch
                  v-model="samlConfig.enabled"
                  active-text="启用"
                  inactive-text="禁用"
                ></el-switch>
              </div>
            </template>
            <el-form :model="samlConfig" label-width="120px" label-position="left">
              <el-form-item label="身份提供商">
                <el-select v-model="samlConfig.identityProvider" placeholder="选择身份提供商">
                  <el-option label="Okta" value="okta"></el-option>
                  <el-option label="Azure AD" value="azure"></el-option>
                  <el-option label="自定义" value="custom"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="元数据URL">
                <el-input v-model="samlConfig.metadataUrl" :disabled="samlConfig.identityProvider !== 'custom'"></el-input>
              </el-form-item>
              <el-form-item label="实体ID">
                <el-input v-model="samlConfig.entityId" disabled></el-input>
              </el-form-item>
              <el-form-item label="ACS URL">
                <el-input v-model="samlConfig.acsUrl" disabled></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="handleSaveSamlConfig">保存配置</el-button>
                <el-button @click="handleDownloadMetadata">下载SP元数据</el-button>
              </el-form-item>
            </el-form>
          </el-card>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import SysIntegrationApi from '@/api/sysIntegrationService';

// 选项卡状态
const activeTab = ref('tools');
const activeTools = ref(['jira']);
const oauthActiveProvider = ref('google');
const loading = ref(false);

// 分页和查询参数
const queryParams = ref({
  page: 1,
  size: 10
});

// 第三方工具集成数据
const toolIntegrations = ref([]); // 存储所有工具集成
const jiraConfig = ref({
  connected: false,
  url: '',
  username: '',
  apiToken: '',
  defaultProject: ''
});

const gitConfig = ref({
  connected: false,
  type: 'github',
  url: '',
  token: '',
  repository: ''
});

const jenkinsConfig = ref({
  connected: false,
  url: '',
  username: '',
  apiToken: ''
});

const seleniumConfig = ref({
  connected: false,
  url: 'http://localhost:4444/wd/hub',
  maxSessions: 10
});

// API管理数据
const apiKeyList = ref([]);
const callbackUrlList = ref([]);

// 单点登录配置数据
const ssoConfigs = ref([]); // 存储所有SSO配置
const ldapConfig = ref({
  enabled: false,
  server: '',
  bindDn: '',
  bindPassword: '',
  userSearchBase: '',
  userFilter: '',
  userIdAttribute: ''
});

const oauthConfig = ref({
  enabled: false,
  google: {
    clientId: '',
    clientSecret: '',
    redirectUri: window.location.origin + '/oauth/callback/google'
  },
  github: {
    clientId: '',
    clientSecret: '',
    redirectUri: window.location.origin + '/oauth/callback/github'
  }
});

const samlConfig = ref({
  enabled: false,
  identityProvider: 'okta',
  metadataUrl: '',
  entityId: window.location.origin,
  acsUrl: window.location.origin + '/saml/acs'
});

// 加载第三方工具集成数据
const loadToolIntegrations = async () => {
  loading.value = true;
  try {
    const res = await SysIntegrationApi.toolIntegration.getToolIntegrationList(queryParams.value);
    toolIntegrations.value = res.data.records || [];
    
    // 根据类型提取相应配置
    const jira = toolIntegrations.value.find(item => item.type === 'JIRA');
    if (jira) {
      jiraConfig.value = {
        id: jira.id,
        connected: jira.status,
        url: jira.url,
        username: jira.username,
        apiToken: jira.apiKey,
        defaultProject: jira.config ? JSON.parse(jira.config).defaultProject : ''
      };
    }
    
    const git = toolIntegrations.value.find(item => item.type === 'GITHUB' || item.type === 'GITLAB');
    if (git) {
      gitConfig.value = {
        id: git.id,
        connected: git.status,
        type: git.type.toLowerCase(),
        url: git.url,
        token: git.token,
        repository: git.config ? JSON.parse(git.config).repository : ''
      };
    }
    
    const jenkins = toolIntegrations.value.find(item => item.type === 'JENKINS');
    if (jenkins) {
      jenkinsConfig.value = {
        id: jenkins.id,
        connected: jenkins.status,
        url: jenkins.url,
        username: jenkins.username,
        apiToken: jenkins.apiKey
      };
    }
    
    const selenium = toolIntegrations.value.find(item => item.type === 'SELENIUM');
    if (selenium) {
      seleniumConfig.value = {
        id: selenium.id,
        connected: selenium.status,
        url: selenium.url,
        maxSessions: selenium.config ? JSON.parse(selenium.config).maxSessions : 10
      };
    }
  } catch (error) {
    console.error('加载工具集成数据失败:', error);
    ElMessage.error('加载工具集成数据失败');
  } finally {
    loading.value = false;
  }
};

// 加载API管理数据
const loadApiManagement = async () => {
  loading.value = true;
  try {
    const res = await SysIntegrationApi.apiManagement.getApiManagementList(queryParams.value);
    
    // 格式化API密钥列表数据
    apiKeyList.value = (res.data.records || []).map(item => ({
      id: item.id,
      name: item.name,
      key: item.apiKey,
      createdAt: item.createTime,
      expiresAt: item.expirationDate || '永不过期',
      status: item.status ? '有效' : '无效'
    }));
    
    // TODO: 加载回调URL配置，目前后端未提供该API
    // 暂时使用Mock数据
    callbackUrlList.value = [
      { id: 1, name: '测试报告通知', url: 'https://example.com/webhooks/testreport', eventType: '测试报告生成', enabled: true },
      { id: 2, name: '缺陷创建通知', url: 'https://example.com/webhooks/bug', eventType: '缺陷创建', enabled: false }
    ];
  } catch (error) {
    console.error('加载API管理数据失败:', error);
    ElMessage.error('加载API管理数据失败');
  } finally {
    loading.value = false;
  }
};

// 加载单点登录配置数据
const loadSsoConfig = async () => {
  loading.value = true;
  try {
    const res = await SysIntegrationApi.ssoConfig.getSsoConfigList(queryParams.value);
    ssoConfigs.value = res.data.records || [];
    
    // 提取LDAP配置
    const ldap = ssoConfigs.value.find(item => item.type === 'LDAP');
    if (ldap) {
      ldapConfig.value = {
        id: ldap.id,
        enabled: ldap.status,
        server: ldap.serverUrl,
        bindDn: ldap.config ? JSON.parse(ldap.config).bindDn : '',
        bindPassword: ldap.clientSecret,
        userSearchBase: ldap.baseDn,
        userFilter: ldap.config ? JSON.parse(ldap.config).userFilter : '',
        userIdAttribute: ldap.usernameAttribute
      };
    }
    
    // 提取OAuth配置
    const oauth = ssoConfigs.value.find(item => item.type === 'OAUTH');
    if (oauth) {
      oauthConfig.value.enabled = oauth.status;
      
      if (oauth.provider === 'Google') {
        oauthConfig.value.google = {
          clientId: oauth.clientId,
          clientSecret: oauth.clientSecret,
          redirectUri: window.location.origin + '/oauth/callback/google'
        };
      } else if (oauth.provider === 'GitHub') {
        oauthConfig.value.github = {
          clientId: oauth.clientId,
          clientSecret: oauth.clientSecret,
          redirectUri: window.location.origin + '/oauth/callback/github'
        };
      }
    }
    
    // 提取SAML配置
    const saml = ssoConfigs.value.find(item => item.type === 'SAML');
    if (saml) {
      samlConfig.value = {
        id: saml.id,
        enabled: saml.status,
        identityProvider: saml.provider ? saml.provider.toLowerCase() : 'okta',
        metadataUrl: saml.config ? JSON.parse(saml.config).metadataUrl : '',
        entityId: window.location.origin,
        acsUrl: window.location.origin + '/saml/acs'
      };
    }
  } catch (error) {
    console.error('加载单点登录配置数据失败:', error);
    ElMessage.error('加载单点登录配置数据失败');
  } finally {
    loading.value = false;
  }
};

// 第三方工具集成方法
const handleSaveJiraConfig = async () => {
  loading.value = true;
  try {
    const data = {
      name: 'JIRA集成',
      type: 'JIRA',
      url: jiraConfig.value.url,
      apiKey: jiraConfig.value.apiToken,
      username: jiraConfig.value.username,
      config: JSON.stringify({ defaultProject: jiraConfig.value.defaultProject }),
      status: jiraConfig.value.connected
    };
    
    if (jiraConfig.value.id) {
      // 更新
      data.id = jiraConfig.value.id;
      await SysIntegrationApi.toolIntegration.updateToolIntegration(data);
      ElMessage.success('更新JIRA配置成功');
    } else {
      // 创建
      const res = await SysIntegrationApi.toolIntegration.createToolIntegration(data);
      jiraConfig.value.id = res.data;
      ElMessage.success('创建JIRA配置成功');
    }
    
    // 重新加载数据
    await loadToolIntegrations();
  } catch (error) {
    console.error('保存JIRA配置失败:', error);
    ElMessage.error('保存JIRA配置失败');
  } finally {
    loading.value = false;
  }
};

const handleTestJiraConnection = async () => {
  if (!jiraConfig.value.id) {
    ElMessage.warning('请先保存配置');
    return;
  }
  
  loading.value = true;
  try {
    const res = await SysIntegrationApi.toolIntegration.testToolIntegrationConnection(jiraConfig.value.id);
    if (res.data) {
      ElMessage.success('JIRA连接测试成功');
  jiraConfig.value.connected = true;
    } else {
      ElMessage.error('JIRA连接测试失败');
      jiraConfig.value.connected = false;
    }
  } catch (error) {
    console.error('测试JIRA连接失败:', error);
    ElMessage.error('测试JIRA连接失败');
    jiraConfig.value.connected = false;
  } finally {
    loading.value = false;
  }
};

const handleSaveGitConfig = async () => {
  loading.value = true;
  try {
    const data = {
      name: gitConfig.value.type === 'github' ? 'GitHub集成' : 'GitLab集成',
      type: gitConfig.value.type.toUpperCase(),
      url: gitConfig.value.type === 'gitlab' ? gitConfig.value.url : 'https://api.github.com',
      token: gitConfig.value.token,
      config: JSON.stringify({ repository: gitConfig.value.repository }),
      status: gitConfig.value.connected
    };
    
    if (gitConfig.value.id) {
      // 更新
      data.id = gitConfig.value.id;
      await SysIntegrationApi.toolIntegration.updateToolIntegration(data);
      ElMessage.success(`更新${gitConfig.value.type === 'github' ? 'GitHub' : 'GitLab'}配置成功`);
    } else {
      // 创建
      const res = await SysIntegrationApi.toolIntegration.createToolIntegration(data);
      gitConfig.value.id = res.data;
      ElMessage.success(`创建${gitConfig.value.type === 'github' ? 'GitHub' : 'GitLab'}配置成功`);
    }
    
    // 重新加载数据
    await loadToolIntegrations();
  } catch (error) {
    console.error('保存Git配置失败:', error);
    ElMessage.error('保存Git配置失败');
  } finally {
    loading.value = false;
  }
};

const handleTestGitConnection = async () => {
  if (!gitConfig.value.id) {
    ElMessage.warning('请先保存配置');
    return;
  }
  
  loading.value = true;
  try {
    const res = await SysIntegrationApi.toolIntegration.testToolIntegrationConnection(gitConfig.value.id);
    if (res.data) {
      ElMessage.success(`${gitConfig.value.type === 'github' ? 'GitHub' : 'GitLab'}连接测试成功`);
  gitConfig.value.connected = true;
    } else {
      ElMessage.error(`${gitConfig.value.type === 'github' ? 'GitHub' : 'GitLab'}连接测试失败`);
      gitConfig.value.connected = false;
    }
  } catch (error) {
    console.error('测试Git连接失败:', error);
    ElMessage.error('测试Git连接失败');
    gitConfig.value.connected = false;
  } finally {
    loading.value = false;
  }
};

const handleSaveJenkinsConfig = async () => {
  loading.value = true;
  try {
    const data = {
      name: 'Jenkins集成',
      type: 'JENKINS',
      url: jenkinsConfig.value.url,
      username: jenkinsConfig.value.username,
      apiKey: jenkinsConfig.value.apiToken,
      status: jenkinsConfig.value.connected
    };
    
    if (jenkinsConfig.value.id) {
      // 更新
      data.id = jenkinsConfig.value.id;
      await SysIntegrationApi.toolIntegration.updateToolIntegration(data);
      ElMessage.success('更新Jenkins配置成功');
    } else {
      // 创建
      const res = await SysIntegrationApi.toolIntegration.createToolIntegration(data);
      jenkinsConfig.value.id = res.data;
      ElMessage.success('创建Jenkins配置成功');
    }
    
    // 重新加载数据
    await loadToolIntegrations();
  } catch (error) {
    console.error('保存Jenkins配置失败:', error);
    ElMessage.error('保存Jenkins配置失败');
  } finally {
    loading.value = false;
  }
};

const handleTestJenkinsConnection = async () => {
  if (!jenkinsConfig.value.id) {
    ElMessage.warning('请先保存配置');
    return;
  }
  
  loading.value = true;
  try {
    const res = await SysIntegrationApi.toolIntegration.testToolIntegrationConnection(jenkinsConfig.value.id);
    if (res.data) {
      ElMessage.success('Jenkins连接测试成功');
  jenkinsConfig.value.connected = true;
    } else {
      ElMessage.error('Jenkins连接测试失败');
      jenkinsConfig.value.connected = false;
    }
  } catch (error) {
    console.error('测试Jenkins连接失败:', error);
    ElMessage.error('测试Jenkins连接失败');
    jenkinsConfig.value.connected = false;
  } finally {
    loading.value = false;
  }
};

const handleSaveSeleniumConfig = async () => {
  loading.value = true;
  try {
    const data = {
      name: 'Selenium Grid配置',
      type: 'SELENIUM',
      url: seleniumConfig.value.url,
      config: JSON.stringify({ maxSessions: seleniumConfig.value.maxSessions }),
      status: seleniumConfig.value.connected
    };
    
    if (seleniumConfig.value.id) {
      // 更新
      data.id = seleniumConfig.value.id;
      await SysIntegrationApi.toolIntegration.updateToolIntegration(data);
      ElMessage.success('更新Selenium配置成功');
    } else {
      // 创建
      const res = await SysIntegrationApi.toolIntegration.createToolIntegration(data);
      seleniumConfig.value.id = res.data;
      ElMessage.success('创建Selenium配置成功');
    }
    
    // 重新加载数据
    await loadToolIntegrations();
  } catch (error) {
    console.error('保存Selenium配置失败:', error);
    ElMessage.error('保存Selenium配置失败');
  } finally {
    loading.value = false;
  }
};

const handleTestSeleniumConnection = async () => {
  if (!seleniumConfig.value.id) {
    ElMessage.warning('请先保存配置');
    return;
  }
  
  loading.value = true;
  try {
    const res = await SysIntegrationApi.toolIntegration.testToolIntegrationConnection(seleniumConfig.value.id);
    if (res.data) {
      ElMessage.success('Selenium连接测试成功');
  seleniumConfig.value.connected = true;
    } else {
      ElMessage.error('Selenium连接测试失败');
      seleniumConfig.value.connected = false;
    }
  } catch (error) {
    console.error('测试Selenium连接失败:', error);
    ElMessage.error('测试Selenium连接失败');
    seleniumConfig.value.connected = false;
  } finally {
    loading.value = false;
  }
};

// API管理方法
const handleGenerateApiKey = async () => {
  try {
    loading.value = true;
    
    // 弹出对话框让用户输入名称
    ElMessageBox.prompt('请输入API密钥名称', '生成新API密钥', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      inputPattern: /\S+/,
      inputErrorMessage: '名称不能为空'
    }).then(async ({ value }) => {
      // 创建API密钥
      const data = {
        name: value,
        description: `${value} API密钥`,
        status: true
      };
      
      const res = await SysIntegrationApi.apiManagement.createApiManagement(data);
      ElMessage.success('生成API密钥成功');
      
      // 重新加载API密钥列表
      await loadApiManagement();
    }).catch(() => {
      // 用户取消
    });
  } catch (error) {
    console.error('生成API密钥失败:', error);
    ElMessage.error('生成API密钥失败');
  } finally {
    loading.value = false;
  }
};

const handleCopyApiKey = (key) => {
  navigator.clipboard.writeText(key).then(() => {
    ElMessage.success('API密钥已复制到剪贴板');
  }).catch(() => {
    ElMessage.error('复制失败，请手动复制');
  });
};

const handleRevokeApiKey = async (row) => {
  try {
    await ElMessageBox.confirm(
      '确定要撤销此API密钥吗？撤销后将无法恢复，依赖此密钥的应用将无法访问API。',
      '撤销API密钥',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    );
    
    loading.value = true;
    await SysIntegrationApi.apiManagement.deleteApiManagement(row.id);
    ElMessage.success('撤销API密钥成功');
    
    // 重新加载API密钥列表
    await loadApiManagement();
  } catch (error) {
    if (error !== 'cancel') {
      console.error('撤销API密钥失败:', error);
      ElMessage.error('撤销API密钥失败');
    }
  } finally {
    loading.value = false;
  }
};

const handleAddCallbackUrl = () => {
  ElMessage.info('回调URL管理功能正在开发中');
};

const handleEditCallbackUrl = (row) => {
  ElMessage.info('回调URL管理功能正在开发中');
};

const handleToggleCallbackStatus = (row) => {
  ElMessage.info('回调URL管理功能正在开发中');
};

const handleTestCallback = (row) => {
  ElMessage.info('回调URL管理功能正在开发中');
};

const handleDeleteCallbackUrl = (row) => {
  ElMessage.info('回调URL管理功能正在开发中');
};

// 单点登录配置方法
const handleSaveLdapConfig = async () => {
  loading.value = true;
  try {
    const data = {
      name: 'LDAP配置',
      type: 'LDAP',
      provider: 'ActiveDirectory',
      serverUrl: ldapConfig.value.server,
      baseDn: ldapConfig.value.userSearchBase,
      usernameAttribute: ldapConfig.value.userIdAttribute,
      clientId: 'ldap-client',
      clientSecret: ldapConfig.value.bindPassword,
      config: JSON.stringify({
        bindDn: ldapConfig.value.bindDn,
        userFilter: ldapConfig.value.userFilter
      }),
      status: ldapConfig.value.enabled,
      isDefault: true
    };
    
    if (ldapConfig.value.id) {
      // 更新
      data.id = ldapConfig.value.id;
      await SysIntegrationApi.ssoConfig.updateSsoConfig(data);
      ElMessage.success('更新LDAP配置成功');
    } else {
      // 创建
      const res = await SysIntegrationApi.ssoConfig.createSsoConfig(data);
      ldapConfig.value.id = res.data;
      ElMessage.success('创建LDAP配置成功');
    }
    
    // 重新加载数据
    await loadSsoConfig();
  } catch (error) {
    console.error('保存LDAP配置失败:', error);
    ElMessage.error('保存LDAP配置失败');
  } finally {
    loading.value = false;
  }
};

const handleTestLdapConnection = async () => {
  if (!ldapConfig.value.id) {
    ElMessage.warning('请先保存配置');
    return;
  }
  
  loading.value = true;
  try {
    const res = await SysIntegrationApi.ssoConfig.testSsoConfigConnection(ldapConfig.value.id);
    if (res.data) {
      ElMessage.success('LDAP连接测试成功');
    } else {
      ElMessage.error('LDAP连接测试失败');
    }
  } catch (error) {
    console.error('测试LDAP连接失败:', error);
    ElMessage.error('测试LDAP连接失败');
  } finally {
    loading.value = false;
  }
};

const handleSaveOauthConfig = async () => {
  loading.value = true;
  try {
    // 保存Google OAuth配置
    if (oauthConfig.value.google.clientId) {
      const googleData = {
        name: 'Google OAuth',
        type: 'OAUTH',
        provider: 'Google',
        clientId: oauthConfig.value.google.clientId,
        clientSecret: oauthConfig.value.google.clientSecret,
        config: JSON.stringify({
          redirectUri: oauthConfig.value.google.redirectUri
        }),
        status: oauthConfig.value.enabled,
        isDefault: false
      };
      
      // 查找现有配置
      const existingGoogle = ssoConfigs.value.find(
        item => item.type === 'OAUTH' && item.provider === 'Google'
      );
      
      if (existingGoogle) {
        // 更新
        googleData.id = existingGoogle.id;
        await SysIntegrationApi.ssoConfig.updateSsoConfig(googleData);
      } else {
        // 创建
        await SysIntegrationApi.ssoConfig.createSsoConfig(googleData);
      }
    }
    
    // 保存GitHub OAuth配置
    if (oauthConfig.value.github.clientId) {
      const githubData = {
        name: 'GitHub OAuth',
        type: 'OAUTH',
        provider: 'GitHub',
        clientId: oauthConfig.value.github.clientId,
        clientSecret: oauthConfig.value.github.clientSecret,
        config: JSON.stringify({
          redirectUri: oauthConfig.value.github.redirectUri
        }),
        status: oauthConfig.value.enabled,
        isDefault: false
      };
      
      // 查找现有配置
      const existingGithub = ssoConfigs.value.find(
        item => item.type === 'OAUTH' && item.provider === 'GitHub'
      );
      
      if (existingGithub) {
        // 更新
        githubData.id = existingGithub.id;
        await SysIntegrationApi.ssoConfig.updateSsoConfig(githubData);
      } else {
        // 创建
        await SysIntegrationApi.ssoConfig.createSsoConfig(githubData);
      }
    }
    
    ElMessage.success('保存OAuth配置成功');
    
    // 重新加载数据
    await loadSsoConfig();
  } catch (error) {
    console.error('保存OAuth配置失败:', error);
    ElMessage.error('保存OAuth配置失败');
  } finally {
    loading.value = false;
  }
};

const handleSaveSamlConfig = async () => {
  loading.value = true;
  try {
    const data = {
      name: `${samlConfig.value.identityProvider.charAt(0).toUpperCase() + samlConfig.value.identityProvider.slice(1)} SAML`,
      type: 'SAML',
      provider: samlConfig.value.identityProvider === 'custom' ? 'Custom' : 
               samlConfig.value.identityProvider === 'azure' ? 'Azure AD' : 'Okta',
      serverUrl: null,
      config: JSON.stringify({
        metadataUrl: samlConfig.value.metadataUrl,
        entityId: samlConfig.value.entityId,
        acsUrl: samlConfig.value.acsUrl
      }),
      status: samlConfig.value.enabled,
      isDefault: false
    };
    
    if (samlConfig.value.id) {
      // 更新
      data.id = samlConfig.value.id;
      await SysIntegrationApi.ssoConfig.updateSsoConfig(data);
      ElMessage.success('更新SAML配置成功');
    } else {
      // 创建
      const res = await SysIntegrationApi.ssoConfig.createSsoConfig(data);
      samlConfig.value.id = res.data;
      ElMessage.success('创建SAML配置成功');
    }
    
    // 重新加载数据
    await loadSsoConfig();
  } catch (error) {
    console.error('保存SAML配置失败:', error);
    ElMessage.error('保存SAML配置失败');
  } finally {
    loading.value = false;
  }
};

const handleDownloadMetadata = () => {
  // 创建一个包含SP元数据的XML文件
  const metadata = `<?xml version="1.0"?>
<md:EntityDescriptor xmlns:md="urn:oasis:names:tc:SAML:2.0:metadata"
                     validUntil="2099-01-01T00:00:00Z"
                     entityID="${samlConfig.value.entityId}">
  <md:SPSSODescriptor AuthnRequestsSigned="false" WantAssertionsSigned="false" protocolSupportEnumeration="urn:oasis:names:tc:SAML:2.0:protocol">
    <md:NameIDFormat>urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified</md:NameIDFormat>
    <md:AssertionConsumerService Binding="urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST"
                                 Location="${samlConfig.value.acsUrl}"
                                 index="1" />
  </md:SPSSODescriptor>
</md:EntityDescriptor>`;
  
  const blob = new Blob([metadata], { type: 'application/xml' });
  const link = document.createElement('a');
  link.href = URL.createObjectURL(blob);
  link.download = 'sp-metadata.xml';
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
  URL.revokeObjectURL(link.href);
  
  ElMessage.success('SP元数据已下载');
};

onMounted(() => {
  // 初始化加载数据
  loadToolIntegrations();
  loadApiManagement();
  loadSsoConfig();
  
  // 监听选项卡切换，动态加载数据
  watch(activeTab, (newVal) => {
    if (newVal === 'tools') {
      loadToolIntegrations();
    } else if (newVal === 'api') {
      loadApiManagement();
    } else if (newVal === 'sso') {
      loadSsoConfig();
    }
  });
});
</script>

<style scoped>
.integration-container {
  padding: 20px;
}

.integration-card {
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

.integration-tabs {
  min-height: 500px;
}
</style> 