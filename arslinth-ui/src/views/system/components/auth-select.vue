<template>
	<div class="auth-select">
		<div class="auth-checke" :style="{'text-align':positiion}">
			<el-checkbox :disabled="disabled" v-model="authBox.expand" @change="handleCheckedTreeExpand">展开/折叠</el-checkbox>
			<el-checkbox :disabled="disabled" v-model="authBox.nodeAll" @change="handleCheckedTreeNodeAll">全选/全不选</el-checkbox>
			<el-checkbox :disabled="disabled" :value="strictly" @change="handleCheckedTreeConnect">父子联动</el-checkbox>
		</div>
		<div class="auth-tree">
			<el-tree :data="tableTree" node-key="name" ref="tree" empty-text="加载中，请稍候..." show-checkbox default-expand-all
				:check-strictly="!strictly" :props="defaultProps" @check="checkChange">
				<span class="custom-tree-node" slot-scope="{ node, data }">
					<span style="line-height: 2;">{{node.label}}</span>
					<span>
						<el-button size="mini" type="text" :class="data.isFunc ?'red':'blue'">{{data.name}}</el-button>
					</span>
				</span>
			</el-tree>
		</div>
	</div>
</template>

<script>
	export default {
		name: 'auth-select',
		props: {
			disabled: {
				type:Boolean,
				default: false
			},
			positiion: {
				type: String,
				default: 'left'
			},
			strictly: {
				type: Boolean,
				default: false
			},
			permissions: {
				type: Array,
				default: () => []
			}
		},
		data() {
			return {
				authBox: {
					expand: true,
					nodeAll: false,
				},
				defaultProps: {
					children: 'children',
					label: 'label',
					disabled:()=>this.disabled
				},
				tableTree: []
			}
		},
		methods: {
			init() {
				this.$api.role.getAuthTree().then(res => {
					if (res.code === 200) {
						this.tableTree = res.data.tableTree
						this.setAuths(this.permissions)
					} else
						this.$message.error(res.message)
				})
			},
			//展开
			handleCheckedTreeExpand(value) {
				let treeList = this.tableTree;
				for (let i = 0; i < treeList.length; i++) {
					this.$refs.tree.store.nodesMap[treeList[i].name].expanded = value;
				}
			},
			//全选/全不选
			handleCheckedTreeNodeAll(value) {
				this.$refs.tree.setCheckedNodes(value ? this.tableTree : []);
			},
			//父子联动
			handleCheckedTreeConnect(value) {
				this.$emit('update:strictly', value)
			},
			getAuths() {
				return this.$refs.tree.getCheckedNodes().map(m => m.name)
			},
			setAuths(keys) {
				if (keys instanceof Array)
					this.$refs.tree.setCheckedKeys(keys)
			},
			checkChange(data, checked, indeterminate) {
				this.$emit('update:permissions', checked.checkedKeys)
			}
		}
	}
</script>

<style scoped>
	.auth-checke {
		margin-bottom: 20px;
	}

	.custom-tree-node {
		width: 100%;
		display: flex;
		justify-content: space-between;
		padding-right: 20px;
	}
</style>
