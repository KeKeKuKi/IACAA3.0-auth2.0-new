package com.gapache.commons.jvm.bytecode;

/**
 * @author HuSen
 * create on 2020/4/15 4:58 下午
 */
public class RealSubject implements Subject {

    @Override
    public void request() {
        System.out.println("From real subject");
    }
}
