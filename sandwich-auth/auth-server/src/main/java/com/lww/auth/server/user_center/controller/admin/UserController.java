package com.lww.auth.server.user_center.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lww.auth.server.user_center.entity.User;
import com.lww.auth.server.user_center.req.UserReq;
import com.lww.auth.server.user_center.service.UserService;
import com.lww.auth.server.user_center.vo.UserPageQuery;
import com.lww.auth.server.user_center.vo.UserVo;
import com.lww.common.utils.CustomBeanUtils;
import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
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
    @PostMapping("/create")
    public ResponseResult<UserVo> createUser(@RequestBody UserReq userReq) {
        User user = new User();
        BeanUtils.copyProperties(userReq, user);
        userService.createUser(user);
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        return ResultUtil.success(userVo);
    }

    /**
     * 根据ID获取用户
     */
    @Operation(summary = "根据ID获取用户")
    @GetMapping("/get/{id}")
    public ResponseResult<UserVo> getUserById(@PathVariable Long id) {
        User user = userService.getUserWithDepts(id);
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        return ResultUtil.success(userVo);
    }

    /**
     * 获取所有用户（分页）
     */
    @Operation(summary = "获取所有用户（分页）")
    @PostMapping("/list")
    public ResponseResult<IPage<UserVo>> getAllUsers(@RequestBody UserPageQuery pageVo) {
        Page<User> page = new Page<>(pageVo.getPageNum(), pageVo.getPageSize());
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (pageVo.getDeptId() != null) {
            // 关联查询，筛选出属于该部门的用户
            wrapper.inSql(User::getId, "select user_id from t_user_dept where dept_id = " + pageVo.getDeptId());
        }
        Page<User> users = userService.page(page, wrapper);
        IPage<UserVo> userVos = users.convert(user -> CustomBeanUtils.copyProperties(user, UserVo.class));
        return ResultUtil.success(userVos);
    }

    /**
     * 更新用户
     */
    @Operation(summary = "更新用户")
    @PostMapping("/update")
    public ResponseResult<UserVo> updateUser(@RequestBody UserReq userReq) {
        User user = new User();
        BeanUtils.copyProperties(userReq, user);
        userService.updateUser(user);
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        return ResultUtil.success(userVo);
    }


    /**
     * 删除用户
     */
    @Operation(summary = "删除用户")
    @DeleteMapping("/delete/{id}")
    public ResponseResult<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResultUtil.success();
    }


}
