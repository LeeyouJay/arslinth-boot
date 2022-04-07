<template>
	<div class="view-page">
		<div class="header-bar">
			<el-form ref="formTable" :model="queryParams" @submit.native.prevent>
				<el-row :gutter="20">
					<el-col :span="6">
						<el-form-item label-width="0px">
							<el-input v-model="queryParams.dictName" placeholder="参数名称/值" clearable
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
					</div>
					<right-toolbar @queryTable="getData" ></right-toolbar>
				</div>
			</el-header>
			<el-main>
				<el-table :data="tableData" v-loading="loading" class="table" ref="dictTable"
					header-cell-class-name="table-header">
					<el-table-column prop="indexNum" align="center" label="排序" width="80" />
					<el-table-column prop="dictName"  align="center" label="字典名称" />
					<el-table-column label="字典代号"  align="center" >
						 <template slot-scope="scope">
							 <div class="link-type" @click="toDictValue(scope.row)" >{{scope.row.dictValue}}</div>
						 </template>
					</el-table-column>
					<el-table-column prop="notes" align="center"  label="描述" />
					<el-table-column prop="createTime"  align="center" label="创建时间" />
					<el-table-column label="操作" width="180" fixed="right">
						<template slot-scope="scope">
							<el-button type="text" icon="el-icon-user" @click="handleEdit(scope.row)">修改</el-button>
							<el-button type="text" icon="el-icon-delete" class="red" v-hasPermi="['DelDict']" @click="handleDel(scope.row)">删除
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div class="pagination">
					 <Pagination :total="pageTotal" :pageIndex.sync="page.pageIndex" :pageSize.sync="page.pageSize" @pagination="getData" />
				</div>
			</el-main>
		</el-container>

		<!-- 编辑弹出框 -->
		<el-dialog :visible.sync="formVisible" width="35%" append-to-body  center>
			<template slot="title">
				<span class="dialog-title">{{dialogText}}</span>
			</template>
			<el-form ref="formTable" :model="form" label-width="80px" :disabled="!$utils.checkPermi(['AddDict','EditDict'])">
				<el-row :gutter="20">
					<el-col :span="24">
						<el-form-item label="字典名称"  prop="dictName" :rules="[{required: true, message: '请输入字典名称', trigger: 'blur' }]">
							<el-input v-model="form.dictName" placeholder="请输入字典名称"></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="24">
						<el-form-item label="字典代号" prop="dictValue" :rules="[{required: true, message: '请输入字典代号', trigger: 'blur' }]">
							<el-input v-model="form.dictValue" placeholder="请输入字典代号" ></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="24">
						<el-form-item label="排序" prop="indexNum" :rules="[{required: true, message: '请输入排序', trigger: 'blur' }]">
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

		<el-dialog :visible.sync="tableVisible" @open="dialogOpen" fullscreen append-to-body center>
			<template slot="title">
				<span class="dialog-title">字典数据</span>
			</template>
			<div class="dialog-body scroll-bar">
				<DictValue ref="dictValueList"></DictValue>
			</div>
			<span slot="footer" class="dialog-footer">
				<el-button type="primary" @click="tableVisible = false">关闭</el-button>
			</span>
		</el-dialog>
	</div>
</template>

<script>

	import DictValue from './DictValue.vue'

	export default {
		name: 'SysDict',
		components:{
			DictValue
		},
		data() {
			return {
				isAdd: false,
				loading: false,
				tableVisible: false,
				tableData: [],
				parentId:'',
				parentValue: '',
				dictName: '',
				queryParams: {
					dictName: '',
				},
				page: {
					pageIndex: 1,
					pageSize: 10
				},
				form: {
					indexNum: 0,
				},
				formVisible: false,
				pageTotal: 0,
				dialogText: '添加字典',
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
				this.$api.dict.getTypePage(this.queryParams, this.page).then(res => {
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
						this.isAdd ? this.addDict() : this.editDict()
					} else {
						console.log('验证不通过');
						return false;
					}
				});
			},
			toDictValue(row) {
				this.parentId = row.id
				this.parentValue = row.dictValue
				this.dictName = row.dictName
				this.tableVisible = true
			},
			dialogOpen(){
				this.$nextTick(()=>{
					this.$refs.dictValueList.queryParams = {
						parentId: this.parentId,
						dictName: '',
						parentValue: this.parentValue
					}
					this.$refs.dictValueList.page = {
						pageIndex: 1,
						pageSize: 10
					}
					this.$refs.dictValueList.label = this.dictName
					this.$refs.dictValueList.handleQuery()
				})
			},
			handleAdd() {
				this.isAdd = true
				this.dialogText = "添加字典"
				this.formVisible = true
				this.$nextTick(()=> {
					this.form = Object.assign({},this.$options.data().form);
					this.$refs.formTable.clearValidate()
					this.form.indexNum = this.pageTotal +1
				})
			},
			handleEdit(row) {
				this.$api.dict.getDictById(row.id).then(res=>{
					if(res.code === 200){
						this.form = res.data.dict
						this.isAdd = false
						this.dialogText = "修改字典"
						this.formVisible = true
						this.$nextTick(()=>this.$refs.formTable.clearValidate())
					}else
						this.$message.error(res.message)
				})
			},
			handleDel(row) {
				this.$confirm('删除数据将不可恢复，确定要删除吗？', '提示', {
						type: 'warning'
					})
					.then(() => {
						this.delDict(row.id)
					})
					.catch((e) => {
						console.log(e)
					});
			},
			addDict(){
				this.$api.dict.addDict(this.form).then(res=>{
					if(res.code === 200 ){
						this.handleQuery()
						this.$message.success(res.message)
						this.formVisible = false;
					}else
						this.$message.error(res.message)
				})
			},
			editDict(){
				this.$api.dict.editDict(this.form).then(res=>{
					if(res.code === 200 ){
						this.handleQuery()
						this.$message.success(res.message)
						this.formVisible = false;
					}else
						this.$message.error(res.message)
				})
			},
			delDict(id){
				this.$api.dict.delDict(id).then(res=>{
					if(res.code === 200 ){
						this.handleQuery()
						this.$message.success(res.message)
					}else
						this.$message.error(res.message)
				})
			},
		}
	}
</script>

<style scoped>

</style>
