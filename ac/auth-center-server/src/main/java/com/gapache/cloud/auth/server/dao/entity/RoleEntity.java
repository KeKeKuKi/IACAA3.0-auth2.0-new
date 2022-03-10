package com.gapache.cloud.auth.server.dao.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * 角色
 *
 * @author HuSen
 * @since 2021/1/25 1:36 下午
 */
@Setter
@Getter
@ToString
@Entity
@Table(name = "tb_role")
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "is_manager")
    private Boolean isManager;
}
