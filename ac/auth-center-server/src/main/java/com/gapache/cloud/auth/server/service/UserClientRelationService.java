package com.gapache.cloud.auth.server.service;

import com.gapache.cloud.auth.server.model.UserClientRelationDTO;

/**
 * @author HuSen
 * @since 2020/8/3 9:08 上午
 */
public interface UserClientRelationService {

    UserClientRelationDTO findByUserIdAndClientId(Long userId, Long clientId);

    Boolean create(UserClientRelationDTO userClientRelationDTO);
}
