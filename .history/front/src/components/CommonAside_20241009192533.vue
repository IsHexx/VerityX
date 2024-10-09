<template>
  <div class="aside">
    <el-menu
      :default-active="activeMainMenu"
      class="el-menu-vertical-demo"
      :collapse="isCollapse"
      @open="handleOpen"
      @close="handleClose"
    >
      <el-button
        :icon="isCollapse ? 'el-icon-arrow-right' : 'el-icon-arrow-down'"
        @click="toggleCollapse"
        style="width: 55px; border: 0px"
      >
        {{ isCollapse ? "展开" : "折叠" }}
      </el-button>
      <div v-for="item in menuItems" :key="item.index">
        <el-sub-menu v-if="item.subMenus && item.subMenus.length > 0" :index="item.index">
          <template #title>
            <el-icon><component :is="item.icon" /></el-icon>
            <span>{{ item.title }}</span>
          </template>
          <el-menu-item 
            v-for="subItem in item.subMenus" 
            :key="subItem.index" 
            :index="subItem.index"
            @click="setActiveMenu(item.index, subItem.index)"
          >
            {{ subItem.title }}
          </el-menu-item>
        </el-sub-menu>
        <el-menu-item 
          v-else
          :index="item.index"
          @click="setActiveMenu(item.index)"
        >
          <el-icon><component :is="item.icon" /></el-icon>
          <template #title>{{ item.title }}</template>
        </el-menu-item>
      </div>
    </el-menu>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import {
  Menu as House,
  Edit,
  Notification,
  Setting,
} from "@element-plus/icons-vue";
import { useMenuStore } from '@/store/menuStore';

const { menuItems, activeMainMenu, setActiveMenu, initializeActiveMenu } = useMenuStore();

const isCollapse = ref(true);
const toggleCollapse = () => {
  isCollapse.value = !isCollapse.value;
};
const handleOpen = (key) => {
  console.log(key);
};
const handleClose = (key) => {
  console.log(key);
};

onMounted(() => {
  initializeActiveMenu();
});
</script>