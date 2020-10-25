package com.softd.test.springcloud.usercenter.service;

import com.softd.test.springcloud.usercenter.entity.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            AuthUser authUser = jdbcTemplate.queryForObject("select * from auth_user where username = ?",
                    new BeanPropertyRowMapper<>(AuthUser.class), username);
            if (authUser == null) {
                throw new UsernameNotFoundException("用户名不存在");
            }
            //用户权限
            Set<SimpleGrantedAuthority> authorities = new HashSet<>();
            String sql = "select b.role_id, b.role_code from auth_user_role a join auth_role b on a.role_id = b.role_id where a.user_id = ?";
            List<Map<String,Object>> list = jdbcTemplate.queryForList(sql, authUser.getUserId());
            if (!CollectionUtils.isEmpty(list)) {
                authorities = list.stream().map(t -> {
                    return new SimpleGrantedAuthority((String) t.get("role_code"));
                }).collect(Collectors.toSet());
            }
            return new User(authUser.getUsername(), authUser.getPassword(), authorities);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
}
