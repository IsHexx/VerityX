<template>
    <div class="demo-pagination-block">
      <el-pagination
        :current-page="currentPage4"
        :page-size="pageSize4"
        :page-sizes="[100, 200, 300, 400]"
        :size="size"
        :disabled="disabled"
        :background="background"
        layout="total, sizes, prev, pager, next, jumper"
        :total="400"
        @update:current-page="currentPage4 = $event"
        @update:page-size="pageSize4 = $event"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue'
  

  const currentPage4 = ref(4)
  const pageSize4 = ref(100)
  const size = ref('default')
  const background = ref(false)
  const disabled = ref(false)
  
  const handleSizeChange = (val) => {
    console.log(`${val} items per page`)
  }
  const handleCurrentChange = (val) => {
    console.log(`current page: ${val}`)
  }
  </script>
  
  <style scoped>
  .demo-pagination-block + .demo-pagination-block {
    margin-top: 10px;
  }
  .demo-pagination-block .demonstration {
    margin-bottom: 16px;
  }
  </style>