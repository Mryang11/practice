package utils;

/**
 * @author: youxingyang
 * @date: 2018/9/4 17:11
 */
public class Partion {
    public static void main(String[] args) {
        int startIndex = 0;
        int size = 22859;
        int endIndex;
        int partion = 1;
        if (size > 200) {
            partion = 40;
        }
        int pos = size / partion;
        System.out.println(pos);
        for (int q = 0; q < partion; q++) {
            startIndex = q * pos;
            endIndex = (q + 1) * pos;
            if (q == partion - 1) {
                endIndex += size % partion;
            }
            System.out.println(startIndex);
            System.out.println(endIndex);
            System.out.println("=============");
        }
    }
}
