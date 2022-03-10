package com.gapache.jpa;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * @author HuSen
 * create on 2020/4/29 3:33 下午
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class OpByRecordEntity<OP_MAN extends Serializable> {
    /**
     * 被谁创建
     */
    @CreatedBy
    @Column(name = "create_user_id")
    private OP_MAN createBy;
    /**
     * 最近一次被谁修改
     */
    @LastModifiedBy
    @Column(name = "last_modified_by")
    private OP_MAN lastModifiedBy;
}
