package com.gapache.vertx.redis.support;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author HuSen
 * @since 2021/3/8 11:07 上午
 */
@Slf4j
@Setter
@Getter
public class RedisEntityDescriptor {

    public static final String SPLIT_SIGN = ":";
    public static final String PAGE = "PAGE";

    private String key;
    private Field idField;
    private List<Field> fields;

    public Object getId(Object entity) {
        try {
            Assert.notNull(entity, "entity is null!");
            return this.idField.get(entity);
        } catch (Exception e) {
            log.error("getId error.", e);
        }
        return null;
    }

    public Object getValue(Object entity, Field field) {
        try {
            return field.get(entity);
        } catch (IllegalAccessException e) {
            log.error("getValue error.", e);
            return null;
        }
    }
}
