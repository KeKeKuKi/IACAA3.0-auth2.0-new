package com.gapache.commons.utils;

import org.apache.commons.collections4.MapUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author HuSen
 * create on 2020/4/12 12:59
 */
public class HttpUtils {

    private static final CloseableHttpClient SYNC_CLIENT;

    static {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(500000)
                .setSocketTimeout(500000)
                .setConnectionRequestTimeout(500000)
                .build();

        SYNC_CLIENT = HttpClients.custom()
                .setConnectionManager(new PoolingHttpClientConnectionManager())
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    public static String getSync(String uri, Map<String, String> params, Map<String, String> headers) {
        try {
            // 封装参数
            StringBuilder targetUri = new StringBuilder(uri);
            targetUri.append("?");
            if (MapUtils.isNotEmpty(params)) {
                params.forEach((name, value) -> targetUri.append(name).append("=").append(value).append("&"));
            }
            HttpGet httpGet = new HttpGet(targetUri.deleteCharAt(targetUri.length() - 1).toString());
            // 请求头
            if (MapUtils.isNotEmpty(headers)) {
                headers.forEach(httpGet::addHeader);
            }
            CloseableHttpResponse response = SYNC_CLIENT.execute(httpGet);
            return inputStream2String(response.getEntity().getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String inputStream2String(InputStream inputStream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString(StandardCharsets.UTF_8.name());
    }
}
