<template>
  <div class="screenshot-comparison">
    <el-card class="comparison-card" v-if="screenshots.length > 0">
      <template #header>
        <div class="card-header">
          <span>截图对比</span>
          <div class="comparison-controls">
            <el-radio-group v-model="comparisonMode" size="small">
              <el-radio-button label="slider">滑块对比</el-radio-button>
              <el-radio-button label="sideBySide">并排对比</el-radio-button>
              <el-radio-button label="fade">透明度对比</el-radio-button>
            </el-radio-group>
          </div>
        </div>
      </template>
      
      <div class="no-screenshots" v-if="filteredScreenshots.length === 0">
        <el-empty description="没有可用的截图对比" />
      </div>
      
      <div v-else>
        <el-row style="margin-bottom: 15px">
          <el-col :span="24">
            <el-select
              v-model="selectedScreenshotPair"
              placeholder="选择截图对比"
              style="width: 100%"
            >
              <el-option
                v-for="(item, index) in filteredScreenshots"
                :key="index"
                :label="item.title"
                :value="index"
              />
            </el-select>
          </el-col>
        </el-row>
        
        <el-row>
          <el-col :span="24">
            <!-- 滑块对比模式 -->
            <div class="comparison-container slider-comparison" v-if="comparisonMode === 'slider' && selectedPair">
              <div class="comparison-wrapper">
                <img :src="selectedPair.expectedImage" class="base-image" />
                <div class="comparison-slider" :style="{ width: `${sliderPosition}%` }">
                  <img :src="selectedPair.actualImage" class="comparison-image" />
                </div>
                <div class="slider-handle" :style="{ left: `${sliderPosition}%` }" @mousedown="startDragging"></div>
                <div class="image-label left-label">期望结果</div>
                <div class="image-label right-label">实际结果</div>
              </div>
              <el-slider v-model="sliderPosition" :min="0" :max="100" :step="1" />
            </div>
            
            <!-- 并排对比模式 -->
            <div class="comparison-container side-by-side" v-if="comparisonMode === 'sideBySide' && selectedPair">
              <el-row :gutter="10">
                <el-col :span="12">
                  <div class="image-container">
                    <div class="image-title">期望结果</div>
                    <el-image 
                      :src="selectedPair.expectedImage" 
                      fit="contain"
                      class="comparison-image"
                      :preview-src-list="[selectedPair.expectedImage]"
                    />
                  </div>
                </el-col>
                <el-col :span="12">
                  <div class="image-container">
                    <div class="image-title">实际结果</div>
                    <el-image 
                      :src="selectedPair.actualImage" 
                      fit="contain"
                      class="comparison-image"
                      :preview-src-list="[selectedPair.actualImage]"
                    />
                  </div>
                </el-col>
              </el-row>
            </div>
            
            <!-- 透明度对比模式 -->
            <div class="comparison-container fade-comparison" v-if="comparisonMode === 'fade' && selectedPair">
              <div class="comparison-wrapper">
                <img :src="selectedPair.expectedImage" class="base-image" />
                <img :src="selectedPair.actualImage" class="overlay-image" :style="{ opacity: fadeOpacity }" />
                <div class="image-label center-label">
                  透明度: {{ Math.round(fadeOpacity * 100) }}%
                </div>
              </div>
              <el-slider v-model="fadeOpacity" :min="0" :max="1" :step="0.01" />
            </div>
            
            <div class="comparison-details" v-if="selectedPair">
              <el-descriptions :column="1" border size="small">
                <el-descriptions-item label="步骤名称">{{ selectedPair.title }}</el-descriptions-item>
                <el-descriptions-item label="比对结果">
                  <el-tag :type="selectedPair.diffPercentage > 5 ? 'danger' : 'success'">
                    差异率: {{ selectedPair.diffPercentage.toFixed(2) }}%
                  </el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="备注" v-if="selectedPair.description">
                  {{ selectedPair.description }}
                </el-descriptions-item>
              </el-descriptions>
            </div>
          </el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { defineProps, ref, computed, onMounted, watch } from 'vue';

