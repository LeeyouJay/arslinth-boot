package com.arslinthboot.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @className: FileProperty
 * @description: 文件配置
 * @author: Arslinth
 * @date: 2022/2/10
 **/
@Data
@ConfigurationProperties(prefix = "file")
public class FileProperty {

    //上传的图片映射路径
    private String imgStaticPath;

    //本地上传图片真实路径
    private String imgFolder;

    //上传的文件映射路径
    private String uploadStaticPath;

    //本地上传文件真实路径
    private String uploadFolder;
}
