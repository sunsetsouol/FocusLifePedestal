package com.qgStudio.pedestal.handler;

import com.qgStudio.pedestal.entity.vo.Result;
import com.qgStudio.pedestal.entity.vo.ResultStatusEnum;
import com.qgStudio.pedestal.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2023/11/16
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * 处理预期以外的错误
     *
     * @param e 异常
     * @return 统一结果返回
     */
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        log.error(e.getMessage(), e);
        return Result.fail(ResultStatusEnum.UNKNOWN_ERROR);
    }



    /**
     * 参数为基本类型时，校验参数异常
     *
     * @param e 异常
     * @return 统一结果返回
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result handleConstraintViolationException(ConstraintViolationException e) {
        return Result.fail(ResultStatusEnum.PARAMS_ERROR, e.getMessage());
    }

    /**
     * 参数为对象时，校验参数异常
     *
     * @param e 异常
     * @return 统一结果返回
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return Result.fail(ResultStatusEnum.PARAMS_ERROR, e.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(ServiceException.class)
    public Result<String> handleServiceException(ServiceException e, HttpServletRequest request) {
        log.info("请求地址{}, 异常{}", request.getRequestURI(), e.getMessage());
        return Result.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public Result<String> handleInternalAuthenticationServiceException(InternalAuthenticationServiceException e, HttpServletRequest request) {
        log.info("请求地址{}, 异常{}", request.getRequestURI(), e.getMessage());
        return Result.fail(ResultStatusEnum.LOGIN_FAIL);
    }

}
