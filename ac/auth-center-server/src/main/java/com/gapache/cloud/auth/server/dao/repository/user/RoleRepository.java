package com.gapache.cloud.auth.server.dao.repository.user;

import com.gapache.cloud.auth.server.dao.entity.RoleEntity;
import com.gapache.jpa.BaseJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author HuSen
 * @since 2021/1/26 9:47 上午
 */
public interface RoleRepository extends BaseJpaRepository<RoleEntity, Long> {

    RoleEntity findByName(String name);

    List<RoleEntity> findAllByNameLike(String name);

    List<RoleEntity> findAllByGroupIdAndIdNotAndNameLike(Long groupId, Long roleId, String name);

    @Query("FROM RoleEntity R WHERE R.id = (SELECT UR.roleId FROM UserRoleEntity UR WHERE UR.userId = ?1)")
    RoleEntity findByUserId(Long userId);

    Page<RoleEntity> findAllByGroupIdAndIdNot(Long group, Long roleId, Pageable pageable);

    List<RoleEntity> findAllByGroupIdAndIdIsNot(Long group, Long roleId);

    boolean existsByIdAndGroupIdAndIsManager(Long id, Long groupId, Boolean isManager);
}
