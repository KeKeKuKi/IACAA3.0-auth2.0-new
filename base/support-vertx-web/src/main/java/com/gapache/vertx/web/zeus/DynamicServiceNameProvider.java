package com.gapache.vertx.web.zeus;

/**
 * 动态获取ServiceName
 *
 * @author HuSen
 * @since 2021/3/3 5:44 下午
 */
public interface DynamicServiceNameProvider {

    /**
     * 获取ServiceName
     *
     * @return ServiceName
     */
    String getServiceName();

    /**
     * ip
     *
     * @return 指定Ip
     */
    default String ip() {
        return null;
    }

    /**
     * port
     *
     * @return 指定端口
     */
    default int port() {
        return 0;
    }
}
