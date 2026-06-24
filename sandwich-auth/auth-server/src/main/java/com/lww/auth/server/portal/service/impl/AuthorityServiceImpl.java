package com.lww.auth.server.portal.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lww.auth.server.portal.entity.Menu;
import com.lww.auth.server.portal.mapper.MenuMapper;
import com.lww.auth.server.portal.req.MenuReq;
import com.lww.auth.server.portal.service.MenuService;
import com.lww.auth.server.portal.vo.MenuTreeVO;
import com.lww.auth.server.portal.vo.MenuVo;
import com.lww.common.web.vo.PageVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author sandw
 */
@RequiredArgsConstructor
@Service
public class AuthorityServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    private final MenuMapper menuMapper;

    @Override
    public MenuVo createMenu(MenuReq menuReq) {
        Menu menu = new Menu();
        BeanUtils.copyProperties(menuReq, menu);
        menu.setMenuPid(Optional.ofNullable(menu.getMenuPid()).orElse(0L));
        this.save(menu);
        return convertToMenuVo(menu);
    }

    @Override
    public MenuVo getMenuById(Long id) {
        return convertToMenuVo(this.getById(id));
    }

    @Override
    public IPage<MenuVo> listMenu(PageVo pageVo) {
        Page<Menu> page = new Page<>(pageVo.getPageNum(), pageVo.getPageSize());
        return this.page(page).convert(this::convertToMenuVo);
    }

    @Override
    public MenuVo updateMenu(MenuReq menuReq) {
        Menu menu = new Menu();
        BeanUtils.copyProperties(menuReq, menu);
        this.updateById(menu);
        return convertToMenuVo(menu);
    }

    @Override
    public void deleteMenu(Long id) {
        this.removeById(id);
    }

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

    private MenuVo convertToMenuVo(Menu menu) {
        MenuVo menuVo = new MenuVo();
        BeanUtils.copyProperties(menu, menuVo);
        return menuVo;
    }

    private List<MenuTreeVO> buildMenuTree(List<Menu> menus, Long parentId) {
        // 对菜单列表按 sort 字段进行升序排序，确保同一层级菜单有序处理
        menus.sort(Comparator.comparing(Menu::getSort, Comparator.nullsFirst(Comparator.naturalOrder())));
        return menus.stream()
                .filter(menu -> menu.getMenuPid().equals(parentId))
                .sorted(Comparator.comparing(Menu::getSort, Comparator.nullsFirst(Comparator.naturalOrder())))
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
                    menuTreeVO.setStatus(menu.getDeleted());
                    menuTreeVO.setCreateUser(menu.getCreateBy());
                    menuTreeVO.setCreateTime(menu.getCreateTime().toString());
                    menuTreeVO.setDisabled(null);
                    menuTreeVO.setChildren(buildMenuTree(menus, menu.getId()));
                    return menuTreeVO;
                }).collect(Collectors.toList());
    }
}
