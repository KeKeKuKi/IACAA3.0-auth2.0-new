package com.gapache.commons.algorithm.structure;

import com.gapache.commons.algorithm.Utils;

/**
 * 稀疏数组
 *
 * @author HuSen
 * @since 2020/5/27 5:12 下午
 */
public class SparseArray {
    public static final String C = "s";

    public static void main(String[] args) {
        int[][] ints = new int[10][10];
        ints[1][1] = 2;
        ints[8][8] = 8;
        Utils.print(ints);

        int[][] sparse = toSparse(ints);
        Utils.print(sparse);

        int[][] normal = toNormal(sparse);
        Utils.print(normal);
    }

    private static int[][] toNormal(int[][] source) {
        int totalRow = source[0][0];
        int totalCol = source[0][1];
        int total = source[0][2];
        int[][] result = new int[totalRow][totalCol];
        for (int i = 1; i <= total; i++) {
            int[] row = source[i];
            result[row[0]][row[1]] = row[2];
        }
        return result;
    }

    private static int[][] toSparse(int[][] source) {
        // 先获取原始数组有多少行多少列以及不为0的元素有多少个
        int totalRow = source.length;
        int totalCol = source[0].length;
        int total = 0;
        for (int[] ints : source) {
            for (int anInt : ints) {
                if (anInt != 0) {
                    total++;
                }
            }
        }
        // 初始化稀疏数组，并且将第一行的第一列设置为总行数，第一行第二列设置为总列数，第一行第3列设置为不为0的元素个数
        int[][] sparse = new int[1 + total][3];
        sparse[0][0] = totalRow;
        sparse[0][1] = totalCol;
        sparse[0][2] = total;
        // 然后把后面的每行的第一列设置为不为0的元素对应的行，第二列设置为对应的列，第三列设置为对应的值
        int index = 1;
        for (int i = 0; i < source.length; i++) {
            for (int j = 0; j < source[i].length; j++) {
                int value = source[i][j];
                if (value != 0) {
                    sparse[index][0] = i;
                    sparse[index][1] = j;
                    sparse[index][2] = value;
                    index++;
                }
            }
        }
        return sparse;
    }
}
