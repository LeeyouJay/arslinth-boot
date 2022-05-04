<template>
	<div class="view-page">
		<div class="view-page-bar">
			<el-form ref="queryTable" :model="queryParams" @submit.native.prevent>
				<el-row :gutter="20">
					<el-col :span="4">
						<el-form-item label-width="0px">
							<el-select v-model="queryParams.status" placeholder="状态" clearable >
								<el-option :value="true" label="正常"></el-option>
								<el-option :value="false" label="失败"></el-option>
							</el-select>
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
						<el-button type="danger" icon="el-icon-delete" size="mini" plain :disabled="ids.length == 0"
							@click="handleDelBatch">删除</el-button>
						<el-button type="success" icon="el-icon-magic-stick" size="mini" plain @click="handleClean">清除日志</el-button>
					</div>
					<right-toolbar @queryTable="getData"></right-toolbar>
				</div>
			</el-header>
			<el-main>
				<el-table :data="tableData" v-loading="loading" class="table" ref="jobLogTable" row-key="id"
					header-cell-class-name="table-header" @selection-change="handleSelectionChange">
					<el-table-column type="selection" :reserve-selection="true" width="55" align="center" />
					<el-table-column label="序号" align="center" width="80">
						<template scope="scope">
							<span>{{(page.pageIndex - 1) * page.pageSize + scope.$index + 1}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="jobName" label="任务名称" align="center"  :show-overflow-tooltip="true"/>
					<el-table-column label="任务组名" align="center" >
						<template scope="scope">
							<span class="black">{{$utils.dictFormatter( scope.row.jobGroup, dict.sys_job_group)}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="invokeTarget" label="调用目标字符串" align="center" :show-overflow-tooltip="true"/>
					<el-table-column label="执行状态" align="center" >
						<template scope="scope">
							<span v-if="scope.row.status" class="green">正常</span>
							<span v-else class="red">异常</span>
						</template>
					</el-table-column>
					<el-table-column prop="jobMessage" label="日志信息" align="center" />
					<el-table-column prop="exceptionInfo" label="异常信息" align="center" />
					<el-table-column prop="createTime" align="center" label="记录时间" />
					<el-table-column label="操作" width="180" fixed="right">
						<template slot-scope="scope">
							<el-button type="text" icon="el-icon-view" @click="handleView(scope.row)">详情</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div class="pagination">
					<Pagination :total="pageTotal" :pageIndex.sync="page.pageIndex" :pageSize.sync="page.pageSize"
						@pagination="getData" />
				</div>
			</el-main>
		</el-container>
		<!-- 编辑弹出框 -->
		<el-dialog :visible.sync="formVisible" width="45%" append-to-body center>
			<template slot="title">
				<span class="dialog-title">{{dialogText}}</span>
			</template>
			<el-form ref="form" :model="form" label-width="100px" size="mini">
				<el-row>
					<el-col :span="12">
						<el-form-item label="任务名称：">{{ form.jobName }}</el-form-item>
						<el-form-item label="任务分组：">
							<span class="black">{{$utils.dictFormatter( form.jobGroup, dict.sys_job_group)}}</span>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="执行时间：">{{ form.createTime }}</el-form-item>
						<el-form-item label="执行状态：">
							<div v-if="form.status" class="green">正常</div>
							<div v-else class="red">异常</div>
						</el-form-item>
					</el-col>
					<el-col :span="24">
						<el-form-item label="调用方法：">{{ form.invokeTarget }}</el-form-item>
					</el-col>
					<el-col :span="24">
						<el-form-item label="日志信息：">{{ form.jobMessage }}</el-form-item>
					</el-col>
					<el-col :span="24">
						<el-form-item label="异常信息：" v-if="form.status">{{ form.exceptionInfo }}</el-form-item>
					</el-col>
				</el-row>
			</el-form>
			<span slot="footer" class="dialog-footer">
				<el-button type="primary" @click="formVisible = false">确 定</el-button>
			</span>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		dicts: ['sys_job_group'],
		data() {
			return {
				loading: false,
				tableData: [],
				selectData: [],
				ids: [],
				label: '',
				queryParams: {
					jobId: '-1',
					status: null
				},
				page: {
					pageIndex: 1,
					pageSize: 10
				},
				form: { },
				formVisible: false,
				pageTotal: 0,
				dialogText: '调度日志详细',
			}
		},
		methods: {
			handleQuery() {
				this.page.pageIndex = 1
				this.clearSelection()
				this.getData()
			},
			getData() {
				this.loading = true
				this.$api.jobLog.getJobLogPage(this.queryParams, this.page).then(res => {
					if (res.code === 200) {
						this.tableData = res.data.list
						this.pageTotal = res.data.total
					} else
						this.$message.error(res.message)
					this.loading = false
				})
			},
			handleView(row) {
				this.$api.jobLog.getJobLogById(row.id).then(res=>{
					if(res.code == 200){
						this.form = res.data.jobLog
						this.formVisible = true
					} else 
						this.$message.error(res.message)
				})
			},
			handleDelBatch() {
				if (this.ids.length == 0) return
				let text = '此操作将永久删除数据, 是否继续?'
				this.handleConfirm(this.delJobLogBatch, null, text)
			},
			handleClean() {
				let text = '此操作将永久清空所有数据, 是否继续?'
				this.handleConfirm(this.doClean, null, text)
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
			doClean() {
				this.$api.jobLog.cleanJobLog().then(res=> {
					if(res.code == 200 ){
						this.handleQuery()
						this.$message.success(res.message)
					}else 
						this.$message.error(res.message)
				})
			},
			delJobLogBatch() {
				this.$api.jobLog.delJobLogByIds(this.ids).then(res => {
					if (res.code === 200) {
						this.handleQuery()
						this.clearSelection()
						this.$message.success(res.message)
					} else
						this.$message.error(res.message)
				})
			},
			handleSelectionChange(section) {
				this.ids = section.map(val => val.id)
			},
			clearSelection() {
				this.$refs.jobLogTable.clearSelection()
				this.ids = []
			},
		}
	}
</script>

<style>
</style>
