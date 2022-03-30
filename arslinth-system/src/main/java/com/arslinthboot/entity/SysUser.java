package com.arslinthboot.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.Set;

/**
 * @author Arslinth
 * @ClassName SysUser
 * @Description 登入用户
 * @Date 2021/7/25
 */

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(autoResultMap = true)
public class SysUser extends BaseEntity {

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

    private String notes;

}
