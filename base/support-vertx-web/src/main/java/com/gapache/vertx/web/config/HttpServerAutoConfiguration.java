package com.gapache.vertx.web.config;

import com.gapache.vertx.core.VertxCreatedEvent;
import com.gapache.vertx.core.VertxManager;
import com.gapache.vertx.core.VertxSettings;
import com.gapache.vertx.web.annotation.*;
import com.gapache.vertx.web.handler.*;
import com.gapache.vertx.web.server.HttpServerVerticle;
import com.gapache.vertx.web.server.NoOpWebServer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.reflect.Java15AnnotationFinder;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.boot.web.reactive.context.StandardReactiveWebEnvironment;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.support.StandardServletEnvironment;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

import static com.gapache.vertx.core.PathHelper.correctPath;

/**
 * @author HuSen
 * @since 2021/3/1 3:26 下午
 */
@Slf4j
@Configuration
@ConditionalOnBean(annotation = EnableVertxWeb.class)
@EnableConfigurationProperties(VertxSettings.HttpServer.class)
public class HttpServerAutoConfiguration implements ApplicationListener<VertxCreatedEvent>, ApplicationContextAware {

    private ApplicationContext context;
    private final VertxSettings.HttpServer settings;
    private final Java15AnnotationFinder java15AnnotationFinder;

    HttpServerAutoConfiguration(VertxSettings.HttpServer settings) {
        this.settings = settings;
        java15AnnotationFinder = new Java15AnnotationFinder();
        java15AnnotationFinder.setClassLoader(this.getClass().getClassLoader());
    }

    @Override
    public void onApplicationEvent(@NonNull VertxCreatedEvent event) {
        if (event.isSuccess()) {
            start();
        } else {
            throw new RuntimeException("Vertx created fail");
        }
    }

    public void start() {
        // 扫描Controller => VertxController
        Map<String, Object> vertxControllerMap = context.getBeansWithAnnotation(VertxController.class);
        if (log.isDebugEnabled()) {
            log.debug("vert.x web >>>>>> 扫描到 VertxController{}, {}", System.lineSeparator(), Json.encode(vertxControllerMap));
        }

        Router router = Router.router(VertxManager.checkNewStandalone());
        // 404
        StatusCode404Handler statusCode404Handler = new StatusCode404Handler();
        vertxControllerMap.values().forEach(vertxController -> {
            RequestRouting basicRouting = AnnotationUtils.findAnnotation(vertxController.getClass(), RequestRouting.class);
            // 基础路径
            String basicPath = Objects.isNull(basicRouting) ? "" : basicRouting.value();
            basicPath = correctPath(basicPath);

            // 该controller允许的http方法
            RequestMethod[] requestMethods = Objects.isNull(basicRouting) ? new RequestMethod[]{} : basicRouting.method();
            // 如果为空，则允许所有的http方法
            boolean isAllowAllMethodByController = requestMethods.length == 0;

            Method[] declaredMethods = vertxController.getClass().getDeclaredMethods();
            Method.setAccessible(declaredMethods, true);

            for (Method method : declaredMethods) {
                // 生成route
                Route route = createRoute(router, statusCode404Handler, basicPath, method, requestMethods, isAllowAllMethodByController);
                // 映射json body为方法参数的处理器
                if (route != null) {
                    route.handler(new ZeusJsonParserHandler());
                    route.handler(new JsonParserHandler(method, java15AnnotationFinder));
                }
                // 调用目标方法的处理器
                if (route != null) {
                    route.handler(new MethodInvokeHandler(method, vertxController));
                }
                // 处理返回结果的处理器
                if (route != null) {
                    route.handler(new ReturnResultHandler());
                }
            }
        });

        HttpServerVerticle httpServerVerticle = new HttpServerVerticle(settings, router);
        // TODO settings
        VertxManager.checkNewStandalone().deployVerticle(httpServerVerticle)
                .onComplete(as -> {
                    if (as.succeeded()) {
                        // 这是为了能够注册到nacos上
                        Environment environment = context.getEnvironment();
                        // 非Web环境的标准环境
                        if ((environment instanceof StandardServletEnvironment) || (environment instanceof StandardReactiveWebEnvironment)) {
                            return;
                        }

                        context.publishEvent(new WebServerInitializedEvent(new NoOpWebServer(settings.getPort())) {
                            private static final long serialVersionUID = -3737413969588035128L;
                            @Override
                            public WebServerApplicationContext getApplicationContext() {
                                return new ServletWebServerApplicationContext();
                            }
                        });
                    }
                });
    }

    private Route createRoute(Router router, StatusCode404Handler statusCode404Handler, String basicPath, Method method, RequestMethod[] requestMethods, boolean isAllowAllMethodByController) {
        RequestRouting methodRouting = AnnotationUtils.findAnnotation(method, RequestRouting.class);
        // 生成route
        if (methodRouting != null) {
            String subPath = methodRouting.value();
            subPath = correctPath(subPath);
            String correctPath = correctPath(basicPath + subPath);
            Route route = router.route(correctPath).produces("application/json");
            RequestMethod[] methods = methodRouting.method();
            if (methods.length == 0 && !isAllowAllMethodByController) {
                for (RequestMethod requestMethod : requestMethods) {
                    route.method(HttpMethod.valueOf(requestMethod.name()));
                    statusCode404Handler.cache(correctPath, requestMethod.name());
                }
            } else {
                for (RequestMethod requestMethod : methods) {
                    route.method(HttpMethod.valueOf(requestMethod.name()));
                    statusCode404Handler.cache(correctPath, requestMethod.name());
                }
            }
            return route;
        }

        String correctPath = "";
        HttpMethod httpMethod = null;
        // GET
        GetRouting getRouting = AnnotationUtils.findAnnotation(method, GetRouting.class);
        PostRouting postRouting = AnnotationUtils.findAnnotation(method, PostRouting.class);
        PutRouting putRouting = AnnotationUtils.findAnnotation(method, PutRouting.class);
        DeleteRouting deleteRouting = AnnotationUtils.findAnnotation(method, DeleteRouting.class);

        if (getRouting != null) {
            correctPath = correctPath(basicPath + correctPath(getRouting.value()));
            httpMethod = HttpMethod.GET;
        } else if (postRouting != null) {
            correctPath = correctPath(basicPath + correctPath(postRouting.value()));
            httpMethod = HttpMethod.POST;
        } else if (putRouting != null) {
            correctPath = correctPath(basicPath + correctPath(putRouting.value()));
            httpMethod = HttpMethod.PUT;
        } else if (deleteRouting != null) {
            correctPath = correctPath(basicPath + correctPath(deleteRouting.value()));
            httpMethod = HttpMethod.DELETE;
        }
        // POST
        if (httpMethod != null) {
            Route route = router
                    .route(correctPath)
                    .handler(statusCode404Handler)
                    .handler(BodyHandler.create())
                    .handler(new JsonBodyParserHandler())
                    .produces("application/json")
                    .method(httpMethod);
            statusCode404Handler.cache(correctPath, httpMethod.name());
            return route;
        }
        return null;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
