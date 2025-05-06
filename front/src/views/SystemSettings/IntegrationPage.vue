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
import { ref, onMounted } from 'vue';

// 选项卡状态
const activeTab = ref('tools');
const activeTools = ref(['jira']);
const oauthActiveProvider = ref('google');
const loading = ref(false);

// 第三方工具集成数据
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
const apiKeyList = ref([
  { 
    id: 1, 
    name: '测试API密钥', 
    key: 'vx_test_api_key_12345abcdef', 
    createdAt: '2023-05-15', 
    expiresAt: '2024-05-15', 
    status: '有效' 
  },
  { 
    id: 2, 
    name: '开发API密钥', 
    key: 'vx_dev_api_key_67890ghijkl', 
    createdAt: '2023-06-10', 
    expiresAt: '永不过期', 
    status: '有效' 
  }
]);

const callbackUrlList = ref([
  { id: 1, name: '测试报告通知', url: 'https://example.com/webhooks/testreport', eventType: '测试报告生成', enabled: true },
  { id: 2, name: '缺陷创建通知', url: 'https://example.com/webhooks/bug', eventType: '缺陷创建', enabled: false }
]);

// 单点登录配置数据
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

// 第三方工具集成方法
const handleSaveJiraConfig = () => {
  console.log('保存Jira配置', jiraConfig.value);
};

const handleTestJiraConnection = () => {
  console.log('测试Jira连接');
  // 模拟成功连接
  jiraConfig.value.connected = true;
};

const handleSaveGitConfig = () => {
  console.log('保存Git配置', gitConfig.value);
};

const handleTestGitConnection = () => {
  console.log('测试Git连接');
  // 模拟成功连接
  gitConfig.value.connected = true;
};

const handleSaveJenkinsConfig = () => {
  console.log('保存Jenkins配置', jenkinsConfig.value);
};

const handleTestJenkinsConnection = () => {
  console.log('测试Jenkins连接');
  // 模拟成功连接
  jenkinsConfig.value.connected = true;
};

const handleSaveSeleniumConfig = () => {
  console.log('保存Selenium配置', seleniumConfig.value);
};

const handleTestSeleniumConnection = () => {
  console.log('测试Selenium连接');
  // 模拟成功连接
  seleniumConfig.value.connected = true;
};

// API管理方法
const handleGenerateApiKey = () => {
  console.log('生成新API密钥');
};

const handleCopyApiKey = (key) => {
  console.log('复制API密钥', key);
  // 复制到剪贴板
};

const handleRevokeApiKey = (row) => {
  console.log('撤销API密钥', row);
};

const handleAddCallbackUrl = () => {
  console.log('添加回调URL');
};

const handleEditCallbackUrl = (row) => {
  console.log('编辑回调URL', row);
};

const handleToggleCallbackStatus = (row) => {
  console.log('切换回调URL状态', row);
};

const handleTestCallback = (row) => {
  console.log('测试回调URL', row);
};

const handleDeleteCallbackUrl = (row) => {
  console.log('删除回调URL', row);
};

// 单点登录配置方法
const handleSaveLdapConfig = () => {
  console.log('保存LDAP配置', ldapConfig.value);
};

const handleTestLdapConnection = () => {
  console.log('测试LDAP连接');
};

const handleSaveOauthConfig = () => {
  console.log('保存OAuth配置', oauthConfig.value);
};

const handleSaveSamlConfig = () => {
  console.log('保存SAML配置', samlConfig.value);
};

const handleDownloadMetadata = () => {
  console.log('下载SP元数据');
};

onMounted(() => {
  // 初始化加载数据
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