<!-- src/components/MainContent.vue -->
<template>
  <div class="main-layout">
    <!-- 上部内容 -->
    <div class="upper-content">
      上部内容
    </div>
    <!-- 下部内容 -->
    <div class="lower-content">
      下部内容
    </div>
  </div>
</template>

<script>
export default {
  name: 'MainContent',
};
</script>

<style scoped>
.main-layout {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.upper-content {
  flex: 2; /* 上部区域占 2/3 高度 */
  background-color: #f5f5f5;
  padding: 20px;
  width: 100%;
  text-align: center;
  border-radius: 4px;
}

.lower-content {
  flex: 1; /* 下部区域占 1/3 高度 */
  background-color: #e0e0e0;
  padding: 20px;
  width: 100%;
  text-align: center;
  border-radius: 4px;
}
</style>
