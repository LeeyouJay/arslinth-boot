<template>
	<div>
		<el-form :model="form" status-icon :rules="rules" ref="formTable" label-width="80px">
			<el-form-item label="用户昵称" prop="nickName">
				<el-input type="text" v-model="form.nickName" autocomplete="off"></el-input>
			</el-form-item>
			<el-form-item label="手机号码" prop="phone">
				<el-input type="text" v-model="form.phone" autocomplete="off"></el-input>
			</el-form-item>
			<el-form-item label="邮箱" prop="email">
				<el-input type="text" v-model="form.email" autocomplete="off"></el-input>
			</el-form-item>
			<el-form-item label="性别" prop="sex">
				<el-select v-model="form.sex" placeholder="请选择性别" clearable>
					<el-option value="m" label="男"></el-option>
					<el-option value="w" label="女"></el-option>
					<el-option value="s" label="保密"></el-option>
				</el-select>
			</el-form-item>
			<el-form-item>
				<el-button type="primary" @click="submit">提交</el-button>
				<el-button @click="resetForm">重置</el-button>
			</el-form-item>
		</el-form>
	</div>
</template>

<script>
	export default {
		name: 'BasicInfo',
		props: {
			form: {
				type: Object,
				require: true,
				default: ()=>{
					return {
						nickName:'',
						phone:'',
						sex:'s'
					}
				}
			}
		},
		data() {
			//手机号码验证规则
			var checkPhone = (rule, value, callback) => {
				const reg = /^1[3|4|5|7|8][0-9]\d{8}$/
				if (value !== "") {
					if (!reg.test(value)) {
						callback(new Error('请输入正确的手机号'));
					} else {
						callback();
					}
				} else {
					callback();
				}
			};
			return {
				rules: {
					nickName: [{
						required: true,
						trigger: 'blur',
						message: '昵称不能为空'
					}],
					phone: [{
						validator: checkPhone,
						trigger: 'blur',
					}],
				},
			}
		},
		methods: {
			submit() {
				this.$refs.formTable.validate((valid) => {
					if (valid) {
						this.changeUserInfo()
					} else {
						console.log('验证不通过');
						return false;
					}
				});
			},
			changeUserInfo() {
				this.$api.user.changeUserInfo(this.form).then(res => {
					if (res.code === 200) {
						this.$message.success(res.message)
					} else
						this.$message.error(res.message)
				})
			},
			
			resetForm() {
				this.$refs.formTable.resetFields();
			},
		}
	}
</script>

<style>
</style>
