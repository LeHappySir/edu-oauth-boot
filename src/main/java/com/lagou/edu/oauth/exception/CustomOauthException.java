package com.lagou.edu.oauth.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lagou.edu.common.entity.vo.Result;
import lombok.EqualsAndHashCode;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * CustomOauthException
 *
 * @author xianhongle
 * @data 2022/4/10 6:48 下午
 **/
@EqualsAndHashCode(callSuper = true)
@JsonSerialize(using = CustomOauthExceptionSerializer.class)
public class CustomOauthException extends OAuth2Exception {

    private Result result;

    public CustomOauthException(OAuth2Exception exception) {
        super(exception.getSummary() , exception);
        this.result = Result.fail(AuthErrorType.valueOf(exception.getOAuth2ErrorCode().toUpperCase()), exception);
    }

    public Result getResult() {
        return result;
    }
}
