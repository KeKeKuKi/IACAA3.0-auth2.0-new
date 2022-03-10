package com.gapache.security.checker.impl;

import com.gapache.security.checker.AsyncSecurityChecker;
import com.gapache.security.checker.SecurityChecker;
import com.gapache.security.model.AccessCard;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author HuSen
 * @since 2020/7/31 12:45 下午
 */
@Slf4j
public class LocalSecurityChecker implements SecurityChecker {

    private final AsyncSecurityChecker asyncSecurityChecker;

    public LocalSecurityChecker(AsyncSecurityChecker asyncSecurityChecker) {
        this.asyncSecurityChecker = asyncSecurityChecker;
    }

    @Override
    public AccessCard checking(String token) {
        AtomicReference<AccessCard> accessCard = new AtomicReference<>();
        AtomicBoolean wait = new AtomicBoolean(false);
        asyncSecurityChecker.checking(token)
                .onSuccess(card -> {
                    wait.set(true);
                    accessCard.set(card);
                })
                .onFailure(error -> {
                    wait.set(true);
                    log.error("checker AccessCard fail, token:{}.", token, error);
                });
        while (!wait.get()) {
            try {
                TimeUnit.MILLISECONDS.sleep(1);
            } catch (InterruptedException ignored) {

            }
        }

        return accessCard.get();
    }
}
