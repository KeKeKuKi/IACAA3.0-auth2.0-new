package com.gapache.security.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

/**
 * @author HuSen
 * @since 2021/1/26 9:58 上午
 */
@Data
public class RoleCreateDTO implements Serializable {
    private static final long serialVersionUID = 3825742211662606703L;

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
    /**
     * 是否为组
     * 默认false
     */
    private Boolean isGroup = false;
}
