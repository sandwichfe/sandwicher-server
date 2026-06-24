package com.lww.auth.server.portal.controller.admin;

import com.lww.auth.server.portal.config.api.ApiPortalRestController;
import com.lww.auth.server.portal.service.UserRoleService;
import com.lww.common.web.log.Loggable;
import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.lww.auth.server.portal.vo.req.AssignRolesToUserRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lww
 * @since 2024-12-16 11:53:38
 */
@ApiPortalRestController
@RequestMapping("/sys/user-role")
@RequiredArgsConstructor
public class UserRoleController {

    private final UserRoleService userRoleService;

    @GetMapping("/getRolesByUserId/{userId}")
    public ResponseResult<List<Long>> getRolesByUserId(@PathVariable Long userId) {
        return ResultUtil.success(userRoleService.getRoleIdsByUserId(userId));
    }

    @PostMapping("/assignRolesToUser")
    @Loggable(module = "userRole", type = "assign", description = "为用户分配角色 userId: #request.userId", logResult = false)
    public ResponseResult<Void> assignRolesToUser(@RequestBody AssignRolesToUserRequest request) {
        userRoleService.assignRolesToUser(request);
        return ResultUtil.success();
    }
}
