package com.gapache.commons.jvm.bytecode;

/**
 * @author HuSen
 * create on 2020/4/14 4:31 下午
 */
public class MyTest5 {

    /*
    * 3方法重载，是一种静态的行为，编译期就可以完全确定。
    * */

    public void test(Grandpa grandpa) {
        System.out.println("grandpa");
    }

    public void test(Father father) {
        System.out.println("father");
    }

    public void test(Son son) {
        System.out.println("son");
    }

    public static void main(String[] args) {
        // 2g1的静态类型是Grandpa，而g1的实际类型（真正指向的类型）是Father
        // 我们可以得出一个结论：变量的静态类型是不会发生变化的，而变量的实际类型则是可以发生变化的（多态的一种体现），实际类型是在运行期方可确定的。
        Grandpa g1 = new Father();
        Grandpa g2 = new Son();

        // 1方法的静态分派
        MyTest5 myTest5 = new MyTest5();
        myTest5.test(g1);
        myTest5.test(g2);
    }
}

class Grandpa {

}

class Father extends Grandpa {

}

class Son extends Father {

}
