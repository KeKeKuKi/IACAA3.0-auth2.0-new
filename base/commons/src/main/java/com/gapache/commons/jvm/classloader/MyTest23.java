package com.gapache.commons.jvm.classloader;

import java.sql.Driver;
import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * 线程上下文类加载器的一般使用模式（获取 - 使用 - 还原）
 * 伪代码
 * ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
 * myMethod();
 *
 * try {
 *     Thread.currentThread().setContextClassLoader(targetCcl);
 * } finally {
 *     Thread.currentThread().setContextClassLoader(classLoader);
 * }
 *
 * myMethod()里面则调用了Thread.currentThread().getContextClassLoader()，获取当前线程的上下文类加载器做某些事情。
 * 如果一个类由类加载器A加载，那么这个类的依赖类也是由相同的类加载器加载的（如果该依赖类之前没有加载过的话）
 * ContextClassLoader的作用就是为了破坏Java的类加载委托机制。
 *
 * 当高层提供类统一的接口让底层去实现，同时又要在高层加载（或实例化）低层的类时，就必须要通过线程上下文类加载器来帮助高层的ClassLoader
 * 找到并加载该类。
 *
 * @author HuSen
 * create on 2020/3/16 4:40 下午
 */
public class MyTest23 {

    public static void main(String[] args) {
        // 引入MySql驱动
        // 看源码
        ServiceLoader<Driver> loader = ServiceLoader.load(Driver.class);
        Iterator<Driver> iterator = loader.iterator();
        while (iterator.hasNext()) {
            Driver driver = iterator.next();
            System.out.println("driver: " + driver.getClass() + ", loader: " + driver.getClass().getClassLoader());
        }
        System.out.println("当前线程上下文类加载器: " + Thread.currentThread().getContextClassLoader());
        System.out.println("ServiceLoader的类加载器: " + ServiceLoader.class.getClassLoader());
    }
}
