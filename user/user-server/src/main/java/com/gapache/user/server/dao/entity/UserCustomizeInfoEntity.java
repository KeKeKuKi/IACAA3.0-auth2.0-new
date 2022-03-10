package com.gapache.user.server.dao.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * @author HuSen
 * @since 2021/1/25 11:50 上午
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "tb_user_customize_info")
public class UserCustomizeInfoEntity{

    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "client_id", nullable = false)
    private String clientId;

    @Column(name = "info", length = 4096)
    private String info;
}
