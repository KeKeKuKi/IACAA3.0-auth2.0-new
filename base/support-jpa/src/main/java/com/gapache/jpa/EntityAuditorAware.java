package com.gapache.jpa;

import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;

import java.util.Optional;

/**
 * @author HuSen
 * create on 2020/4/29 3:47 下午
 */
public class EntityAuditorAware implements AuditorAware<Long> {

    @NonNull
    @Override
    public Optional<Long> getCurrentAuditor() {
        return Optional.of(CurrentOpManHolder.getCurrent());
    }
}
