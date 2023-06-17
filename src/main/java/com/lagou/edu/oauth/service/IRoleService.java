package com.lagou.edu.oauth.service;

import com.lagou.edu.oauth.entity.Role;

import java.util.Set;

/**
 * IRoleService
 *
 * @author xianhongle
 * @data 2022/5/23 22:42
 **/
public interface IRoleService {

    Set<Role> queryUserRoleByUserId(String userId);
}
