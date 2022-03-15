import axios from '@/utils/request';
import base from '@/api/base.js'

const menu = {

  getMenuList(params) {
    return axios.post(`${base.localUrl}/menu/list`, params);
  }

}
export default menu;
