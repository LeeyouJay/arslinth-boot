package com.arslinthboot.config.security;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.arslinthboot.common.ApiResponse;
import com.arslinthboot.config.tokenConfig.TokenProperty;
import com.arslinthboot.utils.HttpServletUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.arslinthboot.common.ResponseCode.ACCESS_NOT;
import static com.arslinthboot.common.ResponseCode.NO_LOGIN;


/**
 * @className: JwtAuthorizedEntryPoint
 * @description: 认证失败处理类
 * @author: Arslinth
 * @date: 2022/2/11
 **/
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtAuthorizedEntryPoint implements AuthenticationEntryPoint {

    private final TokenProperty tokenProperty;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String authorization = request.getHeader(tokenProperty.getHeader());
        String message = StrUtil.format("请求访问：{}，认证失败，您的访问权限不足！", request.getRequestURI());
        ApiResponse apiResponse = StrUtil.isEmpty(authorization) ? ApiResponse.code(NO_LOGIN).message("您的登入信息已失效！") :
                ApiResponse.code(ACCESS_NOT).message(message);
        HttpServletUtil.print(response, JSON.toJSONString(apiResponse));

    }
}
