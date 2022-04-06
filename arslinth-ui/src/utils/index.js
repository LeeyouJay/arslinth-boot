
import store from '@/store'

const utils = {
  //对象深拷贝
  deepClone(source) {
    return JSON.parse(JSON.stringify(source))
  },

  //将base64图片数据转换成Blob
  base64toBlob(image) {
    const bytes = window.atob(image.split(',')[1])
    const array = []
    for (let i = 0; i < bytes.length; i++) {
      array.push(bytes.charCodeAt(i))
    }
    return new Blob([new Uint8Array(array)], {type: 'image/png'})
  },

  //树结构扁平化
  treeToArray(tree) {
    return tree.reduce((res, item) => {
      const {children, ...i} = item
      return res.concat(i, children && children.length ? this.treeToArray(children) : [])
    }, [])
  },
  //获取粘贴时的纯文本
  getPlainText(e) {
    function uniform(str) {
      const _str = supportIE(str).replace(/</g, '&lt;').replace(/>/g, '&gt;')
      const arr = _str.split(/\r\n|\r|\n/)
      if (arr) {
        return arr.map(item => {
          if (item.length > 0) {
            return '<div>' + item + '</div>'
          }
          return '<div><br></div>'
        }).join('')
      }
      return _str
    }

    let innerText = ''
    const TAB = ' '.repeat(2)
    e.preventDefault()
    if (e.clipboardData) {
      innerText = (e.originalEvent || e).clipboardData.getData('text/plain').replace(/\t/g, TAB)
      document.execCommand('insertHtml', false, uniform(innerText))
    } else if (window.clipboardData) {
      innerText = window.clipboardData.getData('Text').replace(/\t/g, TAB)
      if (document.selection) {
        document.selection.createRange().pasteHTML(uniform(innerText))
      } else if (window.getSelection) {
        const sel = window.getSelection()
        const range = sel.getRangeAt(0)
        // 创建临时元素，使得TextRange可以移动到正确的位置
        const tempEl = document.createElement('span')
        tempEl.innerHTML = '&#FEFF;'
        range.deleteContents()
        range.insertNode(tempEl)
        const textRange = document.body.createTextRange()
        textRange.moveToElementText(tempEl)
        tempEl.parentNode.removeChild(tempEl)
        textRange.pasteHTML(uniform(innerText))
        textRange.collapse(false)
        textRange.select()
      }
    }
    return uniform(innerText)
  },
  //树转数组
  treeToArray(tree) {
    return tree.reduce((res, item) => {
      const {children, ...i} = item
      return res.concat(i, children && children.length ? this.treeToArray(children) : [])
    }, [])
  },

  checkPermi(auths) {
    if (auths && auths instanceof Array && auths.length > 0) {
      const permissions = store.state.user.permissions || []
      return permissions.some(permission => {
        return permission === "*" || auths.includes(permission)
      })
    } else {
      return false
    }
  },
}

export default utils;
