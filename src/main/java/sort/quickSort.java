package sort;

/**
 * Created on 2017/6/12
 * Author: youxingyang.
 */
public class quickSort {

    public static void main(String[] args) {
        int[] arr = {4, 1, 8, 0, 2, 8, 9, 6, 14, 18, 3};

        sort(arr, 0, arr.length - 1);

        for (Integer a : arr) {
            System.out.print(a + " ");
        }
    }

    private static void sort(int[] arr, int low, int high) {
        if (low < high && arr.length > 0) {
            int index = partition(arr, low, high);
            sort(arr, low, index - 1);
            sort(arr, index + 1, high);
        }
    }

    /**
     * 按照key基准给数组分区
     *
     * @param arr  数组
     * @param low  数组初始下标
     * @param high 数组最大下标
     * @return key基准的下标
     */
    private static int partition(int[] arr, int low, int high) {

        int key = arr[low];
        while (low < high) {
            while (arr[high] >= key && low < high) {
                high--;
            }
            arr[low] = arr[high];

            while (arr[low] <= key && low < high) {
                low++;
            }
            arr[high] = arr[low];
        }
        arr[high] = key;
        return high;
    }
}
