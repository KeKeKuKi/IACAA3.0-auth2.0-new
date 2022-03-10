package com.gapache.commons.jvm.classloader;

/**
 * @author HuSen
 * create on 2020/3/13 10:48 上午
 */
public class MyTest14 {

    public static void main(String[] args) throws Exception{
        MyClassLoader loader1 = new MyClassLoader("", "loader1");
        Class<?> clazz = loader1.loadClass("com.gapache.commons.jvm.classloader.MySample");
        System.out.println("class: " + clazz.hashCode());
        // 如果注释掉该行，那么并不会实例化MySample对象，即MySample构造方法不会被调用
        // 因此不会实例化MyCat对象，即没有对MyCat进行主动使用，这里就不会加载MyCat CLass
        Object object = clazz.newInstance();
    }
}
