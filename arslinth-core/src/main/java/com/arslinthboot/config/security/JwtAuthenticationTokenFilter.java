package com.arslinthboot.config.security;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;

import com.arslinthboot.common.ApiResponse;
import com.arslinthboot.config.tokenConfig.LoginUser;
import com.arslinthboot.config.tokenConfig.TokenProperty;
import com.arslinthboot.config.tokenConfig.TokenService;
import com.arslinthboot.exception.IllegalTokenException;
import com.arslinthboot.utils.HttpServletUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import static com.arslinthboot.common.ResponseCode.NO_LOGIN;


/**
 * @className: JwtAuthenticationTokenFilter
 * @description: token 验证过滤器
 * @author: Arslinth
 * @date: 2022/2/10
 **/
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    private final TokenProperty tokenProperty;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(tokenProperty.getHeader());
        if (StrUtil.isEmpty(header)) {
            filterChain.doFilter(request, response);
            return;
        }
        LoginUser<?> loginUser;
        try {

            loginUser = tokenService.checkToken(header);

        } catch (IllegalTokenException e) {
            //这里返回具体的验证错误信息
            log.info(e.getMessage());
            ApiResponse res = ApiResponse.code(NO_LOGIN).message(e.getMessage());
            HttpServletUtil.print(response, JSON.toJSONString(res));
            return;
        }

        String userId = loginUser.getUserId();
        Set<String> permissions = loginUser.getPermissions();
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        permissions.forEach(val -> {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(val);
            authorities.add(authority);
        });
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, loginUser, authorities);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }
}
