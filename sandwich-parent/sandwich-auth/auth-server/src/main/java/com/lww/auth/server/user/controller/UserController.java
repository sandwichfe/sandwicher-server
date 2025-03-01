package com.lww.auth.server.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lww.auth.dep.resources.server.config.utils.SecurityUserUtils;
import com.lww.auth.server.user.entity.User;
import com.lww.auth.server.user.service.UserService;
import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import com.lww.common.web.vo.PageVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 基础用户信息表 前端控制器
 * </p>
 *
 * @author lww
 * @since 2024-12-16 11:53:38
 */
@Tag(name = "用户管理")
@RestController
@RequestMapping("/sys/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 新增用户
     */
    @Operation(summary = "新增用户")
    @PostMapping("/create")
    public ResponseResult<User> createUser(@RequestBody User user) {
        userService.save(user);
        return ResultUtil.success(user);
    }

    /**
     * 根据ID获取用户
     */
    @Operation(summary = "根据ID获取用户")
    @GetMapping("/get/{id}")
    public ResponseResult<User> getUserById(@PathVariable Long id) {
        User user = userService.getById(id);
        return ResultUtil.success(user);
    }

    /**
     * 获取所有用户（分页）
     */
    @Operation(summary = "获取所有用户（分页）")
    @PostMapping("/list")
    public ResponseResult<Page<User>> getAllUsers(@RequestBody PageVo pageVo) {
        Long userId = SecurityUserUtils.getUserId();
        Page<User> page = new Page<>(pageVo.getPageNum(), pageVo.getPageSize());
        Page<User> users = userService.page(page);
        return ResultUtil.success(users);
    }

    /**
     * 更新用户
     */
    @Operation(summary = "更新用户")
    @PostMapping("/update")
    public ResponseResult<User> updateUser(@RequestBody User user) {
        userService.updateById(user);
        return ResultUtil.success(user);
    }

    /**
     * 删除用户
     */
    @Operation(summary = "删除用户")
    @DeleteMapping("/delete/{id}")
    public ResponseResult<Void> deleteUser(@PathVariable Long id) {
        userService.removeById(id);
        return ResultUtil.success();
    }
}