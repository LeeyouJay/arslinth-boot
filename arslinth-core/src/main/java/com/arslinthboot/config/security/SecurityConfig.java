package com.arslinthboot.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

/**
 * @className: SecurityConfig
 * @description: Security配置
 * @author: Arslinth
 * @date: 2022/2/10
 **/
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthorizedEntryPoint jwtAuthorizedEntryPoint;

    private final JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();
        //放开所有请求
        http.authorizeRequests().anyRequest().permitAll();

        //所有请求都需要被认证
//        http.authorizeRequests(). antMatchers(HttpMethod.GET,
//                        "/slider/image",
//                        "/user/logout",
//                        "/loadImg/**",
//                        "/file/**").permitAll()
//                .antMatchers(HttpMethod.POST,
//                        "/user/login").permitAll().
//                anyRequest().
//                authenticated();

        http.exceptionHandling().authenticationEntryPoint(jwtAuthorizedEntryPoint).
                //关闭session  用token验证，所以关闭session
                        and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
                and().addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
