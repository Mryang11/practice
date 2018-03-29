package main.java.thread.runimp;

/**
 * @Author: youxingyang
 * @date: 2016/6/23 13:20
 */
public class Machine implements Runnable {
    private int a = 0;
    @Override
    public void run() {
        for (a = 0; a < 23; a++) {
            System.out.println(Thread.currentThread().getName() + ":" + a);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) {
       /* Machine m1 = new Machine();
        Thread t1 = new Thread(m1);
        //t1,t2共享同一个Machine对象m1,在执行run()方法时共享同一个实例变量a
        Thread t2 = new Thread(m1);
        t1.start();
        t2.start();*/

        Machine m1 = new Machine();
        Machine m2 = new Machine();
        Thread t1 = new Thread(m1);
        //t1,t2各自共享Machine对象m1,m2,在执行run()方法时各自共享实例变量a
        Thread t2 = new Thread(m2);
        t1.start();
        t2.start();
    }


}
