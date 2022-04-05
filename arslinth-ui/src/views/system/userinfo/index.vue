<template>
	<div>
		<el-row :gutter="20">
			<el-col :span="8">
				<el-card shadow="always">
					<div slot="header" class="user-head">个人信息</div>
					<div class="user-info">
						<div style="margin-bottom: 20px;" v-if="reloadImg">
							<el-image style="width: 120px; height: 120px" :src="userInfo.avatar" fit="fill"></el-image>
						</div>
						<el-button type="primary" size="mini" round @click="editVisible = true">更换头像</el-button>
					</div>
					<ul class="list-group">
						<li class="list-group-item">
							<i class="el-icon-star-off"></i>
							登入账号：
							<div style="float: right;">{{userInfo.username}}</div>
						</li>
						<li class="list-group-item">
							<i class="el-icon-star-off"></i>
							手机号码：
							<div style="float: right;">{{userInfo.phone}}</div>
						</li>
						<li class="list-group-item">
							<i class="el-icon-star-off"></i>
							用户邮箱：
							<div style="float: right;">{{userInfo.email}}</div>
						</li>
						<li class="list-group-item">
							<i class="el-icon-star-off"></i>
							创建日期：
							<div style="float: right;">{{userInfo.createTime}}</div>
						</li>
					</ul>
				</el-card>
			</el-col>
			<el-col :span="16">
				<el-card shadow="always">
					<div slot="header" class="user-head">基本信息</div>
					<el-tabs v-model="activeName">
						<el-tab-pane label="基本资料" name="first">
							<basic-info :form.sync="userInfo"></basic-info>
						</el-tab-pane>
						<el-tab-pane label="修改密码" name="second">
							<change-password></change-password>
						</el-tab-pane>
					</el-tabs>
				</el-card>
			</el-col>
		</el-row>

		<!-- 编辑弹出框 -->
		<el-dialog :visible.sync="editVisible" width="50%" center>
			<template slot="title">
				<span class="dialog-title">头像设置</span>
			</template>
			<el-card shadow="always">
				<div class="cropper-content">
					<el-row :gutter="20">
						<el-col :span="12">
							<div style="height: 400px;">
								<VueCropper ref="cropper" :img="configObj.imgSrc" :can-move="true" :auto-crop="true" :center-box="false"
								 :info-true="true" :full="false" :fixedBox="true" :autoCropWidth="200" :autoCropHeight="200" output-type="png"
								 @realTime="realTime" />
							</div>
							<div style="margin-top: 20px;">
								<el-row :gutter="20">
									<el-col :span="8">
										<el-upload ref="uploadImage" class="upload-demo" action="#"  :http-request="requestUpload" accept=".jpg, .png, .bmp, .jpeg, .webp"
										 :multiple="false" :show-file-list="false" :auto-upload="false" :on-change="handleChange">
											<el-button size="small" plain type="primary">选择照片</el-button>
										</el-upload>
									</el-col>
									<el-col :span="16">
										<el-button size="small" icon="el-icon-plus" plain type="primary" @click="changeScale(1)"></el-button>
										<el-button size="small" icon="el-icon-minus" plain type="primary" @click="changeScale(-1)"></el-button>
										<el-button size="small" icon="el-icon-refresh-left" plain type="primary" @click="rotateLeft"></el-button>
										<el-button size="small" icon="el-icon-refresh-right" plain type="primary" @click="rotateRight"></el-button>
									</el-col>
								</el-row>
							</div>
						</el-col>
						<el-col :span="12">
							<div class="avatar-upload-preview">
								<div :style="{'width': previews.w + 'px', 'height': previews.h + 'px', 'zoom': (200 / previews.h)}">
									<el-image :src="previews.url" :style="previews.img" alt="预览"></el-image>
								</div>
							</div>
							<div class="avatar-upload-button">
								<el-button type="primary" :loading="loading" @click="saveEdit">提 交</el-button>
							</div>
						</el-col>
					</el-row>
				</div>
			</el-card>
		</el-dialog>
	</div>

</template>

