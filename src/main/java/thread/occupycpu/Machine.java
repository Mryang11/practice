package main.java.thread.occupycpu;

/**
 * @Author: youxingyang
 * @date: 2016/6/24 13:19
 */
public class Machine extends Thread {
    private static StringBuffer log = new StringBuffer();
    private static int count = 0;

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            log.append(currentThread().getName()).append(":").append(i).append(" ");
            if (++count % 10 == 0) {
                log.append("\n");
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Machine machine1 = new Machine();
        Machine machine2 = new Machine();
        machine1.setName("m1");
        machine2.setName("m2");

        //require main thread
        Thread main = currentThread();
        System.out.println("default priority of main:" + main.getPriority());

        System.out.println("default priority of machine1:" + machine1.getPriority());
        System.out.println("default priority of machine2:" + machine2.getPriority());

        machine1.setPriority(MIN_PRIORITY);
        machine2.setPriority(MAX_PRIORITY);

        machine1.start();
        machine2.start();
        // main thread sleep 2000ms
        Thread.sleep(2000);
        System.out.println(log);
    }

}
