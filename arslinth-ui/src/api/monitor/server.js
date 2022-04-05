import axios from '@/request';
import base from '@/api/base.js'

const server = {
  getServerInfo() {
    return axios.get(`${base.requestUrl}/server/getServerInfo`);
  },
}

export default server;
