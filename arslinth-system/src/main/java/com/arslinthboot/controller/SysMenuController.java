package com.arslinthboot.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.arslinthboot.annotation.SysLog;
import com.arslinthboot.annotation.RepeatSubmit;
import com.arslinthboot.common.ApiResponse;
import com.arslinthboot.config.tokenConfig.LoginUser;
import com.arslinthboot.entity.SysMenu;
import com.arslinthboot.entity.SysUser;
import com.arslinthboot.service.SysMenuService;
import com.arslinthboot.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

    /**
     * 添加菜单
     */
    @SysLog("#{'添加菜单:'+ #menu.label}")
    @RepeatSubmit
    @PostMapping("/add")
    @PreAuthorize("@auth.hasAnyAuthority('AddMenu')")
    public ApiResponse addMenu(@RequestBody SysMenu menu) {
        sysMenuService.addMenu(menu);
        return ApiResponse.code(SUCCESS).message("添加成功！");
    }

    /**
     * 删除菜单
     */
    @SysLog("删除菜单")
    @RepeatSubmit
    @GetMapping("/del/{id}")
    @PreAuthorize("@auth.hasAnyAuthority('DelMenu')")
    public ApiResponse delMenu(@PathVariable("id") String id) {
        sysMenuService.delMenu(id);
        return ApiResponse.code(SUCCESS).message("删除成功！");
    }

    /**
     * 修改菜单
     */
    @SysLog("#{'修改菜单:'+ #menu.label}")
    @RepeatSubmit
    @PostMapping("/edit")
    @PreAuthorize("@auth.hasAnyAuthority('EditMenu')")
    public ApiResponse editMenu(@RequestBody SysMenu menu) {
        sysMenuService.editMenu(menu);
        return ApiResponse.code(SUCCESS).message("修改成功！");
    }

    /**
     * 根据名称查询菜单列表
     */
    @GetMapping({"/list/{menuName}", "/list"})
    @PreAuthorize("@auth.hasAnyAuthority('SysMenu')")
    public ApiResponse list(@PathVariable(required = false) String menuName) {
        List<SysMenu> list = sysMenuService.getMenuList(null,menuName, false);
        String rootId = list.stream().min(Comparator.comparing(SysMenu::getLevel))
                .map(SysMenu::getParentId).orElse("0");
        return ApiResponse.code(SUCCESS).data("list", generateTree(list, rootId));
    }

    /**
     * 根据id查询菜单
     */
    @GetMapping("/getMenuById/{id}")
    @PreAuthorize("@auth.hasAnyAuthority('SysMenu')")
    public ApiResponse getMenuById(@PathVariable("id") String id) {
        return ApiResponse.code(SUCCESS).data("menu", sysMenuService.getMenuById(id));
    }


    /**
     * 生成菜单路由
     */
    @GetMapping("/generateRoutes")
    public ApiResponse generateRoutes() {
        //根据权限生成路由
        LoginUser<SysUser> loginUser = SecurityUtils.getLoginUser();
        Set<String> auths = Optional.ofNullable(loginUser).map(LoginUser::getPermissions).orElse(new HashSet<>());
        if (CollUtil.isEmpty(auths)){
            return ApiResponse.code(SUCCESS).data("routes", auths);
        }
        List<SysMenu> list = sysMenuService.getMenuList(auths,null, true);
        return ApiResponse.code(SUCCESS).data("routes", generateTree(list, "0"));
    }

    private List<Tree<String>> generateTree(List<SysMenu> list, String rootId) {
        TreeNodeConfig config = new TreeNodeConfig();
        //排序字段
        config.setWeightKey("indexNum");
        //最大递归深度
        config.setDeep(4);

        return TreeUtil.build(list, rootId, config, (treeNode, tree) -> {
            tree.setId(treeNode.getId());
            tree.setParentId(treeNode.getParentId());
            tree.setWeight(treeNode.getIndexNum());
            if ("0".equals(treeNode.getParentId()) && "Layout".equals(treeNode.getComponent())) {
                tree.putExtra("path", treeNode.getParentId().equals("0") ? "/" + treeNode.getPath() : treeNode.getPath());
            } else {
                tree.putExtra("path", treeNode.getPath());
            }
            tree.setName(treeNode.getName());
            tree.putExtra("meta", treeNode.getMeta());
            tree.putExtra("label", treeNode.getLabel());
            tree.putExtra("level", treeNode.getLevel());
            tree.putExtra("menuType", treeNode.getMenuType());
            tree.putExtra("component", treeNode.getComponent());
            tree.putExtra("createTime", treeNode.getCreateTime());
        });

    }
}
