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
 * @ClassName SysRole
 * @Description TODO
 * @Date 2021/3/7
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(autoResultMap = true)
public class SysRole {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String role;

    private String roleName;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private Set<String> permissions;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

}
