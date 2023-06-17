package com.lagou.edu.oauth.provider;

import com.lagou.edu.common.entity.vo.Result;
import com.lagou.edu.oauth.entity.Role;

import java.util.HashSet;
import java.util.Set;

/**
 * OrganizationProviderFallback
 *
 * @author xianhongle
 * @data 2022/5/26 22:14
 **/
public class OrganizationProviderFallback implements OrganizationProvider{
    @Override
    public Result<Set<Role>> queryUserRoleByUserId(String userId) {
        return Result.success(new HashSet<Role>());
    }
}
