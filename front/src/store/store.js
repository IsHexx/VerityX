export default new Vuex.Store({
    state: {
      selectedApiData: null
    },
    mutations: {
      SET_SELECTED_API_DATA(state, data) {
        state.selectedApiData = data
      }
    },
    actions: {
      setSelectedApiData({ commit }, data) {
        commit('SET_SELECTED_API_DATA', data)
      }
    }
  })