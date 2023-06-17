package com.lagou.edu.oauth.mult;

/**
 * MultAuthenticationContext
 *
 * @author xianhongle
 * @data 2022/4/7 11:15 下午
 **/
public class MultAuthenticationContext {

    private static ThreadLocal<MultAuthentication> holder = new ThreadLocal<>();

    public static void set(MultAuthentication multAuthentication){holder.set(multAuthentication);}

    public static MultAuthentication get(){ return holder.get(); }

    public static void clear(){ holder.remove(); }

}
