import { createRouter, createWebHistory } from 'vue-router';
import UserLogin from '@/components/UserlLogin.vue';


const routes = [
  {
    path: '/login',
    name: 'Login',
    component: UserLogin
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

// 路由守卫，检查登录状态
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token');
  if (to.matched.some(record => record.meta.requiresAuth) && !token) {
    next({ name: 'Login' });
  } else {
    next();
  }
});

export default router;
