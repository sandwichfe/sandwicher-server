package com.lww.auth.server.user_center.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lww.auth.server.user_center.entity.Menu;
import com.lww.auth.server.user_center.req.MenuReq;
import com.lww.auth.server.user_center.vo.MenuTreeVO;
import com.lww.auth.server.user_center.vo.MenuVo;
import com.lww.common.web.vo.PageVo;

import java.util.List;

/**
 * <p>
 * 系统菜单表 服务类
 * </p>
 *
 * @author lww
 * @since 2024-12-16 11:53:37
 */
public interface MenuService extends IService<Menu> {

    MenuVo createMenu(MenuReq menuReq);

    MenuVo getMenuById(Long id);

    IPage<MenuVo> listMenu(PageVo pageVo);

    MenuVo updateMenu(MenuReq menuReq);

    void deleteMenu(Long id);

    /**
     * 获取菜单树
     *
     * @return 菜单树结构
     */
    List<MenuTreeVO> getMenuTree();

    /**
     * 获取当前用户的菜单树
     *
     * @param userId 当前用户ID
     * @return 菜单树结构
     */
    List<MenuTreeVO> getCurrentUserMenuTree(Long userId);

}
