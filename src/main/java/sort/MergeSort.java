package sort;

import java.util.Arrays;

/**
 * Created on 2017/6/15
 * Author: youxingyang.
 */
public class MergeSort {
    public static void main(String[] args) {
        int[] arr = {6, 5, 4, 3, 2, 1, 0};

        sort(arr, 0, arr.length - 1);

        System.out.println(Arrays.toString(arr));
    }

    private static void sort(int[] arr, int low, int high) {
        int mid = (low + high) / 2;
        if (low < high) {
            sort(arr, low, mid);
            sort(arr, mid + 1, high);
            merge(arr, low, mid, high);
        }
    }

    /**
     * 归并
     * 将两个（或两个以上）有序表合并成一个新的有序表 即把待排序序列分为若干个子序列，每个子序列是有序的。然后再把有序子序列合并为整体有序序列
     *
     * @param arr  待排序数组
     * @param low  开始位置
     * @param mid  二分位置
     * @param high 结束位置
     */
    private static void merge(int[] arr, int low, int mid, int high) {
        int[] tmp = new int[high - low + 1];
        int i = low;
        int j = mid + 1;
        int k = 0;

        //把较小的值移入新数组tmp中
        while (i <= mid && j <= high) {
            if (arr[i] < arr[j]) {
                tmp[k++] = arr[i++];
            } else {
                tmp[k++] = arr[j++];
            }
        }

        //左边剩余的数移入
        while (i <= mid) {
            tmp[k++] = arr[i++];
        }

        //右边剩余的数移入
        while (j <= high) {
            tmp[k++] = arr[j++];
        }

        //用tmp数组的值覆盖原数组
        for (int l = 0; l < tmp.length; l++) {
            arr[l + low] = tmp[l];
        }
    }

}
