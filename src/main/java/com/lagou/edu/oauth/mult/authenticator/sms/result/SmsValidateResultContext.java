package com.lagou.edu.oauth.mult.authenticator.sms.result;

/**
 * SmsValidateResultContext
 *
 * @author xianhongle
 * @data 2022/5/28 18:41
 **/
public class SmsValidateResultContext {

    private static ThreadLocal<SmsCodeValidateResult> holder = new ThreadLocal<>();

    public static void set(SmsCodeValidateResult smsCodeValidateResult){
        holder.set(smsCodeValidateResult);
    }

    public static SmsCodeValidateResult get(){
        return holder.get();
    }

    public static void clear(){
        holder.remove();
    }
}
