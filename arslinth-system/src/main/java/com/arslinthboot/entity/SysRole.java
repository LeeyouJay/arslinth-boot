package com.arslinthboot.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

/**
 * @author Arslinth
 * @ClassName SysRole
 * @Description 系统角色
 * @Date 2021/3/7
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(autoResultMap = true)
public class SysRole extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String role;

    private Integer indexNum;

    private Boolean strictly;

    private String roleName;

    private String notes;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private Set<String> permissions;

}
