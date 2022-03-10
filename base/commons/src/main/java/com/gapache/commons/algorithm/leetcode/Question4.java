package com.gapache.commons.algorithm.leetcode;

/**
 * @author HuSen
 * @since 2020/7/20 2:25 下午
 */
public class Question4 {

    public static double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int[] newNums = new int[nums1.length + nums2.length];
        int nums1Index = 0;
        int nums2Index = 0;
        for (int i = 0; i < newNums.length; i++) {
            if (nums1Index == nums1.length) {
                newNums[i] = nums2[nums2Index++];
                continue;
            }
            if (nums2Index == nums2.length) {
                newNums[i] = nums1[nums1Index++];
                continue;
            }
            if (nums1[nums1Index] <= nums2[nums2Index]) {
                newNums[i] = nums1[nums1Index++];
            } else {
                newNums[i] = nums2[nums2Index++];
            }
        }
        int middle = newNums.length / 2;
        if (newNums.length % 2 == 0) {
            return (newNums[middle - 1] + newNums[middle]) / 2D;
        } else {
            return newNums[middle];
        }
    }

    public static void main(String[] args) {
        System.out.println(findMedianSortedArrays(new int[] {1, 3}, new int[] {2}));
    }
}
