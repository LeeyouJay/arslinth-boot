<template>
	<div class="view-page">
		<div class="header-bar">
			<el-form ref="formTable" :model="queryParams" label-width="80px">
				<el-row :gutter="20">
					<el-col :span="6">
						<el-form-item label="菜单名称">
							<el-input v-model="queryParams.label" placeholder="请输入菜单名称" clearable ></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="6">
						<el-form-item label-width="0px">
							<el-button type="primary" @click="handleQuery">搜索</el-button>
						</el-form-item>
					</el-col>
				</el-row>
			</el-form>
		</div>
		<el-container>
			<el-header>
				<div class="header-bar">
					<el-button type="primary"  size="mini" plain @click="handleAdd">添加菜单</el-button>
				</div>
			</el-header>
			<el-main>
				<el-table :data="tableData" v-loading="loading" class="table" row-key="id" ref="menuTable" header-cell-class-name="table-header">
					<el-table-column prop="label" align="left" label="菜单名称" width="200"></el-table-column>
					<el-table-column align="center" label="图标" >
						<template slot-scope="scope">
							<i :class="scope.row.meta.icon"></i>
						</template>
					</el-table-column>
					<el-table-column prop="indexNum" label="排序"></el-table-column>
					<el-table-column prop="name" label="权限标识"></el-table-column>
					<el-table-column prop="indexNum" label="排序"></el-table-column>
					<el-table-column prop="menuType" align="center" label="类型"></el-table-column>
					<el-table-column prop="createTime" align="center" label="创建时间"></el-table-column>
					<el-table-column label="操作" width="180" align="center">
						<template slot-scope="scope">
							<el-button type="text" icon="el-icon-edit" @click="handleEdit(scope.$index, scope.row)">修改</el-button>
							<el-button v-if="!scope.row.children || scope.row.children.length == 0" type="text" icon="el-icon-delete" class="red" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-main>
		</el-container>
		<!-- 编辑弹出框 -->
		<el-dialog :visible.sync="formVisible" width="43%" center>
			<template slot="title">
				<span class="dialog-title">{{formTitle}}</span>
			</template>
			<el-form ref="formTable" :model="form" label-width="80px">
				<el-row :gutter="20">
					<el-col :span="24">
						<el-form-item label="上级菜单" key="0">
							<el-select v-model="label" placeholder="点击选择页面" ref="selectUpResId">
								<el-option hidden :value="form.parentId" :label="label" style="height: auto">
								</el-option>
								<el-tree :data="tableTree" node-key="id" ref="tree" highlight-current
									expand-on-click-node check-on-click-node default-expand-all @node-click="handleNodeClick"></el-tree>
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="24">
						<el-form-item label="类型" key="1" >
							<el-radio-group v-model="form.menuType" @change="radioChange()" >
								<el-radio label="M">目录</el-radio>
								<el-radio label="C">菜单</el-radio>
								<el-radio label="N">内链</el-radio>
								<el-radio label="O">外链</el-radio>
								<el-radio label="F">按钮</el-radio>
							</el-radio-group>
						</el-form-item>
					</el-col>
					<el-col :span="24" v-if="form.menuType != 'F'">
						<el-form-item label="图标" key="2">
							<e-icon-picker ref="eIconPicker" v-model="form.icon" :options="options" />
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="菜单名称" key="3" prop="label" :rules="[{required: true,message: '请输入菜单名称'}]">
							<el-input v-model="form.label"></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="显示排序" key="4" >
							<el-input-number v-model="form.indexNum" :min="0"></el-input-number>
						</el-form-item>
					</el-col>
					<el-col :span="12" v-if="form.menuType != 'F'">
						<el-form-item label="路径地址" key="5" prop="path" :rules="[{required: true,message: '请输入路径地址'}]">
							<el-input v-model="form.path"></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="权限字符" key="6" prop="name" :rules="[{required: true,message: '请输入权限字符'}]">
							<el-input v-model="form.name"></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="12" v-if="form.menuType == 'N'">
						<el-form-item label="地址链接" key="7" prop="link" :rules="[{required: true,message: '请输入地址链接'}]">
							<el-input v-model="form.link"></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="12" v-if="form.menuType == 'C'">
						<el-form-item label="组件地址" key="8" prop="component" :rules="[{required: true,message: '请输入路径地址'}]">
							<el-input v-model="form.component"></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="12" v-if="form.menuType == 'C'">
						<el-form-item label="是否缓存" key="9">
							<el-radio-group v-model="form.keepAlive">
								<el-radio :label="true">缓存</el-radio>
								<el-radio :label="false">不缓存</el-radio>
							</el-radio-group>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="状态" key="10">
							<el-radio-group v-model="form.hidden">
								<el-radio :label="false">正常</el-radio>
								<el-radio :label="true">停用</el-radio>
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
	</div>
