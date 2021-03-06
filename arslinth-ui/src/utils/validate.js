import {sha256} from 'js-sha256'

export function isExternal(path) {
  return /^(https?:|mailto:|tel:)/.test(path)
}

export function doSha256(password) {
  return sha256(password)
}

//时间格式化
export function formatDate(cellValue) {
  if (cellValue == null || cellValue == "") return "";
  var date = new Date(cellValue)
  var year = date.getFullYear()
  var month = date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1
  var day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate()
  var hours = date.getHours() < 10 ? '0' + date.getHours() : date.getHours()
  var minutes = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()
  var seconds = date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds()
  return year + '-' + month + '-' + day + ' ' + hours + ':' + minutes + ':' + seconds
}

export function checkPermi(auths) {
  if (auths && auths instanceof Array && auths.length > 0) {
  	const permissions = this.$store.user.permissions || []
  	return permissions.some(permission => {
      return permission === "*" || auths.includes(permission)
    })
  } else {
    return false
  }

}
