import axios from '@/request';
import base from '@/api/base.js'

const dict = {

  getTypePage(params) {
    return axios.post(`${base.requestUrl}/dict/typePage`, params);
  },
  getValuePage(params) {
    return axios.post(`${base.requestUrl}/dict/valuePage`, params);
  },
  getValueList(){
    return axios.get(`${base.requestUrl}/dict/valueList`);
  },
  addDict(params) {
    return axios.post(`${base.requestUrl}/dict/add`, params);
  },
  editDict(params) {
    return axios.post(`${base.requestUrl}/dict/edit`, params);
  },
  delDict(params) {
    return axios.get(`${base.requestUrl}/dict/del/` + params);
  },

}
export default dict;
