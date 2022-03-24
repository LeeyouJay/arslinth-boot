package com.arslinthboot.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

/**
 * @author Arslinth
 * @ClassName SysUser
 * @Description 登入用户
 * @Date 2021/7/25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(autoResultMap = true)
public class SysUser {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private Set<String> roleIds;

    private String username;

    private String password;

    private String nickName;

    private String sex;

    private String phone;

    private String email;

    private String avatar;

    private Boolean forbidden;

    private Boolean setRight;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private Set<String> permissions;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

}
