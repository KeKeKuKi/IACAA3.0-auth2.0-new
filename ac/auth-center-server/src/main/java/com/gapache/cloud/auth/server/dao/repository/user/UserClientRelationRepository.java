package com.gapache.cloud.auth.server.dao.repository.user;

import com.gapache.cloud.auth.server.dao.entity.UserClientRelationEntity;
import com.gapache.jpa.BaseJpaRepository;

/**
 * @author HuSen
 * @since 2020/8/3 9:08 上午
 */
public interface UserClientRelationRepository extends BaseJpaRepository<UserClientRelationEntity, Long> {

    UserClientRelationEntity findByUserIdAndClientId(Long userId, Long clientId);

    Boolean existsByUserIdAndClientId(Long userId, Long clientId);
}
