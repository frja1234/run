import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar
    noCache: true                if set true, the page will no be cached(default is false)
    affix: true                  if set true, the tag will affix in the tags-view
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
// 所有角色都有的
export const constantRoutes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: () => import('@/views/redirect/index')
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/auth-redirect',
    component: () => import('@/views/login/auth-redirect'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/error-page/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/error-page/401'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        component: () => import('@/views/dashboard/index'),
        name: 'Dashboard',
        meta: { title: '控制面板', icon: 'dashboard', affix: true }
      }
    ]
  },
  {
    path: '/updatePassword',
    component: Layout,
    hidden: true,
    redirect: '/updatePassword/index',
    children: [
      {
        path: 'index',
        component: () => import('@/views/tb-user/updatePassword'),
        name: 'updatePassword',
        meta: { title: '修改密码', icon: 'updatePassword', affix: true }
      }
    ]
  },


]

/**
 * asyncRoutes
 * the routes that need to be dynamically loaded based on user roles
 */
// 特定角色才有的
export const asyncRoutes = [
  {
    path: '/tb-task',
    component: Layout,
    children: [
      {
        path: 'index',
        component: () => import('@/views/tb-task/index'),
        name: 'tb-task',
        meta: { title: '打卡任务管理', icon: 'icon-tb-task', affix: true, roles: ['admin', 'teacher'], },

      }
    ]
  },
  {
    path: '/tb-edu',
    component: Layout,
    redirect: '/tb-edu/tb-college/index', //重定向地址，在面包屑中点击会重定向去的地址
    // hidden: true, // 不在侧边栏显示
    alwaysShow: true, //一直显示根路由
    meta: {
      title: '教研机构管理',
      icon: 'tb-edu',
      affix: true, // 如果设置为true，它则会固定在tags-view中(默认 false)
      roles: ['admin', 'teacher'],
    },
    // meta: { roles: ['admin', 'editor'] }, //你可以在根路由设置权限，这样它下面所有的子路由都继承了这个权限
    children: [{
      path: 'tb-college/index',
      component: () => import('@/views/tb-college/index'),
      name: 'tb-college',
      meta: {
        title: '学院管理',
        icon: 'tb-college',
        affix: true, // 如果设置为true，它则会固定在tags-view中(默认 false)
        roles: ['admin', 'teacher'],
      },
    },
    {
      path: 'tb-class/index',
      component: () => import('@/views/tb-class/index'),
      name: 'tb-class',
      meta: {
        title: '班级管理',
        icon: 'tb-class',
        affix: true, // 如果设置为true，它则会固定在tags-view中(默认 false)
        roles: ['admin', 'teacher'],
      },
    },]
  },
  {
    path: '/tb-user',
    component: Layout,
    redirect: '/tb-user/student/index', //重定向地址，在面包屑中点击会重定向去的地址
    // hidden: true, // 不在侧边栏显示
    alwaysShow: true, //一直显示根路由
    meta: {
      title: '用户管理',
      icon: 'peoples',
      affix: true, // 如果设置为true，它则会固定在tags-view中(默认 false)
      roles: ['admin', 'teacher'],
    },
    // meta: { roles: ['admin', 'editor'] }, //你可以在根路由设置权限，这样它下面所有的子路由都继承了这个权限
    children: [{
      path: 'student/index',
      component: () => import('@/views/tb-user/student/index'),
      name: 'tb-user/student',
      meta: {
        title: '学生管理',
        icon: 'student',
        affix: true, // 如果设置为true，它则会固定在tags-view中(默认 false)
        roles: ['admin', 'teacher'],
      },
    },
    {
      path: 'teacher/index',
      component: () => import('@/views/tb-user/teacher/index'),
      name: 'tb-user/teacher',
      meta: {
        title: '教师管理',
        icon: 'teacher',
        affix: true, // 如果设置为true，它则会固定在tags-view中(默认 false)
        roles: ['admin', 'teacher'],
      },
    },
    {
      path: 'admin/index',
      component: () => import('@/views/tb-user/admin/index'),
      name: 'tb-user/admin',
      meta: {
        title: '管理员管理',
        icon: 'admin',
        affix: true, // 如果设置为true，它则会固定在tags-view中(默认 false)
        roles: ['admin'], // 只能管理员才能看到
      },
    },]
  },
  {
    path: '/tb-sys',
    component: Layout,
    redirect: '/tb-sys/tb-role/index', //重定向地址，在面包屑中点击会重定向去的地址
    // hidden: true, // 不在侧边栏显示
    alwaysShow: true, //一直显示根路由
    meta: {
      title: '系统管理',
      icon: 'tb-sys',
      affix: true, // 如果设置为true，它则会固定在tags-view中(默认 false)
      roles: ['admin'],
    },
    // meta: { roles: ['admin', 'editor'] }, //你可以在根路由设置权限，这样它下面所有的子路由都继承了这个权限
    children: [{
      path: 'tb-role/index',
      component: () => import('@/views/tb-role/index'),
      name: 'tb-role',
      meta: {
        title: '角色管理',
        icon: 'user',
        affix: true, // 如果设置为true，它则会固定在tags-view中(默认 false)
        roles: ['admin'],
      },
    },
    {
      path: 'tb-permission/index',
      component: () => import('@/views/tb-permission/index'),
      name: 'tb-permission',
      meta: {
        title: '权限管理',
        icon: 'lock',
        affix: true, // 如果设置为true，它则会固定在tags-view中(默认 false)
        roles: ['admin'],
      },
    },]
  },
  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter () {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
