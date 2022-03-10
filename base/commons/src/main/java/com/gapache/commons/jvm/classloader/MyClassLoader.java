package com.gapache.commons.jvm.classloader;

import java.io.*;

/**
 * @author HuSen
 * create on 2020/1/25 13:15
 */
public class MyClassLoader extends ClassLoader {

    private String classDir;
    private String classLoaderName;
    private static final String fileExtension = ".class";

    public MyClassLoader(ClassLoader parent) {
        super(parent);
    }

    public MyClassLoader(ClassLoader parent, String classDir, String classLoaderName) {
        super(parent);
        this.classDir = classDir;
        this.classLoaderName = classLoaderName;
    }

    public MyClassLoader(String classDir, String classLoaderName) {
        this.classDir = classDir;
        this.classLoaderName = classLoaderName;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] data = this.loadClassData(name);
        if (data == null) {
            throw new ClassNotFoundException(name + " is not found");
        }
        return defineClass(name, data, 0, data.length);
    }

    private byte[] loadClassData(String name) {
        name = name.replace(".", File.separator);
        byte[] data = null;
        try (InputStream is = new FileInputStream(new File(this.classDir.concat(name).concat(fileExtension))); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            int ch;
            while (-1 != (ch = is.read())) {
                baos.write(ch);
            }
            data = baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    public String toString() {
        return "MyClassLoader{" +
                "classLoaderName='" + classLoaderName + '\'' +
                '}';
    }

    public static void main(String[] args) throws Exception {
        MyClassLoader loader1 = new MyClassLoader("/Users/macos/Documents/codes/mine/game/target/classes/", "loader1");
        // 加载ClassPath下的类
        test(loader1, "com.gapache.commons.jvm.classloader.MyTest1");
        // class: 1581781576
        // classLoader: sun.misc.Launcher$AppClassLoader@18b4aac2
        // newInstance: com.gapache.commons.jvm.classloader.MyTest1@66d3c617
        System.out.println("------------------");
        // 加载指定classDir目录下的类
        test(loader1, "com.qdhh.game.MyEncrypt");
        // class: 895328852
        // classLoader: MyClassLoader{classLoaderName='loader1'}
        // newInstance: com.qdhh.game.MyEncrypt@4dc63996
        System.out.println("------------------");
        // 再次使用不同的加载器加载同一个类
        MyClassLoader loader2 = new MyClassLoader("/Users/macos/Documents/codes/mine/game/target/classes/", "loader2");
        test(loader2, "com.gapache.commons.jvm.classloader.MyTest1");
        // class: 1581781576
        // classLoader: sun.misc.Launcher$AppClassLoader@18b4aac2
        // newInstance: com.gapache.commons.jvm.classloader.MyTest1@d716361
        System.out.println("------------------");
        // 再次使用不同的加载器加载classDir目录下的类
        test(loader2, "com.qdhh.game.MyEncrypt");
        // class: 1259475182
        // classLoader: MyClassLoader{classLoaderName='loader2'}
        // newInstance: com.qdhh.game.MyEncrypt@4d7e1886
        System.out.println("-------------------");
        // 使用loader1作为loader3的父加载器，加载claasDir目录下的类
        MyClassLoader loader3 = new MyClassLoader(loader1, "/Users/macos/Documents/codes/mine/game/target/classes/", "loader3");
        test(loader3, "com.qdhh.game.MyEncrypt");
        // class: 895328852
        // classLoader: MyClassLoader{classLoaderName='loader1'}
        // newInstance: com.qdhh.game.MyEncrypt@3cd1a2f1
    }

    public static void test(ClassLoader classLoader, String className) throws Exception {
        Class<?> aClass = classLoader.loadClass(className);
        System.out.println("class: " + aClass.hashCode());
        System.out.println("classLoader: " + aClass.getClassLoader());
        System.out.println("newInstance: " + aClass.newInstance());
    }
}
