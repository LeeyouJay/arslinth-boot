package com.arslinthboot.annotation;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.arslinthboot.common.ApiResponse;
import com.arslinthboot.config.tokenConfig.LoginUser;
import com.arslinthboot.entity.OperLog;
import com.arslinthboot.service.SysLogService;
import com.arslinthboot.utils.HttpServletUtil;
import com.arslinthboot.utils.IpInfoUtil;
import com.arslinthboot.utils.SecurityUtils;
import com.arslinthboot.utils.SpELUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

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
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysLogAspect {

    private final SysLogService sysLogService;


    @AfterReturning(pointcut = "@annotation(annotation)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, SysLog annotation, Object jsonResult) {
        handleLog(joinPoint, annotation, null, jsonResult);
    }


    @AfterThrowing(value = "@annotation(annotation)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, SysLog annotation, Exception e) {

        handleLog(joinPoint, annotation, e, null);

    }

    protected void handleLog(final JoinPoint joinPoint, SysLog annotation, final Exception e, Object jsonResult) {
        HttpServletRequest request = HttpServletUtil.getRequest();
        LoginUser<?> loginUser = SecurityUtils.getLoginUser();
        String username = Optional.ofNullable(loginUser).map(LoginUser::getUsername).orElse("未知用户");
        String userType = Optional.ofNullable(loginUser).map(LoginUser::getUserType).orElse("未知用户类型");

        String requestMethod = request.getMethod();
        log.info("请求方式：{}", requestMethod);

        String parameters = JSON.toJSONString(joinPoint.getArgs());
        log.info("入参：{}", parameters);
        //ip地址
        String ipAddr = IpInfoUtil.getIpAddr(request);
        //获取浏览器信息
        String browser = IpInfoUtil.getBrowser(request);
        //获取系统名称
        String sysName = IpInfoUtil.getSystemName(request);
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        log.info("方法：{}", className + "." + methodName + "()");

//        String details = annotation.value();
        String details = SpELUtil.generateKeyBySpEL(annotation.value(), joinPoint);

        OperLog operLog = OperLog.builder()
                .username(username)
                .userType(userType)
                .requestMethod(requestMethod)
                .ipAddr(ipAddr)
                .browser(browser)
                .sysName(sysName)
                .parameters(parameters)
                .details(details)
                .method(className + "." + methodName + "()")
                .build();
        if (jsonResult != null){
            JSONObject jsonObject = JSONUtil.parseObj(jsonResult);
            ApiResponse response = JSONUtil.toBean(jsonObject, ApiResponse.class);
            operLog.setResultCode(response.getCode());
            operLog.setResultMessage(response.getMessage());
        }
        if (e != null) {
            operLog.setResultCode(FAIL);
            operLog.setResultMessage(e.getMessage());
        }
        sysLogService.saveOperLog(operLog);
    }
}
