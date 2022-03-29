<template>
	<div class="main-view">
		<transition name="fade-transform" mode="out-in">
			<keep-alive :include="cachedViews">
				<router-view :key="key" />
			</keep-alive>
		</transition>
	</div>
</template>
<script>
	export default {
		name: 'MainView',
		computed: {
			cachedViews() {
				return this.$store.state.tagsView.cachedViews
			},
			key() {
				return this.$route.path
			}
		}
	}
</script>

<style lang="less">
	.main-view {
		min-height: calc(100vh - 155px);
		width: calc(100% - 50px);
		padding: 25px;
		position: relative;
		overflow: hidden;
	}
	.dialog-body {
		overflow-y: auto;
		height: calc(100vh - 170px);
		width: 100%;
		padding-right: 14px;
	}
	/* fade-transform */
	.fade-transform-leave-active,
	.fade-transform-enter-active {
		transition: all 0.5s;
		text-align: left;
	}

	.fade-transform-enter {
		opacity: 0;
		transform: translateX(-30px);
	}

	.fade-transform-leave-to {
		opacity: 0;
		transform: translateX(30px);
	}
</style>
