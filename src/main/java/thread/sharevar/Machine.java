package thread.sharevar;

/**
 * @Author: youxingyang
 * @date: 2016/6/23 13:20
 */
public class Machine extends Thread {
    private int a = 0;

    @Override
    public void run() {
        for (a = 0; a < 50; a++) {
            System.out.println(currentThread().getName() + ":" + a);
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) {
       /* Machine m1 = new Machine();
        m1.start(); //一个线程只能被启动一次
        m1.run();*/ //共享同一实例变量a

        /*Machine m1 = new Machine();
        Machine m2 = new Machine();
        m1.start();
        m2.start();*/   //m1线程和m2线程同时维护各自的实例变量a

    }
}
