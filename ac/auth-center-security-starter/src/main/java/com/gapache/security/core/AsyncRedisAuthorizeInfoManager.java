package com.gapache.security.core;

import com.alibaba.fastjson.JSON;
import com.gapache.security.entity.AuthorizeInfoEntity;
import com.gapache.security.interfaces.AsyncAuthorizeInfoManager;
import com.gapache.security.model.AuthorizeInfoDTO;
import com.gapache.security.model.CustomerInfo;
import com.gapache.vertx.redis.support.SimpleRedisRepository;
import com.gapache.vertx.redis.support.SuccessType;
import io.vertx.core.Future;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author HuSen
 * @since 2021/3/17 8:01 下午
 */
public class AsyncRedisAuthorizeInfoManager implements AsyncAuthorizeInfoManager {

    private final SimpleRedisRepository simpleRedisRepository;

    public AsyncRedisAuthorizeInfoManager(SimpleRedisRepository simpleRedisRepository) {
        this.simpleRedisRepository = simpleRedisRepository;
    }

    @Override
    public Future<Void> save(String token, Long timeout, CustomerInfo customerInfo, Collection<String> scopes) {
        AuthorizeInfoEntity entity = new AuthorizeInfoEntity();
        entity.setId(token);
        entity.setScopes(CollectionUtils.isNotEmpty(scopes) ? String.join(",", scopes) : "");
        entity.setCustomerInfo(JSON.toJSONString(customerInfo));
        return Future.future(event -> simpleRedisRepository.save(entity, timeout)
                .onSuccess(response -> {
                    if (SuccessType.SET_OK.success(response)) {
                        event.complete();
                    } else {
                        event.fail("保存失败");
                    }
                })
                .onFailure(event::fail));
    }

    @Override
    public Future<Void> delete(String token) {
        AuthorizeInfoEntity entity = new AuthorizeInfoEntity();
        entity.setId(token);
        return Future.future(event -> simpleRedisRepository.delete(entity)
                .onSuccess(response -> {
                    if (SuccessType.DEL_OK.success(response)) {
                        event.complete();
                    } else {
                        event.fail("删除失败");
                    }
                })
                .onFailure(event::fail));
    }

    @Override
    public Future<AuthorizeInfoDTO> get(String token) {
        return Future.future(event -> simpleRedisRepository.findById(token, AuthorizeInfoEntity.class)
                .onSuccess(entity -> {
                    if (entity.getId() == null) {
                        event.complete();
                    } else {
                        AuthorizeInfoDTO dto = new AuthorizeInfoDTO();
                        dto.setScopes(Arrays.stream(entity.getScopes().split(",")).collect(Collectors.toList()));
                        dto.setCustomerInfo(JSON.parseObject(entity.getCustomerInfo(), CustomerInfo.class));
                        event.complete(dto);
                    }
                })
                .onFailure(event::fail));
    }
}
