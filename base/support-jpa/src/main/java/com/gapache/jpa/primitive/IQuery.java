package com.gapache.jpa.primitive;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Map;

/**
 * @author HuSen
 * @since 2021/1/29 11:30 上午
 */
public interface IQuery {

	/**
	 * 创建NativeQuery
	 *
	 * @return NativeQuery
	 */
	Query createNativeQuery(EntityManager entityManager);
	
	/**
	 * 设置查询参数
	 *
	 * @param query     	{@link Query}
	 * @param parameters 	参数
	 */
	Query setParameters(Query query, Map<String, Object> parameters);

	default Query build(EntityManager entityManager, Map<String, Object> parameters) {
		Query nativeQuery = createNativeQuery(entityManager);
		return setParameters(nativeQuery, parameters);
	}
}
