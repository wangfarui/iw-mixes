package com.itwray.iw.auth.model;

import com.itwray.iw.auth.model.entity.AuthRole;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户详情
 *
 * @author wray
 * @since 2024/3/12
 */
@Data
public class AuthUserDetails implements UserDetails {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 姓名
     */
    private String name;

    /**
     * 账号是否过期
     */
    private boolean accountNonExpired;

    /**
     * 账号是否锁定
     */
    private boolean accountNonLocked;

    /**
     * 用户凭证是否过期
     */
    private boolean credentialsNonExpired;

    /**
     * 是否启用
     */
    private boolean enabled;

    /**
     * 授权列表
     */
    private List<? extends GrantedAuthority> authorities;

    public void setRoles(List<AuthRole> roles) {
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        for (AuthRole role : roles) {
            authorityList.add(new SimpleGrantedAuthority(role.getName()));
        }
        this.authorities = authorityList;
    }
}
