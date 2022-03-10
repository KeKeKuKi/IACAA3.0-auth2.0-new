package com.gapache.user.server.dao.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author HuSen
 * @since 2021/3/26 2:27 下午
 */
@Data
@Entity
@Table(name = "tb_user_relation")
public class UserRelationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Column(name = "user_id", nullable = false)
    private Long userId;
}
