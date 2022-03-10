package com.gapache.cloud.bus;

import org.springframework.cloud.bus.jackson.RemoteApplicationEventScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author HuSen
 * @since 2021/3/29 2:16 下午
 */
@Configuration
@RemoteApplicationEventScan(basePackageClasses = BusConfiguration.class)
public class BusConfiguration {

}
