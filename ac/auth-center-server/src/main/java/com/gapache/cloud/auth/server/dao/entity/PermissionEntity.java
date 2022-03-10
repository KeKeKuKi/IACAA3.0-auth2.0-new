package com.gapache.cloud.auth.server.dao.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * 权限
 *
 * @author HuSen
 * @since 2021/1/25 1:37 下午
 */
@Setter
@Getter
@ToString
@Entity
@Table(name = "tb_permission")
public class PermissionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 关联的资源ID
     */
    @Column(name = "resource_id")
    private Long resourceId;
}
