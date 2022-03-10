package com.gapache.vertx.web.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gapache.vertx.web.utils.TypeUtils;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.reflect.Java15AnnotationFinder;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 一些基本类型参数的解析
 *
 * @author HuSen
 * @since 2021/3/1 6:39 下午
 */
@Slf4j
public class JsonParserHandler implements Handler<RoutingContext> {

    private final Method method;
    private final Java15AnnotationFinder java15AnnotationFinder;

    public JsonParserHandler(Method method, Java15AnnotationFinder java15AnnotationFinder) {
        this.method = method;
        this.java15AnnotationFinder = java15AnnotationFinder;
    }

    @Override
    public void handle(RoutingContext ctx) {
        Boolean parseByArgOrder = ctx.get("parseByArgOrder");
        // 解析参数
        Parameter[] parameters = method.getParameters();
        String[] parameterNames = java15AnnotationFinder.getParameterNames(method);
        JSONArray jsonArray = ctx.get("jsonArray");
        JSONObject jsonObject = ctx.get("jsonObject");
        Object[] args = null;
        if (parameters.length > 0) {
            args = new Object[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                Parameter parameter = parameters[i];
                Class<?> classType = parameter.getType();
                if (classType.getName().equals(RoutingContext.class.getName())) {
                    args[i] = ctx;
                    continue;
                }
                if (jsonObject != null) {
                    String parameterName = parseByArgOrder != null && parseByArgOrder ? "arg" + i : parameterNames[i];
                    if (classType.getName().equals(String.class.getName())) {
                        args[i] = jsonObject.getString(parameterName);
                        continue;
                    }
                    if (classType.getName().equals(Integer.class.getName()) || classType.getName().equals(int.class.getName())) {
                        args[i] = jsonObject.getInteger(parameterName);
                        continue;
                    }
                    if (classType.getName().equals(Byte.class.getName()) || classType.getName().equals(byte.class.getName())) {
                        args[i] = jsonObject.getByte(parameterName);
                        continue;
                    }
                    if (classType.getName().equals(Character.class.getName()) || classType.getName().equals(char.class.getName())) {
                        args[i] = jsonObject.getByte(parameterName);
                        continue;
                    }
                    if (classType.getName().equals(Short.class.getName()) || classType.getName().equals(short.class.getName())) {
                        args[i] = jsonObject.getShort(parameterName);
                        continue;
                    }
                    if (classType.getName().equals(Long.class.getName()) || classType.getName().equals(long.class.getName())) {
                        args[i] = jsonObject.getLong(parameterName);
                        continue;
                    }
                    if (classType.getName().equals(Double.class.getName()) || classType.getName().equals(double.class.getName())) {
                        args[i] = jsonObject.getDouble(parameterName);
                        continue;
                    }
                    if (classType.getName().equals(Boolean.class.getName()) || classType.getName().equals(boolean.class.getName())) {
                        args[i] = jsonObject.getBoolean(parameterName);
                        continue;
                    }
                    if (classType.getName().equals(BigDecimal.class.getName())) {
                        args[i] = jsonObject.getBigDecimal(parameterName);
                        continue;
                    }
                    if (classType.getName().equals(BigInteger.class.getName())) {
                        args[i] = jsonObject.getBigInteger(parameterName);
                        continue;
                    }
                    if (classType.getName().equals(byte[].class.getName())) {
                        args[i] = jsonObject.getBytes(parameterName);
                        continue;
                    }
                    // 其他类型，这里意味着为普通的PoJo
                    args[i] = jsonObject.toJavaObject(classType);
                } else {
                    // List类型
                    Type type = parameter.getParameterizedType();
                    Class<?> actualClass = TypeUtils.getActualClass(type);
                    if (actualClass != null) {
                        args[i] = jsonArray.toJavaList(actualClass);
                    }
                }
            }
        }

        if (args != null) {
            ctx.data().put("args", args);
        }

        ctx.next();
    }
}
