package com.gapache.security.checker.impl;

import com.alibaba.fastjson.JSON;
import com.gapache.security.checker.AsyncSecurityChecker;
import com.gapache.security.entity.RoleScopesEntity;
import com.gapache.security.interfaces.AsyncAuthorizeInfoManager;
import com.gapache.security.model.AccessCard;
import com.gapache.security.model.impl.CertificationImpl;
import com.gapache.security.utils.JwtUtils;
import com.gapache.vertx.redis.support.SimpleRedisRepository;
import io.vertx.core.Future;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.security.PublicKey;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author HuSen
 * @since 2020/7/31 12:45 下午
 */
@Slf4j
public class LocalAsyncSecurityChecker implements AsyncSecurityChecker {

    private final PublicKey publicKey;
    private final AsyncAuthorizeInfoManager asyncAuthorizeInfoManager;
    private final SimpleRedisRepository simpleRedisRepository;

    public LocalAsyncSecurityChecker(PublicKey publicKey, AsyncAuthorizeInfoManager asyncAuthorizeInfoManager, SimpleRedisRepository simpleRedisRepository) {
        this.publicKey = publicKey;
        this.asyncAuthorizeInfoManager = asyncAuthorizeInfoManager;
        this.simpleRedisRepository = simpleRedisRepository;
    }

    @Override
    public Future<AccessCard> checking(String token) {
        // 先解析并检查token
        return Future.future(event -> {
            try {
                String content = JwtUtils.parseToken(token, publicKey);
                log.info("parse token result:{}", content);
                if (content == null) {
                    event.complete(null);
                    return;
                }
                CertificationImpl certification = JSON.parseObject(content, CertificationImpl.class);
                Long uniqueId = certification.getUniqueId();
                String name = certification.getName();
                AccessCard accessCard = new AccessCard();
                accessCard.setUserId(uniqueId);
                accessCard.setUsername(name);
                accessCard.setClientId(certification.getClientId());
                accessCard.setSign(certification.getSign());

                asyncAuthorizeInfoManager.get(token)
                        .onSuccess(dto -> {
                            dto.getScopes().removeIf(StringUtils::isBlank);
                            accessCard.setAuthorities(new HashSet<>(dto.getScopes()));
                            accessCard.setCustomerInfo(dto.getCustomerInfo());
                            Long roleId = certification.getRoleId();
                            if (roleId != null) {
                                simpleRedisRepository.findById(roleId.toString(), RoleScopesEntity.class)
                                        .onSuccess(roleScopes -> {
                                            if (StringUtils.isNotBlank(roleScopes.getScopes())) {
                                                List<String> collect = Arrays.stream(roleScopes.getScopes().split(",")).filter(StringUtils::isNotBlank)
                                                        .collect(Collectors.toList());
                                                accessCard.getAuthorities().addAll(collect);
                                            }
                                            event.complete(accessCard);
                                        })
                                        .onFailure(error -> {
                                            log.error(">>>>>> get role scopes fail.", error);
                                            event.complete(accessCard);
                                        });
                            } else {
                                event.complete(accessCard);
                            }
                        })
                        .onFailure(event::fail);


            } catch (Exception e) {
                log.error("check token error.", e);
                event.fail(e);
            }
        });
    }
}
