package com.arslinthboot.controller;

import com.arslinthboot.annotation.Log;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.arslinthboot.common.ApiResponse;
import com.arslinthboot.entity.OperLog;
import com.arslinthboot.entity.LoginLog;
import com.arslinthboot.service.SysLogService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.arslinthboot.common.ResponseCode.FAIL;
import static com.arslinthboot.common.ResponseCode.SUCCESS;


/**
 * @className: SysLogController
 * @description: 系统日志
 * @author: Arslinth
 * @date: 2021/12/20
 **/
@RestController
@RequestMapping("/log")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysLogController {

    private final SysLogService sysLogService;


    /**
     * 分页查询操作日志
     *
     **/
    @PostMapping("/operLogPage")
    public ApiResponse operLogPage(@RequestBody OperLog operLog) {
        Page<OperLog> listPage = sysLogService.operLogPage(operLog);
        return ApiResponse.code(SUCCESS)
                .data("list", listPage.getRecords())
                .data("page", listPage.getPages())
                .data("total", listPage.getTotal())
                .message("查询成功！");
    }

    /**
     * 分页查询登入日志
     *
     **/
    @PostMapping("/loginLogPage")
    public ApiResponse loginLogPage(@RequestBody LoginLog loginLog) {
        Page<LoginLog> listPage = sysLogService.loginLogPage(loginLog);
        return ApiResponse.code(SUCCESS)
                .data("list", listPage.getRecords())
                .data("page", listPage.getPages())
                .data("total", listPage.getTotal())
                .message("查询成功！");
    }

    /**
     * 删除操作日志
     *
     **/
    @Log("删除操作日志")
    @PostMapping("/delOperLog")
    public ApiResponse delOperLog(@RequestBody List<String> ids) {
        int i = sysLogService.delOperLogByIds(ids);
        if (i>0) {
            return ApiResponse.code(SUCCESS).message("删除成功!");
        } else {
            return ApiResponse.code(FAIL).message("删除出现异常!" + i);
        }
    }

    /**
     * 删除全部操作日志
     *
     **/
    @Log("删除全部操作日志")
    @GetMapping("/delAllOperLog")
    public ApiResponse delAllOperLog() {
        int i = sysLogService.delAllOperLog();
        if (i>0) {
            return ApiResponse.code(SUCCESS).message("删除成功!");
        } else {
            return ApiResponse.code(FAIL).message("删除出现异常!" + i);
        }
    }

    /**
     * 删除登入日志
     *
     **/
    @Log("删除登入日志")
    @PostMapping("/delLoginLog")
    public ApiResponse delLoginLog(@RequestBody List<String> ids) {
        int i = sysLogService.delLoginLogByIds(ids);
        if (i>0) {
            return ApiResponse.code(SUCCESS).message("删除成功!");
        } else {
            return ApiResponse.code(FAIL).message("删除出现异常!" + i);
        }
    }

    /**
     * 删除全部登入日志
     *
     **/
    @Log("删除全部登入日志")
    @GetMapping("/delAllLoginLog")
    public ApiResponse delAllLoginLog() {
        int i = sysLogService.delAllLoginLog();
        if (i>0) {
            return ApiResponse.code(SUCCESS).message("删除成功!");
        } else {
            return ApiResponse.code(FAIL).message("删除出现异常!" + i);
        }
    }

}
