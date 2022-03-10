package com.gapache.cloud.auth.server.dao.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author HuSen
 * @since 2021/3/25 4:43 下午
 */
@Data
@Entity
@Table(name = "tb_position")
public class PositionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "above_id")
    private Long aboveId;

    @Column(name = "company_id", nullable = false)
    private Long companyId;
}
