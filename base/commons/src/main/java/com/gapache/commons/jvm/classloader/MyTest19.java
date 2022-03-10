package com.gapache.commons.jvm.classloader;

/**
 * java -Djava.ext.dirs=./ com.gapache.commons.jvm.classloader.MyTest19 观察输出
 *
 * jar cvf test.jar com/gapache/commons/jvm/classloader/MyTest1.class
 * 再次允许第一个命令 观察输出
 *
 * 扩展类加载器要从jar包进行加载
 *
 * @author HuSen
 * create on 2020/3/15 7:04 下午
 */
public class MyTest19 {

    static {
        System.out.println("MyTest19 initializer");
    }

    public static void main(String[] args) {
        System.out.println(MyTest19.class.getClassLoader());

        System.out.println(MyTest1.class.getClassLoader());
    }
}
