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
 * @className: SysLogin
 * @description: TODO 类描述
 * @author: Arslinth
 * @date: 2022/1/5
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysLogin {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String ipAddr;

    private String ipSource;

    private String browser;

    private String sysName;

    private String username;

    private String empId;

    private String message;

    private Boolean state;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}
