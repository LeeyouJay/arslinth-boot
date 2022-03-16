<template>
	<div>
		<div class="header-bar">
			<el-button type="primary" @click="handleCreate">添加菜单</el-button>
		</div>

		<!-- 编辑弹出框 -->
		<el-dialog :visible.sync="formVisible" width="50%" center>
			<template slot="title">
				<span class="dialog-title">修改</span>
			</template>
			<el-form ref="formTable" :model="form" label-width="80px" :rules="rules">
				<el-row :gutter="20">
					<el-col :span="24">
						<el-form-item label="上级权限">
							<el-select v-model="slelectLable" placeholder="点击选择页面" ref="selectUpResId">

							</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="权限名称" prop="authorityName">
							<el-input v-model="form.authorityName"></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="权限标识" prop="authority">
							<el-input v-model="form.authority"></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="类型">
							<el-radio-group v-model="form.menuType">
								<el-radio label="M">目录</el-radio>
								<el-radio label="C">菜单</el-radio>
								<el-radio label="F">按钮</el-radio>
							</el-radio-group>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="显示排序" prop="sorted">
							<el-input-number v-model="form.sorted" :min="0"></el-input-number>
						</el-form-item>
					</el-col>
					<el-col :span="24">
						<el-form-item label="图标">
							<e-icon-picker ref="eIconPicker" v-model="icon" :options="options" />
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
		name: 'SystemMenu',
		components: {
			EIconPicker
		},
		data() {
			return {
				formVisible: false,
				form: {},
				slelectLable: '',
				icon: '',
				options: {
					FontAwesome: true,
					ElementUI: false,
				},
				rules: {
					authorityName: [{
						required: true,
						message: '请输入权限名称',
						trigger: 'blur'
					}],
					authority: [{
						required: true,
						message: '请输入权限标示',
						trigger: 'blur'
					}],
					sorted: [{
						required: true,
						message: '请选择排序大小',
						trigger: 'blur'
					}]
				}
			}
		},
		watch: {
			formVisible(val) {
				if (!val && this.$refs.eIconPicker) {
					this.$refs.eIconPicker.destroyIconList()
				}
			},
		},
		methods: {
			handleCreate() {
				// this.$refs['eIconPicker'].show()
				this.formVisible = true
			},
			submit() {

			}
		}

	}
</script>

<style lang="css" scoped>
	@import '~e-icon-picker/lib/index.css';
	@import '~font-awesome/css/font-awesome.min.css';
	@import '~element-ui/lib/theme-chalk/icon.css';
</style>
