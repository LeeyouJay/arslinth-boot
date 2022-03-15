package com.archiveboot.annotation;

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

    int interval() default 4500;

    String message() default "不允许重复提交，请稍候再试";
}
