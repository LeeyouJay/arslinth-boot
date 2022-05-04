package com.arslinthboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;

/**
 * @author Arslinth
 * @ClassName SysJobLog
 * @Description 定时任务调度日志表
 * @Date 2022/5/3
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SysJobLog extends BaseEntity {

    /**
     * ID主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 关联任务id
     */
    private String jobId;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 任务组名
     */
    private String jobGroup;

    /**
     * 调用目标字符串
     */
    private String invokeTarget;

    /**
     * 日志信息
     */
    private String jobMessage;

    /**
     * 执行状态（true正常 false异常）
     */
    private Boolean status;

    /**
     * 异常信息
     */
    private String exceptionInfo;

    /**
     * 开始时间
     */
    @TableField(exist = false)
    private Date startTime;

    /**
     * 停止时间
     */
    @TableField(exist = false)
    private Date stopTime;
}
