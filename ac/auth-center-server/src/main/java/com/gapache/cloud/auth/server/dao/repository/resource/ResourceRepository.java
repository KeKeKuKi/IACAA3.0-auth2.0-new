package com.gapache.cloud.auth.server.dao.repository.resource;

import com.gapache.cloud.auth.server.dao.entity.ResourceEntity;
import com.gapache.jpa.BaseJpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author HuSen
 * @since 2020/8/6 6:03 下午
 */
public interface ResourceRepository extends BaseJpaRepository<ResourceEntity, Long>, ResourceDao {

    /**
     * 根据资源服务名查询所有
     *
     * @param resourceServerId 资源服务ID
     * @return 资源
     */
    List<ResourceEntity> findAllByResourceServerId(Long resourceServerId);

    @Query(nativeQuery = true, value = "select * from tb_resource")
    List<ResourceEntity> findAllCustomize();
}
