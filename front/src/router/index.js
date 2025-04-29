import { createRouter, createWebHashHistory } from 'vue-router';
import TestPlanPage from '@/views/TestManage/TestPlanPage.vue';
import OverviewPage from '@/views/TestManage/OverviewPage.vue';
import ApiManagePage from '@/views/ApiTest/ApiManagePage.vue';
import ApiAutoTestPage from '@/views/ApiTest/ApiAutoTestPage.vue';
import ApiTestReport from '@/views/ApiTest/ApiTestReport.vue';
import TestCasePage from '@/views/TestManage/TestCasePage.vue';
import BugManagePage from '@/views/TestManage/BugManagePage.vue';
import TestReportPage from '@/views/TestManage/TestReportPage.vue';
import TestReportAuditPage from '@/components/TestReportAuditPage.vue';
import TestReportWorkflow from '@/components/TestReportWorkflow.vue';
import ProjectManagePage from '@/views/ProjectManagePage.vue';
import ApiEnvironmentPage from '@/views/ApiTest/ApiEnvironmentPage.vue';

// 预加载关键组件，提高性能
const UserLogin = () => import(/* webpackChunkName: "user" */ '@/components/UserLogin.vue')

// 路由配置
const routes = [
  {
    path: '/',
    redirect: '/overview'
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/components/UserLogin.vue'),
    meta: { requiresMenu: false } // 标记不需要菜单导航
  },
  {
    path: '/projectmanage',
    name: 'ProjectManage',
    component: ProjectManagePage
  },
  {
    path: '/testreport/audit',
    name: 'TestReportAudit',
    component: TestReportAuditPage
  },
  {
    path: '/overview',
    name: 'Overview',
    component: OverviewPage,
    meta: { requiresMenu: true }
  },
  {
    path: '/testplan',
    name: 'TestPlan',
    component: TestPlanPage
  },
  {
    path: '/bugmanage',
    name: 'BugManage',
    component: BugManagePage
  },
  {
    path: '/testcase',
    name: 'TestCase',
    component: TestCasePage
  },
  {
    path: '/testreport',
    name: 'TestReport',
    component: TestReportPage
  },
  {
    path: '/apimanage',
    name: 'Apimanage',
    component: ApiManagePage,
    meta: { requiresMenu: true }
  },
  {
    path: '/apiautotest',
    name: 'ApiAutoTest',
    component: ApiAutoTestPage
  },
  {
    path: '/testreport/workflow',
    name: 'TestReportWorkflow',
    component: TestReportWorkflow
  },
  {
    path: '/apitestreport',
    name: 'ApiTestReport',
    component: ApiTestReport,
    meta: { requiresMenu: true }
  },
  {
    path: '/apienvironment',
    name: 'ApiEnvironment',
    component: ApiEnvironmentPage,
    meta: { requiresMenu: true }
  },
];

// 创建路由实例
const router = createRouter({
  history: createWebHashHistory(),
  routes,
  // 添加滚动行为，提高页面跳转体验
  scrollBehavior() {
    return { top: 0 }
  }
});

// 优化的路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token');
  const lastPath = localStorage.getItem('lastPath');

  // 未登录且尝试访问需要登录的页面
  if (!token && to.path !== '/login') {
    // 保存尝试访问的路径，但不包括根路径和登录页
    if (to.path !== '/' && to.path !== '/login') {
      localStorage.setItem('lastPath', to.path);
    }
    next('/login');
    return;
  } 
  
  // 已登录但访问登录页
  if (token && to.path === '/login') {
    next(lastPath || '/overview');
    return;
  }
  
  // 访问根路径
  if (to.path === '/' && token) {
    // 避免重定向循环
    if (!from.path || from.path === '/') {
      next(lastPath || '/overview');
      return;
    }
  }
  
  // 其他情况正常放行
  next();
});

export default router;
