package com.gapache.user.common.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author HuSen
 * create on 2020/3/31 10:31 上午
 */
@Data
public class AccountVO implements Serializable {
    private static final long serialVersionUID = 5224557662102429053L;

    private Long id;

    private String nickName;

    private String phone;

    private String password;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
}
