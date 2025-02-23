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
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lww.common.web.vo.PageVo;

@Tag(name = "菜单管理")
@RestController
@RequestMapping("/sys/authority")
public class AuthorityController {

    @Resource
    private AuthorityService authorityService;

    @Operation(summary = "新增菜单")
    @PostMapping("/create")
    public ResponseResult<Authority> createAuthority(@RequestBody Authority authority) {
        authorityService.save(authority);
        return ResultUtil.success(authority);
    }

    @Operation(summary = "根据ID获取菜单")
    @GetMapping("/get/{id}")
    public ResponseResult<Authority> getAuthorityById(@PathVariable Long id) {
        Authority authority = authorityService.getById(id);
        return ResultUtil.success(authority);
    }

    @Operation(summary = "获取所有菜单")
    @PostMapping("/list")
    public ResponseResult<Page<Authority>> getAllAuthorities(@RequestBody PageVo pageVo) {
        Page<Authority> page = new Page<>(pageVo.getPageNum(), pageVo.getPageSize());
        Page<Authority> authorities = authorityService.page(page);
        return ResultUtil.success(authorities);
    }

    @Operation(summary = "更新菜单")
    @PostMapping("/update")
    public ResponseResult<Authority> updateAuthority(@RequestBody Authority authority) {
        authorityService.updateById(authority);
        return ResultUtil.success(authority);
    }

    @Operation(summary = "删除菜单")
    @DeleteMapping("/delete/{id}")
    public ResponseResult<Void> deleteAuthority(@PathVariable Long id) {
        authorityService.removeById(id);
        return ResultUtil.success();
    }
}