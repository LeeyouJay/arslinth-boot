package com.arslinthboot.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Arslinth
 * @ClassName SysDict
 * @Description 系统字典
 * @Date 2022/3/28
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SysDict extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String dictType;

    private String dictName;

    private String dictValue;

    private String parentId;

    private String parentValue;

    private Integer level;

    private String notes;

}
