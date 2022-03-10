package com.gapache.commons.jvm.bytecode;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * 通过字节码生成审视Java动态代理运作机制
 *
 * @author HuSen
 * create on 2020/4/15 5:02 下午
 */
public class Client {

    public static void main(String[] args) {
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");

        RealSubject rs = new RealSubject();
        InvocationHandler ds = new DynamicSubject(rs);
        Class<? extends RealSubject> cls = rs.getClass();

        // 怎么创建出来的
        Subject subject = (Subject) Proxy.newProxyInstance(cls.getClassLoader(), cls.getInterfaces(), ds);

        subject.request();
    }
}
