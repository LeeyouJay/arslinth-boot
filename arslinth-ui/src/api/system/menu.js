import axios from '@/request';
import base from '@/api/base.js'

const menu = {
  generateRoutes() {
    return axios.get(`${base.requestUrl}/menu/generateRoutes`);
  },
  addMenu(params) {
    return axios.post(`${base.requestUrl}/menu/add`, params);
  },
  delMenu(params) {
    return axios.get(`${base.requestUrl}/menu/del/` + params);
  },
  editMenu(params) {
    return axios.post(`${base.requestUrl}/menu/edit`, params);
  },
  getMenuList(params) {
    return axios.get(`${base.requestUrl}/menu/list/` + params);
  },
  getMenuById(params) {
    return axios.get(`${base.requestUrl}/menu/getMenuById/` + params);
  }
}
export default menu;
