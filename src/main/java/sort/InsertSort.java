package sort;

/**
 * Created on 2017/6/12
 * Author: youxingyang.
 */
public class InsertSort {
    public static void main(String[] args) {
        int[] arr = {4, 1, 8, 0, 2, 8, 9, 6, 14, 18, 3};

        sort(arr);

        for (Integer a : arr) {
            System.out.print(a + " ");
        }
    }

    /**
     * 标准插入排序,可借鉴斗地主理牌顺序的思路
     * 1. 假定第一个元素有序,从第二个元素开始循环比较相邻元素
     * 2. 如果后一个比前一个小,则从当前下标-1出开始逐个查找插入位置
     *
     * @param arr
     */
    private static void sort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < arr[i - 1]) {
                //给待插入元素tmp赋值,同时给元素后移腾出位置
                int tmp = arr[i];
                int j;
                //从后向前逐个找到符合条件的位置插入
                for (j = i - 1; j >= 0 && tmp < arr[j]; j--) {
                    //如果tmp比arr[j]小,元素后移
                    arr[j + 1] = arr[j];
                }
                //跳出for循环,说明tmp至少比arr[j]大(可能相等)或者j为0,应该在下标j+1处插入此元素
                arr[j + 1] = tmp;
            }
        }
    }
}
