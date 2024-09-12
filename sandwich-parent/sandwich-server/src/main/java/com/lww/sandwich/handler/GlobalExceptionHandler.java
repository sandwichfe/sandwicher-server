package com.lww.sandwich.handler;

import com.lww.constant.HttpStatusCodes;
import com.lww.response.ResponseResult;
import com.lww.sandwich.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.StringJoiner;

/**
 * 全局异常处理类
 * RestControllerAdvice 监管所有RestController
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * 处理自定义异常  处理特定异常
     */
    @ExceptionHandler(AppException.class)
    public ResponseResult handleBizException(AppException e) {
        log.error("异常信息：", e);
        ResponseResult<Object> result = new ResponseResult<>();
        result.setCode(e.getCode());
        result.setMsg(e.getMessage());
        return result;
    }

    /**
     * 参数校验不通过异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseResult handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("异常信息：", e);
        StringJoiner sj = new StringJoiner(";");
        e.getBindingResult().getFieldErrors().forEach(x -> sj.add(x.getDefaultMessage()));
        ResponseResult<Object> result = new ResponseResult<>();
        result.setCode(HttpStatusCodes.HTTP_VERSION_NOT_SUPPORTED);
        result.setMsg(sj.toString());
        return result;
    }

    /**
     * Controller参数绑定错误
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseResult handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error("异常信息：", e);
        ResponseResult<Object> result = new ResponseResult<>();
        result.setCode(HttpStatusCodes.VARIANT_ALSO_NEGOTIATES);
        result.setMsg(e.getMessage());
        return result;
    }

    /**
     * 其他未知异常(拦截的是全局最底层异常,兜底)
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseResult handleException(Exception e) {
        log.error("异常信息：", e);
        ResponseResult result = new ResponseResult();
        result.setCode(HttpStatusCodes.INTERNAL_SERVER_ERROR);
        result.setMsg("服务器内部错误");
        return result;
    }
}