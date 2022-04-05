import axios from '@/request';
import base from '@/api/base.js'

const user = {

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
//---------------------------------------------------------------------------
  getUserPage(params, page) {
    return axios.post(`${base.requestUrl}/user/userPage`, params, {params: page});
  },
  getUserById(params) {
    return axios.get(`${base.requestUrl}/user/getUserById/` + params);
  },
  addUser(params) {
    return axios.post(`${base.requestUrl}/user/add`, params);
  },
  editUser(params) {
    return axios.post(`${base.requestUrl}/user/edit`, params);
  },
  setPermissions(params) {
    return axios.post(`${base.requestUrl}/user/setPermissions`, params);
  },
  delUser(params) {
    return axios.get(`${base.requestUrl}/user/del/` + params);
  },
  delUserByIds(params) {
    return axios.post(`${base.requestUrl}/user/delUserByIds`, params);
  },
  resetPassword(params) {
    return axios.post(`${base.requestUrl}/user/resetPassword`, params);
  },
  //---------------------------------------------------------------------------
  getInfo() {
    return axios.get(`${base.requestUrl}/userinfo/getUserInfo`);
  },
  changeAvatar(params) {
    return axios.post(`${base.requestUrl}/userinfo/changeAvatar`, params);
  },
  changeUserInfo(params) {
    return axios.post(`${base.requestUrl}/userinfo/changeUserInfo`, params);
  },
  changePassword(params) {
    return axios.post(`${base.requestUrl}/userinfo/changePassword`, params);
  }

}

export default user;
