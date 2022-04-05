import Vue from 'vue'
import Router from 'vue-router'
import Layout from '@/layout'
import InnerLink from '@/layout/components/InnerLink'
import ParentView from '@/layout/components/ParentView'

Vue.use(Router)

/**
 * hidden: true                  如果设置为 true，该项菜单将不会显示在菜单栏中(默认为 false)
 * meta : {
    title: 'title'               菜单名
    icon: 'icon-name'            图标名
    fixed: true                  如果设置为 true，该项 tag 将一直存在 tag 栏中(默认为 false)
  }
 * */

export const constantRoutes = [{
  path: '/login',
  name: 'Login',
  component: () => import('../views/login'),
  hidden: true,
  meta: {
    title: '登录'
  }
},
  {
    path: '/',
    redirect: '/home',
    component: Layout,
    children: [{
      path: 'home',
      name: 'Home',
      component: () => import('../views/Home'),
      meta: {
        title: '首页',
        icon: 'vue-dsn-icon-index',
        fixed: true,
        keepAlive: true
      }
    }]
  },
  {
    path: '/',
    component: Layout,
    hidden: true,
    children: [{
      path: '403',
      name: 'Page403',
      component: () => import('../views/error-page/Page403'),
      meta: {
        title: '权限不足',
        keepAlive: true
      }
    }]
  },
  {
    path: '/',
    component: Layout,
    hidden: true,
    children: [{
      path: 'user-center',
      name: 'UserCenter',
      component: () => import('../views/system/userinfo'),
      meta: {
        title: '个人中心'
      }
    }]
  }
]

export default new Router({
  mode: 'history', // 去掉url中的#
  scrollBehavior: () => ({
    y: 0
  }),
  routes: constantRoutes
})
