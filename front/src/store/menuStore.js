// menuStore.js
import { ref, computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'

export const useMenuStore = () => {
  const route = useRoute()
  const router = useRouter()

  const menuItems = [
    {
      index: '1',
      icon: 'House',
      title: '测试管理',
      route: '/overview',
      subMenus: [
        { index: '1-1', title: '概览', route: '/overview' },
        { index: '1-2', title: '测试计划', route: '/testplan' },
        { index: '1-3', title: '测试用例', route: '/testplan' },
        { index: '1-4', title: '缺陷管理', route: '/testplan' },
        { index: '1-5', title: '测试报告', route: '/testplan' },
      ]
    },
    {
      index: '2',
      icon: 'Notification',
      title: '接口测试',
      route: '/apimanage',
      subMenus: [
        { index: '2-1', title: '接口管理', route: '/apimanage' },
        { index: '2-2', title: '接口自动化', route: '/apimanage' },
        { index: '2-3', title: '测试报告', route: '/apimanage' },
        { index: '2-4', title: '全局配置', route: '/apimanage' },
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

  const activeMainMenu = ref(menuItems[0].index)
  const activeSubMenu = ref(menuItems[0].subMenus[0]?.index || '')
  // 要让这个值初有变化
  // console.log('默认激活的菜单是:', activeSubMenu)
  // console.log('默认激活的子菜单是:', activeSubMenu)
  
  const currentSubMenus = computed(() => {
    // console.log('currentSubMenus被调用')
    const mainMenu = menuItems.find(item => item.index === activeMainMenu.value)
    return mainMenu ? mainMenu.subMenus : []
  })

  const setActiveMenu = (mainIndex, subIndex) => {
    // console.log('setActiveMenu被调用，参数是:', mainIndex, subIndex)
    const mainMenu = menuItems.find(item => item.index === mainIndex)
    if (!mainMenu) return
    // console.log('当前激活的菜单是:', mainMenu)
    activeMainMenu.value = mainIndex

    
    // console.log('激活的主菜单:', activeMainMenu.value); // 确保这里的值已经更新

    // 重新调用 currentSubMenus
    // console.log('currentSubMenus:', currentSubMenus.value);
    if (subIndex) {
      activeSubMenu.value = subIndex
    } else if (mainMenu.subMenus.length > 0) {
      
      activeSubMenu.value = mainMenu.subMenus[0].index
      // console.log('当前激活的子菜单是:', activeSubMenu.value)
      
    } else {
      activeSubMenu.value = ''
    }
    const targetSubMenu = mainMenu.subMenus.find(item => item.index === activeSubMenu.value)
    const targetRoute = targetSubMenu ? targetSubMenu.route : mainMenu.route
    // console.log('跳转路由到:', targetRoute)
    // console.log('跳转子路由到:', targetSubMenu)

    router.push(targetRoute)
  }

  // 监听路由变化, 加了这一句实现了左侧菜单和顶部菜单联动
  watch(() => route.path, (newPath) => {
    initializeActiveMenu()
  })
  const initializeActiveMenu = () => {
    const currentPath = route.path
    for (const mainMenu of menuItems) {
      if (currentPath === mainMenu.route || currentPath.startsWith(mainMenu.route + '/')) {
        activeMainMenu.value = mainMenu.index
        // console.log('initializeActiveMenu中activeMainMenu的值是:', activeMainMenu.value)
        const subMenu = mainMenu.subMenus.find(sub => sub.route === currentPath)
        activeSubMenu.value = subMenu ? subMenu.index : (mainMenu.subMenus[0]?.index || '')
        return
      }
    }
    // If no match found, default to the first menu item
    activeMainMenu.value = menuItems[0].index
    activeSubMenu.value = menuItems[0].subMenus[0]?.index || ''
  }
  // 初始化当前激活的菜单
  initializeActiveMenu()
  
  return {
    menuItems,
    activeMainMenu,
    activeSubMenu,
    currentSubMenus,
    setActiveMenu,
    initializeActiveMenu
  }
}