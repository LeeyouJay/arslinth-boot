import Layout from '@/layout'
import InnerLink from '@/layout/components/InnerLink'
import ParentView from '@/layout/components/ParentView'


/**
 * hidden: true               如果设置为 true，该项菜单将不会显示在菜单栏中(默认为 false)
 * meta : {
    title: 'title'                 菜单名
    icon: 'icon-name'      图标名
    fixed: true                 如果设置为 true，该项 tag 将一直存在 tag 栏中(默认为 false)
    keepAlive: true          如果设置为true，则tag存在是为缓存状态
  }
 * */

export const asyncRoutes = [{
  path: '/system',
  component: Layout,
  meta: {
    title: '系统设置',
    icon: 'vue-dsn-icon-biaoge'
  },
  children: [{
    path: 'menu',
    name: 'SysMenu',
    component: () => import('@/views/system/menu'),
    meta: {
      title: '菜单管理',
      icon: 'vue-dsn-icon-biaoge'
    }
  },
    {
      path: 'user',
      name: 'SysUser',
      component: () => import('@/views/system/user'),
      meta: {
        title: '用户管理',
        icon: 'vue-dsn-icon-biaoge'
      }
    },
    {
      path: 'role',
      name: 'SysRole',
      component: () => import('@/views/system/role'),
      meta: {
        title: '角色管理'
      }
    },
    {
      path: 'dict',
      name: 'SysDict',
      component: () => import('@/views/system/dict'),
      meta: {
        title: '字典管理'
      }
    },
    {
      path: 'log',
      name: 'SysLog',
      component: ParentView,
      meta: {
        title: '系统日志'
      },
      children: [{
        path: 'loginlog',
        name: 'Loginlog',
        component: () => import('@/views/system/log/loginlog'),
        meta: {
          title: '登入日志'
        }
      },
        {
          path: 'operlog',
          name: 'Operlog',
          component: () => import('@/views/system/log/operlog'),
          meta: {
            title: '操作日志'
          }
        }]
    }]
},
  // {
  //   path: '/form-table',
  //   component: Layout,
  //   meta: {
  //     title: '表格&表单',
  //     icon: 'vue-dsn-icon-biaoge'
  //   },
  //   children: [{
  //     path: 'table-classic',
  //     name: 'TableClassic',
  //     component: () => import('@/views/form-table/TableClassic'),
  //     meta: {
  //       title: '综合表格'
  //     }
  //   },
  //     {
  //       path: 'form-list',
  //       name: 'FormList',
  //       component: () => import('../views/form-table/FormList'),
  //       meta: {
  //         title: '表单列表',
  //       }
  //     },
  //     {
  //       path: 'table-inline-edit',
  //       name: 'TableInlineEdit',
  //       component: () => import('../views/form-table/TableInlineEdit'),
  //       meta: {
  //         keepAlive: true,
  //         title: '行内编辑表格'
  //       }
  //     }
  //   ]
  // },
  // {
  //   path: '/image',
  //   component: Layout,
  //   meta: {
  //     title: '图片处理',
  //     icon: 'vue-dsn-icon-picture'
  //   },
  //   children: [{
  //     path: 'image-cropper',
  //     name: 'ImageCropper',
  //     component: () => import('../views/image/ImageCropper'),
  //     meta: {
  //       title: '图片裁剪'
  //     }
  //   },
  //     {
  //       path: 'image-compress',
  //       name: 'ImageCompress',
  //       component: () => import('../views/image/ImageCompress'),
  //       meta: {
  //         title: '图片压缩'
  //       }
  //     },
  //     {
  //       path: 'image-synthesizer',
  //       name: 'ImageSynthesizer',
  //       component: () => import('../views/image/ImageSynthesizer'),
  //       meta: {
  //         title: '图片合成'
  //       }
  //     }
  //   ]
  // },
  // {
  //   path: '/tools',
  //   component: Layout,
  //   meta: {
  //     title: '组件',
  //     icon: 'vue-dsn-icon-zujian'
  //   },
  //   children: [{
  //     path: 'image-upload',
  //     name: 'ImageUpload',
  //     component: () => import('../views/tools/ImageUpload'),
  //     meta: {
  //       title: '图片上传'
  //     }
  //   },
  //     {
  //       path: 'transfer',
  //       name: 'Transfer',
  //       component: () => import('../views/tools/TransferPage'),
  //       meta: {
  //         title: '穿梭框'
  //       }
  //     },
  //     {
  //       path: 'count-to',
  //       name: 'CountTo',
  //       component: () => import('../views/tools/CountToPage'),
  //       meta: {
  //         title: '数字滚动'
  //       }
  //     }
  //   ]
  // },
  // {
  //   path: '/editors',
  //   name: 'Editors',
  //   component: Layout,
  //   redirect: '/editors/markdown',
  //   meta: {
  //     title: '编辑器',
  //     icon: 'vue-dsn-icon-bianjiqi'
  //   },
  //   children: [{
  //     path: 'markdown',
  //     name: 'Markdown',
  //     component: () => import('../views/editors/MarkdownEditor'),
  //     meta: {
  //       title: 'Markdown编辑器'
  //     }
  //   },
  //     {
  //       path: 'rich-text',
  //       name: 'ImageRichText',
  //       component: () => import('../views/editors/RichTextEditor'),
  //       meta: {
  //         title: '富文本编辑器'
  //       }
  //     }
  //   ]
  // },
  // {
  //   path: '/tree',
  //   name: 'Tree',
  //   component: Layout,
  //   redirect: '/tree/org-tree',
  //   meta: {
  //     title: '树形结构',
  //     icon: 'vue-dsn-icon-shuxing'
  //   },
  //   children: [{
  //     path: 'org-tree',
  //     name: 'OrgTree',
  //     component: () => import('../views/tree/OrgTree'),
  //     meta: {
  //       title: '组织树'
  //     }
  //   },
  //     {
  //       path: 'ele-tree',
  //       name: 'EleTree',
  //       component: () => import('../views/tree/EleTree'),
  //       meta: {
  //         title: '控件树'
  //       }
  //     }
  //   ]
  // },
]
