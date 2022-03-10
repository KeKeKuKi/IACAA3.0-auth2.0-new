package com.gapache.vertx.redis.support;

import com.alibaba.fastjson.JSON;
import com.gapache.commons.model.IPageRequest;
import com.gapache.commons.model.PageResult;
import com.gapache.commons.utils.IStringUtils;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.redis.client.Command;
import io.vertx.redis.client.Redis;
import io.vertx.redis.client.Request;
import io.vertx.redis.client.Response;
import io.vertx.redis.client.impl.RequestImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author HuSen
 * @since 2021/3/8 1:06 下午
 */
@Slf4j
public class SimpleRedisRepository {

    private final Map<String, String> shaCache;
    private final Redis redis;

    public SimpleRedisRepository(Redis redis) {
        this.redis = redis;
        this.shaCache = new HashMap<>(4);
    }

    public Future<Response> save(Object entity) {
        return save(entity, 0);
    }

    public Future<Response> save(Object entity, long timeout) {
        Assert.notNull(entity, "entity must not null!");
        RedisEntityDescriptor descriptor = DescriptorUtils.getDescriptor(entity.getClass());

        JsonObject redisMap = new JsonObject();
        redisMap.put(descriptor.getKey().concat(RedisEntityDescriptor.SPLIT_SIGN).concat(descriptor.getId(entity).toString()), JSON.toJSONString(entity));

        return Future.future(event -> {
            String sha = shaCache.get("save");
            if (sha == null) {
                Request request = new RequestImpl(Command.SCRIPT);
                String lua = loadLua("Save.lua");
                Assert.notNull(lua, "save lua is empty!");
                request.arg("LOAD");
                request.arg(lua);

                redis.send(request)
                        .onSuccess(res -> {
                            shaCache.put("save", res.toString());
                            doSave(event, res.toString(), descriptor, entity, timeout);
                        })
                        .onFailure(event::fail);
            } else {
                doSave(event, sha, descriptor, entity, timeout);
            }
        });
    }

