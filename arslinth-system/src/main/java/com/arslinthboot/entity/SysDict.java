package com.arslinthboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @author Arslinth
 * @ClassName SysDict
 * @Description 系统字典
 * @Date 2022/3/28
 */

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SysDict extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String parentId;

    private String dictName;

    private String dictValue;

    private Integer indexNum;

    private String parentValue;

    private String notes;

}
