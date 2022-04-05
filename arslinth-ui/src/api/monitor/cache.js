import axios from '@/request';
import base from '@/api/base.js'

const cache = {
  getCacheInfo() {
    return axios.get(`${base.requestUrl}/server/getCacheInfo`);
  },

}

export default cache;
