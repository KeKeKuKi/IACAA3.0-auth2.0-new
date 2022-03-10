package com.gapache.commons.jvm.memory;

/**
 * 虚拟机栈溢出
 * jconsole
 *
 * @author HuSen
 * create on 2020/4/20 11:44 上午
 */
public class MyTest2 {

    private int length;

    public int getLength() {
        return length;
    }

    public void test() {
        this.length++;
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        test();
    }

    public static void main(String[] args) {
        // -Xss160k
        MyTest2 myTest2 = new MyTest2();
        try {
            myTest2.test();
        } catch (Throwable ex) {
            System.out.println(myTest2.getLength());
            ex.printStackTrace();
        }
    }
}
