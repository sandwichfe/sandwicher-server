package com.lww.auth.server.user_center.vo.req;

import lombok.Data;

import java.util.List;

/**
 * @author lww
 * @since 2024-12-17
 */
@Data
public class AssignMenusToRoleRequest {

    private Long roleId;

    private List<Long> menuIds;

}