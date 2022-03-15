package com.archiveboot.utils;

import cn.hutool.core.convert.Convert;
import com.archiveboot.config.tokenConfig.LoginUser;
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
        return Convert.convert(LoginUser.class, authentication.getCredentials());
    }

    public static <T> T getUser(Class<T> c) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser<?> loginUser = Convert.convert(LoginUser.class, authentication.getCredentials());
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
