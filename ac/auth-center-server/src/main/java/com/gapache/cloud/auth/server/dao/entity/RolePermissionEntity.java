package com.gapache.cloud.auth.server.dao.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * 角色权限关系
 * 多对多
 *
 * @author HuSen
 * @since 2021/1/25 1:37 下午
 */
@Setter
@Getter
@ToString
@Entity
@Table(name = "tb_role_permission")
public class RolePermissionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_id", nullable = false)
    private Long roleId;

    @Column(name = "permission_id", nullable = false)
    private Long permissionId;
}
