import {constantRoutes} from '@/router'
import Layout from '@/layout'
import InnerLink from '@/layout/components/InnerLink'
import ParentView from '@/layout/components/ParentView'

import menu from '@/api/system/menu'


const state = {
  hasGetRoute: false,
  routes: [],
}

const getters = {
  hasGetRoute: () => state.hasGetRoute,
  routes: () => state.routes
}

const mutations = {
  SET_HASGETROUTE(state, val) {
    state.hasGetRoute = val
  },
  SET_ROUTES(state, val) {
    state.routes = [...constantRoutes, ...val]
  }
}
const actions = {
  getRouters({commit}, val) {
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
        console.log(routes)
        const result = filterChildren(routes)
        commit("SET_HASGETROUTE", true)
        commit("SET_ROUTES", result)
        resolve(result)
      })
    })
  }
}

function filterChildren(routes) {
  return routes.filter(route => {
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
      route.children = filterChildren(route.children)
    } else {
      delete route['children']
    }
    return true
  })
}


export const loadView = (view) => {
  return (resolve) => require([`@/views/${view}`], resolve)
}

export default {
  namespace: true,
  state,
  getters,
  actions,
  mutations
}
