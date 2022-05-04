package com.arslinthboot.service;

import cn.hutool.core.util.StrUtil;
import com.arslinthboot.dao.SysJobLogDao;
import com.arslinthboot.entity.SysJob;
import com.arslinthboot.entity.SysJobLog;
import com.arslinthboot.utils.SelectDomain;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Arslinth
 * @ClassName SysJobLogService
 * @Description
 * @Date 2022/5/3
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysJobLogService {

    private final SysJobLogDao sysJobLogDao;


    public Page<SysJobLog> getSysJobLogPage(SysJobLog sysJobLog) {
        QueryWrapper<SysJobLog> wrapper = new QueryWrapper<>();
        Page<SysJobLog> page = SelectDomain.buildPage();
        String jobId = Optional.ofNullable(sysJobLog.getJobId()).orElse("-1");
        Boolean status = sysJobLog.getStatus();
        wrapper.eq("job_id", jobId);
        if (status != null) {
            wrapper.eq("status", status);
        }
        return sysJobLogDao.selectPage(page, wrapper);

    }

    public int addJobLog(SysJobLog sysJobLog) {
        return sysJobLogDao.insert(sysJobLog);
    }

    public SysJobLog getJobLogById(String id) {
        return sysJobLogDao.selectById(id);
    }

    public int deleteJobLogByIds(List<String> ids) {
        return sysJobLogDao.deleteBatchIds(ids);
    }

    public void cleanJobLogs() {
        sysJobLogDao.remove();
    }
}
