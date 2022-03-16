import Vue from 'vue'
import App from './App'
import router from './router'
import store from './store'
import api from './api'

import './router/auth'

import './assets/icon-fonts/iconfont.css'
import './assets/icon-fonts/iconfont'
import 'element-ui/lib/theme-chalk/index.css'
import ElementUI from 'element-ui'

Vue.use(ElementUI, {size: 'small'})

Vue.prototype.$api = api;
Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
