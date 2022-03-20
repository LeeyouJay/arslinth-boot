package com.arslinthboot.config.interceptor;

import com.alibaba.fastjson.JSON;
import com.arslinthboot.annotation.RepeatSubmit;
import com.arslinthboot.common.ApiResponse;
import com.arslinthboot.utils.HttpServletUtil;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

import static com.arslinthboot.common.ResponseCode.FAIL;

/**
 * @className: RepeatSubmitInterceptor
 * @description: 防重放拦截器
 * @author: Arslinth
 * @date: 2022/2/18
 **/
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RepeatSubmitInterceptor implements HandlerInterceptor {

    private final RepeatInterceptorService repeatInterceptorService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        RepeatSubmit annotation = method.getAnnotation(RepeatSubmit.class);
        if (annotation == null) {
            return true;
        }
        if (repeatInterceptorService.isRepeatSubmit(request, annotation)) {
            ApiResponse message = ApiResponse.code(FAIL).message(annotation.message());
            HttpServletUtil.print(response, JSON.toJSONString(message));
            return false;
        }
        return true;

    }

}
