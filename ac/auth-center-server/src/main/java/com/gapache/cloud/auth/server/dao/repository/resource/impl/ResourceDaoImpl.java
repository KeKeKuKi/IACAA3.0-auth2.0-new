package com.gapache.cloud.auth.server.dao.repository.resource.impl;

import com.gapache.cloud.auth.server.dao.entity.ResourceEntity;
import com.gapache.cloud.auth.server.dao.repository.resource.ResourceDao;
import com.gapache.commons.utils.IStringUtils;
import com.gapache.jpa.BaseJpaRepositoryBean;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author HuSen
 * @since 2021/1/26 4:55 下午
 */
@Slf4j
public class ResourceDaoImpl extends BaseJpaRepositoryBean<ResourceEntity, Long> implements ResourceDao {

    private final Map<String, String> sqlMap = new HashMap<>(4);

    public ResourceDaoImpl(EntityManager entityManager) {
        super(ResourceEntity.class, entityManager);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ResourceEntity> findAllResource(Long userId) {
        String sql = sqlMap.get("FindAllResourceByUserId");
        if (StringUtils.isBlank(sql)) {
            try {
                byte[] bytes = FileCopyUtils.copyToByteArray(new ClassPathResource("sql/FindAllResourceByUserId.sql").getInputStream());
                sql = IStringUtils.newString(bytes);
                sqlMap.put("FindAllResourceByUserId", sql);
            } catch (IOException e) {
                log.error("findAllResource error:{}", userId, e);
                return Lists.newArrayList();
            }
        }
        Query query = em.createNativeQuery(sql, ResourceEntity.class);
        query.setParameter("userId", userId);

        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ResourceEntity> findCustomizeAllResourceFromRid(Long roleId) {
        String sql = sqlMap.get("FindAllResourceByRoleId");
        if (StringUtils.isBlank(sql)) {
            try {
                byte[] bytes = FileCopyUtils.copyToByteArray(new ClassPathResource("sql/FindAllResourceByRoleId.sql").getInputStream());
                sql = IStringUtils.newString(bytes);
                sqlMap.put("FindAllResourceByRoleId", sql);
            } catch (IOException e) {
                log.error("findAllResourceCustomizeByRoleId error:{}", roleId, e);
                return Lists.newArrayList();
            }
        }
        Query query = em.createNativeQuery(sql, ResourceEntity.class);
        query.setParameter("roleId", roleId);

        return query.getResultList();
    }

    @Override
    public ResourceEntity findResourceCustomizeById(Long id) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("id", 1L);
        Query nativeQuery = com.gapache.cloud.auth.server.dao.sql.Query.FIND_ALL_RESOURCE
                .build(em, params);

        return (ResourceEntity) nativeQuery.getSingleResult();
    }
}
