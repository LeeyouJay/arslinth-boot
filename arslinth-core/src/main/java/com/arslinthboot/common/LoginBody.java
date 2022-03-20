package com.arslinthboot.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @className: LoginBody
 * @description: 用户登入对象
 * @author: Arslinth
 * @date: 2022/2/11
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginBody {

    private String username;

    private String password;

    private String captchaUUid;

    private String moveX;
}
