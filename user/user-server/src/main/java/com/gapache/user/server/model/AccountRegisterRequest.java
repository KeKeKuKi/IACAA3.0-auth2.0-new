package com.gapache.user.server.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 最简单的注册方式，手机号注册 验证码进行验证
 *
 * @author HuSen
 * create on 2020/1/10 17:33
 */
@Data
public class AccountRegisterRequest implements Serializable {
    private static final long serialVersionUID = -1011541610302142851L;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 密码
     */
    private String password;

    /**
     * 短信验证码
     */
    private String smsCode;
}
