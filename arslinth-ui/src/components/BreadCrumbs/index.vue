<template>
	<div class="bread-crumbs-wrapper">
		<el-breadcrumb separator="/">
			<transition-group name="breadcrumb" mode="out-in">
				<el-breadcrumb-item key="home">
					<el-button type="text" class="home-link" :disabled="!hasClick" @click="goHome">
						<i class="arslinth-vue-home" />
						首页
					</el-button>
				</el-breadcrumb-item>
				<el-breadcrumb-item v-for="(item, index) in dataList" :key="item.path">{{ item.label }}
				</el-breadcrumb-item>
			</transition-group>
		</el-breadcrumb>
	</div>
</template>

<script>
	export default {
		name: 'BreadCrumbs',
		data() {
			return {
				levelList: [],
				hasClick: false,
				dataList: []
			}
		},
		computed:{
			rdata() {
				return this.$store.getters.rdata
			}
		},
		watch: {
			$route(route) {
				this.getBreadCrumb()
				this.isHome(route.path)
			}
		},
		created() {
			this.getBreadCrumb()
			this.isHome(this.$route.path)
		},
		methods: {
			isHome(path) {
				if (path === '/home') {
					this.hasClick = false
				} else {
					this.hasClick = true
				}
			},
			goHome() {
				this.$router.push('/home')
			},
			getBreadCrumb() {
				const path = this.$route.path.split("/").slice(1)
				this.dataList = this.rdata.filter(item=> item.label!== '首页' && (path.includes(item.path) || path.includes(item.path.substr(1))) ).map(m=>{return {label:m.label,path: m.path } })
			},
		}
	}
</script>

<style lang="less" scoped>
	.bread-crumbs-wrapper {
		width: 35%;
		height: 32px;
		padding: 0 20px;

		.el-breadcrumb {
			line-height: 32px;
		}
		.home-link {
			&,
			.icon {
				padding: 0;
				font-weight: bold;
				font-size: 14px;
				color: #333 !important;
			}
		}
	}
</style>
