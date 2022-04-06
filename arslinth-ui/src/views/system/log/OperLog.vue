<template>
	<div class="view-page">
		<div class="header-bar">
			<el-form ref="formTable" :model="queryParams" @submit.native.prevent>
				<el-row :gutter="20">
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
							<el-input v-model="queryParams.username" placeholder="用户名/IP地址/访问方法" clearable
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
						<el-button type="danger" icon="el-icon-delete" size="mini" plain :disabled="ids.length == 0" v-hasPermi="['DelOperLog']"
							@click="handleDel">删除</el-button>
						<el-button type="warning" icon="el-icon-magic-stick" size="mini" plain v-hasPermi="['DelOperLog']" @click="handleDelAll">清空日志
						</el-button>
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
					<el-table-column prop="ipSource" label="操作地点" />
					<el-table-column prop="method" label="访问方法" :show-overflow-tooltip="true" />
					<el-table-column prop="details" label="操作信息" />
					<el-table-column label="操作状态">
						<template slot-scope="scope">
							<span class="green" v-if="scope.row.resultCode == 200">成功</span>
							<span clase="red" v-else>异常</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="操作时间" sortable />
					<el-table-column label="操作" width="180" align="center" fixed="right">
						<template slot-scope="scope">
							<el-button type="text" icon="el-icon-user" @click="showDetailsFun(scope.row)">查看详情
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div class="pagination">
					 <Pagination :total="pageTotal" :pageIndex.sync="page.pageIndex" :pageSize.sync="page.pageSize" @pagination="getData" />
				</div>

			</el-main>
		</el-container>

		<!-- 操作日志详细 -->
		<el-dialog title="操作日志详细" :visible.sync="showDetails" width="45%" append-to-body center>
			<el-form label-width="100px" size="mini">
				<el-row>
					<el-col :span="12">
						<el-form-item label="用户：">{{sysLog.username}}</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="IP地址：">{{sysLog.ipAddr}}</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="操作地点：">{{sysLog.ipSource}}</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="操作信息：">{{sysLog.details}}</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="浏览器信息：">{{sysLog.browser}}</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="系统名称：">{{sysLog.sysName}}</el-form-item>
					</el-col>
					<el-col :span="24">
						<el-form-item label="访问方法：">{{sysLog.method}}</el-form-item>
					</el-col>
					<el-col :span="24">
						<el-form-item label="请求参数：">{{sysLog.parameters}}</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="操作状态：">
							<span class="green" v-if="sysLog.resultCode == 200">成功</span>
							<span clase="red" v-else>异常</span>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="返回代码："> {{sysLog.resultCode}} </el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="操作时间：">{{sysLog.createTime}}</el-form-item>
					</el-col>
				</el-row>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button type="primary" @click="showDetails = false">关 闭</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		name: 'OperLog',
		data() {
			return {
				queryParams: {
					username: ''
				},
				page: {
					pageIndex: 1,
					pageSize: 10
				},
				tableData: [],
				loading: false,
				pageTotal: 10,
				showDetails: false,
				sysLog: {},
				ids: [],
				dateRange: [],

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
				this.$api.log.operLogPage(this.queryParams, this.page).then(res => {
					if (res.code === 200) {
						this.tableData = res.data.list
						this.pageTotal = res.data.total
					} else
						this.$message.error(res.message)
					this.loading = false
				})
			},
			// 查看详情
			showDetailsFun(e) {
				this.sysLog = e;
				this.showDetails = true;
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
				this.$api.log.delOperLog(this.ids).then(res => {
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
				this.$api.log.delAllOperLog().then(res => {
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
