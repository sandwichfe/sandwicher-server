package com.lww.auth.server.user_center.controller.user;

import java.util.Map;

import com.lww.auth.server.user_center.entity.User;
import com.lww.auth.server.user_center.service.UserService;
import com.lww.common.web.log.Loggable;
import com.lww.common.web.response.ResponseCode;
import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
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
@RequestMapping("/api/user-center/user")
@RestController
public class UserLoginController {

    @Resource
    private UserService userService;

    @PostMapping(value = "/login")
    @Operation(summary = "用户登录", description = "用户登录")
    @Loggable(module = "login", type = "login", description = "用户登录")
    public ResponseResult<String> userLogin(String username, String password) {
        return ResultUtil.success(userService.userLogin(username, password));
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
}
