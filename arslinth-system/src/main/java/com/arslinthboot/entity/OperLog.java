package com.arslinthboot.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @className: SysLog
 * @description: 操作日志
 * @author: Arslinth
 * @date: 2021/12/20
 **/

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_oper_log")
public class OperLog extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String ipAddr;

    private String ipSource;

    private String browser;

    private String sysName;

    private String username;

    private String parameters;

    private String method;

    private Integer resultCode;

    private String resultMessage;

    private String details;

}
