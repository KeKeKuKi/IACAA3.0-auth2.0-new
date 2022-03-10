package com.gapache.jpa.primitive;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Map;

/**
 * @author HuSen
 * @since 2021/1/29 11:32 上午
 */
public enum Demo implements IQuery{;
	
	private final Class<?> entityClass;
	private final SqlLoader sqlLoader;
	private final ParametersSetter parametersSetter;

	Demo(Class<?> entityClass, SqlLoader sqlLoader, ParametersSetter parametersSetter) {
		this.entityClass = entityClass;
		this.sqlLoader = sqlLoader;
		this.parametersSetter = parametersSetter;
	}
	
	@Override
	public Query createNativeQuery(EntityManager entityManager) {
		return entityManager.createNativeQuery(sqlLoader.loading(), entityClass);
	}
	
	@Override
	public Query setParameters(Query query, Map<String, Object> parameters) {
		this.parametersSetter.setParameters(query, parameters);
		return query;
	}
}
