package com.archiveboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;

import java.net.InetAddress;
import java.net.UnknownHostException;


@EnableAsync // 开启@Async注解
@EnableCaching //开启Redis缓存注解
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
@MapperScan("com.archiveboot.dao")
public class ArslinthBootApplication {
    //关闭druid使用ping方法超过60秒丢弃连接设置
    static {
        System.setProperty("druid.mysql.usePingMethod", "false");
    }

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext run = SpringApplication.run(ArslinthBootApplication.class, args);
        Environment env = run.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");
        System.out.println("\n----------------------------------------------------------\n\t" +
                "*****@_@***** 启动成功 *****@_@*****\n\t" +
                "Application  is running! Access URLs:\n\t" +
                "Local访问网址: \t\thttp://localhost:" + port + path + "\n\t" +
                "External访问网址: \thttp://" + ip + ":" + port + path + "\n" +
                "----------------------------------------------------------\n\t");
    }

}
