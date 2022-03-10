package com.gapache.security.loader;

import com.alibaba.fastjson.JSONObject;
import com.gapache.commons.model.JsonResult;
import com.gapache.security.interfaces.ClientLoader;
import com.gapache.security.model.ClientDTO;
import org.springframework.web.client.RestTemplate;

/**
 * @author HuSen
 * @since 2021/1/25 5:29 下午
 */
public class RemoteClientLoader implements ClientLoader {

    private final RestTemplate restTemplate;

    private final String serverName;

    public RemoteClientLoader(RestTemplate restTemplate, String serverName) {
        this.restTemplate = restTemplate;
        this.serverName = serverName;
    }

    @Override
    public ClientDTO load(String clientId) {
        @SuppressWarnings("rawtypes")
        JsonResult jsonResult = restTemplate.postForObject("http://" + serverName + "/api/client/findByClientId/" + clientId, null, JsonResult.class);
        if (jsonResult.getCode() == 0) {
            JSONObject data = (JSONObject) jsonResult.getData();
            return data.toJavaObject(ClientDTO.class);
        }
        return null;
    }
}
