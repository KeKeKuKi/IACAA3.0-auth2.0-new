package com.gapache.commons.algorithm.sort;

import java.util.Arrays;

/**
 * @author HuSen
 * @since 2020/7/17 11:12 上午
 */
public class Select {

    public static void sort(int[] arr) {
        int length = arr.length;
        for (int i = 0; i < length; i++) {
            int minIndex = i;
            for (int j = i + 1; j < length; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                int temp = arr[i];
                arr[i] = arr[minIndex];
                arr[minIndex] = temp;
            }
        }
    }

    public static void main(String[] args) {
        int[] test = new int[]{9, 8, 7, 6, 5, 4, 3, 2, 1};
        sort(test);
        System.out.println(Arrays.toString(test));
    }
}
