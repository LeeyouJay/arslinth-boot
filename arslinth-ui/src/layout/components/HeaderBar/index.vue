<template>
  <div class="header-bar clear-fix">
    <FoldSideMenu />
    <BreadCrumbs />
    <div class="header-right clear-fix">
      <FullScreen />
      <LangSelect />
      <GitHubLink />
      <UserAvatar />
    </div>
  </div>
</template>

<script>
import FoldSideMenu from '../../../components/FoldSideMenu'
import BreadCrumbs from '../../../components/BreadCrumbs'
import FullScreen from '../../../components/FullScreen'
import LangSelect from '../../../components/LangSelect'
import GitHubLink from '../../../components/GitHubLink'
import UserAvatar from '../../../components/UserAvatar'

export default {
  components: {
    FoldSideMenu,
    BreadCrumbs,
    FullScreen,
    LangSelect,
    GitHubLink,
    UserAvatar
  }
}
</script>

<style lang="less">
	.view-page {
		display: flex;
		flex-direction: column;
	}
  .header-bar {
    height: 32px;
    padding: 16px 20px;
    .header-right {
      float: right;
      width: 200px;
    }
  }
	.pagination {
	    margin: 20px 0;
	    text-align: right;
	}
	.green {
		font-weight: bold;
		color: #67C23A !important;
	}
	.red {
		font-weight: bold;
		color: #ff0000 !important;
	}
	
	.blue {
		font-weight: bold;
		color: #409EFF !important;
	}
	
	.yellow {
		font-weight: bold;
		color: #ffaa00 !important;
	}
	
</style>
