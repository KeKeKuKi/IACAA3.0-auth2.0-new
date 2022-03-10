package com.gapache.user.server.dao.entity;

import com.gapache.jpa.OpFullRecordEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * @author HuSen
 * @since 2020/9/8 11:23 上午
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "tb_user")
public class UserEntity extends OpFullRecordEntity<Long> {

    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户名
     */
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    /**
     * 密码
     */
    @Column(name = "password", nullable = false, length = 1024)
    private String password;

    /**
     * 是否删除
     */
    @Column(name = "is_delete")
    private Boolean isDelete;
}
