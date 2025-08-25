package com.lww.auth.server.user.controller.sys;

import com.lww.auth.server.user.service.RoleMenuService;
import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.lww.auth.server.user.vo.req.AssignMenusToRoleRequest;

/**
 * <p>
 * 角色菜单多对多关联表 前端控制器
 * </p>
 *
 * @author lww
 * @since 2024-12-16 11:53:37
 */
@RestController
@RequestMapping("/api/sys/role-menu")
@RequiredArgsConstructor
public class RoleMenuController {

    private final RoleMenuService roleMenuService;

    @GetMapping("/getMenusByRoleId/{roleId}")
    public ResponseResult<List<Long>> getMenusByRoleId(@PathVariable Long roleId) {
        return ResultUtil.success(roleMenuService.getMenuIdsByRoleId(roleId));
    }

    @PostMapping("/assignMenusToRole")
    public ResponseResult<Void> assignMenusToRole(@RequestBody AssignMenusToRoleRequest request) {
        roleMenuService.assignMenusToRole(request.getRoleId(), request.getMenuIds());
        return ResultUtil.success();
    }
}
