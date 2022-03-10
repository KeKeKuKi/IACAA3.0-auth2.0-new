package com.gapache.commons.jvm.classloader;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

/**
 * @author HuSen
 * create on 2020/1/21 15:48
 */
public class MyTest12 {
    public static void main(String[] args) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        System.out.println(classLoader);
        // sun.misc.Launcher$AppClassLoader@18b4aac2
        String resourceName = "com/gapache/commons/jvm/classloader/MyTest12.class";
        Enumeration<URL> urls = classLoader.getResources(resourceName);
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            System.out.println(url);
            // 打印出了全路径
            // file:/F:/my/big-plan/base/commons/target/classes/com/gapache/commons/jvm/classloader/MyTest12.class
        }
    }
}
