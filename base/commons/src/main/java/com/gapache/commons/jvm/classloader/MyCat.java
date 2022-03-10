package com.gapache.commons.jvm.classloader;

/**
 * @author HuSen
 * create on 2020/3/13 10:44 上午
 */
public class MyCat {

    public MyCat() {
        System.out.println("MyCat is loaded by: " + this.getClass().getClassLoader());
        System.out.println("from MyCat: " + MySample.class);
    }
}
