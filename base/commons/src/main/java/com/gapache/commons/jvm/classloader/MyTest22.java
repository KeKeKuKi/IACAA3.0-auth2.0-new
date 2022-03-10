package com.gapache.commons.jvm.classloader;

/**
 * @author HuSen
 * create on 2020/3/16 4:31 下午
 */
public class MyTest22 implements Runnable {

    private Thread thread;

    public MyTest22() {
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        ClassLoader classLoader = this.thread.getContextClassLoader();

        this.thread.setContextClassLoader(classLoader);

        System.out.println("Class: " + classLoader.getClass() );
        System.out.println("Parent: " + classLoader.getParent().getClass());
        // Class: class sun.misc.Launcher$AppClassLoader
        // Parent: class sun.misc.Launcher$ExtClassLoader
    }

    public static void main(String[] args) {
        new MyTest22();
    }
}
