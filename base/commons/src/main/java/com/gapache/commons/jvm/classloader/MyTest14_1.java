package com.gapache.commons.jvm.classloader;

/**
 * 分别删除MySample和MyCat会有不同的效果
 *
 * @author HuSen
 * create on 2020/3/14 4:59 下午
 */
public class MyTest14_1 {
    public static void main(String[] args) throws Exception {
        MyClassLoader loader1 = new MyClassLoader("/Users/macos/Desktop/", "loader1");
        Class<?> clazz = loader1.loadClass("com.gapache.commons.jvm.classloader.MySample");
        System.out.println("class: " + clazz.hashCode());
        Object object = clazz.newInstance();

        // 关于命名空间的重要说明
        // 1.子加载器所加载的类能够访问到父加载器所加载的类
        // 2.父加载器所加载的类无法访问到子加载器所加载的类
    }
}
