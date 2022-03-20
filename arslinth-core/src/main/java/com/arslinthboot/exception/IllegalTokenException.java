package com.arslinthboot.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @className: IllegalTokenException
 * @description: 自定义token异常
 * @author: Arslinth
 * @date: 2022/3/4
 **/
public class IllegalTokenException extends AuthenticationException {
    public IllegalTokenException(String message) {
        super(message);
    }
}
