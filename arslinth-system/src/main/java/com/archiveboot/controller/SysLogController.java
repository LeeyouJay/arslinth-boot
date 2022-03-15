package com.archiveboot.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.archiveboot.common.ApiResponse;
import com.archiveboot.entity.SysLog;
import com.archiveboot.entity.SysLogin;
import com.archiveboot.entity.VO.QueryBody;
import com.archiveboot.service.SysLogService;

import com.archiveboot.service.SysLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.archiveboot.common.ResponseCode.FAIL;
import static com.archiveboot.common.ResponseCode.SUCCESS;


/**
 * @className: SysLogController
 * @description: TODO 类描述
 * @author: Arslinth
 * @date: 2021/12/20
 **/
@RestController
@RequestMapping("/log")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysLogController {

    private final SysLogService sysLogService;

    private final SysLoginService sysLoginService;


    @PostMapping("/list")
    @PreAuthorize("hasAnyAuthority('sysLog_list')")
    public ApiResponse sysLogList(@RequestBody QueryBody query) {
        Page<SysLog> sysLogList = sysLogService.getLogPageList(query);
        return ApiResponse.code(SUCCESS).data("sysLogList", sysLogList.getRecords())
                .data("page", sysLogList.getPages())
                .data("total", sysLogList.getTotal())
                .message("查询成功！");
    }


    @PostMapping("/delLog")
    @PreAuthorize("hasAnyAuthority('del_log')")
    public ApiResponse delLog(@RequestBody List<String> ids) {
        int i = sysLogService.delLogByIds(ids);
        if (i > 0) {
            return ApiResponse.code(SUCCESS).message("删除成功!");
        } else {
            return ApiResponse.code(FAIL).message("删除出现异常!" + i);
        }
    }

    @GetMapping("/delLogAll")
    @PreAuthorize("hasAnyAuthority('del_log')")
    public ApiResponse delLogAll() {
        int i = sysLogService.delAll();
        if (i > 0) {
            return ApiResponse.code(SUCCESS).message("删除成功!");
        } else {
            return ApiResponse.code(FAIL).message("删除出现异常!" + i);
        }
    }


    @PostMapping("/LoginList")
    @PreAuthorize("hasAnyAuthority('sysLogin_list')")
    public ApiResponse sysLoginList(@RequestBody QueryBody query) {
        Page<SysLogin> sysLoginList = sysLoginService.getLoginPageList(query);
        return ApiResponse.code(SUCCESS).data("sysLoginList", sysLoginList.getRecords())
                .data("page", sysLoginList.getPages())
                .data("total", sysLoginList.getTotal())
                .message("查询成功！");
    }

    @PostMapping("/delLogin")
    @PreAuthorize("hasAnyAuthority('del_login')")
    public ApiResponse delLogin(@RequestBody List<String> ids) {
        int i = sysLoginService.delLoginByIds(ids);
        if (i > 0) {
            return ApiResponse.code(SUCCESS).message("删除成功!");
        } else {
            return ApiResponse.code(FAIL).message("删除出现异常!" + i);
        }
    }

    @GetMapping("/delLoginAll")
    @PreAuthorize("hasAnyAuthority('del_login')")
    public ApiResponse delLoginAll() {
        int i = sysLoginService.delAll();
        if (i > 0) {
            return ApiResponse.code(SUCCESS).message("删除成功!");
        } else {
            return ApiResponse.code(FAIL).message("删除出现异常!" + i);
        }
    }


}
