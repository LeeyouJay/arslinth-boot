import axios from '@/request';
import base from '@/api/base.js'

const log = {
  operLogPage(params) {
    return axios.post(`${base.requestUrl}/log/operLogPage`, params);
  },
  loginLogPage(params) {
    return axios.post(`${base.requestUrl}/log/loginLogPage`, params);
  },
  delAllOperLog(){
     return axios.get(`${base.requestUrl}/log/delAllOperLog`);
  },
  delAllLoginLog(){
     return axios.get(`${base.requestUrl}/log/delAllLoginLog`);
  },
  delOperLog(params){
    return axios.post(`${base.requestUrl}/log/delOperLog`, params);
  },
  delLoginLog(params){
    return axios.post(`${base.requestUrl}/log/delLoginLog`, params);
  }
}
export default log;