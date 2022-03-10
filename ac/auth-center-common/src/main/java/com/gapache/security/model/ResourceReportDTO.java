package com.gapache.security.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author HuSen
 * @since 2020/8/7 1:29 下午
 */
@Data
public class ResourceReportDTO implements Serializable {
    private static final long serialVersionUID = 2879086767064464800L;

    /**
     * clientId
     * 标识源应用是谁
     */
    private String clientId;

    /**
     * 资源服务名
     */
    private String resourceServerName;

    /**
     * 需要进行认证的资源
     */
    private List<AuthResourceDTO> authResources;
}
