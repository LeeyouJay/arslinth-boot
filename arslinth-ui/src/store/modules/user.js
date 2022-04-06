import user from '@/api/system/user'
import base from '@/api/base.js'

const state = {
  userinfo: {
    avatar: "",
    username: '',
    nickName: '',
    permissions: []
  },
  permissions:[]
}

const getters = {
  userinfo: () => state.userinfo,
  permissions: ()=> state.permissions
}

const mutations = {
  SET_USERINFO(state, val) {
    state.userinfo = val
  },
  SET_PERMISSIONS(state, val) {
    state.permissions = val
  }
}
const actions = {
  getUserInfo({commit}, args) {
    return new Promise((resolve, reject) => {
      user.getInfo().then(res => {
        if (res.code == 200) {
          let user = res.data.user
          user.avatar = user.avatar ? (base.requestUrl + user.avatar) : require("@/assets/img/avatar.png")
          commit('SET_USERINFO', user)
          commit('SET_PERMISSIONS',user.permissions)
          resolve(res)
        } else {
          reject(res)
        }
      })
    }).catch(error => {
      reject(error)
    })
  }
}

export default {
  namespace: true,
  state,
  getters,
  actions,
  mutations
}
