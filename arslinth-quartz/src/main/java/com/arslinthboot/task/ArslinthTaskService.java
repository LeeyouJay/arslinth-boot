package com.arslinthboot.task;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Arslinth
 * @ClassName ArslinthTaskService
 * @Description 服务类任务测试
 * @Date 2022/5/4
 */
@Slf4j
@Service
public class ArslinthTaskService {

    public void params(String params) {
        System.out.println("执行有参方法：" + params);
    }

    public void multipleParams(String s, Boolean b, Long l, Double d, Integer i) {
        log.info(StrUtil.format("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整型{}", s, b, l, d, i));
    }
}
