package com.gapache.security.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author HuSen
 * @since 2021/3/30 11:48 上午
 */
@Data
public class ResourceDTO implements Serializable {
    private static final long serialVersionUID = 5716460972166154096L;

    private Long id;

    private String scope;

    private String name;

    private Long resourceServerId;

    private String resourceServerName;
}
