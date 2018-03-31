package main.java.thread.allrun;

/**
 * @Author: youxingyang
 * @date: 2016/6/23 13:18
 */
public class Machine extends Thread {

    @Override
    public void run(){
        for (int i = 1; i < 50; i++) {
            System.out.println(currentThread().getName() + ":" + i);
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) {
        Machine m1 = new Machine();
        Machine m2 = new Machine();
        m1.start();
        m2.start();
        //not recommend
        m1.run();
    }
}
