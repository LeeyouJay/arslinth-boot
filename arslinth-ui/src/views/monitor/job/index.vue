<template>
	<div class="view-page">
		<div class="view-page-bar">
			<el-form ref="queryTable" :model="queryParams" @submit.native.prevent>
				<el-row :gutter="20">
					<el-col :span="6">
						<el-form-item label-width="0px">
							<el-input v-model="queryParams.jobName" placeholder="任务名称" clearable
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
						<el-button type="primary" icon="el-icon-plus" size="mini" plain @click="handleAdd">新增
						</el-button>
						<el-button type="danger" icon="el-icon-delete" size="mini" plain :disabled="ids.length == 0"
							@click="handleDelBatch">删除</el-button>
					</div>
					<right-toolbar @queryTable="getData"></right-toolbar>
				</div>
			</el-header>
			<el-main>
				<el-table :data="tableData" v-loading="loading" class="table" ref="sysJobTable" row-key="id"
					header-cell-class-name="table-header" @selection-change="handleSelectionChange">
					<el-table-column type="selection" :reserve-selection="true" width="55" align="center" />
					<el-table-column label="序号" align="center"  width="80">
						<template scope="scope">
							<span>{{(page.pageIndex - 1) * page.pageSize + scope.$index + 1}}</span>
						</template>
					</el-table-column>
					<el-table-column label="任务名称" prop="jobName" align="center" :show-overflow-tooltip="true" />
					<el-table-column label="任务组名" prop="jobGroup" align="center">
						<template scope="scope">
							<span class="black">{{$utils.dictFormatter( scope.row.jobGroup, dict.sys_job_group)}}</span>
						</template>
					</el-table-column>
					<el-table-column label="调用目标字符串" prop="invokeTarget" align="center" :show-overflow-tooltip="true" />
					<el-table-column label="cron执行表达式" prop="cronExpression" align="center"
						:show-overflow-tooltip="true" />
					<el-table-column label="状态" align="center">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.status" :active-value="true" :inactive-value="false"
								@change="handleStatusChange(scope.row)" />
						</template>
					</el-table-column>
					<el-table-column prop="createTime" align="center" label="创建时间" />
					<el-table-column label="操作" width="180" fixed="right">
						<template slot-scope="scope">
							<el-button type="text" icon="el-icon-edit" @click="handleEdit(scope.row)">修改</el-button>
							<el-button type="text" icon="el-icon-delete" class="red" v-hasPermi="['DelJob']"
								@click="handleDel(scope.row)">删除
							</el-button>
							<el-dropdown size="mini" @command="(command) => handleCommand(command, scope.row)">
								<el-button type="text" icon="el-icon-d-arrow-right el-icon--right">更多</el-button>
								<el-dropdown-menu slot="dropdown">
									<el-dropdown-item command="handleRun" icon="el-icon-caret-right"
										v-hasPermi="['ResetPassword']">执行一次</el-dropdown-item>
									<el-dropdown-item command="handleView" icon="el-icon-view">任务详情
									</el-dropdown-item>
									<el-dropdown-item command="handleJobLog" icon="el-icon-s-operation">调度日志
									</el-dropdown-item>
								</el-dropdown-menu>
							</el-dropdown>
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
		<el-dialog v-dialogDrag :visible.sync="formVisible" width="800px" append-to-body center>
			<template slot="title">
				<span class="dialog-title">{{dialogText}}</span>
			</template>
			<el-form ref="formTable" :model="form" label-width="120px"
				:disabled="!$utils.checkPermi(['AddSysJob','EditSysJob'])">
				<el-row :gutter="20">
					<el-col :span="12">
						<el-form-item label="任务名称" prop="jobName"
							:rules="[{required: true, message: '任务名称不能为空', trigger: 'blur'}]">
							<el-input v-model="form.jobName" placeholder="请输入任务名称" />
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="任务分组" prop="jobGroup"
							:rules="[ {required: true, message: '请选择任务分组', trigger: 'blur' }]">
							<el-select v-model="form.jobGroup" placeholder="请选择任务分组" clearable>
								<el-option v-for="(item, index)  in dict.sys_job_group" :key="index"
									:label="item.dictName" :value="item.dictValue"></el-option>
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="24">
						<el-form-item prop="invokeTarget"
							:rules="[{ required: true, message: '调用目标字符串不能为空', trigger: 'blur' }]">
							<span slot="label">
								调用方法
								<el-tooltip placement="top">
									<div slot="content">
										Bean调用示例：arslinthTaskService.multipleParams('arslinth', true, 100L, 316.50D, 10)
										<br />参数说明：支持字符串，布尔类型，长整型，浮点型，整型
									</div>
									<i class="el-icon-question"></i>
								</el-tooltip>
							</span>
							<el-input v-model="form.invokeTarget" placeholder="请输入调用目标字符串" />
						</el-form-item>
					</el-col>
					<el-col :span="24">
						<el-form-item label="cron表达式" prop="cronExpression"
							:rules="[ {required: true, message: 'cron执行表达式不能为空', trigger: 'blur' }]">
							<el-input v-model="form.cronExpression" placeholder="请输入cron执行表达式">
								<template slot="append">
									<el-button type="primary" @click="handleShowCron">
										生成表达式
										<i class="el-icon-time el-icon--right"></i>
									</el-button>
								</template>
							</el-input>
						</el-form-item>
					</el-col>
					<el-col :span="24">
						<el-form-item label="执行策略" prop="misfirePolicy">
							<el-radio-group v-model="form.misfirePolicy" size="small">
								<el-radio-button label="1">立即执行</el-radio-button>
								<el-radio-button label="2">执行一次</el-radio-button>
								<el-radio-button label="3">放弃执行</el-radio-button>
							</el-radio-group>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="是否并发" prop="concurrent">
							<el-radio-group v-model="form.concurrent" size="small">
								<el-radio-button :label="true">允许</el-radio-button>
								<el-radio-button :label="false">禁止</el-radio-button>
							</el-radio-group>
						</el-form-item>
					</el-col>
				</el-row>
			</el-form>
			<span slot="footer" class="dialog-footer">
				<el-button @click="formVisible = false">取 消</el-button>
				<el-button type="primary" @click="submit">确 定</el-button>
			</span>
		</el-dialog>
		<!-- Cron表达式生成器 -->
		<el-dialog v-dialogDrag="5" title="Cron表达式生成器" :visible.sync="openCron" width="800px" append-to-body
			destroy-on-close center>
			<crontab @hide="openCron=false" @fill="crontabFill" :expression="expression"></crontab>
		</el-dialog>

		<!-- 任务日志详细 -->
		<el-dialog title="任务详细" :visible.sync="openView" width="700px" append-to-body>
			<el-form ref="form" :model="form" label-width="120px" size="mini">
				<el-row>
					<el-col :span="12">
						<el-form-item label="任务名称：">{{ form.jobName }}</el-form-item>
						<el-form-item label="任务分组："> 
							<span class="black">{{$utils.dictFormatter( form.jobGroup, dict.sys_job_group)}}</span>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="创建时间：">{{ form.createTime }}</el-form-item>
						<el-form-item label="cron表达式：">{{ form.cronExpression }}</el-form-item>
					</el-col>
					<el-col :span="24">
						<el-form-item label="下次执行时间：">{{ form.nextValidTime }}</el-form-item>
					</el-col>
					<el-col :span="24">
						<el-form-item label="调用目标方法：">{{ form.invokeTarget }}</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="任务状态：">
							<div v-if="form.status" class="green">正常</div>
							<div v-else class="red">暂停</div>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="是否并发：">
							<div v-if="form.concurrent" class="green">允许</div>
							<div v-else class="red">禁止</div>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="执行策略：">
							<div v-if="form.misfirePolicy == 0">默认策略</div>
							<div v-else-if="form.misfirePolicy == 1" class="green">立即执行</div>
							<div v-else-if="form.misfirePolicy == 2" class="yellow">执行一次</div>
							<div v-else-if="form.misfirePolicy == 3" class="red">放弃执行</div>
						</el-form-item>
					</el-col>
				</el-row>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="openView = false">关 闭</el-button>
			</div>
		</el-dialog>

		<el-dialog :visible.sync="tableVisible" @open="dialogOpen" fullscreen append-to-body center>
			<template slot="title">
				<span class="dialog-title">调度日志</span>
			</template>
			<div class="dialog-body scroll-bar">
				<JobLog ref= "JobLog"></JobLog>
			</div>
			<span slot="footer" class="dialog-footer">
				<el-button type="primary" @click="tableVisible = false">关闭</el-button>
			</span>
		</el-dialog>
	</div>
