import axios from '@/utils/request';
import base from '@/api/base.js'

const login = {

  //账号密码登入
  login(params) {
    return axios.post(`${base.localUrl}/user/login`, params);
  },

  //登出
  logout() {
    return axios.get(`${base.localUrl}/user/logout`);
  },
}

export default login;
