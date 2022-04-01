package com.arslinthboot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.arslinthboot.entity.OperLog;
import org.springframework.stereotype.Repository;

/**
 * @className: SysLogDao
 * @description: TODO 类描述
 * @author: Arslinth
 * @date: 2021/12/20
 **/
@Repository
public interface OperLogDao extends BaseMapper<OperLog> {
}
