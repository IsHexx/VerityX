// src/auth.js
import { reactive } from 'vue';

export const authState = reactive({
  isAuthenticated: !!localStorage.getItem('token'), // 根据本地存储初始化
});
