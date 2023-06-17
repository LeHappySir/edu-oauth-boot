package com.lagou.edu.oauth.mult.authenticator;

import com.lagou.edu.user.client.dto.UserDTO;
import com.lagou.edu.oauth.mult.MultAuthentication;

import javax.servlet.http.HttpServletResponse;

/**
 * AbstractPreparableMultAuthenticator
 *
 * @author xianhongle
 * @data 2022/4/7 11:39 下午
 **/
public abstract class AbstractPreparableMultAuthenticator implements MultAuthenticator{
    @Override
    public abstract UserDTO authenticate(MultAuthentication multAuthentication);

    @Override
    public abstract void prepare(MultAuthentication multAuthentication, HttpServletResponse response);

    @Override
    public abstract boolean support(MultAuthentication multAuthentication);

    @Override
    public void complete(MultAuthentication multAuthentication) {

    }
}
