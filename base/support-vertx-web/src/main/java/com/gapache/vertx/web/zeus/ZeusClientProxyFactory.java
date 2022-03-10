package com.gapache.vertx.web.zeus;

import io.vertx.core.http.HttpClient;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;

/**
 * @author HuSen
 * @since 2021/3/2 2:55 下午
 */
@Getter
@Setter
public class ZeusClientProxyFactory<T> implements FactoryBean<T> {

    private Class<T> interfaceClass;
    private String serviceName;
    private String basicPath;
    private HttpClient httpClient;
    private String group;
    private ApplicationContext applicationContext;

    @SuppressWarnings("unchecked")
    @Override
    public T getObject() {
        return (T) new ZeusClientProxy().bind(interfaceClass, httpClient, serviceName, basicPath, applicationContext);
    }

    @Override
    public Class<?> getObjectType() {
        return interfaceClass;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
