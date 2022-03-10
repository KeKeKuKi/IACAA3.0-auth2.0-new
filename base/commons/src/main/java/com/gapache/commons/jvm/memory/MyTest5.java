package com.gapache.commons.jvm.memory;

/**
 * @author HuSen
 * create on 2020/4/20 3:52 下午
 */
public class MyTest5 {

    public static void main(String[] args) throws InterruptedException {
        // jps
        // jmap -clstats pid
        // jmap -heap pid
        // jstat -gc pid

        // jcmd（从JDK 1.7开始增加的命令）
        // jcmd pid help 列出当前的java进程可以执行的操作
        // jcmd pid help operation 查看具体命令的选项
        // jcmd pid VM.flags 查看VM启动参数
        // jcmd pid PerfCounter.print 查看JVM性能相关的一些参数
        // jcmd pid VM.uptime 查看JVM的运行时长
        // jcmd pid GC.class_histogram 查看系统中类的统计信息
        // jcmd pid Thread.print 查看线程的堆栈信息
        // jcmd pid GC.heap_dump filename 导出Heap dump文件，导出的文件可以通过jvisualvm查看
        // jcmd pid VM.system_properties 查看JVM的属性信息
        // jcmd pid VM.version 查看目标JVM进程的版本信息
        // jcmd pid VM.command_line 查看JVM启动的命令行参数信息

        // jstack 可以查看或是导出Java应用程序中线程的堆栈信息

        // jmc Java Mission Control
        // jfr Java Flight Recorder

        // jhat

        // System.gc()
        while (true) {
            System.out.println("hello world");
            Thread.sleep(100);
        }
    }
}
