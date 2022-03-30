<template>
	<div class="view-page">
		<div class="header-bar">
			<el-form ref="searchTable" :model="queryParams" @submit.native.prevent>
				<el-row :gutter="20">
					<el-col :span="4">
						<el-form-item label="上级标签"  label-width="80px">
							<el-select v-model="label" placeholder="请选择上级字典名称" @change="selectChange">
							    <el-option v-for="item in selectData"  :key="item.id" :label="item.dictName"  :value="item.id">
							    </el-option>
							  </el-select>
						</el-form-item>
					</el-col>
					<el-col :span="6">
						<el-form-item label="字典标签/值"  label-width="100px">
							<el-input v-model="queryParams.dictName" placeholder="请输入字典标签或值" clearable
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
						<el-button type="primary" icon="el-icon-plus" size="mini" plain @click="handleAdd">新增</el-button>
						<el-button type="danger" icon="el-icon-delete" size="mini" plain :disabled="ids.length == 0" @click="handleDelBatch">删除</el-button>
					</div>
				</el-header>
				<el-main>
					<el-table :data="tableData" v-loading="loading" class="table" ref="dictTable" row-key="id"
						header-cell-class-name="table-header"
						@selection-change="handleSelectionChange" >
						<el-table-column type="selection" :reserve-selection="true" width="55" align="center" />
						<el-table-column prop="indexNum" align="center" label="排序" width="80" />
						<el-table-column prop="dictName"  align="center" label="字典标签" />
						<el-table-column prop="dictValue" label="字典值"  align="center" />
						<el-table-column prop="notes" align="center"  label="描述" />
						<el-table-column prop="createTime"  align="center" label="创建时间" />
						<el-table-column label="操作" width="180" fixed="right">
							<template slot-scope="scope">
								<el-button type="text" icon="el-icon-user" @click="handleEdit(scope.row)">修改</el-button>
								<el-button type="text" icon="el-icon-delete" class="red" @click="handleDel(scope.row)">删除
								</el-button>
							</template>
						</el-table-column>
					</el-table>
					<div class="pagination">
						<el-pagination @size-change="handleSizeChange" @current-change="handlePageChange"
							:current-page="queryParams.pageIndex" :page-sizes="[10, 30, 100, 500]"
							:page-size="queryParams.pageSize" layout="total, sizes, prev, pager, next" :total="pageTotal">
						</el-pagination>
					</div>
				</el-main>
			</el-container>
		</div>
		<!-- 编辑弹出框 -->
		<el-dialog :visible.sync="editVisible" width="35%" append-to-body center>
			<template slot="title">
				<span class="dialog-title">{{dialogText}}</span>
			</template>
			<el-form ref="formTable" :model="form" label-width="80px">
				<el-row :gutter="20">
					<el-col :span="24">
						<el-form-item label="字典标签" prop="dictName" :rules="[{required: true, message: '请输入字典名称', trigger: 'blur' }]">
							<el-input v-model="form.dictName"></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="24">
						<el-form-item label="字典值" prop="dictValue" :rules="[{required: true, message: '请输入字典代号', trigger: 'blur' }]">
							<el-input v-model="form.dictValue"></el-input>
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
				<el-button @click="editVisible = false">取 消</el-button>
				<el-button type="primary" @click="submit">确 定</el-button>
			</span>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				isAdd: false,
				loading: false,
				tableVisible: false,
				tableData: [],
				selectData: [],
				ids: [],
				label:'',
				queryParams: {
					parentId:'0',
					parentValue:'',
					dictName: '',
					pageIndex: 1,
					pageSize: 10
				},
				form: {
					indexNum: 0,
				},
				editVisible: false,
				pageTotal: 0,
				dialogText: '添加字典',
			}
		},
		methods: {
			handleQuery() {
				this.queryParams.pageIndex =1
				this.queryParams.pageSize =10
				this.clearSelection()
				this.getData()
				this.getSelection()
			},
			getData() {
				this.loading = true
				this.$api.dict.getValuePage(this.queryParams).then(res => {
					if (res.code === 200) {
						this.tableData = res.data.list
						this.pageTotal = res.data.total
					} else
						this.$message.error(res.message)
					this.loading = false
				})
			},
			getSelection(){
				this.$api.dict.getTypeList().then(res => {
					if (res.code === 200) {
						this.selectData = res.data.list
					} else
						this.$message.error(res.message)
				})
			},
			handleSizeChange(val) {
				this.$set(this.queryParams, 'pageSize', val);
				this.getData();
			},
			handlePageChange(val) {
				this.$set(this.queryParams, 'pageIndex', val);
				this.getData();
			},
			submit() {
				this.$refs.formTable.validate((valid) => {
					if (valid) {
						this.isAdd ? this.addDict() : this.editDict()
					} else {
						console.log('验证不通过');
						return false;
					}
				});
			},
			handleAdd() {
				this.isAdd = true
				this.dialogText = "修改字典"
				this.editVisible = true
				this.$nextTick(()=> {
					this.form = Object.assign({},this.$options.data().form);
					this.$refs.formTable.clearValidate()
					this.form.parentId = this.queryParams.parentId
					this.form.parentValue = this.queryParams.parentValue
					this.form.indexNum = this.pageTotal +1
				})
			},
			handleEdit(row) {
				this.$api.dict.getDictById(row.id).then(res=>{
					if(res.code === 200){
						this.form = res.data.dict
						this.isAdd = false
						this.dialogText = "修改字典"
						this.editVisible = true
					}else
						this.$message.error(res.message)
				})
			},
			handleDel(row) {
				this.handleConfirm(this.delDict,row)
			},
			handleDelBatch() {
				if(this.ids.length == 0) return
				this.handleConfirm(this.delDictBatch)
			},
			handleConfirm(func, arg){
				this.$confirm('此操作将永久删除数据, 是否继续?', "提示", {
					type: 'warning',
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					center: true,
				}).then(() => {
					func(arg)
				}).catch((e) => {console.log(e)})
			},
			addDict(){
				this.$api.dict.addDict(this.form).then(res=>{
					if(res.code === 200 ){
						this.handleQuery()
						this.$message.success(res.message)
						this.editVisible = false;
					}else
						this.$message.error(res.message)
				})
			},
			editDict(){
				this.$api.dict.editDict(this.form).then(res=>{
					if(res.code === 200 ){
						this.getData()
						this.$message.success(res.message)
						this.editVisible = false;
					}else
						this.$message.error(res.message)
				})
			},
			delDict(row){
				this.$api.dict.delDict(row.id).then(res=>{
					if(res.code === 200 ){
						this.handleQuery()
						this.$message.success(res.message)
						this.editVisible = false;
					}else
						this.$message.error(res.message)
				})
			},
			delDictBatch(){
				this.$api.dict.delDictByIds(this.ids).then(res=>{
					if(res.code === 200 ){
						this.handleQuery()
						this.clearSelection()
						this.$message.success(res.message)
						this.editVisible = false;
					}else
						this.$message.error(res.message)
				})
			},
			handleSelectionChange(section) {
				this.ids = section.map(val => val.id)
			},
			clearSelection() {
				this.$refs.dictTable.clearSelection()
				this.ids = []
			},
			selectChange(val) {
				this.queryParams.parentId = val
				this.handleQuery()
			}
		}
		
	}
</script>

<style>
</style>
