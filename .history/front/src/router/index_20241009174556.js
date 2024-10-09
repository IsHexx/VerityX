import { createRouter, createWebHistory } from 'vue-router';
import UserLogin from '@/components/UserlLogin.vue';
import HomePage from '@/views/HomePage.vue';
import TestPlanPage from '@/views/TestManage/TestPlanPage.vue';
import OverviewPage from '@/views/TestManage/OverviewPage.vue';
import ApiManagePage from '@/views/ApiTest/ApiManagePage.vue';

ApiManagePage
const routes = [
  {
    path: '/login',
    name: 'Login',
    component: UserLogin
  },
  {
    path: '/home',  // 新增的导航页面路由
    name: 'Home',
    component: HomePage
  },
  {
    path: '/home/testplan',
    name: 'TestPlan',
    component: TestPlanPage, // 添加 TestPlanPage 的路由
  },
  {
    path: '/home/overview',
    name: 'Overview',
    component: OverviewPage, // 添加 OverviewPage 的路由
  },
  {
    path: '/apimanage',
    name: 'Apimanage',
    component: ApiManagePage, // 添加 OverviewPage 的路由
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
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
