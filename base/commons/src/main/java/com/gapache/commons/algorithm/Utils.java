package com.gapache.commons.algorithm;

/**
 * @author HuSen
 * @since 2020/5/27 5:23 下午
 */
public class Utils {

    public static void print(int[][] array) {
        for (int[] ints : array) {
            for (int anInt : ints) {
                System.out.printf("%d\t", anInt);
            }
            System.out.println();
        }
        System.out.println();
    }
}
