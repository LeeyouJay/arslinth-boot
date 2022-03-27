package com.arslinthboot.common;

/**
 * @author Arslinth
 * @Description 数字常量
 * @Date 2022/2/10
 */
public interface ResponseCode {

    Integer SUCCESS = 200;

    Integer FAIL = 501;

    //验证不正确
    Integer VERIFY_FAIL = 502;

    //访问权限异常
    Integer ACCESS_NOT = 403;
    //token参数异常
    Integer NO_LOGIN = 401;
}