</template>

<script>
	import Crontab from '@/components/Crontab'
	import JobLog from './JobLog'
	
	export default {
		dicts: ['sys_job_group'],
		name: 'SysJob',
		components: {
			Crontab,
			JobLog
		},
		data() {
			return {
				isAdd: false,
				loading: false,
				tableVisible: false,
				openCron: false,
				openView: false,
				tableData: [],
				ids: [],
				expression: '',
				queryParams: {
					jobName: '',
				},
				page: {
					pageIndex: 1,
					pageSize: 10
				},
				form: {
					misfirePolicy: '2',
					concurrent: false,
					status: false,
					cronExpression: ''
				},
				formVisible: false,
				pageTotal: 0,
				dialogText: '添加任务',
			}
		},
		created() {
			this.getData()
		},
		methods: {
			handleQuery() {
				this.page.pageIndex = 1
				this.clearSelection()
				this.getData()
			},
			getData() {
				this.loading = true
				this.$api.job.getSysJobPage(this.queryParams, this.page).then(res => {
					if (res.code === 200) {
						this.tableData = res.data.list
						this.pageTotal = res.data.total
					} else
						this.$message.error(res.message)
					this.loading = false
				})
			},
			submit() {
				if (this.$refs.formTable.disabled) {
					this.formVisible = false
					return
				}
				this.$refs.formTable.validate((valid) => {
					if (valid) {
						this.isAdd ? this.addSysJob() : this.editSysJob()
					} else {
						console.log('验证不通过');
						return false;
					}
				});
			},
			handleAdd() {
				this.isAdd = true
				this.dialogText = "添加任务"
				this.form = this.$options.data().form
				this.formVisible = true
				this.$nextTick(() => {
					this.$refs.formTable.clearValidate()
				})
			},
			handleEdit(row) {
				this.$api.job.getSysJobById(row.id).then(res => {
					if (res.code === 200) {
						this.form = res.data.job
						this.isAdd = false
						this.dialogText = "修改任务"
						this.formVisible = true
						this.$nextTick(() => this.$refs.formTable.clearValidate())
					} else
						this.$message.error(res.message)
				})
			},
			handleDelBatch() {
				if (this.ids.length == 0) return
				let text = '此操作将永久删除数据, 是否继续?'
				this.handleConfirm(this.delJobsBatch, this.ids, text)
			},
			handleDel(row) {
				let text = '此操作将永久删除数据, 是否继续?'
				this.handleConfirm(this.delJobsBatch, new Array(row.id), text)
			},
			handleStatusChange(row) {
				let text = row.status ? '启用' : '停用'
				let color = row.status ? '#67C23A' : '#ff0000'
				this.$confirm('<strong>确认要 <span style="color: ' + color + '">' + text + '</span><i>《' + row.jobName +
					'》</i> 任务吗？</strong>', '提示', {
						type: 'warning',
						dangerouslyUseHTMLString: true,
						confirmButtonText: '确定',
						cancelButtonText: '取消',
						center: true,
					}).then(() => {
					this.$api.job.changeStatus(row).then(res => {
						if (res.code == 200) {
							this.$message.success(res.message)
						} else {
							this.$message.error(res.message)
							row.status = !row.status
						}
					})
				}).catch((e) => {
					row.status = !row.status
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
			addSysJob() {
				this.$api.job.addSysJob(this.form).then(res => {
					if (res.code === 200) {
						this.handleQuery()
						this.$message.success(res.message)
						this.formVisible = false;
					} else
						this.$message.error(res.message)
				})
			},
			editSysJob() {
				this.$api.job.editSysJob(this.form).then(res => {
					if (res.code === 200) {
						this.handleQuery()
						this.$message.success(res.message)
						this.formVisible = false;
					} else
						this.$message.error(res.message)
				})
			},
			delJobsBatch(ids) {
				this.$api.job.delSysJobByIds(ids).then(res => {
					if (res.code === 200) {
						this.handleQuery()
						this.$message.success(res.message)
					} else
						this.$message.error(res.message)
				})
			},
			handleSelectionChange(section) {
				this.ids = section.map(val => val.id)
			},
			clearSelection() {
				this.$refs.sysJobTable.clearSelection()
				this.ids = []
			},
			handleCommand(command, row) {
				switch (command) {
					case "handleRun":
						this.handleRun(row);
						break;
					case "handleView":
						this.handleView(row);
						break;
					case "handleJobLog":
						this.handleJobLog(row);
						break;
					default:
						break;
				}
			},
			handleRun(row) {
				let text = '确定要立即执行一次《' + row.jobName + '》任务吗？'
				this.handleConfirm(this.doRun, row, text)
			},
			handleView(row) {
				this.$api.job.getSysJobById(row.id).then(res => {
					if (res.code === 200) {
						this.form = res.data.job
						this.openView = true
					} else
						this.$message.error(res.message)
				})
			},
			handleJobLog(row) {
				this.tableVisible = true
				this.$nextTick(()=>{
					this.$refs.JobLog.queryParams.jobId = row.id
					this.$refs.JobLog.handleQuery()
				})
			},
			dialogOpen() {
				
			},
			doRun(row) {
				this.$api.job.run(row).then(res => {
					if (res.code == 200) {
						this.$message.success(res.message)
					} else
						this.$message.error(res.message)
				})
			},
			handleShowCron() {
				this.expression = this.form.cronExpression;
				this.openCron = true;
			},
			crontabFill(value) {
				this.form.cronExpression = value;
				this.$nextTick(() => this.$refs.formTable.validateField('cronExpression'))
			},
		}
	}
</script>

<style scoped>

</style>
