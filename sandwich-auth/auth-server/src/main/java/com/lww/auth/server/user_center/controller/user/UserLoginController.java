package com.lww.auth.server.user_center.controller.user;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.lww.auth.server.core.utils.AesUtil;
import com.lww.auth.server.user_center.entity.User;
import com.lww.auth.server.user_center.service.UserService;
import com.lww.auth.server.user_center.vo.Oauth2Param;
import com.lww.common.web.exception.AppException;
import com.lww.common.web.log.Loggable;
import com.lww.common.web.response.ResponseCode;
import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
@RequestMapping("/api/user-center/user")
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
    @Loggable(module = "login", type = "login", description = "用户登录")
    public ResponseResult<String> userLogin(String username, String password) {
        // 对前端传输的密码进行AES解密
        String decryptedPassword = AesUtil.decryptPassword(password);
        
        Oauth2Param oauth2Param = new Oauth2Param()
                .setGrantType("password")
                .setClientId("client_password")
                .setClientSecret("123456")
                .setUsername(username)
                .setPassword(decryptedPassword);
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

    /**
     * 通过密码模式获取OAuth2 token
     * @param oauth2Param OAuth2参数对象
     * @return 带Bearer前缀的token字符串
     * @throws AppException 获取token失败时抛出异常
     */
    private String getOauth2TokenByPassWord(Oauth2Param oauth2Param) {
        // OAuth token endpoint 请求的域名要和ResourceServer配置的issuer-uri一致，否则JWT会认证失败
        log.info("请求OAuth2 token，tokenUrl: {}, 参数: {}", tokenUrl, oauth2Param);

        // 使用try-with-resources确保HttpResponse资源自动关闭
        try (HttpResponse response = HttpRequest.post(tokenUrl)
                .form("grant_type", oauth2Param.getGrantType())
                .form("client_id", oauth2Param.getClientId())
                .form("client_secret", oauth2Param.getClientSecret())
                .form("username", oauth2Param.getUsername())
                .form("password", oauth2Param.getPassword())
                // 设置连接超时时间为30秒（30000毫秒）
                .setConnectionTimeout(30000)
                // 设置读取超时时间为60秒（60000毫秒）
                .setReadTimeout(60000).execute()) {

            // 检查响应状态码
            if (!response.isOk()) {
                String errorBody = response.body();
                log.error("请求OAuth2 token失败，HTTP状态码: {}, 响应体: {}", response.getStatus(), errorBody);
                throw new AppException("认证服务返回错误: " + errorBody);
            }

            // 获取返回的响应体
            String responseBody = response.body();
            log.debug("OAuth2 token响应: {}", responseBody);
            return "Bearer " + extractAccessToken(responseBody);
        }
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
                log.error("oauth2认证失败，响应:{}", responseBody);
                throw new AppException("账号或密码错误");
            }
            // 提取 access_token
            JSONObject data = (JSONObject) rootNode.get("data");
            return data.getString("access_token");
    }
}