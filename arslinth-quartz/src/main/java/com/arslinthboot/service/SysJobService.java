package com.arslinthboot.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.arslinthboot.common.ScheduleConstants;
import com.arslinthboot.dao.SysJobDao;
import com.arslinthboot.dao.SysJobLogDao;
import com.arslinthboot.entity.SysDict;
import com.arslinthboot.entity.SysJob;
import com.arslinthboot.entity.SysJobLog;
import com.arslinthboot.exception.TaskException;
import com.arslinthboot.utils.CronUtils;
import com.arslinthboot.utils.ScheduleUtils;
import com.arslinthboot.utils.SelectDomain;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

/**
 * @author Arslinth
 * @ClassName SysJobService
 * @Description
 * @Date 2022/5/3
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysJobService {

    private final SysJobDao sysJobDao;

    private final SysJobLogDao sysJobLogDao;

    private final Scheduler scheduler;


    /**
     * 项目启动时，初始化定时器 主要是防止手动修改数据库导致未同步到定时任务处理（注：不能手动修改数据库ID和任务组名，否则会导致脏数据）
     */
    @PostConstruct
    public void init() throws SchedulerException, TaskException {
        scheduler.clear();
        List<SysJob> jobList = sysJobDao.selectList(null);
        for (SysJob job : jobList) {
            ScheduleUtils.createScheduleJob(scheduler, job);
        }
    }

    /**
     * 获取quartz调度器的计划任务
     */
    public Page<SysJob> getSysJobPage(SysJob sysJob) {
        QueryWrapper<SysJob> wrapper = new QueryWrapper<>();
        Page<SysJob> page = SelectDomain.buildPage();
        String jobName = sysJob.getJobName();
        String jobGroup = sysJob.getJobGroup();
        if (StrUtil.isNotEmpty(sysJob.getJobName())) {
            wrapper.like("job_name", jobName);
        }
        if (StrUtil.isNotEmpty(jobGroup)) {
            wrapper.eq("job_group", jobGroup);
        }
        return sysJobDao.selectPage(page, wrapper);
    }

    /**
     * 通过调度任务ID查询调度信息
     *
     * @param id 调度任务ID
     * @return 调度任务对象信息
     */
    public SysJob getJobById(String id) {
        SysJob sysJob = sysJobDao.selectById(id);
        if (StrUtil.isNotEmpty(sysJob.getCronExpression())) {
            Date nextExecution = CronUtils.getNextExecution(sysJob.getCronExpression());
            sysJob.setNextValidTime(nextExecution);
        }
        return sysJob;
    }

    /**
     * 新增任务
     *
     * @param job 调度信息 调度信息
     */
    @Transactional(rollbackFor = Exception.class)
    public int insertJob(SysJob job) throws SchedulerException, TaskException {
        //暂停
        job.setStatus(ScheduleConstants.Status.PAUSE.getValue());
        int rows = sysJobDao.insert(job);
        if (rows > 0) {
            ScheduleUtils.createScheduleJob(scheduler, job);
        }
        return rows;
    }

    /**
     * 任务调度状态修改
     *
     * @param job 调度信息
     */
    public int changeStatus(SysJob job) throws SchedulerException {
        Boolean status = job.getStatus();
        return status ? resumeJob(job) : pauseJob(job);
    }

    /**
     * 立即运行任务
     *
     * @param job 调度信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void run(SysJob job) throws SchedulerException {
        String jobId = job.getId();
        String jobGroup = job.getJobGroup();
        // 参数
        JobDataMap dataMap = new JobDataMap();
        dataMap.put(ScheduleConstants.TASK_PROPERTIES, job);
        scheduler.triggerJob(ScheduleUtils.getJobKey(jobId, jobGroup), dataMap);
    }


    /**
     * 更新任务的时间表达式
     *
     * @param job 调度信息
     */
    @Transactional(rollbackFor = Exception.class)
    public int updateJob(SysJob job) throws SchedulerException, TaskException {
        SysJob properties = sysJobDao.selectById(job.getId());
        int rows = sysJobDao.updateById(job);
        if (rows > 0) {
            updateSchedulerJob(job, properties.getJobGroup());
        }
        return rows;
    }

    /**
     * 批量删除调度信息
     *
     * @param jobIds 需要删除的任务ID
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteJobByIds(List<String> jobIds) throws SchedulerException {
        if (CollUtil.isEmpty(jobIds)) {
            return;
        }
        List<SysJob> sysJobs = sysJobDao.selectList(new QueryWrapper<SysJob>().in("id", jobIds));
        for (SysJob job : sysJobs) {
            deleteJob(job);
        }
    }

    /**
     * 更新任务
     *
     * @param job      任务对象
     * @param jobGroup 任务组名
     */
    public void updateSchedulerJob(SysJob job, String jobGroup) throws SchedulerException, TaskException {
        String jobId = job.getId();
        // 判断是否存在
        JobKey jobKey = ScheduleUtils.getJobKey(jobId, jobGroup);
        if (scheduler.checkExists(jobKey)) {
            // 防止创建时存在数据问题 先移除，然后在执行创建操作
            scheduler.deleteJob(jobKey);
        }
        ScheduleUtils.createScheduleJob(scheduler, job);
    }

    /**
     * 恢复任务
     *
     * @param job 调度信息
     */
    @Transactional(rollbackFor = Exception.class)
    public int resumeJob(SysJob job) throws SchedulerException {
        String jobId = job.getId();
        String jobGroup = job.getJobGroup();
        job.setStatus(ScheduleConstants.Status.NORMAL.getValue());
        int rows = sysJobDao.updateById(job);
        if (rows > 0) {
            scheduler.resumeJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
        return rows;
    }

    /**
     * 暂停任务
     *
     * @param job 调度信息
     */
    public int pauseJob(SysJob job) throws SchedulerException {
        String jobId = job.getId();
        String jobGroup = job.getJobGroup();
        job.setStatus(ScheduleConstants.Status.PAUSE.getValue());
        int rows = sysJobDao.updateById(job);
        if (rows > 0) {
            scheduler.pauseJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
        return rows;
    }

    /**
     * 删除任务后，所对应的trigger也将被删除
     *
     * @param job 调度信息
     */
    @Transactional(rollbackFor = Exception.class)
    public int deleteJob(SysJob job) throws SchedulerException {
        String jobId = job.getId();
        String jobGroup = job.getJobGroup();
        int rows = sysJobDao.deleteById(jobId);
        if (rows > 0) {
            sysJobLogDao.delete(new QueryWrapper<SysJobLog>().eq("job_id", jobId));
            scheduler.deleteJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
        return rows;
    }


}
