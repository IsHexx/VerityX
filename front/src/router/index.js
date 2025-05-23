import { createRouter, createWebHashHistory } from 'vue-router';
import TestPlanPage from '@/views/TestManage/TestPlanPage.vue';
import TestPlanDetail from '@/views/TestManage/TestPlanDetail.vue';
import OverviewPage from '@/views/TestManage/OverviewPage.vue';
import ApiManagePage from '@/views/ApiTest/ApiManagePage.vue';
import ApiAutoTestPage from '@/views/ApiTest/ApiAutoTestPage.vue';
import ApiTestReport from '@/views/ApiTest/ApiTestReport.vue';
import TestCasePage from '@/views/TestManage/TestCasePage.vue';
import TestCaseDetail from '@/views/TestManage/TestCaseDetail.vue';
import BugManagePage from '@/views/TestManage/BugManagePage.vue';
import BugCreatePage from '@/views/TestManage/BugCreatePage.vue';
import TestReportPage from '@/views/TestManage/TestReportPage.vue';
import TestReportAuditPage from '@/components/TestReportAuditPage.vue';
import TestReportWorkflow from '@/components/TestReportWorkflow.vue';
import ProjectManagePage from '@/views/ProjectManagePage.vue';
import ApiEnvironmentPage from '@/views/ApiTest/ApiEnvironmentPage.vue';
import UiTestCasePage from '@/views/UiTest/UiTestCasePage.vue';
import UiTestSuitePage from '@/views/UiTest/UiTestSuitePage.vue';
import UiElementLocatorPage from '@/views/UiTest/UiElementLocatorPage.vue';
import UiTestExecutionPage from '@/views/UiTest/UiTestExecutionPage.vue';
import UiTestReportPage from '@/views/UiTest/UiTestReportPage.vue';
import UiTestConfigPage from '@/views/UiTest/UiTestConfigPage.vue';

// 系统设置页面
import UserPermissionPage from '@/views/SystemSettings/UserPermissionPage.vue';
import SystemParamsPage from '@/views/SystemSettings/SystemParamsPage.vue';
import IntegrationPage from '@/views/SystemSettings/IntegrationPage.vue';
import NotificationCenterPage from '@/views/SystemSettings/NotificationCenterPage.vue';
import SystemMonitorPage from '@/views/SystemSettings/SystemMonitorPage.vue';
import SystemInfoPage from '@/views/SystemSettings/SystemInfoPage.vue';

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
    path: '/testplan/:id',
    name: 'TestPlanDetail',
    component: TestPlanDetail,
    props: true
  },
  {
    path: '/bugmanage',
    name: 'BugManage',
    component: BugManagePage
  },
  {
    path: '/bug/create',
    name: 'BugCreate',
    component: BugCreatePage
  },
  {
    path: '/bug/:id',
    name: 'BugDetail',
    component: () => import('@/views/TestManage/BugDetailPage.vue'),
    props: true
  },
  {
    path: '/testcase',
    name: 'TestCase',
    component: TestCasePage
  },
  {
    path: '/testcase/:id',
    name: 'TestCaseDetail',
    component: TestCaseDetail,
    props: true
  },
  {
    path: '/testreport',
    name: 'TestReport',
    component: TestReportPage
  },
  {
    path: '/testreport/:id',
    name: 'TestReportDetail',
    component: () => import('@/views/TestManage/TestReportDetail.vue'),
    props: true
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
  {
    path: '/uitestcase',
    name: 'UiTestCase',
    component: UiTestCasePage,
    meta: { requiresMenu: true }
  },
  {
    path: '/uitestsuite',
    name: 'UiTestSuite',
    component: UiTestSuitePage,
    meta: { requiresMenu: true }
  },
  {
    path: '/uielementlocator',
    name: 'UiElementLocator',
    component: UiElementLocatorPage,
    meta: { requiresMenu: true }
  },
  {
    path: '/uitestexecution',
    name: 'UiTestExecution',
    component: UiTestExecutionPage,
    meta: { requiresMenu: true }
  },
  {
    path: '/uitestreport',
    name: 'UiTestReport',
    component: UiTestReportPage,
    meta: { requiresMenu: true }
  },
  {
    path: '/uitestconfig',
    name: 'UiTestConfig',
    component: UiTestConfigPage,
    meta: { requiresMenu: true }
  },
  // 系统设置路由
  {
    path: '/settings/user-permission',
    name: 'UserPermission',
    component: UserPermissionPage,
    meta: { requiresMenu: true }
  },
  {
    path: '/settings/system-params',
    name: 'SystemParams',
    component: SystemParamsPage,
    meta: { requiresMenu: true }
  },
  {
    path: '/settings/integration',
    name: 'Integration',
    component: IntegrationPage,
    meta: { requiresMenu: true }
  },
  {
    path: '/settings/notification',
    name: 'NotificationCenter',
    component: NotificationCenterPage,
    meta: { requiresMenu: true }
  },
  {
    path: '/settings/monitor',
    name: 'SystemMonitor',
    component: SystemMonitorPage,
    meta: { requiresMenu: true }
  },
  {
    path: '/settings/system-info',
    name: 'SystemInfo',
    component: SystemInfoPage,
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
