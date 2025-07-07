package com.lww.auth.server.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lww.auth.server.user.entity.RoleMenu;

import java.util.List;

/**
 * <p>
 * 角色菜单多对多关联表 服务类
 * </p>
 *
 * @author lww
 * @since 2024-12-16 11:53:37
 */
public interface RoleMenuService extends IService<RoleMenu> {

    /**
     * 根据角色ID获取菜单ID列表
     *
     * @param roleId 角色ID
     * @return 菜单ID列表
     */
    List<Long> getMenuIdsByRoleId(Long roleId);

    /**
     * 为角色分配菜单
     *
     * @param roleId  角色ID
     * @param menuIds 菜单ID列表
     */
    void assignMenusToRole(Long roleId, List<Long> menuIds);
}
