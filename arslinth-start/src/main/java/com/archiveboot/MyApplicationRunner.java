package com.archiveboot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author Arslinth
 * @ClassName MyApplicationRunner
 * @Description 自定义启动任务
 * @Date 2022/3/5
 */

@Slf4j
@Component
public class MyApplicationRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {

        log.info("启动后执行！");
    }
}
