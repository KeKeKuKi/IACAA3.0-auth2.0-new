package com.gapache.security.event;

import com.alibaba.fastjson.JSON;
import com.gapache.commons.model.AuthConstants;
import com.gapache.security.model.AuthorizeInfoDTO;
import com.gapache.security.model.CustomerInfo;
import com.gapache.vertx.core.VertxManager;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

/**
 * @author HuSen
 * @since 2021/3/25 11:49 上午
 */
@Slf4j
public class EventSender {

    public void send(Long userId, String customizeInfo, Collection<String> scopes) {
        JsonObject event = new JsonObject();
        event.put("userId", userId);
        AuthorizeInfoDTO authorizeInfoDTO = new AuthorizeInfoDTO();
        authorizeInfoDTO.setCustomerInfo(JSON.parseObject(customizeInfo, CustomerInfo.class));
        authorizeInfoDTO.setScopes(scopes);
        event.put("authorizeInfo", JSON.toJSONString(authorizeInfoDTO));
        log.info("send event {}", event);
        VertxManager.getVertx().eventBus().send(AuthConstants.EventBusAddress.UPDATE_AUTHORIZE_INFO_ADDRESS, event);
    }
}
