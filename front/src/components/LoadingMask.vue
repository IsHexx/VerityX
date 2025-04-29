<template>
  <Transition name="fade">
    <div class="loading-mask" v-if="visible">
      <div class="loading-container">
        <el-icon class="loading-icon"><Loading /></el-icon>
        <div class="loading-text">{{ text }}</div>
      </div>
    </div>
  </Transition>
</template>

<script setup>
import { defineProps } from 'vue';
import { Loading } from '@element-plus/icons-vue';

// 使用轻量级的defineProps，不使用ref，减少响应式开销
const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  text: {
    type: String,
    default: '正在加载...'
  }
});
</script>

<style scoped>
.loading-mask {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(255, 255, 255, 0.9);
  z-index: 9999;
  display: flex;
  justify-content: center;
  align-items: center;
}

.loading-container {
  text-align: center;
}

.loading-icon {
  font-size: 40px;
  color: #409EFF;
  animation: rotating 1s linear infinite;
}

.loading-text {
  margin-top: 16px;
  font-size: 16px;
  color: #606266;
}

/* 添加淡入淡出过渡效果 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

@keyframes rotating {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}
</style> 