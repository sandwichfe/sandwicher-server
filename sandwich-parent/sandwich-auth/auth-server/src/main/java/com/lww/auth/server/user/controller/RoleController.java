package com.lww.auth.server.user.controller;

import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import com.lww.auth.server.user.entity.Role;
import com.lww.auth.server.user.service.RoleService;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 系统角色表 前端控制器
 * </p>
 *
 * @author lww
 * @since 2024-12-16 11:53:37
 */
@Tag(name = "角色管理")
@RestController
@RequestMapping("/sys/role")
public class RoleController {

    @Resource
    private RoleService roleService;

    /**
     * 新增角色
     */
    @Operation(summary = "新增角色")
    @PostMapping
    public ResponseResult<Role> createRole(@RequestBody Role role) {
        roleService.save(role);
        return ResultUtil.success(role);
    }

    /**
     * 根据ID获取角色
     */
    @Operation(summary = "根据ID获取角色")
    @GetMapping("/{id}")
    public ResponseResult<Role> getRoleById(@PathVariable Long id) {
        Role role = roleService.getById(id);
        return ResultUtil.success(role);
    }

    /**
     * 获取所有角色
     */
    @Operation(summary = "获取所有角色")
    @GetMapping
    public ResponseResult<List<Role>> getAllRoles() {
        List<Role> roles = roleService.list();
        return ResultUtil.success(roles);
    }

    /**
     * 更新角色
     */
    @Operation(summary = "更新角色")
    @PutMapping
    public ResponseResult<Role> updateRole(@RequestBody Role role) {
        roleService.updateById(role);
        return ResultUtil.success(role);
    }

    /**
     * 删除角色
     */
    @Operation(summary = "删除角色")
    @DeleteMapping("/{id}")
    public ResponseResult<Void> deleteRole(@PathVariable Long id) {
        roleService.removeById(id);
        return ResultUtil.success();
    }
}
