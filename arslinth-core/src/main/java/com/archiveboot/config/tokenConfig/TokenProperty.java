package com.archiveboot.config.tokenConfig;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @className: Token
 * @description: token 配置文件
 * @author: Arslinth
 * @date: 2022/2/10
 **/
@Data
@ConfigurationProperties(prefix = "token")
public class TokenProperty {

    private String header;

    private String secret;

    private int expireTime;

    private int interval;
}