<script>
	import {
		VueCropper
	} from 'vue-cropper'
	import BasicInfo from './BasicInfo'
	import ChangePassword from './ChangePassword'
	export default {
		name: 'UserCenter',
		components: {
			VueCropper,
			BasicInfo,
			ChangePassword
		},
		data() {
			return {
				reloadImg: false,
				loading: false,
				editVisible: false,
				activeName: 'first',
				userInfo: {},
				configObj: {
					imgSrc: 'https://cdn.jsdelivr.net/gh/baimingxuan/media-store/images/img02.jpg',
					canMove: false,
					autoCrop: true,
					centerBox: true,
				},
				previews: {}
			}
		},
		created() {
			this.getUserInfo();
		},
		methods: {
			getUserInfo() {
				this.$api.user.getInfo().then(res => {
					if (res.code === 200) {
						this.userInfo = res.data.user 
						if(this.userInfo.avatar) {
							this.userInfo.avatar = this.$requestUrl + this.userInfo.avatar
							this.configObj.imgSrc = this.userInfo.avatar
						} else{
							this.userInfo.avatar = require("@/assets/img/avatar.png")
						}
					} else
						this.$message.error(res.message)
					this.reloadImg = true
				})
			},
			changeScale(size) {
				this.$refs.cropper.changeScale(size)
			},
			rotateLeft() {
				this.$refs.cropper.rotateLeft()
			},
			rotateRight() {
				this.$refs.cropper.rotateRight()
			},
			//提交按钮
			saveEdit() {
				this.$refs.cropper.getCropBlob(data => {
					this.loading = true
					let formData = new FormData();
					formData.append("avatarFile", data);
					this.$api.user.changeAvatar(formData).then(res => {
						if (res.code === 200) {
							this.$message.success(res.message)
							this.reloadImg = false 
							this.editVisible = false
							this.loading = false
						} else
							this.$message.error(res.message)
						this.$nextTick(()=>this.reloadImg = true)
					})
				});
			},
			handleChange(image) {
				const rawImage = image.raw
				if (!rawImage) return false
				if (rawImage.type.indexOf("image/") == -1) {
					this.$message.warning('图片只支持.jpg, .png, .bmp, .jpeg, .webp格式!')
					return false
				}
				if (this.isLimit5M(rawImage)) {
					this.readImage(rawImage)
				}
			},
			isLimit5M(image) {
				const isLimit5M = image.size / 1024 / 1024 < 5
				if (isLimit5M) {
					return true
				} else {
					this.$message.warning('上传的图片大小不能超过5M!')
					return false
				}
			},
			isImage(image) {
				return /\.(jpg|png|bmp|jpeg|webp)$/.test(image.name)
			},
			readImage(image) {
				const reader = new FileReader()
				reader.onload = e => {
					let data
					if (typeof e.target.result === 'object') {
						data = window.URL.createObjectURL(new Blob([e.target.result]))
					} else {
						data = e.target.result
					}
					this.configObj.imgSrc = data
				}
				reader.readAsDataURL(image)
				reader.onerror = e => {
					this.$message.error('图片读取出错!')
				}
			},
			realTime(data) {
				this.previews = data
			},
			requestUpload(){
				
			}
		}

	}
</script>

<style scoped>
	.avatar-upload-button {
		text-align-last: center;
		top: 90%;
		left: 75%;
		position: absolute;
	}

	.cropper-content {
		/* height: 360px;
		width: 360px;
		padding-left: 10px; */
	}

	.user-info {
		text-align: center;
		/* padding-bottom: 20px; */
		margin-bottom: 20px;
	}

	.user-avator {
		width: 120px;
		height: 120px;
		border-radius: 50%;
	}

	.avatar-upload-preview {
		position: absolute;
		overflow: hidden;
		top: 40%;
		-webkit-transform: translate(50%, -50%);
		transform: translate(50%, -50%);
		width: 200px;
		height: 200px;
		border-radius: 50%;
	}

	.user-head {}

	.list-group-item {
		border-bottom: 1px solid #e7eaec;
		border-top: 1px solid #e7eaec;
		margin-bottom: -1px;
		padding: 11px 0;
		font-size: 15px;
	}

	.list-group {
		padding-left: 0;
		list-style: none;
	}
</style>

