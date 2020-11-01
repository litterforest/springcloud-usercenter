package com.softd.test.springcloud.usercenter.support.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 0 - 统一错误码
 * 1 - 统一成功码
 * 5000000 - 错误码，第2~3位代表模块，第4~5位代表功能，第6~7位代表具体错误
 *
 * @author cobee
 * @since 2020-11-01
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RestResponse<T> implements Serializable {
    private String code;
    private String message;
    private T data;
}
