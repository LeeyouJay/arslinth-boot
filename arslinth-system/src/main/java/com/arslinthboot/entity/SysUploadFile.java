package com.arslinthboot.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Arslinth
 * @ClassName SysFile
 * @Description 系统上传文件
 * @Date 2022/4/23
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysUploadFile {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String fileName;

    private String realName;

    @JsonIgnore
    private String filePath;

    private String fileType;

    private String fileExtra;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;
}
