package com.lww.sandwich.config.security;

import com.lww.sandwich.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @description: 数据库User对象转换为UserDetails对象
 * @author lww
 * @since 2022/7/20 15:11
 */
public class SecurityUserDetails extends User implements UserDetails {

    /**
     * 数据库User对象转换为UserDetails对象
     * @author lww
     * @since 2022/7/20 15:13
     * @param user
     * @return
     */
    public SecurityUserDetails(User user) {
        if(user!=null) {
            this.setUsername(user.getUsername());
            this.setPassword(user.getPassword());
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //理想型返回 admin 权限，可自已处理这块
        return AuthorityUtils.commaSeparatedStringToAuthorityList("admin");
    }

    /** 
     * md 大坑  这里一直返回为null  结果导致还是所有的路径被拦截 可能这里的username是拿去校验的
     * @author lww
     * @since 2022/7/21 11:01
     * @param
     * @return
     */
    //@Override
    public String getUsername11111() {
        return null;
    }

    /**
     * 账户是否还未过期
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 是否不禁用
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 密码是否还未过期
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 是否启用
     * @return
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
