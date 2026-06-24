package com.lww.auth.server.user_center.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lww.auth.server.user_center.entity.Role;
import com.lww.auth.server.user_center.req.RoleReq;
import com.lww.auth.server.user_center.vo.RoleVo;
import com.lww.common.web.vo.PageVo;

/**
 * <p>
 * 系统角色表 服务类
 * </p>
 *
 * @author lww
 * @since 2024-12-16 11:53:37
 */
public interface RoleService extends IService<Role> {

    RoleVo createRole(RoleReq roleReq);

    RoleVo getRoleById(Long id);

    IPage<RoleVo> listRole(PageVo pageVo);

    RoleVo updateRole(RoleReq roleReq);

    void deleteRole(Long id);
}
