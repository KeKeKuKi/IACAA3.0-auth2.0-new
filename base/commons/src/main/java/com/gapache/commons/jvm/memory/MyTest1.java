package com.gapache.commons.jvm.memory;

import java.util.ArrayList;
import java.util.List;

/**
 * 宏观布局
 *
 * 虚拟机栈：Stack Frame 栈帧
 *
 * 程序计数器（Program Counter）
 *
 * 本地方法栈：主要用于执行本地方法，native方法使用什么语言实现？怎么组织像栈帧这种为了服务方法的数据结构？
 * 虚拟机规范并未给出强制规定，因此不同的虚拟机实可以进行自由实现，我们常用的HotSpot虚拟机选择合并了虚拟机栈和本地方法栈。
 *
 * 堆（Heap）：JVM管理的最大一块内存空间，与堆相关的一个重要的概念是垃圾收集器。现代几乎所有的垃圾收集器都是采用的分代收集算法，
 * 所以，堆空间也基于这一点进行了相应的划分：新生代与老年代。新生代分为，Eden空间，From Survivor空间与To Survivor空间。
 *
 * 方法区（Method Area）：存储元信息。永久代（Permanent Generation），从jdk1.8开始，已经彻底废弃类永久代，使用元空间（meta space）。
 * 运行时常量池：方法区的一部分内容。
 *
 * 直接内存：Direct Memory，不是由JVM进行管理的，一般是由操作系统来进行管理。与Java NIO密切相关的，JVM通过堆上的DirectByteBuffer来操作直接内存。
 *
 * 关于Java对象创建的过程：
 * new关键字创建对象的3个步骤：
 * 1.在堆内存中创建出对象的实例
 * 2.为对象的实例成员变量赋初值
 * 3.将对象的引用返回
 *
 * new对象时堆内存的分配方式有两种模式：
 * 指针碰撞（前提是堆中的空间通过一个指针进行分割，一侧是已经被占用的空间，另一侧是未被占用的空间）
 * 空闲列表（前提是堆内存空间中已被使用与未被使用的空间是交织在一起的，这时，虚拟机就需要通过一个列表来记录
 * 哪些空间是可以使用的，哪些空间是已被使用的，接下来找出可以容纳下新创建对象的且未被使用的空间，在此空间存放该对象。
 * 同时还要修改列表上的记录）
 *
 * 对象在内存中的布局：
 * 1.对象头
 * 2.实例数据（即我们在一个类中所声明的各项信息）
 * 2.对齐填充（可选）
 *
 * 引用访问对象的方式：
 * 1.使用句柄的方式。
 * 2.使用直接指针的方式。
 *
 * jvisualvm打开转储文件、监视
 *
 * @author HuSen
 * create on 2020/4/17 4:36 下午
 */
public class MyTest1 {

    public static void main(String[] args) {
        // -Xms5m -Xmx5m -XX:+HeapDumpOnOutOfMemoryError
        // 堆内存溢出
        List<MyTest1> list = new ArrayList<>();
        for (;;) {
            list.add(new MyTest1());

            System.gc();
        }
    }

}
