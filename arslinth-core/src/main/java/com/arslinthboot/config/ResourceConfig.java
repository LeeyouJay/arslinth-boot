package com.arslinthboot.config;

import com.arslinthboot.properties.FileProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * @className: ResourceConfig
 * @description: 资源访问配置
 * @author: Arslinth
 * @date: 2022/2/10
 **/
@Configuration
@EnableConfigurationProperties(FileProperty.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ResourceConfig implements WebMvcConfigurer {

    private final FileProperty fileProperty;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(fileProperty.getImgStaticPath()).addResourceLocations("file:" + fileProperty.getImgFolder());
        registry.addResourceHandler(fileProperty.getUploadStaticPath()).addResourceLocations("file:" + fileProperty.getUploadFolder());
    }

}
