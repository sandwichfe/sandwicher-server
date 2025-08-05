package com.lww.auth.server.user.controller;

import com.lww.auth.server.user.service.UserService;
import com.lww.auth.server.user.vo.req.ChangePasswordRequest;
import com.lww.auth.server.utils.AuthUserUtils;
import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户API控制器
 * </p>
 *
 * @author lww
 * @since 2024-12-16
 */
@Tag(name = "用户API")
@RestController
@RequestMapping("/api/user")
public class UserApiController {

    @Resource
    private UserService userService;

    /**
     * 修改密码
     */
    @Operation(summary = "修改密码")
    @PostMapping("/change-password")
    public ResponseResult<String> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        // 验证确认密码
        if (!StringUtils.hasText(request.getConfirmPassword()) || 
            !request.getNewPassword().equals(request.getConfirmPassword())) {
            return ResultUtil.error("两次输入的密码不一致");
        }
        
        // 获取当前登录用户ID
        Long userId = AuthUserUtils.getCurrentUserId();
        
        try {
            // 调用服务层修改密码
            boolean success = userService.changePassword(userId, request.getOldPassword(), request.getNewPassword());
            
            if (success) {
                return ResultUtil.success("密码修改成功");
            } else {
                return ResultUtil.error("密码修改失败");
            }
        } catch (Exception e) {
            return ResultUtil.error(e.getMessage());
        }
    }
}