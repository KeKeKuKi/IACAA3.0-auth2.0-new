package com.gapache.cloud.auth.server.dao.repository.position;

import com.gapache.cloud.auth.server.dao.entity.UserPositionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

/**
 * @author HuSen
 * @since 2021/3/26 2:18 下午
 */
public interface UserPositionRepository extends JpaRepository<UserPositionEntity, Long> {

    UserPositionEntity findByUserIdAndPositionId(Long userId, Long positionId);

    List<UserPositionEntity> findAllByUserId(Long userId);

    List<UserPositionEntity> findAllByPositionId(Long positionId);

    List<UserPositionEntity> findAllByPositionIdIn(Collection<Long> positionIds);

    @Query("FROM UserPositionEntity up LEFT JOIN PositionEntity p ON up.positionId = p.id WHERE up.userId = ?1 AND p.companyId = ?2")
    List<UserPositionEntity> findAllByUserIdAndCompanyId(Long userId, Long companyId);
}
