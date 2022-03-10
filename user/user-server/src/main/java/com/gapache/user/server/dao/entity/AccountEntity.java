package com.gapache.user.server.dao.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author ZhaoZezhong
 * @version V1.0
 * @Title: AccountEntity
 * @Description: Company:成都平凡谷科技有限责任公司
 * @date 2021/3/3010:48
 */
@Data
@Entity
@Table(name = "tb_account")
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "money")
    private BigDecimal money;
}
