package com.gapache.security.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author HuSen
 * @since 2021/1/26 3:47 下午
 */
@Data
public class SetUserRoleDTO implements Serializable {
    private static final long serialVersionUID = -9199443312819230391L;

    private Long userId;

    private Long roleId;
}
