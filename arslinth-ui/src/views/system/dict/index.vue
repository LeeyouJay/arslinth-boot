<template>
	<div class="view-page">
		<div class="header-bar">
			<el-form ref="formTable" :model="queryParams" label-width="80px">
				<el-row :gutter="20">
					<el-col :span="6">
						<el-form-item label="字典名称">
							<el-input v-model="queryParams.dictName" placeholder="请输入字段名称" clearable ></el-input>
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
					<el-button type="primary" size="mini" plain @click="handleAdd">添加字典</el-button>
				</div>
			</el-header>
			<el-main>
				<el-table :data="tableData" v-loading="loading"  class="table" ref="dictTable" 
				 header-cell-class-name="table-header">
					<el-table-column prop="dictName" label="参数名称" />
					<el-table-column prop="dictValue" label="参数值"  />
					<el-table-column prop="parentValue" label="上级参数"  />
					<el-table-column prop="dictType" label="类型"  />
					<el-table-column prop="notes" label="描述"  />
					<el-table-column prop="createTime" label="创建时间"  />
					<el-table-column label="操作" width="180" fixed="right">
						<template slot-scope="scope">
							<el-button type="text" icon="el-icon-user" @click="handleEdit(scope.row)">修改</el-button>
							<el-button type="text" icon="el-icon-delete" class="red" @click="handleDel(scope.row)">删除</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div class="pagination">
					<el-pagination @size-change="handleSizeChange" @current-change="handlePageChange"
						:current-page="queryParams.pageIndex" :page-sizes="[10, 30, 100, 500]" :page-size="queryParams.pageSize"
						layout="total, sizes, prev, pager, next" :total="pageTotal">
					</el-pagination>
				</div>
			</el-main>
		</el-container>
		
		<!-- 编辑弹出框 -->
		<el-dialog :visible.sync="editVisible" width="35%" center>
			<template slot="title">
				<span class="dialog-title">{{dialogText}}</span>
			</template>
			<el-form ref="formTable" :model="form" label-width="80px" :rules="rules" >
				<el-row :gutter="20">
					<el-col :span="12">
						<el-form-item label="参数名称" prop="dictName">
							<el-input v-model="form.dictName"></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="参数值" prop="dictValue">
							<el-input v-model="form.dictValue"></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="24">
						<el-form-item label="字典类型">
							<el-radio-group :disabled="disabledRadio" v-model="form.dictType" @change="radioChange">
								<el-radio label="目录">目录</el-radio>
								<el-radio label="参数">参数</el-radio>
							</el-radio-group>
						</el-form-item>
					</el-col>
					<el-col :span="24" v-show="showParents">
						<el-form-item label="所属上级" prop="parentId">
							<el-select v-model="form.parentId" placeholder="点击选择上级参数" ref="selectUpResId" @change="selectChange">
								<el-option v-for="item in tableData" :key="item.id" :label="item.dictName" :value="item.id" />
							</el-select>
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
		name: 'SysDict',
		data() {
			return {
				loading: false,
				tableData: [],
				queryParams: {
					dictName:'',
					pageIndex: 1,
					pageSize: 10
				},
				disabledRadio:false,
				rules: {
					dictName: [{
						required: true,
						message: '请输入参数名称',
						trigger: 'blur'
					}],
					dictValue: [{
						required: true,
						message: '请输入参数值',
						trigger: 'blur'
					}],
					parentId: [{
						required: true,
						message: '请选择所属上级',
						trigger: 'blur'
					}]
				},
				form: {
					id: '',
					parentId:'none',
					dictValue: '',
					dictName: '',
					level:0,
					dictType: '目录',
				},
				editVisible: false,
				showParents:false,
				pageTotal: 0,
				dialogText: '添加',
			}
		},
		created() {
			this.getData()
		},
		methods:{
			handleQuery(){
				
			},
			getData() {
				this.loading = true
				this.$api.dict.getTypeList(this.queryParams).then(res => {
					if (res.code === 200) {
						this.tableData = res.data.list
						this.pageTotal = res.data.total
					} else
						this.$message.error(res.message)
					this.loading = false
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
			submit(){
				
			},
			handleAdd(){
				
			},
			handleEdit(){
				
			},
			handleDel(){
				
			},
			radioChange(){
				
			},
			selectChange(){
				
			}
		}
	}
</script>

<style>
</style>
