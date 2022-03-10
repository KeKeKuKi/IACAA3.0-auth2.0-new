package com.gapache.commons.jvm.classloader;

/**
 * @author HuSen
 * create on 2020/3/13 10:45 上午
 */
public class MySample {

    public MySample() {
        System.out.println("MySample is loaded by: " + this.getClass().getClassLoader());

        new MyCat();

        System.out.println("from MySample: " + MyCat.class);
    }
}
