package com.gapache.commons.algorithm.sort;

import java.util.Arrays;

/**
 * @author HuSen
 * @since 2020/7/17 11:03 上午
 */
public class Bubble {

    public static void sort(int[] arr) {
        int length = arr.length;
        for (int i = 0; i < length; i++) {
            for (int j = 1; j < length - i; j++) {
                if (arr[j - 1] > arr[j]) {
                    int temp = arr[j];
                    arr[j] = arr[j - 1];
                    arr[j - 1] = temp;
                }
            }
        }
    }

    public static void main(String[] args) {
        int[] test = new int[]{9, 8, 7, 6, 5, 4, 3, 2, 1};
        sort(test);
        System.out.println(Arrays.toString(test));
    }
}
