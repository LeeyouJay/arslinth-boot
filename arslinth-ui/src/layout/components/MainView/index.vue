<template>
	<div class="main-view">
		<transition name="fade-transform" mode="out-in">
			<keep-alive :include="cachedViews">
				<router-view :key="key" />
			</keep-alive>
		</transition>
	</div>
</template>
<script>
	export default {
		name: 'MainView',
		computed: {
			cachedViews() {
				return this.$store.state.tagsView.cachedViews
			},
			key() {
				return this.$route.path
			}
		}
	}
</script>

<style lang="less">
	.view-page-bar {
		height: 32px;
		padding: 16px 20px;
	}
	.main-view {
		min-height: calc(100vh - 155px);
		width: calc(100% - 50px);
		padding: 25px;
		position: relative;
		overflow: hidden;
	}
	.dialog-body {
		overflow-y: auto;
		height: calc(100vh - 170px);
		width: 100%;
		padding-right: 14px;
	}
	.table-bar {
		display: flex;
		justify-content: space-between;
	}
	.link-type,
	.link-type:focus {
		color: #409eff;
		cursor: pointer;
		font-size: 14px;
		font-weight: 500;
	  &:hover {
	    color: rgb(32, 203, 255);
	  }
	}
	//表格更多操作下拉样式
	.el-table .el-dropdown-link {
		cursor: pointer;
		color: #409EFF;
		margin-left: 5px;
	}
	.view-page {
		display: flex;
		flex-direction: column;
	}
	.pagination {
	    margin: 20px 0;
	    text-align: right;
	}
	.green {
		font-weight: bold;
		color: #67C23A !important;
	}
	.red {
		font-weight: bold;
		color: #ff0000 !important;
	}
	
	.blue {
		font-weight: bold;
		color: #409EFF !important;
	}
	
	.yellow {
		font-weight: bold;
		color: #ffaa00 !important;
	}
</style>
