package com.arslinthboot.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author Arslinth
 * @ClassName SysMenu
 * @Description 系统菜单
 * @Date 2022/3/10
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysMenu {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String parentId;

    private Integer indexNum;

    private String path;

    private String name;

    private Boolean hidden;

    private String component;

    private String label;

    private String link;

    private String icon;

    private Boolean keepAlive;

    private Integer level;

    private String menuType;

    @TableField(exist = false)
    private List<SysMenu> children;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @TableField(exist = false)
    private Meta meta;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Meta {

        private String title;

        private String icon;

        private String link;

        private Boolean keepAlive;
    }
}
