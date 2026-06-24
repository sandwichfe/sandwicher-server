package com.lww.auth.server.user_center.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lww.auth.server.user_center.req.RoleReq;
import com.lww.auth.server.user_center.service.RoleService;
import com.lww.auth.server.user_center.vo.RoleVo;
import com.lww.common.web.log.Loggable;
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

/**
 * @author lww
 */
@Tag(name = "角色管理")
@RestController
@RequestMapping("/api/sys/role")
public class RoleController {

    @Resource
    private RoleService roleService;

    @Operation(summary = "新增角色")
    @Loggable(module = "role", type = "create", description = "新增角色", logResult = false)
    @PostMapping("/create")
    public ResponseResult<RoleVo> createRole(@RequestBody RoleReq roleReq) {
        return ResultUtil.success(roleService.createRole(roleReq));
    }

    @Operation(summary = "根据ID获取角色")
    @GetMapping("/get/{id}")
    public ResponseResult<RoleVo> getRoleById(@PathVariable Long id) {
        return ResultUtil.success(roleService.getRoleById(id));
    }

    @Operation(summary = "获取所有角色")
    @PostMapping("/list")
    public ResponseResult<IPage<RoleVo>> getAllRoles(@RequestBody PageVo pageVo) {
        return ResultUtil.success(roleService.listRole(pageVo));
    }

    @Operation(summary = "更新角色")
    @Loggable(module = "role", type = "update", description = "更新角色", logResult = false)
    @PostMapping("/update")
    public ResponseResult<RoleVo> updateRole(@RequestBody RoleReq roleReq) {
        return ResultUtil.success(roleService.updateRole(roleReq));
    }

    @Operation(summary = "删除角色")
    @Loggable(module = "role", type = "delete", description = "删除角色ID: #id", logParams = false)
    @DeleteMapping("/delete/{id}")
    public ResponseResult<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResultUtil.success();
    }
}
