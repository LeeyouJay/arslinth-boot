package com.arslinthboot.utils;

import com.arslinthboot.entity.BaseEntity;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Optional;

/**
 * @author Arslinth
 * @ClassName PageUtil
 * @Description 分页
 * @Date 2022/3/30
 */
public class PageUtil {

    public static <T extends BaseEntity> Page<T> buildPage(T entity) {
        Long pageIndex = Optional.ofNullable(entity).map(BaseEntity::getPageIndex).orElse(1L);
        Long pageSize = Optional.ofNullable(entity).map(BaseEntity::getPageSize).orElse(10L);
        return new Page<T>(pageIndex, pageSize);
    }
}
