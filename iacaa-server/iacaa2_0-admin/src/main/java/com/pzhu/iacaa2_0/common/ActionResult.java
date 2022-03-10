package com.pzhu.iacaa2_0.common;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Data;

import java.io.Serializable;

@Data
public class ActionResult<T> implements Serializable {

    private static final long serialVersionUID = -3818243560898320822L;

    /**
     * 是否成功
     */
    private Boolean succ;

    /**
     * 服务器当前时间戳
     */
    private Long ts = System.currentTimeMillis();

    /**
     * 成功数据
     */
    private T data;

    /**
     * 错误码
     */
    private int code;

    /**
     * 错误描述
     */
    private String msg;

    public static ActionResult ofSuccess() {
        ActionResult result = new ActionResult();
        result.succ = true;
        return result;
    }

    public static ActionResult ofSuccess(Object data) {
        ActionResult result = new ActionResult();
        result.succ = true;
        result.setData(data);
        result.setCode(200);
        return result;
    }

    public static ActionResult ofFail(int code, String msg) {
        ActionResult result = new ActionResult();
        result.succ = false;
        result.code = code;
        result.msg = msg;
        return result;
    }

    public static ActionResult ofFail(int code, String msg, Object data) {
        ActionResult result = new ActionResult();
        result.succ = false;
        result.code = code;
        result.msg = msg;
        result.setData(data);
        return result;
    }

    public static ActionResult ofFail(CommonErrorCode resultEnum) {
        ActionResult result = new ActionResult();
        result.succ = false;
        result.code = resultEnum.getCode();
        result.msg = resultEnum.getMsg();
        return result;
    }

    public static ActionResult ofFail(String message) {
        ActionResult result = new ActionResult();
        result.succ = false;
        result.code = CommonErrorCode.DB_SERVICE_UNKNOWN_ERROR.getCode();
        result.msg = message;
        return result;
    }

    /**
     * 获取 json
     */
    public String buildResultJson(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("succ", this.succ);
        jsonObject.put("code", this.code);
        jsonObject.put("ts", this.ts);
        jsonObject.put("msg", this.msg);
        jsonObject.put("data", this.data);
        return JSON.toJSONString(jsonObject, SerializerFeature.DisableCircularReferenceDetect);
    }

}

