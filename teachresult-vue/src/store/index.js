import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)
export default new Vuex.Store({
  state: {user: {username: sessionStorage.getItem('username') == null ? '' : sessionStorage.getItem('username'), userid: sessionStorage.getItem('userid') == null ? '' : sessionStorage.getItem('userid')}},
  mutations: {
    login (state, user) {
      state.user = user
      sessionStorage.setItem('username', JSON.stringify(user.username))
      sessionStorage.setItem('userid', JSON.stringify(user.userid))
    }
  }
})
