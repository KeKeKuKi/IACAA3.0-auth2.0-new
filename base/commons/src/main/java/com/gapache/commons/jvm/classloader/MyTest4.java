package com.gapache.commons.jvm.classloader;

/**
 * @author HuSen
 * create on 2020/1/19
 */
public class MyTest4 {
    public static void main(String[] args) {
        // 对于数组实例来说，其类型是由JVM在运行期动态生成的，表示为[Lcom.gapache.commons.jvm.classloader.MyParent4
        // 这种形式。动态生成的类型，其父类型就是Object
        // 对于数组来说，JavaDoc经常将构成数组的元素Component，实际上就是将数组降低一个维度后的类型

        // 助记符：anewarray：表示创建一个引用类型的（如类、接口、数组）数组，并将其引用值压入栈顶
        //        newarray：表示创建一个指定的原始类型（int、float、char等）数组，并将其引用值压入栈顶

        MyParent4[] myParent4s = new MyParent4[1];
        System.out.println(myParent4s.getClass());
        // class [Lcom.gapache.commons.jvm.classloader.MyParent4;
        MyParent4[][] myParent4s1 = new MyParent4[1][1];
        System.out.println(myParent4s1.getClass());
        // class [[Lcom.gapache.commons.jvm.classloader.MyParent4;
        System.out.println(myParent4s.getClass().getSuperclass());
        // class java.lang.Object
        System.out.println(myParent4s1.getClass().getSuperclass());
        // class java.lang.Object

        System.out.println("======================");
        int[] ints = new int[1];
        System.out.println(ints.getClass());
        // class [I
        System.out.println(ints.getClass().getSuperclass());
        // class java.lang.Object
    }
}

class MyParent4 {
    static {
        System.out.println("MyParent4 static code");
    }
}
