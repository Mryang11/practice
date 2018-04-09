package main.java.exception;

/**
 * @Author: youxingyang
 * @date: 2017/6/23 13:20
 */
public class TestException {

    /**
     * 内层处理完异常外层就不再处理了
     *
     * @return
     */
    public static void test() {
        try {
            System.out.println("外层try");
            try {
                int xy = 1 / 0;
                System.out.println("内层try");
            } catch (Exception in) {
                System.out.println("内层catch, 除数不为0");
                in.printStackTrace();
            } finally {
                System.out.println("内层finally");
            }
        } catch (Exception out) {
            System.out.println("外层catch");
        } finally {
            System.out.println("外层finally");
        }
    }

    public static void main(String[] args) {
        test();
    }
}
