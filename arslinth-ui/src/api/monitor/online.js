import axios from '@/request';
import base from '@/api/base.js'

const online = {
  onlineUserPage(params, page) {
    return axios.post(`${base.requestUrl}/server/onlineUserPage`, params, {params: page});
  },
  forceLogout(params) {
    return axios.get(`${base.requestUrl}/server/forceLogout/` + params);
  }
}
export default online;
