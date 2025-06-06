package com.lww.auth.server.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lww.auth.server.user.entity.Role;
import com.lww.auth.server.user.service.RoleService;
import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import com.lww.common.web.vo.PageVo;
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

@Tag(name = "角色管理")
@RestController
@RequestMapping("/sys/role")
public class RoleController {

    @Resource
    private RoleService roleService;

    @Operation(summary = "新增角色")
    @PostMapping("/create")
    public ResponseResult<Role> createRole(@RequestBody Role role) {
        roleService.save(role);
        return ResultUtil.success(role);
    }

    @Operation(summary = "根据ID获取角色")
    @GetMapping("/get/{id}")
    public ResponseResult<Role> getRoleById(@PathVariable Long id) {
        Role role = roleService.getById(id);
        return ResultUtil.success(role);
    }

    @Operation(summary = "获取所有角色")
    @PostMapping("/list")
    public ResponseResult<Page<Role>> getAllRoles(@RequestBody PageVo pageVo) {
        Page<Role> page = new Page<>(pageVo.getPageNum(), pageVo.getPageSize());
        Page<Role> roles = roleService.page(page);
        return ResultUtil.success(roles);
    }

    @Operation(summary = "更新角色")
    @PostMapping("/update")
    public ResponseResult<Role> updateRole(@RequestBody Role role) {
        roleService.updateById(role);
        return ResultUtil.success(role);
    }

    @Operation(summary = "删除角色")
    @DeleteMapping("/delete/{id}")
    public ResponseResult<Void> deleteRole(@PathVariable Long id) {
        roleService.removeById(id);
        return ResultUtil.success();
    }
}