import Vue from 'vue'
import Vuex from 'vuex'
import app from './modules/app'
import tagsView from './modules/tagsView'
import permissions from './modules/permission.js'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    app,
    tagsView,
    permissions
  }
})

export default store
