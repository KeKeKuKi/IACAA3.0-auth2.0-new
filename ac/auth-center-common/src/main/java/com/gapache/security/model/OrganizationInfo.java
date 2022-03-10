package com.gapache.security.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 组织信息
 *
 * @author HuSen
 * @since 2021/3/26 1:25 下午
 */
@Data
public class OrganizationInfo implements Serializable {
    private static final long serialVersionUID = 4924869526983564358L;
    /**
     * 所属公司
     */
    private Long companyId;
    /**
     * 所属职位
     */
    private Long positionId;
    /**
     * 所属职位的某一个单位（人员）的ID
     */
    private Long positionItemId;
    /**
     * 上级领导
     */
    private Long superiorId;
}
