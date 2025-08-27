package com.lww.auth.server.user_center.vo;

import lombok.Data;

import java.util.List;

/**
 * @author sandw
 */
@Data
public class MenuTreeVO {
    private Long id;
    private Long parentId;
    private String title;
    private Integer sort;
    private Byte type;
    private String path;
    private String name;
    private String component;
    private String redirect;
    private String icon;
    private Boolean isExternal;
    private Boolean isCache;
    private Boolean isHidden;
    private String permission;
    private Integer status;
    private Long createUser;
    private String createUserString;
    private String createTime;
    private Boolean disabled;
    private List<MenuTreeVO> children;
}