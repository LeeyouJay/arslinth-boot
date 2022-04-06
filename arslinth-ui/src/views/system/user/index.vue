<template>
	<div class="view-page">
		<div class="header-bar">
			<el-form ref="searchTable" :model="queryParams" @submit.native.prevent>
				<el-row :gutter="20">
					<el-col :span="4">
						<el-form-item label-width="0px">
							<el-select v-model="queryParams.forbidden" placeholder="状态" clearable @clear="handleQuery"
								@change="handleQuery">
								<el-option :value="0" label="正常"></el-option>
								<el-option :value="1" label="停用"></el-option>
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="6">
						<el-form-item label-width="0px">
							<el-input v-model="queryParams.nickName" placeholder="昵称/账号/手机号" clearable
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
			<el-container>
				<el-header height="auto">
					<div class="table-bar">
						<div class="table-btn">
							<el-button type="primary" icon="el-icon-plus" size="mini" plain @click="handleAdd">新增
							</el-button>
							<el-button type="danger" icon="el-icon-delete" size="mini" plain :disabled="ids.length == 0"
								@click="handleDelBatch">删除</el-button>
						</div>
						<right-toolbar @queryTable="getData" ></right-toolbar>
					</div>
				</el-header>
				<el-main>
					<el-table :data="tableData" v-loading="loading" class="table" ref="userTable" row-key="id"
						header-cell-class-name="table-header" @selection-change="handleSelectionChange">
						<el-table-column type="selection" :reserve-selection="true" width="55" align="center" />
						<el-table-column prop="indexNum" label="显示排序" align="center"></el-table-column>
						<el-table-column align="center" label="头像">
							<template slot-scope="scope">
								<el-image class="table-avatar" :src="$requestUrl + scope.row.avatar"
									:preview-src-list="[$requestUrl +scope.row.avatar]"></el-image>
							</template>
						</el-table-column>
						<el-table-column prop="username" label="账号" align="center"></el-table-column>
						<el-table-column prop="nickName" label="昵称" align="center"></el-table-column>
						<el-table-column prop="sex" label="性别" align="center"></el-table-column>
						<el-table-column prop="phone" label="手机号码" min-width="100" align="center"></el-table-column>
						<el-table-column prop="email" label="邮箱" min-width="120" align="center"></el-table-column>
						<el-table-column label="禁用状态" align="center">
							<template slot-scope="scope">
								<el-switch v-model="scope.row.forbidden" @change="stateChange(scope.row)">
								</el-switch>
							</template>
						</el-table-column>
						<el-table-column prop="createTime" label="创建时间" min-width="150" align="center" />
						<el-table-column label="操作" width="180" fixed="right">
							<template slot-scope="scope" v-if="scope.row.id != 1">
								<el-button type="text" icon="el-icon-user" @click="handleEdit(scope.row)">修改</el-button>
								<el-button type="text" icon="el-icon-delete" class="red" v-hasPermi="['DelUser']" @click="handleDel(scope.row)">
									删除
								</el-button>
								<el-dropdown size="mini" @command="(command) => handleCommand(command, scope.row)">
									<el-button type="text" icon="el-icon-d-arrow-right el-icon--right">更多</el-button>
									<el-dropdown-menu slot="dropdown">
										<el-dropdown-item command="handleResetPwd" icon="el-icon-key" v-hasPermi="['ResetPassword']">重置密码</el-dropdown-item>
										<el-dropdown-item command="handleAuths" icon="el-icon-circle-check">操作权限</el-dropdown-item>
										<el-dropdown-item command="handleDataScope" icon="el-icon-data-analysis">数据权限</el-dropdown-item>
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
		</div>
		<!-- 编辑弹出框 -->
		<el-dialog :visible.sync="formVisible" width="35%" append-to-body center>
			<template slot="title">
				<span class="dialog-title">{{dialogText}}</span>
			</template>
			<el-form ref="formTable" :model="form" label-width="80px" :disabled="!$utils.checkPermi(['EditUser'])">
				<el-row :gutter="20">
					<el-col :span="12">
						<el-form-item label="登入账号" prop="username" :rules="[{required: true, message: '请输入登入账号', trigger: 'blur' }]">
							<el-input v-model="form.username" placeholder="请输入登入账号"></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="用户昵称">
							<el-input v-model="form.nickName" placeholder="请输入用户昵称"></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="手机号码">
							<el-input v-model="form.phone" placeholder="请输入手机号码"></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="邮箱">
							<el-input v-model="form.email" placeholder="请输入邮箱"></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="性别">
							<el-select v-model="form.sex" placeholder="请选择性别" clearable>
								<el-option value="m" label="男"></el-option>
								<el-option value="w" label="女"></el-option>
								<el-option value="s" label="保密"></el-option>
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="角色">
							<el-select v-model="form.roleIds" multiple placeholder="请选择所属角色" clearable>
								<el-option v-for="item in roleList"  :key="item.id" :label="item.roleName"  :value="item.id">
								</el-option>
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="状态">
							<el-radio-group v-model="form.forbidden">
								<el-radio :label="true">禁用</el-radio>
								<el-radio :label="false">正常</el-radio>
							</el-radio-group>
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
				</el-row>
			</el-form>
			<span slot="footer" class="dialog-footer">
				<el-button @click="formVisible = false">取 消</el-button>
				<el-button type="primary" @click="submit">确 定</el-button>
			</span>
		</el-dialog>

		<!-- 更改权限 -->
		<el-dialog :visible.sync="authVisible" width="35%" append-to-body center>
			<template slot="title">
				<span class="dialog-title">{{authDialogText}}</span>
			</template>
			<auth-select ref="authTree" positiion='center':disabled="!$utils.checkPermi(['SetPermissions'])"
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
		name: 'SysUser',
		components: {
			AuthSelect
		},
		data() {
			return {
				isAdd: false,
				loading: false,
				formVisible: false,
				tableData: [],
				roleList:[],
				queryParams: {
					nickName: '',
				},
				page: {
					pageIndex: 1,
					pageSize: 10
				},
				authBox: {
					expand: true,
					nodeAll: false,
				},
				form: {
					indexNum: 0,
					strictly: false,
					forbidden: false,
					permissions: []
				},
				pageTotal: 0,
				dialogText: '添加用户',
				ids: [],
				authVisible: false,
				authDialogText: '更改操作权限'
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
				this.$api.user.getUserPage(this.queryParams, this.page).then(res => {
					if (res.code === 200) {
						this.tableData = res.data.list
						this.pageTotal = res.data.total
					} else
						this.$message.error(res.message)
					this.loading = false
				})
			},
			getRole() {
				this.$api.role.getRoleList(null).then(res => {
					if (res.code === 200) {
						this.roleList = res.data.list
					} else
						this.$message.error(res.message)
				})
			},
			submit() {
				if (this.$refs.formTable.disabled) {
					this.formVisible = false
					return
				}
				this.$refs.formTable.validate((valid) => {
					if (valid) {
						this.isAdd ? this.addUser() : this.editUser()
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

				this.$api.user.setPermissions(this.form).then(res => {
					if (res.code === 200) {
						this.$message.success(res.message)
						this.authVisible = false;
					} else
						this.$message.error(res.message)
				})
			},
			handleAdd() {
				this.isAdd = true
				this.dialogText = "添加用户"
				this.formVisible = true
				this.getRole()
				this.$nextTick(() => {
					this.form = Object.assign({}, this.$options.data().form)
					this.$refs.formTable.clearValidate()
					this.form.indexNum = this.pageTotal + 1
				})
			},
			handleEdit(row) {
				this.getRole()
				this.$api.user.getUserById(row.id).then(res => {
					if (res.code === 200) {
						this.form = res.data.user
						this.isAdd = false
						this.dialogText = "修改用户信息"
						this.formVisible = true
						this.$nextTick(() => {
							this.$refs.formTable.clearValidate()
						})
					} else
						this.$message.error(res.message)
				})
			},
			handleDel(row) {
				this.handleConfirm(this.delUser, row, '此操作将永久删除数据, 是否继续?')
			},
			handleDelBatch() {
				if (this.ids.length == 0) return
				this.handleConfirm(this.delUserBatch, null, "此操作将永久删除数据, 是否继续?")
			},
			handleReset(row) {
				this.handleConfirm(this.resetUser, row, '确定将要次用户的密码重置为123456吗？')
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
			addUser() {
				this.$api.user.addUser(this.form).then(res => {
					if (res.code === 200) {
						this.handleQuery()
						this.$message.success(res.message)
						this.formVisible = false;
					} else
						this.$message.error(res.message)
				})
			},
			editUser() {
				this.$api.user.editUser(this.form).then(res => {
					if (res.code === 200) {
						this.handleQuery()
						this.$message.success(res.message)
						this.formVisible = false;
					} else
						this.$message.error(res.message)
				})
			},
			delUser(row) {
				this.$api.user.delUser(row.id).then(res => {
					if (res.code === 200) {
						this.handleQuery()
						this.$message.success(res.message)
						this.formVisible = false;
					} else
						this.$message.error(res.message)
				})
			},
			delUserBatch() {
				this.$api.user.delUserByIds(this.ids).then(res => {
					if (res.code === 200) {
						this.handleQuery()
						this.$message.success(res.message)
						this.formVisible = false;
					} else
						this.$message.error(res.message)
				})
			},
			resetUser(row) {
				this.$api.user.resetPassword(row).then(res => {
					if (res.code === 200) {
						this.$message.success(res.message)
					} else
						this.$message.error(res.message)
				})
			},
			stateChange(row) {
				this.$api.user.editUser(row).then(res => {
					if (res.code === 200) {
						this.$message.success(res.message)
					} else
						this.$message.error(res.message)
				})
			},
			handleSelectionChange(section) {
				this.ids = section.map(val => val.id)
			},
			clearSelection() {
				this.$refs.userTable.clearSelection()
				this.ids = []
			},
			// 更多操作触发
			handleCommand(command, row) {
				switch (command) {
					case "handleResetPwd":
						this.handleReset(row);
						break;
					case "handleAuths":
						this.handleAuths(row)
						break;
					case "handleDataScope":
						this.handleDataScope(row)
						break;
					default:
						break;
				}
			},
			async handleAuths(row) {
				let res = await this.$api.user.getUserById(row.id)
				if(res.code == 200) {
					this.form = res.data.user
					this.authVisible = true
					this.isAdd = false
					this.authDialogText = "更改操作权限"
					this.$nextTick(()=>{
						this.$refs.authTree.init()
					})
				} else
					this.$message.error(res.message)

			},
			handleDataScope(row){

			},
			preview(avatar) {
				return avatar ? [this.$requestUrl + avatar.substr(0, avatar.lastIndexOf('-')) + '.jpg'] : []
			},
		}
	}
</script>

<style scoped>
	.table-avatar {
		display: block;
		margin: auto;
		width: 40px;
		height: 40px;
		border-radius: 50%;
	}
</style>
