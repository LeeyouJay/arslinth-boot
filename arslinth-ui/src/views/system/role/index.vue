<template>
	<div class="view-page">
		<div class="view-page-bar">
			<el-form ref="formTable" :model="queryParams" @submit.native.prevent>
				<el-row :gutter="20">
					<el-col :span="6">
						<el-form-item label-width="0px">
							<el-input v-model="queryParams.roleName" placeholder="角色名称/权限字符" clearable
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
						<el-button type="primary" icon="el-icon-plus" size="mini" plain @click="handleAdd">新增</el-button>
						<el-button type="danger" icon="el-icon-delete" size="mini" plain :disabled="ids.length == 0"
							@click="handleDelBatch">删除</el-button>
					</div>
					<right-toolbar @queryTable="getData" ></right-toolbar>
				</div>
			</el-header>
			<el-main>
				<el-table :data="tableData" v-loading="loading" class="table" ref="multipleTable"
					header-cell-class-name="table-header" @selection-change="handleSelectionChange">
					<el-table-column type="selection" width="55" align="center" />
					<el-table-column prop="roleName" label="角色名称" />
					<el-table-column prop="role" label="权限字符" />
					<el-table-column prop="indexNum" label="显示排序" />
					<el-table-column prop="notes" label="备注" />
					<el-table-column prop="createTime" label="创建时间" />
					<el-table-column label="操作" width="180" fixed="right">
						<template slot-scope="scope">
							<el-button type="text" icon="el-icon-solid" @click="handleEdit(scope.row)">修改</el-button>
							<el-button type="text" icon="el-icon-delete" class="red" v-hasPermi="['DelRole']" @click="handleDel(scope.row)">
								删除
							</el-button>
							<el-dropdown size="mini" @command="(command) => handleCommand(command, scope.row)">
								<el-button type="text" icon="el-icon-d-arrow-right el-icon--right">更多</el-button>
								<el-dropdown-menu slot="dropdown">
									<el-dropdown-item command="handleAuthRole" icon="el-icon-key">权限设置
									</el-dropdown-item>
									<el-dropdown-item command="handleAuthUser" icon="el-icon-circle-check">分配用户
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
		<el-dialog :visible.sync="formVisible" width="35%" append-to-body center>
			<template slot="title">
				<span class="dialog-title">{{dialogText}}</span>
			</template>
			<el-form ref="formTable" :model="form" label-width="80px" :disabled="!$utils.checkPermi(['AddRole','EditRole'])">
				<el-row :gutter="20">
					<el-col :span="12">
						<el-form-item label="角色名称" prop="roleName"
							:rules="[{required: true, message: '请输入角色名称', trigger: 'blur' }]">
							<el-input v-model="form.roleName" placeholder="请输入角色名称"></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="权限字符" prop="role"
							:rules="[{required: true, message: '请输入角色名称', trigger: 'blur' }]">
							<el-input v-model="form.role" placeholder="请输入权限字符"></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="排序" prop="indexNum"
							:rules="[{required: true, message: '请输入排序', trigger: 'blur' }]">
							<el-input-number v-model="form.indexNum" :min="0"></el-input-number>
						</el-form-item>
					</el-col>
					<el-col :span="24">
						<el-form-item label="描述">
							<el-input type="textarea" v-model="form.notes" placeholder="请输入描述" />
						</el-form-item>
					</el-col>
					<el-col :span="24">
						<el-form-item label="权限">
							<auth-select ref="authTree" :disabled="!$utils.checkPermi(['AddRole','EditRole'])"
							:strictly.sync="form.strictly" :permissions.sync="form.permissions"></auth-select>
						</el-form-item>
					</el-col>
				</el-row>
			</el-form>
			<span slot="footer" class="dialog-footer">
				<el-button @click="formVisible = false">取 消</el-button>
				<el-button type="primary" @click="submit">确 定</el-button>
			</span>
		</el-dialog>

		<!-- 更改权限 -->
		<el-dialog v-dialogDrag :visible.sync="authVisible" width="35%" append-to-body center>
			<template slot="title">
				<span class="dialog-title">{{authDialogText}}</span>
			</template>
			<auth-select ref="authTree" positiion='center':disabled="!$utils.checkPermi(['AddRole','EditRole'])"
			:strictly.sync="form.strictly" :permissions.sync="form.permissions"></auth-select>
			<span slot="footer" class="dialog-footer">
				<el-button @click="authVisible = false">取 消</el-button>
				<el-button type="primary" @click="authSubmit">确 定</el-button>
			</span>
		</el-dialog>
	</div>
