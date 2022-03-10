package com.gapache.security.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author HuSen
 * @since 2021/3/24 5:53 下午
 */
@Data
public class RolePermissionDTO implements Serializable {
    private static final long serialVersionUID = -6465671182317804619L;

    private RoleDTO role;

    private List<ElmUiTreeNode> permissions;

    private Set<String> defaultExpandedKeys;

    private Set<String> defaultCheckedKeys;
}
