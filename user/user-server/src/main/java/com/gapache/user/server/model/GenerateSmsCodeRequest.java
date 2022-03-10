package com.gapache.user.server.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author HuSen
 * create on 2020/1/11 16:13
 */
@Data
public class GenerateSmsCodeRequest implements Serializable {
    private static final long serialVersionUID = -1821115955340148950L;

    private String thisPhone;

    private String lastPhone;
}
