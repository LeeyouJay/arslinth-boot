package com.arslinthboot.utils;

import com.arslinthboot.entity.BaseEntity;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author Arslinth
 * @ClassName PageUtil
 * @Description 分页
 * @Date 2022/3/30
 */
public class SelectDomain {

    public static <T extends BaseEntity> Page<T> buildPage() {
        HttpServletRequest request = HttpServletUtil.getRequest();
        String pageIndex = request.getParameter("pageIndex");
        String pageSize = request.getParameter("pageSize");
        Long index = Optional.ofNullable(pageIndex).map(Long::parseLong).orElse(1L);
        Long size = Optional.ofNullable(pageSize).map(Long::parseLong).orElse(10L);
        return new Page<T>(index, size);
    }
}
