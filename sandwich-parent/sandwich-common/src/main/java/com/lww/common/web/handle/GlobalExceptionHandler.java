package com.lww.common.web.handle;

import cn.hutool.core.collection.CollectionUtil;
import com.lww.common.web.exception.AppException;
import com.lww.common.web.response.ResponseCode;
import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

/**
 * 全局异常处理类
 * RestControllerAdvice 监管所有RestController
 * @author sandw
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * 处理自定义异常  处理特定异常
     */
    @ExceptionHandler(AppException.class)
    public ResponseResult<Void> handleBizException(AppException e) {
        log.error("异常信息：", e);
        ResponseResult<Void> result = new ResponseResult<>();
        result.setCode(e.getCode());
        result.setMsg(e.getMessage());
        return result;
    }

    /**
     * 处理 json 请求体调用接口对象参数校验失败抛出的异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseResult<Void> jsonParamsException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        List errorList = CollectionUtil.newArrayList();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            String msg = String.format("%s%s；", fieldError.getField(), fieldError.getDefaultMessage());
            errorList.add(msg);
        }
        return ResultUtil.error(ResponseCode.FAILURE,errorList.toString());
    }


    /**
     * 处理单个参数校验失败抛出的异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseResult<Void> paramsException(ConstraintViolationException e) {

        List<String> errorList = CollectionUtil.newArrayList();
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            errorList.add(violation.getMessageTemplate());
        }
        return ResultUtil.error(ResponseCode.FAILURE,errorList.toString());
    }

    /**
     * @param e
     * @return 处理 form data方式调用接口对象参数校验失败抛出的异常
     */
    @ExceptionHandler(BindException.class)
    public ResponseResult<Void> formDaraParamsException(BindException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<String> collect = fieldErrors.stream()
                .map(o -> o.getField() +":"+ o.getDefaultMessage())
                .toList();
        return ResultUtil.error(ResponseCode.FAILURE,collect.toString());
    }

    /**
     * 请求方法不被允许异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseResult<Void> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return ResultUtil.error(ResponseCode.FAILURE,e.getMessage());
    }

    /**
     * @param e
     * @return Content-Type/Accept 异常
     * application/json
     * application/x-www-form-urlencoded
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseResult<Void> httpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        return ResultUtil.error(ResponseCode.FAILURE,e.getMessage());
    }

    /**
     * handlerMapping  接口不存在抛出异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseResult<Void> noHandlerFoundException(NoHandlerFoundException e) {
        return ResultUtil.error(ResponseCode.FAILURE,e.getMessage());
    }

    /**
     * 其他未知异常(拦截的是全局最底层异常,兜底)
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseResult<Void> handleException(Exception e) {
        log.error("异常信息：", e);
        return ResultUtil.error(ResponseCode.FAILURE,"服务器内部错误");
    }
}