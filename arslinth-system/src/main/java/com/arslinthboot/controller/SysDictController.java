package com.arslinthboot.controller;

import com.arslinthboot.common.ApiResponse;
import com.arslinthboot.entity.SysDict;
import com.arslinthboot.service.SysDictService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.arslinthboot.common.ResponseCode.*;

/**
 * @author Arslinth
 * @ClassName SysDictController
 * @Description TODO
 * @Date 2022/3/28
 */
@Slf4j
@RestController
@RequestMapping("/dict")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysDictController {

    private final SysDictService sysDictService;

    @PostMapping("/add")
    public ApiResponse addDict(@RequestBody SysDict sysDict) {
        sysDictService.addDict(sysDict);
        return ApiResponse.code(SUCCESS).message("添加成功！");
    }

    @PostMapping("/typePage")
    public ApiResponse getTypePage(@RequestBody SysDict sysDict) {
        Page<SysDict> dictType = sysDictService.getTypePage(sysDict);
        return ApiResponse.code(SUCCESS)
                .data("list", dictType.getRecords())
                .data("page", dictType.getPages())
                .data("total", dictType.getTotal())
                .message("查询成功！");
    }

    @PostMapping("/valuePage")
    public ApiResponse getValuePage(@RequestBody SysDict sysDict) {
        Page<SysDict> dictType = sysDictService.getValuePage(sysDict);
        return ApiResponse.code(SUCCESS)
                .data("list", dictType.getRecords())
                .data("page", dictType.getPages())
                .data("total", dictType.getTotal())
                .message("查询成功！");
    }

    @GetMapping("/valueList")
    public ApiResponse getValueList(){
        return ApiResponse.code(SUCCESS).data("list",sysDictService.getValueList());
    }

    @PostMapping("/edit")
    public ApiResponse editDict(@RequestBody SysDict sysDict) {
        int i = sysDictService.editDict(sysDict);
        if (i == 1) {
            return ApiResponse.code(SUCCESS).message("修改成功！");
        } else {
            return ApiResponse.code(FAIL).message("修改出现异常：" + i);
        }
    }

    @GetMapping("/del/{id}")
    public ApiResponse delDict(@PathVariable String id) {
        int i = sysDictService.delById(id);
        if (i == 1) {
            return ApiResponse.code(SUCCESS).message("删除成功！");
        } else if (i == -1) {
            return ApiResponse.code(FAIL).message("请先删除剩余子项！");
        } else {
            return ApiResponse.code(FAIL).message("删除出现异常：" + i);
        }
    }
}
