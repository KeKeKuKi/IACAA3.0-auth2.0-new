package com.gapache.commons.type;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.List;

/**
 * @author HuSen
 * @since 2020/9/27 4:29 下午
 */
public class WildcardTypeTest {

    private List<String> a;

    public static void main(String[] args) throws Exception {
        Field a = WildcardTypeTest.class.getDeclaredField("a");
        Type aGenericType = a.getGenericType();
        Class<?> aType = a.getType();

        System.out.println("TypeVariableTest a 域 type名：" + aGenericType.getTypeName());
        System.out.println("TypeVariableTest a 域 type的class名：" + aGenericType.getClass().getCanonicalName());
        System.out.println("TypeVariableTest a 域 Class名: " + aType.getCanonicalName());

        if (aGenericType instanceof ParameterizedType) {
            ParameterizedType aParameterizedType = (ParameterizedType) aGenericType;
            System.out.println("a 域Type 是 ParameterizedType ！");
            Type[] typesString = aParameterizedType.getActualTypeArguments();
            System.out.println("a 域泛型参数[0]的type名：" + typesString[0].getTypeName());
            System.out.println("a 域泛型参数[0]的type类型名：" + typesString[0].getClass().getCanonicalName());
            System.out.println(typesString[0].getClass());
            WildcardType type0 = (WildcardType) typesString[0];
            Type[] type0UpperBounds = type0.getUpperBounds();
            Type[] type0LowerBounds = type0.getLowerBounds();

            System.out.println("泛型参数[0]的上限type:" + type0UpperBounds[0]);
            System.out.println("泛型参数的下限type:" + Arrays.toString(type0LowerBounds));
        }
    }
}
