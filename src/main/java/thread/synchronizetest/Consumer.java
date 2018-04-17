package thread.synchronizetest;


/**
 * @Author: youxingyang
 * @date: 2016/6/24 13:20
 */
public class Consumer extends Thread {
    private Stack theStack;

    public Consumer(Stack s, String name) {
        super(name);
        theStack = s;
        //启动自身消费线程
        start();

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
