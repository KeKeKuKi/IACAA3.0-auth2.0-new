package com.gapache.cloud.auth.server.dao.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author ZhaoZezhong
 * @version V1.0
 * @Title: InStockEntity
 * @Description: Company:成都平凡谷科技有限责任公司
 * @date 2021/3/3010:50
 */
@Data
@Entity
@Table(name = "tb_in_stock")
public class InStockEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "goods_id")
    private Long goodsId;

    @Column(name = "stock")
    private Long stock;
}
