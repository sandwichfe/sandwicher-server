package com.lww.auth.server.portal.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lww.auth.server.portal.config.api.ApiPortalRestController;
import com.lww.auth.server.portal.req.MenuReq;
import com.lww.auth.server.portal.service.MenuService;
import com.lww.auth.server.portal.vo.MenuTreeVO;
import com.lww.auth.server.portal.vo.MenuVo;
import com.lww.common.web.log.Loggable;
import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import com.lww.common.web.vo.PageVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author sandw
 */
@Tag(name = "菜单管理")
@ApiPortalRestController
@RequestMapping("/sys/menu")
public class MenuController {

    @Resource
    private MenuService menuService;

    @Operation(summary = "新增菜单")
    @Loggable(module = "menu", type = "create", description = "新增菜单", logResult = false)
    @PostMapping("/create")
    public ResponseResult<MenuVo> createMenu(@RequestBody MenuReq menuReq) {
        return ResultUtil.success(menuService.createMenu(menuReq));
    }

    @Operation(summary = "根据ID获取菜单")
    @GetMapping("/get/{id}")
    public ResponseResult<MenuVo> getMenuById(@PathVariable Long id) {
        return ResultUtil.success(menuService.getMenuById(id));
    }

    @Operation(summary = "获取所有菜单")
    @PostMapping("/list")
    public ResponseResult<IPage<MenuVo>> getAllAuthorities(@RequestBody PageVo pageVo) {
        return ResultUtil.success(menuService.listMenu(pageVo));
    }

    @Operation(summary = "更新菜单")
    @Loggable(module = "menu", type = "update", description = "更新菜单", logResult = false)
    @PostMapping("/update")
    public ResponseResult<MenuVo> updateMenu(@RequestBody MenuReq menuReq) {
        return ResultUtil.success(menuService.updateMenu(menuReq));
    }

    @Operation(summary = "删除菜单")
    @Loggable(module = "menu", type = "delete", description = "删除菜单ID: #id", logParams = false)
    @DeleteMapping("/delete/{id}")
    public ResponseResult<Void> deleteMenu(@PathVariable Long id) {
        menuService.deleteMenu(id);
        return ResultUtil.success();
    }

    @Operation(summary = "获取菜单树")
    @GetMapping("/tree")
    public ResponseResult<List<MenuTreeVO>> getMenuTree() {
        return ResultUtil.success(menuService.getMenuTree());
    }


}
