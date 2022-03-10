package com.gapache.commons.jvm.memory;

/**
 * @author HuSen
 * create on 2020/4/18 14:16
 */
public class StackOverflowError {

    private static void test() {
        test();
    }

    public static void main(String[] args) {
        test();
    }
}
