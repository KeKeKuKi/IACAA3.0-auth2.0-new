package com.gapache.commons.type;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author HuSen
 * @since 2020/9/27 3:40 下午
 */
public class GenericArrayTypeTest<T> {
    private static final String LINE = "-----------------------------------------------------------" + System.lineSeparator();
    private T[] t;
    private List<T>[] lists;

    public static void main(String[] args) throws Exception {
        Field lists = GenericArrayTypeTest.class.getDeclaredField("lists");
        Type listsGenericType = lists.getGenericType();
        System.out.println("lists域Type类型：" + listsGenericType.getClass().getName());
        GenericArrayType listsGenericArrayType = (GenericArrayType) listsGenericType;
        System.out.println("lists域元素类型:" + listsGenericArrayType.getGenericComponentType());
        System.out.println("lists域元素Type类型:" + listsGenericArrayType.getGenericComponentType().getClass().getName());
        System.out.println(LINE);

        Field t = GenericArrayTypeTest.class.getDeclaredField("t");
        Type tGenericType = t.getGenericType();
        System.out.println("t域Type类型：" + tGenericType.getClass().getName());
        GenericArrayType tGenericArrayType = (GenericArrayType) tGenericType;
        System.out.println("t域元素类型:" + tGenericArrayType.getGenericComponentType());
        System.out.println("t域元素Type类型:" + tGenericArrayType.getGenericComponentType().getClass().getName());
    }
}
