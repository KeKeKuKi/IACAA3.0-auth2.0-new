package com.gapache.cloud.auth.server.dao.sql;

import com.gapache.cloud.auth.server.dao.entity.ResourceEntity;
import com.gapache.jpa.primitive.IQuery;
import com.gapache.jpa.primitive.ParametersSetter;
import com.gapache.jpa.primitive.SqlLoader;
import com.gapache.jpa.primitive.impl.ClasspathSqlLoader;

import javax.persistence.EntityManager;
import java.util.Map;

/**
 * @author HuSen
 * @since 2021/1/29 11:30 上午
 */
public enum Query implements IQuery {
    FIND_ALL_RESOURCE(ResourceEntity.class, new ClasspathSqlLoader("sql/FindAllResourceById.sql"), ((query, parameters) -> {
        query.setParameter("id", parameters.get("id"));
    }));

    private final Class<?> entityClass;
    private final SqlLoader sqlLoader;
    private final ParametersSetter parametersSetter;

    Query(Class<?> entityClass, SqlLoader sqlLoader, ParametersSetter parametersSetter) {
        this.entityClass = entityClass;
        this.sqlLoader = sqlLoader;
        this.parametersSetter = parametersSetter;
    }

    @Override
    public javax.persistence.Query createNativeQuery(EntityManager entityManager) {
        return entityManager.createNativeQuery(sqlLoader.loading(), entityClass);
    }

    @Override
    public javax.persistence.Query setParameters(javax.persistence.Query query, Map<String, Object> parameters) {
        parametersSetter.setParameters(query, parameters);
        return query;
    }
}
