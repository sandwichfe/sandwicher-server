package com.lww.auth.server.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lww.auth.server.user.entity.Menu;
import com.lww.auth.server.user.vo.MenuTreeVO;

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
