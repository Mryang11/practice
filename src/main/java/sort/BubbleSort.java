package sort;

/**
 * Created on 2017/6/12
 * Author: youxingyang.
 */
public class BubbleSort {
    public static void main(String[] args) {
        int[] arr = {4, 1, 8, 0, 2, 8, 9, 6, 14, 18, 3};

        //sort(arr);
        sort1(arr);

        for (Integer a : arr) {
            System.out.print(a + " ");
        }
    }

    /**
     * 标准冒泡排序
     * 二层for循环，每次选出一个最值
     * 复杂度：O(n*n)
     *
     * @param arr
     */
    private static void sort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    swap(arr, j, j + 1);
                }
            }
        }
    }

    /**
     * 改进版冒泡-用pos记住每次最后一次交换的下标,下次遍历到pos-1下标即可
     *
     * @param arr
     */
    private static void sort1(int[] arr) {
        int i = arr.length - 1;
        while (i > 0) {
            int pos = 0;
            for (int j = 0; j < i; j++) {
                if (arr[j] > arr[j + 1]) {
                    swap(arr, j, j + 1);
                    pos = j;
                }
            }
            i = pos;
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
