package com.arslinthboot.utils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.arslinthboot.config.tokenConfig.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.Set;

/**
 * @className: SecurityUtils
 * @description: 安全服务工具
 * @author: Arslinth
 * @date: 2022/3/4
 **/
public class SecurityUtils {

    /**
     * 获取当前用户id
     **/
    public static String getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Optional.ofNullable(authentication.getPrincipal()).map(Object::toString).orElse("未知");
    }

    /**
     * 获取当前登入对象
     *
     **/
    public static <T> LoginUser<T> getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object credentials = authentication.getCredentials();
        if (ObjectUtil.isEmpty(credentials)) return null;
        return Convert.convert(LoginUser.class, credentials);
    }

    /**
     * 获取当前用户对象
     *
     * @param c 用户实体类
     */
    public static <T> T getUser(Class<T> c) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object credentials = authentication.getCredentials();
        if (ObjectUtil.isEmpty(credentials)) return null;
        LoginUser<?> loginUser = Convert.convert(LoginUser.class, credentials);
        return Convert.convert(c, loginUser.getUser());
    }

    /**
     * 初始化登入对象
     *
     * @param t 用户实体
     * @param userId 用户id
     * @param username 内存中用户username
     * @param userType 用户类型
     * @param auths 权限
     * @param dataScope 数据范围
     */
    public static <T> LoginUser<T> initLoginUser(T t, String userId, String username, String userType, Set<String> auths, Set<String> dataScope) {
        return LoginUser.<T>builder()
                .user(t)
                .userId(userId)
                .username(username)
                .userType(userType)
                .dataScope(dataScope)
                .permissions(auths).build();
    }
}
