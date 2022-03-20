package com.arslinthboot.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @className: SysLog
 * @description: 系统日志
 * @author: Arslinth
 * @date: 2021/12/20
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysLog {

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

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;


}
