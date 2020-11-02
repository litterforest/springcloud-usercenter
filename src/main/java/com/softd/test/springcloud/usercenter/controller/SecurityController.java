package com.softd.test.springcloud.usercenter.controller;

import com.softd.test.springcloud.usercenter.config.security.jwt.JwtTokenHelper;
import com.softd.test.springcloud.usercenter.support.web.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * 功能描述
 *
 * @author cobee
 * @since 2020-10-31
 */
@RestController
@Slf4j
public class SecurityController {

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @RequestMapping("/loginMsg")
    public RestResponse<String> loginMsg(HttpSession session) {
        System.out.println("========================session:" + session);
        return new RestResponse("1", "请先登录", "请先登录");
    }

//    @PostMapping("/login")
//    public RestResponse<String> login(String username, String password) {
//        UsernamePasswordAuthenticationToken token = null;
//        Authentication authenticate = null;
//        try {
//            token = new UsernamePasswordAuthenticationToken(username, password);
//            authenticate = authenticationManager.authenticate(token);
//        } catch (AuthenticationException e) {
//            log.error("token info:" + token, e);
//            return new RestResponse("0", "用户名或密码错误", "用户名或密码错误");
//        }
//        User user = null;
//        if (authenticate != null) {
//            user = (User) authenticate.getPrincipal();
//        }
//        return new RestResponse("1", "登录成功", jwtTokenHelper.generateToken(user));
//    }
}
