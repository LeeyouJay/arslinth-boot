import router from './index'
import {getToken} from '@/utils/cookie'
import store from '../store'
import Layout from '@/layout'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'

NProgress.configure({showSpinner: false})

// 白名单列表
const whiteList = ['/login']

router.beforeEach((to, from, next) => {
  NProgress.start()
  if (getToken()) {
    // 已登录且要跳转的是登录页
    if (to.path === '/login') {
      next({path: '/home'})
      NProgress.done()
    } else {
      if (!store.getters.hasGetRoute) {
        store.dispatch('getUserInfo').then(() => {
          store.dispatch("getRouters").then(res => {
            res.push({
              path: '*',
              redirect: '/404',
              component: Layout,
              hidden: true,
              children: [{
                path: '404',
                name: 'Page404',
                component: () => import('../views/error-page/Page404'),
                meta: {title: '资源不存在', keepAlive: true}
              }]
            })
            router.addRoutes(res)
            next({...to, replace: true})
            NProgress.done()
          })
        }).catch(err => {
          console.log(err)
        })
      } else {
        next()
        NProgress.done()
      }
    }
  } else {
    // 在免登录白名单，直接进入
    if (whiteList.includes(to.path)) {
      next()
      NProgress.done()
    } else {
      // 否则重定向到登录页
      next(`/login?redirect=${to.fullPath}`)
      NProgress.done()
    }
  }
})