</template>

<script>

	import AuthSelect from '../components/auth-select'

	export default {
		name: 'SysRole',
		components: {
			AuthSelect
		},
		data() {
			return {
				isAdd: false,
				loading: false,
				formVisible: false,
				authVisible: false,
				tableData: [],
				queryParams: {
					roleName: '',
				},
				page: {
					pageIndex: 1,
					pageSize: 10
				},
				form: {
					indexNum: 0
				},
				authBox: {
					expand: true,
					nodeAll: false,
					checkStrictly: true
				},
				pageTotal: 0,
				dialogText: '添加角色',
				authDialogText: '更改角色权限',
				ids: []
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
				this.$api.role.getRolePage(this.queryParams, this.page).then(res => {
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
						this.isAdd ? this.addRole() : this.editRole()
					} else {
						console.log('验证不通过');
						return false;
					}
				});
			},
			authSubmit() {
				if (this.$refs.authTree.disabled) {
					this.authVisible = false
					return
				}
				this.$refs.formTable.validate((valid) => {
					if (valid) {
						this.isAdd ? this.addRole() : this.editRole()
					} else {
						console.log('验证不通过');
						return false;
					}
				});
			},
			handleAdd() {
				this.isAdd = true
				this.dialogText = "添加用户"
				this.formVisible = true
				this.$nextTick(() => {
					this.form = Object.assign({}, this.$options.data().form);
					this.$refs.formTable.clearValidate()
					this.$refs.authTree.init()
					this.form.indexNum = this.pageTotal + 1
				})
			},
			handleEdit(row) {
				this.$api.role.getRoleById(row.id).then(res => {
					if (res.code === 200) {
						this.form = res.data.role
						this.isAdd = false
						this.dialogText = "修改用户信息"
						this.formVisible = true
						this.$nextTick(() =>{
							this.$refs.authTree.init()
							this.$refs.formTable.clearValidate()
						})
					} else
						this.$message.error(res.message)
				})
			},
			handleDel(row) {
				this.handleConfirm(this.delRole, row, '此操作将永久删除数据, 是否继续?')
			},
			handleDelBatch() {
				if (this.ids.length == 0) return
				this.handleConfirm(this.delRoleBatch, null, "此操作将永久删除数据, 是否继续?")
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

			addRole() {
				this.$api.role.addRole(this.form).then(res => {
					if (res.code === 200) {
						this.handleQuery()
						this.$message.success(res.message)
						this.formVisible = false;
						this.authVisible = false
					} else
						this.$message.error(res.message)
				})
			},
			editRole() {
				this.$api.role.editRole(this.form).then(res => {
					if (res.code === 200) {
						this.handleQuery()
						this.$message.success(res.message)
						this.formVisible = false;
						this.authVisible = false
					} else
						this.$message.error(res.message)
				})
			},
			delRole(row) {
				this.$api.role.delRole(row.id).then(res => {
					if (res.code === 200) {
						this.handleQuery()
						this.$message.success(res.message)
						this.formVisible = false;
					} else
						this.$message.error(res.message)
				})
			},
			delRoleBatch() {
				this.$api.role.delRoleByIds(this.ids).then(res => {
					if (res.code === 200) {
						this.handleQuery()
						this.$message.success(res.message)
						this.formVisible = false;
					} else
						this.$message.error(res.message)
				})
			},
			handleSelectionChange(section) {
				this.ids = section.map(val => val.id)
			},
			clearSelection() {
				this.$refs.multipleTable.clearSelection()
				this.ids = []
			},
			// 更多操作触发
			handleCommand(command, row) {
				switch (command) {
					case "handleAuthRole":
						this.handleAuthRole(row)
						break;
					case "handleAuthUser":
						console.log("分配用户")
						break;
					default:
						break;
				}
			},
			async handleAuthRole(row) {
				let res = await this.$api.role.getRoleById(row.id)
				if(res.code == 200) {
					this.form = res.data.role
					this.authVisible = true
					this.isAdd = false
					this.authDialogText = "更改角色权限"
					this.$nextTick(()=>{
						this.$refs.authTree.init()
					})
				} else
					this.$message.error(res.message)

			},
		}
	}
</script>
<style scoped>

</style>
