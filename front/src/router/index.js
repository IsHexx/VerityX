import { createRouter, createWebHistory } from 'vue-router';
import UserLogin from '@/components/UserLogin.vue';
import TestPlanPage from '@/views/TestManage/TestPlanPage.vue';
import OverviewPage from '@/views/TestManage/OverviewPage.vue';
import ApiManagePage from '@/views/ApiTest/ApiManagePage.vue';
import ApiAutoTestPage from '@/views/ApiTest/ApiAutoTestPage.vue';
import TestCasePage from '@/views/TestManage/TestCasePage.vue';
import BugManagePage from '@/views/TestManage/BugManagePage.vue';
import TestReportPage from '@/views/TestManage/TestReportPage.vue';
import TestReportAuditPage from '@/components/TestReportAuditPage.vue';
import ProjectManagePage from '@/views/ProjectManagePage.vue';

const routes = [
  {
    path: '/',
    name: 'Login',
    component: UserLogin
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
  },
  {
    path: '/overview/testplan',
    name: 'TestPlan',
    component: TestPlanPage
  },
  {
    path: '/overview/bugmanage',
        name: 'BugManage',
        component: BugManagePage
      },
  {
    path: '/overview/testcase',
    name: 'TestCase',
    component: TestCasePage
  },
  {
    path: '/overview/testreport',
    name: 'TestReport',
    component: TestReportPage
  },
  {
    path: '/apimanage',
    name: 'Apimanage',
    component: ApiManagePage,
  },
  {
    path: '/apimanage/apiautotest',
    name: 'ApiAutoTest',
    component: ApiAutoTestPage
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
