<template>
	<div class="view-page">
		<div class="view-page-bar">
			<el-form ref="formTable"  :model="queryParams" @submit.native.prevent>
				<el-row :gutter="20">
					<el-col :span="4">
						<el-form-item label-width="0px">
							<el-select v-model="queryParams.state" placeholder="状态" clearable
							@clear="handleQuery" @change="handleQuery">
								<el-option :value="1" label="成功"></el-option>
								<el-option :value="0" label="失败"></el-option>
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="5">
						<el-form-item label-width="0px">
							<el-date-picker v-model="dateRange" value-format="yyyy-MM-dd" type="daterange"
							style="width: 240px"
							  range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" @change="selectChange">
							  </el-date-picker>
						</el-form-item>
					</el-col>
					<el-col :span="6">
						<el-form-item label-width="0px">
							<el-input v-model="queryParams.username" placeholder="用户名/IP地址" clearable
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
						<el-button type="danger" icon="el-icon-delete" size="mini" plain :disabled="ids.length == 0" v-hasPermi="['DelLoginLog']" @click="handleDel">删除</el-button>
						<el-button type="warning" icon="el-icon-magic-stick" size="mini" plain v-hasPermi="['DelLoginLog']" @click="handleDelAll">清空日志</el-button>
					</div>
					<right-toolbar @queryTable="getData" ></right-toolbar>
				</div>
			</el-header>
			<el-main>
				<el-table :data="tableData" v-loading="loading" class="table" ref="multipleTable"
					header-cell-class-name="table-header" @selection-change="handleSelectionChange">
					<el-table-column type="selection" width="55" align="center" />
					<el-table-column prop="username" label="用户名称" />
					<el-table-column prop="ipAddr" label="IP地址" />
					<el-table-column prop="ipSource" label="登入地点" />
					<el-table-column prop="sysName" label="操作系统" />
					<el-table-column prop="browser" label="浏览器" />
					<el-table-column prop="state" label="状态">
						<template slot-scope="scope">
							<el-button v-if="scope.row.state" type="success" size="mini" disabled plain>成功
							</el-button>
							<el-button v-else type="danger" size="mini" disabled plain>失败</el-button>
						</template>
					</el-table-column>
					<el-table-column prop="message" label="信息" />
					<el-table-column prop="createTime" label="时间" sortable />
				</el-table>
				<div class="pagination">
					 <Pagination :total="pageTotal" :pageIndex.sync="page.pageIndex" :pageSize.sync="page.pageSize" @pagination="getData" />
				</div>
			</el-main>
		</el-container>
	</div>
</template>

<script>
	export default {
		name: 'LoginLog',
		data() {
			return {
				queryParams: {
					username: '',
					state: '',
					beginTime:'',
					endTime:''
				},
				page: {
					pageIndex: 1,
					pageSize: 10
				},
				tableData: [],
				loading: false,
				pageTotal: 10,
				ids: [],
				dateRange: []
			}
		},
		created() {
			this.getData()
		},
		methods: {
			handleQuery() {
				this.page.pageIndex = 1
				this.getData()
				this.clearSelection()
			},
			getData() {
				this.loading = true
				this.$api.log.loginLogPage(this.queryParams, this.page).then(res => {
					if (res.code === 200) {
						this.tableData = res.data.list
						this.pageTotal = res.data.total
					} else
						this.$message.error(res.message)
					this.loading = false
				})
			},
			handleSelectionChange(section) {
				this.ids = section.map(val => val.id)
			},
			handleDel() {
				if (this.ids.length == 0) return
				this.handleConfirm(this.doDel, null, '此操作将永久删除数据, 是否继续?')
			},
			handleDelAll() {
				if (this.tableData.length == 0) return
				this.handleConfirm(this.doDelAll, null, '此操作将清空所有数据, 是否继续?')

			},
			doDel() {
				this.$api.log.delLoginLog(this.ids).then(res => {
					if (res.code == 200) {
						this.$message.success(res.message)
						this.ids = []
						setTimeout(() => {
							this.handleQuery()
						}, 500)
					} else
						this.$message.error(res.message)
				})
			},
			doDelAll() {
				this.$api.log.delAllLoginLog().then(res => {
					if (res.code == 200) {
						this.$message.success(res.message)
						this.ids = []
						setTimeout(() => {
							this.handleQuery()
						}, 500)
					} else
						this.$message.error(res.message)
				})
			},
			handleConfirm(func, arg, text) {
				this.$confirm(text, "提示", {
					type: 'warning',
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					center: true,
				}).then(() => {
					func(arg)
				}).catch((e) => {
					console.log(e)
				})
			},
			clearSelection() {
				this.$refs.multipleTable.clearSelection()
				this.ids = []
			},
			selectChange(val) {
				if(val){
					this.queryParams.beginTime = val[0]
					this.queryParams.endTime = val[1]
				}else{
					this.queryParams.beginTime =''
					this.queryParams.endTime = ''
				}
			},
		}


	}
</script>

<style>
</style>
