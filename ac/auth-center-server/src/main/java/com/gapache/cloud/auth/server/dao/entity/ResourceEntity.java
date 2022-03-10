package com.gapache.cloud.auth.server.dao.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * 资源
 *
 * @author HuSen
 * @since 2020/8/6 4:08 下午
 */
@Data
@Entity
@Table(name = "tb_resource")
public class ResourceEntity {

    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "resource_server_id", nullable = false)
    private Long resourceServerId;

    @Column(name = "resource_server_name", nullable = false)
    private String resourceServerName;

    @Column(name = "scope", nullable = false)
    private String scope;

    @Column(name = "name", nullable = false)
    private String name;

    public String fullScopeName() {
        return this.resourceServerName + ":" + this.scope;
    }
}
