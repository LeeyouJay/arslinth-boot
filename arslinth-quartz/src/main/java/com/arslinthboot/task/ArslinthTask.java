package com.arslinthboot.task;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Arslinth
 * @ClassName ArslinthTask
 * @Description 测试任务类
 * @Date 2022/5/4
 */
@Slf4j
@Component("arslinthTask")
public class ArslinthTask {

    public void doTask() {
        long id = Thread.currentThread().getId();
        log.info("无参任务！线程id：" + id);
    }
}
