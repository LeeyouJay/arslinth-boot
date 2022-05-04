package com.arslinthboot.controller;

import com.arslinthboot.annotation.RepeatSubmit;
import com.arslinthboot.annotation.SysLog;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.arslinthboot.common.ApiResponse;
import com.arslinthboot.entity.OperLog;
import com.arslinthboot.entity.LoginLog;
import com.arslinthboot.service.SysLogService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("@auth.hasAnyAuthority('OperLog')")
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
    @PreAuthorize("@auth.hasAnyAuthority('LoginLog')")
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
    @SysLog("删除操作日志")
    @RepeatSubmit
    @PostMapping("/delOperLog")
    @PreAuthorize("@auth.hasAnyAuthority('DelOperLog')")
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
    @SysLog("删除全部操作日志")
    @RepeatSubmit
    @GetMapping("/delAllOperLog")
    @PreAuthorize("@auth.hasAnyAuthority('DelOperLog')")
    public ApiResponse delAllOperLog() {
        sysLogService.delAllOperLog();
        return ApiResponse.code(SUCCESS).message("删除成功!");

    }

    /**
     * 删除登入日志
     *
     **/
    @SysLog("删除登入日志")
    @RepeatSubmit
    @PostMapping("/delLoginLog")
    @PreAuthorize("@auth.hasAnyAuthority('DelLoginLog')")
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
    @SysLog("删除全部登入日志")
    @RepeatSubmit
    @PreAuthorize("@auth.hasAnyAuthority('DelLoginLog')")
    @GetMapping("/delAllLoginLog")
    public ApiResponse delAllLoginLog() {
        sysLogService.delAllLoginLog();
        return ApiResponse.code(SUCCESS).message("删除成功!");


    }

}
