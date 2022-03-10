package com.gapache.vertx.web.config;

import com.gapache.vertx.core.VertxManager;
import com.gapache.vertx.core.VertxSettings;
import com.gapache.vertx.web.annotation.EnableZeusClients;
import com.gapache.vertx.web.annotation.ZeusClient;
import com.gapache.vertx.web.zeus.ZeusClientProxyFactory;
import io.vertx.core.http.HttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.lang.NonNull;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author HuSen
 * @since 2021/3/2 1:22 下午
 */
@Slf4j
@Configuration
@ConditionalOnBean(annotation = EnableZeusClients.class)
@EnableConfigurationProperties(VertxSettings.HttpClient.class)
public class HttpClientAutoConfiguration implements BeanDefinitionRegistryPostProcessor, ApplicationContextAware, ResourceLoaderAware {

    private ApplicationContext applicationContext;
    private ResourcePatternResolver resolver;
    private MetadataReaderFactory metadataReaderFactory;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(@NonNull BeanDefinitionRegistry registry) throws BeansException {
        // 首先这个类并不是一个实例，所以要去指定的类路径下去找
        Map<String, Object> enableZeusClientsConfigMap = applicationContext.getBeansWithAnnotation(EnableZeusClients.class);
        if (enableZeusClientsConfigMap.isEmpty()) {
            return;
        }
        // TODO settings And cache client
        HttpClient httpClient = VertxManager.checkNewStandalone().createHttpClient();

        enableZeusClientsConfigMap.forEach((name, configBean) -> {
            EnableZeusClients enableZeusClients = AnnotationUtils.findAnnotation(configBean.getClass(), EnableZeusClients.class);
            if (enableZeusClients == null) {
                return;
            }

            Class<?>[] basePackageClasses = enableZeusClients.basePackageClasses();
            if (basePackageClasses.length == 0) {
                return;
            }

            Set<Class<?>> classes = new HashSet<>();
            for (Class<?> basePackageClass : basePackageClasses) {
                String classNameToResourcePath = ClassUtils.convertClassNameToResourcePath(basePackageClass.getName());
                String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                        .concat(classNameToResourcePath.substring(0, classNameToResourcePath.length() - basePackageClass.getSimpleName().length()))
                        .concat("**/*.class");
                try {
                    Resource[] resources = resolver.getResources(packageSearchPath);
                    MetadataReader metadataReader;
                    for (Resource resource : resources) {
                        if (resource.isReadable()) {
                            metadataReader = metadataReaderFactory.getMetadataReader(resource);
                            // 当它是接口的时候添加到集合
                            if (metadataReader.getClassMetadata().isInterface()) {
                                classes.add(Class.forName(metadataReader.getClassMetadata().getClassName()));
                            }
                        }
                    }
                } catch (Exception e) {
                    log.info("load class error.", e);
                }
            }

            if (classes.isEmpty()) {
                return;
            }

            // 加载完成类以后需要生成动态代理类
            for (Class<?> zeusClientClass : classes) {
                ZeusClient zeusClient = AnnotationUtils.findAnnotation(zeusClientClass, ZeusClient.class);
                if (zeusClient == null) {
                    continue;
                }

                String serviceName = StringUtils.hasText(zeusClient.name()) ? zeusClient.name() : StringUtils.hasText(zeusClient.value()) ? zeusClient.value() : "";
                if (!StringUtils.hasText(serviceName)) {
                    log.warn("{} service name is empty!", zeusClientClass);
                    continue;
                }

                String basicPath = zeusClient.path();

                Method[] declaredMethods = zeusClientClass.getDeclaredMethods();
                Method.setAccessible(declaredMethods, true);

                BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(zeusClientClass);
                GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
                definition.getPropertyValues().add("interfaceClass", definition.getBeanClassName());
                definition.getPropertyValues().add("serviceName", serviceName);
                definition.getPropertyValues().add("basicPath", basicPath);
                definition.getPropertyValues().add("httpClient", httpClient);
                definition.getPropertyValues().add("group", zeusClient.group());
                definition.getPropertyValues().add("applicationContext", applicationContext);
                definition.setBeanClass(ZeusClientProxyFactory.class);
                definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
                // 注册bean名,一般为类名首字母小写
                registry.registerBeanDefinition(zeusClientClass.getSimpleName().substring(0, zeusClientClass.getSimpleName().length() - 6), definition);
            }
        });
    }

    @Override
    public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // nothing
    }

    @Override
    public void setResourceLoader(@NonNull ResourceLoader resourceLoader) {
        resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        metadataReaderFactory = new CachingMetadataReaderFactory(resourceLoader);
    }
}
