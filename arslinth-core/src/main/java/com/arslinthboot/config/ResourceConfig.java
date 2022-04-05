package com.arslinthboot.config;

import com.arslinthboot.config.interceptor.RepeatSubmitInterceptor;
import com.arslinthboot.properties.FileProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.Collections;


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

    private final RepeatSubmitInterceptor repeatSubmitInterceptor;


    /**
     * 图片与文件访问映射
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(fileProperty.getImgStaticPath() + "**").addResourceLocations("file:" + fileProperty.getImgFolder());
        registry.addResourceHandler(fileProperty.getUploadStaticPath() + "**").addResourceLocations("file:" + fileProperty.getUploadFolder());
    }

    /**
     * 自定义拦截规则
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(repeatSubmitInterceptor).addPathPatterns("/**");
    }

    /**
     * 跨域配置
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 不允许cookies跨域
        configuration.setAllowCredentials(false);
        // 预检请求的缓存时间（秒），即在这个时间段里，对于相同的跨域请求不会再预检了
        configuration.setMaxAge(18000L);
        // 允许访问的头信息,*表示全部
        configuration.addAllowedHeader("*");
        // 允许向该服务器提交请求的URI，*表示全部允许。。这里尽量限制来源域，比如http://xxxx:8080,以降低安全风险。。
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
