package com.arslinthboot.controller;

import com.arslinthboot.common.ApiResponse;
import com.arslinthboot.entity.SysDict;
import com.arslinthboot.service.SysDictService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.arslinthboot.common.ResponseCode.*;

/**
 * @author Arslinth
 * @ClassName SysDictController
 * @Description 系统字典
 * @Date 2022/3/28
 */
@Slf4j
@RestController
@RequestMapping("/dict")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysDictController {

    private final SysDictService sysDictService;

    /**
     * 添加字典
     *
     **/
    @PostMapping("/add")
    public ApiResponse addDict(@RequestBody SysDict sysDict) {
        sysDictService.addDict(sysDict);
        return ApiResponse.code(SUCCESS).message("添加成功！");
    }

    /**
     * 分页查询字典类型
     *
     **/
    @PostMapping("/typePage")
    public ApiResponse getTypePage(@RequestBody SysDict sysDict) {
        Page<SysDict> dictType = sysDictService.getTypePage(sysDict);
        return ApiResponse.code(SUCCESS)
                .data("list", dictType.getRecords())
                .data("page", dictType.getPages())
                .data("total", dictType.getTotal())
                .message("查询成功！");
    }

    /**
     * 分页查询字典值
     */
    @PostMapping("/valuePage")
    public ApiResponse getValuePage(@RequestBody SysDict sysDict) {
        Page<SysDict> dictType = sysDictService.getValuePage(sysDict);
        return ApiResponse.code(SUCCESS)
                .data("list", dictType.getRecords())
                .data("page", dictType.getPages())
                .data("total", dictType.getTotal())
                .message("查询成功！");
    }

    /**
     * 获取所有字典值
     */
    @GetMapping("/valueList")
    public ApiResponse getValueList() {
        return ApiResponse.code(SUCCESS).data("list", sysDictService.getValueList());
    }

    /**
     * 获取所有字典值
     */
    @GetMapping("/typeList")
    public ApiResponse getTypeList() {
        return ApiResponse.code(SUCCESS).data("list", sysDictService.getTypeList());
    }

    /**
     * 通过id查找字典
     */
    @GetMapping("/getDictById/{id}")
    public ApiResponse getDictById(@PathVariable("id") String id) {
        return ApiResponse.code(SUCCESS).data("dict", sysDictService.getDictById(id));
    }

    /**
     * 修改字典值
     */
    @PostMapping("/edit")
    public ApiResponse editDict(@RequestBody SysDict sysDict) {
        int i = sysDictService.editDict(sysDict);
        if (i == 1) {
            return ApiResponse.code(SUCCESS).message("修改成功！");
        } else {
            return ApiResponse.code(FAIL).message("修改出现异常：" + i);
        }
    }

    /**
     * 删除字典
     */
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

    /**
     * 批量删除字典值
     */
    @PostMapping("/delDictByIds")
    public ApiResponse delDictByIds(@RequestBody List<String> ids) {
        int i = sysDictService.delByIds(ids);
        if (i > 0) {
            return ApiResponse.code(SUCCESS).message("删除成功！");
        } else {
            return ApiResponse.code(FAIL).message("删除出现异常：" + i);
        }
    }
}
