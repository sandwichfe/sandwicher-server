package com.lww.auth.server.user_center.vo;

import java.util.List;

import lombok.Data;

/**
 * @author lww
 */
@Data
public class DeptTreeVO {
    private Long id;
    private Long parentId;
    private String name;
    private Integer sort;
    private String leader;
    private String phone;
    private String email;
    private Boolean status;
    private String createTime;
    private List<DeptTreeVO> children;
}
