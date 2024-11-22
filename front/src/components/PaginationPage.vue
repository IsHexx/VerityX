<template>
  <div class="demo-pagination-block">
    <el-pagination
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :page-sizes="[10, 20, 50, 100]"
      :background="true"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<script setup>
import { ref, defineProps, defineEmits } from 'vue'

const props = defineProps({
  total: {
    type: Number,
    default: 0
  }
})

const emit = defineEmits(['update:pagination'])

const currentPage = ref(1)
const pageSize = ref(10)

const handleSizeChange = (val) => {
  pageSize.value = val
  emit('update:pagination', {
    page: currentPage.value,
    pageSize: val
  })
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  emit('update:pagination', {
    page: val,
    pageSize: pageSize.value
  })
}
</script>

<style scoped>
.demo-pagination-block {
  margin-top: 20px;
  text-align: right;
}
</style>