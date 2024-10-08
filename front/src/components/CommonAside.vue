<template>
    <div class="aside">
        <el-button
      :icon="isCollapse ? 'el-icon-arrow-right' : 'el-icon-arrow-down'"
      @click="toggleCollapse"
      style="width: 63px; border: 0px; background-color: #ffffff;"
    >
      {{ isCollapse ? '展开' : '折叠' }}
    </el-button>
    <el-menu
      default-active="2"
      class="el-menu-vertical-demo"
      :collapse="isCollapse"
      @open="handleOpen"
      @close="handleClose"
    >
      <el-sub-menu index="1">
        <template #title>
            <el-icon><House /></el-icon>
          <span>Navigator One</span>
        </template>
        <el-menu-item-group>
          <template #title><span>Group One</span></template>
          <el-menu-item index="1-1">item one</el-menu-item>
          <el-menu-item index="1-2">item two</el-menu-item>
        </el-menu-item-group>
        <el-menu-item-group title="Group Two">
          <el-menu-item index="1-3">item three</el-menu-item>
        </el-menu-item-group>
        <el-sub-menu index="1-4">
          <template #title><span>item four</span></template>
          <el-menu-item index="1-4-1">item one</el-menu-item>
        </el-sub-menu>
      </el-sub-menu>
      <el-menu-item index="2">
        <el-icon><Notification /></el-icon>
        <template #title>Navigator Two</template>
      </el-menu-item>
      <el-menu-item index="3">
        <el-icon><Edit /></el-icon>
        <template #title>Navigator Three</template>
      </el-menu-item>
      <el-menu-item index="4">
        <el-icon><setting /></el-icon>
        <template #title>Navigator Four</template>
      </el-menu-item>
    </el-menu>
</div>
  </template>
  
  <script  setup>
  import { ref } from 'vue'
  import {
    Menu as House,Edit,Notification,Setting,
  } from '@element-plus/icons-vue'
  
  const isCollapse = ref(true)

  const toggleCollapse = () => {
  isCollapse.value = !isCollapse.value
}
  const handleOpen = (key) => {
    console.log(key)
  }
  const handleClose = (key) => {
    console.log(key)
  }
  </script>
  
  <style>
  .aside{
    background-color: #ffffff;
  }
  .el-menu-vertical-demo:not(.el-menu--collapse) {
    
    min-height: 400px;
    background-color: #ffffff
  }
  </style>
  