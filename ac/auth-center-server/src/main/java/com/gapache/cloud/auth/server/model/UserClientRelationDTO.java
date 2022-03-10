package com.gapache.cloud.auth.server.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author HuSen
 * @since 2020/8/3 9:09 上午
 */
@Data
public class UserClientRelationDTO implements Serializable {
    private static final long serialVersionUID = 2014421490617833548L;

    private Long id;

    private Long userId;

    private Long clientId;
}
