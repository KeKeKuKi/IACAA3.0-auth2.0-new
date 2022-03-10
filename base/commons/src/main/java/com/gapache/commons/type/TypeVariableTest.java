package com.gapache.commons.type;

import java.lang.reflect.Field;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;

/**
 * @author HuSen
 * @since 2020/9/27 4:12 下午
 */
public class TypeVariableTest<T> {
    private static final String LINE = "-------" + System.lineSeparator();
    private T a;
    private List<String> b;

    public static void main(String[] args) throws Exception {
        Field a = TypeVariableTest.class.getDeclaredField("a");
        Type aGenericType = a.getGenericType();
        Class<?> aType = a.getType();
        System.out.println("TypeVariableTest<T> a 域 type名：" + aGenericType.getTypeName());
        System.out.println("TypeVariableTest<T> a 域 type的class名：" + aGenericType.getClass().getCanonicalName());
        System.out.println("TypeVariableTest<T> a 域 Class名: " + aType.getCanonicalName());

        if (aGenericType instanceof TypeVariable) {
            TypeVariable aTypeVariable = (TypeVariable) aGenericType;
            String name = aTypeVariable.getName();
            Type[] bounds = aTypeVariable.getBounds();
            System.out.println("a 域类型是 TypeVariable类型！");
            System.out.println("a 域 type'name: " + name);
            for (int i = 0; i < bounds.length; i++) {
                System.out.println("a 域 type'bounds[" + i + "] Type'name: " + bounds[i].getTypeName());
            }
            GenericDeclaration genericDeclaration = aTypeVariable.getGenericDeclaration();
            System.out.println("声明a 域变量的实体：" + genericDeclaration);
        }
        System.out.println(LINE);

        Field b = TypeVariableTest.class.getDeclaredField("b");
        Type bGenericType = b.getGenericType();
        Class<?> bType = b.getType();
        System.out.println("TypeVariableTest<T> b 域 type名：" + bGenericType.getTypeName());
        System.out.println("TypeVariableTest<T> b 域 type的Class名：" + bGenericType.getClass().getCanonicalName());
        System.out.println("TypeVariableTest<T> b 域 Class名：" + bType.getCanonicalName());
    }
}
