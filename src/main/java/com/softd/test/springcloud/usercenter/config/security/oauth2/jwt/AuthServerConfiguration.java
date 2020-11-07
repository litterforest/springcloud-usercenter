package com.softd.test.springcloud.usercenter.config.security.oauth2.jwt;

import com.softd.test.springcloud.usercenter.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

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
    @Autowired
    private UserDetailsServiceImpl userService;

    @Bean
    public ClientDetailsService clientDetails() {
        return new JdbcClientDetailsService(dataSource);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //对密码进行加密，数据保存在数据库当中
        clients.withClientDetails(clientDetails());
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager).userDetailsService(userService)
                .tokenServices(defaultTokenServices());

        // 获取token，post表单提交
        // http://localhost:8080/oauth/token
        // 刷新token，post非表单提交
        // http://localhost:8080/oauth/token?grant_type=refresh_token&client_id=client&client_secret=123456&refresh_token=6177c269-c4c2-4484-8be9-84b5b1dd72de
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer
                //.realm("oauth2-resources")
                //url:/oauth/token_key,exposes public key for token verification if using JWT tokens
                .tokenKeyAccess("permitAll()")
                //url:/oauth/check_token allow check token
//                .checkTokenAccess("isAuthenticated()")
                .checkTokenAccess("permitAll()")
                //允许表单认证
                .allowFormAuthenticationForClients();
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        //JWT Access Token 转换器，由加密串转为JSON
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        //对称秘钥
        converter.setSigningKey("abc123456");
        //私钥签名、公钥验证
//        converter.setSigningKey("-----BEGIN RSA PRIVATE KEY-----\n" +
//                "MIIEowIBAAKCAQEAyTZHUvEeFCmD3qD3W05IEPlFCRhAp64W+YaOTSGC1+Z1uWBa\n" +
//                "dujx39/cF4i1t5Prfo/F5t7tfb+EDrUIn3jxD8fmlrzC2OcoZlcsRAnLswtx7gJ4\n" +
//                "z2K6nPlXvIp0zacSVxvEOxUIUNCa+6s+dA0K6g0DW5H/0gms0el+LLgRu87jdDWX\n" +
//                "dzSmvIV+xr/pAM3GxgWTeNdngl9UxnevVs26L9UpFwjIDoeQqsQJN20Y7NsXNoL3\n" +
//                "9sKrsxBjHY+iG52Z5i7Ds09Z5DvABW5/gKiSeF6UkhiARaq/lKBbGuRkGjvBdFOS\n" +
//                "Z1ZP45KmMA8lQ1VvJnyh9A1gkwxOKMXLkYd3MQIDAQABAoIBAFIC5Z9Q18mwAifD\n" +
//                "xA5bIdSJRTInyHKP6p1Z0rZtaj9Fl8YvF7BP0gZF4BdXiXmBuQKk5yzqwDH41YKJ\n" +
//                "FzKowNiFyd62oVR3I2hTL5+rvnMDvzQbndIpk5ZXqwMWC/WmmzsV/i12rSUDYagi\n" +
//                "bBoxFrm/BBdWssaRWvdUDDF99Pxu5YDvmXntIJlhbvXcUN4PofHMRQiRRTQeXD9Q\n" +
//                "LGvhsJ1WIgdGmlZagHO6SRTLlJd9cfrqgCmGQpg/LnRPM/fXGR2B82lAeEUv10fD\n" +
//                "2WPXcTl8rlSi/isUUN/twKc3GaqYrnRrCJ3MiJT8Lg1FYf/AwnNZ1rb3OppsR0Ag\n" +
//                "EF1P3oECgYEA9X/oRO+4/naicRnVhN4FeR3r1FgE87Bljna1s9pqgQguBQmgdn46\n" +
//                "Zj154OwPdsN1S5Yg3RDdjZmnjfCPlEFNHBGpleHhQRI7I2DDeabluLHHluCgY4Mw\n" +
//                "4eZca5vaImZVtA4sQz8yaR10mX/wGUw5B5tW7FWliyh/2UbgNIoewi0CgYEA0dFx\n" +
//                "8oYxari39jinKf9bM84lVbzccNrpcMU//dLjmhMeD8J+xHGp8o08FKz/i+XoqAxe\n" +
//                "KljC4ihrc7H3xWi8vWv9dwHFfFwo+VczoxpivCwe2fPaOP4Z6g9+45CZizZxz6GO\n" +
//                "6xhMA+TQ96qlEoJ4T24MY39zZU8wNPlw5N+xH5UCgYEAr8WrvKfS0UaJmecATIhW\n" +
//                "bNhygG+g5AWZQP5XrHUmqkn8ARlabVyFXayIdfUuQT9C3SKZVw57QqYQJH1nn6N9\n" +
//                "nSo8PJckm119QCBI9PH9KlcHa0xbKcTFnAg+hcFp1hVlKWy4XlGCO2aelETY2JQN\n" +
//                "hRfAjafoxhDyMNQhNRzrVuUCgYBrYdsEeWNvMCyOaj47g0IlCFsZPzg+1frlST5P\n" +
//                "5I+xuhkHjc4dMeL9jQTzu/ppmffxkarb12OeJXug0bNyKAF4nH0zXAe7dttNiTCX\n" +
//                "SBjCH36Go4PK6VlP7jBNvSKoGewzjIa9kUjOMVw1dPNYvsdeN39FqOPhNJ8Cbas2\n" +
//                "p7lZ5QKBgCMr2TjoBu5qbu5u2wujj1eyVLt8CkQUgSjUCnbaZmTGEtyAFGOXTnQO\n" +
//                "gX/r2sWBkisUkYQjcb1im1ouNlilajzllrqd3t9FBwVqs6gWFkywvgHbu0m8121w\n" +
//                "Cr9uYKibl8jaVGGMte878hdjK9Gzan7iwFY3Enn5dsUp0d5Hg4DQ\n" +
//                "-----END RSA PRIVATE KEY-----");
//        converter.setVerifierKey("-----BEGIN PUBLIC KEY-----\n" +
//                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAyTZHUvEeFCmD3qD3W05I\n" +
//                "EPlFCRhAp64W+YaOTSGC1+Z1uWBadujx39/cF4i1t5Prfo/F5t7tfb+EDrUIn3jx\n" +
//                "D8fmlrzC2OcoZlcsRAnLswtx7gJ4z2K6nPlXvIp0zacSVxvEOxUIUNCa+6s+dA0K\n" +
//                "6g0DW5H/0gms0el+LLgRu87jdDWXdzSmvIV+xr/pAM3GxgWTeNdngl9UxnevVs26\n" +
//                "L9UpFwjIDoeQqsQJN20Y7NsXNoL39sKrsxBjHY+iG52Z5i7Ds09Z5DvABW5/gKiS\n" +
//                "eF6UkhiARaq/lKBbGuRkGjvBdFOSZ1ZP45KmMA8lQ1VvJnyh9A1gkwxOKMXLkYd3\n" +
//                "MQIDAQAB\n" +
//                "-----END PUBLIC KEY-----");
        return converter;
    }

    @Bean
    @Primary
    public DefaultTokenServices defaultTokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(new RedisTokenStore(redisConnectionFactory));
        // 不允许刷新token
        tokenServices.setSupportRefreshToken(false);
        tokenServices.setTokenEnhancer(this.jwtAccessTokenConverter());
        // jwt有效时长是60秒
        tokenServices.setAccessTokenValiditySeconds(60);
        return tokenServices;
    }

}