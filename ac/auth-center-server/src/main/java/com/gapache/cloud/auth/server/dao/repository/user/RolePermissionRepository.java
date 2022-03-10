package com.gapache.cloud.auth.server.dao.repository.user;

import com.gapache.cloud.auth.server.dao.entity.RolePermissionEntity;
import com.gapache.jpa.BaseJpaRepository;

import java.util.List;

/**
 * @author HuSen
 * @since 2021/1/26 9:49 上午
 */
public interface RolePermissionRepository extends BaseJpaRepository<RolePermissionEntity, Long> {

    List<RolePermissionEntity> findAllByRoleId(Long roleId);

    void deleteAllByRoleId(Long roleId);
}
