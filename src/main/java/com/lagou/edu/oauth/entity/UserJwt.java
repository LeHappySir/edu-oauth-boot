package com.lagou.edu.oauth.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * UserJwt
 *
 * @author xianhongle
 * @data 2022/4/10 3:24 下午
 **/
public class UserJwt extends User {

    private String id;

    public UserJwt(String username, String password, Collection<? extends GrantedAuthority> authorities,String id) {
        super(username, password, authorities);
        this.id = id;
    }


    public UserJwt(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities, String id) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
