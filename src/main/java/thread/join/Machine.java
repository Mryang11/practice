package main.java.thread.join;

/**
 * @Author: youxingyang
 * @date: 2016/6/23 13:18
 */
public class Machine extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 40; i++) {
            System.out.println(getName() + ":" + i);
        }
    }

    public static void main(String[] args) throws Exception {
        Machine machine = new Machine();
        machine.setName("m1");

        machine.start();
        System.out.println("main:join machine");

        //主线程等待machine线程运行结束
        machine.join();
        System.out.println("main:end");
    }
}
