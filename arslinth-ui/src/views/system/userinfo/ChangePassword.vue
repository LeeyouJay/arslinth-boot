<template>
	<div>
		<el-form :model="form" status-icon :rules="rules" ref="formTable" label-width="80px">
			<el-form-item label="旧密码" prop="oldPass" >
				<el-input type="password" placeholder="请输入旧密码" v-model="form.oldPass" autocomplete="off"></el-input>
			</el-form-item>
			<el-form-item label="新密码" prop="newPass" required>
				<el-input type="password" placeholder="请输入新密码" v-model="form.newPass" autocomplete="off"></el-input>
			</el-form-item>
			<el-form-item label="确认密码" prop="checkPass" required>
				<el-input type="password" placeholder="请确认密码" v-model="form.checkPass" autocomplete="off"></el-input>
			</el-form-item>
			<el-form-item>
				<el-button type="primary" @click="submit">提交</el-button>
				<el-button @click="resetForm">重置</el-button>
			</el-form-item>
		</el-form>
	</div>
</template>

<script>
	import { doSha256 } from '@/utils/validate'

	export default {
		name: 'ChangePassword',
		data() {
			var validatePass = (rule, value, callback) => {
				if (value === '') {
					callback(new Error('请输入密码'));
				} else {
					if (this.form.checkPass !== '') {
						this.$refs.formTable.validateField('checkPass');
					}
					callback();
				}
			};
			var validatePass2 = (rule, value, callback) => {
				if (value === '') {
					callback(new Error('请再次输入密码'));
				} else if (value !== this.form.newPass) {
					callback(new Error('两次输入密码不一致!'));
				} else {
					callback();
				}
			};
			return {
				form: {
					oldPass: '',
					newPass: '',
					checkPass: '',
				},
				rules: {
					oldPass: [{
						required: true,
						trigger: 'blur',
						message: '旧密码不能为空'
					}],
					newPass: [{
						validator: validatePass,
						trigger: 'blur',
						message: '新密码不能为空'
					}],
					checkPass: [{
						validator: validatePass2,
						trigger: 'blur'
					}],
				},
			}
		},
		methods: {
			submit() {
				this.$refs.formTable.validate((valid) => {
					if (valid) {
						let form = {
							oldPass: doSha256(this.form.oldPass),
							newPass: doSha256(this.form.newPass)
						}
						this.changePassword(form)
					} else {
						console.log('验证不通过');
						return false;
					}
				});
			},
			changePassword(form) {
				this.$api.user.changePassword(form).then(res => {
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
