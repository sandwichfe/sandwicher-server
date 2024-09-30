package com.lww.common.web.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author lww
 *   返回结果实体
 * @date 2022/3/11 16:28
 */
@Schema(description = "返回结果实体")
public class ResponseResult<T> {

    @Schema(description = "响应码")
    public int code;

    @Schema(description = "响应消息")
    private String msg;

    @Schema(description = "响应数据")
    private T data;

    @Schema(description = "响应时间")
    private String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

    public ResponseResult<T> setCode(ResponseCode retCode) {
        this.code = retCode.code;
        return this;
    }

    public int getCode() {
        return code;
    }

    public ResponseResult<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ResponseResult<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return data;
    }

    public ResponseResult<T> setData(T data) {
        this.data = data;
        return this;
    }

    public String getTime() {
        return time;
    }


}
