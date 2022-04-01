package com.arslinthboot.annotation;

import com.alibaba.fastjson.JSON;
import com.arslinthboot.common.ApiResponse;
import com.arslinthboot.entity.OperLog;
import com.arslinthboot.service.SysLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

import static com.arslinthboot.common.ResponseCode.FAIL;


/**
 * @author Arslinth
 * @ClassName SysLogAspect
 * @Description 系统日志切面
 * @Date 2021/12/20
 */
@Slf4j
@Aspect
@Component
public class SysLogAspect {

    @Resource
    private SysLogService sysLogService;


    // 设置切点 是注解的路径
    @Pointcut("@annotation(com.arslinthboot.annotation.Log)")
    public void logPointCut() {
    }


    @Around("logPointCut()")
    public Object doAround(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String parameters = JSON.toJSONString(joinPoint.getArgs());
        log.info("入参：{}", parameters);
        Log annotation = method.getAnnotation(Log.class);
        String details = annotation.value();
        log.info("备注：{}", details);
        Log logRecord = ((MethodSignature) joinPoint.getSignature())
                .getMethod()
                .getAnnotation(Log.class);
        String methodName = "";
        if (logRecord != null) {
            methodName = ((MethodSignature) joinPoint.getSignature())
                    .getMethod().toGenericString();
        }
        log.info("访问方法：{}", methodName);

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        OperLog operLog = OperLog.builder()
                .parameters(parameters)
                .details(details)
                .method(methodName)
                .build();
        try {
            ApiResponse response = (ApiResponse) joinPoint.proceed();
            log.info("返回结果：{}", response.getCode());
            log.info("返回信息：{}", response.getMessage());
            operLog.setResultCode(response.getCode());
            operLog.setResultMessage(response.getMessage());
            sysLogService.saveOperLog(request, operLog);
            return response;
        } catch (Throwable throwable) {
            operLog.setResultCode(FAIL);
            operLog.setResultMessage("请求发生异常！");
            sysLogService.saveOperLog(request, operLog);
            log.error("代理发生异常, 异常信息{}", throwable.getMessage());
            return ApiResponse.code(FAIL);
        }

    }
}
