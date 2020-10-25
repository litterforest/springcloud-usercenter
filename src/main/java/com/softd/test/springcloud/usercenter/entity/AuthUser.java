package com.softd.test.springcloud.usercenter.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 功能描述
 *
 * @author cobee
 * @since 2020-10-25
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AuthUser implements Serializable {
    private Long userId;
    private String username;
    private String password;
    private String nickName;
}
