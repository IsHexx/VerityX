<template>
  <div class="system-params-container">
    <el-tabs v-model="activeTab" class="system-tabs" @tab-change="handleTabChange">
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
import { ElMessage, ElMessageBox } from 'element-plus';
import SysConfigApi from '@/api/sysConfigService';
import SysDictApi from '@/api/sysDictService';

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
const dictTypeList = ref([]);
const activeDictType = ref('');
const activeDictTypeName = ref('');
const dictDataList = ref([]);

// 邮件服务配置数据
const emailConfig = ref({
  smtpServer: '',
  smtpPort: 25,
  fromEmail: '',
  fromName: '',
  username: '',
  password: '',
  encryptionType: 'none'
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

// 加载系统参数
const loadGlobalParams = async () => {
  loading.value = true;
  try {
    // 定义需要获取的配置键名列表
    const configKeys = [
      'sys.default.test.env', 
      'sys.request.timeout', 
      'sys.retry.count',
      'sys.concurrent.count',
      'sys.default.page.size',
      'sys.date.format',
      'sys.theme',
      'sys.sidebar.mode',
      'sys.default.project',
      'sys.default.report.template'
    ];
    
    // 调用API批量获取配置
    const res = await SysConfigApi.getConfigMap(configKeys);
    if (res.code === 200 && res.data) {
      // 映射API返回的数据到globalParams对象
      globalParams.value = {
        defaultTestEnv: res.data['sys.default.test.env'] || 'test',
        requestTimeout: parseInt(res.data['sys.request.timeout'] || '10'),
        retryCount: parseInt(res.data['sys.retry.count'] || '3'),
        concurrentCount: parseInt(res.data['sys.concurrent.count'] || '5'),
        defaultPageSize: parseInt(res.data['sys.default.page.size'] || '20'),
        dateFormat: res.data['sys.date.format'] || 'YYYY-MM-DD',
        theme: res.data['sys.theme'] || 'light',
        sidebarMode: res.data['sys.sidebar.mode'] || 'expanded',
        defaultProject: res.data['sys.default.project'] || 'all',
        defaultReportTemplate: res.data['sys.default.report.template'] || 'standard'
      };
    }
  } catch (error) {
    console.error('加载系统参数失败:', error);
    ElMessage.error('加载系统参数失败');
  } finally {
    loading.value = false;
  }
};

// 加载邮件配置
const loadEmailConfig = async () => {
  loading.value = true;
  try {
    // 定义需要获取的配置键名列表
    const configKeys = [
      'mail.smtp.host', 
      'mail.smtp.port', 
      'mail.smtp.username',
      'mail.smtp.password',
      'mail.smtp.ssl.enable',
      'mail.smtp.from',
      'mail.smtp.from.name'
    ];
    
    // 调用API批量获取配置
    const res = await SysConfigApi.getConfigMap(configKeys);
    if (res.code === 200 && res.data) {
      // 设置加密类型
      let encType = 'none';
      if (res.data['mail.smtp.ssl.enable'] === 'true') {
        encType = 'ssl';
      } else if (res.data['mail.smtp.starttls.enable'] === 'true') {
        encType = 'tls';
      }
      
      // 映射API返回的数据到emailConfig对象
      emailConfig.value = {
        smtpServer: res.data['mail.smtp.host'] || '',
        smtpPort: parseInt(res.data['mail.smtp.port'] || '25'),
        fromEmail: res.data['mail.smtp.from'] || '',
        fromName: res.data['mail.smtp.from.name'] || '',
        username: res.data['mail.smtp.username'] || '',
        password: res.data['mail.smtp.password'] || '',
        encryptionType: encType
      };
    }
  } catch (error) {
    console.error('加载邮件配置失败:', error);
    ElMessage.error('加载邮件配置失败');
  } finally {
    loading.value = false;
  }
};

// 加载安全策略
const loadSecurityPolicy = async () => {
  loading.value = true;
  try {
    // 定义需要获取的配置键名列表
    const configKeys = [
      'sys.password.min.length', 
      'sys.password.complexity', 
      'sys.password.expiry.days',
      'sys.password.history.count',
      'sys.login.fail.lock.threshold',
      'sys.account.lock.duration',
      'sys.session.timeout',
      'sys.concurrent.login.control'
    ];
    
    // 调用API批量获取配置
    const res = await SysConfigApi.getConfigMap(configKeys);
    if (res.code === 200 && res.data) {
      // 映射API返回的数据到securityPolicy对象
      securityPolicy.value = {
        passwordMinLength: parseInt(res.data['sys.password.min.length'] || '8'),
        passwordComplexity: res.data['sys.password.complexity'] || 'medium',
        passwordExpiryDays: parseInt(res.data['sys.password.expiry.days'] || '90'),
        passwordHistoryCount: parseInt(res.data['sys.password.history.count'] || '3'),
        loginFailLockThreshold: parseInt(res.data['sys.login.fail.lock.threshold'] || '5'),
        accountLockDuration: parseInt(res.data['sys.account.lock.duration'] || '30'),
        sessionTimeout: parseInt(res.data['sys.session.timeout'] || '60'),
        concurrentLoginControl: res.data['sys.concurrent.login.control'] || 'kickout'
      };
    }
  } catch (error) {
    console.error('加载安全策略失败:', error);
    ElMessage.error('加载安全策略失败');
  } finally {
    loading.value = false;
  }
};

// 加载字典类型列表
const loadDictTypes = async () => {
  loading.value = true;
  try {
    const res = await SysDictApi.getAllDictTypes();
    if (res.code === 200 && res.data) {
      dictTypeList.value = res.data.map(item => ({
        id: item.id,
        name: item.dictName
      }));
      
      // 如果有数据，默认选中第一项
      if (dictTypeList.value.length > 0) {
        activeDictType.value = dictTypeList.value[0].id.toString();
        activeDictTypeName.value = dictTypeList.value[0].name;
        loadDictData(activeDictType.value);
      }
    }
  } catch (error) {
    console.error('加载字典类型失败:', error);
    ElMessage.error('加载字典类型失败');
  } finally {
    loading.value = false;
  }
};

// 加载字典数据
const loadDictData = async (typeId) => {
  if (!typeId) return;
  
  loading.value = true;
  try {
    // 先获取字典类型详情，获取dictType值
    const typeRes = await SysDictApi.getDictTypeById(typeId);
    if (typeRes.code === 200 && typeRes.data) {
      const dictType = typeRes.data.dictType;
      
      // 根据dictType获取字典数据列表
      const dataRes = await SysDictApi.getDictDataByType(dictType);
      if (dataRes.code === 200 && dataRes.data) {
        dictDataList.value = dataRes.data.map(item => ({
          id: item.id,
          label: item.dictLabel,
          value: item.dictValue,
          cssClass: item.cssClass || '',
          sort: item.dictSort,
          status: item.status ? '启用' : '停用'
        }));
      }
    }
  } catch (error) {
    console.error('加载字典数据失败:', error);
    ElMessage.error('加载字典数据失败');
  } finally {
    loading.value = false;
  }
};

// 全局参数方法
const handleSaveParams = async () => {
  loading.value = true;
  try {
    // 将globalParams转换为批量更新的请求格式
    const updateRequests = [
      { id: 0, configKey: 'sys.default.test.env', configValue: globalParams.value.defaultTestEnv, configName: '默认测试环境', configType: 'text' },
      { id: 0, configKey: 'sys.request.timeout', configValue: globalParams.value.requestTimeout.toString(), configName: '请求超时时间', configType: 'number' },
      { id: 0, configKey: 'sys.retry.count', configValue: globalParams.value.retryCount.toString(), configName: '重试次数', configType: 'number' },
      { id: 0, configKey: 'sys.concurrent.count', configValue: globalParams.value.concurrentCount.toString(), configName: '并发执行数', configType: 'number' },
      { id: 0, configKey: 'sys.default.page.size', configValue: globalParams.value.defaultPageSize.toString(), configName: '默认分页大小', configType: 'number' },
      { id: 0, configKey: 'sys.date.format', configValue: globalParams.value.dateFormat, configName: '日期格式', configType: 'text' },
      { id: 0, configKey: 'sys.theme', configValue: globalParams.value.theme, configName: '默认主题', configType: 'text' },
      { id: 0, configKey: 'sys.sidebar.mode', configValue: globalParams.value.sidebarMode, configName: '侧边栏模式', configType: 'text' },
      { id: 0, configKey: 'sys.default.project', configValue: globalParams.value.defaultProject, configName: '默认项目', configType: 'text' },
      { id: 0, configKey: 'sys.default.report.template', configValue: globalParams.value.defaultReportTemplate, configName: '默认报告模板', configType: 'text' }
    ];
    
    // 查询现有配置，如果存在则使用其ID
    const configKeysToCheck = updateRequests.map(req => req.configKey);
    const res = await SysConfigApi.getConfigMap(configKeysToCheck);
    
    if (res.code === 200) {
      // 获取所有配置详情，填充ID
      const configPromises = configKeysToCheck.map(key => SysConfigApi.getConfigByKey(key));
      const configResults = await Promise.allSettled(configPromises);
      
      // 处理每个配置的结果
      for (let i = 0; i < configResults.length; i++) {
        const result = configResults[i];
        if (result.status === 'fulfilled' && result.value.code === 200 && result.value.data) {
          updateRequests[i].id = result.value.data.id;
        }
      }
      
      // 分离需要创建的和需要更新的
      const createRequests = updateRequests.filter(req => req.id === 0);
      const updateRequestsWithId = updateRequests.filter(req => req.id !== 0);
      
      // 创建新配置
      for (const req of createRequests) {
        const { id, ...createData } = req;
        await SysConfigApi.createConfig(createData);
      }
      
      // 批量更新现有配置
      if (updateRequestsWithId.length > 0) {
        await SysConfigApi.batchUpdateConfig(updateRequestsWithId);
      }
      
      ElMessage.success('保存全局参数成功');
    }
  } catch (error) {
    console.error('保存全局参数失败:', error);
    ElMessage.error('保存全局参数失败');
  } finally {
    loading.value = false;
  }
};

// 数据字典管理方法
const handleDictTypeSelect = (index) => {
  activeDictType.value = index;
  const selectedType = dictTypeList.value.find(item => item.id.toString() === index);
  activeDictTypeName.value = selectedType ? selectedType.name : '';
  loadDictData(index);
};

const handleAddDictType = () => {
  ElMessageBox.prompt('请输入字典类型名称', '新增字典类型', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    inputPattern: /^.{2,50}$/,
    inputErrorMessage: '名称长度必须在2-50个字符之间'
  }).then(({ value }) => {
    // 显示第二个输入框，获取字典类型编码
    ElMessageBox.prompt('请输入字典类型编码', '新增字典类型', {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      inputPattern: /^[a-zA-Z0-9_]{2,50}$/,
      inputErrorMessage: '编码只能包含字母、数字和下划线，长度在2-50个字符之间'
    }).then(async ({ value: typeCode }) => {
      try {
        loading.value = true;
        const data = {
          dictName: value,
          dictType: typeCode,
          status: true,
          remark: ''
        };
        
        const res = await SysDictApi.createDictType(data);
        if (res.code === 200) {
          ElMessage.success('添加字典类型成功');
          loadDictTypes();
        }
      } catch (error) {
        console.error('添加字典类型失败:', error);
        ElMessage.error('添加字典类型失败');
      } finally {
        loading.value = false;
      }
    });
  });
};

const handleAddDictData = () => {
  // 获取当前选中的字典类型
  const typeId = activeDictType.value;
  if (!typeId) {
    ElMessage.warning('请先选择字典类型');
    return;
  }
  
  // 获取字典类型详情
  SysDictApi.getDictTypeById(typeId).then(res => {
    if (res.code === 200 && res.data) {
      const dictType = res.data.dictType;
      
      // 弹出表单对话框收集字典数据信息
      ElMessageBox.prompt('请输入字典标签', '新增字典数据', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        inputPattern: /^.{1,100}$/,
        inputErrorMessage: '标签长度必须在1-100个字符之间'
      }).then(({ value: dictLabel }) => {
        // 弹出第二个输入框获取字典值
        ElMessageBox.prompt('请输入字典键值', '新增字典数据', {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          inputPattern: /^.{1,100}$/,
          inputErrorMessage: '键值长度必须在1-100个字符之间'
        }).then(async ({ value: dictValue }) => {
          try {
            loading.value = true;
            const data = {
              dictType: dictType,
              dictLabel: dictLabel,
              dictValue: dictValue,
              dictSort: 0,
              cssClass: '',
              listClass: '',
              isDefault: false,
              status: true,
              remark: ''
            };
            
            const res = await SysDictApi.createDictData(data);
            if (res.code === 200) {
              ElMessage.success('添加字典数据成功');
              loadDictData(typeId);
            }
          } catch (error) {
            console.error('添加字典数据失败:', error);
            ElMessage.error('添加字典数据失败');
          } finally {
            loading.value = false;
          }
        });
      });
    }
  });
};

