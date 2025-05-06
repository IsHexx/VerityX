<template>
  <div class="system-info-container">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card shadow="hover" class="info-card">
          <template #header>
            <div class="card-header">
              <span>VerityX 测试平台</span>
              <el-tag type="success">{{ systemInfo.version }}</el-tag>
            </div>
          </template>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="当前版本">{{ systemInfo.version }}</el-descriptions-item>
            <el-descriptions-item label="发布日期">{{ systemInfo.releaseDate }}</el-descriptions-item>
            <el-descriptions-item label="系统名称">{{ systemInfo.name }}</el-descriptions-item>
            <el-descriptions-item label="运行环境">{{ systemInfo.environment }}</el-descriptions-item>
            <el-descriptions-item label="最后更新">{{ systemInfo.lastUpdate }}</el-descriptions-item>
            <el-descriptions-item label="运行时间">{{ systemInfo.uptime }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
    </el-row>
    
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="24">
        <el-tabs v-model="activeTab" class="info-tabs">
          <el-tab-pane label="版本信息" name="version">
            <el-card shadow="hover" class="info-card">
              <template #header>
                <div class="card-header">
                  <span>版本历史</span>
                  <el-button type="primary" size="small" @click="handleCheckUpdate" :loading="checkingUpdate">检查更新</el-button>
                </div>
              </template>
              <el-timeline>
                <el-timeline-item
                  v-for="(version, index) in versionHistory"
                  :key="index"
                  :timestamp="version.date"
                  :type="version.version === systemInfo.version ? 'primary' : ''"
                >
                  <el-card class="version-card">
                    <template #header>
                      <div class="version-header">
                        <span>{{ version.version }}</span>
                        <div>
                          <el-tag v-if="version.version === systemInfo.version" type="success">当前版本</el-tag>
                          <el-tag v-else-if="index === 0" type="danger">最新版本</el-tag>
                        </div>
                      </div>
                    </template>
                    <h4>更新内容：</h4>
                    <el-divider></el-divider>
                    <ul class="version-changes">
                      <li v-for="(change, cIdx) in version.changes" :key="cIdx">
                        <el-tag size="small" :type="getChangeTypeTag(change.type)" class="change-tag">{{ change.type }}</el-tag>
                        {{ change.description }}
                      </li>
                    </ul>
                  </el-card>
                </el-timeline-item>
              </el-timeline>
            </el-card>
          </el-tab-pane>
          
          <el-tab-pane label="许可证管理" name="license">
            <el-card shadow="hover" class="info-card">
              <template #header>
                <div class="card-header">
                  <span>许可证信息</span>
                  <el-button 
                    type="primary" 
                    size="small" 
                    @click="handleUpdateLicense"
                    v-if="licenseInfo.status !== 'valid' || licenseInfo.daysRemaining < 30"
                  >
                    更新许可证
                  </el-button>
                </div>
              </template>
              <el-descriptions :column="2" border>
                <el-descriptions-item label="许可证状态">
                  <el-tag :type="getLicenseStatusType(licenseInfo.status)">
                    {{ getLicenseStatusText(licenseInfo.status) }}
                  </el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="许可证类型">{{ licenseInfo.type }}</el-descriptions-item>
                <el-descriptions-item label="客户名称">{{ licenseInfo.customer }}</el-descriptions-item>
                <el-descriptions-item label="发行日期">{{ licenseInfo.issueDate }}</el-descriptions-item>
                <el-descriptions-item label="过期日期">{{ licenseInfo.expiryDate }}</el-descriptions-item>
                <el-descriptions-item label="剩余天数">
                  <span :class="{'expiry-warning': licenseInfo.daysRemaining < 30}">
                    {{ licenseInfo.daysRemaining }}天
                  </span>
                </el-descriptions-item>
                <el-descriptions-item label="许可用户数">{{ licenseInfo.maxUsers }}</el-descriptions-item>
                <el-descriptions-item label="当前用户数">{{ licenseInfo.currentUsers }}</el-descriptions-item>
                <el-descriptions-item label="许可证密钥" :span="2">
                  <el-input 
                    v-model="licenseInfo.key" 
                    disabled 
                    class="license-key-input"
                  >
                    <template #append>
                      <el-button @click="handleCopyLicenseKey">复制</el-button>
                    </template>
                  </el-input>
                </el-descriptions-item>
              </el-descriptions>
              
              <el-divider content-position="left">可用功能</el-divider>
              <el-row :gutter="20">
                <el-col :span="8" v-for="(feature, index) in licenseInfo.features" :key="index">
                  <el-card shadow="hover" class="feature-card">
                    <div class="feature-item">
                      <span>{{ feature.name }}</span>
                      <el-tag :type="feature.enabled ? 'success' : 'info'">
                        {{ feature.enabled ? '已启用' : '未启用' }}
                      </el-tag>
                    </div>
                  </el-card>
                </el-col>
              </el-row>
            </el-card>
          </el-tab-pane>
          
          <el-tab-pane label="系统升级" name="upgrade">
            <el-card shadow="hover" class="info-card">
              <template #header>
                <div class="card-header">
                  <span>系统升级</span>
                </div>
              </template>
              <el-alert
                v-if="newVersionAvailable"
                type="success"
                :closable="false"
                show-icon
              >
                <template #title>
                  发现新版本 {{ latestVersion.version }}，建议升级
                </template>
                <div class="update-info">
                  <p>发布日期: {{ latestVersion.date }}</p>
                  <h4>新版本特性:</h4>
                  <ul>
                    <li v-for="(change, index) in latestVersion.changes" :key="index">
                      <el-tag size="small" :type="getChangeTypeTag(change.type)" class="change-tag">{{ change.type }}</el-tag>
                      {{ change.description }}
                    </li>
                  </ul>
                </div>
              </el-alert>
              <el-alert
                v-else
                type="info"
                :closable="false"
                show-icon
              >
                <template #title>
                  您的系统已是最新版本
                </template>
                <p>当前版本: {{ systemInfo.version }}，发布日期: {{ systemInfo.releaseDate }}</p>
              </el-alert>
              
              <div class="upgrade-options" v-if="newVersionAvailable">
                <el-button type="primary" @click="handleUpgradeSystem">立即升级</el-button>
                <el-button type="info" @click="handleDownloadPackage">下载升级包</el-button>
              </div>
              
              <el-divider content-position="left">升级历史</el-divider>
              <el-table :data="upgradeHistory" style="width: 100%">
                <el-table-column prop="version" label="版本" width="120"></el-table-column>
                <el-table-column prop="upgradeDate" label="升级日期" width="180"></el-table-column>
                <el-table-column prop="operator" label="操作人" width="120"></el-table-column>
                <el-table-column prop="status" label="状态" width="100">
                  <template #default="scope">
                    <el-tag :type="scope.row.status === '成功' ? 'success' : 'danger'">
                      {{ scope.row.status }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="notes" label="备注"></el-table-column>
              </el-table>
            </el-card>
          </el-tab-pane>
          
          <el-tab-pane label="关于VerityX" name="about">
            <el-card shadow="hover" class="info-card about-card">
              <div class="about-content">
                <div class="about-logo">
                  <img src="@/assets/logo.png" alt="VerityX Logo" class="logo-img">
                  <h2>VerityX 测试平台</h2>
                  <p>版本 {{ systemInfo.version }}</p>
                </div>
                <el-divider></el-divider>
                <p class="about-description">
                  VerityX是一个现代化的自动化测试平台，旨在提供全面的测试解决方案，
                  包括API测试、UI自动化测试等功能，帮助企业提高测试效率和软件质量。
                </p>
                <el-divider></el-divider>
                <div class="about-links">
                  <a href="https://github.com/verityx/platform" target="_blank">GitHub</a>
                  <a href="https://www.verityx.io/docs" target="_blank">文档</a>
                  <a href="https://www.verityx.io/support" target="_blank">支持</a>
                </div>
                <el-divider></el-divider>
                <div class="about-copyright">
                  <p>© {{ new Date().getFullYear() }} VerityX. 保留所有权利。</p>
                </div>
              </div>
            </el-card>
          </el-tab-pane>
        </el-tabs>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';

// 选项卡状态
const activeTab = ref('version');
const checkingUpdate = ref(false);

// 系统信息数据
const systemInfo = ref({
  version: 'v1.2.3',
  releaseDate: '2023-06-15',
  name: 'VerityX 测试平台',
  environment: 'Production',
  lastUpdate: '2023-07-08',
  uptime: '15天8小时43分钟'
});

// 版本历史数据
const versionHistory = ref([
  {
    version: 'v1.3.0',
    date: '2023-07-10',
    changes: [
      { type: '新功能', description: '添加了自动化测试报告生成功能' },
      { type: '新功能', description: '支持测试计划的定时执行' },
      { type: '优化', description: '改进了UI界面的响应速度' },
      { type: '修复', description: '修复了测试执行过程中偶发的连接中断问题' }
    ]
  },
  {
    version: 'v1.2.3',
    date: '2023-06-15',
    changes: [
      { type: '优化', description: '改进了测试执行引擎的性能' },
      { type: '修复', description: '修复了某些情况下API测试响应解析错误的问题' },
      { type: '安全', description: '加强了用户认证和授权机制' }
    ]
  },
  {
    version: 'v1.2.0',
    date: '2023-05-20',
    changes: [
      { type: '新功能', description: '添加了UI测试模块' },
      { type: '新功能', description: '支持测试结果与JIRA集成' },
      { type: '优化', description: '优化了数据库查询性能' },
      { type: '修复', description: '修复了多个用户界面问题' }
    ]
  }
]);

// 许可证信息
const licenseInfo = ref({
  status: 'valid', // valid, expiring, expired, invalid
  type: '企业版',
  customer: 'ABC科技有限公司',
  issueDate: '2023-01-01',
  expiryDate: '2024-01-01',
  daysRemaining: 175,
  maxUsers: 100,
  currentUsers: 65,
  key: 'VRTX-ENTX-ABCD-1234-5678-EFGH-9012',
  features: [
    { name: 'API测试', enabled: true },
    { name: 'UI测试', enabled: true },
    { name: '性能测试', enabled: true },
    { name: '移动应用测试', enabled: false },
    { name: '安全测试', enabled: true },
    { name: '智能测试分析', enabled: false }
  ]
});

// 升级历史
const upgradeHistory = ref([
  { version: 'v1.2.3', upgradeDate: '2023-06-15', operator: 'admin', status: '成功', notes: '常规版本升级' },
  { version: 'v1.2.0', upgradeDate: '2023-05-20', operator: 'admin', status: '成功', notes: '主要功能更新' },
  { version: 'v1.1.5', upgradeDate: '2023-04-10', operator: 'admin', status: '失败', notes: '数据库迁移出错，已回滚' },
  { version: 'v1.1.5', upgradeDate: '2023-04-11', operator: 'admin', status: '成功', notes: '修复数据库迁移问题后重新升级' }
]);

// 计算属性
const newVersionAvailable = computed(() => {
  return versionHistory.value.length > 0 && versionHistory.value[0].version !== systemInfo.value.version;
});

const latestVersion = computed(() => {
  return versionHistory.value.length > 0 ? versionHistory.value[0] : null;
});

// 方法
const getChangeTypeTag = (type) => {
  const typeMap = {
    '新功能': 'success',
    '优化': 'primary',
    '修复': 'warning',
    '安全': 'danger'
  };
  return typeMap[type] || '';
};

const getLicenseStatusType = (status) => {
  const statusMap = {
    'valid': 'success',
    'expiring': 'warning',
    'expired': 'danger',
    'invalid': 'danger'
  };
  return statusMap[status] || '';
};

const getLicenseStatusText = (status) => {
  const statusMap = {
    'valid': '有效',
    'expiring': '即将过期',
    'expired': '已过期',
    'invalid': '无效'
  };
  return statusMap[status] || status;
};

const handleCheckUpdate = () => {
  checkingUpdate.value = true;
  console.log('检查更新');
  // 模拟请求
  setTimeout(() => {
    checkingUpdate.value = false;
    // 这里可以添加通知
  }, 1500);
};

const handleCopyLicenseKey = () => {
  console.log('复制许可证密钥', licenseInfo.value.key);
  // 复制到剪贴板
};

const handleUpdateLicense = () => {
  console.log('更新许可证');
};

const handleUpgradeSystem = () => {
  console.log('升级系统', latestVersion.value);
};

const handleDownloadPackage = () => {
  console.log('下载升级包', latestVersion.value);
};

onMounted(() => {
  // 初始化加载数据
});
</script>

<style scoped>
.system-info-container {
  padding: 20px;
}

.info-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.version-card {
  margin-bottom: 10px;
}

.version-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.version-changes {
  padding-left: 20px;
  margin: 10px 0;
}

.change-tag {
  margin-right: 8px;
}

.feature-card {
  margin-bottom: 15px;
  height: 60px;
}

.feature-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.license-key-input {
  font-family: 'Courier New', monospace;
}

.expiry-warning {
  color: #e6a23c;
  font-weight: bold;
}

.upgrade-options {
  margin: 20px 0;
  display: flex;
  justify-content: center;
  gap: 20px;
}

.update-info {
  margin-top: 10px;
}

.about-card {
  min-height: 500px;
}

.about-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
}

.about-logo {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 20px;
}

.logo-img {
  width: 120px;
  height: 120px;
  margin-bottom: 10px;
}

.about-description {
  text-align: center;
  max-width: 800px;
  line-height: 1.8;
  margin: 20px 0;
}

.about-links {
  display: flex;
  gap: 30px;
  margin: 20px 0;
}

.about-links a {
  color: #409eff;
  text-decoration: none;
  font-size: 16px;
}

.about-links a:hover {
  text-decoration: underline;
}

.about-copyright {
  margin-top: 20px;
  color: #909399;
}

.info-tabs {
  min-height: 500px;
}
</style> 