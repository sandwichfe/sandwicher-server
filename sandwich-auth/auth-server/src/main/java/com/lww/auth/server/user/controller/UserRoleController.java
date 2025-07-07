package com.lww.auth.server.user.controller;

import com.lww.auth.server.user.service.UserRoleService;
import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.lww.auth.server.user.vo.req.AssignRolesToUserRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lww
 * @since 2024-12-16 11:53:38
 */
@RestController
@RequestMapping("/sys/user-role")
@RequiredArgsConstructor
public class UserRoleController {

    private final UserRoleService userRoleService;

    @GetMapping("/getRolesByUserId/{userId}")
    public ResponseResult<List<Long>> getRolesByUserId(@PathVariable Long userId) {
        return ResultUtil.success(userRoleService.getRoleIdsByUserId(userId));
    }

    @PostMapping("/assignRolesToUser")
    public ResponseResult<Void> assignRolesToUser(@RequestBody AssignRolesToUserRequest request) {
        userRoleService.assignRolesToUser(request.getUserId(), request.getRoleIds());
        return ResultUtil.success();
    }
}
