import axios from '@/request';
import base from '@/api/base.js'

const jobLog = {
  getJobLogPage(params, page) {
    return axios.post(`${base.requestUrl}/jobLog/jobLogPage`, params, {params: page});
  },
  getJobLogById(params) {
    return axios.get(`${base.requestUrl}/jobLog/getJobLogInfo/` + params);
  },
  delJobLogByIds(params) {
    return axios.post(`${base.requestUrl}/jobLog/delJobLogByIds`, params);
  },
  cleanJobLog() {
    return axios.get(`${base.requestUrl}/jobLog/clean`);
  }
}
export default jobLog;
