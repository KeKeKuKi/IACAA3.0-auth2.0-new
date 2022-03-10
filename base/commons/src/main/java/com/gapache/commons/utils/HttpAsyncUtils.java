package com.gapache.commons.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author HuSen
 * @since 2020/6/24 2:59 下午
 */
public class HttpAsyncUtils {

    private static final CloseableHttpAsyncClient ASYNC_CLIENT;

    static {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(500000)
                .setSocketTimeout(500000)
                .setConnectionRequestTimeout(500000)
                .build();

        IOReactorConfig ioReactorConfig = IOReactorConfig.custom().
                setIoThreadCount(Runtime.getRuntime().availableProcessors())
                .setSoKeepAlive(true)
                .build();

        ConnectingIOReactor ioReactor = null;
        try {
            ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);
        } catch (IOReactorException e) {
            e.printStackTrace();
        }

        assert ioReactor != null;
        PoolingNHttpClientConnectionManager connManager = new PoolingNHttpClientConnectionManager(ioReactor);
        connManager.setMaxTotal(100);
        connManager.setDefaultMaxPerRoute(100);

        ASYNC_CLIENT = HttpAsyncClients.custom().
                setConnectionManager(connManager)
                .setDefaultRequestConfig(requestConfig)
                .build();

        ASYNC_CLIENT.start();
    }

    public static void optionsAsync(String uri, Map<String, String> headers, FutureCallback<HttpResponse> callback) {
        HttpOptions httpOptions = new HttpOptions(uri);
        headers.forEach(httpOptions::addHeader);
        ASYNC_CLIENT.execute(httpOptions, callback);
    }

    public static void getAsync(String uri, Map<String, String> headers, FutureCallback<HttpResponse> callback) {
        HttpGet httpGet = new HttpGet(uri);
        headers.forEach(httpGet::addHeader);
        ASYNC_CLIENT.execute(httpGet, callback);
    }

    public static void postAsync(String uri, String body, Map<String, String> headers, FutureCallback<HttpResponse> callback) {
        headers.remove("Content-Length");
        HttpPost httpPost = new HttpPost(uri);
        headers.forEach(httpPost::addHeader);
        StringEntity entity = null;
        try {
            entity = new StringEntity(body);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        httpPost.setEntity(entity);
        ASYNC_CLIENT.execute(httpPost, callback);
    }

    public static void deleteAsync(String uri, Map<String, String> headers, FutureCallback<HttpResponse> callback) {
        HttpDelete httpDelete = new HttpDelete(uri);
        headers.forEach(httpDelete::addHeader);
        ASYNC_CLIENT.execute(httpDelete, callback);
    }

    public static void putAsync(String uri, String body, Map<String, String> headers, FutureCallback<HttpResponse> callback) {
        headers.remove("Content-Length");
        HttpPut httpPut = new HttpPut(uri);
        headers.forEach(httpPut::addHeader);
        StringEntity entity = new StringEntity(body, StandardCharsets.UTF_8);
        httpPut.setEntity(entity);
        ASYNC_CLIENT.execute(httpPut, callback);
    }
}
