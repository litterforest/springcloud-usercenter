package com.softd.test.springcloud.usercenter.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softd.test.springcloud.usercenter.support.web.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
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
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private ObjectMapper objectMapper;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        RestResponse<String> restResponse = new RestResponse("1", "登录成功", "登录成功");
        response.getWriter().print(objectMapper.writeValueAsString(restResponse));
    }
}
