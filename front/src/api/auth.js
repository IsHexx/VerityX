import axios from 'axios';

export function login(data) {
    return axios.post('/user/login', data);
}
