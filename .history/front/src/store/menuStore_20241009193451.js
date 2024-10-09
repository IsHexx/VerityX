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

  const activeMainMenu = ref(menuItems[0].index)
  const activeSubMenu = ref(menuItems[0].subMenus[0]?.index || '')

  const currentSubMenus = computed(() => {
    const mainMenu = menuItems.find(item => item.index === activeMainMenu.value)
    
    return mainMenu ? mainMenu.subMenus : []
  })

  const setActiveMenu = (mainIndex) => {
    console.log('setActiveMenu:', mainIndex)
    const mainMenu = menuItems.find(item => item.index === mainIndex)
    if (!mainMenu) return

    activeMainMenu.value = mainIndex
    
    if (mainMenu.subMenus.length > 0) {
      // If the menu has submenus, select the first submenu
      activeSubMenu.value = mainMenu.subMenus[0].index
      router.push(mainMenu.subMenus[0].route)
    } else {
      // If the menu has no submenus, clear the activeSubMenu and navigate to the main menu route
      activeSubMenu.value = ''
      router.push(mainMenu.route)
    }
  }

  const setActiveSubMenu = (subIndex) => {
    console.log('setActiveSubMenu:', subIndex)
    const mainMenu = menuItems.find(item => item.index === activeMainMenu.value)
    if (!mainMenu) return

    const subMenu = mainMenu.subMenus.find(sub => sub.index === subIndex)
    if (subMenu) {
      activeSubMenu.value = subIndex
      router.push(subMenu.route)
    }
  }

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

  return {
    menuItems,
    activeMainMenu,
    activeSubMenu,
    currentSubMenus,
    setActiveMenu,
    setActiveSubMenu,
    initializeActiveMenu
  }
}