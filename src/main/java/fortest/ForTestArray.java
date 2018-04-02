package main.java.fortest;

/**
 * @Author youxingyang
 * @date 2018/4/118:26
 */
public class ForTestArray {

    public static int[] create(int num) {
        int[] arr = new int[num];
        for (int i = 0; i < num; i++) {
            arr[i] = i;
        }
        return arr;
    }

    public static void print(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
    }

    public static void print1(int[] arr) {
        for (Integer s : arr) {
            System.out.println(s);
        }
    }

    public static void main(String[] args) {
        int[] arr = create(60000);
        String type = "arr";

        //传统for
        long a = System.currentTimeMillis();
        print(arr);
        long b = System.currentTimeMillis();

        //foreach
        long c = System.currentTimeMillis();
        print1(arr);
        long d = System.currentTimeMillis();

        System.out.println(type + " -> 传统for循环耗时：" + (b - a) + "ms");
        System.out.println(type + " -> foreach循环耗时：" + (d - c) + "ms");
    }

}
