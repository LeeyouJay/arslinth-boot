import Vue from 'vue'
import Vuex from 'vuex'
import app from './modules/app'
import tagsView from './modules/tagsView'
import routes from './modules/routes.js'
import user from './modules/user.js'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    app,
    tagsView,
    routes,
    user
  }
})

export default store
