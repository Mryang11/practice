package main.java.thread.waitio;

/**
 * @Author: youxingyang
 * @date: 2016/6/24 13:21
 */

/**
 * 阻塞状态有3种:
 * 1.位于对象等待池中：当线程处于运行状态时，如果执行了某个对象的wait()方法,JVM就把线程放到这个对象的等待池中
 * 2.位于对象锁池中：当线程处于运行状态时，试图获得某个对象的同步锁时,如果该对象的同步锁已经被其他线程占用,JVM就把线程放到这个对象的等待池中
 * 3.当前线程调用sleep(),调用join(),发出IO请求
 */
public class Machine extends Thread {
    private static StringBuffer log = new StringBuffer();
    private static int a = 0;

    @Override
    public void run() {
        for (a = 0; a < 20; a++) {
            System.out.println(currentThread().getName() + ":" + a);
        }
    }

    public static void main(String[] args) throws Exception {
        Machine m1 = new Machine();
        m1.start();
        System.out.println("please input your number:");
        int data = System.in.read();
        m1.run();
    }
}
