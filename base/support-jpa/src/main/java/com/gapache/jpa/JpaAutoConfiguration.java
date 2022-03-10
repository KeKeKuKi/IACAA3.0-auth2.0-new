package com.gapache.jpa;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author HuSen
 * create on 2020/4/29 3:47 下午
 */
@EnableJpaAuditing
@Configuration
public class JpaAutoConfiguration {

    @Bean
    public AuditorAware<Long> auditorAware() {
        return new EntityAuditorAware();
    }
}