const handleEditDictData = (row) => {
  // 弹出表单对话框编辑字典数据信息
  ElMessageBox.prompt('请输入字典标签', '编辑字典数据', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    inputPattern: /^.{1,100}$/,
    inputErrorMessage: '标签长度必须在1-100个字符之间',
    inputValue: row.label
  }).then(({ value: dictLabel }) => {
    // 弹出第二个输入框编辑字典值
    ElMessageBox.prompt('请输入字典键值', '编辑字典数据', {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      inputPattern: /^.{1,100}$/,
      inputErrorMessage: '键值长度必须在1-100个字符之间',
      inputValue: row.value
    }).then(async ({ value: dictValue }) => {
      try {
        loading.value = true;
        
        // 获取完整的字典数据
        const res = await SysDictApi.getDictDataById(row.id);
        if (res.code === 200 && res.data) {
          const dictData = res.data;
          
          // 更新数据
          dictData.dictLabel = dictLabel;
          dictData.dictValue = dictValue;
          
          const updateRes = await SysDictApi.updateDictData(dictData);
          if (updateRes.code === 200) {
            ElMessage.success('更新字典数据成功');
            loadDictData(activeDictType.value);
          }
        }
      } catch (error) {
        console.error('更新字典数据失败:', error);
        ElMessage.error('更新字典数据失败');
      } finally {
        loading.value = false;
      }
    });
  });
};

