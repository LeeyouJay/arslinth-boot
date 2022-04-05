package com.arslinthboot.controller;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.arslinthboot.common.ApiResponse;
import com.arslinthboot.config.tokenConfig.LoginUser;
import com.arslinthboot.entity.SysMenu;
import com.arslinthboot.entity.SysUser;
import com.arslinthboot.service.SysMenuService;
import com.arslinthboot.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.arslinthboot.common.ResponseCode.SUCCESS;

/**
 * @className: SysAuthController
 * @description: 权限控制
 * @author: Arslinth
 * @date: 2022/4/2
 **/

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysAuthController {

    private final SysMenuService sysMenuService;

    /**
     * 获取权限树
     */
    @GetMapping("/authTree")
    public ApiResponse getAuthTree() {
        LoginUser<SysUser> loginUser = SecurityUtils.getLoginUser();
        Set<String> auths = Optional.ofNullable(loginUser).map(LoginUser::getPermissions).orElse(new HashSet<>());
        List<SysMenu> list = sysMenuService.getMenuList(auths, null, false);
        TreeNodeConfig config = new TreeNodeConfig();
        config.setWeightKey("indexNum");
        config.setDeep(4);
        List<Tree<String>> treeList = TreeUtil.build(list, "0", config, (treeNode, tree) -> {
            tree.setId(treeNode.getId());
            tree.setParentId(treeNode.getParentId());
            tree.setWeight(treeNode.getIndexNum());
            tree.setName(treeNode.getName());
            tree.putExtra("isFunc", "F".equals(treeNode.getMenuType()));
            tree.putExtra("label", treeNode.getLabel());
        });
        return ApiResponse.code(SUCCESS).data("tableTree",treeList);
    }

}
