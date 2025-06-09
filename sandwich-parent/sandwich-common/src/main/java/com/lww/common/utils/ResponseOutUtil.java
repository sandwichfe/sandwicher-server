package com.lww.common.utils;

import com.alibaba.fastjson.JSON;
import com.lww.common.web.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *    使用response输出JSON
 * @author lww
 * @since 2022/7/20 16:58
 */
@Slf4j
public class ResponseOutUtil {

    private ResponseOutUtil(){
    }

    /**
     *  使用response输出JSON
     * @param response response
     * @param resultMap resultMap
     */
    public static void out(HttpServletResponse response, ResponseResult<Object> resultMap){

        ServletOutputStream out = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            out = response.getOutputStream();
            out.write(JSON.toJSONBytes(resultMap));
        } catch (Exception e) {
            log.error(e + "输出JSON出错");
        } finally{
            if(out!=null){
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    log.info("error",e);
                }
            }
        }
    }
}