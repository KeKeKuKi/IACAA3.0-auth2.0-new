package com.gapache.user.common.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author HuSen
 * @since 2021/3/26 2:55 下午
 */
@Data
public class SaveUserRelationVO implements Serializable {
    private static final long serialVersionUID = 7935937825406657287L;

    private Integer type;

    private List<Long> ownerIdList;

    private Long userId;
}
