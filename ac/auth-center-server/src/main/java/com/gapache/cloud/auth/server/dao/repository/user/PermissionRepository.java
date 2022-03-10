package com.gapache.cloud.auth.server.dao.repository.user;

import com.gapache.cloud.auth.server.dao.entity.PermissionEntity;
import com.gapache.jpa.BaseJpaRepository;

import java.util.Collection;
import java.util.List;

/**
 * @author HuSen
 * @since 2021/1/26 9:47 上午
 */
public interface PermissionRepository extends BaseJpaRepository<PermissionEntity, Long> {

    List<PermissionEntity> findAllByResourceIdIn(Collection<Long> resourceIds);
}
