package thread.synchronizetest;

/**
 * Created by youxingyang on 2016/6/24.
 */
public class SyncTest {
    public static void main(String args[]) {
        Stack stack = new Stack("stack1");
        Producer producer1 = new Producer(stack,"producer1");
        Producer producer2 = new Producer(stack,"producer2");
        Consumer consumer1 = new Consumer(stack,"consumer1");
    }
}
