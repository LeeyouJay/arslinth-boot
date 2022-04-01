<template>
  <div class="pagination-wrapper">
    <el-pagination
      :background="background"
      :current-page.sync="index"
      :page-sizes="pageArray"
      :page-size.sync="size"
      :total="total"
      :layout="layout"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<script>
export default {
  name: 'Pagination',
  props: {
    pageIndex: {
      type: Number,
      default: 1,
      required: true
    },
    pageSize: {
      type: Number,
      default: 10,
      required: true
    },
    background: {
      type: Boolean,
      default: true
    },
    pageArray: {
      type: Array,
      default: () => [10, 15, 20, 50]
    },
    total: {
      type: Number,
      default: 0,
      required: true
    },
    layout: {
      type: String,
      default: 'total, prev, pager, next, sizes, jumper'
    }
  },
  computed: {
    index: {
      get() {
        return this.pageIndex
      },
      set(val) {
        this.$emit('update:pageIndex', val)
      }
    },
    size: {
      get() {
        return this.pageSize
      },
      set(val) {
        this.$emit('update:pageSize', val)
      }
    }
  },
  methods: {
    handleSizeChange(val) {
      this.$emit('pagination', { pageIndex: this.index, pageSize: val })
    },
    handleCurrentChange(val) {
      this.$emit('pagination', { pageIndex: val, pageSize: this.size })
    }
  }
}
</script>

<style lang="less">
.pagination-wrapper{
  margin-top: 25px;
  .el-pagination{
    float: right;
  }
}
</style>
