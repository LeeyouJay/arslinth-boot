package com.archiveboot.config.tokenConfig;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @className: LoginUser
 * @description: 在线用户
 * @author: Arslinth
 * @date: 2022/2/10
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginUser<T> {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户唯一标识
     */
    private String token;

    /**
     * 登录时间
     */
    private Long loginTime;

    /**
     * 过期时间
     */
    private Long expireTime;

    /**
     * 登录IP地址
     */
    private String ipaddr;

    /**
     * 登录地点
     */
    private String loginLocation;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 权限列表
     */
    private Set<String> permissions;

    /**
     * 数据权限
     */
    private Set<String> dataScope;

    /**
     * 用户类型
     */
    private String userType;

    /**
     * 用户信息
     */
    private T user;


}
