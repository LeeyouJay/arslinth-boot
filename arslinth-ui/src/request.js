import axios from 'axios'
import {Message, MessageBox, Notification} from 'element-ui'
import {getToken, setToken, removeToken} from './utils/cookie'
import base from './api/base.js'

axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=UTF-8';

// 创建axios实例
const service = axios.create({
  baseURL: base.requestUrl,
  timeout: 90000 // 请求超时时间
})

/**
 * 请求拦截器
 * 每次请求前，如果存在token则在请求头中携带token
 */
service.interceptors.request.use(
  config => {
    // 登录流程控制中，根据本地是否存在token判断用户的登录情况
    // 但是即使token存在，也有可能token是过期的，所以在每次的请求头中携带token
    // 后台根据携带的token判断用户的登录情况，并返回给我们对应的状态码
    // 而后我们可以在响应拦截器中，根据状态码进行一些统一的操作。
    const token = getToken();
    token && (config.headers.Authorization = token);
    return config;
  },
  error => Promise.error(error)
)

// 是否显示重新登录
let isReloginShow;
// 响应拦截器
service.interceptors.response.use(
  // 请求成功
  res => {
    if (res.status != 200) return Promise.reject(res)
    let result = res.data
    if (result.code == 200) {
      let token = result.data.token
      token && setToken(token)

      return Promise.resolve(res.data)
    }
    if (result.code == 401) {
      if (!isReloginShow) {
        isReloginShow = true;
        MessageBox.confirm('登录状态已过期，您可以继续留在该页面，或者重新登录', '系统提示', {
          confirmButtonText: '重新登录',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          isReloginShow = false;
          removeToken()
          location.reload()
        }).catch((e) => {
          isReloginShow = false;
          console.log(e)
        });
      }
    }
    return Promise.resolve(res.data)
  },
  // 请求失败
  error => {
    const {
      response, message
    } = error;
    if (response) {
      errorHandle(response.status);
      return Promise.reject(response);
    } else {
      // 关于断网组件中的刷新重新获取数据，会在断网组件中说明
      if (message == "Network Error") {
        Notification.error({ title: '错误',message: "后端接口连接异常"})
      }
      else if (message.includes("timeout")) {
        Notification.error({ title: '错误',message: "系统接口请求超时"})
      }
      else if (message.includes("Request failed with status code")) {
        Notification.error({ title: '错误',message: "系统接口" + message.substr(message.length - 3) + "异常"})

      }
      if (!window.navigator.onLine) {
        Notification.error({ title: '错误',message: "网络连接中断！服务器可能不在线。"})
      } else {
        return Promise.reject(error);
      }
    }
  });

const errorHandle = (status) => {
  // 状态码判断
  switch (status) {

    case 400:
      Message.error('参数解析异常！')
      break;
    case 403:
      Message.error('访问拒绝！')
      break;
    case 404:
      Message.error('请求资源不存在！')
      break;
    case 500:
      Message.error('服务器错误！')
  }
}
export default service