const props = defineProps({
  screenshots: {
    type: Array,
    required: true,
    default: () => []
  }
});

// 对比模式
const comparisonMode = ref('slider');
const selectedScreenshotPair = ref(0);
const sliderPosition = ref(50);
const fadeOpacity = ref(0.5);
const isDragging = ref(false);

// 筛选有效的截图对比（需要有期望和实际截图）
const filteredScreenshots = computed(() => {
  return props.screenshots.filter(
    screenshot => screenshot.expectedImage && screenshot.actualImage
  );
});

// 当前选择的截图对
const selectedPair = computed(() => {
  if (filteredScreenshots.value.length === 0) return null;
  return filteredScreenshots.value[selectedScreenshotPair.value];
});

// 拖动滑块处理
const startDragging = (event) => {
  isDragging.value = true;
  document.addEventListener('mousemove', onDragging);
  document.addEventListener('mouseup', stopDragging);
  event.preventDefault();
};

const onDragging = (event) => {
  if (!isDragging.value) return;
  
  const container = event.currentTarget.querySelector('.comparison-wrapper');
  if (!container) return;
  
  const rect = container.getBoundingClientRect();
  const position = ((event.clientX - rect.left) / rect.width) * 100;
  sliderPosition.value = Math.max(0, Math.min(100, position));
};

const stopDragging = () => {
  isDragging.value = false;
  document.removeEventListener('mousemove', onDragging);
  document.removeEventListener('mouseup', stopDragging);
};

// 当筛选后的截图为空时，重置选择的索引
watch(filteredScreenshots, (newVal) => {
  if (newVal.length === 0) {
    selectedScreenshotPair.value = 0;
  } else if (selectedScreenshotPair.value >= newVal.length) {
    selectedScreenshotPair.value = newVal.length - 1;
  }
});

// 当对比模式改变时重置相关状态
watch(comparisonMode, () => {
  if (comparisonMode.value === 'slider') {
    sliderPosition.value = 50;
  } else if (comparisonMode.value === 'fade') {
    fadeOpacity.value = 0.5;
  }
});

// 组件卸载时清理事件监听
onMounted(() => {
  return () => {
    document.removeEventListener('mousemove', onDragging);
    document.removeEventListener('mouseup', stopDragging);
  };
});
</script>

<style scoped>
.comparison-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.comparison-container {
  margin-bottom: 15px;
}

.comparison-wrapper {
  position: relative;
  width: 100%;
  overflow: hidden;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.base-image, .comparison-image, .overlay-image {
  width: 100%;
  display: block;
}

.slider-comparison .comparison-slider {
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  overflow: hidden;
  border-right: 2px solid #409EFF;
}

.slider-comparison .comparison-image {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
}

.slider-handle {
  position: absolute;
  top: 0;
  width: 4px;
  height: 100%;
  background-color: #409EFF;
  cursor: ew-resize;
  transform: translateX(-50%);
}

.fade-comparison .overlay-image {
  position: absolute;
  top: 0;
  left: 0;
}

.image-label {
  position: absolute;
  background-color: rgba(0, 0, 0, 0.5);
  color: white;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.left-label {
  top: 10px;
  left: 10px;
}

.right-label {
  top: 10px;
  right: 10px;
}

.center-label {
  top: 10px;
  left: 50%;
  transform: translateX(-50%);
}

.image-container {
  position: relative;
  border: 1px solid #ddd;
  border-radius: 4px;
  overflow: hidden;
}

.image-title {
  padding: 8px;
  background-color: #f5f7fa;
  text-align: center;
  font-weight: bold;
  border-bottom: 1px solid #ddd;
}

.comparison-image {
  width: 100%;
  max-height: 400px;
}

.comparison-details {
  margin-top: 15px;
}

.no-screenshots {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 200px;
}
</style> 