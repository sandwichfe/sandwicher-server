package com.lww.auth.server.user_center.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lww.auth.server.user_center.entity.Role;
import com.lww.auth.server.user_center.mapper.RoleMapper;
import com.lww.auth.server.user_center.req.RoleReq;
import com.lww.auth.server.user_center.service.RoleService;
import com.lww.auth.server.user_center.vo.RoleVo;
import com.lww.common.utils.CustomBeanUtils;
import com.lww.common.web.vo.PageVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统角色表 服务实现类
 * </p>
 *
 * @author lww
 * @since 2024-12-16 11:53:37
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public RoleVo createRole(RoleReq roleReq) {
        Role role = new Role();
        BeanUtils.copyProperties(roleReq, role);
        this.save(role);
        return convertToRoleVo(role);
    }

    @Override
    public RoleVo getRoleById(Long id) {
        return convertToRoleVo(this.getById(id));
    }

    @Override
    public IPage<RoleVo> listRole(PageVo pageVo) {
        Page<Role> page = new Page<>(pageVo.getPageNum(), pageVo.getPageSize());
        LambdaQueryWrapper<Role> wrapper = new  LambdaQueryWrapper<>();
        wrapper.orderByDesc(Role::getId);
        return this.page(page, wrapper).convert(this::convertToRoleVo);
    }

    @Override
    public RoleVo updateRole(RoleReq roleReq) {
        Role role = new Role();
        BeanUtils.copyProperties(roleReq, role);
        this.updateById(role);
        return convertToRoleVo(role);
    }

    @Override
    public void deleteRole(Long id) {
        this.removeById(id);
    }

    private RoleVo convertToRoleVo(Role role) {
        return CustomBeanUtils.copyProperties(role, RoleVo.class);
    }
}
