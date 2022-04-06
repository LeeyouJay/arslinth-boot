import {constantRoutes} from '@/router'
import Layout from '@/layout'
import InnerLink from '@/layout/components/InnerLink'
import ParentView from '@/layout/components/ParentView'
import utils from '@/utils/index.js'

import menu from '@/api/system/menu'


const state = {
  hasGetRoute: false,
  routes: [],
  rdata: []
}

const getters = {
  hasGetRoute: () => state.hasGetRoute,
  routes: () => state.routes,
  rdata: () => state.rdata
}

const mutations = {
  SET_HASGETROUTE(state, val) {
    state.hasGetRoute = val
  },
  SET_ROUTES(state, val) {
    state.routes = [...constantRoutes, ...val]
  },
  SET_RDATA(state, val) {
    state.rdata = utils.treeToArray(val)
  }
}
const actions = {
  getRouters({commit}, args) {
    return new Promise(resolve => {
      menu.generateRoutes().then(res => {
        let routes = JSON.parse(JSON.stringify(res.data.routes))
        routes = routes.map(val => {
          if (val.parentId == '0' && val.menuType != 'M') {
            let main = {
              path: '/',
              component: 'Layout',
              children: []
            }
            main.children.push(val)
            return main
          } else {
            return val
          }
        })
        const sdata = utils.deepClone(routes)
        const rdata = utils.deepClone(routes)
        // const sidebarRoutes = filterChildren(routes)
        const sidebarRoutes = filterAsyncRouter(sdata)
        const rewriteRoutes = filterAsyncRouter(rdata, false, true)

        commit("SET_HASGETROUTE", true)
        commit("SET_ROUTES", sidebarRoutes)
        commit("SET_RDATA", routes)
        resolve(rewriteRoutes)
      })
    })
  }
}

// 遍历后台传来的路由字符串，转换为组件对象
function filterAsyncRouter(asyncRouterMap, lastRouter = false, type = false) {
  return asyncRouterMap.filter(route => {
    if (type && route.children) {
      route.children = filterChildren(route.children)
    }
    if (route.component) {
      if (route.component === 'Layout') {
        route.component = Layout
      } else if (route.component === 'ParentView') {
        route.component = ParentView
      } else if (route.component === 'InnerLink') {
        route.component = InnerLink
      } else {
        route.component = loadView(route.component)
      }
    }
    if (route.children != null && route.children && route.children.length) {
      route.children = filterAsyncRouter(route.children, route, type)
    } else {
      delete route['children']
      delete route['redirect']
    }
    return true
  })
}

function filterChildren(childrenMap, lastRouter = false) {
  var children = []
  childrenMap.forEach((el, index) => {
    if (el.children && el.children.length) {
      if (el.component === 'ParentView' && !lastRouter) {
        el.children.forEach(c => {
          c.path = el.path + '/' + c.path
          if (c.children && c.children.length) {
            children = children.concat(filterChildren(c.children, c))
            return
          }
          children.push(c)
        })
        return
      }
    }
    if (lastRouter) {
      el.path = lastRouter.path + '/' + el.path
    }
    children = children.concat(el)
  })
  return children
}

// function filterChildren(routes) {
//   return routes.filter(route => {
//     if (route.component) {
//       if (route.component === 'Layout') {
//         route.component = Layout
//       } else if (route.component === 'ParentView') {
//         route.component = ParentView
//       } else if (route.component === 'InnerLink') {
//         route.component = InnerLink
//       } else {
//         route.component = loadView(route.component)
//       }
//     }
//     if (route.children != null && route.children && route.children.length) {
//       route.children = filterChildren(route.children)
//     } else {
//       delete route['children']
//     }
//     return true
//   })
// }
function loadView(view) {
  return (resolve) => require([`@/views/${view}`], resolve)
}

export default {
  namespace: true,
  state,
  getters,
  actions,
  mutations
}
