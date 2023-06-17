package com.lagou.edu.oauth.endpoint;

import com.lagou.edu.common.response.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * TokenRevokeEndpoint 退出登陆接口
 *
 * @author xianhongle
 * @data 2022/5/26 22:31
 **/
@FrameworkEndpoint
public class TokenRevokeEndpoint {

    @Autowired
    @Qualifier("consumerTokenServices")
    private ConsumerTokenServices consumerTokenServices;

    @DeleteMapping("/oauth/token")
    public ResponseDTO<String> deleteAccessToken(@RequestParam("access_token") String accessToken){
        consumerTokenServices.revokeToken(accessToken);
        return ResponseDTO.success();
    }

}
