import {asyncRoutes} from '@/router/routes.js'
import {constantRoutes} from '@/router'

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
    state.routes = [...constantRoutes, ...asyncRoutes]
  }
}
const actions = {
  getRouters({commit}, val) {
    return new Promise(resolve => {
      commit("SET_HASGETROUTE", true)
      commit("SET_ROUTES")
      resolve(val)
    })
  }
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
