package com.lagou.edu.oauth.mult;

import lombok.Data;

import java.util.Map;

/**
 * AultAuthentication
 *
 * @author xianhongle
 * @data 2022/4/7 11:08 下午
 **/
@Data
public class MultAuthentication {

    private String authType;

    private String username;

    private Map<String,String[]> authParameters;

    public String getAuthParameter(String parameterName) {
        if(authParameters != null){
            String[] values = authParameters.get(parameterName);
            if(values != null && values.length > 0){
                return values[0];
            }
        }
        return null;
    }



}
