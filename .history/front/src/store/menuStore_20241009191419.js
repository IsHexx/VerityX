// menuStore.js
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

/**
 * 使用菜单存储
 * 该函数用于管理侧边栏菜单的状态，包括当前激活的主菜单和子菜单
 * 它还计算当前菜单项和设置活动菜单的逻辑
 */
export const useMenuStore = () => {
  // 获取当前路由信息
  const route = useRoute()
  // 获取路由器实例
  const router = useRouter()

  // 定义菜单项数组，包含主菜单及其子菜单
  const menuItems = [
    {
      index: '1',
      icon: 'House',
      title: '测试管理',
      route: '/overview',
      subMenus: [
        { index: '1-1', title: '概览', route: '/overview' },
        { index: '1-2', title: '测试计划', route: '/testplan' },
      ]
    },
    {
      index: '2',
      icon: 'Notification',
      title: '接口测试',
      route: '/apimanage',
      subMenus: [
        { index: '2-1', title: '测试', route: '/apimanage' },
      ]
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

  // 初始化激活的主菜单和子菜单索引
  const activeMainMenu = ref(menuItems[0].index)
  const activeSubMenu = ref(menuItems[0].subMenus[0]?.index || '')

  // 计算当前主菜单的子菜单项
  const currentSubMenus = computed(() => {
    const mainMenu = menuItems.find(item => item.index === activeMainMenu.value)
    
    return mainMenu ? mainMenu.subMenus : []
  })

  /**
   * 设置活动菜单和子菜单
   * @param {string} mainIndex 主菜单索引
   * @param {string} subIndex 子菜单索引
   */
  const setActiveMenu = (mainIndex, subIndex) => {
    const mainMenu = menuItems.find(item => item.index === mainIndex)
    if (!mainMenu) return

    activeMainMenu.value = mainIndex
    
    if (subIndex) {
      activeSubMenu.value = subIndex
    } else if (mainMenu.subMenus.length > 0) {
      activeSubMenu.value = mainMenu.subMenus[0].index
    } else {
      activeSubMenu.value = ''
    }

    const targetSubMenu = mainMenu.subMenus.find(item => item.index === activeSubMenu.value)
    const targetRoute = targetSubMenu ? targetSubMenu.route : mainMenu.route
    router.push(targetRoute)
  }

  /**
   * 初始化激活菜单
   * 根据当前路由初始化激活的主菜单和子菜单
   */
  const initializeActiveMenu = () => {
    const currentPath = route.path
    for (const mainMenu of menuItems) {
      if (currentPath === mainMenu.route || currentPath.startsWith(mainMenu.route + '/')) {
        activeMainMenu.value = mainMenu.index
        const subMenu = mainMenu.subMenus.find(sub => sub.route === currentPath)
        activeSubMenu.value = subMenu ? subMenu.index : (mainMenu.subMenus[0]?.index || '')
        return
      }
    }
    // If no match found, default to the first menu item
    activeMainMenu.value = menuItems[0].index
    activeSubMenu.value = menuItems[0].subMenus[0]?.index || ''
  }

  // 返回菜单相关状态和方法
  return {
    menuItems,
    activeMainMenu,
    activeSubMenu,
    currentSubMenus,
    setActiveMenu,
    initializeActiveMenu
  }
}