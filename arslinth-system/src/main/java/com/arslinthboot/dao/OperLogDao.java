package com.arslinthboot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.arslinthboot.entity.OperLog;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @className: SysLogDao
 * @description:
 * @author: Arslinth
 * @date: 2021/12/20
 **/
@Repository
public interface OperLogDao extends BaseMapper<OperLog> {

    @Update("truncate table sys_oper_log")
    void remove();
}
