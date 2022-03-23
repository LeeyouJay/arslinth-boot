package com.arslinthboot.common;

/**
 * @author Arslinth
 * @ClassName Constants
 * @Description 常量类
 * @Date 2022/2/10
 */
public class Constants {

    // 图片滑动误差的范围
    public static final int MOVE_CHECK_ERROR = 5;
    // 内存token标识
    public static final String LOGIN_USER_KEY = "login_user_key";

    public static final String LOGIN_TOKEN_KEY = "login_token:";

    // 尝试登入次数标识
    public static final String LOGIN_TIMES = "login_times:";

    // 缩略图后缀
    public static final String MINI_IMG_SUFFIX = "-thumbnail";

    //防重提交 redis key
    public static final String REPEAT_SUBMIT_KEY = "repeat_submit:";

    public static final String SLIDER_PREFIX = "captcha_ids:";

    //重置密码123456
    public static final String RESET_CODE = "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92";

}
