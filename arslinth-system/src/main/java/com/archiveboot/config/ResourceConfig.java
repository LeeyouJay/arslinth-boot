package com.archiveboot.config;


import com.archiveboot.properties.FileProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
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

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        // 允许cookies跨域
        config.setAllowCredentials(false);
        // 允许向该服务器提交请求的URI，*表示全部允许。。这里尽量限制来源域，比如http://xxxx:8080,以降低安全风险。。
        config.addAllowedOrigin("*");
        // 允许访问的头信息,*表示全部
        config.addAllowedHeader("*");
        // 预检请求的缓存时间（秒），即在这个时间段里，对于相同的跨域请求不会再预检了
        config.setMaxAge(18000L);
        // 允许提交请求的方法，*表示全部允许，也可以单独设置GET、PUT等
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
