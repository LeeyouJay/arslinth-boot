package com.arslinthboot.controller;

import com.arslinthboot.common.ApiResponse;
import com.arslinthboot.entity.SysRole;
import com.arslinthboot.service.SysRoleService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.arslinthboot.common.ResponseCode.FAIL;
import static com.arslinthboot.common.ResponseCode.SUCCESS;

/**
 * @className: SysRoleController
 * @description: 系统角色
 * @author: Arslinth
 * @date: 2022/4/2
 **/
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysRoleController {

    private final SysRoleService sysRoleService;

    /**
     * 添加角色
     */
    @PostMapping("/add")
    public ApiResponse addRole(@RequestBody SysRole sysRole) {
        sysRoleService.addRole(sysRole);
        return ApiResponse.code(SUCCESS).message("添加成功！");
    }

    /**
     * 获取角色列表
     */
    @PostMapping("/rolePage")
    public ApiResponse rolePage(@RequestBody SysRole sysRole) {
        Page<SysRole> rolePage = sysRoleService.getRolePage(sysRole);
        return ApiResponse.code(SUCCESS)
                .data("list", rolePage.getRecords())
                .data("page", rolePage.getPages())
                .data("total", rolePage.getTotal())
                .message("查询成功！");
    }
    /**
     * 获取所有角色
     */
    @PostMapping("/roleList")
    public ApiResponse getRoleList(@RequestBody SysRole sysRole) {
        return ApiResponse.code(SUCCESS).data("list", sysRoleService.getRoleList(sysRole));
    }

    /**
     * 根据id查询角色
     */
    @GetMapping("/getRoleById/{id}")
    public ApiResponse getRoleById(@PathVariable("id") String id) {
        return ApiResponse.code(SUCCESS).data("role", sysRoleService.getRoleById(id));
    }

    /**
     * 修改角色
     *
     **/
    @PostMapping("/edit")
    public ApiResponse editRole(@RequestBody SysRole sysRole) {
        int i = sysRoleService.editRole(sysRole);
        if (i == 1) {
            return ApiResponse.code(SUCCESS).message("修改成功！");
        } else {
            return ApiResponse.code(FAIL).message("修改出现异常：" + i);
        }
    }

    /**
     * 删除角色
     */
    @GetMapping("/del/{id}")
    public ApiResponse delRole(@PathVariable String id) {
        int i = sysRoleService.delById(id);
        if (i == 1) {
            return ApiResponse.code(SUCCESS).message("删除成功！");
        } else if (i == -1) {
            return ApiResponse.code(FAIL).message("超级管理员不允许删除！");
        } else {
            return ApiResponse.code(FAIL).message("删除出现异常：" + i);
        }
    }

    /**
     * 批量删除角色
     */
    @PostMapping("/delRoleByIds")
    public ApiResponse delRoleByIds(@RequestBody List<String> ids) {
        int i = sysRoleService.delByIds(ids);
        if (i > 0) {
            return ApiResponse.code(SUCCESS).message("删除成功！");
        } else if (i == -1) {
            return ApiResponse.code(FAIL).message("超级管理员不允许删除！");
        } else {
            return ApiResponse.code(FAIL).message("删除出现异常：" + i);
        }
    }


}
