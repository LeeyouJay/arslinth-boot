import dict from "@/api/system/dict"
import Vue from "vue";
class Dict {
	constructor(dict) {
		this.dict = dict;
	}

	async init(names) {
    const ps = [];
    let res = await dict.getValueList(names)
    const map = res.data.map
    names.forEach((name) => {
      Vue.set(this.dict, name, []);
      ps.push(
        this.dict[name] = Object.freeze(map[name] || [])
      );
    });
    await Promise.all(ps);
  }
}

const install = function(Vue) {
	Vue.mixin({
		data() {
      if (this.$options === undefined || this.$options.dicts === undefined || this.$options.dicts === null) {
        return {}
      }
			const dict = new Dict()
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
