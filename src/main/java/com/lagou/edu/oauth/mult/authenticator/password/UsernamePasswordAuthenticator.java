package com.lagou.edu.oauth.mult.authenticator.password;

import com.lagou.edu.oauth.mult.MultAuthentication;
import com.lagou.edu.oauth.mult.authenticator.AbstractPreparableMultAuthenticator;
import com.lagou.edu.user.client.dto.UserDTO;
import com.lagou.edu.user.client.remote.UserRemoteService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

/**
 * UsernamePasswordAuthenticator
 *
 * @author xianhongle
 * @data 2022/4/10 12:14 下午
 **/
@Component
@Slf4j
public class UsernamePasswordAuthenticator extends AbstractPreparableMultAuthenticator {

    @Autowired
    private UserRemoteService userRemoteService;

    @Override
    public UserDTO authenticate(MultAuthentication multAuthentication) {
        return userRemoteService.getUserByPhone(multAuthentication.getUsername());
    }

    @Override
    public void prepare(MultAuthentication multAuthentication, HttpServletResponse response) {

    }

    @Override
    public boolean support(MultAuthentication multAuthentication) {
        return StringUtils.isBlank(multAuthentication.getAuthType());
    }
}
