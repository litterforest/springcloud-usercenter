package com.softd.test.springcloud.usercenter.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softd.test.springcloud.usercenter.support.web.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 功能描述
 *
 * @author cobee
 * @since 2020-11-01
 */
@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Autowired
    private ObjectMapper objectMapper;
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        RestResponse<String> restResponse = new RestResponse("0", "用户名或密码错误",
                "用户名或密码错误");
        response.getWriter().print(objectMapper.writeValueAsString(restResponse));
    }
}
