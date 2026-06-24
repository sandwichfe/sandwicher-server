package com.lww.auth.server.portal.vo.req;

import lombok.Data;

import java.util.List;

/**
 * @author lww
 * @since 2024-12-17
 */
@Data
public class AssignRolesToUserRequest {

    private Long userId;

    private List<Long> roleIds;

}