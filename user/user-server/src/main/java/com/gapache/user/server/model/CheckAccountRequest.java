package com.gapache.user.server.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author HuSen
 * create on 2020/3/31 10:27 上午
 */
@Data
public class CheckAccountRequest implements Serializable {
    private static final long serialVersionUID = -3310240225033545207L;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 密码
     */
    private String password;
}