</template>

<script>
	import {
		EIconPicker
	} from 'e-icon-picker';
	export default {
		name: 'SysMenu',
		components: {
			EIconPicker
		},
		data() {
			return {
				isAdd: false,
				formVisible: false,
				loading: false,
				formTitle: '添加菜单',
				queryParams: {
					label:''
				},
				form: {
					parentId: "0",
					menuType: "M",
					keepAlive: false,
					hidden: false,
					level: 0,
					indexNum: 0
				},
				tableTree: [{
					id: '0',
					label: '主目录',
					level: -1,
					children: [],
				}],
				tableData:[],
				label: '主目录',
				options: {
					ElementUI: true,
				}
			}
		},
		created() {
			this.getTreeData(this.queryParams.label)
		},
		watch: {
			formVisible(val) {
				if (!val && this.$refs.eIconPicker) {
					this.$refs.eIconPicker.destroyIconList()
				}
			},
		},
		methods: {
			getTreeData(menuName) {
				this.loading = true
				this.$api.menu.getMenuList(menuName).then(res => {
					if (res.code == 200) {
						let data = res.data.list
						this.tableData = data
						this.tableTree = Object.assign([],this.$options.data().tableTree);
						data.forEach(val => this.tableTree.push(val))
					} else
						this.$message.error(res.message)
					this.loading = false
				})
			},
			handleQuery(){
				this.getTreeData(this.queryParams.label)
			},
			handleAdd() {
				this.isAdd = true
				this.formVisible = true
				this.$nextTick(()=>{
					this.form = Object.assign({},this.$options.data().form);
					this.$refs.formTable.clearValidate()
				})
			},
			handleEdit(index, row){
				this.isAdd = false
				this.$api.menu.getMenuById(row.id).then(res=> {
					if(res.code == 200){
						this.form = res.data.menu
						this.label = this.getParentName(this.form.parentId)
						this.formTitle = '修改菜单'
						this.formVisible = true
					}else
						this.$message.error(res.message)
				})
			},
			handleDelete(index, row){
				this.$confirm('删除菜单将不可恢复，确定要删除吗？', '提示', {
						type: 'warning'
					})
					.then(() => {
						this.delMenu(row.id)
					})
					.catch((e) => {
						console.log(e)
					});
			},
			submit() {
				this.$refs.formTable.validate((valid) => {
					if (valid) {
						this.initFormData (this.form, this.form.menuType)
						this.isAdd ? this.addMenu() : this.editMenu()
					} else {
						console.log('验证不通过');
						return false;
					}
				});
			},
			addMenu() {
				this.$api.menu.addMenu(this.form).then(res=>{
					if(res.code === 200 ){
						this.getTreeData(this.queryParams.label)
						this.$message.success(res.message)
						this.formVisible = false;
					}else
						this.$message.error(res.message)
				})
			},
			editMenu() {
				this.$api.menu.editMenu(this.form).then(res=>{
					if(res.code === 200 ){
						this.getTreeData(this.queryParams.label)
						this.$message.success(res.message)
						this.formVisible = false;
					}else
						this.$message.error(res.message)
				})
			},
			delMenu(id) {
				this.$api.menu.delMenu(id).then(res=>{
					if(res.code === 200 ){
						this.getTreeData(this.queryParams.label)
						this.$message.success(res.message)
					}else
						this.$message.error(res.message)
				})
			},
			handleNodeClick(data) {
				this.label = data.label
				this.form.parentId = data.id
				this.form.level = data.level + 1;
				this.$refs.selectUpResId.blur()
			},
			radioChange(data){
				this.$nextTick(()=>{
					this.$refs.formTable.clearValidate()
				})
			},
			getParentName(parentId){
				let list = this.$utils.treeToArray(this.tableTree)
				for (let i = 0; i < list.length; i++) {
					if(list[i].id === parentId)
						return list[i].label
				}
			},
			initFormData(form, menuType){
				switch(menuType) {
					case 'M':
					form.component = form.parentId == "0"?"Layout": "ParentView"
					this.$delete(form, 'link') 
					this.$delete(form, 'keepAlive') 
					break;
					case 'C':
					
					
					break;
					case 'O':
					form.component = "ParentView"
					this.$delete(form, 'link')
					this.$delete(form, 'keepAlive') 
					break;
					case 'N':
					form.component = "InnerLink"
					this.$delete(form, 'keepAlive') 
					break;
					case 'F':
					
					break;
				}
			}
		}

	}
</script>

<style lang="css" scoped>
	@import '~e-icon-picker/lib/index.css';
	@import '~font-awesome/css/font-awesome.min.css';
	@import '~element-ui/lib/theme-chalk/icon.css';
</style>
