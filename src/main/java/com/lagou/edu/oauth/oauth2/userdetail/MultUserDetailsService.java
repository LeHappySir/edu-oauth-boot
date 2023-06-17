package com.lagou.edu.oauth.oauth2.userdetail;

import com.lagou.edu.oauth.entity.Role;
import com.lagou.edu.oauth.entity.UserJwt;
import com.lagou.edu.oauth.mult.MultAuthentication;
import com.lagou.edu.oauth.mult.MultAuthenticationContext;
import com.lagou.edu.oauth.mult.authenticator.MultAuthenticator;
import com.lagou.edu.oauth.service.IRoleService;
import com.lagou.edu.user.client.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户集成认证
 *
 * @author xianhongle
 * @data 2022/4/10 3:11 下午
 **/
@Service("userDetailsService")
@Slf4j
public class MultUserDetailsService implements UserDetailsService {

    @Autowired(required = false)
    private List<MultAuthenticator> multAuthenticators;

    @Autowired
    private IRoleService roleService;

    /**
     * 在登陆过程中会赶紧登陆的用户名去数据库查找对应的用户
     * 系统会根据登陆的密码和数据库对应的用户密码进行校验
     * 自定义的查找用户方法中增加了根据登陆方式去查找登陆用户的方法
     *
     * @author xianhongle
     * @date 2022-04-10
     * @param username
     * @return org.springframework.security.core.userdetails.UserDetails
    */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 从集成认证的上下文中获取认证信息，认证信息会在过滤器中被保存到上下文中
        MultAuthentication multAuthentication = MultAuthenticationContext.get();
        if(multAuthentication == null ){
            multAuthentication = new MultAuthentication();
        }
        multAuthentication.setUsername(username);
        UserDTO userDTO = authenticate(multAuthentication);
        if(userDTO == null){
            throw new UsernameNotFoundException("用户名或密码错误");
        }

        return new UserJwt(
                userDTO.getPhone(),
                userDTO.getPassword(),
                !userDTO.getIsDel(),
                userDTO.getAccountNonExpired(),
                userDTO.getCredentialsNonExpired(),
                userDTO.getAccountNonLocked(),
                obtainGrantedAuthority(userDTO),
                userDTO.getId().toString()
        );
    }

    /**
     * obtainGrantedAuthority 获得登录者所有角色的权限集合
     *
     * @author xianhongle
     * @date 2022-05-26
     * @param userDTO
     * @return java.util.Set<org.springframework.security.core.GrantedAuthority>
    */
    private Set<GrantedAuthority> obtainGrantedAuthority(UserDTO userDTO){
        try {
            Set<Role> roles = roleService.queryUserRoleByUserId(userDTO.getId().toString());
            log.info("user:{},roles:{}",userDTO,roles);
            Set<GrantedAuthority> grantedAuthorities = roles.stream().map(role -> new SimpleGrantedAuthority(role.getCode())).collect(Collectors.toSet());
            return grantedAuthorities;
        }catch(Exception e){
            e.printStackTrace();
            Set<GrantedAuthority> rs = new HashSet<GrantedAuthority>();
            rs.add(new SimpleGrantedAuthority("NONE"));
            return rs;
        }
    }

    /**
     * 根据登陆方法去选择系统中的集成认证方式
     *
     * @author xianhongle
     * @date 2022-04-10
     * @param multAuthentication
     * @return com.lagou.edu.dto.UserDTO
    */
    private UserDTO authenticate(MultAuthentication multAuthentication){
        if(multAuthenticators != null){
            for (MultAuthenticator multAuthenticator : multAuthenticators) {
                if(multAuthenticator.support(multAuthentication)){
                    return multAuthenticator.authenticate(multAuthentication);
                }
            }
        }
        return null;
    }
}
