package com.arslinthboot.controller;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.arslinthboot.common.ApiResponse;
import com.arslinthboot.entity.SysMenu;
import com.arslinthboot.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

import static com.arslinthboot.common.ResponseCode.SUCCESS;

/**
 * @author Arslinth
 * @ClassName SysMenuController
 * @Description 菜单控制器
 * @Date 2022/3/13
 */
@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysMenuController {

    private final SysMenuService sysMenuService;

    @PostMapping("/add")
    public ApiResponse addMenu(@RequestBody SysMenu menu) {
        sysMenuService.addMenu(menu);
        return ApiResponse.code(SUCCESS).message("添加成功！");
    }

    @GetMapping("/generateRoutes")
    public ApiResponse generateRoutes() {
        List<SysMenu> list = sysMenuService.getMenuList();
        TreeNodeConfig config = new TreeNodeConfig();
        //排序字段
        config.setWeightKey("indexNum");
        //最大递归深度
        config.setDeep(3);
        List<Tree<String>> treeList = TreeUtil.build(list, "0", config, (treeNode, tree) -> {
            tree.setId(treeNode.getId());
            tree.setParentId(treeNode.getParentId());
            tree.setWeight(treeNode.getIndexNum());
            tree.putExtra("path", treeNode.getParentId().equals("0") ? "/" + treeNode.getPath() : treeNode.getPath());
            tree.setName(treeNode.getName());
            tree.putExtra("meta", treeNode.getMeta());
            tree.putExtra("component", treeNode.getComponent());
        });

        return ApiResponse.code(SUCCESS).data("routes", treeList);
    }
}
