import axios from '@/request';
import base from '@/api/base.js'

const role = {
  getRolePage(params, page) {
    return axios.post(`${base.requestUrl}/role/rolePage`, params, {params: page});
  },
  getRoleList(params) {
    return axios.post(`${base.requestUrl}/role/roleList`, params);
  },
  getRoleById(params) {
    return axios.get(`${base.requestUrl}/role/getRoleById/`+params);
  },
  addRole(params) {
    return axios.post(`${base.requestUrl}/role/add`, params);
  },
  editRole(params) {
    return axios.post(`${base.requestUrl}/role/edit`, params);
  },
  delRole(params) {
    return axios.get(`${base.requestUrl}/role/del/` + params);
  },
  delRoleByIds(params) {
    return axios.post(`${base.requestUrl}/role/delRoleByIds`, params);
  },
//----------------------------------------------------------------------------------
  getAuthTree(){
    return axios.get(`${base.requestUrl}/auth/authTree`);
  },
  
  
}
export default role;
