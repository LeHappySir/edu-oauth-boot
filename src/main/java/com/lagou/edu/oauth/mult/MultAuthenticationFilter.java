package com.lagou.edu.oauth.mult;

import com.alibaba.fastjson.JSON;
import com.lagou.edu.common.entity.vo.Result;
import com.lagou.edu.oauth.exception.AuthErrorType;
import com.lagou.edu.oauth.mult.authenticator.MultAuthenticator;
import com.lagou.edu.oauth.mult.authenticator.sms.exception.SmsValidateException;
import com.lagou.edu.oauth.mult.authenticator.sms.result.SmsCodeValidateResult;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * 集成生产token方式的过滤器
 *
 * @author xianhongle
 * @data 2022/4/7 10:37 下午
 **/
@Component
public class MultAuthenticationFilter extends GenericFilterBean implements ApplicationContextAware {

    private static final String OAUTH_TOKEN_URL = "/oauth/token";

    private static final String AUTH_TYPE_PARAM_NAME = "auth_type";

    private final RequestMatcher requestMatcher;

    private ApplicationContext applicationContext;

    private Collection<MultAuthenticator> authenticators;


    public MultAuthenticationFilter() {
        requestMatcher = new OrRequestMatcher(
                new AntPathRequestMatcher(OAUTH_TOKEN_URL,"GET"),
                new AntPathRequestMatcher(OAUTH_TOKEN_URL,"POST")
        );
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String grantType = request.getParameter("grant_type");
        // 指定请求方式 并且为 用户名密码登陆
        if(requestMatcher.matches(request) && "password".equals(grantType)){
            MultAuthentication multAuthentication = new MultAuthentication();
            multAuthentication.setAuthType(request.getParameter(AUTH_TYPE_PARAM_NAME));
            multAuthentication.setAuthParameters(request.getParameterMap());

            MultAuthenticationContext.set(multAuthentication);
            try {

                prepare(multAuthentication, response);

                filterChain.doFilter(request, response);

                complete(multAuthentication);

            }catch(Exception e) {
                PrintWriter writer = response.getWriter();
                if (e instanceof SmsValidateException) {
                    SmsValidateException exception = (SmsValidateException) e;
                    SmsCodeValidateResult result = exception.getResult();
                    response.setContentType("application/json;charset=UTF-8");
                    response.setCharacterEncoding("UTF-8");
                    Result fail = Result.fail(result.getAuthErrorType(), AuthErrorType.ERROR_VERIFY_CODE);
                    writer.write(JSON.toJSONString(fail));
                    writer.flush();
                    writer.close();
                }
                return;
            }finally {
                // 从上下文中清除对应线程存储的信息
                MultAuthenticationContext.clear();
            }

        }else{
            filterChain.doFilter(request, response);
        }
    }

    /**
     * 匹配处理器进行预处理
     *
     * @author xianhongle
     * @date 2022-04-07
     * @param multAuthentication
     * @param response
     * @return void
    */
    private void prepare(MultAuthentication multAuthentication,HttpServletResponse response){
        if(authenticators == null){
            synchronized(this){
                Map<String, MultAuthenticator> authenticatorMap = applicationContext.getBeansOfType(MultAuthenticator.class);
                if(authenticatorMap != null){
                    authenticators = authenticatorMap.values();
                }
            }
        }
        if(authenticators == null){
            authenticators = new ArrayList<>();
        }

        authenticators.forEach(authenticator -> {
            if(authenticator.support(multAuthentication)){
                authenticator.prepare(multAuthentication, response);
            }
        });

    }

    /**
     * 后置处理
     *
     * @author xianhongle
     * @date 2022-04-07
     * @param multAuthentication
     * @return void
    */
    private void complete(MultAuthentication multAuthentication){
        authenticators.forEach(authenticator -> {
            if(authenticator.support(multAuthentication)){
                authenticator.complete(multAuthentication);
            }
        });
    }

}
