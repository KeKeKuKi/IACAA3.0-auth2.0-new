package com.gapache.security.interfaces;

import com.gapache.security.model.ResourceReportDTO;

/**
 * @author HuSen
 * @since 2020/8/6 5:11 下午
 */
public interface ResourceReporter {

    /**
     * 上报资源
     *
     * @param resourceReportDTO 需要上报的资源
     * @return 是否上报成功
     */
    boolean reporting(ResourceReportDTO resourceReportDTO);
}
