package thread.withex;

/**
 * @Author: youxingyang
 * @date: 2016/6/24 13:21
 */
public class Machine extends Thread {
    @Override
    public void run() {
        for (int a = 0; a < 30; a++) {
            System.out.println(currentThread().getName() + ":" + a);
            if (a == 1 && "m1".equals(currentThread().getName())) {
                throw new RuntimeException("wrong from Machine");
            }
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Machine machine = new Machine();
        machine.setName("m1");

        machine.start();
        machine.run();
        //线程处于死亡状态或者新建状态返回false
        System.out.println("Is machine alive?" + machine.isAlive());
        System.out.println(currentThread().getName() + ": end");
    }
}
