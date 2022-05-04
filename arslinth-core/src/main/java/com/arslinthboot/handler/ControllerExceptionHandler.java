package com.arslinthboot.handler;

import cn.hutool.core.collection.CollUtil;
import com.arslinthboot.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.BindingResultUtils;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.arslinthboot.common.ResponseCode.FAIL;

/**
 * @author Arslinth
 * @ClassName ControllerExceptionHandler
 * @Description 全局异常捕获处理
 * @Date 2022/5/4
 */
@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class ControllerExceptionHandler {

    /**
     * 处理 Validator 框架实体类验证的错误返回
     */
    @ExceptionHandler({BindException.class})
    public ApiResponse BindingResult(BindingResult result) {
        List<ObjectError> errors = result.getAllErrors();
        String errMsg = errors.stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining("，"));
        return ApiResponse.code(FAIL).data("errors", errMsg);
    }

}
