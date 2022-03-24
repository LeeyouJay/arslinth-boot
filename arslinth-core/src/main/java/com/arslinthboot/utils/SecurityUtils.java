package com.arslinthboot.utils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.arslinthboot.config.tokenConfig.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Set;

/**
 * @className: SecurityUtils
 * @description: 安全服务工具
 * @author: Arslinth
 * @date: 2022/3/4
 **/
public class SecurityUtils {

    public static String getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getPrincipal().toString();
    }

    public static LoginUser<?> getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object credentials = authentication.getCredentials();
        if (ObjectUtil.isEmpty(credentials)) return null;
        return Convert.convert(LoginUser.class, credentials);
    }

    public static <T> T getUser(Class<T> c) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object credentials = authentication.getCredentials();
        if (ObjectUtil.isEmpty(credentials)) return null;
        LoginUser<?> loginUser = Convert.convert(LoginUser.class, credentials);
        return Convert.convert(c, loginUser.getUser());
    }

    public static <T> LoginUser<T> initLoginUser(T t, String userId, String userType, Set<String> auths) {
        return LoginUser.<T>builder()
                .user(t)
                .userId(userId)
                .userType(userType)
                .permissions(auths).build();
    }
}
