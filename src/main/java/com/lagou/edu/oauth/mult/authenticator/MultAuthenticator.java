package com.lagou.edu.oauth.mult.authenticator;

import com.lagou.edu.user.client.dto.UserDTO;
import com.lagou.edu.oauth.mult.MultAuthentication;

import javax.servlet.http.HttpServletResponse;

/**
 * MultAuthenticator
 *
 * @author xianhongle
 * @data 2022/4/7 11:22 下午
 **/
public interface MultAuthenticator {


    /**
     * 处理集成认证
     *
     * @param multAuthentication
     * @return
     */
    UserDTO authenticate(MultAuthentication multAuthentication);

    /**
     * 进行预处理
     *
     * @param multAuthentication
     * @param request
     */
    void prepare(MultAuthentication multAuthentication, HttpServletResponse response);

    /**
     * 判断是否支持集成认证类型
     *
     * @param multAuthentication
     * @return
     */
    boolean support(MultAuthentication multAuthentication);

    /**
     * 认证结束后执行
     *
     * @param multAuthentication
     */
    void complete(MultAuthentication multAuthentication);

}
