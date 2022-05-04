package com.arslinthboot;

import com.arslinthboot.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.arslinthboot.common.ResponseCode.ACCESS_NOT;
import static com.arslinthboot.common.ResponseCode.FAIL;

/**
 * @author Arslinth
 * @ClassName GlobalExceptionHandler
 * @Description 全局异常处理器
 * @Date 2022/5/5
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理 Validator 框架实体类验证的错误返回
     */
    @ExceptionHandler({BindException.class})
    public ApiResponse BindingResult(BindingResult result) {
        List<ObjectError> errors = result.getAllErrors();
        String errMsg = errors.stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining("，"));
        return ApiResponse.code(FAIL).message(errMsg);
    }

    /**
     * 权限校验异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ApiResponse handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',权限校验失败'{}'", requestURI, e.getMessage());
        return ApiResponse.code(ACCESS_NOT).message("没有权限，请联系管理员授权");
    }
}
