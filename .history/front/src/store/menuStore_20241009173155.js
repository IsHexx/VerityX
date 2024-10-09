// menuStore.js
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

export const useMenuStore = () => {
  const route = useRoute()
  const router = useRouter()

  const menuItems = [
    {
      index: '1',
      icon: 'House',
      title: '测试管理',
      route: '/test-management',
      subMenus: [
        { index: '1-1', title: '概览', route: '/test-management/overview' },
        { index: '1-2', title: '测试计划', route: '/test-management/testplan' },
        { index: '1-3', title: '测试用例', route: '/test-management/testcase' },
        { index: '1-4', title: '缺陷管理', route: '/test-management/defects' },
        { index: '1-5', title: '测试报告', route: '/test-management/reports' },
      ]
    },
    {
      index: '2',
      icon: 'Notification',
      title: '接口测试',
      route: '/api-testing',
      subMenus: []
    },
    {
      index: '3',
      icon: 'Edit',
      title: 'UI测试',
      route: '/ui-testing',
      subMenus: []
    },
    {
      index: '4',
      icon: 'Setting',
      title: '系统设置',
      route: '/settings',
      subMenus: []
    }
  ]

  const activeMainMenu = ref(menuItems[0].index)
  const activeSubMenu = ref(menuItems[0].subMenus[0].index)

  const currentSubMenus = computed(() => {
    const mainMenu = menuItems.find(item => item.index === activeMainMenu.value)
    return mainMenu ? mainMenu.subMenus : []
  })

  const setActiveMenu = (mainIndex, subIndex) => {
    activeMainMenu.value = mainIndex
    if (subIndex) {
      activeSubMenu.value = subIndex
    } else {
      // If no subIndex is provided, set to the first sub-menu
      const mainMenu = menuItems.find(item => item.index === mainIndex)
      activeSubMenu.value = mainMenu.subMenus[0]?.index
    }

    // Navigate to the corresponding route
    const targetMenu = menuItems.find(item => item.index === mainIndex)
    const targetSubMenu = targetMenu.subMenus.find(item => item.index === activeSubMenu.value)
    const targetRoute = targetSubMenu ? targetSubMenu.route : targetMenu.route
    router.push(targetRoute)
  }

  // Initialize active menu based on current route
  const initializeActiveMenu = () => {
    const currentPath = route.path
    for (const mainMenu of menuItems) {
      if (currentPath.startsWith(mainMenu.route)) {
        activeMainMenu.value = mainMenu.index
        const subMenu = mainMenu.subMenus.find(sub => sub.route === currentPath)
        if (subMenu) {
          activeSubMenu.value = subMenu.index
        }
        break
      }
    }
  }

  return {
    menuItems,
    activeMainMenu,
    activeSubMenu,
    currentSubMenus,
    setActiveMenu,
    initializeActiveMenu
  }
}
