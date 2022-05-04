package com.arslinthboot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.arslinthboot.entity.LoginLog;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @className: SysLoginDao
 * @description:
 * @author: Arslinth
 * @date: 2022/1/5
 **/
@Repository
public interface LoginLogDao extends BaseMapper<LoginLog> {

    @Update("truncate table sys_login_log")
    void remove();
}
