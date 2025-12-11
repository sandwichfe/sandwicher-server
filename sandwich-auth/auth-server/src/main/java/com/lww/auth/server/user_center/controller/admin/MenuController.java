package com.lww.auth.server.user_center.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lww.auth.server.user_center.entity.Menu;
import com.lww.auth.server.user_center.req.MenuReq;
import com.lww.auth.server.user_center.service.MenuService;
import com.lww.auth.server.user_center.vo.MenuTreeVO;
import com.lww.auth.server.user_center.vo.MenuVo;
import com.lww.common.utils.CustomBeanUtils;
import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import com.lww.common.web.vo.PageVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
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
    public ResponseResult<MenuVo> createMenu(@RequestBody MenuReq menuReq) {
        Menu menu = new Menu();
        BeanUtils.copyProperties(menuReq, menu);
        menu.setMenuPid(Optional.ofNullable(menu.getMenuPid()).orElse(0L));
        menuService.save(menu);
        MenuVo menuVo = new MenuVo();
        BeanUtils.copyProperties(menu, menuVo);
        return ResultUtil.success(menuVo);
    }

    @Operation(summary = "根据ID获取菜单")
    @GetMapping("/get/{id}")
    public ResponseResult<MenuVo> getMenuById(@PathVariable Long id) {
        Menu menu = menuService.getById(id);
        MenuVo menuVo = new MenuVo();
        BeanUtils.copyProperties(menu, menuVo);
        return ResultUtil.success(menuVo);
    }

    @Operation(summary = "获取所有菜单")
    @PostMapping("/list")
    public ResponseResult<IPage<MenuVo>> getAllAuthorities(@RequestBody PageVo pageVo) {
        Page<Menu> page = new Page<>(pageVo.getPageNum(), pageVo.getPageSize());
        Page<Menu> authorities = menuService.page(page);
        IPage<MenuVo> menuVos = authorities.convert(menu -> CustomBeanUtils.copyProperties(menu, MenuVo.class));
        return ResultUtil.success(menuVos);
    }

    @Operation(summary = "更新菜单")
    @PostMapping("/update")
    public ResponseResult<MenuVo> updateMenu(@RequestBody MenuReq menuReq) {
        Menu menu = new Menu();
        BeanUtils.copyProperties(menuReq, menu);
        menuService.updateById(menu);
        MenuVo menuVo = new MenuVo();
        BeanUtils.copyProperties(menu, menuVo);
        return ResultUtil.success(menuVo);
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