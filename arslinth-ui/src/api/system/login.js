import axios from '@/request';
import base from '@/api/base.js'

const login = {

//获取滑动解锁图片
  getCaptcha() {
    return axios.get(`${base.requestUrl}/slider/image`);
  },
  //账号密码登入
  login(params) {
    return axios.post(`${base.requestUrl}/user/login`, params);
  },

  //登出
  logout() {
    return axios.get(`${base.requestUrl}/user/logout`);
  },
}

export default login;
