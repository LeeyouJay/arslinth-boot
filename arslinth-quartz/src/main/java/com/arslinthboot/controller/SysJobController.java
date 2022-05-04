package com.arslinthboot.controller;

import cn.hutool.core.util.StrUtil;
import com.arslinthboot.annotation.RepeatSubmit;
import com.arslinthboot.annotation.SysLog;
import com.arslinthboot.common.ApiResponse;
import com.arslinthboot.entity.SysJob;
import com.arslinthboot.exception.TaskException;
import com.arslinthboot.service.SysJobService;
import com.arslinthboot.utils.CronUtils;
import com.arslinthboot.utils.ScheduleUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.arslinthboot.common.ResponseCode.FAIL;
import static com.arslinthboot.common.ResponseCode.SUCCESS;
import static com.arslinthboot.common.ScheduleConstants.*;

/**
 * @author Arslinth
 * @ClassName SysJobController
 * @Description 定时任务控制器
 * @Date 2022/5/3
 */
@RestController
@RequestMapping("/job")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysJobController {

    private final SysJobService sysJobService;


    /**
     * 分页查询定时任务列表
     **/
    @PostMapping("/sysJobPage")
    @PreAuthorize("@auth.hasAnyAuthority('Job')")
    public ApiResponse getSysJobPage(@RequestBody SysJob sysJob) {
        Page<SysJob> listPage = sysJobService.getSysJobPage(sysJob);
        return ApiResponse.code(SUCCESS)
                .data("list", listPage.getRecords())
                .data("page", listPage.getPages())
                .data("total", listPage.getTotal())
                .message("查询成功！");
    }

    /**
     * 获取定时任务详细信息
     */
    @GetMapping("/getJobInfo/{id}")
    @PreAuthorize("@auth.hasAnyAuthority('Job')")
    public ApiResponse getJobInfo(@PathVariable String id) {
        return ApiResponse.code(SUCCESS).data("job", sysJobService.getJobById(id));
    }

    /**
     * 新增定时任务
     */
    @SysLog("#{'添加定时任务:'+ #job.jobName}")
    @RepeatSubmit
    @PostMapping("/add")
    @PreAuthorize("@auth.hasAnyAuthority('AddJob')")
    public ApiResponse addJob(@RequestBody @Validated SysJob job) throws SchedulerException, TaskException {
        if (!CronUtils.isValid(job.getCronExpression())) {
            return ApiResponse.code(FAIL).message("新增任务'" + job.getJobName() + "'失败，Cron表达式不正确");
        } else if (StrUtil.containsIgnoreCase(job.getInvokeTarget(), LOOKUP_RMI)) {
            return ApiResponse.code(FAIL).message("新增任务'" + job.getJobName() + "'失败，目标字符串不允许'rmi'调用");
        } else if (StrUtil.containsAnyIgnoreCase(job.getInvokeTarget(), LOOKUP_LDAP, LOOKUP_LDAPS)) {
            return ApiResponse.code(FAIL).message("新增任务'" + job.getJobName() + "'失败，目标字符串不允许'ldap(s)'调用");
        } else if (StrUtil.containsAnyIgnoreCase(job.getInvokeTarget(), HTTP, HTTPS)) {
            return ApiResponse.code(FAIL).message("新增任务'" + job.getJobName() + "'失败，目标字符串不允许'http(s)'调用");
        } else if (StrUtil.containsAnyIgnoreCase(job.getInvokeTarget(), JOB_ERROR_STR)) {
            return ApiResponse.code(FAIL).message("新增任务'" + job.getJobName() + "'失败，目标字符串存在违规");
        } else if (!ScheduleUtils.whiteList(job.getInvokeTarget())) {
            return ApiResponse.code(FAIL).message("新增任务'" + job.getJobName() + "'失败，目标字符串不在白名单内");
        }
        sysJobService.insertJob(job);
        return ApiResponse.code(SUCCESS).message("新增任务'" + job.getJobName() + "'成功!");
    }

    /**
     * 修改定时任务
     */
    @SysLog("#{'修改定时任务:'+ #job.jobName}")
    @RepeatSubmit
    @PostMapping("/edit")
    @PreAuthorize("@auth.hasAnyAuthority('EditJob')")
    public ApiResponse editJob(@RequestBody @Validated SysJob job) throws SchedulerException, TaskException {
        if (!CronUtils.isValid(job.getCronExpression())) {
            return ApiResponse.code(FAIL).message("修改任务'" + job.getJobName() + "'失败，Cron表达式不正确");
        } else if (StrUtil.containsIgnoreCase(job.getInvokeTarget(), LOOKUP_RMI)) {
            return ApiResponse.code(FAIL).message("修改任务'" + job.getJobName() + "'失败，目标字符串不允许'rmi'调用");
        } else if (StrUtil.containsAnyIgnoreCase(job.getInvokeTarget(), LOOKUP_LDAP, LOOKUP_LDAPS)) {
            return ApiResponse.code(FAIL).message("修改任务'" + job.getJobName() + "'失败，目标字符串不允许'ldap(s)'调用");
        } else if (StrUtil.containsAnyIgnoreCase(job.getInvokeTarget(), HTTP, HTTPS)) {
            return ApiResponse.code(FAIL).message("修改任务'" + job.getJobName() + "'失败，目标字符串不允许'http(s)'调用");
        } else if (StrUtil.containsAnyIgnoreCase(job.getInvokeTarget(), JOB_ERROR_STR)) {
            return ApiResponse.code(FAIL).message("修改任务'" + job.getJobName() + "'失败，目标字符串存在违规");
        } else if (!ScheduleUtils.whiteList(job.getInvokeTarget())) {
            return ApiResponse.code(FAIL).message("修改任务'" + job.getJobName() + "'失败，目标字符串不在白名单内");
        }
        sysJobService.updateJob(job);
        return ApiResponse.code(SUCCESS).message("修改任务'" + job.getJobName() + "'成功!");
    }


    @SysLog("批量删除定时任务")
    @RepeatSubmit
    @PostMapping("/delJobByIds")
    @PreAuthorize("@auth.hasAnyAuthority('DelJob')")
    public ApiResponse deleteJobByIds(@RequestBody List<String> ids) throws SchedulerException, TaskException {
        sysJobService.deleteJobByIds(ids);
        return ApiResponse.code(SUCCESS).message("删除任务成功！");
    }

    /**
     * 修改定时任务状态
     */
    @SysLog("#{'修改定时任务状态:'+ #job.jobName}")
    @RepeatSubmit
    @PostMapping("/changeStatus")
    @PreAuthorize("@auth.hasAnyAuthority('EditJob')")
    public ApiResponse changeStatus(@RequestBody SysJob job) throws SchedulerException {
        SysJob sysJob = sysJobService.getJobById(job.getId());
        if (sysJob == null) {
            return ApiResponse.code(FAIL).message("设置的任务不存在！");
        }
        sysJob.setStatus(job.getStatus());
        sysJobService.changeStatus(sysJob);
        return ApiResponse.code(SUCCESS).message("状态修改成功！");
    }


    @SysLog("#{'立即执行一次:'+ #job.jobName}")
    @RepeatSubmit
    @PostMapping("/run")
    @PreAuthorize("@auth.hasAnyAuthority('RunJob')")
    public ApiResponse run(@RequestBody SysJob job) throws SchedulerException {
        SysJob sysJob = sysJobService.getJobById(job.getId());
        if (sysJob == null) {
            return ApiResponse.code(FAIL).message("执行的任务不存在！");
        }
        sysJobService.run(sysJob);
        return ApiResponse.code(SUCCESS).message("指令下发成功！");
    }

}
