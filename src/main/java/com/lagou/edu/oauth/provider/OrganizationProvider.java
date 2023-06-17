package com.lagou.edu.oauth.provider;

import com.lagou.edu.common.entity.vo.Result;
import com.lagou.edu.oauth.entity.Role;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Set;

/**
 * OrganizationProvider
 *
 * @author xianhongle
 * @data 2022/5/26 22:13
 **/
@FeignClient(name = "edu-boss-boot",fallback = OrganizationProviderFallback.class)
public interface OrganizationProvider {

    @GetMapping("/role/user/{userId}")
    Result<Set<Role>> queryUserRoleByUserId(@PathVariable("userId") String userId);

}
