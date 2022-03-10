package com.gapache.security.interfaces;

import com.gapache.security.model.ResourceReportDTO;

/**
 * @author HuSen
 * @since 2020/8/6 6:04 下午
 */
public interface ResourceReceiver {

    /**
     * 接收上报的资源
     *
     * @param reportData 上报的数据
     * @return 上报结果
     */
    Boolean receiveReportData(ResourceReportDTO reportData);
}
