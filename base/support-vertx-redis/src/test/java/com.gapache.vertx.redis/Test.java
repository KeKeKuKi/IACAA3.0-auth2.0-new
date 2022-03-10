package com.gapache.vertx.redis;

import com.gapache.commons.model.IPageRequest;
import com.gapache.vertx.redis.config.VertxRedisClientAutoConfiguration;
import com.gapache.vertx.redis.support.SimpleRedisRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.StopWatch;

import java.util.UUID;

/**
 * @author HuSen
 * @since 2021/3/8 1:16 下午
 */
public class Test {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(VertxRedisClientAutoConfiguration.class);
        SimpleRedisRepository repository = applicationContext.getBean(SimpleRedisRepository.class);

        Cat cat = new Cat();
        cat.setId(UUID.randomUUID().toString());
        cat.setName("Tom");
        cat.setAge(7);

        repository.save(cat, 60)
                .onSuccess(res -> repository.findById(cat.getId(), Cat.class).onSuccess(r -> {
                    for (int i = 0; i < 10; i++) {
                        IPageRequest<Cat> iPageRequest = new IPageRequest<>();
                        iPageRequest.setAsc(true);
                        StopWatch stopWatch = new StopWatch();
                        stopWatch.start();
                        repository.page(iPageRequest, Cat.class).onSuccess(x -> {
                            stopWatch.stop();
                            System.out.println(stopWatch.getTotalTimeMillis());
//                            System.out.println(x.getItems());
                        }).onFailure(System.out::println);
                    }
                }))
                .onFailure(Throwable::printStackTrace);
    }
}
