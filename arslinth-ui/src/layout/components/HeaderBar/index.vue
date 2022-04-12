<template>
	<div class="header-bar clear-fix">
		<FoldSideMenu />
		<BreadCrumbs />
		<div class="header-right clear-fix">
			<HeaderSearch/>
			<FullScreen />
			<!-- <LangSelect /> -->
			<!-- <GitHubLink /> -->
			<UserAvatar />
		</div>
	</div>
</template>

<script>
	import FoldSideMenu from '../../../components/FoldSideMenu'
	import BreadCrumbs from '../../../components/BreadCrumbs'
	import HeaderSearch from '../../../components/HeaderSearch'
	import FullScreen from '../../../components/FullScreen'
	// import LangSelect from '../../../components/LangSelect'
	// import GitHubLink from '../../../components/GitHubLink'
	import UserAvatar from '../../../components/UserAvatar'

	export default {
		components: {
			FoldSideMenu,
			BreadCrumbs,
			HeaderSearch,
			FullScreen,
			// LangSelect,
			// GitHubLink,
			UserAvatar
		}
	}
</script>

<style lang="less">
	.header-bar {
		display: flex;
		height: 32px;
		padding: 16px 20px;
		.header-right {
			flex: 1;
			display: flex;
			justify-content: right;
		}
	}
</style>
