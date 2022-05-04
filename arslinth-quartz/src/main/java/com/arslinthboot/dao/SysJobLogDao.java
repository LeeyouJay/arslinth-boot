package com.arslinthboot.dao;

import com.arslinthboot.config.mybatisPlus.EasyBaseMapper;
import com.arslinthboot.entity.SysJobLog;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @author Arslinth
 * @Description
 * @Date 2022/5/3
 */
@Repository
public interface SysJobLogDao extends EasyBaseMapper<SysJobLog> {

    @Update("truncate table sys_job_log")
    void remove();
}
