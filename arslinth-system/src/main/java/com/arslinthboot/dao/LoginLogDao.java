package com.arslinthboot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.arslinthboot.entity.LoginLog;
import org.springframework.stereotype.Repository;

/**
 * @className: SysLoginDao
 * @description: TODO 类描述
 * @author: Arslinth
 * @date: 2022/1/5
 **/
@Repository
public interface LoginLogDao extends BaseMapper<LoginLog> {
}
