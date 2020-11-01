package com.softd.test.springcloud.usercenter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 功能描述
 *
 * @author cobee
 * @since 2020-10-31
 */
@Service
public class AuthService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Resource
    private AuthenticationManager authenticationManager;
    /**
     * 返回所有的
     * @return
     */
    public Map<String, List<String>> roleResourceMap() {
        String sql = "select a.role_code, c.resource_path from auth_role a join auth_role_resource b on a.role_id = b.role_id join auth_resource c on b.resource_id = c.resource_id";
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql);
        Map<String, List<Map<String, Object>>> roleCode = mapList.stream().collect(Collectors.groupingBy((map) -> {
            return (String) map.get("role_code");
        }));
        if (roleCode != null && !roleCode.isEmpty()) {
            Map<String, List<String>> resultMap = new HashMap<>();
            for (Map.Entry<String, List<Map<String, Object>>> entry : roleCode.entrySet()) {
                String role = entry.getKey();
                List<Map<String, Object>> value = entry.getValue();
                List<String> resourcePathList = value.stream().map((a) -> {
                    return (String) a.get("resource_path");
                }).collect(Collectors.toList());
                resultMap.put(role.substring("ROLE_".length()), resourcePathList);
            }
            return resultMap;
        }
        return Collections.emptyMap();
    }
}
