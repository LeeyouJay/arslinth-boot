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
    title: '系统菜单',
    icon: 'vue-dsn-icon-biaoge'
  },
  children: [{
    path: 'menu',
    name: 'SystemMenu',
    component: () => import('@/views/system/menu'),
    meta: {
      keepAlive: true,
      title: '系统菜单'
    }
  }]
},
  {
    path: '/form-table',
    component: Layout,
    meta: {
      title: '表格&表单',
      icon: 'vue-dsn-icon-biaoge'
    },
    children: [{
      path: 'table-classic',
      name: 'TableClassic',
      component: () => import('@/views/form-table/TableClassic'),
      meta: {
        keepAlive: true,
        title: '综合表格'
      }
    },
      {
        path: 'form-list',
        name: 'FormList',
        component: () => import('../views/form-table/FormList'),
        meta: {
          title: '表单列表',
          keepAlive: true,
        }
      },
      {
        path: 'table-inline-edit',
        name: 'TableInlineEdit',
        component: () => import('../views/form-table/TableInlineEdit'),
        meta: {
          keepAlive: true,
          title: '行内编辑表格'
        }
      }
    ]
  },
  {
    path: '/image',
    component: Layout,
    meta: {
      title: '图片处理',
      icon: 'vue-dsn-icon-picture'
    },
    children: [{
      path: 'image-cropper',
      name: 'ImageCropper',
      component: () => import('../views/image/ImageCropper'),
      meta: {
        title: '图片裁剪'
      }
    },
      {
        path: 'image-compress',
        name: 'ImageCompress',
        component: () => import('../views/image/ImageCompress'),
        meta: {
          title: '图片压缩'
        }
      },
      {
        path: 'image-synthesizer',
        name: 'ImageSynthesizer',
        component: () => import('../views/image/ImageSynthesizer'),
        meta: {
          title: '图片合成'
        }
      }
    ]
  },
  {
    path: '/video',
    component: Layout,
    meta: {
      title: '视频处理',
      icon: 'vue-dsn-icon-video'
    },
    children: [{
      path: 'video-player',
      name: 'VideoPlayer',
      component: () => import('../views/video/VideoPlayer'),
      meta: {
        title: '视频播放器'
      }
    },
      {
        path: 'video-mark',
        name: 'VideoMark',
        component: () => import('../views/video/VideoMark'),
        meta: {
          title: '视频水印'
        }
      }
    ]
  },
  {
    path: '/tools',
    component: Layout,
    meta: {
      title: '组件',
      icon: 'vue-dsn-icon-zujian'
    },
    children: [{
      path: 'image-upload',
      name: 'ImageUpload',
      component: () => import('../views/tools/ImageUpload'),
      meta: {
        title: '图片上传'
      }
    },
      {
        path: 'drag',
        component: ParentView,
        meta: {
          title: '拖拽'
        },
        children: [{
          path: 'drag-list',
          name: 'DragList',
          component: () => import('../views/tools/Drag/DragList'),
          meta: {
            title: '列表拖拽'
          }
        },
          {
            path: 'vue-drr',
            name: 'VueDrr',
            component: () => import('../views/tools/Drag/VueDrrTool'),
            meta: {
              title: '组件拖拽'
            }
          }
        ]
      },
      {
        path: 'transfer',
        name: 'Transfer',
        component: () => import('../views/tools/TransferPage'),
        meta: {
          title: '穿梭框'
        }
      },
      {
        path: 'count-to',
        name: 'CountTo',
        component: () => import('../views/tools/CountToPage'),
        meta: {
          title: '数字滚动'
        }
      }
    ]
  },
  {
    path: '/editors',
    name: 'Editors',
    component: Layout,
    redirect: '/editors/markdown',
    meta: {
      title: '编辑器',
      icon: 'vue-dsn-icon-bianjiqi'
    },
    children: [{
      path: 'markdown',
      name: 'Markdown',
      component: () => import('../views/editors/MarkdownEditor'),
      meta: {
        title: 'Markdown编辑器'
      }
    },
      {
        path: 'rich-text',
        name: 'ImageRichText',
        component: () => import('../views/editors/RichTextEditor'),
        meta: {
          title: '富文本编辑器'
        }
      }
    ]
  },
  {
    path: '/tree',
    name: 'Tree',
    component: Layout,
    redirect: '/tree/org-tree',
    meta: {
      title: '树形结构',
      icon: 'vue-dsn-icon-shuxing'
    },
    children: [{
      path: 'org-tree',
      name: 'OrgTree',
      component: () => import('../views/tree/OrgTree'),
      meta: {
        title: '组织树'
      }
    },
      {
        path: 'ele-tree',
        name: 'EleTree',
        component: () => import('../views/tree/EleTree'),
        meta: {
          title: '控件树'
        }
      }
    ]
  },
  {
    path: '/excel',
    name: 'Excel',
    component: Layout,
    redirect: '/excel/export-excel',
    meta: {
      title: 'Excel',
      icon: 'vue-dsn-icon-excel'
    },
    children: [{
      path: 'export-excel',
      name: 'ExportExcel',
      component: () => import('../views/excel/ExportExcel'),
      meta: {
        title: '导出Excel'
      }
    },
      {
        path: 'import-excel',
        name: 'ImportExcel',
        component: () => import('../views/excel/ImportExcel'),
        meta: {
          title: '导入Excel'
        }
      }
    ]
  },
  // {
  //   path: '/error-page',
  //   name: 'ErrorPage',
  //   component: Layout,
  //   redirect: '/error-page/page-401',
  //   meta: {
  //     title: '错误页面',
  //     icon: 'vue-dsn-icon-bug'
  //   },
  //   children: [{
  //       path: 'page-401',
  //       name: 'Page401',
  //       component: () => import('../views/error-page/401'),
  //       meta: {
  //         title: '401页面'
  //       }
  //     },
  //     {
  //       path: 'page-404',
  //       name: 'Page404',
  //       component: () => import('../views/error-page/404'),
  //       meta: {
  //         title: '404页面'
  //       }
  //     }
  //   ]
  // }
]
