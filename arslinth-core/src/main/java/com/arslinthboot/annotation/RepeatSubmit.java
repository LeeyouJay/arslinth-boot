package com.arslinthboot.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @className: RepeatSubmit
 * @description: 防重提交注解
 * @author: Arslinth
 * @date: 2022/2/18
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RepeatSubmit {

    int interval() default 1500;

    String message() default "数据正在处理中，请稍候再试";
}
