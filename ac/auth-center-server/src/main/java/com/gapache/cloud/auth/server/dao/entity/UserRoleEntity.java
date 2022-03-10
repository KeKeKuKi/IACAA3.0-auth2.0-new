package com.gapache.cloud.auth.server.dao.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * @author HuSen
 * @since 2021/1/26 2:54 下午
 */
@Setter
@Getter
@ToString
@Entity
@Table(name = "tb_user_role")
public class UserRoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "role_id")
    private Long roleId;
}
