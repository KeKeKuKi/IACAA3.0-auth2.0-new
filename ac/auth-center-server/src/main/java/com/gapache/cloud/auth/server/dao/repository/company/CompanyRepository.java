package com.gapache.cloud.auth.server.dao.repository.company;

import com.gapache.cloud.auth.server.dao.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author HuSen
 * @since 2021/3/26 11:12 上午
 */
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {

    boolean existsByNameAndClientId(String name, String clientId);

    List<CompanyEntity> findAllByAboveId(Long aboveId);

    List<CompanyEntity> findAllByClientId(String clientId);
}
