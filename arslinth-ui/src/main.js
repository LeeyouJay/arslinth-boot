import Vue from 'vue'
import App from './App'
import router from './router'
import store from './store'
import api from './api'
import utils from './utils/index.js'
import base from './api/base.js'

import 'default-passive-events'
import './router/auth'
import './assets/icon-fonts/iconfont.css'
import './assets/icon-fonts/iconfont'
import 'element-ui/lib/theme-chalk/index.css'

import ElementUI from 'element-ui'

import Pagination from '@/components/Pagination'
import RightToolbar from "@/components/RightToolbar"

Vue.use(ElementUI, {size: 'small'})

Vue.component('Pagination', Pagination)
Vue.component('RightToolbar', RightToolbar)

Vue.prototype.$requestUrl = base.requestUrl
Vue.prototype.$api = api
Vue.prototype.$utils = utils
Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
