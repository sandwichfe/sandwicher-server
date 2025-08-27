package com.lww.auth.server.user_center.controller.user;

import java.util.List;

import com.lww.auth.server.user_center.entity.User;
import com.lww.auth.server.user_center.service.MenuService;
import com.lww.auth.server.user_center.service.UserService;
import com.lww.auth.server.user_center.vo.MenuTreeVO;
import com.lww.auth.server.user_center.vo.req.ChangePasswordRequest;
import com.lww.auth.server.core.utils.AuthUserUtils;
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
@RequestMapping("/api/user-center/user")
public class UserApiController {

    @Resource
    private UserService userService;

    @Resource
    private MenuService menuService;

    /**
     * 获取当前登录用户信息
     */
    @Operation(summary = "获取当前登录用户信息")
    @GetMapping("/getCurrentUser")
    public ResponseResult<User> getCurrentUser() {
        Long userId = AuthUserUtils.getCurrentUserId();
        User user = userService.getById(userId);
        return ResultUtil.success(user);
    }


    /**
     * 更新当前登录用户信息
     */
    @Operation(summary = "更新当前登录用户信息")
    @PostMapping("/updateCurrentUser")
    public ResponseResult<User> updateCurrentUser(@RequestBody User user) {
        Long userId = AuthUserUtils.getCurrentUserId();
        user.setId(userId);
        userService.updateById(user);
        return ResultUtil.success(user);
    }

    @Operation(summary = "获取当前用户的菜单")
    @GetMapping("/currentUserMenu")
    public ResponseResult<List<MenuTreeVO>> getCurrentUserMenu() {
        // 获取当前用户ID
        Long userId = AuthUserUtils.getCurrentUserId();

        // 调用服务层获取当前用户的菜单树
        List<MenuTreeVO> menuTree = menuService.getCurrentUserMenuTree(userId);

        return ResultUtil.success(menuTree);
    }


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