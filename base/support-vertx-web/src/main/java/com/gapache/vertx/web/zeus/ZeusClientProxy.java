package com.gapache.vertx.web.zeus;

import com.alibaba.fastjson.JSON;
import com.gapache.vertx.web.client.HttpRequestHelper;
import com.gapache.vertx.web.utils.TypeUtils;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.HttpMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.Proxy;
import org.springframework.cloud.netflix.ribbon.ServerIntrospector;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * @author HuSen
 * @since 2021/3/2 2:48 下午
 */
@Slf4j
public class ZeusClientProxy implements InvocationHandler {

    /**
     * TODO config
     */
    private Class<?> zeusClientClass;
    private String serviceName;
    private String basicPath;
    private HttpClient httpClient;
    private CacheableMethodRoutingResolver methodRoutingResolver;
    private BodyGenerator bodyGenerator;
    private CachingSpringLoadBalancerFactory cachingSpringLoadBalancerFactory;

    public Object bind(Class<?> cls, HttpClient httpClient, String serviceName, String basicPath, ApplicationContext applicationContext) {
        this.zeusClientClass = cls;
        this.serviceName = serviceName;
        this.basicPath = basicPath;
        this.httpClient = httpClient;
        this.methodRoutingResolver = new CacheableMethodRoutingResolver();
        this.bodyGenerator = new BodyGenerator();
        this.cachingSpringLoadBalancerFactory = new CachingSpringLoadBalancerFactory(applicationContext.getBean(SpringClientFactory.class));
        return Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{cls}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        // try first resolve
        methodRoutingResolver.resolve(method, this.basicPath);
        HttpMethod httpMethod = methodRoutingResolver.checkMethod(method);
        String path = methodRoutingResolver.checkPath(method);

        boolean useSpecify = false;
        String host = null;
        int port = 0;
        String dynamicServiceName = null;
        if (args != null && args.length > 0) {
            for (Object arg : args) {
                if (arg instanceof DynamicServiceNameProvider) {
                    DynamicServiceNameProvider dynamicServiceNameProvider = (DynamicServiceNameProvider) arg;
                    if (StringUtils.hasText(dynamicServiceNameProvider.ip()) && dynamicServiceNameProvider.port() > 0) {
                        host = dynamicServiceNameProvider.ip();
                        port = dynamicServiceNameProvider.port();
                        useSpecify = true;
                    } else {
                        dynamicServiceName = dynamicServiceNameProvider.getServiceName();
                    }
                    break;
                }
            }
        }

        if (httpMethod != null) {
            // body
            String body = bodyGenerator.generating(method, args);
            // headers
            Map<String, Object> headers = new HashMap<>(4);
            headers.put(Constants.FROM_ZEUS_HEADER, String.valueOf(System.currentTimeMillis()));

            // port and host 轮询算法进行负载均衡，这里要用到nacos了
            // 每个service对应一个loadBalancer 这里没有办法获得指定组的实例 要想办法生成一个
            if (!useSpecify) {
                String targetService = StringUtils.hasText(dynamicServiceName) ? dynamicServiceName : serviceName;
                ILoadBalancer loadBalancer = cachingSpringLoadBalancerFactory.create(targetService);
                ServerIntrospector serverIntrospector = cachingSpringLoadBalancerFactory.getFactory().getInstance(targetService, ServerIntrospector.class);
                Server server = loadBalancer.chooseServer(targetService);
                if (server == null) {
                    return Future.failedFuture("no available server of " + targetService);
                }

                Map<String, String> metadata = serverIntrospector.getMetadata(server);
                String zeusPort = metadata.get("zeus-port");
                if (!StringUtils.hasText(zeusPort)) {
                    return Future.failedFuture("not found zeus server of " + targetService);
                }

                host = server.getHost();
                port = Integer.parseInt(zeusPort);
            }

            String finalHost = host;
            int finalPort = port;
            return Future.future(objectPromise -> HttpRequestHelper
                    .call(httpClient, httpMethod, finalHost, finalPort, path, body, headers)
                    .onComplete(asyncResult -> {
                        if (asyncResult.succeeded()) {
                            AsyncResult<HttpClientResponse> result = asyncResult.result();
                            if (result.succeeded()) {
                                HttpClientResponse response = result.result();
                                response.bodyHandler(buffer -> {

                                    if (buffer.length() > 0) {
                                        String string = buffer.getString(0, buffer.length());
                                        log.info("ok:{}", string);
                                        // 这里要获得Future的泛型
                                        Type type = method.getGenericReturnType();
                                        if (type instanceof ParameterizedType) {
                                            Type futureRawType = TypeUtils.getFutureRawType(type);
                                            Object returnObj = JSON.parseObject(string, futureRawType);
                                            // 如果域类型是对象，则会转成JSONObject

                                            if (returnObj instanceof MetadataHolder) {
                                               Map<String, String> metadata = new HashMap<>(2);
                                               metadata.put("ip", finalHost);
                                               metadata.put("zeus-port", finalPort + "");
                                                ((MetadataHolder) returnObj).setMetadata(metadata);
                                            }
                                            objectPromise.complete(returnObj);
                                        }
                                    }
                                });
                            } else {
                                objectPromise.fail(result.cause());
                            }
                        } else {
                            objectPromise.fail(asyncResult.cause());
                        }
                    }));
        }
        return Future.failedFuture("is not a routing method");
    }

    public static void main(String[] args) {
        System.out.println("".getClass().equals(String.class));
    }
}
