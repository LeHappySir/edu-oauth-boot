package com.lagou.edu.oauth.service.impl;

import com.lagou.edu.oauth.entity.Role;
import com.lagou.edu.oauth.provider.OrganizationProvider;
import com.lagou.edu.oauth.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * RoleServiceImpl
 *
 * @author xianhongle
 * @data 2022/5/23 22:42
 **/
@Service
public class RoleServiceImpl implements IRoleService {

    @Autowired
    private OrganizationProvider organizationProvider;

    @Override
    public Set<Role> queryUserRoleByUserId(String userId) {
        return organizationProvider.queryUserRoleByUserId(userId).getData();
    }
}
