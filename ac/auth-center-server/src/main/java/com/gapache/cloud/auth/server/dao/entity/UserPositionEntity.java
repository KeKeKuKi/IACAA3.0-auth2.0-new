package com.gapache.cloud.auth.server.dao.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author HuSen
 * @since 2021/3/26 2:15 下午
 */
@Data
@Entity
@Table(name = "tb_user_position")
public class UserPositionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 用户
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;
    /**
     * 职位
     */
    @Column(name = "position_id")
    private Long positionId;
    /**
     * 直接上级
     */
    @Column(name = "superior_id")
    private Long superiorId;
}
