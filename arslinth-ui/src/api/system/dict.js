import axios from '@/request';
import base from '@/api/base.js'

const dict = {

  getTypeList(params) {
    return axios.post(`${base.requestUrl}/dict/typeList`, params);
  },
  getValueList(params) {
    return axios.post(`${base.requestUrl}/dict/valueList`, params);
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
