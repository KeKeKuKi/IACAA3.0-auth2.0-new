//package com.gapache.openfeign;
//
//import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
//import com.gapache.sentinel.DegradeRuleHelper;
//import com.gapache.sentinel.ProtectingCaller;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.context.ApplicationContext;
//import org.springframework.core.annotation.AnnotationUtils;
//import org.springframework.http.HttpMethod;
//import org.springframework.web.bind.annotation.*;
//
//import java.lang.reflect.Method;
//import java.util.*;
//
///**
// * @author HuSen
// * @since 2021/3/15 4:03 下午
// */
//@Slf4j
//public class FeignProtectingCaller implements ProtectingCaller {
//
//    @Override
//    public List<DegradeRule> init(ApplicationContext context) {
//        // 给Feign生成限流规则
//        Map<String, Object> feignClientMap = context.getBeansWithAnnotation(FeignClient.class);
//
//        List<DegradeRule> configs = new ArrayList<>();
//        Set<String> checker = new HashSet<>(feignClientMap.size());
//        feignClientMap.forEach((name, bean) -> {
//            FeignClient feignClient = AnnotationUtils.findAnnotation(bean.getClass(), FeignClient.class);
//            if (feignClient == null) {
//                return;
//            }
//
//            if (checker.contains(feignClient.value().concat(feignClient.path()))) {
//                return;
//            }
//
//            checker.add(feignClient.value().concat(feignClient.path()));
//
//            Method[] methods = bean.getClass().getDeclaredMethods();
//            Method.setAccessible(methods, true);
//
//            for (Method method : methods) {
//                // 一个都找不到才找RequestMapping
//                String server = pathText(feignClient.value());
//                String path = pathText(feignClient.path());
//                String uri = null;
//                HttpMethod httpMethod = null;
//                PostMapping postMapping;
//                PutMapping putMapping;
//                DeleteMapping deleteMapping;
//                GetMapping getMapping = AnnotationUtils.findAnnotation(method, GetMapping.class);
//                if (getMapping != null) {
//                    httpMethod = HttpMethod.GET;
//                    uri = getMapping.value().length > 0 ? pathText(getMapping.value()[0]) : "";
//                } else if ((postMapping = AnnotationUtils.findAnnotation(method, PostMapping.class)) != null) {
//                    httpMethod = HttpMethod.POST;
//                    uri = postMapping.value().length > 0 ? pathText(postMapping.value()[0]) : "";
//                } else if ((putMapping = AnnotationUtils.findAnnotation(method, PutMapping.class)) != null) {
//                    httpMethod = HttpMethod.PUT;
//                    uri = putMapping.value().length > 0 ? pathText(putMapping.value()[0]) : "";
//                } else if ((deleteMapping = AnnotationUtils.findAnnotation(method, DeleteMapping.class)) != null) {
//                    httpMethod = HttpMethod.DELETE;
//                    uri = deleteMapping.value().length > 0 ? pathText(deleteMapping.value()[0]) : "";
//                }
//                if (httpMethod != null) {
//                    DegradeRule degradeRule = create(httpMethod, server, path, uri);
//                    configs.add(degradeRule);
//                    continue;
//                }
//
//                RequestMapping requestMapping = AnnotationUtils.findAnnotation(method, RequestMapping.class);
//                if (requestMapping != null) {
//                    uri = pathText(requestMapping.value()[0]);
//                    RequestMethod[] requestMethods = requestMapping.method();
//                    if (requestMethods.length == 0) {
//                        configs.add(create(HttpMethod.GET, server, path, uri));
//                        configs.add(create(HttpMethod.POST, server, path, uri));
//                        configs.add(create(HttpMethod.PUT, server, path, uri));
//                        configs.add(create(HttpMethod.DELETE, server, path, uri));
//                    } else {
//                        for (RequestMethod requestMethod : requestMethods) {
//                            configs.add(create(HttpMethod.valueOf(requestMethod.name()), server, path, uri));
//                        }
//                    }
//                }
//            }
//        });
//
//        return configs;
//    }
//
//    private DegradeRule create(HttpMethod httpMethod, String server, String path, String uri) {
//        String resourceName = "METHOD:http://server/path/uri";
//        resourceName = resourceName.replace("METHOD", httpMethod.name()).replace("server", server);
//        if (StringUtils.isBlank(path)) {
//            resourceName = resourceName.replace("/path", "");
//        } else {
//            resourceName = resourceName.replace("path", path);
//        }
//
//        if (StringUtils.isBlank(uri)) {
//            resourceName = resourceName.replace("/uri", "");
//        } else {
//            resourceName = resourceName.replace("uri", uri);
//        }
//        log.info("protect resource name {}", resourceName);
//
//        // 生成降级的策略
//        return DegradeRuleHelper.createDefault(resourceName);
//    }
//
//    private String pathText(String path) {
//        path = path.startsWith("/") ? path.substring(1) : path;
//        path = path.endsWith("/") ? path.substring(0, path.length() - 1) : path;
//        return path;
//    }
//}
