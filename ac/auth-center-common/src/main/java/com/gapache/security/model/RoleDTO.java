package com.gapache.security.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author HuSen
 * @since 2021/1/26 1:27 下午
 */
@Data
public class RoleDTO implements Serializable {
    private static final long serialVersionUID = -6310420542695445535L;

    private Long id;

    private String name;

    private String description;
}
