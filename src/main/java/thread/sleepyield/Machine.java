package thread.sleepyield;

/**
 * Created by youxingyang on 2016/6/24.
 */

/**
 * sleep VS yield
 * 1.sleep 会给其他线程机会，不考虑优先级，因此会给优先级低的线程一个机会;yield只会给相同或者更高的优先级线程一个机会
 * 2.执行sleep,线程转到阻塞状态;执行yield,线程转到就绪状态.
 * 3.sleep方法声明抛出InterruptException异常,而yield没有声明任何异常
 * 4.sleep方法比yield方法可移植性较好.yield方法唯一用途是在测试期间认为地提高程序的并发性能.
 */
public class Machine extends Thread {
    private static StringBuffer log = new StringBuffer();
    private static int count = 0;
    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            log.append(currentThread().getName() + ":" + i + " ");
            if (++count % 10 == 0) {
                log.append("\n");
            }

            //1.sleep
            /*try {
                sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }*/
            //2.yield
            yield();
        }
    }

    public static void main(String args[]) throws Exception {
        Machine machine1 = new Machine();
        Machine machine2 = new Machine();
        machine1.setName("m1");
        machine2.setName("m2");

        machine1.start();
        machine2.start();

        while (machine1.isAlive() || machine2.isAlive()) {
            Thread.sleep(500);
        }

        System.out.println(log);
    }
}
