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
  component: () => import('../views/Login'),
  hidden: true,
  meta: {
    title: '登录'
  }
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
    path: '/external-link',
    component: Layout,
    children: [{
      path: 'inner-link',
      name: 'InnerLink',
      component: InnerLink,
      meta: {
        title: '内链',
        icon: 'vue-dsn-icon-wendang',
        link: 'https://github.com/baimingxuan/vue-admin-design.git',
      }
    }]
  },
  {
    path: '/outlink',
    component: Layout,
    children: [{
      path: 'https://github.com/baimingxuan/vue-admin-design.git',
      name: 'Outlink',
      meta: {
        title: '外链',
        icon: 'vue-dsn-icon-wendang'
      }
    }]
  },
  {
    path: '/catalog',
    name: 'Catalog',
    component: Layout,
    meta: {
      title: '目录',
      icon: 'vue-dsn-icon-zujian'
    },
    children: [{
      path: 'child-catalog',
      name: 'ChildCatalog',
      component: ParentView,
      meta: {
        title: '子目录',
      },
      children: [{
        path: 'child-catalog1',
        name: 'ChildCatalog1',
        component: ParentView,
        meta: {
          title: '子目录1',
        },
      },
        {
          path: 'child-catalog12',
          name: 'ChildCatalog12',
          component: ParentView,
          meta: {
            title: '子目录2',
          },
        }]
    },
      {
        path: 'catalog3',
        name: 'Catalog3',
        component: ParentView,
        meta: {
          title: '目录3',
        },
      }]
  },
  {
    path: '/',
    component: Layout,
    hidden: true,
    children: [{
      path: 'user-center',
      name: 'UserCenter',
      component: () => import('../views/UserCenter'),
      meta: {
        title: '个人中心'
      }
    }]
  }
]

export default new Router({
  mode: 'history', // 去掉url中的#
  scrollBehavior: () => ({y: 0}),
  routes: constantRoutes
})
