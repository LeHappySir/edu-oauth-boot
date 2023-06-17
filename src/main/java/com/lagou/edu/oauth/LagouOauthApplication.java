package com.lagou.edu.oauth;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * LagouOauthApplication
 *
 * @author xianhongle
 * @data 2022/3/9 10:55 下午
 **/
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients("com.lagou.edu")
@EnableCreateCacheAnnotation
public class LagouOauthApplication {
    public static void main(String[] args) {
        SpringApplication.run(LagouOauthApplication.class,args);
    }
}
