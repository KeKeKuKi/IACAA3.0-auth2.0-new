package com.gapache.commons.jvm.bytecode;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;

/**
 * this:
 * 编译后每一个实例方法的第一个参数都是this，方法参数的数量总是会比源代码中方法参数的数量多一个。
 * 所以，在实例方法的局部变量表中，至少会有一个指向当前对象的局部变量。
 *
 * Java字节码对于异常的处理方式:
 * 1统一采用异常表的方式来对异常进行处理。
 * 2在jdk1.4.2之前的版本中，并是不使用异常表的方式来对异常进行处理的，而是采用特定的指令方式。
 * 3当异常处理存在finally语句块时，现代化的JVM采取的处理方式是将finally语句块的字节码拼接到每一个catch块后面。
 *
 * @author HuSen
 * create on 2020/4/2 9:56 上午
 */
public class MyTest3 {

    public void test() throws IOException, FileNotFoundException, NullPointerException {
        try {
            InputStream is = new FileInputStream("test.txt");
            ServerSocket serverSocket = new ServerSocket(9999);
            serverSocket.accept();
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
        } finally {
            System.out.println("finally");
        }
    }
}
