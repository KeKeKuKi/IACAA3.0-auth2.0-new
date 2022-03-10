package com.gapache.vertx.redis.test;

import io.vertx.core.Vertx;
import io.vertx.core.file.FileSystem;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author HuSen
 * @since 2021/3/15 11:31 上午
 */
@Slf4j
public class Test {

    private static final String URI = "/?app=weather.history&weaid={WEA_ID}&date={DATE}&appkey=57881&sign=76a471ed0300acb8b838c6ca355839d1&format=json";

    public static void main(String[] args) throws Exception {
        String basicPath = "/Users/husen/weather";
        int end = 2021;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.YEAR, 2020);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        String weaId = "265";
        Vertx vertx = Vertx.vertx();
        FileSystem fs = vertx.fileSystem();
        if (!fs.existsBlocking(basicPath)) {
            fs.mkdirBlocking(basicPath);
        }
        if (!fs.existsBlocking(basicPath.concat(File.separator).concat(weaId))) {
            fs.mkdirBlocking(basicPath.concat(File.separator).concat(weaId));
        }
        HttpClient httpClient = vertx.createHttpClient(new HttpClientOptions().setConnectTimeout(5000));

        while (calendar.get(Calendar.YEAR) < end) {
            String date = DateFormatUtils.format(calendar.getTime(), "yyyyMMdd");
            String requestUri = URI.replace("{WEA_ID}", weaId).replace("{DATE}", date);
            FileWriter writer = new FileWriter(basicPath.concat(File.separator).concat(weaId).concat(File.separator).concat(date).concat(".json"));
            httpClient.request(HttpMethod.GET, "api.k780.com", requestUri)
                    .onSuccess(req -> {
                        log.info(">>>>>> req {}", requestUri);
                        req.send()
                                .onSuccess(resp -> resp.bodyHandler(buffer -> {
                                    JsonObject jsonObject = buffer.toJsonObject();
                                    if ("1".equals(jsonObject.getString("success"))) {
                                        JsonArray jsonArray = jsonObject.getJsonArray("result");
                                        try {
                                            writer.write(jsonArray.toString());
                                            writer.flush();
                                            writer.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }))
                                .onFailure(t -> System.out.println(222));
                    })
                    .onFailure(error -> log.error(">>>>>> error", error));
            TimeUnit.SECONDS.sleep(1);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
    }
}
