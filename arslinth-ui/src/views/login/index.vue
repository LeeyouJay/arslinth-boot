<template>
	<div class="login-wrapper" :style="'background-image:url('+ Background +')'">
		<div class="form-box">
			<div class="form-title">
				<img src="@/assets/img/logo.png" alt="icon">
			</div>
			<el-tabs v-model="activeName" @tab-click="handleTabClick">
				<el-tab-pane label="账号密码登入" name="first">
					<el-form ref="loginForm" :model="loginForm" :rules="loginRules" label-width="0px"
						class="login-form">
						<el-form-item prop="username">
							<el-input v-model="loginForm.username" type="text" auto-complete="off" placeholder="请输入账号"
								prefix-icon="el-icon-user" />
						</el-form-item>
						<el-form-item prop="password">
							<el-input v-model="loginForm.password" type="password" auto-complete="off"
								placeholder="请输入密码" prefix-icon="el-icon-lock" @keyup.enter.native="verifyForm" />
						</el-form-item>
						<el-form-item>
							<div class="login-btn">
								<el-button :loading="loading"  type="primary"
									@click.native.prevent="verifyForm">
									<span v-if="!loading">登 录</span>
									<span v-else>登 录 中...</span>
								</el-button>
							</div>
						</el-form-item>
					</el-form>
				</el-tab-pane>
			</el-tabs>
		</div>
		
		<!-- 验证码弹框 -->
		<el-dialog width="340px" append-to-body :visible.sync="dialogVisible" :show-close="false"
			:close-on-click-modal="true">
			<template slot="title">
				<span class="dialog-title">请完成图片验证</span>
			</template>
			<Captcha ref="dialogopen" :captchaUUid.sync="loginForm.captchaUUid" @verify="verify"  />
		</el-dialog>
	</div>
</template>

<script>
	import Background from '@/assets/img/login-background.jpg'
	import Captcha from './captcha.vue'
	import { doSha256 } from '@/utils/validate'
	
	export default {
		name: 'Login',
		components: {
			Captcha
		},
		data() {
			return {
				Background,
				activeName: 'first',
				loginForm: {
					username: 'arslinth',
					password: '123456',
					captchaUUid:''
				},
				loginRules: {
					username: [{
						required: true,
						trigger: 'blur',
						message: '用户名不能为空'
					}],
					password: [{
						required: true,
						trigger: 'blur',
						message: '密码不能为空'
					}]
				},
				loading: false,
				redirect: undefined,
				dialogVisible: false,
				refreshCode: false
			}
		},
		watch: {
			$route: {
				handler: function(route) {
					this.redirect = route.query && route.query.redirect
				},
				immediate: true
			}
		},
		methods: {
			verifyForm() {
				this.$refs.loginForm.validate(valid => {
					if (valid){
						this.dialogVisible = true
						this.$nextTick(()=>this.$refs.dialogopen.init())
					} 
				})
			},
			verify(left, stddev) {
				let user = {
					username: this.loginForm.username,
					password: this.loginForm.password,
					captchaUUid: this.loginForm.captchaUUid,
					moveX: left
				}
				user.password = doSha256(user.password);
				this.handleLogin(user)
			},
			handleLogin(user) {
				this.$api.user.login(user).then(res=>{
					if(res.code == 200){
						this.$refs.dialogopen.handleSuccess()
						setTimeout(()=>{
							this.dialogVisible = false
							this.$router.push({ path: this.redirect || '/home'}).catch(()=>{})
						},700)
						
					}else if(res.code == 502) {
						this.$refs.dialogopen.handleFail()
						setTimeout(()=>{
							this.$refs.dialogopen.refresh()
						},1000)
					}else {
						this.$message.error(res.message)
						this.dialogVisible = false
						setTimeout(()=>{
							this.$refs.dialogopen.refresh()
						},1000)
					}
				})
			},
			handleTabClick(tab, event) {
				
			},
		}
	}
</script>

<style lang="less">
	.login-wrapper {
		display: flex;
		justify-content: center;
		align-items: center;
		width: 100%;
		height: 100vh;
		background-size: cover;
		.form-box {
			width: 320px;
			padding: 15px 30px 20px;
			border-radius: 4px;
			box-shadow: 0 15px 30px 0 rgba(0, 0, 1, .1);
			background: rgba(255, 255, 255, 30%);
			overflow: hidden;
			.form-title {
				margin: 0 auto 0px;
				text-align: center;
				color: #707070;
				font-size: 18px;
				letter-spacing: 2px;
			}
		}
	}
	.login-btn {
		text-align: center;
	}
	.login-btn button {
		width: 35%;
		height: 36px;
		margin-bottom: 10px;
	}
</style>
