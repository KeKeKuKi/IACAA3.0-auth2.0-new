package com.gapache.commons.utils;

import com.google.common.collect.Sets;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author HuSen
 * @since 2020/9/24 7:27 下午
 */
public class BeanCopyUtils extends BeanUtils {

    public static void copyIgnoreProperties(Object source, Object target) {
        Field[] declaredFields = source.getClass().getDeclaredFields();
        Field.setAccessible(declaredFields, true);
        String[] ignoreProperties = Arrays.stream(declaredFields).filter(field -> {
            try {
                return field.get(source) != null;
            } catch (Exception e) {
                return false;
            }
        }).map(Field::getName).toArray(String[]::new);
        copyProperties(source, target, ignoreProperties);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void populate(Map<String, ?> source, Object target, String... excludes) {
        try {
            // 如果是Map
            if (target instanceof Map) {
                Map targetMap = (Map) target;
                Set<String> sourceKey = source.keySet();
                HashSet<String> excludesSets = excludes != null && excludes.length > 0 ? Sets.newHashSet(excludes) : new HashSet<>(0);
                sourceKey.forEach(key -> {
                    if (!excludesSets.contains(key) && targetMap.containsKey(key)) {
                        targetMap.put(key, source.get(key));
                    }
                });
            } else {
                org.apache.commons.beanutils.BeanUtils.populate(target, source);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Map<String, String> source = new HashMap<>(2);
        source.put("a", "a");
        source.put("b", "b");

        Map<String, String> target = new HashMap<>(2);
        target.put("a", "");

        populate(source, target);

        System.out.println(target);
    }
}
