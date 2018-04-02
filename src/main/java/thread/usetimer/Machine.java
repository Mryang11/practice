package main.java.thread.usetimer;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @Author: youxingyang
 * @date: 2016/6/24 13:21
 */
public class Machine extends Thread {
    private int a;
    private static int count;

    @Override
    public void start() {
        super.start();
        //把与Timer关联的线程设为后台线程
        Timer timer = new Timer(true);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                while (true) {
                    reset();
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };

        //10ms以后开始执行task任务,以后每个500ms执行一次
        timer.schedule(task, 10, 500);
    }
    public void reset(){a = 0;}
    @Override
    public void run() {
        while (true) {
            System.out.println(getName() + ":" + a++);
            if (count++ == 100) {
                break;
            }

            yield();
        }
    }

    public static void main(String[] args)throws Exception {
        Machine machine = new Machine();
        machine.start();
    }
}
