package com.softd.test.springcloud.usercenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

@SpringBootApplication
@EnableWebSecurity
@EnableAuthorizationServer
public class SpringCloudUsercenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudUsercenterApplication.class, args);
    }

}
