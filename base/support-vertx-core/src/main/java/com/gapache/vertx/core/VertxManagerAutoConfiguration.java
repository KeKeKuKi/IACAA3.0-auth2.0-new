package com.gapache.vertx.core;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author HuSen
 * @since 2021/3/1 3:01 下午
 */
@Configuration
@EnableConfigurationProperties(VertxSettings.class)
public class VertxManagerAutoConfiguration {

    @Bean
    @ConditionalOnProperty(value = "com.gapache.vertx.settings.cluster", havingValue = "true", matchIfMissing = true)
    public VertxManager vertxManager(VertxSettings vertxSettings, ApplicationContext applicationContext) {
        VertxManager vertxManager = new VertxManager();
        vertxManager.createCluster(vertxSettings, applicationContext);
        return vertxManager;
    }

    @Bean
    @ConditionalOnProperty(value = "com.gapache.vertx.settings.cluster", havingValue = "false")
    public VertxManager vertxManagerStandalone(VertxSettings vertxSettings, ApplicationContext applicationContext) {
        VertxManager vertxManager = new VertxManager();
        vertxManager.createStandalone(vertxSettings, applicationContext);
        return vertxManager;
    }
}
