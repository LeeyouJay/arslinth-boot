package com.archiveboot.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @className: SecurityConfig
 * @description: Security配置
 * @author: Arslinth
 * @date: 2022/2/10
 **/
@EnableWebSecurity// 开启 Security
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)// 开启注解配置支持
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthorizedEntryPoint jwtAuthorizedEntryPoint;

    private final JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();
        http.authorizeRequests().
                anyRequest().
//                authenticated().//所有请求都需要被认证
        permitAll().//所有请求通过
                and().exceptionHandling().authenticationEntryPoint(jwtAuthorizedEntryPoint).//未登入和未授权时的处理
                and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).//关闭session  用token验证，所以关闭session
                and().addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
