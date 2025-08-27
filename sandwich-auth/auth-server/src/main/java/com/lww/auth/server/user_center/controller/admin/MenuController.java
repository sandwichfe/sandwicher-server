package com.lww.auth.server.user_center.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lww.auth.server.user_center.entity.Menu;
import com.lww.auth.server.user_center.service.MenuService;
import com.lww.auth.server.user_center.vo.MenuTreeVO;
import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import com.lww.common.web.vo.PageVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author sandw
 */
@Tag(name = "菜单管理")
@RestController
@RequestMapping("/api/sys/menu")
public class MenuController {

    @Resource
    private MenuService menuService;

    @Operation(summary = "新增菜单")
    @PostMapping("/create")
    public ResponseResult<Menu> createMenu(@RequestBody Menu menu) {
        menu.setMenuPid(Optional.ofNullable(menu.getMenuPid()).orElse(0L));
        menuService.save(menu);
        return ResultUtil.success(menu);
    }

    @Operation(summary = "根据ID获取菜单")
    @GetMapping("/get/{id}")
    public ResponseResult<Menu> getMenuById(@PathVariable Long id) {
        Menu menu = menuService.getById(id);
        return ResultUtil.success(menu);
    }

    @Operation(summary = "获取所有菜单")
    @PostMapping("/list")
    public ResponseResult<Page<Menu>> getAllAuthorities(@RequestBody PageVo pageVo) {
        Page<Menu> page = new Page<>(pageVo.getPageNum(), pageVo.getPageSize());
        Page<Menu> authorities = menuService.page(page);
        return ResultUtil.success(authorities);
    }

    @Operation(summary = "更新菜单")
    @PostMapping("/update")
    public ResponseResult<Menu> updateMenu(@RequestBody Menu menu) {
        menuService.updateById(menu);
        return ResultUtil.success(menu);
    }

    @Operation(summary = "删除菜单")
    @DeleteMapping("/delete/{id}")
    public ResponseResult<Void> deleteMenu(@PathVariable Long id) {
        menuService.removeById(id);
        return ResultUtil.success();
    }

    @Operation(summary = "获取菜单树")
    @GetMapping("/tree")
    public ResponseResult<List<MenuTreeVO>> getMenuTree() {
        return ResultUtil.success(menuService.getMenuTree());
    }


}