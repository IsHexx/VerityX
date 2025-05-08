<template>
  <div class="system-info-container">
    <el-card>
          <template #header>
            <div class="card-header">
          <span>系统信息</span>
            </div>
          </template>
      
      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
          <el-tab-pane label="版本信息" name="version">
          <VersionInfoTab ref="versionTabRef" />
          </el-tab-pane>
          <el-tab-pane label="许可证管理" name="license">
          <LicenseTab ref="licenseTabRef" />
          </el-tab-pane>
          <el-tab-pane label="系统升级" name="upgrade">
          <UpgradeTab ref="upgradeTabRef" />
          </el-tab-pane>
          <el-tab-pane label="关于VerityX" name="about">
          <AboutTab ref="aboutTabRef" />
          </el-tab-pane>
        </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import VersionInfoTab from './components/VersionInfoTab.vue'
import LicenseTab from './components/LicenseTab.vue'
import UpgradeTab from './components/UpgradeTab.vue'
import AboutTab from './components/AboutTab.vue'

const activeTab = ref('version')

// 子组件引用
const versionTabRef = ref(null)
const licenseTabRef = ref(null)
const upgradeTabRef = ref(null)
const aboutTabRef = ref(null)

// 处理标签页切换
const handleTabChange = (tab) => {
  console.log('切换到标签页:', tab)
  
  // 根据当前活动的标签页触发相应的数据加载
  switch (tab) {
    case 'version':
      if (versionTabRef.value && versionTabRef.value.fetchData) {
        versionTabRef.value.fetchData()
      } else {
        console.warn('版本信息标签页的fetchData方法不可用')
      }
      break
    case 'license':
      if (licenseTabRef.value && licenseTabRef.value.fetchData) {
        licenseTabRef.value.fetchData()
      } else {
        console.warn('许可证管理标签页的fetchData方法不可用')
      }
      break
    case 'upgrade':
      if (upgradeTabRef.value && upgradeTabRef.value.fetchData) {
        upgradeTabRef.value.fetchData()
      } else {
        console.warn('系统升级标签页的fetchData方法不可用')
      }
      break
    case 'about':
      if (aboutTabRef.value && aboutTabRef.value.fetchData) {
        aboutTabRef.value.fetchData()
      } else {
        console.warn('关于VerityX标签页的fetchData方法不可用')
      }
      break
  }
}

// 从路由参数中获取初始tab
onMounted(() => {
  const hash = window.location.hash
  if (hash) {
    const tab = hash.substring(1)
    if (['version', 'license', 'upgrade', 'about'].includes(tab)) {
      activeTab.value = tab
    }
  }
  
  // 初始加载当前标签页数据
  setTimeout(() => {
    handleTabChange(activeTab.value)
  }, 100)
})
</script>

<style scoped>
.system-info-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style> 