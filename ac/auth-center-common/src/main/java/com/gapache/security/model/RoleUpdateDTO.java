package com.gapache.security.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

/**
 * @author HuSen
 * @since 2021/1/26 10:34 上午
 */
@Setter
@Getter
@ToString
public class RoleUpdateDTO implements Serializable {
    private static final long serialVersionUID = 6231096817246449391L;

    private Long id;

    /**
     * 角色名称
     */
    @NotBlank
    private String name;

    /**
     * 角色描述
     */
    @NotBlank
    private String description;

    /**
     * 拥有的权限
     */
    private Set<Long> permissionList;

    /**
     * 是否是超级管理员
     */
    @NotNull
    private Boolean admin;
}
