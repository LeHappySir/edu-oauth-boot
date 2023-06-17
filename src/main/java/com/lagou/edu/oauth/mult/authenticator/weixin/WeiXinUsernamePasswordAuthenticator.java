package com.lagou.edu.oauth.mult.authenticator.weixin;

import com.lagou.edu.oauth.mult.MultAuthentication;
import com.lagou.edu.oauth.mult.authenticator.AbstractPreparableMultAuthenticator;
import com.lagou.edu.user.client.dto.UserDTO;
import com.lagou.edu.user.client.dto.WeixinDTO;
import com.lagou.edu.user.client.remote.UserRemoteService;
import com.lagou.edu.user.client.remote.UserWeixinRemoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

/**
 * UsernamePasswordAuthenticator
 *
 * @author xianhongle
 * @data 2022/5/28 17:03
 **/
@Component
@Slf4j
public class WeiXinUsernamePasswordAuthenticator extends AbstractPreparableMultAuthenticator {

    @Autowired
    private UserRemoteService userRemoteService;

    @Autowired
    private UserWeixinRemoteService userWeixinRemoteService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    private final static String WEIXIN_AUTH_TYPE = "weixin";


    @Override
    public UserDTO authenticate(MultAuthentication multAuthentication) {
        String openId = multAuthentication.getAuthParameter("password");
        WeixinDTO weixinDTO = userWeixinRemoteService.getUserWeixinByOpenId(openId);
        if(weixinDTO == null){
            return null;
        }
        UserDTO user = userRemoteService.getUserById(weixinDTO.getUserId());
        if(user == null){
            return null;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return user;
    }

    @Override
    public void prepare(MultAuthentication multAuthentication, HttpServletResponse response) {

    }

    @Override
    public boolean support(MultAuthentication multAuthentication) {
        return WEIXIN_AUTH_TYPE.equals(multAuthentication.getAuthType());
    }
}
