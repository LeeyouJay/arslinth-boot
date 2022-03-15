const state = {
  visitedViews: [],
  cachedViews: []
}

const getters = {
  visitedViews: (state, getters) => state.visitedViews,
  cachedViews: (state) => state.cachedViews
}

const mutations = {
  addVisitedView(state, payload) {
    if (state.visitedViews.some(item => item.path === payload.path)) return
    state.visitedViews.push(
      Object.assign({}, payload, {
        title: payload.meta.title || 'no-title'
      })
    )
  },
  delVisitedView(state, payload) {
    const index = state.visitedViews.findIndex(item => {
      return item.path === payload.path
    })
    state.visitedViews.splice(index, 1)
  },
  delAllVisitedView(state, payload) {
    const fixedTags = state.visitedViews.filter(item => item.meta.fixed)
    state.visitedViews = fixedTags
  },
  delOthersVisitedView(state, payload) {
    state.visitedViews = state.visitedViews.filter(item => {
      return item.meta.fixed || item.path === payload.path
    })
  },
  addCachedViews(state, payload) {
    if (state.cachedViews.includes(payload.name)) return false
    if (payload.meta.keepAlive) {
      state.cachedViews.push(payload.name)
    }
  },
  delCachedViews(state, payload) {
    const index = state.cachedViews.indexOf(payload.name)
    index > -1 && state.cachedViews.splice(index, 1)
  },
  delAllCachedViews(state, payload) {
    state.cachedViews = []
  },
  delOthersCachedViews(state, payload) {
    const index = state.cachedViews.indexOf(payload.name)
    if (index > -1) {
      state.cachedViews = state.cachedViews.slice(index, index + 1)
    } else {
      state.cachedViews = []
    }
  }
}

export default {
  namespace: true,
  state,
  getters,
  mutations
}
