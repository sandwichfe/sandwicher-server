package com.lww.auth.server.user.controller;

import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import com.lww.auth.server.user.entity.Authority;
import com.lww.auth.server.user.service.AuthorityService;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 系统菜单表 前端控制器
 * </p>
 *
 * @author lww
 * @since 2024-12-16 11:53:37
 */
@Tag(name = "菜单管理")
@RestController
@RequestMapping("/sys/authority")
public class AuthorityController {

    @Resource
    private AuthorityService authorityService;

    /**
     * 新增菜单
     */
    @Operation(summary = "新增菜单")
    @PostMapping
    public ResponseResult<Authority> createAuthority(@RequestBody Authority authority) {
        authorityService.save(authority);
        return ResultUtil.success(authority);
    }

    /**
     * 根据ID获取菜单
     */
    @Operation(summary = "根据ID获取菜单")
    @GetMapping("/{id}")
    public ResponseResult<Authority> getAuthorityById(@PathVariable Long id) {
        Authority authority = authorityService.getById(id);
        return ResultUtil.success(authority);
    }

    /**
     * 获取所有菜单
     */
    @Operation(summary = "获取所有菜单")
    @GetMapping
    public ResponseResult<List<Authority>> getAllAuthorities() {
        List<Authority> authorities = authorityService.list();
        return ResultUtil.success(authorities);
    }

    /**
     * 更新菜单
     */
    @Operation(summary = "更新菜单")
    @PutMapping
    public ResponseResult<Authority> updateAuthority(@RequestBody Authority authority) {
        authorityService.updateById(authority);
        return ResultUtil.success(authority);
    }

    /**
     * 删除菜单
     */
    @Operation(summary = "删除菜单")
    @DeleteMapping("/{id}")
    public ResponseResult<Void> deleteAuthority(@PathVariable Long id) {
        authorityService.removeById(id);
        return ResultUtil.success();
    }
}
