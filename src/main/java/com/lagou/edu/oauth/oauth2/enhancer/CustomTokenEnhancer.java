package com.lagou.edu.oauth.oauth2.enhancer;

import com.lagou.edu.oauth.entity.UserJwt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;

/**
 * 自定义Token携带内容
 *
 * @author xianhongle
 * @data 2022/4/10 9:00 下午
 **/
@Slf4j
public class CustomTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

        HashMap<String, Object> additionalInfo = new HashMap<>();
        additionalInfo.put("organization",authentication.getName());
        try {
            UserJwt userJwt = (UserJwt) authentication.getPrincipal();
            if(userJwt != null){
                additionalInfo.put("user_id",userJwt.getId());
            }
        }catch(Exception e){
            log.error("user name:{}",authentication.getName());
        }
        ((DefaultOAuth2AccessToken)accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}
