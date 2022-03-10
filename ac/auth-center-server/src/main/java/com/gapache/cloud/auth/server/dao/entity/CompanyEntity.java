package com.gapache.cloud.auth.server.dao.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author HuSen
 * @since 2021/3/26 11:07 上午
 */
@Data
@Entity
@Table(name = "tb_company")
public class CompanyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "above_id")
    private Long aboveId;

    @Column(name = "client_id", nullable = false)
    private String clientId;
}
