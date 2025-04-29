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
        { index: '1-3', title: '测试用例', route: '/testcase' },
        { index: '1-4', title: '缺陷管理', route: '/bugmanage' },
        { index: '1-5', title: '测试报告', route: '/testreport' },
      ]
    },
    {
      index: '2',
      icon: 'Notification',
      title: '接口测试',
      route: '/apimanage',
      subMenus: [
        { index: '2-1', title: '接口管理', route: '/apimanage' },
        { index: '2-2', title: '接口自动化', route: '/apiautotest' },
        { index: '2-3', title: '测试报告', route: '/apitestreport' },
        { index: '2-4', title: '全局配置', route: '/apienvironment' },
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
    },
    {
      index: '5',
      title: '',
      route: '/projectmanage',
      subMenus: [
      ]
    },
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


  
  // 添加登录页面路径判断
  const isLoginPage = (path) => {
    return path === '/login'
  }
  const setActiveMenu = (mainIndex, subIndex) => {
    // 如果是登录页面，不执行菜单激活逻辑
    if (isLoginPage(route.path)) return
    
    const mainMenu = menuItems.find(item => item.index === mainIndex)
    if (!mainMenu) return
    
    activeMainMenu.value = mainIndex
    
    // 设置子菜单索引
    if (subIndex) {
      activeSubMenu.value = subIndex
    } else if (mainMenu.subMenus.length > 0) {
      activeSubMenu.value = mainMenu.subMenus[0].index
    } else {
      activeSubMenu.value = ''
    }
    
    // 找到对应的路由
    const targetSubMenu = mainMenu.subMenus.find(item => item.index === activeSubMenu.value)
    const targetRoute = targetSubMenu ? targetSubMenu.route : mainMenu.route
    
    // 如果目标路由与当前路由不同，则跳转
    // 但不在页面刷新时强制跳转
    if (targetRoute !== route.path) {
      // 更新 localStorage 中的 lastPath
      if (targetRoute !== '/' && targetRoute !== '/login') {
        localStorage.setItem('lastPath', targetRoute)
      }
      router.push(targetRoute)
    }
  }

  const shouldHandleMenu = computed(() => {
    return route.meta.requiresMenu !== false
  })
  
  // 监听路由变化是否不需要实时调用 initializeActiveMenu
  watch(() => route.path, (newPath) => {
    if (!isLoginPage(newPath) && shouldUpdateMenu.value) {
      initializeActiveMenu()
    }
  })
  
  // 控制是否应该响应路由变化来更新菜单
  const shouldUpdateMenu = ref(true)
  
  const initializeActiveMenu = () => {
    // 如果是登录页面，不执行初始化菜单逻辑
    if (isLoginPage(route.path)) return
    
    const currentPath = route.path
    
    if (currentPath === '/') {
      // 如果是根路径，不做菜单激活处理，避免干扰
      return
    }
    
    // 首先尝试直接匹配路径
    let foundMainMenu = null
    let foundSubMenu = null
    
    // 直接查找完全匹配的子菜单
    for (const mainMenu of menuItems) {
      const subMenu = mainMenu.subMenus.find(sub => sub.route === currentPath)
      if (subMenu) {
        foundMainMenu = mainMenu
        foundSubMenu = subMenu
        break
      }
    }
    
    // 如果没找到完全匹配的子菜单，尝试匹配主菜单
    if (!foundMainMenu) {
      foundMainMenu = menuItems.find(menu => menu.route === currentPath)
    }
    
    // 如果找到匹配项，设置激活菜单
    if (foundMainMenu) {
      activeMainMenu.value = foundMainMenu.index
      if (foundSubMenu) {
        activeSubMenu.value = foundSubMenu.index
      } else if (foundMainMenu.subMenus.length > 0) {
        activeSubMenu.value = foundMainMenu.subMenus[0].index
      } else {
        activeSubMenu.value = ''
      }
    } else {
      // 如果没有找到任何匹配，默认选中第一个菜单
      activeMainMenu.value = menuItems[0].index
      activeSubMenu.value = menuItems[0].subMenus[0]?.index || ''
    }
  }
  // 初始化当前激活的菜单
  initializeActiveMenu()
  
  return {
    menuItems,
    activeMainMenu,
    activeSubMenu,
    currentSubMenus,
    setActiveMenu,
    initializeActiveMenu,
    shouldUpdateMenu
  }
}