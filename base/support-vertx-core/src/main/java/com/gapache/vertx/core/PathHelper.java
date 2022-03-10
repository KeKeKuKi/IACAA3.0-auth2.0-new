package com.gapache.vertx.core;

import org.springframework.util.StringUtils;

/**
 * @author HuSen
 * @since 2021/3/2 3:18 下午
 */
public class PathHelper {

    private static final String SUB_PATH_PREFIX = "/";

    public static String correctPath(String path) {
        if (!StringUtils.hasText(path)) {
            return "";
        }
        if (!path.startsWith(SUB_PATH_PREFIX)) {
            path = SUB_PATH_PREFIX + path;
        }
        if (path.endsWith(SUB_PATH_PREFIX)) {
            path = path.substring(0, path.length() - 1);
        }
        if (path.equals(SUB_PATH_PREFIX)) {
            return "";
        }
        return path;
    }
}
