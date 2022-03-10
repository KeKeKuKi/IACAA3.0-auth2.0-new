package com.gapache.security.core;

import com.alibaba.fastjson.JSON;
import com.gapache.security.interfaces.ParseTokenStrategy;
import com.gapache.security.model.AccessCard;
import com.gapache.security.model.AuthorizeInfoDTO;

/**
 * @author HuSen
 * @since 2020/8/6 1:57 下午
 */
public class JwtParseTokenStrategy implements ParseTokenStrategy {

    @Override
    public AccessCard parse(String content) {
        AuthorizeInfoDTO authorizeInfoDTO = JSON.parseObject(content, AuthorizeInfoDTO.class);
        return null;
    }
}
