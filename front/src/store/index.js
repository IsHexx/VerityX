import { createStore } from 'vuex';
import { login } from '@/api/auth';

export default createStore({
    state: {
        user: null,
        token: null,
    },
    mutations: {
        SET_USER(state, user) {
            state.user = user;
        },
        SET_TOKEN(state, token) {
            state.token = token;
        },
        LOGOUT(state) {
            state.user = null;
            state.token = null;
        }
    },
    actions: {
        async login({ commit }, credentials) {
            try {
                const response = await login(credentials);
                const { user, token } = response.data;
                commit('SET_USER', user);
                commit('SET_TOKEN', token);
                return true;
            } catch (error) {
                console.error('登录失败:', error);
                return false;
            }
        },
        logout({ commit }) {
            commit('LOGOUT');
        }
    }
});
