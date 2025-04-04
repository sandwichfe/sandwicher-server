package com.lww.auth.server.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lww.auth.server.user.entity.Menu;
import com.lww.auth.server.user.mapper.MenuMapper;
import com.lww.auth.server.user.service.MenuService;
import com.lww.auth.server.user.vo.MenuTreeVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sandw
 */
@RequiredArgsConstructor
@Service
public class AuthorityServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    private final MenuMapper menuMapper;

    @Override
    public List<MenuTreeVO> getMenuTree() {
        List<Menu> menus = this.list();
        return buildMenuTree(menus, 0L);
    }

    @Override
    public List<MenuTreeVO> getCurrentUserMenuTree(Long userId) {
        // 根据用户ID查询其权限范围内的菜单列表
        List<Menu> userMenus = menuMapper.selectMenusByUserId(userId);
        // 构建菜单树
        return buildMenuTree(userMenus, 0L);
    }


    private List<MenuTreeVO> buildMenuTree(List<Menu> menus, Long parentId) {
        return menus.stream()
            .filter(menu -> menu.getMenuPid().equals(parentId))
            .map(menu -> {
                MenuTreeVO menuTreeVO = new MenuTreeVO();
                menuTreeVO.setId(menu.getId());
                menuTreeVO.setParentId(menu.getMenuPid());
                menuTreeVO.setTitle(menu.getName());
                menuTreeVO.setSort(menu.getSort());
                menuTreeVO.setType(menu.getType());
                menuTreeVO.setPath(menu.getPath());
                menuTreeVO.setName(menu.getName());
                menuTreeVO.setComponent(menu.getPath());
                menuTreeVO.setRedirect(null);
                menuTreeVO.setIcon(null);
                menuTreeVO.setIsExternal(false);
                menuTreeVO.setIsCache(false);
                menuTreeVO.setIsHidden(false);
                menuTreeVO.setPermission(menu.getAuthority());
                menuTreeVO.setStatus(menu.getDeleted() ? 0 : 1);
                menuTreeVO.setCreateUser(menu.getCreateUserId());
                menuTreeVO.setCreateUserString("系统管理员");
                menuTreeVO.setCreateTime(menu.getCreateTime().toString());
                menuTreeVO.setDisabled(null);
                menuTreeVO.setChildren(buildMenuTree(menus, menu.getId()));
                return menuTreeVO;
            })
            .collect(Collectors.toList());
    }
}