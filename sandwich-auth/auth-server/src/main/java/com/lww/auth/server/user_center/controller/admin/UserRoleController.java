package com.lww.auth.server.user_center.controller.admin;

import com.lww.auth.server.user_center.service.UserRoleService;
import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.lww.auth.server.user_center.vo.req.AssignRolesToUserRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lww
 * @since 2024-12-16 11:53:38
 */
@RestController
@RequestMapping("/api/sys/user-role")
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
