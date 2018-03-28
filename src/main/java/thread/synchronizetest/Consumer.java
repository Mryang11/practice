package thread.synchronizetest;

/**
 * Created by youxingyang on 2016/6/24.
 */
public class Consumer extends Thread {
    private Stack theStack;
    public Consumer(Stack s, String name) {
        super(name);
        theStack = s;
        start();    //启动自身消费线程

    }
    @Override
    public void run() {
        String goods;
        for (int i = 0; i < 200; i++) {
            goods = theStack.pop();
            System.out.println(getName() + ": pop " + goods + " from " + theStack.getName());
            yield();
        }
    }
}
