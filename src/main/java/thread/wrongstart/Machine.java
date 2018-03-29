package main.java.thread.wrongstart;

/**
 * @Author: youxingyang
 * @date: 2016/6/23 13:21
 */
public class Machine extends Thread {
    //共享实例变量b
    private int b = 0;
    @Override
    public void start(){
        run();
    }
    @Override
    public void run() {
        for (b = 0; b < 45; b++) {
            System.out.println(currentThread().getName() + ":" + b);
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void main(String[] args) {
        Machine m1 = new Machine();
        //覆盖start()方法之后，run方法由主线程执行,所以不应该随便覆盖start()方法
        m1.start();
    }
}
