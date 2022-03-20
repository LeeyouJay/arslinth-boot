import axios from '@/request';
import base from '@/api/base.js'

const menu = {
  generateRoutes(params) {
    return axios.get(`${base.requestUrl}/menu/generateRoutes`, params);
  }

}
export default menu;
