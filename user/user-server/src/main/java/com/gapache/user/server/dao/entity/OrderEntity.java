package com.gapache.user.server.dao.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author ZhaoZezhong
 * @version V1.0
 * @Title: OrderEntity
 * @Description: Company:成都平凡谷科技有限责任公司
 * @date 2021/3/3010:46
 */
@Data
@Entity
@Table(name = "tb_order")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_no", nullable = false, unique = true)
    private String orderNo;
}
