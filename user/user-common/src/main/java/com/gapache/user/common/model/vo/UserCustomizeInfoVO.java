package com.gapache.user.common.model.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author HuSen
 * @since 2021/1/25 1:25 下午
 */
@Getter
@Setter
@ToString
public class UserCustomizeInfoVO implements Serializable {
    private static final long serialVersionUID = 2675236376034506830L;

    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    private String clientId;

    private String info;
}
