package com.lww.auth.server.user.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.lww.auth.server.user.entity.User;
import com.lww.auth.server.user.service.UserService;
import com.lww.auth.server.user.vo.Oauth2Param;
import com.lww.common.web.exception.AppException;
import com.lww.common.web.response.ResponseCode;
import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
public class UserLoginController {

    @Resource
    private UserService userService;

    @Value("${oauth2.token-url:http://127.0.0.1:9000/oauth2/token}")
    private String tokenUrl;

    // 用于存储滑块验证码的缓存
    private Map<String, Integer> sliderCache = new HashMap<>();

    @PostMapping(value = "/login")
    @Operation(summary = "用户登录", description = "用户登录")
    public ResponseResult<String> userLogin(String username, String password) {
        Oauth2Param oauth2Param = new Oauth2Param()
                .setGrantType("password")
                .setClientId("client_password")
                .setClientSecret("123456")
                .setUsername(username)
                .setPassword(password);
        String token = getOauth2TokenByPassWord(oauth2Param);

        return ResultUtil.success(token);
    }

    @GetMapping(value = "/slider/generate")
    @Operation(summary = "生成滑块验证码", description = "生成滑块验证码")
    public ResponseResult<Map<String, Object>> generateSlider() {
        Random random = new Random();
        // 生成滑块的目标位置
        int targetX = random.nextInt(200);
        // 生成唯一的滑块ID
        String sliderId = IdWorker.getIdStr();

        // 将滑块ID和目标位置存入缓存
        sliderCache.put(sliderId, targetX);

        Map<String, Object> result = new HashMap<>();
        result.put("sliderId", sliderId);
        result.put("targetX", targetX);

        return ResultUtil.success(result);
    }

    @PostMapping(value = "/slider/verify")
    @Operation(summary = "验证滑块位置", description = "验证滑块位置")
    public ResponseResult<Boolean> verifySlider(String sliderId, int userX) {
        Integer targetX = sliderCache.get(sliderId);
        if (targetX == null) {
            throw new AppException("滑块验证码已过期或无效");
        }

        // 允许一定的误差范围
        boolean isValid = Math.abs(userX - targetX) <= 5;

        // 验证完成后移除缓存
        sliderCache.remove(sliderId);

        isValid = true;
        return ResultUtil.success(isValid);
    }


    /**
     * 新增用户
     */
    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public ResponseResult<Void> register(@RequestBody User user) {
        userService.registerUser(user);
        return ResultUtil.response(ResponseCode.SUCCESS,"注册成功！");
    }

    private String getOauth2TokenByPassWord(Oauth2Param oauth2Param) {
        // OAuth token endpoint  请求的域名 要和ResourceServer配置的issuer-uri 一致 不然jwt会认证失败
        // 通过 Hu tool HttpRequest 构建请求体和请求头
        log.info("tokenUrl:{}", tokenUrl);
        log.info("oauth2Param:{}", oauth2Param);
        HttpResponse response = HttpRequest.post(tokenUrl)
                .form("grant_type", oauth2Param.getGrantType())
                .form("client_id", oauth2Param.getClientId())
                .form("client_secret", oauth2Param.getClientSecret())
                .form("username", oauth2Param.getUsername())
                .form("password", oauth2Param.getPassword())
                .execute();
        // 获取返回的响应体
        String responseBody = response.body();
        String token = extractAccessToken(responseBody);
        if (!StringUtils.hasText(token)) {
            throw new AppException("登录失败");
        }
        return "Bearer "+token;
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
            // 解析根级 JSON
            JSONObject rootNode = JSON.parseObject(responseBody);
            if (HttpStatus.HTTP_OK!=rootNode.getInteger("code")){
                throw new AppException(rootNode.getString("msg"));
            }
            // 提取 access_token
            JSONObject data = (JSONObject) rootNode.get("data");
            return data.getString("access_token");
    }
}