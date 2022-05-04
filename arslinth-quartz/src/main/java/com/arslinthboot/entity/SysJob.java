package com.arslinthboot.entity;

import com.arslinthboot.common.ScheduleConstants;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * @author Arslinth
 * @ClassName SysJob
 * @Description 定时任务
 * @Date 2022/5/3
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SysJob extends BaseEntity {

    /**
     * 任务ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 任务名称
     */
    @NotNull(message = "任务名称不能为空")
    @Size(min = 0, max = 64, message = "任务名称不能超过64个字符")
    private String jobName;

    /**
     * 任务组名
     */
    private String jobGroup;

    /**
     * 调用目标字符串
     */
    @NotNull(message = "调用目标字符串不能为空")
    @Size(min = 0, max = 500, message = "调用目标字符串长度不能超过500个字符")
    private String invokeTarget;

    /**
     * cron执行表达式
     */
    @NotNull(message = "Cron执行表达式不能为空")
    @Size(min = 0, max = 255, message = "Cron执行表达式不能超过255个字符")
    private String cronExpression;

    /**
     * cron计划策略
     */
    private String misfirePolicy = ScheduleConstants.MISFIRE_DEFAULT;

    /**
     * 是否并发执行（true允许 false禁止）
     */
    private Boolean concurrent;

    /**
     * 任务状态（true正常 false暂停）
     */
    private Boolean status;

    @TableField(exist = false)
    private Date nextValidTime;

}
