package com.gapache.vertx.web.zeus;

import com.gapache.vertx.core.PathHelper;
import com.gapache.vertx.web.annotation.DeleteRouting;
import com.gapache.vertx.web.annotation.GetRouting;
import com.gapache.vertx.web.annotation.PostRouting;
import com.gapache.vertx.web.annotation.PutRouting;
import io.vertx.core.http.HttpMethod;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author HuSen
 * @since 2021/3/3 9:17 上午
 */
public class CacheableMethodRoutingResolver {

    private final Map<Method, String> methodPathMap;
    private final Map<Method, HttpMethod> methodHttpMethodMap;

    public CacheableMethodRoutingResolver() {
        this.methodPathMap = new HashMap<>(64);
        this.methodHttpMethodMap = new HashMap<>(64);
    }

    public String checkPath(Method method) {
        return this.methodPathMap.get(method);
    }

    public HttpMethod checkMethod(Method method) {
        return this.methodHttpMethodMap.get(method);
    }

    public void resolve(Method method, String basicPath) {
        if (methodHttpMethodMap.containsKey(method)) {
            return;
        }
        String path = null;
        HttpMethod httpMethod = null;
        PostRouting postRouting = AnnotationUtils.findAnnotation(method, PostRouting.class);
        GetRouting getRouting = AnnotationUtils.findAnnotation(method, GetRouting.class);
        PutRouting putRouting = AnnotationUtils.findAnnotation(method, PutRouting.class);
        DeleteRouting deleteRouting = AnnotationUtils.findAnnotation(method, DeleteRouting.class);
        if (getRouting != null) {
            path = PathHelper.correctPath(PathHelper.correctPath(basicPath) + PathHelper.correctPath(getRouting.value()));
            httpMethod = HttpMethod.GET;
        } else if (putRouting != null) {
            path = PathHelper.correctPath(PathHelper.correctPath(basicPath) + PathHelper.correctPath(putRouting.value()));
            httpMethod = HttpMethod.PUT;
        } else if (postRouting != null) {
            path = PathHelper.correctPath(PathHelper.correctPath(basicPath) + PathHelper.correctPath(postRouting.value()));
            httpMethod = HttpMethod.POST;
        } else if (deleteRouting != null) {
            path = PathHelper.correctPath(PathHelper.correctPath(basicPath) + PathHelper.correctPath(deleteRouting.value()));
            httpMethod = HttpMethod.DELETE;
        }

        if (httpMethod != null) {
            this.methodPathMap.put(method, path);
            this.methodHttpMethodMap.put(method, httpMethod);
        }
    }
}
