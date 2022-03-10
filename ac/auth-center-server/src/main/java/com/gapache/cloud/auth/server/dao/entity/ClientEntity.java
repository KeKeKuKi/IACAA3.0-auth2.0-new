package com.gapache.cloud.auth.server.dao.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author HuSen
 * @since 2020/7/31 4:54 下午
 */
@Data
@Entity
@Table(name = "tb_client")
public class ClientEntity {

    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id", unique = true, nullable = false)
    private String clientId;

    @Column(name = "secret", nullable = false, length = 1024)
    private String secret;

    @Column(name = "grant_types")
    private String grantTypes;

    @Column(name = "scopes")
    private String scopes;

    @Column(name = "redirect_url")
    private String redirectUrl;

    @Column(name = "timeout", nullable = false)
    private Long timeout;

    @Column(name = "refresh_token_timeout", nullable = false)
    private Long refreshTokenTimeout;

    @Column(name = "auto_grant", nullable = false)
    private Boolean autoGrant;

    @Column(name = "private_key")
    private String privateKey;
}