    public <T> Future<T> findById(String id, Class<T> tClass) {
        Assert.notNull(id, "id must not null!");
        RedisEntityDescriptor descriptor = DescriptorUtils.getDescriptor(tClass);

        return Future.future(promise -> {
            Request request = new RequestImpl(Command.HMGET);
            request.arg(descriptor.getKey().concat(RedisEntityDescriptor.SPLIT_SIGN).concat(id));
            List<Field> fields = descriptor.getFields();
            for (Field field : fields) {
                request.arg(field.getName());
            }

            redis.send(request)
                    .onSuccess(res -> {
                        T instance = null;
                        try {
                            if (res.size() == 0) {
                                promise.complete(null);
                                return;
                            }

                            instance = tClass.newInstance();
                            for (int i = 0; i < res.size(); i++) {
                                Field field = fields.get(i);
                                Response response = res.get(i);
                                if (response == null) {
                                    continue;
                                }

                                setValue(instance, field, response);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        promise.complete(instance);
                    })
                    .onFailure(promise::fail);
        });
    }

    public <T> Future<PageResult<T>> page(IPageRequest<T> pageRequest, Class<T> clazz) {
        return Future.future(event -> {
            Integer page = pageRequest.getPage();
            Integer number = pageRequest.getNumber();
            Request countRequest = new RequestImpl(Command.ZCOUNT);
            RedisEntityDescriptor descriptor = DescriptorUtils.getDescriptor(clazz);
            countRequest.arg(RedisEntityDescriptor.PAGE.concat(RedisEntityDescriptor.SPLIT_SIGN).concat(descriptor.getKey()));
            countRequest.arg(0).arg(System.currentTimeMillis());
            redis.send(countRequest)
                    .onSuccess(countRes -> {
                        Integer total = countRes.toInteger();
                        int start = (page - 1) * number;
                        int temp = start + number - 1;
                        int end  = temp > total - 1 ? -1 : temp;
                        Request request = new RequestImpl(pageRequest.getAsc() ? Command.ZRANGE : Command.ZREVRANGE);
                        request.arg(RedisEntityDescriptor.PAGE.concat(RedisEntityDescriptor.SPLIT_SIGN).concat(descriptor.getKey()));
                        request.arg(start).arg(end);
                        redis.send(request)
                                .onSuccess(rangeRes -> batchFind(clazz, event, descriptor, total, rangeRes));
                    });
        });
    }

    public Future<Response> delete(Object entity) {
        Assert.notNull(entity, "entity must not null!");
        RedisEntityDescriptor descriptor = DescriptorUtils.getDescriptor(entity.getClass());

        Request request = new RequestImpl(Command.DEL);
        request.arg(descriptor.getKey().concat(RedisEntityDescriptor.SPLIT_SIGN).concat(descriptor.getId(entity).toString()));

        return redis.send(request);
    }

    private <T> void batchFind(Class<T> clazz, Promise<PageResult<T>> event, RedisEntityDescriptor descriptor, Integer total, Response rangeRes) {
        List<Request> requests = new ArrayList<>();
        rangeRes.forEach(x -> {
            Request getRequest = new RequestImpl(Command.HGETALL);
            getRequest.arg(descriptor.getKey().concat(RedisEntityDescriptor.SPLIT_SIGN).concat(x.toString()));
            requests.add(getRequest);
        });

        redis.batch(requests)
                .onSuccess(list -> {
                    List<T> content = new ArrayList<>();
                    Map<String, Field> nameMap = descriptor.getFields().stream().collect(Collectors.toMap(Field::getName, f -> f));
                    list.forEach(response -> {
                        try {
                            T instance = clazz.newInstance();
                            for (String key : response.getKeys()) {
                                if (nameMap.containsKey(key)) {
                                    setValue(instance, nameMap.get(key), response.get(key));
                                }
                            }
                            content.add(instance);
                        } catch (Exception e) {
                            log.error("set value fail id {}", response.toString());
                        }
                    });
                    PageResult<T> pageResult = new PageResult<>();
                    pageResult.setItems(content);
                    pageResult.setTotal(total);
                    event.complete(pageResult);
                })
                .onFailure(event::fail);
    }

    private <T> void setValue(T instance, Field field, Response response) throws IllegalAccessException {
        Type genericType = field.getGenericType();
        if (genericType.equals(String.class)) {
            field.set(instance, response.toString());
            return;
        }

        if (genericType.equals(Integer.class)) {
            field.set(instance, response.toInteger());
            return;
        }

        if (genericType.equals(Boolean.class)) {
            field.set(instance, response.toBoolean());
            return;
        }

        if (genericType.equals(Byte.class)) {
            field.set(instance, response.toByte());
            return;
        }

        if (genericType.equals(byte[].class)) {
            field.set(instance, response.toBytes());
            return;
        }

        if (genericType.equals(Short.class)) {
            field.set(instance, response.toShort());
            return;
        }

        if (genericType.equals(BigInteger.class)) {
            field.set(instance, response.toBigInteger());
            return;
        }

        if (genericType.equals(Long.class)) {
            field.set(instance, response.toLong());
            return;
        }

        if (genericType.equals(Double.class)) {
            field.set(instance, response.toDouble());
            return;
        }

        if (genericType.equals(BigDecimal.class)) {
            field.set(instance, new BigDecimal(response.toString()));
        }
    }

    private void doSave(Promise<Response> event, String sha, RedisEntityDescriptor descriptor, Object entity, long timeout) {
        Request newRequest = new RequestImpl(Command.EVALSHA);
        newRequest.arg(sha);

        int keysNumber = 1;
        List<Field> fields = descriptor.getFields();
        for (Field field : fields) {
            Object value = descriptor.getValue(entity, field);
            if (value != null) {
                keysNumber++;
            }
        }
        newRequest.arg(keysNumber);
        newRequest.arg(descriptor.getKey().concat(RedisEntityDescriptor.SPLIT_SIGN).concat(descriptor.getId(entity).toString()));

        for (Field field : fields) {
            Object value = descriptor.getValue(entity, field);
            if (value != null) {
                newRequest.arg(field.getName());
            }
        }

        for (Field field : fields) {
            Object value = descriptor.getValue(entity, field);
            if (value != null) {
                newRequest.arg(value.toString());
            }
        }

        newRequest.arg(descriptor.getKey())
                .arg(System.currentTimeMillis())
                .arg(descriptor.getId(entity).toString())
                .arg(timeout);

        redis.send(newRequest).onSuccess(event::complete).onFailure(event::fail);
    }

    @SuppressWarnings("SameParameterValue")
    private String loadLua(String fileName) {
        try {
            byte[] bytes = FileCopyUtils.copyToByteArray(new ClassPathResource("lua/" + fileName).getInputStream());
            return IStringUtils.newString(bytes);
        } catch (Exception e) {
            log.error("loadLua error {}.", fileName, e);
        }
        return null;
    }
}
