package com.gapache.commons.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author HuSen
 * create on 2020/1/11 11:34
 */
@Data
public class JsonResult<T> implements Serializable {
    private static final long serialVersionUID = -1916466521535767957L;

    public static final int SUCCESS = 0;

    private int code;
    private String error;
    private T data;
    private long timestamp;

    public static <T> JsonResult<T> of(T d) {
        JsonResult<T> jsonResult = new JsonResult<>();
        jsonResult.setCode(SUCCESS);
        jsonResult.setTimestamp(System.currentTimeMillis());
        jsonResult.setData(d);
        return jsonResult;
    }

    public static <T> JsonResult<T> of(int code, String error) {
        JsonResult<T> jsonResult = new JsonResult<>();
        jsonResult.setCode(code);
        jsonResult.setError(error);
        jsonResult.setTimestamp(System.currentTimeMillis());
        return jsonResult;
    }

    public static <T> JsonResult<T> of(int code, String error, T d) {
        JsonResult<T> jsonResult = new JsonResult<>();
        jsonResult.setCode(code);
        jsonResult.setError(error);
        jsonResult.setTimestamp(System.currentTimeMillis());
        jsonResult.setData(d);
        return jsonResult;
    }

    public static <T> JsonResult<T> success() {
        JsonResult<T> jsonResult = new JsonResult<>();
        jsonResult.setCode(SUCCESS);
        jsonResult.setTimestamp(System.currentTimeMillis());
        return jsonResult;
    }

    public static <T> JsonResult<T> of(Error error) {
        JsonResult<T> jsonResult = new JsonResult<>();
        jsonResult.setCode(error.getCode());
        jsonResult.setError(error.getError());
        jsonResult.setTimestamp(System.currentTimeMillis());
        return jsonResult;
    }

    public boolean requestSuccess() {
        return this.code == SUCCESS;
    }
}
