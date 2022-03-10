package com.gapache.commons.model;

import java.util.Map;

/**
 * 参数异常
 *
 * @author HuSen.
 * @date 2018/11/28 16:32.
 */
public class ParamException extends RuntimeException {
    private static final long serialVersionUID = -878270520763922880L;

    private final Map<String, StringBuilder> errors;

    public ParamException(Map<String, StringBuilder> errors) {
        super("参数错误", null, false, false);
        this.errors = errors;
    }

    public Map<String, StringBuilder> getErrors() {
        return errors;
    }
}
