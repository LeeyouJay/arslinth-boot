package com.arslinthboot.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;

/**
 * @className: SysLogin
 * @description: TODO 类描述
 * @author: Arslinth
 * @date: 2022/1/5
 **/
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_login_log")
public class LoginLog extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private String id;

    private String username;

    private String userType;

    private String ipAddr;

    private String ipSource;

    private String browser;

    private String sysName;

    private String message;

    private Boolean state;

}
