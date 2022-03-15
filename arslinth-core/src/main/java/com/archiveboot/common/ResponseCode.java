package com.archiveboot.common;

/**
 * @author Arslinth
 * @Description 数字常量
 * @Date 2022/2/10
 */
public interface ResponseCode {

    Integer SUCCESS = 200;

    //微信成功获取openId但不返回token
    Integer SUCCESS_WITH_NO_TOKEN = 201;

    //账号不存在
    Integer NO_USER = 2000;
    //账号已被禁用
    Integer IS_LOCKED = 2001;
    //密码不正确
    Integer NO_PASS = 2002;
    //验证失败
    Integer CHECK_CAPTCHA_FAIL = 2003;

    Integer FAIL = 501;
    //访问权限异常
    Integer ACCESS_NOT = 403;
    //token参数异常
    Integer NO_LOGIN = 401;
}
