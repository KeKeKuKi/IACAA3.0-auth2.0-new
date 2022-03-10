package com.gapache.web.utils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * 实体数据校验工具
 *
 * @author HuSen
 * @date 2019/01/29
 */
public class ValidatorUtil {

    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    public static class Then<T> {
        private final T t;

        public Then(T t) {
            this.t = t;
        }

        public Then<T> then(Consumer<T> consumer) {
            consumer.accept(t);
            return new Then<>(t);
        }

        public void end() {}
    }

    /**
     * 实体数据校验
     *
     * @param obj 实体
     * @return 校验错误信息
     */
    public static <T> Then<Map<String, StringBuilder>> validate(T obj) {
        Map<String, StringBuilder> errorMap = null;
        Set<ConstraintViolation<T>> set = VALIDATOR.validate(obj, Default.class);
        return new Then<>(getErrorMap(errorMap, set));
    }

    private static <T> Map<String, StringBuilder> getErrorMap(Map<String, StringBuilder> errorMap, Set<ConstraintViolation<T>> set) {
        StringBuilder sb;
        if (set != null && set.size() > 0) {
            errorMap = new HashMap<>(set.size());
            String property;
            for (ConstraintViolation<T> cv : set) {
                property = cv.getPropertyPath().toString();
                if (errorMap.get(property) != null) {
                    errorMap.get(property).append(",").append(cv.getMessage());
                } else {
                    sb = new StringBuilder();
                    sb.append(cv.getMessage());
                    errorMap.put(property, sb);
                }
            }
        }
        return errorMap;
    }
}
