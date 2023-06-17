package com.lagou.edu.oauth.mult.authenticator.sms;

import com.lagou.edu.common.response.ResponseDTO;
import com.lagou.edu.oauth.exception.AuthErrorType;
import com.lagou.edu.oauth.mult.MultAuthentication;
import com.lagou.edu.oauth.mult.authenticator.AbstractPreparableMultAuthenticator;
import com.lagou.edu.oauth.mult.authenticator.sms.event.SmsAuthenticateBeforeEvent;
import com.lagou.edu.oauth.mult.authenticator.sms.event.SmsAuthenticateSuccessEvent;
import com.lagou.edu.oauth.mult.authenticator.sms.exception.SmsValidateException;
import com.lagou.edu.oauth.mult.authenticator.sms.result.SmsCodeValidateResult;
import com.lagou.edu.oauth.mult.authenticator.sms.result.SmsValidateResultContext;
import com.lagou.edu.user.client.dto.UserDTO;
import com.lagou.edu.user.client.remote.UserRemoteService;
import com.lagou.edu.user.client.remote.VerificationCodeRemoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletResponse;

/**
 * SmsMultAuthenticator
 *
 * @author xianhongle
 * @data 2022/5/28 18:32
 **/
@Slf4j
public class SmsMultAuthenticator extends AbstractPreparableMultAuthenticator implements ApplicationEventPublisherAware {

    @Autowired
    private UserRemoteService userRemoteService;

    @Autowired
    private VerificationCodeRemoteService verificationCodeRemoteService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private ApplicationEventPublisher applicationEventPublisher;

    private final static String SMS_AUTH_TYPE = "mobile";


    @Override
    public UserDTO authenticate(MultAuthentication multAuthentication) {

        SmsCodeValidateResult smsCodeValidateResult = SmsValidateResultContext.get();
        if(smsCodeValidateResult != null && !smsCodeValidateResult.isSuccess()){
            AuthErrorType authErrorType = smsCodeValidateResult.getAuthErrorType();
            log.info("短信验证码错误,手机号:{}, 结果:{}", multAuthentication.getUsername(), authErrorType);
            return null;
        }
        String code = multAuthentication.getAuthParameter("password");
        String phone = multAuthentication.getUsername();
        applicationEventPublisher.publishEvent(new SmsAuthenticateBeforeEvent(multAuthentication));

        UserDTO userDTO = userRemoteService.getUserByPhone(phone);
        if(userDTO != null){
            userDTO.setPassword(passwordEncoder.encode(code));
            applicationEventPublisher.publishEvent(new SmsAuthenticateSuccessEvent(multAuthentication));
        }
        return userDTO;
    }

    @Override
    public void prepare(MultAuthentication multAuthentication, HttpServletResponse response) {
        String code = multAuthentication.getAuthParameter("password");
        String phone = multAuthentication.getUsername();
        ResponseDTO responseDTO = verificationCodeRemoteService.checkCode(phone, code);

        if(responseDTO == null || !responseDTO.isSuccess()){

            SmsCodeValidateResult validateResult = new SmsCodeValidateResult();
            validateResult.setSuccess(false);
            validateResult.setAuthErrorType(AuthErrorType.ERROR_VERIFY_CODE);
            throw new SmsValidateException(validateResult);
        }
    }

    @Override
    public boolean support(MultAuthentication multAuthentication) {
        return SMS_AUTH_TYPE.equals(multAuthentication.getAuthType());
    }


    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
