import axios from '@/request';
import base from '@/api/base.js'

const job = {
  getSysJobPage(params, page) {
    return axios.post(`${base.requestUrl}/job/sysJobPage`, params, {params: page});
  },
  getSysJobById(params) {
    return axios.get(`${base.requestUrl}/job/getJobInfo/` + params);
  },
  addSysJob(params) {
    return axios.post(`${base.requestUrl}/job/add`, params);
  },
  editSysJob(params) {
    return axios.post(`${base.requestUrl}/job/edit`, params);
  },
  delSysJobByIds(params) {
    return axios.post(`${base.requestUrl}/job/delJobByIds`, params);
  },
  changeStatus(params) {
    return axios.post(`${base.requestUrl}/job/changeStatus`, params);
  },
  run(params) {
    return axios.post(`${base.requestUrl}/job/run`, params);
  }

}
export default job;
