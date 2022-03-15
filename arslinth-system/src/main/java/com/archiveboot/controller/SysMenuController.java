package com.archiveboot.controller;

import com.archiveboot.common.ApiResponse;
import com.archiveboot.entity.SysMenu;
import com.archiveboot.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.archiveboot.common.ResponseCode.SUCCESS;

/**
 * @author Arslinth
 * @ClassName SysMenuController
 * @Description TODO
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

    @GetMapping("/list")
    public ApiResponse getSysMenuList() {
        return ApiResponse.code(SUCCESS).data("menuList", sysMenuService.getMenuList());
    }
}
