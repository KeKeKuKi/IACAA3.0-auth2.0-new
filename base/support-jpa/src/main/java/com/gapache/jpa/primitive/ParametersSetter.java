package com.gapache.jpa.primitive;

import javax.persistence.Query;
import java.util.Map;

/**
 * 参数设置器
 *
 * @author HuSen
 * @since 2021/1/29 11:00 上午
 */
@FunctionalInterface
public interface ParametersSetter {

	/**
	 * 设置查询参数
	 *
	 * @param query     	{@link Query}
	 * @param parameters 	参数
	 */
	void setParameters(Query query, Map<String, Object> parameters);
}
