package main.java.sort;

/**
 * Created on 2017/6/12
 * Author: youxingyang.
 */
public class ShellSort {
    public static void main(String[] args) {
        int[] arr = {4, 1, 8, 0, 2, 8, 9, 6, 14, 18, 3};

        sort(arr);

        for (Integer a : arr) {
            System.out.print(a + " ");
        }
    }

    private static void sort(int[] arr) {
        int k = arr.length / 2;
        while (k >= 1) {
            shellSort(arr, k);
            k /= 2;
        }
    }

    /**
     * 希尔排序:  插入排序 = 希尔排序的增量为1
     *
     * @param arr
     * @param k
     */
    private static void shellSort(int[] arr, int k) {
        for (int i = k; i < arr.length; i++) {
            if (arr[i] < arr[i - k]) {
                int j;
                int tmp = arr[i];
                for (j = i - k; j >= 0 && tmp < arr[j]; j = j - k) {
                    arr[j + k] = arr[j];
                }
                arr[j + k] = tmp;
            }
        }
    }
}
