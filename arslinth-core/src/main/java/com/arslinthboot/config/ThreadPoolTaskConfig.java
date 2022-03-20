package com.arslinthboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @className: ThreadPoolTaskConfig
 * @description: 多线程配置类
 * @author: Arslinth
 * @date: 2021/12/15
 **/
@Configuration
@EnableAsync
public class ThreadPoolTaskConfig {

    //核心线程数（默认线程数）
    private static final int corePoolSize = 10;
    //最大线程数
    private static final int maxPoolSize = 50;
    //允许线程空闲时间（单位：默认为秒）
    private static final int keepAliveTime = 5;
    // 缓冲队列大小
    private static final int queueCapacity = 10;
    //线程池名前缀
    private static final String threadNamePrefix = "Async-Service-";

    @Bean // bean的名称，默认为首字母小写的方法名
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveTime);
        executor.setThreadNamePrefix(threadNamePrefix);

        // 线程池对拒绝任务的处理策略
        // CallerRunsPolicy：由调用线程（提交任务的线程）处理该任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 初始化
        executor.initialize();
        return executor;
    }

}
