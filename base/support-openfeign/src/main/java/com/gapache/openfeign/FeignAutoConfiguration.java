package com.gapache.openfeign;

import com.gapache.commons.helper.AccessCardHeaderHolder;
import com.gapache.commons.model.AuthConstants;
//import com.gapache.sentinel.ProtectingCaller;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Feign传递身份凭证请求头
 *
 * @author HuSen
 * @since 2021/2/1 3:11 下午
 */
@Configuration
public class FeignAutoConfiguration implements RequestInterceptor {

    @Value("${spring.application.name}")
    private String applicationName;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String header = AccessCardHeaderHolder.getHeader();
        if (StringUtils.isNotBlank(header)) {
            requestTemplate.header(AuthConstants.ACCESS_CARD_HEADER, header);
        }
        requestTemplate.header(AuthConstants.X_FROM_HEADER, applicationName);
    }

//    @Bean
//    @ConditionalOnMissingBean
//    public ProtectingCaller protectingCaller() {
//        return new FeignProtectingCaller();
//    }
}
