package com.softd.test.springcloud.usercenter.config.security.oauth2.authcode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;

/**
 * 认证服务器配置+
 */
@Configuration
public   class AuthServerConfiguration extends AuthorizationServerConfigurerAdapter {
    private static final String DEMO_RESOURCE_ID = "order";

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    RedisConnectionFactory redisConnectionFactory;
    @Autowired
    private DataSource dataSource;

    @Bean
    public ClientDetailsService clientDetails() {
        return new JdbcClientDetailsService(dataSource);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //对密码进行加密
        clients.withClientDetails(clientDetails());
//        String secret = new BCryptPasswordEncoder().encode("123456");
//        clients.jdbc(dataSource).withClient("client");
//        //能够使用内存或者JDBC来实现客户端详情服务
//        //此处使用：内存式
//        clients.inMemory()
//
//                //创建客户端id和密码
//                .withClient("client")
//                .secret(secret)
//                //支持的oath2.0的授权模式
//                .authorizedGrantTypes("authorization_code")
//                //在authorization_code、implicit模式下的重定向地址
//                .redirectUris("http://example.com")
//                //作用范围,随便写
//                .scopes("select")
//                .accessTokenValiditySeconds(1200)
//                .refreshTokenValiditySeconds(50000);

        //authorization_code模式测试地址：
        //  第一步获取code(GET)：  http://localhost:9908/oauth/authorize?client_id=client&response_type=cope&scope=select&redirect_uri=http://example.com
        //  第二部获取token(POST)： http://localhost:9908/oauth/token?grant_type=authorization_code&code=I2LK5r&client_id=client&client_secret=123456&redirect_uri=http://example.com
        //implicit模式测试地址(GET)： http://localhost:9908/oauth/authorize?client_id=client&response_type=token&scope=select&redirect_uri=http://example.com
        //password模式测试地址(POST)：http://localhost:8801/mima-boot-oauth-security/oauth/token?username=user_1&password=123456&grant_type=password&scope=select&client_id=client&client_secret=123456
        //client_credentials模式测试地址(POST)： http://localhost:9908/oauth/token?grant_type=client_credentials&scope=select&client_id=client&client_secret=123456
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(new RedisTokenStore(redisConnectionFactory))
                .authenticationManager(authenticationManager);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer
                //.realm("oauth2-resources")
                //url:/oauth/token_key,exposes public key for token verification if using JWT tokens
                .tokenKeyAccess("permitAll()")
                //url:/oauth/check_token allow check token
                .checkTokenAccess("isAuthenticated()")
                //允许表单认证
                .allowFormAuthenticationForClients();
    }

}