<template>
	<div class="view-page">
		<div class="view-page-bar">
			<el-form ref="formTable" :model="queryParams" @submit.native.prevent>
				<el-row :gutter="20">
					<el-col :span="4">
						<el-form-item label-width="0px">
							<el-input v-model="queryParams.username" placeholder="用户名称" clearable
							@keyup.enter.native="handleQuery"></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="4">
						<el-form-item label-width="0px">
							<el-input v-model="queryParams.loginLocation" placeholder="登入地址" clearable
							@keyup.enter.native="handleQuery"></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="6">
						<el-form-item label-width="0px">
							<el-button icon="el-icon-search" type="primary" @click="handleQuery">搜索</el-button>
						</el-form-item>
					</el-col>
				</el-row>
			</el-form>
		</div>
		<el-container>
			<el-header height="auto">
				<div class="table-bar">
					<div class="table-btn">
						
					</div>
					<right-toolbar @queryTable="getData" ></right-toolbar>
				</div>
			</el-header>
			<el-main>
				<el-table :data="tableData" v-loading="loading" class="table" ref="dictTable" 
					header-cell-class-name="table-header">
					<el-table-column label="序号" align="center" width="80px">
						<template scope="scope">
							<span>{{(page.pageIndex - 1) * page.pageSize + scope.$index + 1}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="token"  label="会话编号" :show-overflow-tooltip="true" />
					<el-table-column prop="username"  align="center" label="登入名称" />
					<el-table-column prop="userType" align="center"  label="用户类型" />
					<el-table-column prop="ipaddr" align="center"  label="主机" />
					<el-table-column prop="loginLocation" align="center"  label="登入地点" />
					<el-table-column prop="browser" align="center"  label="浏览器" />
					<el-table-column prop="os" align="center"  label="操作系统" />
					<el-table-column prop="loginTime" width="180" align="center" sortable label="登入时间" :formatter="dateTimeFormat"/>
					<el-table-column label="操作" width="180" align="center" fixed="right">
						<template slot-scope="scope">
							<el-button type="text" icon="el-icon-delete" class="red" @click="handleLogout(scope.row)">强退
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div class="pagination">
					 <Pagination :total="pageTotal" :pageIndex.sync="page.pageIndex" :pageSize.sync="page.pageSize" @pagination="getData" />
				</div>
			</el-main>
		</el-container>
	</div>
</template>

<script>
	import { formatDate } from '@/utils/validate.js'
	export default {
		name: 'OnlineUser',
		data() {
			return {
				loading: false,
				tableData: [],
				queryParams: {
					loginLocation: '',
					username:''
				},
				page: {
					pageIndex: 1,
					pageSize: 10
				},
				pageTotal: 0,
			}
		},
		created() {
			this.getData()
		},
		methods: {
			handleQuery() {
				this.page.pageIndex = 1
				this.getData()
			},
			getData() {
				this.loading = true
				this.$api.online.onlineUserPage(this.queryParams, this.page).then(res => {
					if (res.code === 200) {
						this.tableData = res.data.list
						this.pageTotal = res.data.total
					} else
						this.$message.error(res.message)
					this.loading = false
				})
			},
			handleLogout(row) {
				this.$confirm('是否确认强退名称为"'+ row.username +'"的用户？', '提示', {
						type: 'warning'
					})
					.then(() => {
						this.doLogout(row)
					})
					.catch((e) => {
						console.log(e)
					});
			},
			doLogout(row){
				this.$api.online.forceLogout(row.token).then(res=>{
					if(res.code == 200){
						this.handleQuery()
						this.$message.success(res.message)
					}else
						this.$message.error(res.message)
				})
			},
			dateTimeFormat(row){
				return formatDate(row.loginTime)
			}
		}
	}
</script>

<style scoped>

</style>
