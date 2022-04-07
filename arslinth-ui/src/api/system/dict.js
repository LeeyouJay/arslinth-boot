import axios from '@/request';
import base from '@/api/base.js'

const dict = {

  getTypePage(params, page) {
    return axios.post(`${base.requestUrl}/dict/typePage`, params, {params: page});
  },
  getValuePage(params, page) {
    return axios.post(`${base.requestUrl}/dict/valuePage`, params, {params: page});
  },
  getValueList(params) {
    return axios.post(`${base.requestUrl}/dict/valueList`, params);
  },
  getTypeList() {
    return axios.get(`${base.requestUrl}/dict/typeList`);
  },
  getDictById(params) {
    return axios.get(`${base.requestUrl}/dict/getDictById/` + params);
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
  delDictByIds(params) {
    return axios.post(`${base.requestUrl}/dict/delDictByIds`, params);
  }

}
export default dict;
