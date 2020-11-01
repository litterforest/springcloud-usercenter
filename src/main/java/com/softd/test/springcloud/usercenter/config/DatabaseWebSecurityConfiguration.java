package com.softd.test.springcloud.usercenter.config;

import com.softd.test.springcloud.usercenter.service.AuthService;
import com.softd.test.springcloud.usercenter.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import java.util.List;
import java.util.Map;

@Configuration
public class DatabaseWebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private AuthService authService;
    @Autowired
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
    @Autowired
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Bean
    public SCryptPasswordEncoder sCryptPasswordEncoder() {
        return new SCryptPasswordEncoder();
    }

    // 配置认证（用户名密码认证）
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(sCryptPasswordEncoder());
    }

    //配置授权(资源的访问规则)
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //允许跨域访问
        http.csrf().disable();
        http.cors().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 表单登录认证（通过浏览器访问的时候回跳转到/login登录页），如果不配置，则浏弹出览器自带的登录框
//        http.formLogin().loginPage("/loginMsg").loginProcessingUrl("/login").
//                successHandler(myAuthenticationSuccessHandler).failureHandler(myAuthenticationFailureHandler);
        //允许基于HttpServletRequest使用
        http.authorizeRequests()
                //对于/oauth/any/** 赋予任何人都可以访问的权限
                .antMatchers("/loginMsg", "/login").permitAll();
        Map<String, List<String>> roleResourceMap = authService.roleResourceMap();
        if (roleResourceMap != null && !roleResourceMap.isEmpty()) {
            for (Map.Entry<String, List<String>> entry : roleResourceMap.entrySet()) {
                String role = entry.getKey();
                List<String> value = entry.getValue();
                String[] resources = new String[value.size()];
                http.authorizeRequests().antMatchers(value.toArray(resources)).hasRole(role);
            }
        }
//        http.authorizeRequests()
//                //对于/oauth/admin/** 只有ADMIN角色可以访问
//                .antMatchers(HttpMethod.GET,"/oauth/admin/**")
//                .hasRole("ADMIN");
//        http.authorizeRequests()
//                //对于/oauth/user/** 只有USER角色可以访问
//                .antMatchers("/oauth/user/**")
//                .hasRole("USER");
//        http.authorizeRequests()
//                //对于/oauth/both/** 同时具有两个角色才可以访问
//                .antMatchers("/oauth/both/**")
//                .access("hasRole('ADMIN') and hasRole('USER')");
        //对于没有匹配上的路径, 则需要认证, 不区分角色
        http.authorizeRequests().anyRequest().authenticated();
        // httpbasic认证（通过postman访问时）, 如果不配置则自动跳到登录页
//        http.httpBasic();
    }

}
