package com.pzhu.iacaa2_0.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ZhaoZezhong
 * @version V1.0
 * @Title: UserLoginDTO
 * @Description: Company:成都平凡谷科技有限责任公司
 * @date 2021/3/1013:53
 */
@Data
public class UserLoginDTO implements Serializable {

    private String username;
    private String password;
}
