package com.gapache.vertx.redis.support;

import com.gapache.vertx.redis.annotation.Id;
import com.gapache.vertx.redis.annotation.RedisEntity;
import com.google.common.collect.Lists;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author HuSen
 * @since 2021/3/8 11:12 上午
 */
public final class DescriptorUtils {

    private static final Map<Class<?>, RedisEntityDescriptor> DESCRIPTOR_MAP = new HashMap<>();

    public static RedisEntityDescriptor getDescriptor(Class<?> clazz) {
        if (DESCRIPTOR_MAP.containsKey(clazz)) {
            return DESCRIPTOR_MAP.get(clazz);
        }

        RedisEntity redisEntity = AnnotationUtils.findAnnotation(clazz, RedisEntity.class);
        Assert.notNull(redisEntity, clazz.getName() + " is not a redis entity!");

        Field[] fields = clazz.getDeclaredFields();
        Field.setAccessible(fields, true);

        Field idField = null;
        for (Field field : fields) {
            Id id = AnnotationUtils.findAnnotation(field, Id.class);
            if (id != null) {
                idField = field;
            }
        }

        Assert.notNull(idField, clazz.getName() + " is no id!");
        RedisEntityDescriptor descriptor = new RedisEntityDescriptor();
        descriptor.setIdField(idField);
        descriptor.setFields(Lists.newArrayList(fields));

        String key = redisEntity.value();
        if (StringUtils.hasText(key)) {
            descriptor.setKey(key);
        } else {
            descriptor.setKey(clazz.getName());
        }

        DESCRIPTOR_MAP.put(clazz, descriptor);
        return descriptor;
    }
}
