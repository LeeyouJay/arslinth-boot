package com.arslinthboot.config.mybatisPlus;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Collection;

/**
 * @author Arslinth
 * @Description 扩展批量插入
 * @Date 2021/3/7
 */
public interface EasyBaseMapper<T> extends BaseMapper<T> {

    // 批量插入 仅适用于mysql
    Integer insertBatchSomeColumn(Collection<T> entityList);
}
