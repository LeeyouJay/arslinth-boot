
import Vue from "vue";
class Dict {
	constructor(dict) {
		this.dict = dict;
	}

	async init(names) {
		const ps = [];
		names.forEach((name) => {
			Vue.set(this.dict, name, []);
			ps.push(
        this.dict[name] = Object.freeze(solveData(name))
				// get(name).then((data) => {
				// 	this.dict[name] = Object.freeze(data.content);
				// })
			);
		});
		await Promise.all(ps);
	}
}

function solveData(name){
  let data = [
    {parentValue: "menu_type", dictName: "目录", dictValue: "M"},
    {parentValue: "menu_type", dictName: "菜单", dictValue: "C"},
    {parentValue: "gender", dictName: "男", dictValue: "1"},
    {parentValue: "gender", dictName: "女", dictValue: "2"},
    {parentValue: "gender", dictName: "未知", dictValue: "0"}
  ]
  return data.filter(f=>f.parentValue === name)
}

const install = function(Vue) {
	Vue.mixin({
		data() {
      if (this.$options === undefined || this.$options.dicts === undefined || this.$options.dicts === null) {
        return {}
      }
			const dict = new Dict()
			dict.owner = this
			return {
			  dict
			}
		},
		created() {
			if (this.$options.dicts instanceof Array && this.$options.dicts.length>0) {
						new Dict(this.dict).init(this.$options.dicts);
				}
		},
	});
};

export default { install };