const handleToggleDictDataStatus = async (row) => {
  try {
    loading.value = true;
    
    // 获取完整的字典数据
    const res = await SysDictApi.getDictDataById(row.id);
    if (res.code === 200 && res.data) {
      const dictData = res.data;
      
      // 切换状态
      dictData.status = !dictData.status;
      
      const updateRes = await SysDictApi.updateDictData(dictData);
      if (updateRes.code === 200) {
        ElMessage.success(`${dictData.status ? '启用' : '停用'}字典数据成功`);
        loadDictData(activeDictType.value);
      }
    }
  } catch (error) {
    console.error('切换字典数据状态失败:', error);
    ElMessage.error('切换字典数据状态失败');
  } finally {
    loading.value = false;
  }
};

const handleDeleteDictData = (row) => {
  ElMessageBox.confirm(`确定要删除字典数据"${row.label}"吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      loading.value = true;
      const res = await SysDictApi.deleteDictData(row.id);
      if (res.code === 200) {
        ElMessage.success('删除字典数据成功');
        loadDictData(activeDictType.value);
      }
    } catch (error) {
      console.error('删除字典数据失败:', error);
      ElMessage.error('删除字典数据失败');
    } finally {
      loading.value = false;
    }
  });
};

// 邮件服务配置方法
const handleSaveEmailConfig = async () => {
  loading.value = true;
  try {
    // 将emailConfig转换为批量更新的请求格式
    const updateRequests = [
      { id: 0, configKey: 'mail.smtp.host', configValue: emailConfig.value.smtpServer, configName: 'SMTP服务器地址', configType: 'text' },
      { id: 0, configKey: 'mail.smtp.port', configValue: emailConfig.value.smtpPort.toString(), configName: 'SMTP服务器端口', configType: 'number' },
      { id: 0, configKey: 'mail.smtp.username', configValue: emailConfig.value.username, configName: 'SMTP用户名', configType: 'text' },
      { id: 0, configKey: 'mail.smtp.password', configValue: emailConfig.value.password, configName: 'SMTP密码', configType: 'text' },
      { id: 0, configKey: 'mail.smtp.from', configValue: emailConfig.value.fromEmail, configName: '发件人邮箱', configType: 'text' },
      { id: 0, configKey: 'mail.smtp.from.name', configValue: emailConfig.value.fromName, configName: '发件人名称', configType: 'text' },
      { id: 0, configKey: 'mail.smtp.ssl.enable', configValue: (emailConfig.value.encryptionType === 'ssl').toString(), configName: '启用SSL', configType: 'boolean' },
      { id: 0, configKey: 'mail.smtp.starttls.enable', configValue: (emailConfig.value.encryptionType === 'tls').toString(), configName: '启用TLS', configType: 'boolean' }
    ];
    
    // 查询现有配置，如果存在则使用其ID
    const configKeysToCheck = updateRequests.map(req => req.configKey);
    const res = await SysConfigApi.getConfigMap(configKeysToCheck);
    
    if (res.code === 200) {
      // 获取所有配置详情，填充ID
      const configPromises = configKeysToCheck.map(key => SysConfigApi.getConfigByKey(key));
      const configResults = await Promise.allSettled(configPromises);
      
      // 处理每个配置的结果
      for (let i = 0; i < configResults.length; i++) {
        const result = configResults[i];
        if (result.status === 'fulfilled' && result.value.code === 200 && result.value.data) {
          updateRequests[i].id = result.value.data.id;
        }
      }
      
      // 分离需要创建的和需要更新的
      const createRequests = updateRequests.filter(req => req.id === 0);
      const updateRequestsWithId = updateRequests.filter(req => req.id !== 0);
      
      // 创建新配置
      for (const req of createRequests) {
        const { id, ...createData } = req;
        await SysConfigApi.createConfig(createData);
      }
      
      // 批量更新现有配置
      if (updateRequestsWithId.length > 0) {
        await SysConfigApi.batchUpdateConfig(updateRequestsWithId);
      }
      
      ElMessage.success('保存邮件配置成功');
    }
  } catch (error) {
    console.error('保存邮件配置失败:', error);
    ElMessage.error('保存邮件配置失败');
  } finally {
    loading.value = false;
  }
};

const handleTestEmailConnection = () => {
  ElMessage.info('测试邮件连接功能待实现');
};

const handleSendTestEmail = () => {
  ElMessage.info('发送测试邮件功能待实现');
};

const handleAddEmailTemplate = () => {
  ElMessage.info('添加邮件模板功能待实现');
};

const handleEditEmailTemplate = (row) => {
  ElMessage.info('编辑邮件模板功能待实现');
};

const handleViewEmailTemplate = (row) => {
  ElMessage.info('预览邮件模板功能待实现');
};

const handleDeleteEmailTemplate = (row) => {
  ElMessage.info('删除邮件模板功能待实现');
};

// 安全策略方法
const handleSaveSecurityPolicy = async () => {
  loading.value = true;
  try {
    // 将securityPolicy转换为批量更新的请求格式
    const updateRequests = [
      { id: 0, configKey: 'sys.password.min.length', configValue: securityPolicy.value.passwordMinLength.toString(), configName: '密码最小长度', configType: 'number' },
      { id: 0, configKey: 'sys.password.complexity', configValue: securityPolicy.value.passwordComplexity, configName: '密码复杂度', configType: 'text' },
      { id: 0, configKey: 'sys.password.expiry.days', configValue: securityPolicy.value.passwordExpiryDays.toString(), configName: '密码过期天数', configType: 'number' },
      { id: 0, configKey: 'sys.password.history.count', configValue: securityPolicy.value.passwordHistoryCount.toString(), configName: '密码历史记录数', configType: 'number' },
      { id: 0, configKey: 'sys.login.fail.lock.threshold', configValue: securityPolicy.value.loginFailLockThreshold.toString(), configName: '登录失败锁定阈值', configType: 'number' },
      { id: 0, configKey: 'sys.account.lock.duration', configValue: securityPolicy.value.accountLockDuration.toString(), configName: '账户锁定时长', configType: 'number' },
      { id: 0, configKey: 'sys.session.timeout', configValue: securityPolicy.value.sessionTimeout.toString(), configName: '会话超时时间', configType: 'number' },
      { id: 0, configKey: 'sys.concurrent.login.control', configValue: securityPolicy.value.concurrentLoginControl, configName: '并发登录控制', configType: 'text' }
    ];
    
    // 查询现有配置，如果存在则使用其ID
    const configKeysToCheck = updateRequests.map(req => req.configKey);
    const res = await SysConfigApi.getConfigMap(configKeysToCheck);
    
    if (res.code === 200) {
      // 获取所有配置详情，填充ID
      const configPromises = configKeysToCheck.map(key => SysConfigApi.getConfigByKey(key));
      const configResults = await Promise.allSettled(configPromises);
      
      // 处理每个配置的结果
      for (let i = 0; i < configResults.length; i++) {
        const result = configResults[i];
        if (result.status === 'fulfilled' && result.value.code === 200 && result.value.data) {
          updateRequests[i].id = result.value.data.id;
        }
      }
      
      // 分离需要创建的和需要更新的
      const createRequests = updateRequests.filter(req => req.id === 0);
      const updateRequestsWithId = updateRequests.filter(req => req.id !== 0);
      
      // 创建新配置
      for (const req of createRequests) {
        const { id, ...createData } = req;
        await SysConfigApi.createConfig(createData);
      }
      
      // 批量更新现有配置
      if (updateRequestsWithId.length > 0) {
        await SysConfigApi.batchUpdateConfig(updateRequestsWithId);
      }
      
      ElMessage.success('保存安全策略成功');
    }
  } catch (error) {
    console.error('保存安全策略失败:', error);
    ElMessage.error('保存安全策略失败');
  } finally {
    loading.value = false;
  }
};

// 加载数据
onMounted(() => {
  // 只加载当前活动tab的数据
  loadTabData(activeTab.value);
});

// 监听tab切换
const handleTabChange = (tabName) => {
  // 切换tab时加载对应数据
  loadTabData(tabName);
};

// 根据当前tab加载数据
const loadTabData = (tabName) => {
  loading.value = true;
  
  try {
    switch (tabName) {
      case 'global':
        loadGlobalParams();
        break;
      case 'dictionary':
        loadDictTypes();
        break;
      case 'email':
        loadEmailConfig();
        break;
      case 'security':
        loadSecurityPolicy();
        break;
    }
  } catch (error) {
    console.error(`加载${tabName}标签页数据失败:`, error);
    ElMessage.error(`加载数据失败`);
  } finally {
    loading.value = false;
  }
};
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