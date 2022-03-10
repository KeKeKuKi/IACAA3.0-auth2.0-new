package com.gapache.sentinel.config;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.exception.NacosException;
import com.gapache.sentinel.DegradeRuleHelper;
import com.gapache.sentinel.ProtectingCaller;
import com.gapache.sentinel.handler.GlobalExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author HuSen
 * @since 2021/3/11 9:38 上午
 */
@Slf4j
@Configuration
public class SentinelAutoConfiguration implements ApplicationListener<ApplicationStartedEvent> {

    @Override
    public void onApplicationEvent(@NonNull ApplicationStartedEvent event) {
        ConfigurableApplicationContext context = event.getApplicationContext();
        List<DegradeRule> configs = new ArrayList<>();
        // 保护服务自身
        protectingSelf(context, configs);
        // 保护调用方
        protectingCaller(context, configs);

        // try save config
        if (CollectionUtils.isEmpty(configs)) {
            return;
        }

        try {
            ConfigService configService = checkNacosConfigService(context);
            String dataId = context.getEnvironment().getProperty("spring.cloud.sentinel.datasource.degrade.nacos.dataId");
            String groupId = context.getEnvironment().getProperty("spring.cloud.sentinel.datasource.degrade.nacos.groupId");
            log.info(">>>>>> degrade config dataId:{}, groupId:{}", dataId, groupId);
            String config = configService.getConfig(dataId, groupId, 20000);
            if (StringUtils.isBlank(config)) {
                boolean result = configService.publishConfig(dataId, groupId, JSON.toJSONString(configs, true), ConfigType.JSON.getType());
                if (!result) {
                    log.warn("publish degrade config fail");
                }
                return;
            }

            List<DegradeRule> degradeRules = JSONArray.parseArray(config, DegradeRule.class);
            int size = degradeRules.size();
            Map<String, DegradeRule> oldMap = degradeRules.stream().collect(Collectors.toMap(DegradeRule::getResource, r -> r));
            // 不动旧的，只新增
            for (DegradeRule rule : configs) {
                if (oldMap.containsKey(rule.getResource())) {
                    continue;
                }
                degradeRules.add(rule);
            }

            if (degradeRules.size() != size) {
                boolean result = configService.publishConfig(dataId, groupId, JSON.toJSONString(degradeRules, true), ConfigType.JSON.getType());
                if (!result) {
                    log.warn("publish degrade config fail");
                }
            }
        } catch (NacosException e) {
            log.error("check checkNacosConfigService error.", e);
        }
    }

    private void protectingCaller(ConfigurableApplicationContext context, List<DegradeRule> configs) {
        // 委托给具体的实现
        try {
            ProtectingCaller protectingCaller = context.getBean(ProtectingCaller.class);
            configs.addAll(protectingCaller.init(context));
        } catch (Exception e) {
            log.warn("cannot found ProtectingCaller, the caller is danger!!!{}", e.getMessage());
        }
    }

    private void protectingSelf(ConfigurableApplicationContext context, List<DegradeRule> configs) {
        // 获取所有的RestController和Controller
        Map<String, Object> controllers = context.getBeansWithAnnotation(Controller.class);

        // 扫描自动生成对应的规则
        scanRule(configs, controllers);
    }

    private ConfigService checkNacosConfigService(ApplicationContext context) throws NacosException {
        try {
            return context.getBean(ConfigService.class);
        } catch (Exception e) {
            log.warn("load NacosConfigService in context fail, try to init it by config");
            Environment environment = context.getEnvironment();
            String serverAddr = environment.getProperty("spring.cloud.sentinel.datasource.degrade.nacos.server-addr");
            return NacosFactory.createConfigService(serverAddr);
        }
    }

    private void scanRule(List<DegradeRule> configs, Map<String, Object> controllers) {
        if (MapUtils.isEmpty(controllers)) {
            return;
        }

        Set<String> checker = new HashSet<>();

        controllers.forEach((name, bean) -> {
            Method[] methods = bean.getClass().getDeclaredMethods();
            Method.setAccessible(methods, true);
            for (Method method : methods) {
                SentinelResource sentinelResource = AnnotationUtils.findAnnotation(method, SentinelResource.class);
                if (sentinelResource == null) {
                    continue;
                }
                String value = sentinelResource.value();
                if (StringUtils.isBlank(value)) {
                    throw new IllegalStateException(value + " is blank");
                }
                if (checker.contains(value)) {
                    throw new IllegalStateException(value + " is repeated");
                }
                checker.add(value);
                // 慢调用比例：当资源的响应时间超过最大RT（以ms为单位，最大RT即最大响应时间）之后，资源进入准降级状态。
                // 如果接下来1s内持续进入5个请求（最小请求数），它们的RT都持续超过这个阈值，那么在接下来的熔断时长之内，就会对这个方法进行服务降级。

                configs.add(DegradeRuleHelper.createDefault(value));
            }
        });
    }



    @Bean
    public BlockExceptionHandler blockExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    @Bean
    public RequestOriginParser requestOriginParser() {
        return new RequestOriginParserDefinition();
    }

    static class RequestOriginParserDefinition implements RequestOriginParser {

        @Override
        public String parseOrigin(HttpServletRequest httpServletRequest) {
            return httpServletRequest.getParameter("serviceName");
        }
    }
}
