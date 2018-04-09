package main.java.sort;

import java.util.Arrays;

/**
 * Created on 2017/6/12
 * Author: youxingyang.
 */
public class SelectSort {
    public static void main(String[] args) {
        int[] arr = {6, 5, 4, 3, 2, 1};

        sort(arr);

        System.out.println(Arrays.toString(arr));
    }

    /**
     * 标准选择排序-每次选取一个最值
     *
     * @param arr
     */
    private static void sort(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            int index = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[index]) {
                    index = j;
                }
            }
            swap(arr, i, index);
        }
    }

    /**
     * 不使用第三个变量交换两个数
     *
     * @param arr
     * @param i
     * @param j
     */
    private static void swap(int[] arr, int i, int j) {
        if (i != j) {
            //arr[i]被存为和,arr[j]没有变
            arr[i] = arr[i] + arr[j];
            //arr[i](和)减去arr[j](没有变)等于arr[i],赋值给arr[j]
            arr[j] = arr[i] - arr[j];
            //此时的arr[j]为arr[i]的值,和arr[i]没有变,arr[i]减去arr[j]等于原本arr[i]的值,赋值给arr[i]
            arr[i] = arr[i] - arr[j];
        }
    }
}
