package com.lww.auth.server.controller.user;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 注意：本来这个服务只是用来做 授权认证的
 * 但是我也不想专门再起一个服务作为client来写 登录 接口  再来调用这个授权认证服务   就直接写在一起了。
 * <p>
 * 用户相关controller
 *
 * @author lww
 * @since 2024/12/9
 */
@Slf4j
@RequestMapping("/user")
@RestController
public class UserController {

    @PostMapping(value = "/login")
    @Operation(summary = "用户登录", description = "用户登录")
    public ResponseResult collectTag(String username, String password) {
        // OAuth token endpoint  请求的域名 要和ResourceServer配置的issuer-uri 一致 不然jwt会认证失败
        String tokenUrl = "http://localhost:9000/oauth2/token";
        // 通过 Hu tool HttpRequest 构建请求体和请求头
        HttpResponse response = HttpRequest.post(tokenUrl)
                .form("grant_type", "password")
                .form("client_id", "client_password")
                .form("client_secret", "123456")
                .form("username", username)
                .form("password", password)
                .execute();
        // 获取返回的响应体
        String responseBody = response.body();
        String token = extractAccessToken(responseBody);
        return ResultUtil.success(token);
    }

    /**
     * 获取 access_token
     *
     * @param responseBody
     * @return
     * @author lww
     * @since 2024/12/9
     */
    public static String extractAccessToken(String responseBody) {
        log.info("oauth token responseBody:{}", responseBody);
        try {
            // 解析根级 JSON
            JSONObject rootNode = JSON.parseObject(responseBody);
            // 提取 access_token
            return "Bearer "+rootNode.getString("access_token");
        } catch (Exception e) {
            log.error("login error",e);
            return null;
        }
    }


}
