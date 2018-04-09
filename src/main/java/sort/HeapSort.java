package main.java.sort;

import java.util.Arrays;

/**
 * Created on 2017/6/15
 * Author: youxingyang.
 */

/**
 * 思想:初始时把要排序的数的序列看作是一棵顺序存储的二叉树，调整它们的存储序，
 * 使之成为一个 堆，这时堆的根节点的数最大。然后将根节点与堆的最后一个节点交换。
 * 然后对前面(n-1)个数重新调整使之成为堆。依此类推，直到只有两个节点的堆，并对 它们作交换，
 * 最后得到有n个节点的有序序列。从算法描述来看，堆排序需要两个过程，一是建立堆，二是堆顶与堆的最后一个元素交换位置。
 * 所以堆排序有两个函数组成。一是建堆的渗透函数，二是反复调用渗透函数实现排序的函数。
 */
public class HeapSort {
    public static void main(String[] args) {
        int[] arr = {6, 5, 4, 3, 2, 1, 0};

        //留最后一个空位放第一个最大值
        for (int i = 0; i < arr.length - 1; i++) {
            buildMaxHeap(arr, arr.length - 1 - i);
            //交换堆顶最大值与最后一个空位
            swap(arr, 0, arr.length - 1 - i);
        }

        System.out.println(Arrays.toString(arr));

    }

    /**
     * 建立一个最大堆
     *
     * @param arr
     * @param lastIndex
     */
    private static void buildMaxHeap(int[] arr, int lastIndex) {
        //从最后一个父节点开始建堆
        for (int i = (lastIndex - 1) / 2; i >= 0; i--) {
            int k = i;
            //子节点存在
            while (2 * k + 1 <= lastIndex) {
                int biggerIndex = 2 * k + 1;
                //如果biggerIndex小于lastIndex,即biggerIndex+1代表的右节点存在
                if (biggerIndex < lastIndex) {
                    //左节点小于右节点
                    if (arr[biggerIndex] < arr[biggerIndex + 1]) {
                        biggerIndex++;
                    }
                }

                //当前节点值小于两者最大值,把最大值调到堆顶
                if (arr[k] < arr[biggerIndex]) {
                    swap(arr, k, biggerIndex);
                    //保证k节点的值大于其左右子节点的值
                    k = biggerIndex;
                } else {
                    break;
                }
            }
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
