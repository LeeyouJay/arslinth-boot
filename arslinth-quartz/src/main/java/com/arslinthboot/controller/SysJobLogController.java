package com.arslinthboot.controller;

import com.arslinthboot.annotation.RepeatSubmit;
import com.arslinthboot.annotation.SysLog;
import com.arslinthboot.common.ApiResponse;
import com.arslinthboot.entity.SysJobLog;
import com.arslinthboot.service.SysJobLogService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.arslinthboot.common.ResponseCode.FAIL;
import static com.arslinthboot.common.ResponseCode.SUCCESS;

/**
 * @author Arslinth
 * @ClassName SysJobLogController
 * @Description
 * @Date 2022/5/3
 */
@RestController
@RequestMapping("/jobLog")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysJobLogController {

    private final SysJobLogService sysJobLogService;

    /**
     * 查询定时任务调度日志列表
     **/
    @PostMapping("/jobLogPage")
    @PreAuthorize("@auth.hasAnyAuthority('JobLog')")
    public ApiResponse sysJobLogPage(@RequestBody SysJobLog sysJobLog) {
        Page<SysJobLog> listPage = sysJobLogService.getSysJobLogPage(sysJobLog);
        return ApiResponse.code(SUCCESS)
                .data("list", listPage.getRecords())
                .data("page", listPage.getPages())
                .data("total", listPage.getTotal())
                .message("查询成功！");
    }

    /**
     * 获取调度日志详细信息
     */
    @GetMapping("/getJobLogInfo/{id}")
    @PreAuthorize("@auth.hasAnyAuthority('JobLog')")
    public ApiResponse getJobLogInfo(@PathVariable String id) {
        return ApiResponse.code(SUCCESS).data("jobLog", sysJobLogService.getJobLogById(id));
    }


    @SysLog("批量删除调度日志")
    @RepeatSubmit
    @PostMapping("/delJobLogByIds")
    @PreAuthorize("@auth.hasAnyAuthority('DelJobLog')")
    public ApiResponse deleteJobByIds(@RequestBody List<String> ids) {
        int i = sysJobLogService.deleteJobLogByIds(ids);
        if (i > 0) {
            return ApiResponse.code(SUCCESS).message("删除成功！");
        } else {
            return ApiResponse.code(FAIL).message("删除发生异常！" + i);
        }
    }

    /**
     * 清除调度日志
     */
    @SysLog("清除调度日志")
    @RepeatSubmit
    @GetMapping("/clean")
    @PreAuthorize("@auth.hasAnyAuthority('DelJobLog')")
    public ApiResponse cleanLogs() {
        sysJobLogService.cleanJobLogs();
        return ApiResponse.code(SUCCESS).message("清除成功！");
    }
}
