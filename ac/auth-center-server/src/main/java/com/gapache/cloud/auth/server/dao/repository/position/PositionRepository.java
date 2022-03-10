package com.gapache.cloud.auth.server.dao.repository.position;

import com.gapache.cloud.auth.server.dao.entity.PositionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

/**
 * @author HuSen
 * @since 2021/3/25 4:49 下午
 */
public interface PositionRepository extends JpaRepository<PositionEntity, Long> {

    boolean existsByName(String name);

    List<PositionEntity> findAllByAboveId(Long aboveId);

    List<PositionEntity> findAllByCompanyId(Long companyId);

    List<PositionEntity> findAllByCompanyIdIn(Collection<Long> companyIds);

    @Query(nativeQuery = true, value = "SELECT tp.* FROM `tb_position` tp LEFT JOIN tb_user_position tup ON tp.id = tup.position_id WHERE tp.company_id = ?1 AND tup.user_id = ?2")
    List<PositionEntity> findAllByCompanyIdAndUserPosition(Long companyId, Long userId);
}
