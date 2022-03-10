package com.gapache.cloud.auth.server.dao.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * @author HuSen
 * @since 2021/1/26 2:01 下午
 */
@Setter
@Getter
@ToString
@Entity
@Table(name = "tb_resource_server")
public class ResourceServerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "client_id", unique = true)
    private String clientId;
}
