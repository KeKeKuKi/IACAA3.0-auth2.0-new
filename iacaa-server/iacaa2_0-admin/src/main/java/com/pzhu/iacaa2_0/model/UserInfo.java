package com.pzhu.iacaa2_0.model;

import lombok.Data;

import java.util.List;

/**
 * @author HuSen
 * @since 2020/9/17 1:43 下午
 */
@Data
public class UserInfo {
    private String name;
    private String introduction;
    private List<String> roles;
    private String avatar;
    private String token;
}
