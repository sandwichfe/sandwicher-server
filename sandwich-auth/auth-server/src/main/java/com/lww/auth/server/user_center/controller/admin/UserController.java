package com.lww.auth.server.user_center.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lww.auth.server.user_center.req.UserReq;
import com.lww.auth.server.user_center.service.UserService;
import com.lww.auth.server.user_center.vo.UserPageQuery;
import com.lww.auth.server.user_center.vo.UserVo;
import com.lww.common.web.log.Loggable;
import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/api/sys/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 新增用户
     */
    @Operation(summary = "新增用户")
    @Loggable(module = "user", type = "create", description = "新增用户", logResult = false)
    @PostMapping("/create")
    public ResponseResult<UserVo> createUser(@RequestBody UserReq userReq) {
        return ResultUtil.success(userService.createUser(userReq));
    }

    /**
     * 根据ID获取用户
     */
    @Operation(summary = "根据ID获取用户")
    @GetMapping("/get/{id}")
    public ResponseResult<UserVo> getUserById(@PathVariable Long id) {
        return ResultUtil.success(userService.getUserVoById(id));
    }

    /**
     * 获取所有用户（分页）
     */
    @Operation(summary = "获取所有用户（分页）")
    @PostMapping("/list")
    public ResponseResult<IPage<UserVo>> getAllUsers(@RequestBody UserPageQuery pageVo) {
        return ResultUtil.success(userService.listUser(pageVo));
    }

    /**
     * 更新用户
     */
    @Operation(summary = "更新用户")
    @Loggable(module = "user", type = "update", description = "更新用户", logResult = false)
    @PostMapping("/update")
    public ResponseResult<UserVo> updateUser(@RequestBody UserReq userReq) {
        return ResultUtil.success(userService.updateUser(userReq));
    }


    /**
     * 删除用户
     */
    @Operation(summary = "删除用户")
    @Loggable(module = "user", type = "delete", description = "删除用户ID: #id", logParams = false)
    @DeleteMapping("/delete/{id}")
    public ResponseResult<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResultUtil.success();
    }


}
