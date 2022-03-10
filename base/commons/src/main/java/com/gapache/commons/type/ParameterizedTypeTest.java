package com.gapache.commons.type;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author HuSen
 * @since 2020/9/27 3:17 下午
 */
public class ParameterizedTypeTest<T> {
    private static final String LINE = "----------" + System.lineSeparator();
    private List<T> list;
    private Set<T> set;
    private Map<String, T> map;
    private Map.Entry<String, Integer> map2;

    public static void main(String[] args) throws Exception {
        Field list = ParameterizedTypeTest.class.getDeclaredField("list");
        Type listGenericType = list.getGenericType();
        System.out.println("list域类型名：" + listGenericType.getTypeName());
        System.out.println("list域实际的Type类型：" + listGenericType.getClass().getName());
        System.out.println(LINE);

        Field set = ParameterizedTypeTest.class.getDeclaredField("set");
        Type setGenericType = set.getGenericType();
        System.out.println("set域类型名：" + setGenericType.getTypeName());
        System.out.println("set域实际的Type类型：" + setGenericType.getClass().getName());
        System.out.println(LINE);

        Field map = ParameterizedTypeTest.class.getDeclaredField("map");
        Type mapGenericType = map.getGenericType();
        System.out.println("map域类型名：" + mapGenericType.getTypeName());
        System.out.println("map域实际的Type类型：" + mapGenericType.getClass().getName());
        if (mapGenericType instanceof ParameterizedType) {
            ParameterizedType mapParameterizedType = (ParameterizedType) mapGenericType;
            Type[] actualTypeArguments = mapParameterizedType.getActualTypeArguments();
            System.out.println("map域泛型参数类型[0]:" + actualTypeArguments[0]);
            System.out.println("map域泛型参数类型[1]:" + actualTypeArguments[1]);
            System.out.println("map域声明泛型参数的类类型：" + mapParameterizedType.getRawType());
            System.out.println("泛型的拥有者类型：" + mapParameterizedType.getOwnerType());
        }
        System.out.println(LINE);

        Field map2 = ParameterizedTypeTest.class.getDeclaredField("map2");
        Type map2GenericType = map2.getGenericType();
        System.out.println("map2域类型名：" + map2GenericType.getTypeName());
        System.out.println("map2域实际的Type类型：" + map2GenericType.getClass().getName());
        if (map2GenericType instanceof ParameterizedType) {
            ParameterizedType map2ParameterizedType = (ParameterizedType) map2GenericType;
            Type[] types = map2ParameterizedType.getActualTypeArguments();
            System.out.println("map2域泛型参数类型[0]:" + types[0]);
            System.out.println("map2域泛型参数类型[1]:" + types[1]);
            System.out.println("map2域声明泛型参数的类类型：" + map2ParameterizedType.getRawType());
            System.out.println("泛型的拥有者类型：" + map2ParameterizedType.getOwnerType());
        }
    }
}
