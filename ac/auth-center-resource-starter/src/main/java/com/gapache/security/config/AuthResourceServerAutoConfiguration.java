package com.gapache.security.config;

import com.gapache.security.annotation.AuthResource;
import com.gapache.security.annotation.EnableAuthResourceServer;
import com.gapache.security.annotation.NeedAuth;
import com.gapache.security.aspect.PreAuthResourceAspect;
import com.gapache.security.cache.AuthResourceCache;
import com.gapache.security.interfaces.ClientLoader;
import com.gapache.security.interfaces.ResourceReceiver;
import com.gapache.security.interfaces.ResourceReporter;
import com.gapache.security.loader.RemoteClientLoader;
import com.gapache.security.model.AuthResourceDTO;
import com.gapache.security.model.ResourceReportDTO;
import com.gapache.security.properties.SecurityProperties;
import com.gapache.security.reporter.LocalResourceReporter;
import com.gapache.security.reporter.RemoteResourceReporter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.util.Assert;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author HuSen
 * @since 2020/8/6 4:15 下午
 */
@Slf4j
@Configuration
@ConditionalOnBean(annotation = EnableAuthResourceServer.class)
@ServletComponentScan("com.gapache.security.filter")
@Import(PreAuthResourceAspect.class)
@EnableConfigurationProperties(SecurityProperties.class)
public class AuthResourceServerAutoConfiguration implements InitializingBean {

    private final static String AC = "auth-center-server";

    @Resource
    private SecurityProperties securityProperties;

    @Override
    public void afterPropertiesSet() {
        log.info("auto setting AuthResourceServer's config");
    }

    @EventListener(ApplicationReadyEvent.class)
    public void listening(ApplicationReadyEvent readyEvent) {
        log.info("AuthResourceServerAutoConfiguration listening event:{}", readyEvent);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Map<String, Object> enableAuthResourceServerMap = readyEvent.getApplicationContext().getBeansWithAnnotation(EnableAuthResourceServer.class);
        if (!enableAuthResourceServerMap.values().iterator().hasNext()) {
            return;
        }
        Object firstFind = enableAuthResourceServerMap.values().iterator().next();
        EnableAuthResourceServer enableAuthResourceServer = AnnotationUtils.findAnnotation(firstFind.getClass(), EnableAuthResourceServer.class);
        Assert.notNull(enableAuthResourceServer, "not found @Annotation EnableAuthResourceServer");
        String resourceServerName = enableAuthResourceServer.value();

        Map<String, Object> needAuthMap = readyEvent.getApplicationContext().getBeansWithAnnotation(NeedAuth.class);
        if (MapUtils.isEmpty(needAuthMap)) {
            log.warn("not found NeedAuth");
            return;
        }
        Set<String> repeatChecker = new HashSet<>(needAuthMap.size() * 4);

        needAuthMap.forEach((beanName, bean) -> {
            NeedAuth needAuth = AnnotationUtils.findAnnotation(bean.getClass(), NeedAuth.class);
            Assert.notNull(needAuth, "not found @Annotation NeedAuth");
            String category = needAuth.value();
            log.info("find need auth component:{}, {}, {}", category, beanName, bean);
            Class<?> aClass = bean.getClass();
            while (aClass != Object.class) {
                Method[] declaredMethods = aClass.getDeclaredMethods();
                Method.setAccessible(declaredMethods, true);
                for (Method method : declaredMethods) {
                    AuthResource authResource = AnnotationUtils.getAnnotation(method, AuthResource.class);
                    if (authResource == null) {
                        continue;
                    }
                    if (repeatChecker.contains(category + authResource.scope())) {
                        throw new RuntimeException("duplicate scope:" + authResource.scope());
                    }
                    log.info("find auth resource:{}", authResource);
                    AuthResourceCache.put(resourceServerName, category, authResource);
                    repeatChecker.add(category + authResource.scope());
                }
                aClass = aClass.getSuperclass();
            }
        });

        Boolean updateResources = securityProperties.getUpdateResources();
        if (!updateResources) {
            log.info("$$------AuthResource------not update resources");
            return;
        }

        // ResourceReporter
        ResourceReporter reporter = readyEvent.getApplicationContext().getBean(ResourceReporter.class);
        // data
        List<AuthResourceDTO> reportData = new ArrayList<>();
        // checks
        Set<String> categories = AuthResourceCache.categories();
        for (String category : categories) {
            List<AuthResource> authResources = AuthResourceCache.check(category);
            for (AuthResource authResource : authResources) {
                AuthResourceDTO dto = new AuthResourceDTO();
                dto.setName(authResource.name());
                dto.setScope(category + ":" + authResource.scope());
                reportData.add(dto);
                log.info("$$------AuthResource------{}", dto);
            }
        }

        ResourceReportDTO resourceReportDTO = new ResourceReportDTO();
        resourceReportDTO.setResourceServerName(resourceServerName);
        resourceReportDTO.setAuthResources(reportData);
        resourceReportDTO.setClientId(securityProperties.getClientId());
        boolean reportingResult = reporter.reporting(resourceReportDTO);
        log.info("reporting resource finish, result:{}, cost time:{}", reportingResult, stopWatch.getTotalTimeMillis() + "ms");
        if (!reportingResult) {
            throw new RuntimeException("reporting resource fail, please check");
        }
    }

    @Order(1)
    @Configuration
    @ConditionalOnProperty(value = "com.gapache.security.register-remote", havingValue = "true")
    static class ImportAuthFeignAutoConfiguration implements InitializingBean {

        @Bean
        @ConditionalOnMissingBean
        public ResourceReporter resourceReporter(RestTemplate restTemplate) {
            // 可以不用feign的
            return new RemoteResourceReporter(AC, restTemplate);
        }

        @Override
        public void afterPropertiesSet() {
            log.info("enable remote register to Auth-Center-Server");
        }
    }

    @Order(2)
    @Configuration
    @ConditionalOnProperty(value = "com.gapache.security.register-remote", havingValue = "false", matchIfMissing = true)
    static class LocalResourceReporterAutoConfiguration implements InitializingBean {

        @Override
        public void afterPropertiesSet() {
            log.info("enable register to self[Auth-Center-Server]");
        }

        @Bean
        @ConditionalOnMissingBean
        public ResourceReporter resourceReporter(ApplicationContext applicationContext) {
            return new LocalResourceReporter(applicationContext.getBean(ResourceReceiver.class));
        }
    }

    @Order(3)
    @Configuration
    static class RemotePublicKeyLoaderAutoConfiguration {

        @Bean
        @LoadBalanced
        @ConditionalOnMissingBean
        public RestTemplate cloudRestTemplate() {
            log.info("cloudRestTemplate");
            return new RestTemplate();
        }

        @Bean
        @ConditionalOnMissingBean
        public ClientLoader publicKeyLoader(RestTemplate restTemplate) {
            return new RemoteClientLoader(restTemplate, AC);
        }
    }
}
